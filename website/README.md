# Flyfish Validation Website

Flyfish Validation 1.0.0 的官方中文网站。站点使用 vinext 构建为 Cloudflare
Worker，包含响应式产品介绍、验证流水线动画、能力矩阵、快速接入示例与发布质量说明。

线上地址：<https://flyfish-validation-site.wybaby168.workers.dev>

## 本地开发

要求 Node.js 22.13 或更高版本。

```bash
npm install
npm run dev
```

访问 `http://localhost:3000`。

## 验证与发布

```bash
npm test
npm run deploy:cloudflare
```

`npm test` 会执行生产构建并对 Worker 服务端渲染结果做回归验证。
`deploy:cloudflare` 会重新构建并通过当前 Wrangler 账号发布编译产物。

## 主要文件

- `app/page.tsx`：页面结构与内容
- `app/globals.css`：视觉系统、响应式布局与动画
- `app/layout.tsx`：SEO 与社交分享元数据
- `public/og.png`：定制社交分享卡片
- `tests/rendered-html.test.mjs`：生产渲染回归测试
