import java.util.*;

/**
 * High-Level Program:
 * Predictive Self-Healing Cache using Markov Chain
 * Author: You
 */

class PredictiveCache {

    // Cache storage
    private Map<String, String> cache = new HashMap<>();

    // Transition model (Markov Chain)
    private Map<String, Map<String, Integer>> transitions = new HashMap<>();

    private String lastAccess = null;

    // Access data
    public void access(String key) {
        System.out.println("\nRequesting: " + key);

        // Update Markov Chain
        if (lastAccess != null) {
            transitions.putIfAbsent(lastAccess, new HashMap<>());
            Map<String, Integer> nextMap = transitions.get(lastAccess);
            nextMap.put(key, nextMap.getOrDefault(key, 0) + 1);
        }

        lastAccess = key;

        // Self-healing cache
        if (!cache.containsKey(key)) {
            System.out.println("Cache miss detected → Self-healing...");
            cache.put(key, loadFromSource(key));
        } else {
            System.out.println("Cache hit ✔");
        }

        // Predict and prefetch
        String prediction = predictNext(key);
        if (prediction != null && !cache.containsKey(prediction)) {
            System.out.println("Prefetching predicted key: " + prediction);
            cache.put(prediction, loadFromSource(prediction));
        }
    }

    // Predict next access using Markov Chain
    private String predictNext(String key) {
        Map<String, Integer> nextMap = transitions.get(key);
        if (nextMap == null) return null;

        return nextMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    // Simulated database or API call
    private String loadFromSource(String key) {
        return "DATA[" + key + "]";
    }

    // Debug view
    public void printCache() {
        System.out.println("\nCurrent Cache State:");
        cache.forEach((k, v) -> System.out.println(k + " → " + v));
    }
}

public class Main {
    public static void main(String[] args) {

        PredictiveCache cache = new PredictiveCache();

        cache.access("UserProfile");
        cache.access("Dashboard");
        cache.access("UserProfile");
        cache.access("Settings");
        cache.access("UserProfile");
        cache.access("Dashboard");

        cache.printCache();
    }
}