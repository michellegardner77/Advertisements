package main.models;

/**
 * Created by mgard on 4/30/2017.
 */
public abstract class Categories{

    protected String cat_name;
    protected String category_ID;

    public Categories (String cat_name, String category_ID){
        this.cat_name = cat_name;
        this.category_ID = category_ID;
    }

    public String getcat_name(){
        return cat_name;
    }

    public String getCategory_ID(){
        return category_ID;
    }

}