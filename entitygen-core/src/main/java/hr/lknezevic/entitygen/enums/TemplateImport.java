package hr.lknezevic.entitygen.enums;

import lombok.Getter;

/**
 * Enum representing import for Java and other libraries.
 * The order determines the priority of imports when generating code.
 *
 * @author leonknezevic
 */
@Getter
public enum TemplateImport {
    // Java SE - Core Types (order 1)
    JAVA_INTEGER(1, "java.lang.Integer"),
    JAVA_LONG(1, "java.lang.Long"),
    JAVA_DOUBLE(1, "java.lang.Double"),
    JAVA_BOOLEAN(1, "java.lang.Boolean"),
    JAVA_STRING(1, "java.lang.String"),
    JAVA_BIG_DECIMAL(1, "java.math.BigDecimal"),
    JAVA_BIG_INTEGER(1, "java.math.BigInteger"),
    
    // Java SE - Time API (order 1)
    JAVA_DATE(1, "java.util.Date"),
    JAVA_LOCAL_DATE(1, "java.time.LocalDate"),
    JAVA_LOCAL_DATE_TIME(1, "java.time.LocalDateTime"),
    JAVA_LOCAL_TIME(1, "java.time.LocalTime"),
    JAVA_INSTANT(1, "java.time.Instant"),
    JAVA_DURATION(1, "java.time.Duration"),
    JAVA_PERIOD(1, "java.time.Period"),
    JAVA_ZONE_ID(1, "java.time.ZoneId"),
    JAVA_ZONE_OFFSET(1, "java.time.ZoneOffset"),
    JAVA_OFFSET_DATE_TIME(1, "java.time.OffsetDateTime"),
    JAVA_ZONED_DATE_TIME(1, "java.time.ZonedDateTime"),
    JAVA_TEMPORAL_UNIT(1, "java.time.temporal.TemporalUnit"),
    JAVA_CHRONO_UNIT(1, "java.time.temporal.ChronoUnit"),
    
    // Java SE - Collections (order 1)
    JAVA_LIST(1, "java.util.List"),
    JAVA_SET(1, "java.util.Set"),
    JAVA_COLLECTION(1, "java.util.Collection"),
    JAVA_ARRAY_LIST(1, "java.util.ArrayList"),
    JAVA_LINKED_LIST(1, "java.util.LinkedList"),
    JAVA_HASH_SET(1, "java.util.HashSet"),
    JAVA_LINKED_HASH_SET(1, "java.util.LinkedHashSet"),
    JAVA_TREE_SET(1, "java.util.TreeSet"),
    JAVA_HASH_MAP(1, "java.util.HashMap"),
    JAVA_LINKED_HASH_MAP(1, "java.util.LinkedHashMap"),
    JAVA_TREE_MAP(1, "java.util.TreeMap"),
    JAVA_CONCURRENT_HASH_MAP(1, "java.util.concurrent.ConcurrentHashMap"),
    JAVA_VECTOR(1, "java.util.Vector"),
    JAVA_STACK(1, "java.util.Stack"),
    JAVA_QUEUE(1, "java.util.Queue"),
    JAVA_DEQUE(1, "java.util.Deque"),
    JAVA_PRIORITY_QUEUE(1, "java.util.PriorityQueue"),
    
    // Java SE - Utilities (order 1)
    JAVA_OPTIONAL(1, "java.util.Optional"),
    JAVA_UUID(1, "java.util.UUID"),
    JAVA_COLLECTORS(1, "java.util.stream.Collectors"),
    JAVA_STREAM(1, "java.util.stream.Stream"),
    JAVA_STREAM_COLLECTOR(1, "java.util.stream.Collector"),
    JAVA_OBJECTS(1, "java.util.Objects"),
    JAVA_ARRAYS(1, "java.util.Arrays"),
    JAVA_COLLECTIONS(1, "java.util.Collections"),
    JAVA_COMPARATOR(1, "java.util.Comparator"),
    
    // Java SE - Functional Programming (order 1)
    JAVA_FUNCTION(1, "java.util.function.Function"),
    JAVA_PREDICATE(1, "java.util.function.Predicate"),
    JAVA_CONSUMER(1, "java.util.function.Consumer"),
    JAVA_SUPPLIER(1, "java.util.function.Supplier"),
    
