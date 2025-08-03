# java_library_management
cd "c:\Users\adminnn\OneDrive\Documents\java_library_management\src" && ^
javac -cp "../lib/mysql-connector-j-9.4.0.jar" -d . config/*.java dao/*.java model/*.java LibraryApp.java
java -cp ".;../lib/mysql-connector-j-9.4.0.jar" LibraryApp
mysql -u root -p
SELECT * FROM library_management.users;