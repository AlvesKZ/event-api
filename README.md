# Event API

Complete REST API for event management with image upload, coupon system, and address handling.

## About the Project

Event API is a robust backend application built with Spring Boot for managing events, allowing creation, listing, filtering, and discount coupon management. The application includes AWS S3 integration for image storage and full pagination support.

## Features

## Features

- ✅ Event creation with image upload
- ✅ Paginated listing of upcoming events
- ✅ Advanced event filtering (title, city, state, date)
- ✅ Discount coupon system linked to events
- ✅ Support for remote and in-person events
- ✅ Address management for in-person events
- ✅ Image upload to AWS S3
- ✅ Complete event details with valid coupons

## Technologies

## Technologies

### Backend
- **Java 21** - Programming language
- **Spring Boot 4.0.2** - Main framework
    - Spring Data JPA - Data persistence
    - Spring Web MVC - REST APIs
    - Spring DevTools - Development
- **PostgreSQL 16** - Relational database
- **Flyway 9.8.1** - Database versioning
- **Lombok** - Boilerplate code reduction
- **AWS SDK for S3 1.12.320** - Image upload

### DevOps & Tools
- **Docker & Docker Compose** - Containerization
- **Maven** - Dependency management
- **H2 Database** - In-memory database for tests
- **Spring DotEnv** - Environment variable management

## Project Structure

## Project Structure

```
src/
├── main/
│   ├── java/com/eventapi/event_api/
│   │   ├── config/
│   │   │   └── AWSConfig.java              # AWS S3 configuration
│   │   ├── controllers/
│   │   │   ├── EventController.java        # Event endpoints
│   │   │   └── CouponController.java       # Coupon endpoints
│   │   ├── domain/
│   │   │   ├── event/
│   │   │   │   ├── Event.java              # Event entity
│   │   │   │   ├── EventRequestDTO.java    # Request DTO
│   │   │   │   ├── EventResponseDTO.java   # Response DTO
│   │   │   │   └── EventDetailsDTO.java    # Details DTO
│   │   │   ├── coupon/
│   │   │   │   ├── Coupon.java             # Coupon entity
│   │   │   │   └── CouponRequestDTO.java   # Request DTO
│   │   │   └── address/
│   │   │       └── Address.java            # Address entity
│   │   ├── repositories/
│   │   │   ├── EventRepository.java        # Event repository
│   │   │   ├── CouponRepository.java       # Coupon repository
│   │   │   └── AddressRepository.java      # Address repository
│   │   ├── services/
│   │   │   ├── EventService.java           # Event business logic
│   │   │   ├── CouponService.java          # Coupon business logic
│   │   │   └── AddressService.java         # Address business logic
│   │   └── EventApiApplication.java        # Main class
│   └── resources/
│       ├── db/migrations/
│       │   ├── V1__create-event-table.sql
│       │   ├── V2__create-coupon-tables.sql
│       │   └── V3__create-address-tables.sql
│       ├── application.properties
│       └── application-dev.yml
└── test/
    └── java/com/eventapi/event_api/
        └── EventApiApplicationTests.java
```

## Data Model

## Data Model

### Entities

#### Event
```java
{
  "id": "UUID",
  "title": "String",
  "description": "String",
  "imgUrl": "String",
  "eventUrl": "String",
  "remote": "Boolean",
  "date": "Date",
  "address": "Address (OneToOne)"
}
```

#### Address
```java
{
  "id": "UUID",
  "city": "String",
  "uf": "String",
  "event": "Event (OneToOne)"
}
```

#### Coupon
```java
{
  "id": "UUID",
  "code": "String",
  "discount": "Integer",
  "valid": "Date",
  "event": "Event (ManyToOne)"
}
```

### Relationships
- Event ↔ Address: 1:1 relationship (in-person events have address)
- Event → Coupon: 1:N relationship (one event can have multiple coupons)

## Installation and Setup

### Prerequisites

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose
- AWS Account (for S3 usage)

### 1️⃣ Clone the Repository

```bash
git clone <repository-url>
cd event-api
```

### 2️⃣ Configure Environment Variables

Create a `.env` file in the project root:

```env
# Database
POSTGRES_DB=eventapi
POSTGRES_USER=alveskz
POSTGRES_PASSWORD=password

# Spring DataSource
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/eventapi
SPRING_DATASOURCE_USERNAME=alveskz
SPRING_DATASOURCE_PASSWORD=password

# AWS Credentials
AWS_REGION=us-east-1
AWS_BUCKET_NAME=your-bucket-name
AWS_ACCESS_KEY_ID=your_access_key_id
AWS_SECRET_ACCESS_KEY=your_secret_access_key
```

### 3️⃣ Start the Database

```bash
docker-compose up -d
```

This will:
- ✅ Create a PostgreSQL 16 Alpine container
- ✅ Configure the `eventapi` database
- ✅ Expose port 5432
- ✅ Create a persistent volume for data
- ✅ Configure the `eventapi-network` network

### 4️⃣ Run Migrations

Flyway migrations run automatically when starting the application. Scripts include:
- **V1**: Create `event` table
- **V2**: Create `coupon` table
- **V3**: Create `address` table

### 5️⃣ Compile and Run the Application

```bash
# Using Maven Wrapper
./mvnw clean install
./mvnw spring-boot:run

# Or building JAR
./mvnw clean package
java -jar target/event-api-0.0.1-SNAPSHOT.jar
```

Application will be available at: **http://localhost:8080**

## API Endpoints

### Events

