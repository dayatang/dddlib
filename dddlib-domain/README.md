DDDLIB-DOMAIN
======

[dddlib-domain模块是DDDLib的核心模块。这是用户项目中所必须的唯一编译时dddlib依赖（由于dddlib-domain也依赖于dddlib-utils，用户项目对dddlib-utils有传递性依赖）。IoC模块和持久化模块只是在集成时选择部署，用户项目不直接依赖于这些模块。

## 目标
dddlib-domain的主要目标是为基于领域驱动设计（Domain Driven Design, 简称DDD）范式的开发提供支持。在分层企业应用架构（表示层、应用层、领域层、基础设施层，其中应用层和领域层往往合称业务逻辑层）中，dddlib-domain直接为领域层服务，为领域对象提供统一的接口和基类，并通过依赖倒置（DI）的方式隔离领域层对基础设施层的IoC容器和持久化技术的依赖。

## API概述
dddlib-domain的API大概可分为三大类：

### 实体接口和基类
包括：
* [Domain](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/Entity.html)接口：代表DDD中的“实体”概念。实体是领域模型中的最重要的组成部分，对应到业务领域中的业务实体，例如员工、机构、岗位、账户、商品、财产，等等。所有的实体都必须实现Entity接口。同一种类型的实体通过唯一性的id属性相互区分。
* [BaseEntity](http://www.dayatang.org/dddlib/apidocs/org/dayatang/domain/BaseEntity.html)抽象类：BaseEntity为实体类提供一个抽象基类，它提供了持久化的能力，使得实体能够通过仓储（）
