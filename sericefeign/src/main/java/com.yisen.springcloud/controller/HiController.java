package com.yisen.springcloud.controller;

import com.yisen.springcloud.feign.SchedualServiceHi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HiController {
	@Resource
	SchedualServiceHi schedualServiceHi;

	@GetMapping(value = "/hi")
	public String sayHi(@RequestParam String name) {
		return schedualServiceHi.sayHiFromClientOne(name);
	}
}

