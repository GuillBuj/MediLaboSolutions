package com.medilabosolutions.report_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = {"com.medilabosolutions.report_service"})
@EnableFeignClients(basePackages = "com.medilabosolutions.report_service.client")
public class ReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportServiceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner testContext(ApplicationContext ctx) {
//		return args -> {
//			System.out.println("🎯 Listing Spring Beans...");
//			Arrays.stream(ctx.getBeanDefinitionNames())
//					.sorted()
//					.forEach(System.out::println);
//		};
//	}
}

//@RestController
//@RequestMapping("/test")
//class TestController {
//	@GetMapping
//	public String ping() {
//		return "pong";
//	}
//}
