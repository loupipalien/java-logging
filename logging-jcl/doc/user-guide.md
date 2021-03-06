### 用户指南

#### 目录
TODO

#### 介绍
Apache Commons Logging (JCL) 提供了一个 `Log` 接口, 旨在实现轻量级和独立的其他日志工具包的抽象; 它为中间件/工具开发者提供了简单的日志记录抽象, 允许用户 (应用程序开发人员) 插入特定的日志记录实现  
JCL 为其他日志工具提供了精简包装 `Log` 实现, 包括 Log4J, Avalon LogKit (Avalon Framework 的日志记录基础架构), JDK 1.4 以及以前版本的日志 API (JSR-47) 的实现; 该接口与 Log4J 和 LogKit 紧密相关
假定你熟悉 Logging 实现相关的高级细节

#### 快速开始
尽可能地, JCL试图尽可能的简单; 在大多数情况下, 在类路径中包含 commons-logging.jar, JCL 就会以合理的方式配置自身; 它很可能会猜测 (发现) 你首选的日志记录系统, 根本不需要对 JCL 进行任何配置!  
但请注意, 如果你有特殊的偏好, 那么建议提供一个简单的 commons-logging.properties 文件, 该文件指定要使用的具体日志库, 因为 (在这种情况下) JCL 将仅记录到该系统, 并将报告任何阻止系统使用的配置问题  
如果未指定特定的日志记录库, 则 JCL 将静默忽略它找到的任何日志记录库, 并且无法初始化并继续查找其他备用库; 这是一个刻意的设计; 没有应用程序会因为无法使用 "猜测" 的日志库而运行失败; 要确保在无法使用特定日志记录库时报告异常, 请使用其中一个可用的 JCL 配置机制强制选择该库 (即禁用JCL的发现过程)

