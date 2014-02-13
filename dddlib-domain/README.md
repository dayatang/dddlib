DDDLIB-DOMAIN
======

[dddlib-domain模块是DDDLib的核心模块。这是用户项目中所必须的唯一编译时dddlib依赖（由于dddlib-domain也依赖于dddlib-utils，用户项目对dddlib-utils有传递性依赖）。IoC模块和持久化模块只是在集成时选择部署，用户项目不直接依赖于这些模块。

## 目标
dddlib-domain的主要目标是为基于领域驱动设计（Domain Driven Design, 简称DDD）范式的开发提供支持。在分层企业应用架构（表示层、应用层、领域层、基础设施层，其中应用层和领域层往往合称业务逻辑层）中，dddlib-domain直接为领域层服务，为领域对象提供统一的接口和基类，并通过依赖倒置（DI）的方式隔离领域层对基础设施层的IoC容器和持久化技术的依赖。

## API概述
dddlib-domain的主要API集中在org.dayatang.domain包中，大概可分为五部分：

### 实体和值对象

实体相关的接口和基类包括：

* [Domain](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/Entity.html)接口：代表DDD中的“实体”概念，等价于JPA中的Entity。实体是领域模型中的最重要的组成部分，对应到业务领域中的业务实体，例如员工、机构、岗位、账户、商品、财产，等等。所有的实体都必须实现Entity接口。同一种类型的实体通过唯一性的id属性相互区分。

* [BaseEntity](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/BaseEntity.html)抽象类：BaseEntity为实体类提供一个抽象基类，它提供了持久化的能力，使得实体能够通过仓储（[EntityRepository](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/EntityRepository.html)）接口持久化到数据库中。

* [AbstractEntity](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/AbstractEntity.html)抽象类：AbstractEntity抽象类继承[BaseEntity](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/BaseEntity.html)抽象类，为实体类提供一个更具体的、功能更多的基类。具体而言，它将id属性的数据类型限定为Long类型，提供version属性以支持持久化框架进行乐观锁并发控制，并提供了将自身保存到仓储，从仓储中删除自身的实例方法，以及根据id获取实体、查找本实体类型的所有实例、根据一个或多个属性查找实体等等静态方法。

* [ValueObject](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/ValueObject.html)接口：代表DDD中的“值对象”概念，等价于JPA中的Embeddable。值对象于实体的根本区别是：实体具有自己的标识（ID），可以独立存在，有自己独立的生命周期；而值对象没有自己的标识，不能独立存在，它是实体的一个组成部分，其生命周期依附于其所属的实体。

之所以区分BaseEntity和AbstractEntity两个实体基类，是为了满足软件开发中的两种不同的需求。如果严格遵循DDD的范式，那么只有聚合根实体才能够更改聚合内的实体的持久化状态，并不是每一个实体都能够自由save()和remove()自身的，否则可能打破聚合的规则不变性。在这种情况下，最好是实体类都从BaseEntity继承，由开发者精细定义实体类的持久化行为。

同时为了方便起见，也有很多开发者愿意在实体基类上实现很多公共行为，以最小化具体实体子类需要编写的方法接口的数量。他们或者没有聚合的概念，或者不忌讳打破聚合业务规则不变性。这种情况下，让实体类从AbstractEntity继承是个好的起点。

### 持久化

* [EntityRepository](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/EntityRepository.html)：这是持久化的核心接口，代表DDD中的“仓储”概念。实体以及从属于实体上的值对象通过仓储接口持久化到仓储（一般是数据库）里面，通过仓储接口可以以各种各样的方式查找实体。仓储接口功能上约等于JPA的EntityManager和Hibernate的Session。dddlib-persistence-jpa和dddlib-persistence-hibernate两个模块分别为该仓储接口提供了不同的实现。

dddlib支持四种查询方式：条件查询、命名查询、JPQL查询和原生SQL查询，可以分别用EntityRepository的createCriteriaQuery()、createJpqlQuery()、createNamedQuery()和createSqlQuery()方法创建。这些查询分别由下面的类和接口支持：

* [CriteriaQuery](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/CriteriaQuery.html)：条件查询。条件查询是通过面向对象而不是面向查询语言的方式进行查询。为了构造条件查询，我们提供了下面的接口和类：用[QueryCriterion](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/QueryCriterion.html)接口代表单个的查询条件（internal子包中有它的各种各样的实现类，分别代表不同的查询条件），用[CriterionBuilder](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/CriterionBuilder.html)作为QueryCriterion的工厂来生成各种查询条件，用[OrderSetting](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/OrderSetting.html)代表单个的排序字段。

* [JpqlQuery](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/JpqlQuery.html)：JPQL查询。JPQL查询支持通过JPA的对象查询语言JPQL进行查询。Hibernate本身也支持通过JPQL进行查询，JPA是持久化的规范，因此我们可以采用JPQL作为通用的查询语言来使用。

* [NamedQuery](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/NamedQuery.html)：命名查询。根据预定义的查询的名称来进行查询。

* [SqlQuery](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/SqlQuery.html)：原生SQL查询。根据原生SQL语句进行查询。查询的结果可以是标量的，也可以映射到一个实体类型。

除CriteriaQuery之外，其他三种查询往往需要指定查询参数。查询参数集由下面的接口和类代表：


* [QueryParameters](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/QueryParameters.html)：查询参数集接口，代表每个查询的一批参数。几种查询都支持两种形式的参数：定位参数和命名参数，在DDDLib中分别用QueryParameters的两个实现类[ArrayParameters](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/ArrayParameters.html)和[MapParameters](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/MapParameters.html)代表。注意：定位参数是落后的形式，建议统一采用命名参数的形式。

* [ArrayParameters](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/ArrayParameters.html)：代表定位参数集，如"... where e.name = ?"。在对象的内部用一个数组来保存参数。

* [MapParameters](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/MapParameters.html)：代表命名参数集，如"... where name = :name"。在对象内部用一个Map来保存参数，Key代表参数名，Value代表参数值。

每种查询都定义有下面两种方法：

* list()：返回一批结果，用List形式表示。
* singleResult()：返回单一结果。

除CriteriaQuery之外，其余三种查询还定义有下面的方法：

* executeUpdate()：执行数据库批量更新操作，返回收到影响的记录的数量。




### IoC依赖查找

DDDLib的依赖查找功能由InstanceFactory代表。它通过InstanceProvider策略接口将对象查找请求委托给具体的后端IoC容器，如Spring或Google Guice等。

* [InstanceFactory](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/InstanceFactory.html)：实例工厂，代表DDD中的“工厂”概念。它是IoC容器的门面，为系统中的其他类提供所需的依赖对象的实例。InstanceFactor顺序通过三种途径获取Bean实例。（1）如果已经给InstanceFactory设置了InstanceProvider，那么就通过后者 查找Bean；（2）如果没有设置InstanceProvider，或者通过InstanceProvider无法找到Bean，就通过JDK6的ServiceLoader机制查找（通 过在类路径或jar中的/META-INF/services/a.b.c.Abc文件中设定内容为x.y.z.Xyz，就表明类型a.b.c.Abc将通过类x.y.z.Xyz 的实例提供）；（3）如果仍然没找到Bean实例，那么将返回那些通过bind()方法设置的Bean实例。（4）如果最终仍然找不到，就抛出 IocInstanceNotFoundException异常。

* [InstanceProvider](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/InstanceProvider.html)：实例提供者接口。这是一个策略接口，封装了IoC的功能。DDDLib的另外三个模块dddlib-ioc-spring，dddlib-ioc-guice和dddlib-ioc-tapestry分别为该接口提供了不同的实现类，将Bean实例请求适配到具体的IoC容器，如SpringIoC、Google Guice和TapestryIoC等。




### 值和数据类型

在很多业务领域中(尤其是在SaaS的环境下)，为了适应不同类型租户的需要，除了定义实体类共同的静态属性（即JavaBean属性）之外，每个租户或用户往往需要定义一批自己专用的动态属性，例如给员工Employee类加入Date类型的"转正日期"和String类型的“护照编号”属性。DDDLib对此提供下面的API支持：

* [Value](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/Value.html)：代表一个值类型，可以用来代表一个动态属性值。它包括两部分：代表数据类型的[DataType](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/DataType.html)和代表具体内容的字符串值。

* [DataType](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/DataType.html)：数据类型枚举。代表值的数据类型。它负责将字符串形式的值转换为指定的数据类型。


### 领域规范

org.dayatang.domain.specification包中的接口和类代表DDD中“规范”的概念。每种规范都针对一种类型的领域对象，用来判断该类型的对象实例是否满足此规范。例如在航运业务中判断某航线是否满足最低费用规范或最短时间规范。规范之间可以执行与、或、非操作。

* [Specification](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/specification/Specification.html)：规范接口。所有的领域规范都必须实现此接口。

* [AbstractSpecification](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/specification/AbstractSpecification.html)：抽象规范，用来实现规范间的与、或、非操作。

* [AndSpecification](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/specification/AndSpecification.html)：“与”规范，代表两个规范的“与”操作的结果。

* [OrSpecification](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/specification/OrSpecification.html)："或"规范，代表两个规范的“或”操作的结果。

* [NotSpecification](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/specification/NotSpecification.html)：“非”规范，代表当前规范的“非”操作的结果。



## 其他内容

### org.dayatang.domain.internal包

internal包主要包含代表各种查询条件的QueryCriterion接口的实现类，它们属于DDDLib的内部实现，DDDLib的使用者和扩展这既不需要了解、也不能依赖于这个包中的内容。


