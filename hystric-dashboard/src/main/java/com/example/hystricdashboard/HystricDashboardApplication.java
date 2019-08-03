package com.example.hystricdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrixDashboard
@SpringBootApplication
public class HystricDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystricDashboardApplication.class, args);
	}

}
