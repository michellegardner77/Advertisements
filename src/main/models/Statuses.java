package main.models;

/**
 * Created by mgard on 4/30/2017.
 */
public abstract class Statuses{

    protected String status_ID;
    protected String status_name;

    public Statuses (String status_ID, String status_name){
        this.status_ID = status_ID;
        this.status_name = status_name;
    }
    public String getStatus_ID(){
        return status_ID;
    }
    public String getStatus_Name(){
        return status_name;
    }

}
