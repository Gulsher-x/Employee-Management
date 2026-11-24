# Employee Management System

A full-stack Employee Management System built with **Spring Boot** and **React (Vite)**. The backend provides REST APIs with Spring Security and MongoDB, while the frontend offers a separate web interface.

## Features

- Employee record management
- RESTful backend APIs
- MongoDB data storage
- Spring Security integration
- React-based frontend
- Maven Wrapper for backend builds
- Responsive Vite development server

## Tech Stack

### Backend

- Java 17
- Spring Boot 3.4.0
- Spring Web
- Spring Security
- Spring Data MongoDB
- Lombok
- Maven

### Frontend

- React
- Vite
- JavaScript
- ESLint

## Prerequisites

Install the following before running the project:

- [Java 17](https://adoptium.net/)
- [MongoDB](https://www.mongodb.com/try/download/community) running locally or a MongoDB connection URI
- [Node.js](https://nodejs.org/) 18 or newer
- npm (included with Node.js)

## Project Structure

```text
Employee-Management/
├── src/main/java/       # Spring Boot application code
├── src/main/resources/  # Backend configuration
├── src/test/            # Backend tests
├── frontend/            # React and Vite application
├── pom.xml              # Maven configuration
├── mvnw                 # Maven Wrapper for Unix/macOS
└── mvnw.cmd             # Maven Wrapper for Windows
```

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Gulsher-x/Employee-Management.git
cd Employee-Management
```

### 2. Start MongoDB

Start MongoDB locally on its default port:

```text
mongodb://localhost:27017
```

If your MongoDB instance uses a different host or credentials, add a matching connection configuration to `src/main/resources/application.properties`.

### 3. Run the Backend

#### Windows

```bash
mvnw.cmd spring-boot:run
```

#### Linux or macOS

```bash
./mvnw spring-boot:run
```

The backend will typically start at:

```text
http://localhost:8080
```

### 4. Run the Frontend

Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

The frontend will typically be available at:

```text
http://localhost:5173
```

## Available Commands

### Backend

```bash
# Run the application
./mvnw spring-boot:run

# Build the JAR
./mvnw clean package

# Run tests
./mvnw test
```

On Windows, replace `./mvnw` with `mvnw.cmd`.

### Frontend

```bash
cd frontend

npm install       # Install dependencies
npm run dev       # Start development server
npm run build     # Create production build
npm run lint      # Run ESLint
npm run preview   # Preview production build
```

## Configuration

Backend configuration is stored in:

```text
src/main/resources/application.properties
```

A typical MongoDB configuration is:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/employee_management
server.port=8080
```

The frontend API URL may need to match the backend host and port. Check API calls in:

```text
frontend/src/
```

## Build for Production

### Backend

```bash
./mvnw clean package
```

The generated JAR will be available in:

```text
target/
```

### Frontend

```bash
cd frontend
npm run build
```

The production files will be generated in:

```text
frontend/dist/
```

## Testing

Run backend tests with:

```bash
./mvnw test
```

Run frontend lint checks with:

```bash
cd frontend
npm run lint
```

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Run tests and lint checks.
5. Open a pull request.

## License

No license has been specified for this repository.
