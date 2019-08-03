package com.example.reservationclient;

import com.netflix.client.util.Resources;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@EnableCircuitBreaker
@EnableBinding(Source.class)
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	@Bean
	RestTemplate restTemplate() {
		return  new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}

@RestController
@RequestMapping("/reservations")
class ReservationAPIRestController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private Source source;

	@PostMapping
	public void writeReservations(@RequestBody Reservation reservation) {
		Message<String> msg = MessageBuilder.withPayload(reservation.getReservationName()).build();
		this.source.output().send(msg);
	}

	public Collection<String> getReservationNamesFallBack() {
		return new ArrayList<>();
	}

	@HystrixCommand(fallbackMethod = "getReservationNamesFallBack")
	@GetMapping("/names")
	public Collection<String> getReservationNames() {
		ParameterizedTypeReference<List<Reservation>> ptr = new ParameterizedTypeReference<List<Reservation>>() {

		};
		ResponseEntity<List<Reservation>> entity = this.restTemplate.exchange("http://reservation-service/reservations", HttpMethod.GET, null, ptr);
		return entity.getBody().stream().map(Reservation::getReservationName).collect(Collectors.toList());
	}
}

class Reservation {
	String reservationName;

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}
}