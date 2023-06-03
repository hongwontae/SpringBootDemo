package org.zerock.bj1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"org.zerock.bj1.**.mappers"})
public class Bj1Application {

	public static void main(String[] args) {
		SpringApplication.run(Bj1Application.class, args);
	}

}
