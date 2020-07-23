package com.zukron.jadwalsholat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.zukron.jadwalsholat.R;
import com.zukron.jadwalsholat.adapter.CityAdapter;
import com.zukron.jadwalsholat.model.City;
import com.zukron.jadwalsholat.networking.APICity;

import java.util.ArrayList;

public class SelectCityActivity extends AppCompatActivity implements TextView.OnEditorActionListener, CityAdapter.OnSelected, APICity.OnResponse {
    private TextInputEditText inputSelectCity;
    private MaterialToolbar mtSelectCity;
    private ListView lvSelectCity;
    private ProgressBar pbSelectCity;
    private APICity apiCity;
    private ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        apiCity = new APICity(this, this);

        lvSelectCity = findViewById(R.id.lv_select_city);
        mtSelectCity = findViewById(R.id.mt_select_city);
        inputSelectCity = findViewById(R.id.input_select_city);
        pbSelectCity = findViewById(R.id.pb_select_city);

        init();
    }

    private void init() {
        changeStatusBarColor();
        setSupportActionBar(mtSelectCity);
        inputSelectCity.setOnEditorActionListener(this);

        // get data
        apiCity.getAllCity();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            if (!cities.isEmpty()) {
                String keyword = inputSelectCity.getText().toString().trim();
                ArrayList<City> citiesResult = new ArrayList<>();

                for (City city : cities) {
                    if (city.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        citiesResult.add(city);
                        showListCity(citiesResult);
                    }
                }

                return true;
            }
        }
        return false;
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        // clear flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // add flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // set status bar color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorHoloBlue));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void showListCity(ArrayList<City> cities) {
        CityAdapter cityAdapter = new CityAdapter(this, cities, this);
        lvSelectCity.setAdapter(cityAdapter);
    }

    @Override
    public void onResponse(ArrayList<City> cities) {
        pbSelectCity.setVisibility(View.INVISIBLE);
        this.cities = cities;

        showListCity(cities);
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onSelectedItem(City city) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("City", city);
        startActivity(intent);
    }
}