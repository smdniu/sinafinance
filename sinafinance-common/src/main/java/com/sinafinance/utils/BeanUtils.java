package com.sinafinance.utils;

import com.google.common.collect.Maps;
import com.sinafinance.enums.EnumHelper;
import com.sinafinance.enums.EnumType;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述： 类属性拷贝帮助类
 *
 * @author liuruizhi
 * @since 2018/12/11
 */
public class BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    private static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();
    private static Converter converter = new DefaultConverter();

    /**
     * 拷贝源对象的属性到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <T, V> void copy(T source, V target) {
        if (source == null || target == null) {
            return;
        }
        BeanCopier beanCopier = createBeanCopier(source.getClass(), target.getClass());
        beanCopier.copy(source, target, converter);
    }

    /**
     * 拷贝源对象的属性到目标类的实例，并返回目标类的实例
     *
     * @param source 源对象
     * @param clazz  目标类
     *
     * @return
     */
    public static <T, V> V convert(T source, Class<V> clazz) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = createBeanCopier(source.getClass(), clazz);
        try {
            V targetObj = clazz.newInstance();
            beanCopier.copy(source, targetObj, converter);
            return targetObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将源对象列表转换为目标类实例的列表
     *
     * @param sourceList 源对象列表
     * @param clazz      目标类
     *
     * @return
     */
    public static <T, V> List<V> convertList(List<T> sourceList, Class<V> clazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return null;
        }
        List<V> targetList = new ArrayList<V>();
        BeanCopier beanCopier = null;
        for (T sourceObj : sourceList) {
            if (beanCopier == null) {
                beanCopier = createBeanCopier(sourceObj.getClass(), clazz);
            }
            try {
                V targetObj = clazz.newInstance();
                beanCopier.copy(sourceObj, targetObj, converter);
                targetList.add(targetObj);
            } catch (Exception e) {
                logger.error("BeanUtil#convertList error: "+e);
            }
        }
        return targetList;
    }
    public static Map<String, Object> createSortedParamsByObject(Object request) {
        Map<String, Object> params = Maps.newTreeMap();
        BeanMap beanMap = BeanMap.create(request);
        for (Object key : beanMap.keySet()) {
            if (beanMap.get(key) != null) {
                params.put((String) key, beanMap.get(key));
            }
        }
        return params;
    }
    private static BeanCopier createBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        String key = generateKey(sourceClass, targetClass);
        BeanCopier beanCopier = beanCopierMap.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(sourceClass, targetClass, true);
            beanCopierMap.put(key, beanCopier);
        }
        return beanCopier;
    }
    private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.getCanonicalName().concat(targetClass.getCanonicalName());
    }

    static class DefaultConverter implements Converter {

        @Override
        public Object convert(Object value, Class targetType, Object context) {
            if (value == null) {
                return null;
            }

            Class<?> sourceType = value.getClass();
            if (sourceType.isAssignableFrom(targetType)) {
                return value;
            } else if (List.class.isAssignableFrom(targetType)) {
                return value;
            } else if (Map.class.isAssignableFrom(targetType)) {
                return value;
            }

            if (value instanceof EnumType) {
                return ((EnumType) value).getCode();
            } else if (EnumType.class.isAssignableFrom(targetType)) {
                return EnumHelper.fromCode(value, targetType);
            } else if (value instanceof Date) {
                if (LocalDate.class.isAssignableFrom(targetType)) {
                    return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                }

                if (LocalDateTime.class.isAssignableFrom(targetType)) {
                    return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }
            } else if (Date.class.isAssignableFrom(targetType)) {
                if (LocalDate.class.isAssignableFrom(sourceType)) {
                    return Date.from(((LocalDate) value).atStartOfDay(ZoneId.systemDefault()).toInstant());
                }

                if (LocalDateTime.class.isAssignableFrom(sourceType)) {
                    return Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
                }
            }

            return null;
        }

    }
}
