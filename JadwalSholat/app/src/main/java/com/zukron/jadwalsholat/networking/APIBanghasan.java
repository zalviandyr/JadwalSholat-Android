package com.zukron.jadwalsholat.networking;

/**
 * Project name is Jadwal Sholat
 * Created by Zukron Alviandy R on 7/21/2020
 */
public interface APIBanghasan {
    String baseUrl = "https://api.banghasan.com/";
    String allCity = baseUrl + "sholat/format/json/kota";
    String shalatSchedule = baseUrl + "sholat/format/json/jadwal/kota/{0}/tanggal/{1}";
}
