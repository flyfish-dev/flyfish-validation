# Flyfish Validation 约束参考

本文列出 `flyfish-validation-javax` 与 `flyfish-validation-jakarta` 共同提供的 72 个约束。两套模块的 Flyfish 注解包名完全一致，仅底层 Bean Validation 命名空间不同。

## 通用语义

- 字段级约束通常把 `null` 视为有效；必填使用标准 `@NotNull`、`@NotBlank` 或 `@NotEmpty`。
- 注解均支持 `message`、`groups`、`payload`，并提供默认、简体中文和英文消息资源。
- 跨字段约束把错误挂载到 `reportOn` 或其业务主字段，便于前端表单直接定位。
- 日期、生日、年龄和身份证验证使用 Bean Validation `ClockProvider`，测试中可固定时钟。

## 身份、组织与行政区划

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@ChinaIdCard` | 中国居民身份证号码。空值默认有效，必填请组合标准约束。 | `allowLegacy15`=false、`maximumAge`=150 |
| `@ChinesePassport` | 中国护照号码结构。空值默认有效，必填请组合标准约束。 | 无 |
| `@UnifiedSocialCreditCode` | 统一社会信用代码。空值默认有效，必填请组合标准约束。 | 无 |
| `@OrganizationCode` | 组织机构代码。空值默认有效，必填请组合标准约束。 | 无 |
| `@ChinaAdministrativeDivisionCode` | 中国行政区划代码。空值默认有效，必填请组合标准约束。 | `level`=AdministrativeDivisionLevel.AUTO |

## 电话、地址与个人信息

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@ChineseMobile` | 中国大陆手机号码。空值默认有效，必填请组合标准约束。 | `allowCountryCode`=true、`allowSeparators`=true |
| `@ChinaLandline` | 中国大陆固定电话号码。空值默认有效，必填请组合标准约束。 | `allowCountryCode`=true、`allowExtension`=true |
| `@E164Phone` | E.164 国际电话号码。空值默认有效，必填请组合标准约束。 | 无 |
| `@Phone` | 可配置的电话号码。空值默认有效，必填请组合标准约束。 | `type`=PhoneType.ANY |
| `@ChinaPostalCode` | 中国大陆邮政编码。空值默认有效，必填请组合标准约束。 | 无 |
| `@ChineseName` | 中文姓名。空值默认有效，必填请组合标准约束。 | `min`=2、`max`=32 |
| `@InternationalName` | 非中文国际姓名。空值默认有效，必填请组合标准约束。 | `min`=2、`max`=64 |
| `@PersonName` | 可配置的自然人姓名。空值默认有效，必填请组合标准约束。 | `type`=NameType.ANY、`min`=2、`max`=64 |
| `@Birthday` | 生日与年龄范围。空值默认有效，必填请组合标准约束。 | `pattern`="yyyy-MM-dd"、`zone`=""、`minAge`=0、`maxAge`=150 |
| `@Age` | 整数年龄范围。空值默认有效，必填请组合标准约束。 | `min`=0、`max`=150 |

