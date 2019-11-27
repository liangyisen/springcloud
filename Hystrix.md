# Hystrix是怎么工作的？
## 架构图
下图显示通过Hystrix向服务依赖关系发出请求时会发生什么：

![image](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-command-flow-chart.png)

具体将从以下几个方面进行描述：
### 1.构建一个HystrixCommand或者HystrixObservableCommand 对象。
第一步是构建一个HystrixCommand或HystrixObservableCommand对象来表示你对依赖关系的请求。 其中构造函数需要和请求时的参数一致。

构造HystrixCommand对象，如果依赖关系预期返回单个响应。 可以这样写：

`HystrixCommand command = new HystrixCommand(arg1, arg2);`
同理，可以构建HystrixObservableCommand ：

HystrixObservableCommand command = new HystrixObservab

`HystrixObservableCommand command = new HystrixObservableCommand(arg1, arg2);`

### 2.执行Command
- 通过使用Hystrix命令对象的以下四种方法之一，可以执行该命令有四种方法（前两种方法仅适用于简单的HystrixCommand对象，并不适用于HystrixObservableCommand）：
- execute()–阻塞，，然后返回从依赖关系接收到的单个响应（或者在发生错误时抛出异常）
- queue()–返回一个可以从依赖关系获得单个响应的future 对象
- observe()–订阅Observable代表依赖关系的响应，并返回一个Observable，该Observable会复制该来源Observable
- toObservable() –返回一个Observable，当您订阅它时，将执行Hystrix命令并发出其响应
`
K             value   = command.execute();

Future<K>     fValue  = command.queue();

Observable<K> ohValue = command.observe();  
       
Observable<K> ocValue = command.toObservable();
`
同步调用execute（）调用queue（）.get（）. queue（）依次调用toObservable（）.toBlocking（）.toFuture（）。 这就是说，最终每个HystrixCommand都由一个Observable实现支持，甚至是那些旨在返回单个简单值的命令。

### 3.响应是否有缓存？
如果为该命令启用请求缓存，并且如果缓存中对该请求的响应可用，则此缓存响应将立即以“可观察”的形式返回。

### 4.断路器是否打开？
当您执行该命令时，Hystrix将检查断路器以查看电路是否打开。

如果电路打开（或“跳闸”），则Hystrix将不会执行该命令，但会将流程路由到（8）获取回退。

如果电路关闭，则流程进行到（5）以检查是否有可用于运行命令的容量。

### 5.线程池/队列/信号量是否已经满负载？
如果与命令相关联的线程池和队列（或信号量，如果不在线程中运行）已满，则Hystrix将不会执行该命令，但将立即将流程路由到（8）获取回退。

### 6.HystrixObservableCommand.construct() 或者 HystrixCommand.run()
在这里，Hystrix通过您为此目的编写的方法调用对依赖关系的请求，其中之一是：

- HystrixCommand.run（） - 返回单个响应或者引发异常

- HystrixObservableCommand.construct（） - 返回一个发出响应的Observable或者发送一个onError通知

如果run（）或construct（）方法超出了命令的超时值，则该线程将抛出一个TimeoutException（或者如果命令本身没有在自己的线程中运行，则会产生单独的计时器线程）。 在这种情况下，Hystrix将响应通过8进行路由。获取Fallback，如果该方法不取消/中断，它会丢弃最终返回值run（）或construct（）方法。

请注意，没有办法强制潜在线程停止工作 - 最好的Hystrix可以在JVM上执行它来抛出一个InterruptedException。 如果由Hystrix包装的工作不处理InterruptedExceptions，Hystrix线程池中的线程将继续工作，尽管客户端已经收到了TimeoutException。 这种行为可能使Hystrix线程池饱和，尽管负载“正确地流失”。 大多数Java HTTP客户端库不会解释InterruptedExceptions。 因此，请确保在HTTP客户端上正确配置连接和读/写超时。

如果该命令没有引发任何异常并返回响应，则Hystrix在执行某些日志记录和度量报告后返回此响应。 在run（）的情况下，Hystrix返回一个Observable，发出单个响应，然后进行一个onCompleted通知; 在construct（）的情况下，Hystrix返回由construct（）返回的相同的Observable。

### 7.计算Circuit 的健康
Hystrix向断路器报告成功，失败，拒绝和超时，该断路器维护了一系列的计算统计数据组。

它使用这些统计信息来确定电路何时“跳闸”，此时短路任何后续请求直到恢复时间过去，在首次检查某些健康检查之后，它再次关闭电路。

### 8.获取Fallback
当命令执行失败时，Hystrix试图恢复到你的回退：当construct（）或run（）（6.）抛出异常时，当命令由于电路断开而短路时（4.），当 命令的线程池和队列或信号量处于容量（5.），或者当命令超过其超时长度时。

编写Fallback ,它不一依赖于任何的网络依赖，从内存中获取获取通过其他的静态逻辑。如果你非要通过网络去获取Fallback,你可能需要些在获取服务的接口的逻辑上写一个HystrixCommand。

### 9.返回成功的响应
如果 Hystrix command成功，如果Hystrix命令成功，它将以Observable的形式返回对呼叫者的响应或响应。 根据您在上述步骤2中调用命令的方式，此Observable可能会在返回给您之前进行转换：

![image](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-return-flow.png)

- execute（） - 以与.queue（）相同的方式获取Future，然后在此Future上调用get（）来获取Observable发出的单个值
- queue（） - 将Observable转换为BlockingObservable，以便将其转换为Future，然后返回此未来
- observe（） - 立即订阅Observable并启动执行命令的流程; 返回一个Observable，当您订阅它时，重播排放和通知
- toObservable（） - 返回Observable不变; 您必须订阅它才能实际开始导致命令执行的流程

## 断路器（Circuit Breaker）

下图显示HystrixCommand或HystrixObservableCommand如何与HystrixCircuitBreaker及其逻辑和决策流程进行交互，包括计数器在断路器中的行为。

![image](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/circuit-breaker-1280.png)

发生电路开闭的过程如下：

1. 假设电路上的音量达到一定阈值（HystrixCommandProperties.circuitBreakerRequestVolumeThreshold（））…

1. 并假设错误百分比超过阈值错误百分比（HystrixCommandProperties.circuitBreakerErrorThresholdPercentage（））…

1. 然后断路器从CLOSED转换到OPEN。

1. 虽然它是开放的，它使所有针对该断路器的请求短路。

1. 经过一段时间（HystrixCommandProperties.circuitBreakerSleepWindowInMilliseconds（）），下一个单个请求是通过（这是HALF-OPEN状态）。 如果请求失败，断路器将在睡眠窗口持续时间内返回到OPEN状态。 如果请求成功，断路器将转换到CLOSED，逻辑1.重新接管。

## 隔离（Isolation）
Hystrix采用隔板模式来隔离彼此的依赖关系，并限制对其中任何一个的并发访问。

![image](https://www.fangzhipeng.com/img/2019jianshu/soa-5-isolation-focused-640.png)