    // Java SE - Math (order 1)
    JAVA_MATH_CONTEXT(1, "java.math.MathContext"),
    JAVA_MATH_ROUNDING_MODE(1, "java.math.RoundingMode"),

    // Java SE - IO & NIO (order 1)
    JAVA_SERIALIZABLE(1, "java.io.Serializable"),
    JAVA_SERIAL(1, "java.io.Serial"),
    JAVA_FILE(1, "java.io.File"),
    JAVA_PATH(1, "java.nio.file.Path"),
    JAVA_PATHS(1, "java.nio.file.Paths"),
    JAVA_FILES(1, "java.nio.file.Files"),
    JAVA_INPUT_STREAM(1, "java.io.InputStream"),
    JAVA_OUTPUT_STREAM(1, "java.io.OutputStream"),
    JAVA_BYTE_ARRAY_OUTPUT_STREAM(1, "java.io.ByteArrayOutputStream"),
    
    // Java SE - Text & Regex (order 1)
    JAVA_PATTERN(1, "java.util.regex.Pattern"),
    JAVA_MATCHER(1, "java.util.regex.Matcher"),
    JAVA_DECIMAL_FORMAT(1, "java.text.DecimalFormat"),
    JAVA_SIMPLE_DATE_FORMAT(1, "java.text.SimpleDateFormat"),
    JAVA_NUMBER_FORMAT(1, "java.text.NumberFormat"),
    JAVA_LOCALE(1, "java.util.Locale"),
    
    // Java SE - Network (order 1)
    JAVA_URL(1, "java.net.URL"),
    JAVA_URI(1, "java.net.URI"),
    
    // Java SE - SQL Types (order 1)
    JAVA_SQL_DATE(1, "java.sql.Date"),
    JAVA_SQL_TIME(1, "java.sql.Time"),
    JAVA_SQL_TIMESTAMP(1, "java.sql.Timestamp"),
    JAVA_SQL_BLOB(1, "java.sql.Blob"),
    JAVA_SQL_CLOB(1, "java.sql.Clob"),
    
    // Jakarta EE - JPA Core (order 2)
    JPA_ENTITY(2, "jakarta.persistence.Entity"),
    JPA_TABLE(2, "jakarta.persistence.Table"),
    JPA_ID(2, "jakarta.persistence.Id"),
    JPA_GENERATED_VALUE(2, "jakarta.persistence.GeneratedValue"),
    JPA_GENERATION_TYPE(2, "jakarta.persistence.GenerationType"),
    JPA_COLUMN(2, "jakarta.persistence.Column"),
    JPA_UNIQUE_CONSTRAINT(2, "jakarta.persistence.UniqueConstraint"),
    JPA_ENUMERATED(2, "jakarta.persistence.Enumerated"),
    JPA_ENUM_TYPE(2, "jakarta.persistence.EnumType"),
    JPA_LOB(2, "jakarta.persistence.Lob"),
    
    // Jakarta EE - JPA Embedded (order 2)
    JPA_EMBEDDABLE(2, "jakarta.persistence.Embeddable"),
    JPA_EMBEDDED_ID(2, "jakarta.persistence.EmbeddedId"),

    // Jakarta EE - JPA Relationships (order 2)
    JPA_ONE_TO_ONE(2, "jakarta.persistence.OneToOne"),
    JPA_ONE_TO_MANY(2, "jakarta.persistence.OneToMany"),
    JPA_MANY_TO_ONE(2, "jakarta.persistence.ManyToOne"),
    JPA_MANY_TO_MANY(2, "jakarta.persistence.ManyToMany"),
    JPA_JOIN_COLUMN(2, "jakarta.persistence.JoinColumn"),
    JPA_JOIN_COLUMNS(2, "jakarta.persistence.JoinColumns"),
    JPA_JOIN_TABLE(2, "jakarta.persistence.JoinTable"),
    JPA_CASCADE_TYPE(2, "jakarta.persistence.CascadeType"),
    JPA_FETCH_TYPE(2, "jakarta.persistence.FetchType"),
    JPA_ORPHAN_REMOVAL(2, "jakarta.persistence.OrphanRemoval"),
    JPA_MAPS_ID(2, "jakarta.persistence.MapsId"),
    
