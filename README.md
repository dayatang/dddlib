DDDLib
======

DDDLib是一个领域驱动设计（Domain Driven Design，简称DDD）类库，它的目的是：
* 为基于DDD的开发范式提供基本的接口和抽象，实现一致性。
* 为业务代码和技术代码分离提供支持。
* 隔离业务代码对对IoC容器和持久化框架等等基础设施的依赖。可以自由切换IoC容器（Spring、Guice、TapestryIoC等）和持久化框架（JPA，Hibernate等）的实现。
* 提供程序设计中经常用到的工具，例如Excel导入导出、系统配置、规则引擎封装，等等。
 
主要模块结构如下：
* dddlib-domain：整个DDDLib的核心。
* dddlib-ioc-spring：实现对Spring Ioc容器的封装。
* dddlib-ioc-guice：