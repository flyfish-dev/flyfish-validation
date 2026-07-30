#!/usr/bin/env python3
"""执行发布前不依赖第三方工具的源码策略检查。"""
from pathlib import Path
import re
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
errors = []

# 所有 Maven 描述符必须是合法 XML。
for pom in ROOT.rglob("pom.xml"):
    try:
        ET.parse(str(pom))
    except ET.ParseError as exc:
        errors.append("POM XML 无效 %s: %s" % (pom.relative_to(ROOT), exc))

# 生产 Java 包必须位于 dev.flyfish 下；离线桩和示例构建脚本不属于生产源码。
for source in ROOT.glob("flyfish-validation-*/src/main/java/**/*.java"):
    text = source.read_text(encoding="utf-8")
    if source.name != "module-info.java" and not re.search(r"\bpackage\s+dev\.flyfish(?:\.|;)", text):
        errors.append("生产包名不以 dev.flyfish 开头: %s" % source.relative_to(ROOT))

# 每个 Bean Validation 约束都必须声明规范要求的 message、groups 与 payload。
for source in ROOT.glob(
        "flyfish-validation-*/src/main/java/dev/flyfish/validation/constraints/**/*.java"):
    text = source.read_text(encoding="utf-8")
    if "@Constraint(" not in text:
        continue
    required_members = {
        "message": r"\bString\s+message\s*\(\)",
        "groups": r"\bClass<\?>\[\]\s+groups\s*\(\)",
        "payload": r"\bClass<\? extends Payload>\[\]\s+payload\s*\(\)",
    }
    for member, pattern in required_members.items():
        if not re.search(pattern, text):
            errors.append("Bean Validation 约束缺少 %s(): %s" %
                          (member, source.relative_to(ROOT)))

# 禁止把完全限定类名直接用于类型声明或实例化。字符串形式的可选类名、
# 消息资源 key、package/import 语句不属于此类问题。
qualified_usage = re.compile(
    r"\b(?:new|extends|implements|instanceof|throws|catch\s*\()\s*"
    r"(?:org\.springframework|javax\.validation|jakarta\.validation|dev\.flyfish)\."
)
for source in ROOT.glob("flyfish-validation-*/src/main/java/**/*.java"):
    for number, line in enumerate(source.read_text(encoding="utf-8").splitlines(), 1):
        stripped = line.strip()
        if stripped.startswith(("package ", "import ", "*", "//")):
            continue
        if qualified_usage.search(line):
            errors.append("方法体或声明中使用完全限定类名 %s:%d" %
                          (source.relative_to(ROOT), number))

# 发布包不得混入编译产物。
for suffix in ("*.class", "*.pyc"):
    for artifact in ROOT.rglob(suffix):
        if "target" not in artifact.parts:
            errors.append("源码树混入编译产物: %s" % artifact.relative_to(ROOT))

if errors:
    print("源码策略检查失败：", file=sys.stderr)
    for item in errors:
        print(" - " + item, file=sys.stderr)
    sys.exit(1)

java_count = sum(1 for _ in ROOT.glob("flyfish-validation-*/src/main/java/**/*.java"))
print("源码策略检查通过：%d 个生产 Java 源文件，POM、包名和发布树均符合约束。" % java_count)
