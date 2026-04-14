package uk.gov.companieshouse.api.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import tools.jackson.databind.ObjectMapper;


@ReadingConverter
public class ReadConverter<T> implements Converter<Document, T> {

    private final ObjectMapper objectMapper;
    private final Class<T> objectClass;

    public ReadConverter(ObjectMapper objectMapper, Class<T> objectClass) {
        this.objectMapper = objectMapper;
        this.objectClass = objectClass;
    }

    @Override
    public T convert(Document source) {
        return this.objectMapper.readValue(source.toJson(), objectClass);
    }
}
