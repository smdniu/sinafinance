package com.sinafinance.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class FastJsonUtils {
    public <T> T fromJsonFile(String filePath, Type type) {

        ClassPathResource resource = new ClassPathResource(filePath);

        try {
            if (resource != null && resource.exists()) {
                return JSON.parseObject(resource.getInputStream(), StandardCharsets.UTF_8, type,
                        // 自动关闭流
                        Feature.AutoCloseSource,
                        // 允许注释
                        Feature.AllowComment,
                        // 允许单引号
                        Feature.AllowSingleQuotes,
                        // 使用 Big decimal
                        Feature.UseBigDecimal
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
