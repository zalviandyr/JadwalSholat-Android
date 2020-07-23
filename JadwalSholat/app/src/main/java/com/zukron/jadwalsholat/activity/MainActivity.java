package com.zukron.jadwalsholat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.zukron.jadwalsholat.R;
import com.zukron.jadwalsholat.adapter.ShalatAdapter;
import com.zukron.jadwalsholat.model.City;
import com.zukron.jadwalsholat.model.Shalat;
import com.zukron.jadwalsholat.networking.APIShalatSchedule;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, APIShalatSchedule.OnResponse {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private MaterialToolbar mtMain;
    private TextView tvNextShalatSchedule, tvNextShalatCountdown, tvDateToday;
    private RelativeLayout rlMainContent;
    private ListView lvShalat;
    private ProgressBar pbMainContent;
    private APIShalatSchedule apiShalatSchedule;
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNextShalatSchedule = findViewById(R.id.tv_next_shalat_schedule);
        tvNextShalatCountdown = findViewById(R.id.tv_next_shalat_countdown);
        tvDateToday = findViewById(R.id.tv_date_today);
        rlMainContent = findViewById(R.id.rl_main_content);
        lvShalat = findViewById(R.id.lv_shalat);
        pbMainContent = findViewById(R.id.pb_main_content);
        navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        mtMain = findViewById(R.id.mt_main);

        checkPreferences();
        init();
    }

    private void checkPreferences() {
        SharedPreferences preferences = getApplication().getSharedPreferences("MyPref", MODE_PRIVATE);
        String cityCode = preferences.getString("cityCode", "600");
        String cityName = preferences.getString("cityName", "Bungo");

        if (getIntent().getParcelableExtra("City") != null) {
            city = getIntent().getParcelableExtra("City");
        } else {
            city = new City(cityCode, cityName);
        }
    }

    private void init() {
        apiShalatSchedule = new APIShalatSchedule(this, this);
        navView.setNavigationItemSelectedListener(this);
        setTranslucentStatusBar();

        mtMain.setTitle(city.getName());
        setSupportActionBar(mtMain);
        apiShalatSchedule.getShalat(city.getId());
    }

    private void setTranslucentStatusBar() {
        // set translucency status bar
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_select_city:
                Intent selectCityIntent = new Intent(this, SelectCityActivity.class);
                startActivity(selectCityIntent);
                break;
            case R.id.nav_item_setting:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResponse(Shalat shalat) {
        rlMainContent.setVisibility(View.VISIBLE);
        pbMainContent.setVisibility(View.INVISIBLE);

        tvDateToday.setText(shalat.getDateToday());
        ShalatAdapter shalatAdapter = new ShalatAdapter(this, shalat);
        lvShalat.setAdapter(shalatAdapter);

        setHeaderTime(shalat);
    }

    private void setHeaderTime(Shalat shalat) {
        ArrayList<String> names = shalat.getName();
        ArrayList<LocalTime> times = shalat.getTimes();
        int lastIndex = times.size() - 1;

        LocalTime now = LocalTime.now();

        // jika waktu sudah lebih dari waktu isya
        if (now.isAfter(times.get(lastIndex))) {
            tvNextShalatSchedule.setText(names.get(0));
            long hour = 23 - Math.abs(durationHour(times.get(0)));
            long minute = 60 - Math.abs(durationMinute(times.get(0)));
            String duration = hour + " jam : " + minute + " menit lagi";
            tvNextShalatCountdown.setText(duration);
        } else {
            for (int i = 0; i < names.size(); i++) {
                if (durationMinute(times.get(i)) > 0) {
                    tvNextShalatSchedule.setText(names.get(i));
                    tvNextShalatCountdown.setText(format(times.get(i)));
                    break;
                }
            }
        }
    }

    private long durationHour(LocalTime localTime) {
        LocalTime now = LocalTime.now();
        return Duration.between(now, localTime).toHours();
    }

    private long durationMinute(LocalTime localTime) {
        LocalTime now = LocalTime.now();
        return Duration.between(now, localTime).toMinutes() % 60;
    }

    private String format(LocalTime localTime) {
        long durationHour = durationHour(localTime);
        long durationMinute = durationMinute(localTime);
        return durationHour + " jam : " + durationMinute + " menit lagi";
    }

    @Override
    public void errorResponse(VolleyError error) {
        error.printStackTrace();
    }
}