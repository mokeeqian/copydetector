package cn.edu.ahut.copydetector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@MapperScan(value = "cn.edu.ahut.copydetector.dao")
public class CopydetectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CopydetectorApplication.class, args);
    }

}
