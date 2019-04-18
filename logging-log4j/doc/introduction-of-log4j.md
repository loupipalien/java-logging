### Log4j 介绍

#### 生命的尽头
TODO

#### Log4j 的简短介绍: Ceki Gülcü, March 2002
TODO

#### 概要
本文档叙述了 log4j API 其独特的功能和设计理念; log4j 是基于许多作者的工作的开源项目; 它允许开发人员控制以任意粒度输出哪些日志语句; 它在运行时使用外部配置文件完全可配置; 最重要的是 log4j 具有温和的学习曲线; 注意：从用户反馈来看, 它非常吸引人

#### 介绍
TODO

#### Loggers, Appenders, Layouts
Log4j 有三个主要组件: Loggers, Appenders, Layouts; 这三种类型的组件协同工作, 根据消息类型和级别制定消息, 并在运行时控制这些消息的格式化以及报告的位置

##### Logger 的层次结构
相对于 `System.out.println`, 日志记录的 API 的第一个也是最重要的功能在于它能够禁用某些日志语句, 同时允许其他语句不受阻碍地打印; 此功能根据开发人员中使用的某些条件进行分类, 假定日志记录有空间 (即所有可能的日志记录语句的空间; 此观察结果被用作包装的核心概念; 但自 log4j 1.2 版以来, Logger 类已经取代了 Category 类; 对于熟悉早期版本的 log4j 的人来说, 可以将 logger 类可以被视为 Category 类的别名  
Loggers 是命名实体, 名称区分大小写, 它们遵循分层命名规则:
```
命名层次结构
如果记录器后跟一个点是后代记录器名称的前缀, 则称该记录器是另一个记录器的祖先; 如果记录器与后代记录器之间没有祖先, 则称该记录器是子记录器的父节点
```
例如, 名为 "com.foo" 的 Logger 是名为 "com.foo.Bar" 的 Logger 的父级; 类似地, "java" 是 "java.util" 的父级和 "java.util.Vector" 的祖先; 大多数开发人员都应该熟悉这种命名方案  
根记录器位于记录器层次结构的顶部, 它在以下两个方面是特殊的
- 它总是存在的
- 它不能按名称搜索

调用类静态 [Logger.getRootLogger](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Logger.html#getRootLogger) 方法检索它; 使用静态 [Logger.getLogger](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Logger.html#getLoggerjava.lang.String) 方法实例化和检索所有其他记录器; 此方法将所需记录器的名称作为参数; 下面列出了 Logger 类中的一些基本方法
```
package org.apache.log4j;

public class Logger {

  // Creation & retrieval methods:
  public static Logger getRootLogger();
  public static Logger getLogger(String name);

  // printing methods:
  public void trace(Object message);
  public void debug(Object message);
  public void info(Object message);
  public void warn(Object message);
  public void error(Object message);
  public void fatal(Object message);

  // generic printing method:
  public void log(Level l, Object message);
}
```
Loggers 可以指定为级别, 可能的级别集合是 [TRACE, DEBUG, INFO, WARN, ERROR, FATAL]; 这些被定义在 [org.apache.log4j.Level](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Level.html) 类中; 虽然我们不鼓励这样做, 但你仍可以通过对级别类进行子类化来定义自己的级别; 稍后将解释更好的方法  
如果给定的记录器没有分配级别, 那么它是具有最近的祖先的指定级别
```
级别继承
给定记录器 C 的继承级别等于记录器层次结构中的第一个非 null 级别, 从 C 开始并在层次结构中向上朝直到 root 记录器
```
