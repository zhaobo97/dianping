package com.zhaobo.spark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"com.zhaobo.spark"})
@MapperScan(basePackages = "com.bin.spark.mapper")
@EnableScheduling
public class SparkApplication {
    public static void main(String[] args) {
        SpringApplication.run(SparkApplication.class, args);
        System.out.println("/ \\  / \\  / \\  / \\  / \\ \n" +
                " ( S )( P )( A )( R )( K )\n" +
                "  \\_/  \\_/  \\_/  \\_/  \\_/    启动成功ლ(´ڡ`ლ)");
    }
}
