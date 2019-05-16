package com.luttu.tjatt.BusinessLogic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchDecalBinaryTask extends AsyncTask<Void, Void, String> {

    private final String DEBUG_TAG = "FetchDecalBinaryTask";
    private TjattModel mModel;
    private Decal mDecal;

    public FetchDecalBinaryTask(TjattModel model, Decal decal) {
        mModel = model;
        mDecal = decal;
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
            Log.d(DEBUG_TAG, "Trying to fetch decal " + mDecal.getID());
            URL url = new URL("https://" + mModel.getSSLHelper().getServerIP() + ":443/api/decal/" + mDecal.getID());
            httpsConnection = (HttpsURLConnection) url.openConnection();
            httpsConnection.setRequestMethod("GET");
            httpsConnection.setRequestProperty("Content-Type", "image/bmp");
            httpsConnection.setSSLSocketFactory(mModel.getSSLHelper().getSSLContext().getSocketFactory());
            httpsConnection.setHostnameVerifier(mModel.getSSLHelper().getHostnameVerifier());
            httpsConnection.connect();
            Log.d(DEBUG_TAG, "Connected");
            int lengthOfFile = httpsConnection.getContentLength();
            // Read the input stream into a String
            InputStream inputStream = httpsConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            Log.d(DEBUG_TAG, "AVAILABLE BYTES: " + inputStream.available());
            byte[] decalBinary = IOUtils.toByteArray(inputStream);
            Log.d(DEBUG_TAG, "pasred binary");
            Log.d(DEBUG_TAG, decalBinary.toString());

            Bitmap bmp = BitmapFactory.decodeByteArray(decalBinary, 0,decalBinary.length );
            mDecal.setImgBinary(bmp);
            return forecastJsonStr;
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
    }
}
