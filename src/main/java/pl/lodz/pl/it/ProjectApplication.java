package pl.lodz.pl.it;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Library APIS", version ="1.0", description = "Library Management Apis."))
public class ProjectApplication {

	public static void main(String[] args) {


		SpringApplication.run(ProjectApplication.class, args);

	}

}
