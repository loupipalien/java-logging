### 有关 SLF4J 的常见问题解答

#### 通识
##### 什么是 SLF4J ?
SLF4J 是用于日志记录系统的简单外观, 允许最终用户在部署时插入所需的日志记录系统
##### 什么时候应该使用 SLF4J ?
TODO
##### SLF4J 是另一日志门面吗 ?
SLF4J 在概念上与 Jakarta Commons Logging (JCL) 非常相似; 因此, 它可以被认为是另一个日志门面; 但是, SLF4J 在设计上要简单得多, 而且可以说更强大; 简而言, SLF4J 避免了困扰 JCL 的类加载器问题
##### 如果说 SLF4J 修复了 JCL, 那么为什么不在 JCL 中进行修复而是创建新项目 ?
这个问题问得好; 首先, SLF4J 静态绑定方法非常简单, 或许甚至简单到可笑; 要让开发人员相信这种方法的有效性并不容易; 只有在 SLF4J 发布并开始被接受之后, s才能在相关社区中获得尊重  
其次, SLF4J 提供了两个往往被低估的增强功能; 参数化日志消息解决日志记录性能相关的重要问题; org.slf4j.Logger 接口支持的标记对象为采用高级日志记录系统铺平了道路, 如果需要, 仍然可以切换回更传统的日志记录系统
##### 使用 SLF4J 时, 是否必须重新编译应用程序才能切换到其他日志记录系统 ?
TODO
##### SLF4J 的要求是什么
TODO
##### SLF4J 的 1.8.0 版本有什么变化 ?
TODO
##### SLF4J 是否向后兼容 ?
TODO
##### 使用SLF4J时, 收到 IllegalAccessError 异常; 这是为什么 ?
TODO
##### 为什么 SLF4J 使用 X11 类型许可证而不是 Apache软件许可证 ?
SLF4J 使用 X11 类型许可证而不是 ASL 或 LGPL 许可证; 因为 Apache 软件基金会和自由软件基金会认为 X11 许可证与其各自的许可证兼容
##### 可以从哪里获得特定的 SLF4J 绑定 ?
TODO
##### 库是否应该尝试配置日志记录 ?
TODO
##### 为了减少软件的依赖性, 我们希望 SLF4J 成为可选的依赖项; 这是一个好主意吗 ?
TODO
##### 那么 Maven 传递性依赖 ?
作为使用 Maven 构建的库的开发者, 你可能希望使用绑定测试应用程序, 例如 slf4j-log4j12 或 logback-classic, 而不强制使用 log4j或 logback-classic 作为对用户的依赖, 这很容易实现;  
鉴于你的库的代码依赖于 SLF4J API, 你需要将 slf4j-api 声明为编译时 (默认范围) 依赖项
```
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
  <version>1.8.0-beta4</version>
</dependency>
```
限制测试中使用的 SLF4J 绑定的传递性可以通过将 SLF4J 绑定依赖性的范围声明为 "test" 来实现; 这是一个例子
```
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
  <version>1.8.0-beta4</version>
  <scope>test</scope>
</dependency>
```
因此, 就你的用户而言, 你应该导出 slf4j-api 作为库的传递依赖, 而不是任何 SLF4J 绑定或任何底层日志记录系统  
请注意, 从 SLF4J-1.6 版本开始, 在没有 SLF4J 绑定的情况下, slf4j-api 将默认为 NOP 实现
##### 如何在 Maven 中排除 commons-logging 依赖项 ?
###### 显示排除
许多使用 Maven 的软件项目将 commons-logging 声明为依赖项; 因此, 如果你希望迁移到 SLF4J 或使用 jcl-over-slf4j, 则需要在所有项目的依赖项中排除 commons-logging, 这些依赖项可传递地依赖于 commons-logging; Maven 文档中描述了依赖性排除, 对于分布在多个 pom.xml 文件上的多个依赖项显式排除 commons-logging 可能是一个麻烦且相对容易出错的过程
###### 使用 provided 范围
通过在项目的 pom.xml 文件中的使用 `provided` 范围声明它, 可以相当简单方便地将 commons-logging 依赖项排除; 实际的 commons-logging 将由 jcl-over-slf4j 提供; 即以下 pom.xml 文件片段
```
<dependency>
  <groupId>commons-logging</groupId>
  <artifactId>commons-logging</artifactId>
  <version>1.1.1</version>
  <scope>provided</scope>
</dependency>

<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>jcl-over-slf4j</artifactId>
  <version>1.8.0-beta4</version>
</dependency>
```
第一个依赖的声明表示 commons-logging 将 "以某种方式" 由你的环境提供; 第二个声明把 jcl-over-slf4j 包含到你的项目中; 由于 jcl-over-slf4j 是 commons-logging 的完美二进制兼容替代品, 因此第一个断言为真  
不幸的是, 虽然以 `provided` 范围声明 commons-loggin 可以完成工作, 但是 IDE, 例如 Eclipse 仍会将 commons-logging.jar 放在 IDE 所见的项目类路径上; 你需要确保在 IDE 的 commons-logging.jar 之前可以看到 jcl-over-slf4j.jar
###### 使用空构件
另一种方法是依赖于空的 commons-logging.jar 构件; 这种聪明的方法首先被由 Erik van Oosten 想象并且最初支持  
空构建可从 `http://version99.qos.ch` 获得, 这是一个高可用性 Maven 存储库, 可在位于不同地理区域的多个主机上复制  
以下声明将 version99 存储库添加到 Maven 搜索的远程存储库集中; 此存储库包含 commons-logging 和 log4j 的空工件
```
<repositories>
  <repository>
    <id>version99</id>
    <!-- highly available repository serving empty artifacts -->
    <url>http://version99.qos.ch/</url>
  </repository>
</repositories>
```
在项目的 `<dependencyManagement>` 部分中声明 commons-logging 的 99-empty 版本将指示 commons-logging 的所有传递依赖项导入 99-empty 版本, 从而很好地解决了 commons-logging 排除问题; 公共日志记录的类将由 jcl-over-slf4j 提供; 以下行声明了 commons-logging 的 99-empty 版本 (在依赖关系管理部分中) 并将 jcl-over-slf4j 声明为依赖项
```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>commons-logging</groupId>
      <artifactId>commons-logging</artifactId>
      <version>99-empty</version>
    </dependency>
    ... other declarations...
  </dependencies>
</dependencyManagement>

<!-- 不要忘记在依赖项部分中声明对 jcl-over-slf4j 的依赖 -->
<!-- 请注意, commons-logging 的依赖项将以传递性依赖项导入, 不必自己声明 -->
<dependencies>
  <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
    <version>1.8.0-beta4</version>
  </dependency>
  ... other dependency declarations
</dependencies>
```

