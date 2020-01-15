package com.sinafinance.cashout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.sinafinance.cashout.mapper")
@SpringBootApplication
public class SinafinanceCashApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinafinanceCashApplication.class, args);
    }

}
