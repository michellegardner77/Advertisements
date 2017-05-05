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

    // Method to call database to get user object
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

    // Method to call database for all advs based on filter qualifications
    // Return everything = ("%", "CURRENT_DATE()", "%")
    // Return only ELC ("ELC", "CURRENT_DATE()", "%")
    // Return last three months ("ELC", "3", "%")
    // Return title/desc like Smart ("ELC", "3", "Smart")
    public List<Advertisements> getFilteredAdvertisements(String cat, String period, String advTitleDesc){
        PreparedStatement stmt;

        String periodQuery = "?";

        // Injecting function into sql statement if passed in as string else will stay as prepared string
        if(Objects.equals(period, "CURRENT_DATE()")){
            periodQuery = period;
        }

        String query = "SELECT adv.Advertisement_ID, adv.AdvTitle, adv.AdvDetails, adv.AdvDateTime, adv.Price, adv.User_ID, adv.Moderator_ID, adv.Category_ID, Categories.Category_Name, adv.Status_ID, Statuses.Status_Name " +
                "FROM Advertisements adv " +
                "INNER JOIN Categories ON adv.Category_ID = Categories.Category_ID " +
                "INNER JOIN Statuses ON adv.Status_ID = Statuses.Status_ID " +
                "WHERE adv.Category_ID LIKE ? " +
                    "AND (adv.AdvTitle LIKE ? OR adv.AdvDetails LIKE ?) " +
                    "AND TIMESTAMPDIFF(MONTH, adv.AdvDateTime, CURRENT_DATE()) <= " + periodQuery;

        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, cat); //binding the parameter with the given string
            stmt.setString(2, "%"+advTitleDesc+"%"); //binding the parameter with the given string
            stmt.setString(3, "%"+advTitleDesc+"%"); //binding the parameter with the given string

            // if period parameter is not CURRENT_DATE() function then just set ? to query values
            if(!Objects.equals(period, "CURRENT_DATE()")) {
                stmt.setString(4, period); //binding the parameter with the given string
            }
            rs = stmt.executeQuery();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }

        // Call method to take result set and turn into array list.
        return  resultSetToAdvList(rs);
    }

    // Method to call database to get all users advs based on 1 filter
    public List<Advertisements> getMyAdvertisements(String userId){
        PreparedStatement stmt;

        String query = "SELECT adv.Advertisement_ID, adv.AdvTitle, adv.AdvDetails, adv.AdvDateTime, adv.Price, adv.User_ID, adv.Moderator_ID, adv.Category_ID, Categories.Category_Name, adv.Status_ID, Statuses.Status_Name " +
                "FROM Advertisements adv " +
                "INNER JOIN Categories ON adv.Category_ID = Categories.Category_ID " +
                "INNER JOIN Statuses ON adv.Status_ID = Statuses.Status_ID " +
                "WHERE adv.User_ID = ? ";

        ResultSet rs;

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, userId); //binding the parameter with the given string

            rs = stmt.executeQuery();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
        // Call method to take result set and turn into array list.
        return  resultSetToAdvList(rs);
    }


    // Method to parse result sed from db query into a List of Advs
    private List<Advertisements> resultSetToAdvList(ResultSet advResultSet) {
        List<Advertisements> advList = new ArrayList<>();

        try{
            while (advResultSet.next()) {
                int adv_id = advResultSet.getInt("Advertisement_ID");
                String adv_title = advResultSet.getString("AdvTitle");
                String adv_date_time = advResultSet.getString("AdvDateTime");
                double price = advResultSet.getDouble("Price");
                String moderator_ID = advResultSet.getString("Moderator_ID");
                String status_ID = advResultSet.getString("Status_ID");
                String status_Name = advResultSet.getString("Status_Name");
                String advDetails = advResultSet.getString("AdvDetails");
                String category_ID = advResultSet.getString("Category_ID");
                String category_Name = advResultSet.getString("Category_Name");
                String user_ID = advResultSet.getString("User_ID");

                Advertisements adv = new Advertisements(adv_id, price, adv_title, advDetails, adv_date_time, moderator_ID, category_ID, category_Name, status_ID, status_Name, user_ID);

                // add new advertisement to List
                advList.add(adv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return advList;
        }
        return advList;
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

