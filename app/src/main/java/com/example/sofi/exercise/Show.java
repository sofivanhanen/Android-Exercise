package com.example.sofi.exercise;

/**
 * Created by Sofi on 23.2.2017.
 */

public class Show {

    private String typeMedia;
    private String indexDataModified;
    private Title itemTitle;
    private String id;

    public String getTypeMedia() {
        return typeMedia;
    }

    public String getItemTitle() {
        return itemTitle.toString(); //I rewrote the toString method of Title to return the 'unknown' variable
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return itemTitle + ", " + typeMedia + ", " + indexDataModified + ", " + id;
    }

    public String toStringWithoutTitle() {
        return typeMedia + ", " + indexDataModified + ", " + id;
    }
}
