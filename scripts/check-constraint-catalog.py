#!/usr/bin/env python3
"""校验每个约束的 Validator、消息 key 与三语言资源是否完整。"""
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[1]
errors = []
counts = []
for module in ("flyfish-validation-javax", "flyfish-validation-jakarta"):
    base = ROOT / module
    constraint_root = (base / "src/main/java/dev/flyfish/validation"
                       / "constraints")
    message_keys = []
    for source in constraint_root.rglob("*.java"):
        if source.name == "package-info.java":
            continue
        text = source.read_text(encoding="utf-8")
        annotation = re.search(r"public\s+@interface\s+(\w+)", text)
        if not annotation:
            errors.append("未识别约束注解: %s" % source.relative_to(ROOT))
            continue
        name = annotation.group(1)
        message = re.search(
            r'String\s+message\(\)\s+default\s+"\{([^}]+)\}"', text)
        if not message:
            errors.append("缺少消息 key: %s" % name)
        else:
            message_keys.append(message.group(1))
        validator = re.search(
            r"@Constraint\(validatedBy\s*=\s*(\w+)\.class\)", text)
        if not validator:
            errors.append("缺少 validatedBy: %s" % name)
            continue
        candidates = list((base / "src/main/java/dev/flyfish/validation"
                           / "validator").rglob(validator.group(1) + ".java"))
        if len(candidates) != 1:
            errors.append("Validator 缺失或重名: %s -> %s" %
                          (name, validator.group(1)))

    for resource_name in ("ValidationMessages.properties",
                          "ValidationMessages_zh_CN.properties",
                          "ValidationMessages_en.properties"):
        resource = base / "src/main/resources" / resource_name
        actual = set()
        for line in resource.read_text(encoding="utf-8").splitlines():
            stripped = line.strip()
            if stripped and not stripped.startswith("#") and "=" in line:
                actual.add(line.split("=", 1)[0].strip())
        expected = set(message_keys)
        for key in sorted(expected - actual):
            errors.append("%s 缺少消息: %s" % (resource_name, key))
        for key in sorted(actual - expected):
            errors.append("%s 存在孤立消息: %s" % (resource_name, key))
    if len(message_keys) != len(set(message_keys)):
        errors.append("%s 存在重复消息 key" % module)
    counts.append(len(message_keys))

if errors:
    print("约束目录完整性检查失败：", file=sys.stderr)
    for error in errors:
        print(" - " + error, file=sys.stderr)
    sys.exit(1)

print("约束目录完整性检查通过：javax=%d，jakarta=%d，Validator 与三语言消息完整。"
      % tuple(counts))
