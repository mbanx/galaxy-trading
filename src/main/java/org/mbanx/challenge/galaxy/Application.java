package org.mbanx.challenge.galaxy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.annotation.PreDestroy;

import org.mbanx.challenge.galaxy.model.ProcessorPatternConfiguration;
import org.mbanx.challenge.galaxy.repo.ProcessorPatternConfigurationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	private ProcessorPatternConfigurationRepo configRepo;
	
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
		
		File file = ResourceUtils.getFile("classpath:initial-data/init-config.csv");
		String filePath = file.getAbsolutePath();
		String content = new String(Files.readAllBytes(Paths.get(filePath)));
		String[] multiLineText = content.split("\\r?\\n");
		for(String text: multiLineText) {
			String[] splits = text.split(Pattern.quote("#"));
			if(splits.length > 1) {
				String pattern = splits[0];
				String name = splits[1];
				
				ProcessorPatternConfiguration config = configRepo.findByProcessorNameAndPattern(name, pattern);
				if(config == null) {
					config = new ProcessorPatternConfiguration();
					config.setPattern(pattern);
					config.setProcessorName(name);
					config = configRepo.save(config);
					log.info("insert init config={}", config);
				}
			}
		}
	}
	
	@PreDestroy
	public void destroy() {
		log.info("Application stopped...!!!");
	}
}
