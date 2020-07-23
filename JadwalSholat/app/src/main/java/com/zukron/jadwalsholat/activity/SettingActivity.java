package com.zukron.jadwalsholat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zukron.jadwalsholat.R;
import com.zukron.jadwalsholat.adapter.CityAdapter;
import com.zukron.jadwalsholat.adapter.CitySpinnerAdapter;
import com.zukron.jadwalsholat.model.City;
import com.zukron.jadwalsholat.networking.APICity;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class SettingActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener, APICity.OnResponse {
    private MaterialToolbar mtSetting;
    private TextInputEditText inputFindCitySetting;
    private MaterialSpinner spinSetting;
    private Button btnOkSetting;
    private APICity apiCity;
    private static ArrayList<City> cities;
    private static City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mtSetting = findViewById(R.id.mt_setting);
        inputFindCitySetting = findViewById(R.id.input_find_city_setting);
        spinSetting = findViewById(R.id.spin_setting);
        btnOkSetting = findViewById(R.id.btn_ok_setting);

        init();
    }

    private void init() {
        apiCity = new APICity(this, this);
        apiCity.getAllCity();

        inputFindCitySetting.setOnEditorActionListener(this);
        btnOkSetting.setOnClickListener(this);
        setSupportActionBar(mtSetting);
        setTranslucentStatusBar();
    }

    private void setTranslucentStatusBar() {
        // set translucency status bar
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            if (SettingActivity.cities == null) {
                Toast.makeText(this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!SettingActivity.cities.isEmpty()) {
                String keyword = inputFindCitySetting.getText().toString().trim();
                ArrayList<City> citiesResult = new ArrayList<>();

                for (City city : SettingActivity.cities) {
                    if (city.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        citiesResult.add(city);
                        showSpinner(citiesResult);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void showSpinner(final ArrayList<City> cities) {
        CitySpinnerAdapter citySpinnerAdapter = new CitySpinnerAdapter(this, cities);
        spinSetting.setAdapter(citySpinnerAdapter);
        spinSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != -1) {
                    SettingActivity.city = cities.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("cityCode", SettingActivity.city.getId());
        editor.putString("cityName", SettingActivity.city.getName());
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        Toast.makeText(this, "Berhasil simpan perubahan", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(final ArrayList<City> cities) {
        SettingActivity.cities = cities;

        showSpinner(cities);
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}