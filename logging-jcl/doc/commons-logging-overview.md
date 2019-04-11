### 概览

#### Logging 组件
当编写库时, 日志记录信息非常有用; 但有许多日志记录实现, 并且不应该在库所属的整个应用程序上强制使用特定的日志实现  
Logging 包是不同日志记录实现之间的超简桥接; 使用 commons-logging API 的库可以在运行时与任何日志记录实现一起使用;  commons-logging 支持许多流行的日志记录实现, 为其他人编写适配器是一项相当简单的任务  
应用程序 (而不是库) 也可以选择使用 commons-logging; 虽然日志记录实现独立性对于应用程序而言并不像库那样重要, 但使用 commons-logging 可允许应用程序在不重新编译代码的情况下更改为不同的日志记录实现  
请注意, commons-logging 不会尝试初始化或终止在运行时使用的底层日志记录实现; 这是应用程序的责任, 然而许多流行的日志记录实现会自动初始化; 在这种情况下, 应用程序能够避免包含特定于所使用的日志记录实现的任何代码

#### 文档
TODO

#### 发行版
TODO

#### 开发构建
TODO

>**参考:**  
[Overview](https://commons.apache.org/proper/commons-logging/index.html)
