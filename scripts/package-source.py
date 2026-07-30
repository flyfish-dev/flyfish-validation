#!/usr/bin/env python3
"""生成可复现的完整源码 ZIP，并同时输出 SHA-256 校验文件。"""
from __future__ import print_function

import argparse
import hashlib
import os
from pathlib import Path
import stat
import sys
import xml.etree.ElementTree as ET
import zipfile

EXCLUDED_PARTS = {
    ".git", ".idea", ".vscode", "target", "__pycache__"
}
EXCLUDED_SUFFIXES = {".class", ".pyc", ".pyo", ".zip"}
FIXED_TIME = (2026, 7, 29, 0, 0, 0)


def project_version(root):
    tree = ET.parse(str(root / "pom.xml"))
    namespace = {"m": "http://maven.apache.org/POM/4.0.0"}
    version = tree.getroot().find("m:version", namespace)
    if version is None or not version.text:
        raise ValueError("根 pom.xml 未声明工程版本")
    return version.text.strip()


def included(path, root, output):
    relative = path.relative_to(root)
    if any(part in EXCLUDED_PARTS for part in relative.parts):
        return False
    if path.suffix.lower() in EXCLUDED_SUFFIXES:
        return False
    if path.name in {".DS_Store"}:
        return False
    try:
        if path.resolve() == output.resolve():
            return False
    except FileNotFoundError:
        pass
    return path.is_file() and not path.is_symlink()


def zip_info(archive_name, mode):
    info = zipfile.ZipInfo(archive_name, FIXED_TIME)
    info.compress_type = zipfile.ZIP_DEFLATED
    info.create_system = 3
    info.external_attr = (stat.S_IFREG | mode) << 16
    return info


def sha256(path):
    digest = hashlib.sha256()
    with path.open("rb") as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def main():
    root = Path(__file__).resolve().parents[1]
    version = project_version(root)
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "output", nargs="?",
        default=str(root.parent / ("flyfish-validation-%s-source.zip" % version)),
        help="输出 ZIP 路径")
    args = parser.parse_args()
    output = Path(args.output).resolve()
    output.parent.mkdir(parents=True, exist_ok=True)
    if output.exists():
        output.unlink()

    prefix = "flyfish-validation-%s" % version
    files = [
        path for path in root.rglob("*")
        if included(path, root, output)
    ]
    files.sort(key=lambda item: item.relative_to(root).as_posix())

    with zipfile.ZipFile(
            str(output), "w", compression=zipfile.ZIP_DEFLATED,
            compresslevel=9, allowZip64=True) as archive:
        for path in files:
            relative = path.relative_to(root).as_posix()
            mode = 0o755 if os.access(str(path), os.X_OK) else 0o644
            info = zip_info(prefix + "/" + relative, mode)
            archive.writestr(info, path.read_bytes())

    digest = sha256(output)
    checksum = output.with_suffix(output.suffix + ".sha256")
    checksum.write_text(
        "%s  %s\n" % (digest, output.name), encoding="utf-8")
    print("源码 ZIP: %s" % output)
    print("文件数量: %d" % len(files))
    print("SHA-256: %s" % digest)
    print("校验文件: %s" % checksum)


if __name__ == "__main__":
    try:
        main()
    except Exception as exception:
        print("打包失败: %s" % exception, file=sys.stderr)
        sys.exit(1)
