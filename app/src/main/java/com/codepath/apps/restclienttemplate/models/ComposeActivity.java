package com.codepath.apps.restclienttemplate.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.R;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }

    EditText simpleEditText = (EditText) findViewById(R.id.tvEditText);
    String editText = simpleEditText.getText().toString();

}
