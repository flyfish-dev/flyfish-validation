import type { Metadata } from "next";
import "./globals.css";

const title = "Flyfish Validation — 企业级 Java 验证增强库";
const description =
  "72 个生产级约束，兼容 Java 8–21 与 Spring Boot 2/3/4。统一输入验证、业务规则与安全失败响应。";
const siteUrl = "https://flyfish-validation-site.wybaby168.workers.dev";
const socialImage = `${siteUrl}/og.png`;

export const metadata: Metadata = {
  metadataBase: new URL(siteUrl),
  title,
  description,
  alternates: { canonical: "/" },
  openGraph: {
    type: "website",
    url: "/",
    title,
    description,
    siteName: "Flyfish Validation",
    images: [{ url: socialImage, width: 1734, height: 907, alt: title }],
  },
  twitter: {
    card: "summary_large_image",
    title,
    description,
    images: [socialImage],
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="zh-CN">
      <body>{children}</body>
    </html>
  );
}
