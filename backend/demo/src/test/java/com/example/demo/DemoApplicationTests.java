package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest(classes = MainApplication.class)
@ComponentScan(basePackages = "it.case_vacanze.manager")
@EntityScan(basePackages = "it.case_vacanze.manager.entity")
@EnableJpaRepositories(basePackages = "it.case_vacanze.manager.repository")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
