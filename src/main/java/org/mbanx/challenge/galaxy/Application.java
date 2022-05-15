package org.mbanx.challenge.galaxy;

import java.util.ArrayList;

import javax.annotation.PreDestroy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"org.mbanx.challenge"})
@EnableAsync
@RequestMapping("")
public class Application extends SpringBootServletInitializer implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping
	@ResponseBody
	public String welcome() {
		return "Galaxt Merchant Tradaing Service - UP";
	}

	@Bean
	public Docket api() {
		Contact contact = new Contact("name", "", "adi.lambang@yahoo.co.id");
		ApiInfo apiInfo = new ApiInfo("Galaxy Merchant Tradaing Service", "", "1.0", "", contact, "", "",
				new ArrayList<>());
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	public void run(String... args) throws Exception {
		log.info("Application started...!!!");
	}

	@PreDestroy
	public void destroy() {
		log.info("Application stopped...!!!");
	}
}
