package ro.mpp2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class RestServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);
    }
    @Bean(name="props")
    @Primary
    public Properties getBdProperties() throws IOException {
        Properties props = new Properties();
        Resource res = new ClassPathResource("bd.config");
        try (InputStream in = res.getInputStream()) {
            props.load(in);
        }
        return props;
    }
}
