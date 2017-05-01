package main.services;

/**
 * Created by mgard on 4/30/2017.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 *
 * @author kuhail
 */
public class DBManager {
    Connection connection;

    public class Record {
        public String ID;
        public String Name;

        public Record(String ID, String Name) {
            this.ID = ID;
            this.Name = Name;
        }

        public String toString() {
            return Name;
        }
    }

    public LinkedList<Record> getAccountTypes() {
        LinkedList<Record> records = new LinkedList();
        PreparedStatement stmt = null;

        String query = "select * FROM Account_Types";

        try {
            stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String account_type_id = rs.getString("Account_Type_ID");
                String account_type_name = rs.getString("Account_Type_Name");
                Record record = new Record(account_type_id, account_type_name);
                records.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return records;
        }
        return records;
    }
}

