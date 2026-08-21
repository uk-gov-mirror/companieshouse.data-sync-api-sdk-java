package uk.gov.companieshouse.api.filinghistory.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionKindResultTest {

    @Test
    void constructorShouldSetFields() {
        TransactionKind kind = TransactionKind.TOP_LEVEL;

        TransactionKindResult result =
                new TransactionKindResult("encoded-id", kind);

        assertEquals("encoded-id", result.getEncodedId());
        assertEquals(kind, result.getKind());
    }

    @Test
    void settersShouldUpdateFields() {
        TransactionKindResult result =
                new TransactionKindResult(null, null);

        result.setEncodedId("new-id");
        result.setKind(TransactionKind.TOP_LEVEL);

        assertEquals("new-id", result.getEncodedId());
        assertEquals(TransactionKind.TOP_LEVEL, result.getKind());
    }

    @Test
    void equalsShouldReturnTrueForSameValues() {
        TransactionKindResult first =
                new TransactionKindResult("id1", TransactionKind.TOP_LEVEL);

        TransactionKindResult second =
                new TransactionKindResult("id1", TransactionKind.TOP_LEVEL);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentEncodedId() {
        TransactionKindResult first =
                new TransactionKindResult("id1", TransactionKind.TOP_LEVEL);

        TransactionKindResult second =
                new TransactionKindResult("id2", TransactionKind.TOP_LEVEL);

        assertNotEquals(first, second);
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentKind() {
        TransactionKindResult first =
                new TransactionKindResult("id1", TransactionKind.TOP_LEVEL);

        TransactionKindResult second =
                new TransactionKindResult("id1", TransactionKind.ASSOCIATED_FILING);

        assertNotEquals(first, second);
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentObjectType() {
        TransactionKindResult result =
                new TransactionKindResult("id1", TransactionKind.ANNOTATION);

        assertNotEquals(result, "not-a-TransactionKindResult");
    }

    @Test
    void equalsShouldHandleNullFields() {
        TransactionKindResult first =
                new TransactionKindResult(null, null);

        TransactionKindResult second =
                new TransactionKindResult(null, null);

        assertEquals(first, second);
    }

    @Test
    void hashCodeShouldHandleNullValues() {
        TransactionKindResult result =
                new TransactionKindResult(null, null);

        result.hashCode();
    }

    @Test
    void hashCodeShouldHandleNonNullValues() {
        TransactionKindResult result =
                new TransactionKindResult(
                        "id1",
                        TransactionKind.RESOLUTION);

        result.hashCode();
    }
}
