package com.faltenreich.diaguard.database;

/**
 * Created by Filip on 09.08.14.
 */
public class Food extends Model {

    private String name;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.FOOD;
    }
}
