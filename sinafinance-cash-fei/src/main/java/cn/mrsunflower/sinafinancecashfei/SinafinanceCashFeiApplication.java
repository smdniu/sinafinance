package cn.mrsunflower.sinafinancecashfei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("cn.mrsunflower.sinafinancecashfei.mapper")
@SpringBootApplication
public class SinafinanceCashFeiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinafinanceCashFeiApplication.class, args);
    }

}
