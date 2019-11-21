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
整合了Hystrix，具有熔断的能力