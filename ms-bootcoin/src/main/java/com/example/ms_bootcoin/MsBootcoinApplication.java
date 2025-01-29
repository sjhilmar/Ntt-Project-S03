package com.example.ms_bootcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsBootcoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBootcoinApplication.class, args);
	}

}
