DDDLib
======
DDDLib is a Domain Driven Design(DDD for short) library。It aims to:

* Provide basic interface and abstract way for DDD development paradigms with great consistency.
* Support the separation of responsibilities between business code and technical code. Make Domain layer express business concept and business rule purely and take specific technology outside.
* Isolate business code from infrastructure as IoC container and persistence repository, etc.
* Reduce the burden of developer's work, lighten their "Understanding Weight". Most of developers just need to
understand dddlib-domain module, and only some classes which is included in dddlib-domain: Entity、EntityRepository、InstanceFactory and 4 kinds of Query class.
* Provide some common utils for development, such as Exporting/Importing Excel, Configuration Service, Rule Engine wrapper and so on.

## Layout of Modules

DDDLib have multiple modules about ten more. We group them into 4 categories roughly: core module, IoC module, persistance module and periphery module. When developer coding, they need dependent code module directly, and choose a IoC module and a persistance module while deploy it. Periphery module would be convenience for you although it not necessary.

Why DDDLib can lighten our "Understanding Weight" for projects, as most of developer at project, they just need to know core module, even only a few interfaces and classes.


### Core Codule
* dddlib-domain: The core of entire DDDLib. It's a compile-time dependency that you need to implement its interfaces and its extends classes.


### IoC Module: provide an ability for IoC
dddlib'IoC module is just a IoC interface. You can choose one of them as your favourite:

* dddlib-ioc-spring: using Spring IoC
* dddlib-ioc-guice: using Google guice
* dddlib-ioc-tapestry: using Tapestry


### Persistence Module: provide an ability for persistence
There're 2 impletations for dddlib-persistence:

* dddlib-persistence-jpa: Using JAP as back-end to implement persistence.
* dddlib-persistence-hibernate: Using Hibernate as back-end to implement persistence.
* dddlib-persistence-test: A test module for other persist module.

The following module is related to persistence, but it's on top of other persist module:

* dddlib-query-channel：provite an extension point for paging query.


### Utils Module
* dddlib-utils: Provide some common utils for processing JavaBean, Array, Collection, Date, Log and so on. The most important of those is Assert class.

* dddlib-configuration: private a unified API and it's implementation for configurating which kind of key-value pair. Currently, configuration source can be from: system file, class path file, database, remote URL. This configuration source can be read/write by dddlib-configuration API except that kind of remote URL just can be read.

* dddlib-cache: Provide a ability for cache implemented by memcached. In the feature, it will be divided to two module: API and some kinds of implementation module which can be redis, encache and so on.

* dddlib-datasource-monitor: Monitoring for the status of datasource on runtime.

* dddlib-datasource-router: Provide a router for datasource what mainly for SaaS route different tenants to different server or database. Other sides, I did some optimizations against MySQL what is implementing R/W Splitting.

* dddlib-datasource-saas: A new SaaS datasource for routing different tenants to his/her specific database. Provide some kinds of strategy for router(e.x. different tenants's database with different IP, port, the name of database, instance, jndi). Meanwhile, support a variety of database server and  database connection pool.

* dddlib-db: A utils for the connection of database, including BTM, DBUnit, the manager of MySQL and Oracle.

* dddlib-excel: Provide a ability for processing Excel file(.xls, .xlsx) which implemented by Apache POI.

* dddlib-i18n: Internationalization and localization module.

* dddlib-observer: A implementation for observer design pattern on entity level. While something happened on an monitored entity, it's observer will get a event message.

* dddlib-rule-engine: Provide API and it's implementation for stateful and stateless rule service which base on JSR94 what's the specification of rule engine.



## Example
[hrm-demo](https://github.com/dayatang/hrm-demo) is an example project using DDDLib.

=========
This document is just a big picture for DDDLib. The details for each sub module is under their fold's README.md
