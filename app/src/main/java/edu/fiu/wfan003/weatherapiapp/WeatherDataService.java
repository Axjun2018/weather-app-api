package edu.fiu.wfan003.weatherapiapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID =
            "https://api.openweathermap.org/data/2.5/weather?appid=eb436a3c32023803c8fbcdb7dce21896&q=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID =
            "https://api.openweathermap.org/data/2.5/forecast?appid=eb436a3c32023803c8fbcdb7dce21896&id=";
    Context context;
    String cityID;

    // pass MainActivity.this
    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);
        void onResponse(String cityID);
    }

    // pass volley lib, to use its 2 methods above
    public void getCityID(String cityName, final VolleyResponseListener volleyResponseListener){
        String url = QUERY_FOR_CITY_ID + cityName;

        // JSON format with {},  means it's a simple Json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { //response is in JSON format
                cityID = "";
                try {
                    cityID  = response.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // this Toast worked, but it didn't return the id number to MainActivity
                // Toast.makeText(context, "City ID = " + cityID, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Invalid Input.", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong!");
            }
        });
        //queue.add(request);
        MySingleton.getInstance(context).addToRequestQueue(request);
        // returned a NULL, asynchronous problem! Need callback
        //return cityID;
    }

    // copy the interface to make another response
    public interface ForecastByIDResponse {
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityForecastByID(String cityID, final ForecastByIDResponse forecastByIDResponse){
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        // get the json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { //pass entire JSONObject
                //Toast.makeText(context, first_day.toString(),Toast.LENGTH_SHORT).show(); // test, display entire object data
                try {
                    JSONArray forecast_list = response.getJSONArray("list");
                    // get the each item in the array
                    for(int i = 0; i < forecast_list.length(); i++){
                        WeatherReportModel one_day = new WeatherReportModel();
                        JSONObject day_from_api = (JSONObject) forecast_list.get(i);

                        JSONObject weather_list = (JSONObject) day_from_api.getJSONArray("weather").get(0);
                        one_day.setId(weather_list.getInt("id"));
                        one_day.setMain(weather_list.getString("main"));
                        one_day.setWeather_description(weather_list.getString("description"));
                        one_day.setTime(day_from_api.getString("dt_txt"));
                        one_day.setVisibility(day_from_api.getInt("visibility"));
                        weatherReportModels.add(one_day);
                    }
                    // send back response to MainActivity
                    forecastByIDResponse.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // get the property call "weather" which is an array

        // get each item in the array and assign it to a new WeatherReportModel object.
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityForecastByName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback){
        // fetch the city id given the city name
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {
                // now we can fetch city id
                getCityForecastByID(cityID, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        // we have the weather report, send it back to MainActivity
                        getCityForecastByNameCallback.onResponse(weatherReportModels);


                    }
                });
            }
        });

        // fetch the city forecast given the city id
    }
}
