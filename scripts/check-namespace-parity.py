#!/usr/bin/env python3
"""核对 javax 与 jakarta 两套公开 API、验证器和消息资源是否保持完全对称。"""
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
LEFT = ROOT / "flyfish-validation-javax" / "src" / "main"
RIGHT = ROOT / "flyfish-validation-jakarta" / "src" / "main"


def files(base: Path):
    return {
        path.relative_to(base).as_posix(): path
        for path in base.rglob("*")
        if path.is_file()
    }


def normalize(text: str) -> str:
    return (text
            .replace("javax.validation", "__BEAN_VALIDATION__")
            .replace("jakarta.validation", "__BEAN_VALIDATION__"))


left = files(LEFT)
right = files(RIGHT)
errors = []
if set(left) != set(right):
    only_left = sorted(set(left) - set(right))
    only_right = sorted(set(right) - set(left))
    if only_left:
        errors.append("仅 javax 存在: " + ", ".join(only_left))
    if only_right:
        errors.append("仅 jakarta 存在: " + ", ".join(only_right))

for relative in sorted(set(left) & set(right)):
    left_text = left[relative].read_text(encoding="utf-8")
    right_text = right[relative].read_text(encoding="utf-8")
    if normalize(left_text) != normalize(right_text):
        errors.append("内容不对称: " + relative)

annotation_count = 0
for relative, path in left.items():
    if relative.endswith(".java") and "public @interface " in path.read_text(encoding="utf-8"):
        annotation_count += 1

if errors:
    print("命名空间对称性检查失败：", file=sys.stderr)
    for item in errors:
        print(" - " + item, file=sys.stderr)
    sys.exit(1)

print("命名空间对称性检查通过：%d 个约束注解，两套主源码与资源完全对称。" % annotation_count)
