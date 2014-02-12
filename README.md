DDDLib
======

[DDDLib](http://www.dayatang.org/dddlib/)是一个领域驱动设计（Domain Driven Design，简称DDD）类库，它的目的是：
* 为基于DDD的开发范式提供基本的接口和抽象，实现一致性。
* 支持业务代码和技术代码分离。
* 隔离业务代码对对IoC容器和持久化框架等等基础设施的依赖。可以自由切换IoC容器（Spring、Guice、TapestryIoC等）和持久化框架（JPA，Hibernate等）的实现。
* 提供程序设计中经常用到的工具，例如Excel导入导出、系统配置、规则引擎封装，等等。
 
主要模块结构如下：
* dddlib-domain：整个DDDLib的核心。在用户项目中作为编译时依赖存在，就是说，用户项目在编码时要实现、继承或使用dddlib-domain中的接口和类。
* dddlib-ioc-spring：实现对Spring Ioc容器的封装，为用户项目提供依赖注入能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-ioc-spring中的接口和类。
* dddlib-ioc-guice：实现Google Guice IoC容器的封装，为用户项目提供依赖注入能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-ioc-spring中的接口和类。
