package com.impactsure.artnook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableConfigurationProperties
@EnableEncryptableProperties
@EntityScan(basePackages = {"com.impactsure.artnook.entity"}) 
public class ArtnookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtnookApplication.class, args);
	}

}
