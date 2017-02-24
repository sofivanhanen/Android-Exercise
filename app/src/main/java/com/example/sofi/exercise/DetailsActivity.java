package com.example.sofi.exercise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView typeMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e("DetailsActivity", "getSupportActionBar returned null");
            Log.i("DetailsActivity", e.getStackTrace().toString());
        }

        Bundle bundle = getIntent().getExtras();

        title = (TextView) findViewById(R.id.tv_details_title);
        title.setText(bundle.getString("Title"));

        typeMedia = (TextView) findViewById(R.id.tv_details_information);
        typeMedia.setText(bundle.getString("Information"));
    }
}
