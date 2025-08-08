# EntityGen

**Advanced JPA Entity Code Generator with Intelligent Relationship Management**

EntityGen is a sophisticated Maven plugin that automatically generates complete Spring Boot application layers from your MySQL database schema. It features intelligent relationship detection, composite key support, and modern JPA best practices with automatic code formatting.

## 🛠️ How It Works

EntityGen analyzes your MySQL database schema and automatically generates:
- **Complete JPA entities** with proper relationships and annotations
- **Composite primary key classes** with `@EmbeddedId` and `@MapsId` support
- **DTOs with circular reference prevention** for clean API layers
- **Spring Data repositories** with basic CRUD operations
- **Service interfaces and implementations** for business logic
- **REST controllers** with GET endpoints and proper exception handling

### Project structure

EntityGen is organized into **three main modules**:
- **entitygen-core** - Core generation engine, template processing, and rendering strategies
- **entitygen-template** - Relationship builders, metadata extraction, and Freemarker templates
- **entitygen-maven-plugin** - Maven plugin wrapper and Spring configuration integration

### Generation Workflow

```
1. Configuration Loading
   ├── Read mandatory settings (springConfigPath, basePackage)
   ├── Parse application.properties for EntityGen configuration
   └── Override defaults with user-specified settings

2. Database Metadata Extraction
   ├── Connect to MySQL using DatabaseMetaData
   ├── Extract schemas, tables, columns, data types
   ├── Analyze primary keys, foreign keys, and constraints
   └── Map database types to Java types

3. Entity Model Creation
   ├── Create internal entity representations
   ├── Analyze and detect all relationship types
   ├── Handle composite keys and association entities
   └── Ensure bidirectional relationship consistency

4. Template Model Generation
   ├── Build template models for each component type
   ├── Generate field mappings and annotations
   ├── Resolve imports and dependencies
   └── Apply naming conventions and suffixes

5. Code Generation & Formatting
   ├── Process Freemarker templates for each component
   ├── Generate Java source code
   ├── Apply Eclipse formatter for consistent styling
   └── Optimize and organize imports systematically
```

## 📋 Requirements & Setup

### Prerequisites
- Java 21+
- Maven 3.6+
- MySQL 8.0+ (currently supported database)
- Spring Boot 3.x project
- Spring Data JPA

### Installation

1. **Clone and install the plugin**:
```bash
git clone https://github.com/lknezevicc/EntityGen.git
cd EntityGen
mvn clean install
```

2. **Add plugin to your Spring Boot project's `pom.xml`**:
```xml
<plugin>
    <groupId>hr.lknezevic.entitygen</groupId>
    <artifactId>entitygen-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.3.0</version>
        </dependency>
    </dependencies>
    <configuration>
        <springConfigPath>${project.basedir}/src/main/resources/application.properties</springConfigPath>
        <basePackage>com.yourcompany.yourproject</basePackage>
        <activeProfile>dev</activeProfile> <!-- Optional -->
    </configuration>
</plugin>
```

3. **Configure database connection in `application.properties`**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

4. **Generate code**:
```bash
# Standard generation
mvn entitygen:generate

# With detailed logging
mvn entitygen:generate -X

# With debugger support
mvnDebug entitygen:generate
```

5. **Generated structure** (by default):
```
src/main/java/
├── com/yourcompany/yourproject/
│   ├── entity/          # JPA Entities
│   ├── embeddable/      # Composite Key classes
│   ├── dto/             # Data Transfer Objects
│   ├── repository/      # Spring Data repositories
│   ├── service/         # Service interfaces
│   ├── service/impl/    # Service implementations
│   └── controller/      # REST Controllers
```

## ⚙️ Configuration

EntityGen supports extensive customization through Spring Boot properties in your `application.properties`:

```properties
# Package locations
entitygen.package.entity=com.yourcompany.yourproject.entity
entitygen.package.dto=com.yourcompany.yourproject.dto
entitygen.package.repository=com.yourcompany.yourproject.repository
entitygen.package.service=com.yourcompany.yourproject.service
entitygen.package.controller=com.yourcompany.yourproject.controller

# Generation features
entitygen.features.generate-all-components=true
entitygen.features.overwrite-existing=true
entitygen.features.javadoc.author=EntityGen Maven Plugin

# Schema filtering
entitygen.schema.target=your_schema
entitygen.table.include=users,orders,products
```

## 🎯 Advanced Features

### Intelligent Relationship Management
- **Smart Detection** - Automatically distinguishes junction tables, composite foreign keys, and self-referencing relations
- **Association Entity Pattern** - Handles junction tables with additional columns as proper JPA entities
- **Composite Key Support** - Full `@EmbeddedId` and `@MapsId` support with automatic `insertable=false, updatable=false`

### Code Quality & Technical Features
- **Lombok Integration** - Clean code with `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **Eclipse Formatter** - Automatic code formatting and import optimization with circular dependency resolution
- **Freemarker Templates** - Flexible, customizable code generation with strategy pattern architecture
- **SLF4J Logging** - Comprehensive operation tracking with configurable log levels
- **JPA Best Practices** - Intelligent cascade strategies and consistent annotation patterns
