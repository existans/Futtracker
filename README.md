# Futtracker
This project is a Java website written using Spring Boot.

## Requirements
- PostgreSQL
- PostgreSQL Maven
- Maven
- JDK 21

## Installation
1. Clone the Repository: git clone https://github.com/existans/Futtracker
2. Create new database in postresSQL with name "Football"
3. Run Database Migrations: Use the provided SQL schema to create the necessary tables:

CREATE TABLE team (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE position (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE player (
    id SERIAL PRIMARY KEY,
    team_id INT NOT NULL,
    position_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    nationality VARCHAR(100),
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES position(id) ON DELETE CASCADE
);

4. Open project in the IDE
5. Add user and password to enviroment variables
6. Package the project with Maven: mvn clean package
7. Start the project and open it in a browser at http://localhost:8080
