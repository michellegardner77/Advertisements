package main.models;

/**
 * Created by mgard on 4/30/2017.
 */
public class Users{

    protected String user_ID;
    protected String userFirst_Name;
    protected String userLast_Name;
    protected boolean isModerator = false;

    public Users (String user_ID, String userFirst_Name, String userLast_Name){
        this.user_ID = user_ID;
        this.userFirst_Name = userFirst_Name;
        this.userLast_Name = userLast_Name;
    }

    public Users (String user_ID, String userFirst_Name, String userLast_Name, boolean isModerator){
        this.user_ID = user_ID;
        this.userFirst_Name = userFirst_Name;
        this.userLast_Name = userLast_Name;
        this.isModerator = isModerator;
    }

    public String getUser_ID(){
        return user_ID;
    }
    public String getUserFirst_Name(){
        return userFirst_Name;
    }
    public String getUserLast_Name(){
        return userLast_Name;
    }
    public boolean isModerator() {
        return isModerator;
    }

    // method to set moderator
    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }
}
