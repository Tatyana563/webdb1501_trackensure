package dao;

import tables.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    private final Connection connection;

    public CountryDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Country> findAll() {
        List<Country> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from country");
            while (rs.next()) {
                int id = rs.getInt("country_id");
                String name = rs.getString("name");
                result.add(new Country(id,name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void add(String countryName) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("insert into country (name) values (?)")) {
            preparedStatement.setString(1, countryName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(String name, int id){
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE country SET name =? " +
                "WHERE country_id = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id){
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM country AS c WHERE c.country_id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
