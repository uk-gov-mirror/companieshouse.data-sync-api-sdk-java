package uk.gov.companieshouse.api.converter;

import com.mongodb.BasicDBObject;
import tools.jackson.databind.ObjectMapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class WriteConverter<S> implements Converter<S, BasicDBObject> {

    private final ObjectMapper objectMapper;

    public WriteConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Write convertor.
     *
     * @param source object.
     * @return BSON object to be saved as part of Document.
     */
    @Override
    public BasicDBObject convert(S source) {
        return BasicDBObject.parse(objectMapper.writeValueAsString(source));
    }
}
