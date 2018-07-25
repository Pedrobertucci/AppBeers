package com.beers.beers.Model;

import com.beers.beers.Classes.Beers;

import java.util.ArrayList;

public class ServerResponse {

    private ArrayList<Beers> beer;
    private String result;
    private String message;
    private Beers[] beers;

    public ArrayList<Beers> getBeer() {
        return beer;
    }
    public Beers[] getBeers(){ return beers;}
    public String getResult() {
        return result;
    }
    public String getMessage() {
        return message;
    }


}
