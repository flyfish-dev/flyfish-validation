const repository = "https://github.com/flyfish-dev/flyfish-validation";

const constraintGroups = [
  ["@ChineseMobile", "@ChinaIdCard", "@StrongPassword", "@StrictEmail"],
  ["@FieldsMatch", "@RequiredIf", "@DateOrder", "@CollectionUnique"],
];

const features = [
  {
    index: "01",
    eyebrow: "STANDARD CONSTRAINTS",
    title: "72 个生产级约束",
    body: "从中国身份与企业数据，到金融、网络、集合和跨字段关系。三语言消息完整，空值语义统一。",
    tags: ["身份数据", "金融网络", "跨字段"],
    className: "feature-card feature-card-wide feature-lime",
  },
  {
    index: "02",
    eyebrow: "BUSINESS RULE ENGINE",
    title: "业务规则，不挤进注解",
    body: "同步、异步与 Spring Validator 三种扩展入口，自动发现 Bean，让数据库与远程校验保持清晰边界。",
    tags: ["可注入", "可组合", "可观测"],
    className: "feature-card feature-card-violet",
  },
  {
    index: "03",
    eyebrow: "SAFE BY DEFAULT",
    title: "失败响应默认安全",
    body: "统一 MVC / WebFlux 错误模型，默认隐藏 rejected value，并开放脱敏、状态码与响应体扩展。",
    tags: ["MVC", "WebFlux"],
    className: "feature-card feature-card-cyan",
  },
  {
    index: "04",
    eyebrow: "ONE PUBLIC API",
    title: "javax ↔ jakarta 同 API",
    body: "迁移 Spring Boot 大版本时，只需替换 Starter 与标准注解导入；Flyfish 包名和业务写法保持不变。",
    tags: ["Boot 2", "Boot 3", "Boot 4"],
    className: "feature-card feature-card-wide feature-dark",
  },
];

const releaseChecks = [
  "Java 8 / 17 字节码策略",
  "javax / jakarta 源码对称",
  "真实 Hibernate Validator 集成",
  "Spring Boot 2 / 3 / 4 上下文",
  "源码、Javadoc 与发布附件",
  "Apache-2.0 开源许可",
];