## 账户、凭证与文本安全

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@Username` | 通用用户名。空值默认有效，必填请组合标准约束。 | `min`=3、`max`=32 |
| `@StrongPassword` | 强密码。空值默认有效，必填请组合标准约束。 | `min`=8、`max`=128、`requireUpper`=true、`requireLower`=true、`requireDigit`=true、`requireSpecial`=true、`rejectCommon`=true、`maxRepeated`=3 |
| `@StrictEmail` | 严格电子邮箱地址。空值默认有效，必填请组合标准约束。 | 无 |
| `@NoWhitespace` | 不包含空白字符。空值默认有效，必填请组合标准约束。 | 无 |
| `@Trimmed` | 首尾无空白。空值默认有效，必填请组合标准约束。 | 无 |
| `@ChineseCharacters` | 中文字符策略。空值默认有效，必填请组合标准约束。 | `allowWhitespace`=true、`allowDigits`=false、`allowPunctuation`=true |
| `@EnglishCharacters` | 英文字符策略。空值默认有效，必填请组合标准约束。 | `allowWhitespace`=true、`allowDigits`=false、`allowPunctuation`=true |
| `@ChineseOrEnglish` | 中英文字符策略。空值默认有效，必填请组合标准约束。 | `allowWhitespace`=true、`allowDigits`=false、`allowPunctuation`=true |

## 金融与数值

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@BankCard` | 银行卡号 Luhn 校验。空值默认有效，必填请组合标准约束。 | 无 |
| `@Luhn` | 通用 Luhn 校验。空值默认有效，必填请组合标准约束。 | `min`=1、`max`=64 |
| `@Iban` | 国际银行账户号码 IBAN。空值默认有效，必填请组合标准约束。 | 无 |
| `@Bic` | BIC/SWIFT 银行代码。空值默认有效，必填请组合标准约束。 | 无 |
| `@CurrencyCode` | ISO 4217 币种代码。空值默认有效，必填请组合标准约束。 | 无 |
| `@Money` | 金额范围、精度和小数位。空值默认有效，必填请组合标准约束。 | `min`=""、`max`=""、`fraction`=2、`precision`=19、`allowNegative`=false |
| `@Percentage` | 百分比数值。空值默认有效，必填请组合标准约束。 | `includeZero`=true、`includeHundred`=true |
| `@Latitude` | 纬度。空值默认有效，必填请组合标准约束。 | 无 |
| `@Longitude` | 经度。空值默认有效，必填请组合标准约束。 | 无 |
| `@Port` | 网络端口。空值默认有效，必填请组合标准约束。 | `min`=1、`max`=65535 |
| `@NumericValue` | 数字文本。空值默认有效，必填请组合标准约束。 | `integerOnly`=false |

## 网络与编码

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@IpAddress` | IP 地址。空值默认有效，必填请组合标准约束。 | `version`=IpVersion.ANY |
| `@Cidr` | CIDR 网络地址。空值默认有效，必填请组合标准约束。 | `version`=IpVersion.ANY |
| `@DomainName` | 互联网域名（支持 IDN）。空值默认有效，必填请组合标准约束。 | 无 |
| `@Url` | 限定协议的 URL。空值默认有效，必填请组合标准约束。 | `schemes`={"http", "https"}、`requireHost`=true |
| `@MacAddress` | MAC 地址。空值默认有效，必填请组合标准约束。 | 无 |
| `@Uuid` | UUID。空值默认有效，必填请组合标准约束。 | `allowCompact`=false |
| `@Base64Value` | Base64 编码文本。空值默认有效，必填请组合标准约束。 | `urlSafe`=false |
| `@HexValue` | 十六进制文本。空值默认有效，必填请组合标准约束。 | `evenLength`=false |

## 车辆、出版与设备

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@ChinaLicensePlate` | 中国大陆机动车号牌。空值默认有效，必填请组合标准约束。 | `includeNewEnergy`=true |
| `@Vin` | 车辆识别代号 VIN。空值默认有效，必填请组合标准约束。 | 无 |
| `@Isbn` | ISBN-10 或 ISBN-13。空值默认有效，必填请组合标准约束。 | 无 |
| `@Imei` | IMEI 设备号。空值默认有效，必填请组合标准约束。 | 无 |

