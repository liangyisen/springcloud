package com.yisen.springcloud;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RestController
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
public class ServiceLucyApplication {


	/**
	 * 访问地址 http://localhost:8762/actuator/hystrix.stream
	 *
	 * @param args
	 */

	public static void main(String[] args) {
		SpringApplication.run(ServiceLucyApplication.class, args);
	}

	@Value("${server.port}")
	String port;

	@RequestMapping("/hi")
	@HystrixCommand(fallbackMethod = "error")
	public String home(String name) {
		return "hi " + name + " ,i am from port:" + port;
	}


	@RequestMapping("/info")
	public String info() {
		return "hello! service-hi";
	}


	public String error(String name) {
		return "hi," + name + "  报错了@@!  ";
	}
}
