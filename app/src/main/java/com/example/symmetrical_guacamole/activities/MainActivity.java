package com.example.symmetrical_guacamole.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.symmetrical_guacamole.R;
import com.example.symmetrical_guacamole.fragments.PlaceholderFragment;


public class MainActivity extends AppCompatActivity {

    private static String JSON_URL = "https://gist.githubusercontent.com/steff2632/89bf836ff5eb6c89391d6c8fdc87d4f7/" +
            "raw/05d986d74640df24f67d106401d32012ab752730/cakes.json";

    private PlaceholderFragment placeholderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeholderFragment = PlaceholderFragment.getInstance(JSON_URL);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
        }
    }
}
