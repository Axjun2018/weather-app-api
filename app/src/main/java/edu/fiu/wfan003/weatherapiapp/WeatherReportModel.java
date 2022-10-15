package edu.fiu.wfan003.weatherapiapp;

public class WeatherReportModel {

    private int id;
    private String main;
    private String weather_description;
    private String time;
    private int visibility;

    public WeatherReportModel() {
    }

    public WeatherReportModel(int id, String main, String weather_description, String time, int visibility) {
        this.id = id;
        this.main = main;
        this.weather_description = weather_description;
        this.time = time;
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Time: " + time + "\n" +
                "Main Weather: " + main + "\n" +
                "Description: " + weather_description + "; \t" +
                "Visibility: " + visibility;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getWeather_description() {
        return weather_description;
    }

    public void setWeather_description(String weather_description) {
        this.weather_description = weather_description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
