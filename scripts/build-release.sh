#!/usr/bin/env bash
# 正式发布构建：源码、测试、Javadoc 与发布附件一次完成。
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
command -v mvn >/dev/null 2>&1 || { echo "正式发布构建需要 Maven 3.8.6+。" >&2; exit 1; }
"$ROOT/scripts/check-namespace-parity.py"
"$ROOT/scripts/check-source-policy.py"
(cd "$ROOT" && mvn -B -ntp -Prelease clean verify package)