## 日期、长度、集合与枚举

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@DatePattern` | 严格日期格式。空值默认有效，必填请组合标准约束。 | `pattern`="yyyy-MM-dd" |
| `@TimePattern` | 严格时间格式。空值默认有效，必填请组合标准约束。 | `pattern`="HH:mm:ss" |
| `@DateTimePattern` | 严格日期时间格式。空值默认有效，必填请组合标准约束。 | `pattern`="yyyy-MM-dd HH:mm:ss" |
| `@ByteLength` | UTF-8 字节长度。空值默认有效，必填请组合标准约束。 | `min`=0、`max`=2147483647 |
| `@CodePointLength` | Unicode 码点长度。空值默认有效，必填请组合标准约束。 | `min`=0、`max`=2147483647 |
| `@FileExtension` | 文件扩展名白名单。空值默认有效，必填请组合标准约束。 | `value`（必填） |
| `@MimeType` | MIME 类型白名单。空值默认有效，必填请组合标准约束。 | `value`（必填） |
| `@AllowedValues` | 候选值白名单。空值默认有效。 | `value`（必填）、`ignoreCase`=false |
| `@ForbiddenValues` | 候选值黑名单。空值默认有效。 | `value`（必填）、`ignoreCase`=false |
| `@EnumValue` | 枚举名称。空值默认有效。 | `ignoreCase`=false |
| `@CollectionUnique` | 集合元素不重复。空值默认有效。 | `ignoreNull`=false |
| `@NoNullElements` | 集合不包含 null 元素。空值默认有效。 | 无 |

## 跨字段关系

| 约束 | 作用 | 主要参数 |
|---|---|---|
| `@FieldsMatch` | 两个字段必须相等。 | `first`（必填）、`second`（必填）、`reportOn`="" |
| `@FieldsNotMatch` | 两个字段必须不相等。 | `first`（必填）、`second`（必填）、`reportOn`="" |
| `@AtLeastOneNotBlank` | 至少一个字段必须为非空文本。 | `fields`（必填）、`reportOn`="" |
| `@AtLeastOneNotNull` | 至少一个字段必须非 null。 | `fields`（必填）、`reportOn`="" |
| `@ExactlyOneNotBlank` | 恰好一个字段必须为非空文本。 | `fields`（必填）、`reportOn`="" |
| `@ExactlyOneNotNull` | 恰好一个字段必须非 null。 | `fields`（必填）、`reportOn`="" |
| `@AllOrNone` | 多个字段必须全部有值或全部无值。 | `fields`（必填）、`blankAsNull`=true、`reportOn`="" |
| `@MutuallyExclusive` | 多个字段最多只能有一个有值。 | `fields`（必填）、`blankAsNull`=true、`reportOn`="" |
| `@RequiredIf` | 条件字段命中指定值时目标字段必填。 | `conditionField`（必填）、`conditionValues`（必填）、`requiredField`（必填）、`ignoreCase`=false、`reportOn`="" |
| `@CompareFields` | 按操作符比较两个字段。 | `left`（必填）、`right`（必填）、`operator`=ComparisonOperator.EQUAL、`pattern`=""、`reportOn`="" |
| `@DateOrder` | 开始日期不得晚于结束日期。 | `start`（必填）、`end`（必填）、`pattern`="yyyy-MM-dd"、`allowEqual`=true、`reportOn`="" |
| `@NumberOrder` | 较小数值不得大于较大数值。 | `smaller`（必填）、`larger`（必填）、`allowEqual`=true、`reportOn`="" |
| `@BirthdayAgeConsistent` | 生日与年龄必须一致。 | `birthday`（必填）、`age`（必填）、`pattern`="yyyy-MM-dd"、`zone`=""、`tolerance`=0、`reportOn`="" |
| `@IdCardBirthdayConsistent` | 身份证出生日期与生日字段必须一致。 | `idCard`（必填）、`birthday`（必填）、`pattern`="yyyy-MM-dd"、`allowLegacy15`=false、`reportOn`="" |

## 使用边界

### 标识类规则不是权威主数据

`ChinaAdministrativeDivisionCode` 和 `ChinaIdCard` 默认使用保守 Provider：验证数字结构、层级长度和稳定省级前缀。需要精确到县、乡、村或需要历史有效期时，应提供业务自己的 `AdministrativeDivisionProvider`。

### 正则不是唯一校验手段

身份证、统一社会信用代码、银行卡、IBAN、VIN、ISBN、IMEI 均执行校验位或模数算法；日期使用严格解析；URL 和域名使用 URI/IDN 语义与边界检查，而不是只依赖单个宽松正则。

### 业务唯一性必须由数据库兜底

用户名、手机号、证件号等“是否已存在”属于业务验证，应实现 `BusinessValidator<T>`；并继续保留数据库唯一索引，处理并发写入竞态。
