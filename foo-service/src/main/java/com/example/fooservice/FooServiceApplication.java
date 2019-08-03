package com.example.fooservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@EnableEurekaClient
@SpringBootApplication
public class FooServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FooServiceApplication.class, args);
	}

}

@RestController
@RequestMapping("/foo")
class FooController{

	@GetMapping
	public String names() {
		return "Names";
	}

}

@Entity
class Foo {
	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public Foo() {

	}

	public Foo(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Foo{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
