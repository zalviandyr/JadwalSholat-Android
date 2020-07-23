package com.zukron.jadwalsholat;

import org.junit.Test;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void start() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime now = LocalTime.parse("21:31", dateTimeFormatter);

        String[] names = {"Imsak", "Subuh", "Terbit", "Dhuha", "Dzuhur", "Ashar", "Magrib", "Isya"};
        ArrayList<LocalTime> times = new ArrayList<>();
        LocalTime imsak = LocalTime.parse("04:48", dateTimeFormatter);
        LocalTime subuh = LocalTime.parse("04:58", dateTimeFormatter);
        LocalTime terbit = LocalTime.parse("06:15", dateTimeFormatter);
        LocalTime dhuha = LocalTime.parse("06:42", dateTimeFormatter);
        LocalTime dzuhur = LocalTime.parse("12:22", dateTimeFormatter);
        LocalTime ashar = LocalTime.parse("15:45", dateTimeFormatter);
        LocalTime magrib = LocalTime.parse("18:23", dateTimeFormatter);
        LocalTime isya = LocalTime.parse("19:35", dateTimeFormatter);
        times.add(imsak);
        times.add(subuh);
        times.add(terbit);
        times.add(dhuha);
        times.add(dzuhur);
        times.add(ashar);
        times.add(magrib);
        times.add(isya);

        int lastIndex = times.size() - 1;
        // jika waktu sudah lebih dari waktu isya
        if (now.isAfter(times.get(lastIndex))) {
            System.out.println("Imsak");
            System.out.println(times.get(0));
            System.out.println(23-Math.abs(Duration.between(now, times.get(0)).toHours()));
            System.out.println(60-Math.abs(Duration.between(now, times.get(0)).toMinutes() % 60));
        } else {
            for (int i = 0; i < times.size(); i++) {
                if (Duration.between(now, times.get(i)).toMinutes() > 0) {
                    System.out.println(names[i]);
                    System.out.println(Duration.between(now, times.get(i)).toHours());
                    System.out.println(Duration.between(now, times.get(i)).toMinutes());
                    break;
                }
            }
        }
    }
}