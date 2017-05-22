package braincollaboration.wordus.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PerformanceMeter {
    private static Map<String, PerformanceMeter> meters = new HashMap();
    private long startTime;
    private long endTime;
    private String name;

    private PerformanceMeter(String name) {
        this.name = name;
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
        meters.remove(name);
        print();
    }

    public static PerformanceMeter start(String name) {
        PerformanceMeter meter = new PerformanceMeter(name);
        meters.put(name, meter);
        return meter;
    }

    public static void end(String name) {
        PerformanceMeter meter = meters.get(name);
        if (meter != null) {
            meter.end();
        } else {
            Log.e("PerformanceMeter", "PerformanceMeter " + name + " not found. Please check the call sequence!");
        }
    }

    private void print() {

        Log.e("PerformanceMeter", name + ": " + (endTime - startTime) + "ms");

    }
}