export default function Home() {
  return (
    <main>
      <div className="ambient ambient-one" aria-hidden="true" />
      <div className="ambient ambient-two" aria-hidden="true" />
      <div className="noise" aria-hidden="true" />

      <header className="site-header">
        <a className="brand" href="#top" aria-label="Flyfish Validation 首页">
          <span className="brand-mark" aria-hidden="true">
            <span />
            <span />
          </span>
          <span>Flyfish</span>
          <strong>Validation</strong>
        </a>
        <nav aria-label="主导航">
          <a href="#features">能力</a>
          <a href="#architecture">架构</a>
          <a href="#quickstart">接入</a>
          <a
            className="nav-github"
            href={repository}
            target="_blank"
            rel="noreferrer"
          >
            GitHub <span aria-hidden="true">↗</span>
          </a>
        </nav>
      </header>

      <section className="hero" id="top">
        <div className="hero-copy">
          <div className="status-pill">
            <span className="status-dot" aria-hidden="true" />
            v1.0.0 · RELEASE READY
          </div>
          <h1>
            让每一次验证，
            <span>都有章法。</span>
          </h1>
          <p className="hero-lead">
            面向 Java 与 Spring Boot 的企业级验证增强库。
            一套干净、可组合、跨代演进的规则系统，把输入校验、业务规则与失败响应接成一条可靠链路。
          </p>
          <div className="hero-actions">
            <a
              className="button button-primary"
              href={`${repository}#快速接入`}
              target="_blank"
              rel="noreferrer"
            >
              立即接入
              <span aria-hidden="true">→</span>
            </a>
            <a
              className="button button-secondary"
              href={`${repository}/tree/main/docs`}
              target="_blank"
              rel="noreferrer"
            >
              阅读文档
            </a>
          </div>
          <div className="install-inline" aria-label="Maven 坐标">
            <span>$</span>
            <code>dev.flyfish : flyfish-validation</code>
            <i>1.0.0</i>
          </div>
        </div>

        <div className="hero-visual" aria-label="验证流水线动画演示">
          <div className="orbit orbit-one" aria-hidden="true" />
          <div className="orbit orbit-two" aria-hidden="true" />
          <div className="pipeline-card">
            <div className="pipeline-topbar">
              <div className="window-dots" aria-hidden="true">
                <i />
                <i />
                <i />
              </div>
              <span>validation.pipeline</span>
              <strong>LIVE</strong>
            </div>
            <div className="pipeline-body">
              <div className="request-block">
                <span className="block-label">INCOMING REQUEST</span>
                <div className="request-line">
                  <i />
                  <span>POST</span>
                  <code>/api/users</code>
                  <b>12ms</b>
                </div>
              </div>

              <div className="flow-line" aria-hidden="true">
                <i />
              </div>

              <div className="validation-stack">
                <div className="validation-row row-one">
                  <span className="check-icon">✓</span>
                  <div>
                    <strong>Bean constraints</strong>
                    <small>structure · format · cross-field</small>
                  </div>
                  <em>72 rules</em>
                </div>
                <div className="validation-row row-two">
                  <span className="check-icon">✓</span>
                  <div>
                    <strong>Business validation</strong>
                    <small>repository · remote · async</small>
                  </div>
                  <em>composed</em>
                </div>
                <div className="validation-row row-three">
                  <span className="check-icon">✓</span>
                  <div>
                    <strong>Safe error mapping</strong>
                    <small>MVC · WebFlux · sanitized</small>
                  </div>
                  <em>ready</em>
                </div>
              </div>

              <div className="flow-line flow-line-last" aria-hidden="true">
                <i />
              </div>

              <div className="response-block">
                <div>
                  <span>STATUS</span>
                  <strong>200</strong>
                </div>
                <div>
                  <span>RULES</span>
                  <strong>PASS</strong>
                </div>
                <div>
                  <span>EXPOSURE</span>
                  <strong>SAFE</strong>
                </div>
              </div>
            </div>
            <div className="scan-line" aria-hidden="true" />
          </div>
          <div className="floating-chip chip-java">JAVA 8—21</div>
          <div className="floating-chip chip-boot">BOOT 2 · 3 · 4</div>
        </div>
      </section>

      <section className="proof-strip" aria-label="项目数据">
        <div>
          <strong>72</strong>
          <span>业务约束</span>
        </div>
        <div>
          <strong>3</strong>
          <span>代 Spring Boot</span>
        </div>
        <div>
          <strong>2</strong>
          <span>套验证命名空间</span>
        </div>
        <div>
          <strong>8—21</strong>
          <span>Java 兼容范围</span>
        </div>
      </section>

      <section className="constraint-marquee" aria-label="部分约束清单">
        {constraintGroups.map((group, groupIndex) => (
          <div
            className={`marquee-track marquee-track-${groupIndex + 1}`}
            key={group.join("")}
          >
            {[...group, ...group, ...group].map((constraint, index) => (
              <span key={`${constraint}-${index}`}>
                {constraint}
                <i aria-hidden="true">✦</i>
              </span>
            ))}
          </div>
        ))}
      </section>

      <section className="section section-intro" id="features">
        <div className="section-kicker">
          <span>01</span>
          <p>WHY FLYFISH</p>
        </div>
        <div className="section-heading">
          <h2>
            验证应该是工程能力，
            <span>不是散落的 if。</span>
          </h2>
          <p>
            Flyfish 把高频规则、业务校验和 Web 失败处理拆成清晰层次。
            规则更容易复用，边界更容易测试，版本迁移也不再推倒重来。
          </p>
        </div>

        <div className="feature-grid">
          {features.map((feature) => (
            <article className={feature.className} key={feature.index}>
              <div className="feature-number">{feature.index}</div>
              <p className="card-eyebrow">{feature.eyebrow}</p>
              <h3>{feature.title}</h3>
              <p className="feature-body">{feature.body}</p>
              <div className="tag-list">
                {feature.tags.map((tag) => (
                  <span key={tag}>{tag}</span>
                ))}
              </div>
              <div className="card-glow" aria-hidden="true" />
            </article>
          ))}
        </div>
      </section>

      <section className="section generations">
        <div className="section-kicker section-kicker-light">
          <span>02</span>
          <p>ONE API · THREE GENERATIONS</p>
        </div>
        <div className="generations-layout">
          <div>
            <h2>
              一套规则，
              <span>贯穿三代 Spring。</span>
            </h2>
            <p>
              Flyfish 保持自己的公共 API 稳定，在底层分别拥抱 javax 与 jakarta。
              应用升级时，业务规则不需要跟着框架重写。
            </p>
          </div>
          <div className="generation-rail">
            <div className="rail-line" aria-hidden="true">
              <i />
            </div>
            <div className="generation-node">
              <span>BOOT</span>
              <strong>2.7</strong>
              <small>javax.validation</small>
              <em>Java 8+</em>
            </div>
            <div className="generation-node generation-node-active">
              <span>BOOT</span>
              <strong>3.x</strong>
              <small>jakarta.validation</small>
              <em>Java 17+</em>
            </div>
            <div className="generation-node">
              <span>BOOT</span>
              <strong>4.x</strong>
              <small>jakarta.validation</small>
              <em>Java 17+</em>
            </div>
          </div>
        </div>
      </section>

      <section className="section architecture" id="architecture">
        <div className="section-kicker">
          <span>03</span>
          <p>ARCHITECTURE</p>
        </div>
        <div className="architecture-heading">
          <h2>把复杂留在框架里，把清晰还给业务。</h2>
          <p>
            从请求入口到业务规则，再到安全的失败输出，每一层只做自己擅长的事。
          </p>
        </div>
        <div className="architecture-flow">
          <div className="architecture-node">
            <span>01</span>
            <strong>Request</strong>
            <small>MVC / WebFlux</small>
          </div>
          <div className="architecture-connector" aria-hidden="true">
            <i />
          </div>
          <div className="architecture-node architecture-node-core">
            <span>02</span>
            <strong>Flyfish Core</strong>
            <small>constraints + business rules</small>
            <div className="core-rings" aria-hidden="true" />
          </div>
          <div className="architecture-connector" aria-hidden="true">
            <i />
          </div>
          <div className="architecture-node">
            <span>03</span>
            <strong>Response</strong>
            <small>stable + sanitized</small>
          </div>
        </div>
      </section>

      <section className="section quickstart" id="quickstart">
        <div className="quickstart-copy">
          <div className="section-kicker">
            <span>04</span>
            <p>START IN MINUTES</p>
          </div>
          <h2>
            引入 Starter，
            <span>规则即刻就位。</span>
          </h2>
          <p>
            选择项目对应的 Spring Boot 版本。自动装配会接入约束验证器、业务规则注册表与统一失败流水线。
          </p>
          <a
            href={`${repository}#快速接入`}
            target="_blank"
            rel="noreferrer"
          >
            查看完整接入指南 <span aria-hidden="true">↗</span>
          </a>
        </div>
        <div className="code-window">
          <div className="code-topbar">
            <span>pom.xml</span>
            <em>Maven</em>
          </div>
          <pre>
            <code>
              <span className="code-muted">&lt;dependency&gt;</span>
              {"\n"}
              {"  "}
              <span className="code-muted">&lt;groupId&gt;</span>
              <span className="code-accent">dev.flyfish</span>
              <span className="code-muted">&lt;/groupId&gt;</span>
              {"\n"}
              {"  "}
              <span className="code-muted">&lt;artifactId&gt;</span>
              <span className="code-highlight">
                flyfish-validation-spring-boot3-starter
              </span>
              <span className="code-muted">&lt;/artifactId&gt;</span>
              {"\n"}
              {"  "}
              <span className="code-muted">&lt;version&gt;</span>
              <span className="code-accent">1.0.0</span>
              <span className="code-muted">&lt;/version&gt;</span>
              {"\n"}
              <span className="code-muted">&lt;/dependency&gt;</span>
            </code>
          </pre>
          <div className="code-footer">
            <span>
              <i aria-hidden="true" /> Auto-configured
            </span>
            <span>Boot 2 / 3 / 4</span>
          </div>
        </div>
      </section>

      <section className="section release-section">
        <div className="release-card">
          <div className="release-copy">
            <div className="release-badge">
              <span aria-hidden="true">✓</span>
              VERIFIED RELEASE
            </div>
            <h2>发布之前，先把信心跑一遍。</h2>
            <p>
              不只“能编译”。离线 API 面、真实 Provider、三代 Spring
              上下文与发布附件都进入同一套可重复验证流程。
            </p>
          </div>
          <div className="release-checks">
            {releaseChecks.map((check, index) => (
              <div key={check}>
                <span>{String(index + 1).padStart(2, "0")}</span>
                <p>{check}</p>
                <i aria-hidden="true">✓</i>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="final-cta">
        <div className="cta-orbit cta-orbit-one" aria-hidden="true" />
        <div className="cta-orbit cta-orbit-two" aria-hidden="true" />
        <p>THE RULES ARE READY</p>
        <h2>
          少写一点防御代码，
          <span>多交付一点确定性。</span>
        </h2>
        <div className="hero-actions">
          <a
            className="button button-primary"
            href={repository}
            target="_blank"
            rel="noreferrer"
          >
            在 GitHub 查看
            <span aria-hidden="true">↗</span>
          </a>
          <a
            className="button button-secondary button-secondary-light"
            href={`${repository}/blob/main/docs/constraints-reference.md`}
            target="_blank"
            rel="noreferrer"
          >
            浏览 72 个约束
          </a>
        </div>
      </section>

      <footer>
        <a className="brand brand-footer" href="#top">
          <span className="brand-mark" aria-hidden="true">
            <span />
            <span />
          </span>
          <span>Flyfish</span>
          <strong>Validation</strong>
        </a>
        <p>Enterprise validation, composed with clarity.</p>
        <div>
          <a href={`${repository}/blob/main/LICENSE`}>Apache-2.0</a>
          <a href={repository}>GitHub</a>
          <span>© 2026 Flyfish Dev</span>
        </div>
      </footer>
    </main>
  );
}
