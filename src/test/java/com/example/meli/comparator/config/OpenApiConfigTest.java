package com.example.meli.comparator.config;

import com.example.meli.comparator.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    @DisplayName("Deve criar OpenAPI com título, versão e descrição corretos")
    void customOpenAPI_ShouldCreateOpenApiWithCorrectInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("Product search API", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("API to provide product details for comparison", info.getDescription());
    }
}
