package com.all4land.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.all4land.generator.ui.controller.MainController;
import com.all4land.generator.ui.view.Sample1;

@SpringBootApplication
@EnableScheduling
@ComponentScan(excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {Sample1.class, MainController.class})
})
public class GeneratorApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GeneratorApplication.class);
		app.setHeadless(true);
		app.run(args);
	}
}
