package org.example;

import java.sql.*;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        String url = "jdbc:h2:mem:";
        try (Connection conn = DriverManager.getConnection(url)) {

            createTablePerson(conn);
            createTableAutomobil(conn);

            createPerson(conn, "Andrei","Popov", 28);
            createPerson(conn, "Vlad","Arefti", 30);
            createPerson(conn, "Ion","Rusu", 30);

            createAutomobil(conn, "BMW F30", "F30", "BMW", 1);
            createAutomobil(conn, "C KLASS", "C',", "MERCEDES", 1);


//            String name = getPersonNameByPersonId(conn, 1);


//            Person person = getPersonByPersonId(conn, 1);

            // getAllPersons(conn) - 1000000000

            List<Person> personList = getAllPersons(conn); // 1000000000 + 100000

            List<Person> filteredPerson = new ArrayList<>(); // 100000

            for(Person person : personList) {
                if(person.firstName.equals("Vlad")) {
                    filteredPerson.add(person);
                }
            }

            getAllPersons(conn).stream()
                     .filter((person) -> person.firstName.equals("Vlad"))
                     .forEach((person) -> System.out.println(person));

            List<Person> person = getPersonByFirstName(conn, "Vlad"); // 100000

//            while(resultSet.next()) {
//                System.out.print(resultSet.getString(1) + " ");
////                System.out.print(resultSet.getInt(1) + " ");
//                System.out.println(resultSet.getString(2) + " ");
////                System.out.print(resultSet.getString(3) + " ");
////                System.out.println(resultSet.getInt(4));
//            }

//            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static List<Person> getPersonByFirstName(Connection conn, String name) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM person WHERE first_name = ?");

        preparedStatement.setString(1, name);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<Person> personList = new ArrayList<>();

        while (resultSet.next()) {
            Integer id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            Integer age = resultSet.getInt(4);

            personList.add(new Person(id, firstName, lastName, age));
        }

        preparedStatement.close();

        return personList;
    }

    static void createPerson(Connection connection, String firstName, String lastName, int age) throws SQLException {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO person(first_name, last_name, age) VALUES(?, ?, ?);");

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);

            stmt.executeUpdate();
            stmt.close();
    }

    static void createAutomobil(Connection connection, String name, String model, String marca, int personId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO automobil(name, model, marca, person_id) VALUES(?, ?, ?, ?);");

        statement.setString(1, name);
        statement.setString(2, model);
        statement.setString(3, marca);
        statement.setInt(4, personId);

        int count = statement.executeUpdate();
        statement.close();
    }

    static void createTablePerson(Connection connection) throws SQLException {
        String createTable = "CREATE TABLE person(\n" +
                "   id INT AUTO_INCREMENT,\n" +
                "   first_name VARCHAR(40),\n" +
                "   last_name VARCHAR(40),\n" +
                "   age INT,\n" +
                "   PRIMARY KEY(id)\n" +
                "); ";

        PreparedStatement stmt = connection.prepareStatement(createTable);

        stmt.execute();
        stmt.close();
    }

    static void createTableAutomobil(Connection connection) throws SQLException {
        PreparedStatement stmt2 = connection.prepareStatement("CREATE TABLE automobil (\n" +
                "  id INT AUTO_INCREMENT,\n" +
                "  name VARCHAR(50),\n" +
                "  model VARCHAR(30) NOT NULL,\n" +
                "  marca VARCHAR(30) UNIQUE,\n" +
                "  person_id INT REFERENCES person(id),\n" +
                "  PRIMARY KEY(id)\n" +
                ")");

        boolean isSelect = stmt2.execute();
        stmt2.close();
    }

    static String getPersonNameByPersonId(Connection connection,  int personId) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name FROM person WHERE id = ?");
        preparedStatement.setInt(1, personId);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        String name = resultSet.getString("first_name");

        preparedStatement.close();

        return name;
    }


    static Person getPersonByPersonId(Connection connection, int personId) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE id = ?");
        preparedStatement.setInt(1, personId);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        Integer id = resultSet.getInt(1);
        String firstName = resultSet.getString(2);
        String lastName = resultSet.getString(3);
        Integer age = resultSet.getInt(4);

        Person person = new Person(id, firstName, lastName, age);

        preparedStatement.close();

        return person;
    }


    static List<Person> getAllPersons(Connection connection) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person");

        ResultSet resultSet = preparedStatement.executeQuery();


        List<Person> personList = new ArrayList<>();

        while (resultSet.next()) {
            Integer id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            Integer age = resultSet.getInt(4);

            personList.add(new Person(id, firstName, lastName, age));
        }

        preparedStatement.close();

        return personList;
    }

}
