package com.sinafinance.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * EnumTypeSerializer
 *
 * @author liuruizhi
 * @since 2018/11/5
 */
public class EnumTypeSerializer extends JsonSerializer<EnumType> {
    @Override
    public void serialize(EnumType value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (value != null) {
            gen.writeObject(value.getCode());
        }
    }
}
