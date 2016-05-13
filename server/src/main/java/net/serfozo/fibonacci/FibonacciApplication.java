package net.serfozo.fibonacci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("net.serfozo.fibonacci")
public class FibonacciApplication {
    public static void main(final String[] args) {
        SpringApplication.run(FibonacciApplication.class, args);
    }
}
