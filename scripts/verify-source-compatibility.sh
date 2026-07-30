#!/usr/bin/env bash
# 在没有 Maven 和外部依赖缓存时，对全部生产源码进行可重复的 API 面编译。
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
WORK="$ROOT/target/offline-verify"
STUBS="$ROOT/build-support/offline-stubs"
rm -rf "$WORK"
mkdir -p "$WORK"

command -v javac >/dev/null 2>&1 || { echo "未找到 javac" >&2; exit 1; }
command -v java >/dev/null 2>&1 || { echo "未找到 java" >&2; exit 1; }

JAVA_MAJOR="$(javac -version 2>&1 | sed -E 's/.* ([0-9]+).*/\1/')"
if [[ "$JAVA_MAJOR" -lt 17 ]]; then
    echo "完整离线校验需要 JDK 17+，当前 javac 主版本为 $JAVA_MAJOR。" >&2
    exit 1
fi

compile() {
    local release="$1" classpath="$2" output="$3" source_list="$4"
    mkdir -p "$output"
    if [[ -n "$classpath" ]]; then
        javac --release "$release" -encoding UTF-8 -parameters \
            -Xlint:all,-options -Werror -cp "$classpath" \
            -d "$output" "@$source_list"
    else
        javac --release "$release" -encoding UTF-8 -parameters \
            -Xlint:all,-options -Werror -d "$output" "@$source_list"
    fi
}

find "$STUBS/bean-validation" -name '*.java' -print | sort > "$WORK/bean-validation.sources"
compile 8 "" "$WORK/bean-validation" "$WORK/bean-validation.sources"

find "$STUBS/spring-validation" -name '*.java' -print | sort > "$WORK/spring-validation.sources"
compile 8 "" "$WORK/spring-validation" "$WORK/spring-validation.sources"

find "$STUBS/junit" -name '*.java' -print | sort > "$WORK/junit.sources"
compile 8 "" "$WORK/junit" "$WORK/junit.sources"

find "$STUBS/spring-boot/common" -name '*.java' -print | sort > "$WORK/spring-common.sources"
compile 8 "$WORK/bean-validation:$WORK/spring-validation" \
    "$WORK/spring-common" "$WORK/spring-common.sources"

for track in boot2 boot34; do
    find "$STUBS/spring-boot/$track" -name '*.java' -print | sort > "$WORK/$track.sources"
    compile 8 "$WORK/bean-validation:$WORK/spring-validation:$WORK/spring-common" \
        "$WORK/$track" "$WORK/$track.sources"
done

find "$ROOT/flyfish-validation-core/src/main/java" -name '*.java' -print | sort > "$WORK/core.sources"
compile 8 "" "$WORK/core" "$WORK/core.sources"

for namespace in javax jakarta; do
    find "$ROOT/flyfish-validation-$namespace/src/main/java" -name '*.java' -print | sort > "$WORK/$namespace.sources"
    compile 8 "$WORK/core:$WORK/bean-validation" \
        "$WORK/$namespace" "$WORK/$namespace.sources"
done

find "$ROOT/flyfish-validation-spring-support/src/main/java" -name '*.java' -print | sort > "$WORK/spring-support.sources"
compile 8 "$WORK/core:$WORK/spring-validation" \
    "$WORK/spring-support" "$WORK/spring-support.sources"

for version in 2 3 4; do
    find "$ROOT/flyfish-validation-spring-boot${version}-autoconfigure/src/main/java" \
        -name '*.java' -print | sort > "$WORK/boot${version}.sources"
    if [[ "$version" == "2" ]]; then
        release=8
        namespace="$WORK/javax"
        version_stubs="$WORK/boot2"
    else
        release=17
        namespace="$WORK/jakarta"
        version_stubs="$WORK/boot34"
    fi
    compile "$release" \
        "$WORK/core:$namespace:$WORK/spring-support:$WORK/bean-validation:$WORK/spring-validation:$WORK/spring-common:$version_stubs" \
        "$WORK/flyfish-boot${version}" "$WORK/boot${version}.sources"
done

# 编译正式测试源码，提前发现测试与公开 API 漂移。
find "$ROOT/flyfish-validation-core/src/test/java" -name '*.java' -print | sort > "$WORK/core-test.sources"
compile 8 "$WORK/core:$WORK/junit" "$WORK/core-test" "$WORK/core-test.sources"

for namespace in javax jakarta; do
    find "$ROOT/flyfish-validation-$namespace/src/test/java" -name '*.java' -print | sort > "$WORK/$namespace-test.sources"
    compile 8 "$WORK/core:$WORK/$namespace:$WORK/bean-validation:$WORK/junit" \
        "$WORK/$namespace-test" "$WORK/$namespace-test.sources"
done

find "$ROOT/flyfish-validation-spring-support/src/test/java" -name '*.java' -print | sort > "$WORK/spring-support-test.sources"
compile 8 "$WORK/core:$WORK/spring-support:$WORK/spring-validation:$WORK/junit" \
    "$WORK/spring-support-test" "$WORK/spring-support-test.sources"

# 运行不依赖 JUnit 的核心算法与业务 SPI 回归。
printf '%s\n' "$ROOT/build-support/CoreSelfTest.java" > "$WORK/self-test.sources"
compile 8 "$WORK/core" "$WORK/self-test" "$WORK/self-test.sources"
java -cp "$WORK/core:$WORK/self-test" dev.flyfish.validation.build.CoreSelfTest

"$ROOT/scripts/check-namespace-parity.py"
"$ROOT/scripts/check-constraint-catalog.py"
"$ROOT/scripts/check-source-policy.py"

echo "离线兼容性校验通过：Java 8 核心/Boot 2，Java 17 API 面 Boot 3/4。"
