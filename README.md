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