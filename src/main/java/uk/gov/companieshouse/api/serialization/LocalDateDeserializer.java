package uk.gov.companieshouse.api.serialization;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.JsonNode;
import java.time.ZoneOffset;
import uk.gov.companieshouse.api.exception.BadRequestException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends ValueDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) {
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            JsonNode dateNode = ((JsonNode) jsonParser.readValueAsTree()).get("$date");
            if (dateNode == null || dateNode.isNull())
                throw new BadRequestException("$date field is missing or null");
            if (dateNode.isString()) {
                String s = dateNode.stringValue();
                try {
                    return LocalDate.parse(s, fmt);
                } catch (Exception e) {
                    throw new BadRequestException("Deserialization failed.", e);
                }
            }
            if (dateNode.isObject() && dateNode.has("$numberLong")) {
                String l = dateNode.get("$numberLong").stringValue();
                try {
                    return Instant.ofEpochMilli(Long.parseLong(l)).atZone(ZoneOffset.UTC).toLocalDate();
                } catch (Exception e) {
                    throw new BadRequestException("Deserialization failed.", e);
                }
            }
            throw new BadRequestException("Unrecognized $date format");
        } catch (Exception e) {
            throw new BadRequestException("Deserialization failed.", e);
        }
    }
}