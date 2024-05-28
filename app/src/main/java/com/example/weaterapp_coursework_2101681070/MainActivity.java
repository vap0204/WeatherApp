package com.example.weaterapp_coursework_2101681070;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weaterapp_coursework_2101681070.R;
import com.example.weaterapp_coursework_2101681070.SearchHistoryActivity;
import com.example.weaterapp_coursework_2101681070.WeatherDataSource;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "138026e3bc6248098c5233852241805";
    private static String CITY = "Sofia";
    private WeatherDataSource dataSource;
    private TextView textViewCity;
    private TextView textViewTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize WeatherDataSource
        dataSource = new WeatherDataSource(this);
        dataSource.open();

        // Initialize text views and button
        textViewCity = findViewById(R.id.textViewCity);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        Button buttonRefresh = findViewById(R.id.buttonRefresh);

        // Logic for fetching weather data and inserting into the database
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextCity = findViewById(R.id.editTextCity);
                String city = editTextCity.getText().toString();
                if (!city.isEmpty()) {
                    CITY = city;
                    new FetchWeatherTask().execute(CITY);
                } else {
                    // Show error message if user hasn't entered a city
                    Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close WeatherDataSource when activity is destroyed
        dataSource.close();
    }

    public void showHistory(View view) {
        // Start new activity to show search history
        Intent intent = new Intent(this, SearchHistoryActivity.class);
        startActivity(intent);
    }

    // Nested class for fetching data from the API
    private class FetchWeatherTask extends AsyncTask<String, Void, Double> {
        private String city;

        @Override
        protected Double doInBackground(String... params) {
            city = params[0];
            try {
                String apiUrl = "https://api.weatherapi.com/v1/current.json?key=" + API_KEY + "&q=" + city + "&aqi=no";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder json = new StringBuilder(1024);
                    String tmp;
                    while ((tmp = reader.readLine()) != null) json.append(tmp).append("\n");
                    reader.close();
                    JSONObject data = new JSONObject(json.toString());
                    if (!data.has("current")) {
                        Log.e("WeatherApp", "Invalid response: " + data);
                        return null;
                    }
                    double temperature = data.getJSONObject("current").getDouble("temp_c");

                    // Insert search history into database
                    long result = dataSource.insertSearchHistory(city, temperature);
                    if (result == -1) {
                        Log.e("WeatherApp", "Failed to insert search history into database");
                    }

                    return temperature;
                } else {
                    Log.e("WeatherApp", "Response code: " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                Log.e("WeatherApp", "Exception caught: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Double temperature) {
            if (temperature != null) {
                textViewCity.setText("City: " + city);
                textViewTemperature.setText("Temperature: " + temperature + "°C");
            } else {
                textViewCity.setText("Failed to fetch weather data for " + city);
                textViewTemperature.setText("");
            }
        }
    }
}
