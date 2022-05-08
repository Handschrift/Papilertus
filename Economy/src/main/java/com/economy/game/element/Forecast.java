package com.economy.game.element;

import com.economy.init.Economy;
import com.economy.util.MathUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Random;

//better singleton implementation
public class Forecast {

    private final HashMap<Double, String> textMapping = new HashMap<>();
    private final ForecastEntry[] data;

    private Forecast(int days) {
        final RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long startTime = rb.getStartTime();
        data = new ForecastEntry[days];

        textMapping.put(1.0, Economy.getEconomyConfig().getForecastNames().get(0));
        textMapping.put(2.0, Economy.getEconomyConfig().getForecastNames().get(1));
        textMapping.put(3.0, Economy.getEconomyConfig().getForecastNames().get(2));

        for (int i = 0; i < days; i++) {
            final Random random = new Random(startTime + LocalDate.now().plusDays(i).atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
            data[i] = new ForecastEntry(MathUtils.round(random.nextDouble() * 3, 1));
        }
    }

    public ForecastEntry[] getData() {
        return data;
    }

    public static Forecast getForecast() {
        //create a new instance every time!
        return new Forecast(7);
    }

    public class ForecastEntry {
        private double value;

        public ForecastEntry(double value) {
            this.value = value;

        }

        public double getValue() {
            return value;
        }

        public String getName() {
            for (double d : textMapping.keySet()) {
                if (value <= d) {
                    return textMapping.get(d);
                }
            }
            return "";
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