    // Jakarta EE - Validation (order 2)
    JAKARTA_NOT_NULL(2, "jakarta.validation.constraints.NotNull"),
    JAKARTA_NOT_BLANK(2, "jakarta.validation.constraints.NotBlank"),
    JAKARTA_NOT_EMPTY(2, "jakarta.validation.constraints.NotEmpty"),
    JAKARTA_SIZE(2, "jakarta.validation.constraints.Size"),
    JAKARTA_MIN(2, "jakarta.validation.constraints.Min"),
    JAKARTA_MAX(2, "jakarta.validation.constraints.Max"),
    JAKARTA_PATTERN(2, "jakarta.validation.constraints.Pattern"),
    JAKARTA_EMAIL(2, "jakarta.validation.constraints.Email"),
    JAKARTA_VALID(2, "jakarta.validation.Valid"),
    JAKARTA_DIGITS(2, "jakarta.validation.constraints.Digits"),
    JAKARTA_DECIMAL_MIN(2, "jakarta.validation.constraints.DecimalMin"),
    JAKARTA_DECIMAL_MAX(2, "jakarta.validation.constraints.DecimalMax"),
    JAKARTA_POSITIVE(2, "jakarta.validation.constraints.Positive"),
    JAKARTA_NEGATIVE(2, "jakarta.validation.constraints.Negative"),
    JAKARTA_FUTURE(2, "jakarta.validation.constraints.Future"),
    JAKARTA_PAST(2, "jakarta.validation.constraints.Past"),
    
    // Jakarta EE - Lifecycle & Events (order 2)
    JAKARTA_POST_CONSTRUCT(2, "jakarta.annotation.PostConstruct"),
    JAKARTA_PRE_DESTROY(2, "jakarta.annotation.PreDestroy"),
    JAKARTA_INJECT(2, "jakarta.inject.Inject"),
    JAKARTA_SINGLETON(2, "jakarta.inject.Singleton"),
    JAKARTA_NAMED(2, "jakarta.inject.Named"),
    
    // Jakarta EE - JSON & Web Services (order 2)
    JAKARTA_JSON_BIND(2, "jakarta.json.bind.annotation.JsonbProperty"),
    JAKARTA_JSON_IGNORE(2, "jakarta.json.bind.annotation.JsonbTransient"),
    JAKARTA_WS_RS_GET(2, "jakarta.ws.rs.GET"),
    JAKARTA_WS_RS_POST(2, "jakarta.ws.rs.POST"),
    JAKARTA_WS_RS_PUT(2, "jakarta.ws.rs.PUT"),
    JAKARTA_WS_RS_DELETE(2, "jakarta.ws.rs.DELETE"),
    JAKARTA_WS_RS_PATH(2, "jakarta.ws.rs.Path"),
    JAKARTA_WS_RS_PRODUCES(2, "jakarta.ws.rs.Produces"),
    JAKARTA_WS_RS_CONSUMES(2, "jakarta.ws.rs.Consumes"),
    
    // Jakarta EE - Converters & Transformers (order 2)
    JAKARTA_PERSISTENCE_CONVERTER(2, "jakarta.persistence.Converter"),
    JAKARTA_PERSISTENCE_ATTRIBUTE_CONVERTER(2, "jakarta.persistence.AttributeConverter"),
    
    // Spring Framework - Core & DI (order 3)
    SPRING_COMPONENT(3, "org.springframework.stereotype.Component"),
    SPRING_SERVICE(3, "org.springframework.stereotype.Service"),
    SPRING_REPOSITORY(3, "org.springframework.stereotype.Repository"),
    SPRING_CONTROLLER(3, "org.springframework.stereotype.Controller"),
    SPRING_REST_CONTROLLER(3, "org.springframework.web.bind.annotation.RestController"),
    SPRING_AUTOWIRED(3, "org.springframework.beans.factory.annotation.Autowired"),
    SPRING_QUALIFIER(3, "org.springframework.beans.factory.annotation.Qualifier"),
    SPRING_VALUE(3, "org.springframework.beans.factory.annotation.Value"),
    SPRING_CONFIGURATION(3, "org.springframework.context.annotation.Configuration"),
    SPRING_BEAN(3, "org.springframework.context.annotation.Bean"),
    SPRING_SCOPE(3, "org.springframework.context.annotation.Scope"),
    SPRING_LAZY(3, "org.springframework.context.annotation.Lazy"),
    
