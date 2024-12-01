# Military Database Management System

This project is a Military Database Management System designed to store and manage information regarding soldiers, divisions, vehicles, missions, and more. The application provides a user-friendly interface to view, edit, and manage military data using JavaFX and an embedded SQLite database.

## Features
- **View and Manage Data**: Retrieve, add, update, and delete information about soldiers, vehicles, missions, operations and more.
- **Role-based Access**: Different functionalities for administrators and commanders.
- **Interactive UI**: A JavaFX-based user interface for effective data management with custom filters for each table.
- **Custom SQL Execution**: Admin users can run custom SQL queries directly from the UI.

## Prerequisites
- **Java 11 or higher**: The project requires Java 11 or a newer version to run successfully.
- **Maven**: Apache Maven is used for building the project and managing dependencies.
- **SQLite**: The database used is SQLite, which is managed within the project.

## Getting Started

Follow the steps below to set up and run the Military Database Management System.

### Step 1: Set Up the Database
The first step is to create and populate the SQLite database with the required tables and initial data.

1. Execute the Python script `CreateAndFillDatabase.py` to create the database (`army.db`) and populate it.

```sh
python3 CreateAndFillDatabase.py
```

This script will:
- Drop the existing database if it already exists.
- Execute the SQL commands in `CREATE_TABLE.sql` to create the schema.
- Execute a series of `INSERT` SQL scripts to populate the tables with sample data.

### Step 2: Build the Project
After the database is set up, the next step is to build the Java project using Maven.

1. Run the Maven clean and install commands:

```sh
mvn clean install
```

This command will compile the Java source code, run tests, and package the application.

### Step 3: Run the Application
Finally, run the JavaFX application using Maven.

```sh
mvn clean javafx:run
```

This command will start the JavaFX application, allowing you to interact with the military database through the provided GUI.

## Project Structure
- **src/main/java**: Contains the Java source code organized by package.
  - `com.tableforge.queries`: Contains classes such as `SoldierQueries.java`, `VehicleQueries.java`, and others to interact with the database.
  - `com.tableforge.models`: Contains Java classes representing the different entities in the database.
  - `com.tableforge`: Contains the main application files (`App.java`, `MainView.java`, etc.).
- **src/main/resources**: Contains the resources such as the CSS styles for the application.
- **database**: Contains the `CREATE_TABLE.sql` and multiple `INSERT` SQL scripts for setting up and populating the database.
- **CreateAndFillDatabase.py**: Python script used to create the database schema and populate it with initial data.

## Technologies Used
- **Java 11+**: Core programming language for application development.
- **JavaFX**: Used for building the graphical user interface.
- **SQLite**: Embedded database for storing military data.
- **Maven**: Build and dependency management tool.
- **Python**: Script used to create and populate the SQLite database.

## Usage
- **Login**: Use the default credentials provided in the `LoginView` (e.g., `admin/admin` or `commander/commander` for admin and commander roles respectively).
- **Role-based Views**: After logging in, users will have access to different views based on their role. Admins can run custom SQL commands, while commanders can manage specific tables.

## Troubleshooting
- **Java Version**: Ensure you have Java 11 or newer installed.
- **Database Errors**: Make sure to execute `CreateAndFillDatabase.py` before running the JavaFX application to create and populate the database (`army.db`).
- **Maven Issues**: Ensure Maven is installed and properly configured in your system's PATH.
- **SQLite**: Make sure you have SQLite installed in your system.

## Acknowledgments
This project was developed to provide an efficient management tool for military-related data, focusing on ease of use, scalability, and portability.


