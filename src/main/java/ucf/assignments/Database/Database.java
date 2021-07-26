/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments.Database;

import ucf.assignments.Converters.DateConverter;
import ucf.assignments.Models.Item;
import java.sql.*;
import java.util.*;

public class Database {
    private final String connectionString = "jdbc:sqlite:itemsDatabase.sqlite";

    public Database() {
    }

    public List<Item> getItems() {
        var items = new LinkedList<Item>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT ROWID, * FROM Items");

            while (resultSet.next()) {
                Item item = new Item();

                item.setName(resultSet.getString("name"));
                item.setSerialNumber(resultSet.getString("serialNumber"));
                item.setPrice(resultSet.getDouble("price"));
                item.setDate(DateConverter.toDate(resultSet.getString("date")));
                item.setId(resultSet.getInt("ROWID"));

                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return items;
    }

    public void updateItem(Item item) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();

            String query = "UPDATE Items SET serialNumber = '%s', name = '%s', price = %f, date = '%s' WHERE ROWID = %d";
            statement.execute(
                    String.format(query,
                            item.getSerialNumber().getValue(),
                            item.getName().getValue(),
                            item.getPrice().getValue(),
                            DateConverter.toString(item.getDate().getValue()),
                            item.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertItem(Item item) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();

            String query = "INSERT INTO Items VALUES('%s', '%s', '%f', '%s')";
            statement.execute(
                    String.format(query,
                            item.getSerialNumber().getValue(),
                            item.getName().getValue(),
                            item.getPrice().getValue(),
                            DateConverter.toString(item.getDate().getValue())));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteItem(Item item) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();

            String query = "DELETE FROM Items WHERE ROWID = %d";
            statement.execute(String.format(query, item.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
