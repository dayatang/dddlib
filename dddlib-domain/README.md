DDDLIB-DOMAIN
======

[dddlib-domain模块是DDDLib的核心模块。这是用户项目中所必须的唯一编译时dddlib依赖（由于dddlib-domain也依赖于dddlib-utils，用户项目对dddlib-utils有传递性依赖）。IoC模块和持久化模块只是在集成时选择部署，用户项目不直接依赖于这些模块。

## 目标
dddlib-domain的主要目标是为基于领域驱动设计（Domain Driven Design, 简称DDD）范式的开发提供支持。在分层企业应用架构（表示层、应用层、领域层、基础设施层，其中应用层和领域层往往合称业务逻辑层）中，dddlib-domain直接为领域层服务，为领域对象提供统一的接口和基类，并通过依赖倒置（DI）的方式隔离领域层对基础设施层的IoC容器和持久化技术的依赖。
