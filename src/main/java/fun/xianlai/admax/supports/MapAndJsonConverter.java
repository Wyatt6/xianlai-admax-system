package fun.xianlai.admax.supports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

/**
 * JPA数据类型转换器：Java Map和JDBC JSON互转
 *
 * @author WyattLau
 */
@Converter
public class MapAndJsonConverter implements AttributeConverter<Map<String, Object>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("无法将Map对象转换为数据库JSON类型", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null;
            }else {
                return objectMapper.readValue(dbData, Map.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("无法将数据库JSON类型读取为Map对象", e);
        }
    }
}
