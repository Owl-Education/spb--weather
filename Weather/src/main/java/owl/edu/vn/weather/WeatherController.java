package owl.edu.vn.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

@Controller
public class WeatherController {


    @GetMapping("/weather")
    @ResponseBody
    public Map<String, Object> getWeather(@RequestParam(name = "city", required = false, defaultValue = "Paris") String city) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=98875111208701b956a9e50ee6b5d2e0";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Failed to get data from OpenWeatherMap API. Response code: " + responseCode);
                return null;
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            String responseBody = sb.toString();

            // Parse response body as JSON object using Gson
            Gson gson = new Gson();
            Map<String, Object> json = gson.fromJson(responseBody, Map.class);

            scanner.close();
            conn.disconnect();

            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
