package com.sinafinance.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化
 *
 * @author liuruizhi
 * @since 2018/12/17
 */
public class DateSerializer extends JsonSerializer<Date> {

    private static final String DATE_FORMAT = "yyyy/MM/dd";


    @Override
    public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws
            IOException,
            JsonProcessingException {
        if (value != null) {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            String dateStr = df.format(value);
            jsonGenerator.writeObject(dateStr);
        } else {
            jsonGenerator.writeObject("");
        }
    }
}
