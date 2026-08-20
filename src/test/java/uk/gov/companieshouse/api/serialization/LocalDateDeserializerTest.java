package uk.gov.companieshouse.api.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
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
        assertEquals(LocalDate.of(2023, 1, 9), returnedDate);
    }

    @Test
    void longStringReturnsLong() {
        String jsonTestString = "{\"date\":{\"$date\": {\"$numberLong\":\"-1431388800000\"}}}";

        LocalDate returnedDate = deserialize(jsonTestString);
        assertEquals(LocalDate.of(1924, 8, 23), returnedDate);
    }

    @Test
    void nullStringReturnsError() {
        String jsonTestString = null;
        assertThrows(IllegalArgumentException.class, () -> deserialize(jsonTestString));
    }

    @Test
    void invalidStringReturnsError() {
        String jsonTestString = "{\"date\":{\"$date\": \"NotADate\"}}}";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> deserialize(jsonTestString));

        assertEquals("Deserialization failed.", exception.getMessage());
    }

    @Test
    void missingDateFieldReturnsError() {
        String jsonTestString = "{\"date\":{}}";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> deserialize(jsonTestString));
        assertEquals("$date field is missing or null", exception.getMessage());
    }

    @Test
    void nullDateFieldReturnsError() {
        String jsonTestString = "{\"date\":{\"$date\":null}}";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> deserialize(jsonTestString));
        assertEquals("$date field is missing or null", exception.getMessage());
    }

    @Test
    void invalidDateShouldReturnError() throws Exception {

        JsonParser parser = mock(JsonParser.class);
        when(parser.readValueAsTree())
                .thenThrow(new RuntimeException("Invalid JSON"));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> deserializer.deserialize(parser, null));

        assertEquals("Deserialization failed.", exception.getMessage());
    }

    @Test
    void invalidNumberLongReturnError() throws Exception {
        String jsonTestString = "{\"date\":{\"$date\": {\"$numberLong\":\"not-a-number\"}}}";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> deserialize(jsonTestString));

        assertEquals("Deserialization failed.", exception.getMessage());
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
            throw e;
        }
    }

}