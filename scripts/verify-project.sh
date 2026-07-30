#!/usr/bin/env bash
# 统一验证入口：始终执行离线严格检查；有 Maven 时继续执行真实依赖测试。
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
"$ROOT/scripts/verify-source-compatibility.sh"
if command -v mvn >/dev/null 2>&1; then
    (cd "$ROOT" && mvn -B -ntp clean verify)
else
    echo "提示：当前环境未安装 Maven，已跳过真实 Hibernate Validator/Spring Boot 依赖测试。"
fi