#### 关于 SLF4J API
##### 为什么 Logger 接口中的打印方法不接受Object类型的消息, 而只接受String类型的消息 ?
TODO
##### 可以在没有附带消息的情况下记录异常吗 ?
TODO
##### (不) 记录的最快方法是什么 ?
SLF4J 支持称为参数化日志记录的高级功能, 可以显着提高禁用日志记录语句的日志记录性能; 对于一些 Logger 记录器记录:
```
logger.debug("Entry number: " + i + " is " + String.valueOf(entry[i]));
```
会有构造消息参数的成本, 无论是否记录消息都将整数 i 和 entry [i] 转换为 String, 并连接中间字符串  
避免参数构造成本的一种可行的方法是使用判断包围日志语句, 以下是一个例子
```
if(logger.isDebugEnabled()) {
  logger.debug("Entry number: " + i + " is " + String.valueOf(entry[i]));
}
```
这样, 如果对记录器禁用调试, 则不会产生参数构造的成本; 另一方面, 如果为 DEBUG 级别启用了记录器, 则会产生评估记录器是否启用的成本两次: 一次在 debugEnabled 中, 一次在 debug 中; 这是一个无关紧要的开销, 因为评估记录器所花费的时间不到实际记录语句所需时间的 1%  
更好的是使用参数化消息; 存在一个基于消息格式的便捷替代方案, 假设 entry 是一个对象, 你可以写:
```
Object entry = new SomeObject();
logger.debug("The entry is {}.", entry);
```
在评估是否记录之后, 并且仅当决策是肯定的时, 记录器实现将格式化消息并用条目的字符串值替换 "{}" 对; 换句话说, 在禁用日志语句的情况下, 此语句不会产生参数构造的成本  
以下两行将产生完全相同的输出; 但是, 在禁用日志记录语句的情况下, 第二种形式的性能将比第一种形式的性能至少提高 30 倍
```
logger.debug("The new entry is "+entry+".");
logger.debug("The new entry is {}.", entry);
```
也可以使用两个参数, 例如可以写
```
logger.debug("The new entry is {}. It replaces {}.", entry, oldEntry);
```
如果需要传递三个或更多参数, 则可以使用打印方法的 Object... 变体, 例如可以写
```
logger.debug("Value {} was inserted between {} and {}.", newVal, below, above);
```
这种形式导致隐藏的构造 Object [] (对象数组) 的成本通常非常小; 一个和两个参数变体不会产生这种隐藏的成本, 并且仅仅因为这个原因而存在 (效率); Object... 变体使得 slf4j-api 更小/更干净; 另外还支持数组类型参数, 包括多维数组  s
SLF4J 使用自己的消息格式化实现, 这与 Java 平台的格式不同; 这是合理的, 因为 SLF4J 的实现速度提高了大约 10 倍, 但代价是非标准且灵活性较低
TODO

##### 如何记录单个 (可能是复杂的) 对象的字符串内容 ?
TODO
##### 为什么 org.slf4j.Logger 接口没有 FATAL 级别的方法 ?
TODO
##### 为什么 TRACE 级别仅在 SLF4J 版本 1.4.0 中引入 ?
TODO
##### SLF4J 日志 API 是否支持 I18N (国际化) ?
TODO
##### 是否可以在不通过 LoggerFactory 中的静态方法的情况下检索记录器 ?
TODO
##### 在存在 exception/throwable 的情况下, 是否可以参数化日志语句 ?

#### 事项 SLF4J API
##### 如何使我的日志框架与 SLF4J 兼容 ?
TODO
##### 我的日志系统如何添加对 Marker 接口的支持 ?
TODO

#### 关于日志记录的通用问题
TODO

>**参考:**  
[Frequently Asked Questions about SLF4J](https://www.slf4j.org/faq.html#what_is)
