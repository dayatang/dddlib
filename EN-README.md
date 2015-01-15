DDDLib
======

[DDDLib](http://www.dayatang.org/dddlib/)是一个领域驱动设计（Domain Driven Design，简称DDD）类库，它的目的是：
* 为基于DDD的开发范式提供基本的接口和抽象，实现一致性。
* 支持业务代码和技术代码分离。使领域层代码纯粹表达业务概念和业务规则，将具体技术隔离出去。
* 隔离业务代码对对IoC容器和持久化框架等等基础设施的依赖。可以自由切换IoC容器（Spring、Guice、TapestryIoC等）和持久化框架（JPA，Hibernate等）的实现。
* 减轻开发人员的工作负担，降低开发人员的“概念重量”。绝大多数开发人员只需要了解dddlib-domain模块，
而且只需要了解dddlib-domain中的几个类：Entity、EntityRepository、InstanceFactory和四种查询对象。
* 提供程序设计中经常用到的工具，例如Excel导入导出、系统配置、规则引擎封装，等等。

DDDLib is a Domain Driven Design(DDD for short) library。It aims to:

* Provide basic interface and abstract way for DDD development paradigms with great consistency.
* Support the separation of responsibilities between business code and technical code. Make Domain layer express business concept and business rule purely and take specific technology outside.
* Isolate business code from infrastructure as IoC container and persistence repository, etc.
* Reduce the burden of developer's work, lighten their "Understanding Weight". Most of developers just need to
understand dddlib-domain module, and only some classes which is included in dddlib-domain: Entity、EntityRepository、InstanceFactory and 4 kinds of Query class.
* Provide some common utils for development, such as Exporting/Importing Excel, Configuration Service, Rule Engine wrapper and so on.




## 模块结构

DDDLib是一个多模块Maven项目，一共有十多个模块，可以粗略划分为核心模块、IoC模块、持久化模块和外围模块三大类。
用户项目编码时需要直接依赖核心模块，在部署时选定一个IoC模块和一个持久化模块，如果有必要时可以选用一些外围模块。
对于项目中的大部分开发人员，都只需要了解核心模块dddlib-domain，
甚至只是dddlib-domain中的少数的几个接口和类，所以可以大大降低“概念重量”。

## Layout of modules

DDDLib have multiple modules about ten more. We group them into 4 categories roughly: core module, IoC module, persistance module and periphery module. When developer coding, they need dependent code module directly, and choose a IoC module and a persistance module while deploy it. Periphery module would be convenience for you although it not necessary.

Why DDDLib can lighten our "Understanding Weight" for projects, as most of developer at project, they just need to know core module, even only a few interfaces and classes.

### 核心模块
* dddlib-domain：整个DDDLib的核心。在用户项目中作为编译时依赖存在，就是说，用户项目在编码时要实现、
继承或使用dddlib-domain中的接口和类。

### Core Codule
* dddlib-domain: The core of entire DDDLib. It's a compile-time dependency that you need to implement its interfaces and its extends classes.


### IoC模块
* dddlib-ioc-spring：实现对Spring Ioc容器的封装，为用户项目提供依赖注入能力。
在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、
继承或使用dddlib-ioc-spring中的接口和类。

* dddlib-ioc-guice：实现Google Guice IoC容器的封装，为用户项目提供依赖注入能力。在用户项目中作为可选的运行时依赖存在，
用户项目在编码时不需要实现、继承或使用dddlib-ioc-spring中的接口和类。

* dddlib-ioc-tapestry：实现TapestryIoC容器的封装，为用户项目提供依赖注入能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-ioc-tapestry中的接口和类。

### IoC Module: provide an ability for IoC
dddlib'IoC module is just a IoC interface. You can choose one of them as your favourite:

* dddlib-ioc-spring: using Spring IoC
* dddlib-ioc-guice: using Google guice
* dddlib-ioc-tapestry: using Tapestry



### 持久化模块

dddlib-persistence是持久化模块。它包含下面三个子模块：

* dddlib-persistence-jpa：使用JPA作为后端的持久化实现技术，
为用户项目提供持久化能力。在用户项目中作为可选的运行时依赖存在，
用户项目在编码时不需要实现、继承或使用dddlib-persistence-jpa中的接口和类。

* dddlib-persistence-hibernate：使用Hibernate作为后端持久化技术，为用户项目提供持久化能力。在用户项目中作为可选的运行时依赖存在，用户项目在编码时不需要实现、继承或使用dddlib-persistence-hibernate中的接口和类。

* dddlib-persistence-test：一个测试模块，为其余的持久化模块提供测试支持。

下面的模块与持久化有关，但作为顶级的模块存在：


* dddlib-query-channel：查询通道模块，为分页查询提供扩展的功能。如果用户项目需要使用此功能，必须把该模块添加为编译时依赖。

### Persistence Module: provide an ability for persistence
There're 2 impletations for dddlib-persistence:

* dddlib-persistence-jpa: Using JAP as back-end to implement persistence.
* dddlib-persistence-hibernate: Using Hibernate as back-end to implement persistence.
* dddlib-persistence-test: A test module for other persist module.

The following module is related to persistence, but it's on top of other persist module:

* dddlib-query-channel：provite an extension point for paging query.


### 外围模块
### Utils module

* dddlib-utils：通用工具类模块。提供各种方便的工具类来操作JavaBean、数组、集合、日期、日志等等。
其中最重要的一个类是断言类Assert。
可以使用Assert进行防御式编程，提高系统的健壮性和可靠性。本模块为dddlib-domain所依赖，因此用户项目对该模块有编译时依赖。

* dddlib-utils: Provide some common utils for processing JavaBean, Array, Collection, Date, Log and so on. The most important of those is Assert class.


* dddlib-configuration：为从各种来源读取键值型配置信息提供统一的接口和实现类。
目前提供了能够从以下各种来源读取配置信息的实现类：文件系统文件、类路径文件、数据库、远程url等等。
除了URL形式的实现是只读的之外，其他实现都是可读写的，能够把修改后的配置数据写回其来源中。

* dddlib-configuration: private a unified API and it's implementation for configurating which kind of key-value pair. Currently, configuration source can be from: system file, class path file, database, remote URL. This configuration source can be read/write by dddlib-configuration API except that kind of remote URL just can be read.

* dddlib-cache：缓存模块。为用户系统提供告诉缓存支持。目前采用memcached实现，
将来将划分为一个公共的API模块和几个实现模块，不同的实现模块采用不同的缓存技术实现，
如memcached、redis、ehcache等。
* dddlib-datasource-monitor：数据源监控模块。可以监控数据源的运行状态。
* dddlib-datasource-router：数据源路由模块。主要为SaaS应用服务，将不同的租户的数据库访问路由到不同的服务器或数据库。
本模块特别为MySQL进行了优化，除了实现数据库路由之外还实现了读写分离。


* dddlib-cache: Provide a ability for cache implemented by memcached. In the feature, it will be divided to two module: API and some kinds of implementation module which can be redis, encache and so on.

* dddlib-datasource-monitor: Monitoring for the status of datasource on runtime.

* dddlib-datasource-router: Provide a router for datasource what mainly for SaaS route different tenants to different server or database. Other sides, I did some optimizations against MySQL what is implementing R/W Splitting.


* dddlib-datasource-saas：新版本的SaaS数据源，将不同的租户的数据库访问路由到其专有的数据库。
可以采用多种路由策略（例如例如不同的租户的数据库拥有不同ip、不同端口、不同的数据库名称、不同的实例、不同的jndi，等等）。
支持各种各样的数据库服务器以及各种各样的数据库连接池。

* dddlib-datasource-saas: A new SaaS datasource for routing different tenants to his/her specific database. Provide some kinds of strategy for router(e.x. different tenants's database with different IP, port, the name of database, instance, jndi). Meanwhile, support a variety of database server and  database connection pool.

* dddlib-db: A utils for the connection of database, including BTM, DBUnit, the manager of MySQL and Oracle.

* dddlib-excel: Provide a ability for processing Excel file(.xls, .xlsx) which implemented by Apache POI.

* dddlib-i18n: Internationalization and localization module.

* dddlib-observer: A implementation for observer design pattern on entity level. While something happened on an monitored entity, it's observer will get a event message.

* dddlib-rule-engine: Provide API and it's implementation for stateful and stateless rule service which base on JSR94 what's the specification of rule engine.

=========
This document is just a big picture for DDDLib. The details for each sub module is under their fold's README.md.

## Example
[hrm-demo](https://github.com/dayatang/hrm-demo) is an example project using DDDLib.

* dddlib-db：为数据库访问提供专门的工具类，包括BTM事务管理器、DBUnit、MySQL和Oracle数据库管理器等等。
本模块主要为数据库集成测试提供支持。



* dddlib-excel：为读写Excel文件内容提供支持。采用Apache POI技术实现，支持.xls和.xlsx两种格式的Excel文件。

* dddlib-i18n：为国际化和本地化提供支持。

* dddlib-observer：在实体层面实现观察者模式实现。当一个实体发生某些事件时向注册的观察者实体发送通知。

* dddlib-rule-engine：为有状态和无状态规则服务提供接口和实现。实现是基于规则引擎规范jsr94的。

## 使用范例
github上的项目[hrm-demo](https://github.com/dayatang/hrm-demo)是一个使用DDDLib的范例项目。这个项目尚在开发中。

=========
各个子模块的详细说明请参阅具体模块的README.md文件
