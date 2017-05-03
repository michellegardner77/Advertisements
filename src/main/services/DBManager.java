package main.services;

/**
 * Created by mgard on 4/30/2017.
 */

import main.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author kuhail
 */
public class DBManager {
    Connection connection;

    public void connect(String userName,
                        String password,
                        String serverName,
                        String portNumber,
                        String schemaName)
            throws SQLException, InstantiationException, IllegalAccessException {
        System.out.println("Loading driver...");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        connectionProps.put("useSSL", "false");

        conn = DriverManager.getConnection(
                "jdbc:mysql://"
                        + serverName
                        + ":" + portNumber + "/" + schemaName,
                connectionProps);

        System.out.println("Connected to database");
        this.connection = conn;
    }

    public User getUser(String userName) {
        PreparedStatement stmt = null;
        User user = null;

        String query = "Select User_ID, UserFirst_Name, UserLast_Name From users Where User_ID=?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, userName); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String user_ID = rs.getString("User_ID");
                String userFirst_Name = rs.getString("UserFirst_Name");
                String userLast_name = rs.getString("UserLast_Name");

                user = new User(user_ID, userFirst_Name, userLast_name);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return user;
        }
        return user;
    }
}

