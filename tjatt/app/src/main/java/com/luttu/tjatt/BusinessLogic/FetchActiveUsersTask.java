package com.luttu.tjatt.BusinessLogic;

import android.os.AsyncTask;
import android.util.Log;

import com.luttu.tjatt.RoomFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchActiveUsersTask extends AsyncTask<Void, Void, String> {

    private final String DEBUG_TAG = "FetchActiveUsersTask";
    private RoomFragment mFragment;

    public FetchActiveUsersTask(RoomFragment fragment) {
        mFragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpsURLConnection httpsConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            URL url = new URL("https://" + mFragment.getModel().getSSLHelper().getServerIP() + ":443/api/activeUsers/" + mFragment.getRoom().getID());
            httpsConnection = (HttpsURLConnection) url.openConnection();
            httpsConnection.setRequestMethod("GET");
            httpsConnection.setRequestProperty("Content-Type", "application/json");
            httpsConnection.setSSLSocketFactory(mFragment.getModel().getSSLHelper().getSSLContext().getSocketFactory());
            httpsConnection.setHostnameVerifier(mFragment.getModel().getSSLHelper().getHostnameVerifier());
            httpsConnection.connect();
            Log.d(DEBUG_TAG, "Connected");
            int lengthOfFile = httpsConnection.getContentLength();
            // Read the input stream into a String
            InputStream inputStream = httpsConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d(DEBUG_TAG, line);
                buffer.append(line + "\n");
            }

            Log.d(DEBUG_TAG, "Read lines");
            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
            Log.d(DEBUG_TAG, forecastJsonStr);

            JSONObject jsonObject = new JSONObject(forecastJsonStr);

            for (Message msg : mFragment.getRoom().getMessages()) {
                msg.setUserIsOnline(false);
            }

            mFragment.getRoom().setActiveUsers(JSONParser.parseActiveUsersList(jsonObject));
            for (int activeUser : mFragment.getRoom().getActiveUsers()) {
                Log.d(DEBUG_TAG, "Online user: activeUser");
                for (Message msg : mFragment.getRoom().getMessages()) {
                    Log.d(DEBUG_TAG, "meddelande: " + msg.getPayload());
                    if (msg.getPostedByID() == activeUser) {
                        msg.setUserIsOnline(true);
                    }
                }
            }
            mFragment.updateUI();
            return forecastJsonStr;
        } catch (JSONException e) {
            Log.d(DEBUG_TAG, e.toString());
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.d(DEBUG_TAG, e.toString());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.d(DEBUG_TAG, e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(DEBUG_TAG, e.toString());
            e.printStackTrace();
        } finally {
            Log.d(DEBUG_TAG, "exiting task");
            if (httpsConnection != null) {
                httpsConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(DEBUG_TAG, "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mFragment.updateUI();
    }
}
