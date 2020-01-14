package com.sinafinance.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * 反序列化类
 *
 * @author liuruizhi
 * @since 2018/11/5
 */
public class EnumTypeDeserializer extends JsonDeserializer<EnumType<?>> implements ContextualDeserializer {
    private Class<? extends EnumType<?>> enumFieldType;
    private Class<?> enumFieldGenericType;

    @Override
    public EnumType<?> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        Object code = p.readValueAs(enumFieldGenericType);
        if (enumFieldGenericType == String.class && StringUtils.isBlank((String) code)) {
            return null;
        }
        EnumType<?> result = EnumHelper.fromCode(code, enumFieldType);
        if (result == null) {
            throw new InvalidFormatException(
                    String.format("field to initialize enums field, type:%s, code:%s ", enumFieldType, code),
                    code,
                    enumFieldType);
        }
        return result;
    }
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        JavaType type = property.getType();
        Class<? extends EnumType<?>> fieldType = (Class<? extends EnumType<?>>) type.getRawClass();
        Class<?> genericType = (Class<?>) ((ParameterizedType) fieldType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        EnumTypeDeserializer result = new EnumTypeDeserializer();
        result.enumFieldType = fieldType;
        result.enumFieldGenericType = genericType;
        return result;
    }
}
