package main.services;

/**
 * Created by mgard on 4/30/2017.
 */

import main.models.Advertisements;
import main.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    // asking database if the username is a moderator, and will return true or false
    public boolean isModerator(String userName) {
        PreparedStatement stmt = null;

        String query = "Select Moderator_ID From moderators Where Moderator_ID=?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, userName); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String moderator_ID = rs.getString("Moderator_ID");

                if(moderator_ID != null){
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return false;
    }

    public List<Advertisements> getAdvertisements(String cat, String period){
        PreparedStatement stmt = null;
        List<Advertisements> advertisementsList = new ArrayList<>();

        String periodFilter = null;
        String catFilter = null;
//        int periodFilter2 = 0;

        String query = "SELECT AdvTitle, AdvDetails, Price, AdvDateTime " +
                "FROM Advertisements " +
                "INNER JOIN Categories ON Advertisements.Category_ID = Categories.Category_ID " +
                "WHERE Advertisements.Category_ID LIKE ?" +
                    "AND TIMESTAMPDIFF(MONTH, AdvDateTime, CURRENT_DATE()) <= ?";

        // Deal with either ALL time period or specific period (3, 6, 12, ...)
        if (Objects.equals(period, "Life")) {
            periodFilter = "CURRENT_DATE()";
        } else {
            // TODO: Get other period numbers as strings, Make sure a number is passed else wont work!!

                // check if string is number
                // if string is then add it to period filter
            switch(period) {
                case "3 Months":
                        periodFilter = "3";
//                        periodFilter2 = 3;
                        break;
                case "6 Months":
                        periodFilter = "6";
//                        periodFilter2 = 6;
                        break;
                case "12 Months":
                        periodFilter = "12";
//                        periodFilter2 = 12;
                        break;
                default:
                        break;

            }
        }

        // check if cat passed in is All for a wild card search or a specified one
        if (Objects.equals(cat, "All")){
            // if all set to wildcard to get all cats
            catFilter = "%";
        } else {
            catFilter = cat;
        }

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, cat); //binding the parameter with the given string
            stmt.setString(2, periodFilter); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int adv_id = rs.getInt("Advertisement_ID");
                String adv_title = rs.getString("AdvTitle");
                String adv_date_time = rs.getString("AdvDateTime");
                double price = rs.getDouble("Price");
                String moderator_ID = rs.getString("Moderator_ID");
                String status_ID = rs.getString("Status_ID");
                String advDetails = rs.getString("AdvDetails");
                String category_ID = rs.getString("Category_ID");

                Advertisements adv = new Advertisements(adv_id, price, adv_title, advDetails, adv_date_time, moderator_ID, category_ID, status_ID);

                // add new advertisement to List
                advertisementsList.add(adv);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return advertisementsList;
        }
        return advertisementsList;
    }

}

