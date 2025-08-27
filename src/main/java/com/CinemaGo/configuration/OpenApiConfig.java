package com.CinemaGo.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Hassan Alwakeel",
                        url = "https://www.linkedin.com/in/hassan-alwakeel-617537287/"
                ),
                description = "Welcome to the OpenAPI documentation for CinemaGo, a movie ticket booking system. This API provides all the essential endpoints needed to build a full-featured movie ticket booking application, including user authentication, movie management, seat reservation, and more.",
                title = "CinemaGo",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
