package org.techtoolkit.algoflow.config;

import com.github.javaparser.JavaParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaParserConfig{
    @Bean
    public JavaParser javaParser() {
        return new JavaParser();
    }
}
