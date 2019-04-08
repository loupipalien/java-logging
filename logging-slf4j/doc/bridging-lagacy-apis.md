### 桥接遗留的 APIs
通常，你依赖的某些组件依赖于 SLF4J 以外的日志记录 API; 你或许也可以假设这些组件在不久的将来不会切换到 SLF4J; 为了解决这种情况, SLF4J 附带了几个桥接模块, 这些模块将对 log4j, JCL 和 java.util.logging API 的进行重定向, 就好像它们调用 SLF4J API 一样; 下图说明了这个想法  
请注意, 对于你控制下的源代码应该使用 [slf4j-migrator](https://www.slf4j.org/migrator.html); 本文描述的基于二进制的解决方案适用于你无法控制的软件  
![image](https://www.slf4j.org/images/legacy.png)  
#### 从 Jakarta Commons Logging (JCL) 逐步迁移到 SLF4J
##### jcl-over-slf4j.jar
