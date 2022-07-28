package it.datawizard.unicom.unicombackend.datamodel.util;

import com.google.gson.Gson;
import org.springframework.core.GenericTypeResolver;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@Converter
public class JsonDataConverter<T extends JsonData> implements AttributeConverter<T, String> {
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public JsonDataConverter() {
        this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), JsonDataConverter.class);
    }

    @Override
    public String convertToDatabaseColumn(T jsonData) {
        Gson gson = new Gson();
        return gson.toJson(jsonData);
    }

    @Override
    public T convertToEntityAttribute(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, this.type);
    }
}
