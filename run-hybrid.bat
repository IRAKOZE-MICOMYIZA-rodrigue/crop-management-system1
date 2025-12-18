@echo off
echo ========================================
echo   CROP MANAGEMENT SYSTEM - HYBRID MODE
echo ========================================

echo Step 1: Starting PostgreSQL in Docker...
docker-compose -f docker-compose.db-only.yml up -d

echo Step 2: Waiting for database to be ready...
timeout /t 15

echo Step 3: Downloading JDBC driver (if needed)...
if not exist "lib" mkdir lib
if not exist "lib\postgresql-42.7.1.jar" (
    echo Downloading PostgreSQL JDBC driver...
    powershell -Command "Invoke-WebRequest -Uri 'https://jdbc.postgresql.org/download/postgresql-42.7.1.jar' -OutFile 'lib\postgresql-42.7.1.jar'"
)

echo Step 4: Compiling Java application...
cd "Crop Managment System 1"
if not exist "build\classes" mkdir build\classes
javac -cp "..\lib\postgresql-42.7.1.jar" -d build\classes src\dao\*.java src\model\*.java src\controller\*.java src\observer\*.java src\util\*.java src\view\*.java

echo Step 5: Starting Crop Management System...
echo Login with: admin@cropmanagement.com / admin123
java -cp "build\classes;..\lib\postgresql-42.7.1.jar" view.LoginPage

echo.
echo To stop database: docker-compose -f docker-compose.db-only.yml down
pause