    // Spring Data JPA (order 3)
    SPRING_JPA_REPOSITORY(3, "org.springframework.data.jpa.repository.JpaRepository"),
    SPRING_CRUD_REPOSITORY(3, "org.springframework.data.repository.CrudRepository"),
    SPRING_PAGING_AND_SORTING_REPOSITORY(3, "org.springframework.data.repository.PagingAndSortingRepository"),
    SPRING_QUERY(3, "org.springframework.data.jpa.repository.Query"),
    SPRING_MODIFYING(3, "org.springframework.data.jpa.repository.Modifying"),
    SPRING_PARAM(3, "org.springframework.data.repository.query.Param"),
    SPRING_PAGEABLE(3, "org.springframework.data.domain.Pageable"),
    SPRING_PAGE(3, "org.springframework.data.domain.Page"),
    SPRING_SORT(3, "org.springframework.data.domain.Sort"),
    
    // Spring Web (order 3)
    SPRING_REQUEST_MAPPING(3, "org.springframework.web.bind.annotation.RequestMapping"),
    SPRING_GET_MAPPING(3, "org.springframework.web.bind.annotation.GetMapping"),
    SPRING_POST_MAPPING(3, "org.springframework.web.bind.annotation.PostMapping"),
    SPRING_PUT_MAPPING(3, "org.springframework.web.bind.annotation.PutMapping"),
    SPRING_DELETE_MAPPING(3, "org.springframework.web.bind.annotation.DeleteMapping"),
    SPRING_REQUEST_BODY(3, "org.springframework.web.bind.annotation.RequestBody"),
    SPRING_REQUEST_PARAM(3, "org.springframework.web.bind.annotation.RequestParam"),
    SPRING_PATH_VARIABLE(3, "org.springframework.web.bind.annotation.PathVariable"),
    SPRING_RESPONSE_ENTITY(3, "org.springframework.http.ResponseEntity"),
    SPRING_HTTP_STATUS(3, "org.springframework.http.HttpStatus"),
    SPRING_HTTP_HEADERS(3, "org.springframework.http.HttpHeaders"),
    SPRING_MEDIA_TYPE(3, "org.springframework.http.MediaType"),
    
    // Spring Transaction (order 3)
    SPRING_TRANSACTIONAL(3, "org.springframework.transaction.annotation.Transactional"),
    SPRING_ISOLATION(3, "org.springframework.transaction.annotation.Isolation"),
    SPRING_PROPAGATION(3, "org.springframework.transaction.annotation.Propagation"),
    
    // Spring Security (order 3)
    SPRING_SECURITY_PREAUTHORIZE(3, "org.springframework.security.access.prepost.PreAuthorize"),
    SPRING_SECURITY_POSTAUTHORIZE(3, "org.springframework.security.access.prepost.PostAuthorize"),
    SPRING_AUTHENTICATION(3, "org.springframework.security.core.Authentication"),
    
    // Spring Boot (order 3)
    SPRING_BOOT_APPLICATION(3, "org.springframework.boot.SpringApplication"),
    SPRING_BOOT_AUTOCONFIGURE(3, "org.springframework.boot.autoconfigure.SpringBootApplication"),
    SPRING_CONFIGURATION_PROPERTIES(3, "org.springframework.boot.context.properties.ConfigurationProperties"),
    SPRING_ENABLE_CONFIGURATION_PROPERTIES(3, "org.springframework.boot.context.properties.EnableConfigurationProperties"),
    
