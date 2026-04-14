package uk.gov.companieshouse.api.serialization;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectMapper;
import uk.gov.companieshouse.api.exception.BadRequestException;

class LocalDateDeserializerTest {

    private LocalDateDeserializer deserializer;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        deserializer = new LocalDateDeserializer();

        mapper = new ObjectMapper();
    }

    @Test
    void dateShouldDeserialize() {
        String jsonTestString = "{\"date\":{\"$date\": \"2023-01-09T00:00:00Z\"}}";

        LocalDate returnedDate = deserialize(jsonTestString);
        Assertions.assertEquals(LocalDate.of(2023, 1, 9), returnedDate);
    }

    @Test
    void longStringReturnsLong() {
        String jsonTestString = "{\"date\":{\"$date\": {\"$numberLong\":\"-1431388800000\"}}}";

        LocalDate returnedDate = deserialize(jsonTestString);
        Assertions.assertEquals(LocalDate.of(1924, 8, 23), returnedDate);
    }

    @Test
    void nullStringReturnsError() {
        String jsonTestString = null;
        assertThrows(IllegalArgumentException.class, () -> deserialize(jsonTestString));
    }

    @Test
    void invalidStringReturnsError() {
        String jsonTestString = "{\"date\":{\"$date\": \"NotADate\"}}}";

        assertThrows(BadRequestException.class, () -> deserialize(jsonTestString));
    }

    private LocalDate deserialize(String jsonString) {
        try {
            JsonParser parser = mapper.createParser(jsonString);
            parser.nextToken();
            parser.nextToken();
            parser.nextToken();
            // Pass null for DeserializationContext as it's not used in the deserializer
            return deserializer.deserialize(parser, null);
        } catch (Exception e) {
            // Unwrap if it's a RuntimeException wrapping another exception
            if (e instanceof RuntimeException && e.getCause() != null) {
                throw (RuntimeException) e.getCause();
            }
            // Otherwise, rethrow as is
            throw e;
        }
    }

}
