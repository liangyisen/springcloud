# springcloud

# 1.Eureka 注册中心
Eureka作为服务注册与发现的组件
# 2.Ribbon 负载均衡
ribbon是一个负载均衡客户端，可以很好的控制htt和tcp的一些行为。Feign默认集成了ribbon。

# 3.Feign  httpclient (声明式的伪Http客户端)
Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。
简而言之：

Feign 采用的是基于接口的注解
Feign 整合了ribbon，具有负载均衡的能力
整合了Hystrix，具有熔断的能力 在D版本的Spring Cloud之后，它没有默认打开 `feign.hystrix.enabled=true`

# 4.hystrix 断路器
`Netflix has created a library called Hystrix that implements the circuit breaker pattern. In a microservice architecture it is common to have multiple layers of service calls.`
Netflix开源了Hystrix组件，实现了断路器模式，SpringCloud对这一组件进行了整合。 在微服务架构中，一个请求需要调用多个服务是非常常见的

 ![image](https://www.fangzhipeng.com/img/jianshu/2279594-08d8d524c312c27d.png)

 较底层的服务如果出现故障，会导致连锁故障。当对特定的服务的调用的不可用达到一个阀值（Hystric 是5秒20次） 断路器将会被打开

 ![image](https://www.fangzhipeng.com/img/jianshu/2279594-8dcb1f208d62046f.png)

 断路打开后，可用避免连锁故障，fallback方法可以直接返回一个固定值。
 
 # 5.Zuul 网关
 Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。
 - zuul有以下功能：
 1. Authentication
 1. Insights
 1. Stress Testing
 1. Canary Testing
 1. Dynamic Routing
 1. Service Migration
 1. Load Shedding
 1. Security
 1. Static Response handling
 1. Active/Active traffic management
 
 # 6.Spring Cloud Config 配置中心
 在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。
 
 ![image](https://www.fangzhipeng.com/img/jianshu/2279594-40ecbed6d38573d9.png)
 
 # 7.Spring Cloud Bus 将分布式系统的节点与轻量级消息代理链接(#依赖太多,不想搞了) 
 Spring Cloud Bus将分布式系统的节点与轻量级消息代理链接。这可以用于广播状态更改（例如配置更改）或其他管理指令。一个关键的想法是，Bus就像一个扩展的Spring Boot应用程序的分布式执行器，但也可以用作应用程序之间的通信渠道。当前唯一的实现是使用AMQP代理作为传输，但是相同的基本功能集（还有一些取决于传输）在其他传输的路线图上。
 
 ![image](https://www.fangzhipeng.com/img/jianshu/2279594-9a119d83cf90069f.png)
 
 # 8.Sleuth  服务追踪
 Spring Cloud Sleuth提供了分布式追踪(distributed tracing)的一个解决方案。其基本思路是在服务调用的请求和响应中加入ID，标明上下游请求的关系。利用这些信息，可以方便地分析服务调用链路和服务间的依赖关系。
 
 ![image](https://www.fangzhipeng.com/img/jianshu/2279594-4b865f2a2c271def.png)
 
 # 9.Hystrix Dashboard 监控
 
 在微服务架构中为例保证程序的可用性，防止程序出错导致网络阻塞，出现了断路器模型。断路器的状况反应了一个程序的可用性和健壮性，它是一个重要指标。Hystrix Dashboard是作为断路器状态的一个组件，提供了数据监控和友好的图形化界面。Hystrix Dashboard去监控断路器的Hystrix command
 
![image](https://www.fangzhipeng.com/img/2018/sc12-2.jpeg)

在界面依次输入：http://localhost:8762/actuator/hystrix.stream 、2000 、miya ；点确定。

在另一个窗口输入： http://localhost:8762/hi?name=forezp

重新刷新hystrix.stream网页，你会看到良好的图形化界面：

![image](https://www.fangzhipeng.com/img/2018/sc12-3.jpeg)

# 10.Hystrix Turbine 监控
当我们有很多个服务的时候，这就需要聚合所以服务的Hystrix Dashboard的数据了。这就需要用到Spring Cloud的另一个组件了，即Hystrix Turbine。
看单个的Hystrix Dashboard的数据并没有什么多大的价值，要想看这个系统的Hystrix Dashboard数据就需要用到Hystrix Turbine。Hystrix Turbine将每个服务Hystrix Dashboard数据进行了整合。Hystrix Turbine的使用非常简单，只需要引入相应的依赖和加上注解和配置就可以了。

# spring boot admin   
Spring Boot Admin是一个开源社区项目，用于管理和监控SpringBoot应用程序。 应用程序作为Spring Boot Admin Client向为Spring Boot Admin Server注册（通过HTTP）或使用SpringCloud注册中心（例如Eureka，Consul）发现。 UI是的AngularJs应用程序，展示Spring Boot Admin Client的Actuator端点上的一些监控。

- 常见的功能或者监控如下：
- 显示健康状况
- 显示详细信息，例如
    - JVM和内存指标
    - micrometer.io指标
    - 数据源指标
    - 缓存指标
- 显示构建信息编号
- 关注并下载日志文件
- 查看jvm系统和环境属性
- 查看Spring Boot配置属性
- 支持Spring Cloud的postable / env-和/ refresh-endpoint
- 轻松的日志级管理
- 与JMX-beans交互
- 查看线程转储
- 查看http跟踪
- 查看auditevents
- 查看http-endpoints
- 查看计划任务
- 查看和删除活动会话（使用spring-session）
- 查看Flyway / Liquibase数据库迁移
- 下载heapdump
- 状态变更通知（通过电子邮件，Slack，Hipchat，……）
- 状态更改的事件日志（非持久性）
