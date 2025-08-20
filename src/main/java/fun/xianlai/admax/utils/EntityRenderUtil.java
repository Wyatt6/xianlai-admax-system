package fun.xianlai.admax.utils;

import fun.xianlai.admax.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 实体类渲染工具
 *
 * @author WyattLau
 */
@Slf4j
public class EntityRenderUtil {
    /**
     * 将源对象中非null的属性值渲染到目标对象同名属性中
     *
     * @param target 目标对象
     * @param source 源对象
     */
    public static void renderNotNullFields(Object target, Object source) {
        Assert.notNull(target, "目标对象为空");
        Assert.notNull(source, "源对象为空");
        Class<?> targetClass = target.getClass();
        Class<?> sourceClass = source.getClass();
        if (targetClass != sourceClass) {
            throw new SystemException("目标对象和源对象类型不同");
        } else {
            Field[] fields = sourceClass.getDeclaredFields();
            Method[] methods = targetClass.getDeclaredMethods();
            for (Field field : fields) {
                String fieldName = field.getName();
                String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                try {
                    Method getter = sourceClass.getMethod(getterName);
                    Object fieldValue = getter.invoke(source);
                    if (fieldValue != null) {
                        for (Method method : methods) {
                            if (setterName.equals(method.getName())) {
                                method.invoke(target, fieldValue);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.info("无法渲染 {} 属性: {}", fieldName, e.getMessage());
                }
            }
            log.info("已完成实体非null属性值渲染");
        }
    }
}