#### Create Event
```http
POST /api/event
Content-Type: multipart/form-data

Parameters:
- title (required): String
- description (optional): String
- date (required): Long (timestamp)
- city (required): String
- state (required): String
- remote (required): Boolean
- eventUrl (required): String
- image (optional): MultipartFile
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/event \
  -F "title=Tech Conference 2026" \
  -F "description=Technology conference" \
  -F "date=1735689600000" \
  -F "city=São Paulo" \
  -F "state=SP" \
  -F "remote=false" \
  -F "eventUrl=https://techconf.com" \
  -F "image=@/path/to/image.jpg"
```

#### List Upcoming Events
```http
GET /api/event?page=0&size=10

Response:
[
  {
    "id": "uuid",
    "title": "string",
    "description": "string",
    "date": "date",
    "city": "string",
    "uf": "string",
    "remote": boolean,
    "eventUrl": "string",
    "imgUrl": "string"
  }
]
```

#### Event Details
```http
GET /api/event/{eventId}

Response:
{
  "id": "uuid",
  "title": "string",
  "description": "string",
  "date": "date",
  "city": "string",
  "uf": "string",
  "imgUrl": "string",
  "eventUrl": "string",
  "coupons": [
    {
      "code": "string",
      "discount": integer,
      "valid": "date"
    }
  ]
}
```

#### Filter Events
```http
GET /api/event/filter?page=0&size=10&title=tech&city=São Paulo&uf=SP&startDate=2026-01-01&endDate=2026-12-31

Optional parameters:
- page: page number (default: 0)
- size: items per page (default: 10)
- title: filter by title
- city: filter by city
- uf: filter by state
- startDate: start date (format: yyyy-MM-dd)
- endDate: end date (format: yyyy-MM-dd)
```

### Coupons

#### Add Coupon to Event
```http
POST /api/coupon/ebent/{eventId}
Content-Type: application/json

{
  "code": "DISCOUNT10",
  "discount": 10,
  "valid": 1735689600000
}

Response:
{
  "id": "uuid",
  "code": "DISCOUNT10",
  "discount": 10,
  "valid": "date",
  "event": { ... }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/coupon/ebent/{eventId} \
  -H "Content-Type: application/json" \
  -d '{
    "code": "DISCOUNT10",
    "discount": 10,
    "valid": 1735689600000
  }'
```

## AWS S3 Configuration

### 1. Create S3 Bucket
```bash
aws s3 mb s3://your-bucket-name --region us-east-1
```

### 2. Configure Permissions

Example bucket policy:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::your-bucket-name/*"
    }
  ]
}
```

### 3. Configure Credentials

Option 1: `.env` file
```env
AWS_ACCESS_KEY_ID=your_key
AWS_SECRET_ACCESS_KEY=your_secret
```

Option 2: AWS CLI
```bash
aws configure
```

## Docker

### Docker Compose Configuration

The `docker-compose.yml` file configures:

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: eventapi-postgres
    environment:
      POSTGRES_DB: eventapi
      POSTGRES_USER: alveskz
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - eventapi-network
```

### Useful Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f postgres

# Access PostgreSQL
docker exec -it eventapi-postgres psql -U alveskz -d eventapi

# Remove volumes (warning: deletes data)
docker-compose down -v
```

## Tests

### Running Tests

```bash
# All tests
./mvnw test

# Specific tests
./mvnw test -Dtest=EventApiApplicationTests
```

### Test Database

Tests use H2 Database in-memory, configured in:
```
src/test/resources/application-test.properties
```

## Database Migrations

### Table Structure

#### Table: event
```sql
CREATE TABLE event (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(250) NOT NULL,
    img_url VARCHAR(100) NOT NULL,
    event_url VARCHAR(100) NOT NULL,
    date TIMESTAMP NOT NULL,
    remote BOOLEAN NOT NULL
);
```

#### Table: address
```sql
CREATE TABLE address (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    uf VARCHAR(100) NOT NULL,
    event_id UUID,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);
```

#### Table: coupon
```sql
CREATE TABLE coupon (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    discount INTEGER NOT NULL,
    valid TIMESTAMP NOT NULL,
    event_id UUID,
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
);
```

### Create New Migration

1. Create a file in `src/main/resources/db/migration/`
2. Name following the pattern: `V{number}__{description}.sql`
3. Example: `V4__add_event_category.sql`

```sql
ALTER TABLE event ADD COLUMN category VARCHAR(50);
```

## Development

### Recommended Tools

- **IDE**: IntelliJ IDEA, Eclipse or VS Code
- **REST Client**: Postman, Insomnia or Thunder Client
- **PostgreSQL Client**: pgAdmin, DBeaver or TablePlus
- **Git**: Version control

### Hot Reload

Spring DevTools is configured to automatically reload the application during development.

### Spring Profiles

```bash
# Development
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Production
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Best Practices

## Best Practices

- ✅ Use DTOs to separate API layer from entities
- ✅ Validate input data in controllers
- ✅ Implement custom exception handling
- ✅ Document API with Swagger/OpenAPI
- ✅ Implement structured logging
- ✅ Add database indexes for frequent queries
- ✅ Implement caching for heavy queries
- ✅ Use transactions for critical operations

## Troubleshooting

### Problem: Error connecting to PostgreSQL
```bash
# Check if container is running
docker ps

# Check container logs
docker-compose logs postgres

# Restart container
docker-compose restart postgres
```

### Problem: Flyway migration failed
```bash
# Clear migrations and restart (WARNING: deletes data)
docker-compose down -v
docker-compose up -d
```

### Problem: Error uploading to S3
- Check AWS credentials
- Confirm bucket exists and has correct permissions
- Verify configured region

## License

This project is under the MIT license. See the `LICENSE` file for more details.

## Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Contact

**Developer**: alveskz  
**Email**: [your-email@example.com]

---

Developed with Spring Boot