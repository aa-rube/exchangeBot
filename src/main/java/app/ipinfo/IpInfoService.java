package app.ipinfo;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final String IPV4_PATTERN = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private final Pattern pattern = Pattern.compile(IPV4_PATTERN);


    private String getCountryByIp(String ip) {
        String BASE_URL = "https://ipinfo.io/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + ip + "/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractCountry(String ip) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(getCountryByIp(ip));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return rootNode.get("country").asText();
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
