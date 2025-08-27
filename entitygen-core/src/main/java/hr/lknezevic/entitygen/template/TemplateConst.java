package hr.lknezevic.entitygen.template;

import hr.lknezevic.entitygen.enums.CollectionType;

/**
 * Centralized template formats for different component types.
 * Uses String.format() placeholders for dynamic content injection.
 */
public class TemplateConst {

    private TemplateConst() {
        // Private constructor to prevent instantiation
    }

    public static final String NEW_LINE = " \n";
    public static final String COMMA_JOIN = ", ";
    public static final String COMMA_JOIN_NEWLINE = ", \n";

    public static final String COMPONENT_NAME = "%s%s";
    public static final String SIMPLE_IMPORT = "%s.%s";
    public static final String IMPORT_STATEMENT = "import %s;";

    public static final String RELATION_FIELD_SINGLE = "%s %sId";
    public static final String RELATION_FIELD_COLLECTION = "%s<%s> %sIds";

    public static final String SIMPLE_PRIMARY_KEY = """
            @Id
            @Column(name = "%s")
            private %s %s;""";
    
    public static final String AUTO_INCREMENT_PRIMARY_KEY = """
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "%s")
            private %s %s;""";
    
    public static final String COMPOSITE_PRIMARY_KEY = """
            @EmbeddedId
            @Builder.Default
            private %s %s = new %s();""";

    public static final String SERVICE_FIND_ALL_METHOD = "List<%s> findAll();";
    public static final String SERVICE_GETTER_FIELD = "entity.get%s()";
    public static final String SERVICE_PK_LAMBDA_SIMPLE = "entity.get%s() != null ? entity.get%s().get%s() : null";

    public static final String SERVICE_RELATION_SET = """
            entity.get%s() != null ?
            entity.get%s().stream()
                .map(item -> item.get%s())
                .collect(Collectors.toSet()) :
            new HashSet<>()
            """;

    public static final String SERVICE_RELATION_LINKED_HASH_SET = """
            entity.get%s() != null ?
            entity.get%s().stream()
                .map(item -> item.get%s())
                .collect(Collectors.toSet()) :
            new LinkedHashSet<>()
            """;

    public static final String SERVICE_RELATION_LIST = """
            entity.get%s() != null ?
            entity.get%s().stream()
                .map(item -> item.get%s())
                .collect(Collectors.toList()) :
            new ArrayList<>()
            """;

    public static final String FIELD_COMMENT = """
            /**
             * Column: %s
             *
             */""";

    public static final String SIMPLE_FIELD = "%s %s";
    
    public static final String REGULAR_FIELD = """
            @Column(%s)
            private %s %s;""";
    
    public static final String FIELD_WITH_DEFAULT = """
            @Column(%s)
            @Builder.Default
            private %s %s = %s;""";
    
    public static final String LOB_FIELD = """
            @Lob
            @Column(%s)
            private %s %s;""";
    
    public static final String RELATION_COMMENT = """
            /**
             * %s relation towards %s
             */""";
    
    public static final String MANY_TO_ONE_RELATION = """
            @ManyToOne%s
            %sprivate %s %s;""";
    
    public static final String ONE_TO_ONE_RELATION = """
            @OneToOne%s
            %sprivate %s %s;""";
    
    public static final String ONE_TO_MANY_RELATION = """
            @OneToMany%s
            @ToString.Exclude
            @EqualsAndHashCode.Exclude
            @Builder.Default
            private %s<%s> %s = %s;""";
    
    public static final String MANY_TO_MANY_RELATION = """
            @ManyToMany%s
            %s@ToString.Exclude
            @EqualsAndHashCode.Exclude
            @Builder.Default
            private %s<%s> %s = %s;""";

    public static final String MANY_TO_MANY_MAPPED_BY_RELATION = """
            @ManyToMany(mappedBy = "%s")
            @ToString.Exclude
            @EqualsAndHashCode.Exclude
            @Builder.Default
            private %s<%s> %s = %s;""";
    
    public static final String JOIN_COLUMN_SINGLE = """
            @JoinColumn(name = "%s", referencedColumnName = "%s"%s)""";
    
    public static final String JOIN_COLUMNS_MULTIPLE = """
            @JoinColumns({
            %s
            })""";
    
    public static final String JOIN_TABLE = """
            @JoinTable(
                name = "%s",
                joinColumns = %s,
                inverseJoinColumns = %s
            )""";
    
    public static final String COLUMN_PARAM_NAME = "name = \"%s\"";
    public static final String COLUMN_PARAM_NULLABLE = "nullable = %s";
    public static final String COLUMN_PARAM_UNIQUE = "unique = %s";
    public static final String COLUMN_PARAM_LENGTH = "length = %d";
    public static final String COLUMN_PARAM_PRECISION = "precision = %d";
    public static final String COLUMN_PARAM_SCALE = "scale = %d";
    public static final String COLUMN_PARAM_LENGTH_LARGE = "columnDefinition = \"TEXT\"";
    
    public static final String RELATION_PARAM_FETCH = "fetch = FetchType.%s";
    public static final String RELATION_PARAM_CASCADE = "cascade = %s";
    public static final String RELATION_PARAM_OPTIONAL = "optional = %s";
    public static final String RELATION_PARAM_MAPPED_BY = "mappedBy = \"%s\"";
    public static final String RELATION_PARAM_ORPHAN_REMOVAL = "orphanRemoval = %s";
    public static final String RELATION_PARAM_UPDATABLE_INSERTABLE = "updatable = %s, insertable = %s";

    public static final String REPOSITORY_IMPLEMENTATION = "%s extends JpaRepository<%s, %s>";

    public static final String CONTROLLER_GET_ALL = """
            @GetMapping
            public ResponseEntity<List<%s>> getAll() {
                List<%s> entities = service.findAll();
                return entities.isEmpty() 
                    ? ResponseEntity.noContent().build() 
                    : ResponseEntity.ok(entities);
            }""";
    
    public static String getCollectionImplementation(CollectionType collectionType) {
        return switch (collectionType) {
            case LIST -> "new ArrayList<>()";
            case SET -> "new HashSet<>()";
            case LINKED_HASH_SET -> "new LinkedHashSet<>()";
        };
    }

    public static final String DEFAULT_REFERENCED_COLUMN = "id";
    public static final String BUILDER_DEFAULT = "@Builder.Default";
    public static final String MAPS_ID = "@MapsId";
    public static final String MAPS_ID_WITH_VALUE = "@MapsId(\"%s\")";
}
