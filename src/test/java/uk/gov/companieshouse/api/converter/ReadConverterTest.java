package uk.gov.companieshouse.api.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.companieshouse.api.exception.InternalServiceException;
import uk.gov.companieshouse.api.psc.Statement;
import uk.gov.companieshouse.api.utils.TestHelper;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentMatchers;

class ReadConverterTest {
    private static final String ETAG = TestHelper.ETAG;

    private ReadConverter<Statement> pscStatementReadConverter;

    @BeforeEach
    void setUp(){
        pscStatementReadConverter = new ReadConverter<>(new ObjectMapper(), Statement.class);
    }
    @Test
    void correctlyConvertsDocumentToStatementObject(){
        Document source = Document.parse("{\"etag\" : \"etag\"}");
        Statement actualStatement = pscStatementReadConverter.convert(source);
        Assertions.assertNotNull(actualStatement);
        Assertions.assertEquals(ETAG, actualStatement.getEtag());
    }

    @Test
    void testConvertThrowsInternalServiceExceptionOnJsonError() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Document source = new Document();
        when(objectMapper.readValue(anyString(), ArgumentMatchers.<Class<Object>>any()))
                .thenThrow(new JsonProcessingException("Error") {});

        // WHEN
        ReadConverter<Statement> converter = new ReadConverter<>(objectMapper, Statement.class);

        // THEN
        Exception ex = Assertions.assertThrows(InternalServiceException.class, () -> converter.convert(source));
        Assertions.assertEquals("failed to read and convert Document to JSON", ex.getMessage());

    }
}
