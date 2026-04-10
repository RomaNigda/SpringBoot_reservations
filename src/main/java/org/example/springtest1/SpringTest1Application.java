package org.example.springtest1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringTest1Application {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        SpringApplication.run(SpringTest1Application.class, args);
//        System.out.print("Pass: ");
//        System.out.println(encoder.encode("user123"));
    }

}
