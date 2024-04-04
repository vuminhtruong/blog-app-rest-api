package com.truongvu.blogrestapi;

import com.truongvu.blogrestapi.entity.Role;
import com.truongvu.blogrestapi.repository.RoleRepository;
import com.truongvu.blogrestapi.service.PostService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot Blog App REST APIs",
				description = "Spring Boot Blog App REST APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Truong Vu",
						email = "ndsampea@gmail.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Spring Boot Blog App Documentation",
				url = "https://github.com/vuminhtruong/blog-app-rest-api"
		)
)
public class BlogRestApiApplication implements CommandLineRunner {
	@Autowired
	private RoleRepository roleRepository;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogRestApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Role adminRole = new Role();
//		adminRole.setRole("ROLE_ADMIN");
//		roleRepository.save(adminRole);
//
//		Role userRole = new Role();
//		userRole.setRole("ROLE_USER");
//		roleRepository.save(userRole);
	}
}
