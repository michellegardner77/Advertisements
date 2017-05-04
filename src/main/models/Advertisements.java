package main.models;

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
    public DBManager dbManager;

    public Advertisements(int advertisement_ID, double price, String advTitle, String advDetails, String advDateTime,String  moderator_ID, String category_ID, String status_ID){
        this.advertisement_ID = advertisement_ID;
        this.price = price;
        this.advTitle = advTitle;
        this.advDetails = advDetails;
        this.advDateTime = advDateTime;
        this.moderator_ID = moderator_ID;
        this.category_ID = category_ID;
        this.status_ID = status_ID;
    }

    public int getAdvertisement_ID(){
        return advertisement_ID;
    }
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
}
