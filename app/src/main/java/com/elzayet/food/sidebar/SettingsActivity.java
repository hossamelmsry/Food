package com.elzayet.food.sidebar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.elzayet.food.R;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner a_s_languageSpinner ,a_s_placeSpinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        a_s_languageSpinner = findViewById(R.id.a_s_languageSpinner);
        a_s_placeSpinner    = findViewById(R.id.a_s_placeSpinner);

        ArrayAdapter<CharSequence> languageSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.languageSpinner, android.R.layout.simple_spinner_item);
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a_s_languageSpinner.setAdapter(languageSpinnerAdapter);
        a_s_languageSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> placeSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.placeSpinner, android.R.layout.simple_spinner_item);
        placeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a_s_placeSpinner.setAdapter(placeSpinnerAdapter);
        a_s_placeSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String x = parent.getItemAtPosition(position).toString();
//        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}