package app.ipinfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
public class IpInfoService {
    private static final String IPV4_PATTERN = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private static final Pattern pattern = Pattern.compile(IPV4_PATTERN);
    private static final String BASE_URL = "https://ipinfo.io/";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getCountryByIp(String ip) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + ip + "/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    public String extractCountry(String ip) {
        try {
            JsonNode rootNode = objectMapper.readTree(getCountryByIp(ip));
            return rootNode.get("country").asText();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmoji(String ip) {
        if (!pattern.matcher(ip).matches()) {
            return "❓";
        }

        String countryCode = extractCountry(ip);
        if (countryCode == null || countryCode.length() != 2) {
            throw new IllegalArgumentException("Country code must be 2 characters long.");
        }
        countryCode = countryCode.toUpperCase();

        int firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6;

        return new String(Character.toChars(firstLetter)) + new String(Character.toChars(secondLetter));
    }
}