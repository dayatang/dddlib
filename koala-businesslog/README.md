koala业务日志子系统
----
## 解决问题
现实项目中，如果将业务日志记录的实现代码放置在业务代码中，会带来难于维护、代码职责不分明、
有碍代码的可读性等问题，举例如下：


        public class ContractApplicationImpl{
            //合同
            public void addContract(Contract contract, Project project, Person person){
                 contract.setProject(project);
                 //持久此合同
                 contract.save();
                 Log.save("合同 " + contract.getName() + " 被 " + person.getName() + " 加入到项目" + project.getName() + '中' );
            }
        }


## 如何解决
koala业务日志子系统将业务日志的格式，渲染，持久化等逻辑与业务逻辑分离。
默认的解决方案是使用spring的AOP实现，对你指定需要拦截的方法，找到相应的业务日志模板，进行渲染，并持久化。

最终，你的业务代码只是包含业务：

        public class ContractApplicationImpl{
            public void addContract(Contract contract, Project project, Person person){
                 contract.setProject(project);
                 contract.save();
            }
        }

## koala业务日志子系统原理







## 使用方法

1. 加入依赖

        <dependency>
            <groupId>org.openkoala.businesslog</groupId>
            <artifactId>koala-businesslog-api</artifactId>
            <version>2.1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.openkoala.businesslog</groupId>
            <artifactId>koala-businesslog-impl</artifactId>
            <version>2.1.0-SNAPSHOT</version>
        </dependency>

1. 加入koala-businesslog.properties文件到classpath中

`koala-businesslog.properties`用于配置业务日志子系统的参数，以及指定你需要拦截的业务方法

内容如下：

        #指定需要拦截的业务方法，使用的是spring的aop拦截表达式
        pointcut=execution(* business.*Application.*(..))

        #日志开关
        kaola.businesslog.enable=true

        #日志配置适配器
        businessLogConfigAdapter=org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter

        #关联查询执行器
        businessLogQueryExecutor=org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor

        #日志模板渲染器
        businessLogRender=org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender

        #日志导出器    BusinessLogConsoleExporter     BusinessLogJpaDefaultExporter
        businessLogExporter=org.openkoala.businesslog.impl.BusinessLogConsoleExporter

        ##数据库设置
        org.db.jdbc.driver=${db.jdbcDriver}
        org.db.jdbc.connection.url=${db.connectionURL}
        org.db.jdbc.username=${db.username}
        org.db.jdbc.password=${db.password}
        org.db.jdbc.dialect=${hibernate.dialect}
        org.hibernate.hbm2ddl.auto=${hibernate.hbm2ddl.auto}
        org.hibernate.show_sql=${hibernate.show_sql}
        org.db.Type=${db.Type}
        db.generateDdl=${db.generateDdl}
        org.maximumConnectionCount=50
        org.minimumConnectionCount=10

        #线程池配置
        #核心线程数
        log.threadPool.corePoolSize=10
        # 最大线程数
        log.threadPool.maxPoolSize=50
        #队列最大长度
        log.threadPool.queueCapacity=1000
        #线程池维护线程所允许的空闲时间
        log.threadPool.keepAliveSeconds=300
        #线程池对拒绝任务(无线程可用)的处理策略
        log.threadPool.rejectedExecutionHandler=java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy


1. 配置业务模块及关联查询:在classpath路径下新建koala-log-conf目录，此目录所有的xml文件都将并koala业务日志子系统的默认实现读取。

配置文件的格式为：

        <?xml version="1.0" encoding="UTF-8"?>
        <businessLogConfigs>
            <businessLogConfig>
                <category>项目操作</category>
                <method>Project[] business.ProjectApplicationImpl.findSomeProjects(List)</method>
                <template><![CDATA[
                查到项目
                <#list names as name>
                  ${name},
                </#list>
                ]]></template>
                <queries>
                    <query key="names">
                        <target class="business.Project"></target>
                        <method>getProjectsName(business.Project[])</method>
                        <args>
                            <arg>${_methodReturn}</arg>
                        </args>
                    </query>
                </queries>
            </businessLogConfig>
              <businessLogConfig>
                    <!--向项目中的合同，添加一张发票-->
                    <category>发票操作</category>
                    <method>Invoice business.InvoiceApplicationImpl.addInvoice(String,long)</method>
                    <template><![CDATA[向项目${project.name}的合同${contract.name}添加发票：${(_methodReturn.sn)!""}]]></template>
                    <queries>
                        <query key="contract">
                            <target class="business.ContractApplicationImpl">contractApplication</target>
                            <method>findContractById(long)</method>
                            <args>
                                <arg>${_param1}</arg>
                            </args>
                        </query>
                        <query key="project">
                            <target class="business.Project"></target>
                            <method>findByContract(business.Contract)</method>
                            <args>
                                <arg>${contract}</arg>
                            </args>
                        </query>
                        <query key="ok">
                            <target class="business.ProjectApplicationImpl">projectApplication</target>
                            <method>addProject()</method>
                        </query>

                    </queries>
                </businessLogConfig>
        </businessLogConfigs>




## 如何扩展
扩展有两种情况：对koala业务日志子系统的默认实现进行扩展、对koala业务日志子系统的API进行扩展。下面将分别说明。

1. 对koala业务日志子系统的默认实现进行扩展








