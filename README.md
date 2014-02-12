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
* dddlib-ioc-tapestry：实现TapestryIoC容器的封装，为用户项目提供依赖注入能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-ioc-tapestry中的接口和类。
* dddlib-persistence-jpa：使用JPA作为后端的持久化实现技术，为用户项目提供持久化能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-persistence-jpa中的接口和类。
* dddlib-persistence-hibernate：使用Hibernate作为后端持久化技术，为用户项目提供持久化能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-persistence-hibernate中的接口和类。
* dddlib-query-channel：查询通道模块，为分页查询提供扩展的功能。如果用户项目需要使用此功能，必须把该模块添加为编译时依赖。
* dddlib-utils：通用工具类模块。提供各种方便的工具类来操作JavaBean、数组、集合、日期、日志等等。其中最重要的一个类是断言类Assert。可以使用Assert进行防御式编程，提高系统的健壮性和可靠性。本模块为dddlib-domain所依赖，因此用户项目对该模块有编译时依赖。
* 