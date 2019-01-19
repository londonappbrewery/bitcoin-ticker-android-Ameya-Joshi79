package com.londonappbrewery.bitcointicker;
/*
POJO class to parse the JSON received from the BitcoinAverage servers after making the API call
All data extraction and manipulation will be done in this POJO object to conform with the MVC design pattern
 */

import org.json.JSONException;
import org.json.JSONObject;


public class BitcoinDataModel {

    //Declaring a reference variable of type String to store the Bitcoin price
   private String askingPrice;


   //Defining the fromJSON static method that will parse our JSON data received from the BitcoinAverage Server by using their API
    //The JSON data is stored inside the JSOBObject object referenced by the jsonObject variable
    public static BitcoinDataModel fromJSON(JSONObject jsonObject){

        //Creating a new BitcoinDataModel object that will store our parsed JSON data
        BitcoinDataModel bitcoinDataModel = new BitcoinDataModel();

        try {

            //Storing the Bitcoin asking price in the double variable bitcoinPriceInDouble. The data is extracted by calling the
            //get() methid of the JSONObject with the "ask" key
            double bitcoinPriceInDouble = (Double) jsonObject.get("ask");

            //Converting the above from double to String and storing the bitcoin price inside the askingPrice String variable of the
            //newly created BitcoinDataModel object
            bitcoinDataModel.askingPrice = Double.toString(bitcoinPriceInDouble);


            //returning the BitcoinDataModel object
            return bitcoinDataModel;

        }catch (JSONException exception){
            exception.printStackTrace();

            //Returning null if there is an exception while parsing the JSONObject object
            return null;

        }
    }

    //Getter method for returning the String object that has the Bitcoin Price
    public String getAskingPrice() {
        return askingPrice;
    }
}
