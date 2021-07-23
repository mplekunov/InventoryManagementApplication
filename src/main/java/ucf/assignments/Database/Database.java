package ucf.assignments.Database;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import ucf.assignments.Models.Item;
import ucf.assignments.Models.ItemModel;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                item.setDate(LocalDate.parse(resultSet.getString("date"), dtf));
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

    public void updateItems(Item item) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();

            String query = "UPDATE Items SET serialNumber = '%s', name = '%s', price = %lf, date = '%s' WHERE ROWID = %d";
            ResultSet resultSet = statement.executeQuery(String.format(query, item.getSerialNumber(), item.getName(), item.getPrice(), item.getDate(), item.getId()));
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

    public void insertItems(Item item) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            Statement statement = connection.createStatement();

            String query = "INSERT INTO Items VALUES('%s', '%s', '%lf', '%s')";
            ResultSet resultSet = statement.executeQuery(String.format(query, item.getSerialNumber(), item.getName(), item.getPrice(), item.getDate().toString()));
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
