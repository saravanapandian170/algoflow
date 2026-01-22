package org.techtoolkit.algoflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaParserConfig {
    @Bean
    public JavaParserConfig javaParserConfig() {
        return new JavaParserConfig();
    }
}
