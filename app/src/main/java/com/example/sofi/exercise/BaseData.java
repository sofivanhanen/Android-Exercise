package com.example.sofi.exercise;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sofi on 23.2.2017.
 */

public class BaseData {

    private String apiVersion;
    private Object meta;
    private ArrayList<Show> data;

    public String getApiVersion() {
        return apiVersion;
    }

    public Object getMeta() {
        return meta;
    }

    public ArrayList<Show> getData() {
        return data;
    }

    public ArrayList<Show> getRelevantData(String searchTerm) { //Looking through items
        ArrayList<Show> list = new ArrayList<Show>();
        for (Show show : data) {
            Log.d("getRelevantData", show.toString());
            if (show.toString().toLowerCase().contains(searchTerm.toLowerCase())) {
                //Rewrote show.toString to display typeMedia, indexDataModified, itemTitle and id
                list.add(show);
            }
        }
        return list;
    }
}
