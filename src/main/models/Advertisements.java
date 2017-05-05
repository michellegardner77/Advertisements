package main.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.services.DBManager;

/**
 * Created by mgard on 4/30/2017.
 */
public class Advertisements{
    protected int advertisement_ID;
    protected String advTitle;
    protected String advDetails;
    protected String advDateTime;
    protected double price;
    protected String moderator_ID;
    protected String category_ID;
    protected String status_ID;

    protected String category_name;
    protected String status_name;
    protected String user_id;


    public Advertisements(int advertisement_ID, double price, String advTitle, String advDetails, String advDateTime,String  moderator_ID, String category_ID, String category_name, String status_ID, String status_name, String user_id){
        this.advertisement_ID = advertisement_ID;
        this.price = price;
        this.advTitle = advTitle;
        this.advDetails = advDetails;
        this.advDateTime = advDateTime;
        this.moderator_ID = moderator_ID;
        this.category_ID = category_ID;
        this.category_name = category_name;
        this.status_ID = status_ID;
        this.status_name = status_name;
        this.user_id = user_id;
    }

    public int getAdvertisement_ID(){
        return advertisement_ID;
    }
    // do we need this? The tutorials show it
//    public int setAdvertisement_ID(int advID){
//        this.set(advID);
//    }

    public double getPrice(){
        return price;
    }
    public String getAdvTitle(){
        return advTitle;
    }
    public String getAdvDetails(){
        return advDetails;
    }
    public String getAdvDateTime(){
        return advDateTime;
    }
    public String getModerator_ID(){
        return moderator_ID;
    }
    public String getCategory_ID(){
        return category_ID;
    }
    public String getStatus_ID(){
        return status_ID;
    }
    public String getCategory_name(){
        return category_name;
    }
    public String getStatus_name(){
        return status_name;
    }
    public String getUser_id(){
        return user_id;
    }


}
