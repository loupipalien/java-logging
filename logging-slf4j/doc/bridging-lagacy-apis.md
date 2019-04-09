### 桥接遗留的 APIs
通常，你依赖的某些组件依赖于 SLF4J 以外的日志记录 API; 你或许也可以假设这些组件在不久的将来不会切换到 SLF4J; 为了解决这种情况, SLF4J 附带了几个桥接模块, 这些模块将对 log4j, JCL 和 java.util.logging API 的进行重定向, 就好像它们调用 SLF4J API 一样; 下图说明了这个想法  
请注意, 对于你控制下的源代码应该使用 [slf4j-migrator](https://www.slf4j.org/migrator.html); 本文描述的基于二进制的解决方案适用于你无法控制的软件  
![image](https://www.slf4j.org/images/legacy.png)  
#### 从 Jakarta Commons Logging (JCL) 逐步迁移到 SLF4J
##### jcl-over-slf4j.jar
为了便于从 JCL 迁移到 SLF4J, SLF4J 发行版包含 jcl-over-slf4j.jar 文件; 此 jar 文件旨在作为 JCL-1.1.1 的替代品; 它实现了 JCL 的公共 API, 但在底层使用了 SLF4J, 因此命名为 "JCL over SLF4J"  
我们的 JCL over SLF4J 实现将允许你逐步迁移到 SLF4J, 特别是如果你的软件依赖的某些库在未来继续使用 JCL; 你可以立即享受 SLF4J 可靠性带来的好处, 同时保持向后兼容性; 只需用 jcl-over-slf4j.jar 替换 commons-logging.jar; 随后, 底层日志框架的选择将由 SLF4J 而不是 JCL 完成, 并且 [没有 JCL 的类加载器困扰](http://articles.qos.ch/classloader.html); 底层日志记录框架可以是 SLF4J 支持的任何框架; 通常用 jcl-over-slf4j.jar 替换 commons-logging.jar 将立即永久地解决与 commons-logging 相关的类加载器问题
##### slf4j-jcl.jar
我们的一些用户在切换到 SLF4J API 后意识到在某些情况下被强制使用 JCL, 并且他们使用 SLF4J 可能是个问题; 对于这种不常见但很重要的情况, 可以在 SLF4J 提供了一个 JCL 绑定中找到; JCL 绑定会将 SLF4J API 进行的所有日志记录调用委托给 JCL; 因此, 如果由于某种原因现有应用程序必须使用 JCL, 那么你的应用程序部分仍然可以以对大型应用程序环境透明的方式对 SLF4J API 进行编码; 你选择的 SLF4J API 对于继续使用 JCL 的应用程序的其余部分是不可见的
##### jcl-over-slf4j.jar 和 slf4j-jcl.jar 不应同时使用
JCL-over-SLF4J, 即 jcl-over-slf4j.jar, 在出于向后兼容性原因需要支持 JCL 的情况下派上用场; 它可以用来修复与 JCL 相关的问题, 而不必采用 SLF4J API, 这个决定可以推迟到以后的时间  
另一方面, 在你已经为组件采用 SLF4J API 之后, 它需要嵌入到 JCL 是正式要求的更大的应用程序环境中, slf4j-jcl.jar 会非常有用; 你的软件组件仍然可以使用 SLF4J API 而不会中断较大的应用程序; 实际上, slf4j-jcl.jar 会将所有日志记录决策委托给 JCL, 这样你的组件使用 SLF4J API 对整个组件来说都是透明的  
请注意, jcl-over-slf4j.jar 和 slf4j-jcl.jar 不能同时部署; 前一个 jar 文件将导致 JCL 将日志系统的选择委托给 SLF4J, 后一个 jar 文件将导致 SLF4J 将日志系统的选择委托给 JCL, 从而导致 [无限循环](https://www.slf4j.org/codes.html#jclDelegationLoop)

#### log4j-over-slf4j 桥接
SLF4J 有一个名为 log4j-over-slf4j 的模块; 它允许 log4j 用户将现有应用程序迁移到 SLF4J, 而无需更改一行代码, 只需将 log4j-over-slf4j.jar 替换为 log4j.jar 文件, 如下所述
##### 它如何工作?
TODO
##### 什么时候不起作用?
TODO
##### 开销如何?
TODO
##### log4j-over-slf4j.jar 和 slf4j-log4j12.jar 不应该同时使用
slf4j-log4j12.jar 的存在将强制所有 SLF4J 调用委托给 log4j; log4j-over-slf4j.jar 的存在将依次将所有 log4j API 调用委托给 SLF4J; 如果两者同时存在, 则 slf4j 调用将委托给 log4j, 并且 log4j 调用将重定向到 SLF4j, 从而导致 [无限循环](https://www.slf4j.org/codes.html#log4jDelegationLoop)

#### jul-to-slf4j 桥接
jul-to-slf4j.jar 构件包括一个 java.util.logging (jul) 处理器, 即 SLF4JBridgeHandler, 它将所有传入的 jul 日志记录路由到 SLF4j API; 有关使用说明详见 [SLF4JBridgeHandler 文档](https://www.slf4j.org/api/org/slf4j/bridge/SLF4JBridgeHandler.html)  
`关于性能的说明`: 与其他桥接模块 jcl-over-slf4j, log4j-over-slf4j (重新实现JCL和log4j) 相反, jul-to-slf4j 模块不会重新实现 java.util.logging, 因为 `java.*` 包的命名空间无法替换; 相反, jul-to-slf4j 将 LogRecord 对象转换为 SLF4J 的等效对象; 请注意, 无论 SLF4J 记录器是否对给定级别禁用, 此装换过程都会产生 LogRecord 实例的开销, 因此 jul-to-slf4j 转换会严重增加禁用日志记录语句的开销 (60 倍), 并且会显着影响已启用日志语句的性能 (总体增长率为20%); 从 logback 版本 0.9.25 开始, 在 [LevelChangePropagator](http://logback.qos.ch/manual/configuration.html#LevelChangePropagator) 的帮助下, 可以完全消除禁用日志语句的 60 倍转换开销  
如果你担心应用程序性能, 那么请在在只有满足以下两个条件之一时才使用 SLF4JBridgeHandler
- 少量的 jul 日志记录语句被使用
- 安装了 LevelChangePropagator
##### jul-to-slf4j.jar 和 slf4j-jdk14.jar 不应同时使用
slf4j-jdk14.jar 的存在将强制将 SLF4J 调用委托给 jul; 另一方面, 通过调用 "SLF4JBridgeHandler.install()", jul-to-slf4j.jar 的存在以及 SLF4JBridgeHandler 的安装将把 jul 记录路由到 SLF4J; 因此, 如果两个 jar 同时存在 (并且安装了 SLF4JBridgeHandler), 则 slf4j 调用将被委托给 jul, jul 记录将被路由到 SLF4J, 从而产生无限循环
>**参考:**
[Bridging legacy APIs](https://www.slf4j.org/legacy.html)
