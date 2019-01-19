package com.londonappbrewery.bitcointicker;

/*
Activity class to display the BitCoin Price tracker layout. This Activity object uses activity_main.xml as the main layout

We are using the spinner_item.xml layout to define how the selected choice appears in the spinner control as opposed to the
simple_spinner_item provided by the system by default

we are using the spinner_dropdown_item layout for displaying the list of spinner choices as opposed to the simple_spinner_dropdown_item defined by the Android platform(default)
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    //Defining our Base URL for making the API calls to the BitcoinAverage.com servers
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";

    // Member Variables:
    //Declaring a reference to our Price TextView object
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencing the TextView object from our Layout to display the Bitcoin prices in the selected currency
        mPriceTextView = (TextView) findViewById(R.id.priceLabel);

        //Referencing the Spinner object from our layout to display the list of currencies
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        //We are creating our ArrayAdapter as our choices/data is comming from an Array. we get the array from the currency_array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner

        //Setting an AdapterView.OnItemSelectedListener object callback on our spinner who's methods get called when the
        //user interacts with the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            //Callback method called when the an item from the Spinner object is selected. Below the AdapterView object in our case
            //is the Spinner object since the Spinner class extends the AbsSpinner class which in turn extends the AdapterView class
            //The View object is the View inside the AdapterView object that was clicked, i.e. in our case this would be the TextView inside the
            //Spinner since each option/choice is displayed in a TextView in the Spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("BITCOIN", parent.getItemAtPosition(position).toString());

                //Calling the letsDoSomeNetworking() method to make the Networking calls to query the
                //BitcoinAverage.com servers to get the Bitcoin related JSON data
                //We use the base URL defined above and append the "BTC" and the selected Item String objects to the
                //URl in accordance with the API
                letsDoSomeNetworking(BASE_URL+"BTC"+parent.getItemAtPosition(position).toString());

            }

            //The below callback is invoked when the selection disappears from the View object
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("BITCOIN", "Nothing Selected");

            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    //Defining the letsDoSomeNetworking method to make the networking request to the Bitcoin Servers using their API
    private void letsDoSomeNetworking(String url) {

        //Creating the AsyncHttpClient object from James Smith's loopj library that makes the Asynchronous Http requests in the
        //background without holding up the main thread
        AsyncHttpClient client = new AsyncHttpClient();

        //Calling the overridden get() method on the AsyncHttpClient object to make the networking request to the Bitcoin average servers
        client.get(this, url, new JsonHttpResponseHandler(){

            //The below callback method gets called when we get the 200 OK response from the Bitcoin servers
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("BITCOIN", "Successful response received from the BITCOIN server = "+response.toString());

                //Creating and getting the BitcoinDataModel object from the fromJson() static method. This object has the parsed JSON
                //data received from the BotcoinAverage server
                BitcoinDataModel bitcoinDataModel = BitcoinDataModel.fromJSON(response);

                //calling the updateUI() method to update our UI with the parsed JSON data using the BitcoinDataModel object
                //referenced by the bitcoinDataModel reference variable
                updateUI(bitcoinDataModel);
            }

            //Callback method called when there is an error while making a connection or if the Server responds with 4xx/3xx status codes
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("BITCOIN", "Connection was a failure");

                //We'll display a Toast to the user if the onFailure() method is called
                Toast.makeText(MainActivity.this, "Connection was not successful", Toast.LENGTH_SHORT).show();
            }
        });


//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // called when response HTTP status is "200 OK"
//                Log.d("Clima", "JSON: " + response.toString());
//                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
//                updateUI(weatherData);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
//                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                Log.d("Clima", "Request fail! Status code: " + statusCode);
//                Log.d("Clima", "Fail response: " + response);
//                Log.e("ERROR", e.toString());
//                Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();
//            }
//        });


    } //LetsDoSomeNetworking


    //Defining the updateUI() method to update our UI with the parsed JSON data held by the BitcoinDataModel POJO object
    public void updateUI(BitcoinDataModel bitcoinDataModel){

        //Setting the Botcoin price on our TextView object depending on the currency selected in the Spinner View object
        mPriceTextView.setText(bitcoinDataModel.getAskingPrice());

    }


}
