package main.models;

/**
 * Created by mgard on 4/30/2017.
 */
public abstract class Moderators{
    protected String moderator_ID;

    public Moderators(String moderator_ID){
        this.moderator_ID = moderator_ID;
    }
    public String getModerator_ID(){
        return moderator_ID;
    }
}
