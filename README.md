# Crop Management System

## Project Overview
A comprehensive Java-based crop management system that helps farmers track their crops from planting to harvest and sales. This system addresses the real-world problem of agricultural management and crop tracking.

## Problem Statement
Farmers often struggle with:
- Tracking multiple crops and their growth stages
- Managing harvest data and inventory
- Recording sales and financial information
- Monitoring crop performance over time

## Features
- User authentication (Farmer/Admin roles)
- Crop lifecycle management
- Harvest tracking and inventory management
- Sales recording and reporting
- Real-time notifications using Observer pattern

## Technology Stack
- **Language**: Java 17
- **Database**: PostgreSQL
- **Build Tool**: Apache Ant
- **Testing**: JUnit 5
- **Containerization**: Docker & Docker Compose
- **Version Control**: Git

## Design Patterns Used
- **Observer Pattern**: Implemented for crop status notifications and event handling
- **DAO Pattern**: Data Access Object pattern for database operations
- **MVC Pattern**: Model-View-Controller architecture

## Architecture
```
src/
├── controller/     # Business logic controllers
├── dao/           # Data Access Objects
├── model/         # Entity models
├── observer/      # Observer pattern implementation
├── util/          # Utility classes
└── view/          # User interface components
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL 15+
- Docker (optional)
- Git

### Local Development Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd crop-management-system
   ```

2. Configure database connection in `src/dao/DatabaseConnection.java`

3. Build the project:
   ```bash
   ant compile
   ```

4. Run the application:
   ```bash
   java -cp build/classes view.LoginPage
   ```

### Docker Setup
1. Build and run with Docker Compose:
   ```bash
   docker-compose up --build
   ```

2. The application will be available with PostgreSQL database automatically configured.

## Testing
Run unit tests:
```bash
ant test
```

Test coverage includes:
- Model validation tests
- Controller business logic tests
- DAO integration tests

## Database Schema
- **users**: User authentication and profiles
- **crop_types**: Predefined crop categories
- **crops**: Individual crop records
- **sales**: Sales transactions

## Contributing
1. Follow Google Java Style Guide
2. Write unit tests for new features
3. Use meaningful commit messages
4. Create pull requests for code review

## License
This project is for educational purposes.