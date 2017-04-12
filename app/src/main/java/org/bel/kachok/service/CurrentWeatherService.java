package org.bel.kachok.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.bel.kachok.ConnectionDetector;
import org.bel.kachok.utils.ApiKeys;
import org.bel.kachok.utils.AppPreference3;
import org.bel.kachok.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.bel.kachok.MainActivity33.mCitySearch;
import static org.bel.kachok.MainActivity33.mWeather;

public class CurrentWeatherService extends IntentService {

    private static final String TAG = "WeatherService";

    public static final String ACTION_WEATHER_UPDATE_OK = "org.asdtm.goodweather.action.WEATHER_UPDATE_OK";
    public static final String ACTION_WEATHER_UPDATE_FAIL = "org.asdtm.goodweather.action.WEATHER_UPDATE_FAIL";
    public static final String ACTION_WEATHER_UPDATE_RESULT = "org.asdtm.goodweather.action.WEATHER_UPDATE_RESULT";

    public CurrentWeatherService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        if (!connectionDetector.isNetworkAvailableAndConnected()) {
            return;
        }

        SharedPreferences preferences = getSharedPreferences(Constants.APP_SETTINGS_NAME, 0);
        String latitude = preferences.getString(Constants.APP_SETTINGS_LATITUDE, "51.51");
        String longitude = preferences.getString(Constants.APP_SETTINGS_LONGITUDE, "-0.13");
        String locale = AppPreference3.getLocale(this, Constants.APP_SETTINGS_NAME);
        String units = AppPreference3.getTemperatureUnit(this);

        String requestResult = "";
        HttpURLConnection connection = null;
        try {
            URL url = getUrl(latitude, longitude, units, locale);
            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                InputStream inputStream = connection.getInputStream();

                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    byteArray.write(buffer, 0, bytesRead);
                }
                byteArray.close();
                requestResult = byteArray.toString();
                AppPreference3.saveLastUpdateTimeMillis(this);
            }

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + requestResult);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        parseWeather(requestResult);
    }

    public void parseWeather(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObject = weatherArray.getJSONObject(0);
            if (weatherObject.has("description")) {
                mWeather.currentWeather.setDescription(weatherObject.getString("description"));
            }
            if (weatherObject.has("icon")) {
                mWeather.currentWeather.setIdIcon(weatherObject.getString("icon"));
            }

            JSONObject mainObj = jsonObject.getJSONObject("main");
            if (mainObj.has("temp")) {
                mWeather.temperature.setTemp(Float.parseFloat(mainObj.getString("temp")));
            }
            if (mainObj.has("pressure")) {
                mWeather.currentCondition.setPressure(
                        Float.parseFloat(mainObj.getString("pressure")));
            }
            if (mainObj.has("humidity")) {
                mWeather.currentCondition.setHumidity(mainObj.getInt("humidity"));
            }

            JSONObject windObj = jsonObject.getJSONObject("wind");
            if (windObj.has("speed")) {
                mWeather.wind.setSpeed(Float.parseFloat(windObj.getString("speed")));
            }
            if (windObj.has("deg")) {
                mWeather.wind.setDirection(Float.parseFloat(windObj.getString("deg")));
            }

            JSONObject cloudsObj = jsonObject.getJSONObject("clouds");
            if (cloudsObj.has("all")) {
                mWeather.cloud.setClouds(cloudsObj.getInt("all"));
            }

            if (jsonObject.has("name")) {
                mCitySearch.setCityName(jsonObject.getString("name"));
            }

            JSONObject sysObj = jsonObject.getJSONObject("sys");
            if (sysObj.has("country")) {
                mCitySearch.setCountryCode(sysObj.getString("country"));
            }
            mWeather.sys.setSunrise(sysObj.getLong("sunrise"));
            mWeather.sys.setSunset(sysObj.getLong("sunset"));

            mWeather.location = mCitySearch;
            sendResult(ACTION_WEATHER_UPDATE_OK);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON");
            sendResult(ACTION_WEATHER_UPDATE_FAIL);
        }
    }

    public void sendResult(String result) {
        Intent intent = new Intent(ACTION_WEATHER_UPDATE_RESULT);
        if (result.equals(ACTION_WEATHER_UPDATE_OK)) {
            intent.putExtra(ACTION_WEATHER_UPDATE_RESULT, ACTION_WEATHER_UPDATE_OK);
        } else if (result.equals(ACTION_WEATHER_UPDATE_FAIL)) {
            intent.putExtra(ACTION_WEATHER_UPDATE_RESULT, ACTION_WEATHER_UPDATE_FAIL);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private URL getUrl(String lat, String lon, String units, String lang) throws
                                                                          IOException {
        String url = Uri.parse(Constants.WEATHER_ENDPOINT).buildUpon()
                        .appendQueryParameter("lat", lat)
                        .appendQueryParameter("lon", lon)
                        .appendQueryParameter("APPID", ApiKeys.OPEN_WEATHER_MAP_API_KEY)
                        .appendQueryParameter("units", units)
                        .appendQueryParameter("lang", lang)
                        .build()
                        .toString();
        return new URL(url);
    }
}