    // Hibernate (order 4)
    HIBERNATE_ENTITY(4, "org.hibernate.annotations.Entity"),
    HIBERNATE_CACHE(4, "org.hibernate.annotations.Cache"),
    HIBERNATE_CACHE_CONCURRENCY_STRATEGY(4, "org.hibernate.annotations.CacheConcurrencyStrategy"),
    HIBERNATE_BATCH_SIZE(4, "org.hibernate.annotations.BatchSize"),
    HIBERNATE_FETCH(4, "org.hibernate.annotations.Fetch"),
    HIBERNATE_FETCH_MODE(4, "org.hibernate.annotations.FetchMode"),
    HIBERNATE_LAZY(4, "org.hibernate.annotations.Lazy"),
    HIBERNATE_FORMULA(4, "org.hibernate.annotations.Formula"),
    HIBERNATE_TYPE(4, "org.hibernate.annotations.Type"),
    HIBERNATE_NATURAL_ID(4, "org.hibernate.annotations.NaturalId"),
    HIBERNATE_IMMUTABLE(4, "org.hibernate.annotations.Immutable"),
    HIBERNATE_WHERE(4, "org.hibernate.annotations.Where"),
    HIBERNATE_FILTER(4, "org.hibernate.annotations.Filter"),
    HIBERNATE_FILTER_DEF(4, "org.hibernate.annotations.FilterDef"),
    HIBERNATE_PARAMETER(4, "org.hibernate.annotations.Parameter"),
    HIBERNATE_SQL_INSERT(4, "org.hibernate.annotations.SQLInsert"),
    HIBERNATE_SQL_UPDATE(4, "org.hibernate.annotations.SQLUpdate"),
    HIBERNATE_SQL_DELETE(4, "org.hibernate.annotations.SQLDelete"),
    HIBERNATE_CREATION_TIMESTAMP(4, "org.hibernate.annotations.CreationTimestamp"),
    HIBERNATE_UPDATE_TIMESTAMP(4, "org.hibernate.annotations.UpdateTimestamp"),
    
    // Lombok (order 5)
    LOMBOK_DATA(5, "lombok.Data"),
    LOMBOK_GETTER(5, "lombok.Getter"),
    LOMBOK_SETTER(5, "lombok.Setter"),
    LOMBOK_BUILDER(5, "lombok.Builder"),
    LOMBOK_BUILDER_DEFAULT(5, "lombok.Builder.Default"),
    LOMBOK_NO_ARGS_CONSTRUCTOR(5, "lombok.NoArgsConstructor"),
    LOMBOK_ALL_ARGS_CONSTRUCTOR(5, "lombok.AllArgsConstructor"),
    LOMBOK_REQUIRED_ARGS_CONSTRUCTOR(5, "lombok.RequiredArgsConstructor"),
    LOMBOK_TO_STRING(5, "lombok.ToString"),
    LOMBOK_EQUALS_AND_HASH_CODE(5, "lombok.EqualsAndHashCode"),
    LOMBOK_VALUE(5, "lombok.Value"),
    LOMBOK_WITH(5, "lombok.With"),
    LOMBOK_ACCESSOR(5, "lombok.experimental.Accessors"),
    LOMBOK_FIELD_DEFAULTS(5, "lombok.experimental.FieldDefaults"),
    LOMBOK_ACCESS_LEVEL(5, "lombok.AccessLevel"),
    LOMBOK_CLEANUP(5, "lombok.Cleanup"),
    LOMBOK_SNEAKY_THROWS(5, "lombok.SneakyThrows"),
    LOMBOK_SYNCHRONIZED(5, "lombok.Synchronized"),
    LOMBOK_VAL(5, "lombok.val"),
    LOMBOK_VAR(5, "lombok.var"),
    
    // Jackson (order 5)
    JACKSON_JSON_PROPERTY(5, "com.fasterxml.jackson.annotation.JsonProperty"),
    JACKSON_JSON_IGNORE(5, "com.fasterxml.jackson.annotation.JsonIgnore"),
    JACKSON_JSON_IGNORE_PROPERTIES(5, "com.fasterxml.jackson.annotation.JsonIgnoreProperties"),
    JACKSON_JSON_FORMAT(5, "com.fasterxml.jackson.annotation.JsonFormat"),
    JACKSON_JSON_SERIALIZE(5, "com.fasterxml.jackson.databind.annotation.JsonSerialize"),
    JACKSON_JSON_DESERIALIZE(5, "com.fasterxml.jackson.databind.annotation.JsonDeserialize"),
    
    // Apache Commons (order 5)
    COMMONS_LANG_STRING_UTILS(5, "org.apache.commons.lang3.StringUtils"),
    COMMONS_LANG_OBJECTS_UTILS(5, "org.apache.commons.lang3.ObjectUtils"),
    COMMONS_LANG_ARRAY_UTILS(5, "org.apache.commons.lang3.ArrayUtils"),
    COMMONS_LANG_COLLECTION_UTILS(5, "org.apache.commons.collections4.CollectionUtils"),
    COMMONS_LANG_BUILDER(5, "org.apache.commons.lang3.builder.ToStringBuilder"),
    COMMONS_LANG_HASH_CODE_BUILDER(5, "org.apache.commons.lang3.builder.HashCodeBuilder"),
    COMMONS_LANG_EQUALS_BUILDER(5, "org.apache.commons.lang3.builder.EqualsBuilder"),
    
