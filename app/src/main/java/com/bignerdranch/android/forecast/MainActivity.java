package com.bignerdranch.android.forecast;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.forecast.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//to accept the imports press ALT + ENTER.
public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;
    private TextView iconView;
    private TextView feelView;
    private SwipeRefreshLayout pullToRefresh;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocationManager mLocationManager;
        super.onCreate(savedInstanceState);

        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        /** Google Places API */

        String apiKey ="AIzaSyCHTo7Q6poJICaImGqcewX_eyPLPYsloI4";

        /** Search Places Implementation */

        /**
         * Initialize Places */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                System.out.println(String.valueOf(latitude) + String.valueOf(longitude));
                getWeather(binding, latitude, longitude);

            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);

            }
        });

        /** Request for Permission for the location */

       ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }

        /** Current Location is fetched */
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);


        //onLocationChanged(location);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);


        if(location!=null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.v(TAG, String.valueOf(latitude));
            getWeather(binding,latitude, longitude);

            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pullToRefresh.post(new Runnable() {
                        @Override
                        public void run() {

                            pullToRefresh.setRefreshing(false);
                            getWeather(binding, latitude, longitude);

                        }
                    });

                }
            });
        }
        else{
            Toast.makeText(this, "Couldn't fetch your current location", Toast.LENGTH_LONG).show();
        }

    }

    private void getWeather(final com.bignerdranch.android.forecast.databinding.ActivityMainBinding binding, double latitude1, double longitude2){

        iconView = findViewById(R.id.icon_text_id);
        feelView = findViewById(R.id.feels_like_id);
        final ConstraintLayout backgroundImage = (ConstraintLayout) findViewById(R.id.constraint_id);

            /** Dark Sky API */
        String ApiKey = "7cc099838d5daf065d9df6adba9515ae";

        String URLForecast = "https://api.darksky.net/forecast/" + ApiKey + "/" + latitude1 + "," + longitude2;

            /** Check if the network is available */
        if (isNetworkAvailable()) {


            /** Create OkHttp Client */
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URLForecast)
                    .build();
            Call call = client.newCall(request); //this is the call from the client to the server.
            //using asynchronous method:

            //Callback:
            //Asynchronous processing makes use of this, coz of communication bridge between background and the main thread.
            call.enqueue(new Callback() { //queue function by okhtttp
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                /** Display UI if the connection is successful */
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        //Response response = call.execute(); //this is the sychronous method
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(jsonData);
                            final CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocation(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getWind(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getTimeZone(),
                                    currentWeather.getDay()
                            );

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    binding.setWeather(displayWeather);
                                    iconView.setText(currentWeather.formatIcon(currentWeather.getIcon()));
                                    feelView.setText(String.valueOf(Math.round(currentWeather.getFeelLike())) + "\u2103");
                                    Drawable drawable = getResources().getDrawable(displayWeather.getIconNumber());
                                    backgroundImage.setBackground(drawable);


                                }
                            });


                        } else {
                            //Log.v(TAG, response.body().string());
                            alertUser(); //we will alert the user using a dialog box
                        }
                    } catch (IOException e) {
                        Log.v(TAG, "IO Exception caught", e);

                    } catch(JSONException e){
                        Log.v(TAG, "JSON Exception caught", e);
                    }


                }
            });


        }

        /** Extract the data from the Dark Sky API and update the UI*/
}
    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{

            JSONObject forecast = new JSONObject(jsonData);
            String timezone = forecast.getString("timezone");
            Log.i(TAG, "From JSON:" + timezone);


            JSONObject currently = forecast.getJSONObject("currently");

            CurrentWeather currentWeather = new CurrentWeather();
            //Log.i(TAG, "Time value" +String.valueOf(currently.getLong("time")));
            currentWeather.setHumidity(currently.getDouble("humidity"));
            currentWeather.setIcon(currently.getString("icon"));
            currentWeather.setLocation("Delta/BC");
            currentWeather.setWind(currently.getDouble("windSpeed"));
            currentWeather.setFeelLike(currently.getDouble("apparentTemperature"));
            currentWeather.setTemperature(currently.getDouble("temperature"));
            currentWeather.setTime(currently.getLong("time"));
            currentWeather.setTimeZone(timezone);
            currentWeather.setDay(currently.getLong("time"));



             return currentWeather;
    }

    private boolean isNetworkAvailable() {
        boolean isavailable = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            isavailable = true;
        }
        else{
            Toast.makeText(this, getString(R.string.network_toast_message), Toast.LENGTH_LONG).show();


        }
        return isavailable;

    }

    private void alertUser() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getSupportFragmentManager(), "error_dialog" );

    }


    @Override
    public void onLocationChanged(Location location) {

        if(location!=null) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String lat = String.valueOf(latitude);
            String longg = String.valueOf(longitude);

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
