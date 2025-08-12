package api.v2.travel_social_network_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TravelSocialNetworkServerApplication {

    public static void main(String[] args) {
        // Load .env if present (dev convenience) and bridge values to system properties.
        // Do not fail if the file is missing; fall back to OS environment variables.
        loadEnvToSystemPropertiesIfPresent();

        // Ensure these critical properties are available from either System properties or ENV
        bridgeEnvToSystemProperties("JWT_SECRET", "GOOGLE_CLIENT_ID", "GOOGLE_CLIENT_SECRET");

        // Optional: warn if expected secrets are still missing
        warnIfMissing("JWT_SECRET", "GOOGLE_CLIENT_ID", "GOOGLE_CLIENT_SECRET");

        SpringApplication.run(TravelSocialNetworkServerApplication.class, args);
    }

    private static void loadEnvToSystemPropertiesIfPresent() {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        // Common candidate locations
        List<Path> candidates = List.of(
            userDir.resolve(".env"),
            userDir.resolve("travel_social_network_server").resolve(".env")
        );

        for (Path path : candidates) {
            if (Files.exists(path)) {
                Map<String, String> kv = parseDotEnv(path);
                // Set as System properties if not already set
                kv.forEach((k, v) -> {
                    if (k != null && !k.isBlank() && v != null && System.getProperty(k) == null) {
                        System.setProperty(k, v);
                    }
                });
                break; // stop at first found
            }
        }
    }

    private static void bridgeEnvToSystemProperties(String... keys) {
        for (String key : keys) {
            if (System.getProperty(key) == null) {
                String envVal = System.getenv(key);
                if (envVal != null) {
                    System.setProperty(key, envVal);
                }
            }
        }
    }

    private static void warnIfMissing(String... keys) {
        for (String key : keys) {
            boolean missing = System.getProperty(key) == null && System.getenv(key) == null;
            if (missing) {
                System.err.println("[WARN] Missing configuration for " + key + ". " +
                    "Provide it via .env (KEY=VALUE) in the working directory or as an OS environment variable.");
            }
        }
    }

    private static Map<String, String> parseDotEnv(Path path) {
        Map<String, String> map = new LinkedHashMap<>();
        try (InputStream in = Files.newInputStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;

                // Allow lines like: export KEY=VALUE
                if (trimmed.startsWith("export ")) {
                    trimmed = trimmed.substring("export ".length()).trim();
                }

                int eq = trimmed.indexOf('=');
                if (eq <= 0) continue; // skip malformed lines

                String key = trimmed.substring(0, eq).trim();
                String value = trimmed.substring(eq + 1).trim();

                // Remove surrounding quotes if present
                if ((value.startsWith("\"") && value.endsWith("\"")) ||
                    (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }

                // Unescape common sequences
                value = value.replace("\\n", "\n")
                             .replace("\\r", "\r")
                             .replace("\\t", "\t");

                if (!key.isEmpty()) {
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("[WARN] Failed to read .env at " + path + ": " + e.getMessage());
        }
        return map;
    }
}