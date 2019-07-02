package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ComposeActivity extends AppCompatActivity {


    private TwitterClient client;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = (TwitterClient) TwitterApp.getRestClient(this);
        // init the arraylist (data source)

        Button button = findViewById(R.id.bPost);
        text = findViewById(R.id.tvEditText);

        // Register the onClick listener with the implementation above
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                client.sendTweet(text.getText().toString(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,  byte[] responseBody) {
                        if (statusCode == 200) {
                            try {
                                JSONObject responseJSON = new JSONObject(new String(responseBody));
                                Tweet tweet = Tweet.fromJSON(responseJSON);
                                Intent data = new Intent();
                                data.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                                setResult(RESULT_OK, data); // set result code and bundle data for response
                                finish(); // closes the activity, pass data to parent
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("ComposeActivity", "error parsing data");
                        error.printStackTrace();
                    }
                });
            }
        });
    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        this.finish();
    }
}
