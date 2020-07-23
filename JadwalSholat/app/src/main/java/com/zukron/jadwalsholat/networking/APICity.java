package com.zukron.jadwalsholat.networking;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zukron.jadwalsholat.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Project name is Jadwal Sholat
 * Created by Zukron Alviandy R on 7/21/2020
 */
public class APICity {
    private Context context;
    private OnResponse onResponse;

    public APICity(Context context, OnResponse onResponse) {
        this.context = context;
        this.onResponse = onResponse;
    }

    public void getAllCity() {
        String url = APIBanghasan.allCity;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<City> cities = new ArrayList<>();
                    JSONObject json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("kota");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonCity = array.getJSONObject(i);

                        String id = jsonCity.getString("id");
                        String nama = jsonCity.getString("nama");
                        City city = new City(id, nama);
                        cities.add(city);
                    }

                    onResponse.onResponse(cities);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResponse.errorResponse(error);
            }
        });

        execute(stringRequest);
    }

    private void execute(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public interface OnResponse {
        void onResponse(ArrayList<City> cities);

        void errorResponse(VolleyError error);
    }
}
