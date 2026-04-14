package uk.gov.companieshouse.api.converter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.BasicDBObject;

import tools.jackson.databind.ObjectMapper;
import uk.gov.companieshouse.api.psc.Statement;
import uk.gov.companieshouse.api.psc.Statement.KindEnum;
import uk.gov.companieshouse.api.utils.TestHelper;

class WriteConverterTest {

    private static final KindEnum KIND = TestHelper.KIND;

    private WriteConverter<Statement> converter;

    @BeforeEach
    void setUp() {
        converter = new WriteConverter<>(new ObjectMapper());
    }

    @Test
    void canConvertStatementDocument() {
        Statement statement = new Statement();
        statement.setKind(KIND);

        BasicDBObject object = converter.convert(statement);
        Assertions.assertNotNull(object);
        String json = object.toJson();

        Assertions.assertTrue(json.contains(KIND.getValue()));
    }

    @Test
    void assertThrowsJsonException() {
        assertThrows(RuntimeException.class, () -> converter.convert(null));
    }
}
