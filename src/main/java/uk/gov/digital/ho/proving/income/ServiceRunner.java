package uk.gov.digital.ho.proving.income;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class ServiceRunner {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRunner.class, args);
    }
}
