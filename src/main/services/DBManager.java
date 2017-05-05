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

    // Return everything = ("%", "CURRENT_DATE()", "%")
    // Return only ELC ("ELC", "CURRENT_DATE()", "%")
    // Return last three months ("ELC", "3", "%")
    // Return title/desc like Smart ("ELC", "3", "Smart")
    public List<Advertisements> getAdvertisements(String cat, String period, String advTitleDesc){
        PreparedStatement stmt;
        List<Advertisements> advertisementsList = new ArrayList<>();

        String periodQuery = "?";

        // Injecting function into sql statement if passed in as string else will stay as prepared string
        if(Objects.equals(period, "CURRENT_DATE()")){
            periodQuery = period;
        }

        String query = "SELECT Advertisement_ID, AdvTitle, AdvDetails, AdvDateTime, Price, User_ID, Moderator_ID, Category_ID, Status_ID " +
                "FROM Advertisements adv " +
                "WHERE Category_ID LIKE ?" +
                    "AND (AdvTitle LIKE ? OR AdvDetails LIKE ?)" +
                    "AND TIMESTAMPDIFF(MONTH, AdvDateTime, CURRENT_DATE()) <= " + periodQuery;

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, cat); //binding the parameter with the given string
            stmt.setString(2, "%"+advTitleDesc+"%"); //binding the parameter with the given string
            stmt.setString(3, "%"+advTitleDesc+"%"); //binding the parameter with the given string

            // if period parameter is not CURRENT_DATE() function then just set ? to query values
            if(!Objects.equals(period, "CURRENT_DATE()")) {
                stmt.setString(4, period); //binding the parameter with the given string
            }

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
            System.err.println(e.getMessage());
            return advertisementsList;
        }
        return advertisementsList;
    }

    // We want to take the inputted data and put it into our table
    public boolean addAdvertisement(String adTitle, String adDetails, double Price, String category_id, String user_id) {
        PreparedStatement stmt = null;

        String query = "INSERT into Advertisements (AdvTitle, AdvDetails, AdvDateTime, Price, Category_ID, User_ID, Moderator_ID, Status_ID) VALUES (?,?,CURRENT_DATE(),?,?,?,NULL, 'PN')";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, adTitle); //binding the parameter with the given string
            stmt.setString(2, adDetails);
            stmt.setDouble(3, Price);      //I AM NOT SURE IF YOU SKIP 3 BECAUSE OF DATE
            stmt.setString(4, category_id);
            stmt.setString(5, user_id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

}

