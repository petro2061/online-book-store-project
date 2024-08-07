package project.onlinebookstore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SCHEMA_TYPE_AND_FORMAT = "bearer";
    private static final String SECURITY_SCHEMA_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Book Store API")
                        .version("1.0.0")
                        .description("API for management online book store"))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEMA_NAME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(SCHEMA_TYPE_AND_FORMAT)
                                .bearerFormat(SCHEMA_TYPE_AND_FORMAT)))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEMA_NAME));
    }
}
