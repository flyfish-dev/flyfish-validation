# Release Checklist

- [ ] `./scripts/verify-source-compatibility.sh`
- [ ] `mvn -B -ntp clean verify`
- [ ] Boot 2：JDK 8、11、17、21 回归
- [ ] Boot 3/4：JDK 17、21 回归
- [ ] Javax/Jakarta 约束 parity 通过
- [ ] 三套示例可以启动并返回统一错误响应
- [ ] 行政区划 Provider 自定义注入测试通过
- [ ] 敏感 rejected value 默认不可见
- [ ] 依赖漏洞与许可证扫描通过
- [ ] 公共 API 二进制兼容检查通过
- [ ] CHANGELOG、README、迁移文档已更新
- [ ] source/javadoc JAR、签名、校验和已生成
- [ ] 发布签名指纹为 `361C 52A3 F6B5 CA75 D8B7  9C08 44BF 9601 A5D2 26FA`
- [ ] Central Portal 中的 `dev.flyfish` namespace 已验证
- [ ] GitHub Actions 已配置 `CENTRAL_USERNAME`、`CENTRAL_PASSWORD`、
      `GPG_PRIVATE_KEY` 与 `GPG_PASSPHRASE`
- [ ] `mvn -B -ntp -Prelease clean deploy` 已等待至 `published`
