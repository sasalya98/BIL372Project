import os
import sqlite3

# Database file
db_path = 'army.db'

# Drop the database if it exists
if os.path.exists(db_path):
    os.remove(db_path)
    print(f"Dropped the database: {db_path}")

# Path to the directory containing SQL files
data_path = './'  # Update this to your actual directory path

# Create a new database
connection = sqlite3.connect(db_path)
cursor = connection.cursor()

# Execute CREATE_TABLE.sql if it exists
create_table_file_path = os.path.join(data_path, 'CREATE_TABLE.sql')
if os.path.exists(create_table_file_path):
    print(f"Executing {create_table_file_path}")
    with open(create_table_file_path, 'r') as sql_file:
        sql_script = sql_file.read()
        try:
            cursor.executescript(sql_script)
            print("Executed: CREATE_TABLE.sql")
        except Exception as e:
            print(f"Error executing CREATE_TABLE.sql: {e}")

# Traverse the directory and execute other SQL files
for root, dirs, files in os.walk(data_path):
    for file in files:
        if file.endswith('.sql') and file != 'CREATE_TABLE.sql':
            sql_file_path = os.path.join(root, file)
            print(f"Executing {sql_file_path}")
            with open(sql_file_path, 'r') as sql_file:
                sql_script = sql_file.read()
                try:
                    cursor.executescript(sql_script)
                    print(f"Executed: {file}")
                except Exception as e:
                    print(f"Error executing {file}: {e}")

# Commit changes and close the connection
connection.commit()
connection.close()
