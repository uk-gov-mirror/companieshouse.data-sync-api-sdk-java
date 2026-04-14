# data-sync-api-sdk-java

Repository to hold common code used across data sync deltas.

## Major Changes

- **Upgraded to Spring Boot 4** and **Jackson 3** (tools.jackson).
- **Java 21** is now required.
- All Jackson 2 (com.fasterxml.jackson) dependencies are excluded where possible, except where required for legacy compatibility (see below).
- Custom serializers/deserializers for MongoDB date fields, compatible with Jackson 3.

## Dependency Notes

- The SDK uses Jackson 3 (tools.jackson) for all serialization/deserialization.
- Some dependencies (e.g., `private-api-sdk-java`) still require Jackson 2 at runtime. Both Jackson 2 and 3 can coexist due to different package namespaces.
- Exclusions are applied to avoid accidental inclusion of Jackson 2 in most cases, but not all can be removed until all dependencies migrate to Jackson 3.

## LocalDateDeserializer (Jackson 3.x compatible)

This SDK includes a custom `LocalDateDeserializer` for MongoDB date fields, compatible with Jackson 3.x. It supports both ISO date strings and MongoDB's `$numberLong` format, and throws a `BadRequestException` for invalid or missing date values.

**Key features:**
- Handles both `{ "$date": "yyyy-MM-dd'T'HH:mm:ss'Z'" }` and `{ "$date": { "$numberLong": "..." } }` formats
- Uses Jackson 3.x methods (`isString()`, `stringValue()`)
- Robust error handling with clear exceptions

**Example usage:**

```json
{"date": {"$date": "2023-01-09T00:00:00Z"}}
{"date": {"$date": {"$numberLong": "-1431388800000"}}}
```

See [`src/main/java/uk/gov/companieshouse/api/serialization/LocalDateDeserializer.java`](src/main/java/uk/gov/companieshouse/api/serialization/LocalDateDeserializer.java) for implementation details.

## Build and Test

To build and run tests:

```sh
mvn clean test
```

## Compatibility

- Requires Java 21+
- Requires Maven 3.9+
- Designed for Spring Boot 4 and Jackson 3.x

## License

See [LICENSE](LICENSE).
