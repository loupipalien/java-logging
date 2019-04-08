### SLF4J 用户手册
Simple Logging Facade for Java (SLF4J) 可用作各种日志框架的简单外观或抽象, 例如 `java.util.logging`, `logback`, `log4j`; SLF4J 允许最终用户在部署时插入所需的日志记录框架; 请注意, 启用 SLF4J 的库/应用程序意味着只添加一个强制依赖项, 即 slf4j-api-1.8.0-beta4.jar
TODO
#### Hello World
TODO

#### 在部署时与日志框架绑定
如前所述, SLF4J 支持各种日志框架; SLF4J 发行版附带了几个称为 "SLF4J绑定" 的 jar 文件, 每个绑定对应一个受支持的框架

- slf4j-log4j12-1.8.0-beta4.jar
绑定 log4j 1.2, 这是一个广泛使用的日志框架; 除此之外你还需要将 log4j.jar 放在类路径上
- slf4j-jdk14-1.8.0-beta4.jar
绑定 java.util.logging, 也称为 JDK1.4 日志记录
- slf4j-nop-1.8.0-beta4.jar
绑定 [NOP](http://www.slf4j.org/api/org/slf4j/helpers/NOPLogger.html), 静默的丢弃所有日志记录
- slf4j-simple-1.8.0-beta4.jar
绑定 [Simple](http://www.slf4j.org/apidocs/org/slf4j/impl/SimpleLogger.html) 实现, 会将所有事件输出到 System.err; 仅打印 INFO 级别及以上的消息; 此绑定在小的应用程序上下文中可能很有用
- slf4j-jcl-1.8.0-beta4.jar
绑定 [Jakarta Commons Logging](http://commons.apache.org/logging/), 此绑定将会代理所有的 SLF4J 日志记录到 JCL
- logback-classic-1.0.13.jar (需要 logback-core-1.0.13.jar)
`原生实现`, 即 SLF4J 项目外部也有 SLF4J 绑定, 例如: [logback](http://logback.qos.ch/) 原生的实现了 SLF4J; logback 的 `ch.qos.logback.classic.Logger` 类是 SLF4J 的 `org.slf4j.Logger ` 接口的直接实现; 因此, SLF4J 和 logback 结合使用意味着严格的零内存和计算开销

为了切换日志框架, 只需替换类路径上的 slf4j 绑定即可; 例如, 要从 java.util.logging 切换到 log4j, 只需将 slf4j-jdk14-1.8.0-beta4.jar 替换为 slf4j-log4j12-1.8.0-beta4.jar 即可  
SLF4J 不依赖于任何特殊的类加载机制;  实际上, 每个 SLF4J 绑定在编译时都是硬连线的, 以使用唯一特定的日志记录框架; 例如, slf4j-log4j12-1.8.0-beta4.jar 绑定在编译时绑定以使用 log4j; 在你的代码中, 除了 slf4j-api-1.8.0-beta4.jar 之外, 你只需将你选择的唯一的绑定放到相应的类路径位置; 不要在类路径上放置多个绑定; 以下是一般概念的图解说明  
![image](https://www.slf4j.org/images/concrete-bindings.png)  
SLF4J 接口及其各种适配器非常简单; 大多数熟悉 Java 语言的开发人员应该能够在很短的时间内阅读并完全理解代码; 不需要类加载器的知识, 因为 SLF4J 并不使用, 也不能直接访问任何类加载器; 因此, SLF4J 不会遇到使用 Jakarta Commons Logging (JCL) 时的类加载器问题或内存泄漏  
鉴于 SLF4J 接口及其部署模型的简单性, 新的日志框架的开发人员会发现编写 SLF4J 绑定非常容易

#### 库
TODO

#### 声明日志记录的项目依赖
鉴于 Maven 的传递依赖规则, 对于 "常规" 项目 (不是库或框架), 可以使用单个依赖声明来声明日志记录依赖性
##### LOGBACK-CLASSIC
如果你希望使用 logback-classic 作为底层日志记录框架, 你需要做的就是将 "ch.qos.logback：logback-classic" 声明为 pom.xml 文件中的依赖项; 除了 logback-classic-1.0.13.jar 之外, 这还会将 slf4j-api-1.8.0-beta4.jar 以及 logback-core-1.0.13.jar 引入到你的项目中; 请注意, 明确声明 logback-core-1.0.13 或 slf4j-api-1.8.0-beta4.jar 的依赖也是没有错的, 并且可能需要凭借 Maven的 "最接近定义" 依赖关系调解规则强调所述构件的正确版本
```
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.0.13</version>
</dependency>
```
##### LOG4J
如果你希望使用 log4j 作为底层日志记录框架, 你需要做的就是将 "org.slf4j:slf4j-log4j12" 声明为 pom.xml 文件中的依赖项; 除了 slf4j-log4j12-1.8.0-beta4.jar 之外, 这将把 slf4j-api-1.8.0-beta4.jar 以及 log4j-1.2.17.jar 引入你的项目; 请注意, 明确声明对 log4j-1.2.17.jar 或 slf4j-api-1.8.0-beta4.jar 的依赖也是没有错的, 并且可能需要凭借 Maven的 "最接近定义" 依赖关系调解规则强调所述构件的正确版本
```
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.8.0-beta4</version>
</dependency>
```
##### JAVA.UTIL.LOGGING
如果你希望使用 java.util.logging 作为底层日志记录框架, 你需要做的就是将 "org.slf4j:slf4j-jdk14" 声明为 pom.xml 文件中的依赖项; 除了 slf4j-jdk14-1.8.0-beta4.jar 之外, 这将把 slf4j-api-1.8.0-beta4.jar 引入你的项目; 请注意, 明确声明对 slf4j-api-1.8.0-beta4.jar 的依赖也是没有错的,并且可能需要凭借 Maven的 "最接近定义" 依赖关系调解规则强调所述构件的正确版本
```
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-jdk14</artifactId>
  <version>1.8.0-beta4</version>
</dependency>
```

#### 二进制兼容性
TODO

#### 通过 SLF4J 整合日志记录
通常, 项目依赖于各种组件, 其依赖于 SLF4J 以外的日志 API; 通常会见到项目依赖于 JCL, java.util.logging, log4j 和 SLF4J 的组合; 通过单个通道合并日志记录成为了需要; SLF4J 通过为 JCL, java.util.logging 和 log4j 提供桥接模块来满足这种常见用例; 更多详细信息见 [Bridging legacy APIs](https://www.slf4j.org/legacy.html)

#### 映射的诊断上下文(MDC) 支持
TODO

>**参考:**
[SLF4J user manual](https://www.slf4j.org/manual.html)