    // Testing - JUnit 5 (order 6)
    JUNIT_TEST(6, "org.junit.jupiter.api.Test"),
    JUNIT_BEFORE_EACH(6, "org.junit.jupiter.api.BeforeEach"),
    JUNIT_AFTER_EACH(6, "org.junit.jupiter.api.AfterEach"),
    JUNIT_BEFORE_ALL(6, "org.junit.jupiter.api.BeforeAll"),
    JUNIT_AFTER_ALL(6, "org.junit.jupiter.api.AfterAll"),
    JUNIT_ASSERTIONS(6, "org.junit.jupiter.api.Assertions"),
    JUNIT_ASSUMPTIONS(6, "org.junit.jupiter.api.Assumptions"),
    JUNIT_DISPLAY_NAME(6, "org.junit.jupiter.api.DisplayName"),
    JUNIT_NESTED(6, "org.junit.jupiter.api.Nested"),
    JUNIT_PARAMETERIZED_TEST(6, "org.junit.jupiter.params.ParameterizedTest"),
    JUNIT_VALUE_SOURCE(6, "org.junit.jupiter.params.provider.ValueSource"),
    JUNIT_METHOD_SOURCE(6, "org.junit.jupiter.params.provider.MethodSource"),
    
    // Testing - Mockito (order 6)
    MOCKITO_MOCK(6, "org.mockito.Mock"),
    MOCKITO_MOCK_BEAN(6, "org.springframework.boot.test.mock.mockito.MockBean"),
    MOCKITO_INJECT_MOCKS(6, "org.mockito.InjectMocks"),
    MOCKITO_WHEN(6, "org.mockito.Mockito.when"),
    MOCKITO_VERIFY(6, "org.mockito.Mockito.verify"),
    MOCKITO_ANY(6, "org.mockito.ArgumentMatchers.any"),
    MOCKITO_ARGUMENT_CAPTOR(6, "org.mockito.ArgumentCaptor"),
    
    // Testing - AssertJ (order 6)
    ASSERTJ_ASSERTIONS(6, "org.assertj.core.api.Assertions"),
    ASSERTJ_SOFT_ASSERTIONS(6, "org.assertj.core.api.SoftAssertions"),
    
    // Testing - Spring Boot Test (order 6)
    SPRING_BOOT_TEST(6, "org.springframework.boot.test.context.SpringBootTest"),
    SPRING_TEST_CONTEXT(6, "org.springframework.test.context.TestExecutionListeners"),
    SPRING_TRANSACTIONAL_TEST(6, "org.springframework.transaction.annotation.Transactional"),
    SPRING_ROLLBACK(6, "org.springframework.test.annotation.Rollback"),
    
    // OpenAPI/Swagger (order 6)
    SWAGGER_API(6, "io.swagger.v3.oas.annotations.Operation"),
    SWAGGER_API_RESPONSE(6, "io.swagger.v3.oas.annotations.responses.ApiResponse"),
    SWAGGER_PARAMETER(6, "io.swagger.v3.oas.annotations.Parameter"),
    SWAGGER_SCHEMA(6, "io.swagger.v3.oas.annotations.media.Schema"),
    SWAGGER_TAG(6, "io.swagger.v3.oas.annotations.tags.Tag"),
    
    // MapStruct (order 6)
    MAPSTRUCT_MAPPER(6, "org.mapstruct.Mapper"),
    MAPSTRUCT_MAPPING(6, "org.mapstruct.Mapping"),
    MAPSTRUCT_MAPPINGS(6, "org.mapstruct.Mappings"),
    MAPSTRUCT_FACTORY(6, "org.mapstruct.factory.Mappers"),
    
    // SLF4J Logging (order 6)
    SLF4J_LOGGER(6, "org.slf4j.Logger"),
    SLF4J_LOGGER_FACTORY(6, "org.slf4j.LoggerFactory");

    private final int order;
    private final String value;

    TemplateImport(int order, String value) {
        this.order = order;
        this.value = value;
    }
}