#### 配置
JCL 使用两种基本的抽象: `Log` (基本记录器) 和 `LogFactory` (知道如何创建 `Log` 实例); 指定特定的 Log 实现是非常有用 (无论是 commons-logging 还是用户定义的); 指定除默认值以外的 LogFactory 实现是高级用户的主题, 因此不在此处说明  
默认的 `LogFactory` 实现使用以下发现过程来确定它应该使用哪种类型的 Log 实现 (当找到第一个确定匹配时, 该过程终止)
- 查找名为 `org.apache.commons.logging.Log` 的工厂配置属性 (为了向后兼容此 API 的 1.0 之前版本, 还会查询属性 `org.apache.commons.logging.log`); 配置属性可以由 java 代码显式设置, 但通常通过在类路径中放置名为 commons-logging.properties 的文件来设置它们; 当存在这样的文件时, 属性文件中的每个条目都成为 LogFactory 的属性; 当类路径中有多个这样的文件时, 1.1 之前的 commons-logging 版本只使用找到的第一个; 从版本 1.1 开始, 每个文件可以定义优先级密钥, 并使用具有最高优先级的文件 (没有优先级定义意味着优先级为零); 当多个文件具有相同的优先级时, 将使用找到的第一个文件; 推荐在 commons-logging.properties 文件中定义此属性以明确选择 Log 实现
- 查找名为 `org.apache.commons.logging.Log` 的系统属性 (为了向后兼容此 API 的 1.0 之前的版本, 还会查询系统属性 `org.apache.commons.logging.log`)
- 如果 Log4J 日志记录系统在应用程序类路径中可用, 请使用相应的包装类 ([Log4JLogger](http://commons.apache.org/logging/apidocs/org/apache/commons/logging/impl/Jdk14Logger.html))
- 如果应用程序在 JDK 1.4 系统上执行, 请使用相应的包装类 ([Jdk14Logger](http://commons.apache.org/logging/apidocs/org/apache/commons/logging/impl/Jdk14Logger.html))
- 回退到默认的简单日志包装器 ([SimpleLog](http://commons.apache.org/logging/apidocs/org/apache/commons/logging/impl/SimpleLog.html))

组件随附的各种 `Log` 实现的详细信息, 请参阅 JCL javadocs (发现过程也有更详细的介绍)

#### 配置底层日志记录系统
JCL SPI 可以配置为使用不同的日志工具包 (见上文); JCL 只提供了写日志消息的桥接; 它不 (并且不会) 支持底层日志记录系统的任何类型的配置 API  
JCL 行为的配置最终取决于所使用的日志工具包; 请参阅所选日志系统的文档  
JCL 不负责底层日志库的初始化, 配置或关闭; 在许多情况下, 日志库将在首次使用时自动初始化/配置, 并且不需要显式关闭过程; 在这些情况下, 应用程序可以简单地使用 JCL, 而不是以任何方式直接依赖于底层日志记录系统的 API;但如果使用的日志库需要特殊的初始化, 配置或关闭, 那么应用程序中将需要一些特定于日志库的代码; JCL 只是将日志记录方法调用转发给正确的底层实现; 在编写库代码时, 此问题并不相关, 因为调用应用程序会负责处理此问题

#### 配置 Log4j
Log4J 是一种非常常用的日志记录实现 (以及作为JCL的主要默认设置), 因此本文提供了一些细节以使开发人员/集成人员使用; 有关 Log4J 及其配置的更多详细信息, 请参阅 [Log4J Homz](http://logging.apache.org/log4j/docs/index.html)  
使用系统属性和/或属性文件配置 Log4J:
- `log4j.configuration = log4j.properties` 使用此系统属性指定 Log4J 配置文件的名称; 如果未指定, 则默认配置文件为 log4j.properties
- `log4j.rootCategory=priority [, appender]*`, 设置默认 (根) 记录器优先级
- `log4j.logger.logger.name = priority` 设置指定记录器的优先级以及所有记录器的层次结构低于或低于命名记录器的优先级; logger.name 对应于 `LogFactory.getLog(logger.name)` 的参数, 用于创建记录器实例; 优先级为: DEBUG, INFO, WARN, ERROR 或 FATAL; Log4J 了解层次结构名称, 允许通过包或高级限定符进行控制: `log4j.logger.org.apache.component = DEBUG `将为 `org.apache.component` 和 `org.apache.component.sub` 中的所有类启用调试消息; 同样, 设置 `log4j.logger.org.apache.component = DEBUG` 将为所有 "组件" 类启用调试消息, 但不为其他 Apache 项目启用调试消息
- `log4j.appender.appender.Threshold=priority`, Log4J appender 对应于不同的输出设备: 控制台, 文件, 套接字等; 如果 appender 的阈值小于或等于消息优先级, 则消息由该 appender 写入; 这允许不同级别的细节出现在不同的日志目的地; 例如: 可以在日志文件中捕获 DEBUG (和更高级别) 级别信息, 同时将控制台输出限制为 INFO (和更高级别)

#### 使用 JCL 开发
TODO

#### Jars 包含在标准分布版中
##### commons-logging.jar
`commons-logging.jar` 文件包括 JCL API, 默认 `LogFactory` 实现和以及 Log4J, Avalon LogKit，Avalon Framework的日志记录基础结构的精简包装日志实现, 以及 JDK 1.4 日志 API 的实现 (JSR-47), 对于 1.4 之前的系统
在大多数情况下, 包括 `commons-logging.jar` 和类路径中首选的日志记录实现应该是使用 JCL 所需的全部内容
##### commons-logging-api.jar
TODO
##### commons-logging-adapters.jar
TODO

#### JCL 最佳实践
TODO

#### 扩展 Commons Logging
TODO

#### 简单日志快速指南
TODO

#### 常见问题解答
见 [FAQ 文档](http://wiki.apache.org/commons/Logging/FrequentlyAskedQuestions)

>**参考:**  
[User Guide](https://commons.apache.org/proper/commons-logging/guide.html)
