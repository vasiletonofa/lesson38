package org.example;

import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        String url = "jdbc:h2:mem:";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE person(\n" +
                    "   id INT AUTO_INCREMENT,\n" +
                    "   first_name VARCHAR(40),\n" +
                    "   last_name VARCHAR(40),\n" +
                    "   age INT,\n" +
                    "   PRIMARY KEY(id)\n" +
                    "); ");

            stmt.execute("CREATE TABLE automobil (\n" +
                    "  id INT AUTO_INCREMENT,\n" +
                    "  name VARCHAR(50),\n" +
                    "  model VARCHAR(30) NOT NULL,\n" +
                    "  marca VARCHAR(30) UNIQUE,\n" +
                    "  person_id INT REFERENCES person(id),\n" +
                    "  PRIMARY KEY(id)\n" +
                    ")");

            stmt.executeUpdate("INSERT INTO person(first_name, last_name, age) VALUES('Andrei', 'Popov', 25);"); // INSERT / UPDATE / DELETE / ALTER  x select
            stmt.executeUpdate("INSERT INTO person(first_name, last_name, age) VALUES('Vlad', 'Arefti', 30);"); // INSERT / UPDATE / DELETE / ALTER  x select
            stmt.executeUpdate("INSERT INTO person VALUES(5, 'Ion', 'Rusu', 28);"); // INSERT / UPDATE / DELETE / ALTER  x select

            stmt.executeUpdate("INSERT INTO automobil(name, model, marca, person_id) VALUES('BMW F30', 'F30', 'BMW', 1);");
            stmt.executeUpdate("INSERT INTO automobil(name, model, marca, person_id) VALUES('C KLASS', 'C', 'MERCEDES', null);");

           ResultSet resultSet =  stmt.executeQuery("SELECT person.first_name, automobil.name FROM person FULL OUTER JOIN automobil ON  automobil.person_id = person.id");

            while(resultSet.next()) {
                System.out.print(resultSet.getString(1) + " ");
//                System.out.print(resultSet.getInt(1) + " ");
                System.out.println(resultSet.getString(2) + " ");
//                System.out.print(resultSet.getString(3) + " ");
//                System.out.println(resultSet.getInt(4));
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
