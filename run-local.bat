@echo off
echo Starting Crop Management System locally...

echo Step 1: Starting PostgreSQL with Docker
docker run -d --name crop-postgres -p 5432:5432 -e POSTGRES_DB=rodrigue -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=12345 postgres:15-alpine

echo Step 2: Waiting for database to start...
timeout /t 10

echo Step 3: Creating database schema...
docker exec -i crop-postgres psql -U postgres -d rodrigue < database\ultimate_schema.sql

echo Step 4: Compiling Java application...
cd "Crop Managment System 1"
javac -cp "..\lib\postgresql-42.7.1.jar" -d build\classes src\dao\*.java src\model\*.java src\controller\*.java src\observer\*.java src\util\*.java src\view\*.java

echo Step 5: Running application...
java -cp "build\classes;..\lib\postgresql-42.7.1.jar" view.LoginPage

echo Cleaning up...
docker stop crop-postgres
docker rm crop-postgres