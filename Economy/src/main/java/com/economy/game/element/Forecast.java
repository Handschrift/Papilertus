package com.economy.game.element;

import com.economy.util.MathUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Random;

//better singleton implementation
public class Forecast {

    private final HashMap<Double, String> textMapping = new HashMap<>();
    private final double[] data;

    private static final Forecast forecast = new Forecast(7);

    private Forecast(int days) {
        final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long startTime = rb.getStartTime();
        final Random random = new Random();
        data = new double[days];

        for (int i = 0; i < days; i++) {
            random.setSeed(startTime * LocalDateTime.of(LocalDate.now().plusDays(i), LocalTime.of(0, 0, 0)).toInstant(ZoneOffset.UTC).toEpochMilli());
            data[i] = MathUtils.round(random.nextDouble() * 3, 1);
        }
    }

    private final String name = "";

    public double[] getData() {
        return data;
    }

    public static Forecast getForecast() {
        return forecast;
    }
}
