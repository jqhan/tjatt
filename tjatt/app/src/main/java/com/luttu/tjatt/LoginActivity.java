package com.luttu.tjatt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.luttu.tjatt.BusinessLogic.JSONParser;
import com.luttu.tjatt.BusinessLogic.TjattModel;
import com.luttu.tjatt.BusinessLogic.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        TjattModel model = ((TjattApp) getApplication()).getModel();
        // Uncomment de 5 raderna under för att skippa login
        //String user = mEmailView.getText().toString();

//        model.setUser(new User(1, "Johan"));
//        Intent intent = new Intent(this, RoomListActivity.class);
//        startActivity(intent);
//        return;
//      /*

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(model, email, password);
            mAuthTask.execute((Void) null);
        }
//        */
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private TjattModel mModel;
        private final String DEBUG_TAG = "LoginTask";

        UserLoginTask(TjattModel model, String email, String password) {
            mModel = model;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            /*
            These two need to be declared outside the try/catch
            so that they can be closed in the finally block.
            */
            HttpsURLConnection httpsConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            DataOutputStream wr = null;

            try {

                URL url = new URL("https://" + mModel.getSSLHelper().getServerIP() + ":443/api/login");
                String postBody = "email=" + mEmail + "&password=" + mPassword; // "param1=data1&param2=data2 ...
                byte[] postData = postBody.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                httpsConnection = (HttpsURLConnection) url.openConnection();
                httpsConnection.setRequestMethod("POST");
                httpsConnection.setDoOutput(true);
                httpsConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpsConnection.setRequestProperty("charset", "utf-8");
                httpsConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                httpsConnection.setUseCaches(false);
                httpsConnection.setSSLSocketFactory(mModel.getSSLHelper().getSSLContext().getSocketFactory());
                httpsConnection.setHostnameVerifier(mModel.getSSLHelper().getHostnameVerifier());
                wr = new DataOutputStream(httpsConnection.getOutputStream());
                wr.write(postData);
                httpsConnection.connect();
                Log.d(DEBUG_TAG, "Connected");
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

                JSONArray jsonArray = new JSONArray(forecastJsonStr);

                mModel.setUser(JSONParser.parseLoginValidationData(jsonArray));
                Log.d(DEBUG_TAG, "Successfully validated user: " +
                        mModel.getUser().getUserName() +
                        " with ID: " + mModel.getUser().getID());
                return true;
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
                if (wr != null) {
                    try {
                        wr.flush();
                        wr.close();
                    } catch (final IOException e) {
                        Log.e(DEBUG_TAG, "Error closing DataOutputStream", e);
                    }
                }
                if (httpsConnection != null) {
                    httpsConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(DEBUG_TAG, "Error closing Reader stream", e);
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                Intent intent = new Intent(getApplication(), RoomListActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

