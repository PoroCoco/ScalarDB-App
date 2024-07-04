package sample;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class TripServlet extends HttpServlet {
    private Kuruma kuruma;

    public TripServlet() {
        try {
            this.kuruma = new Kuruma();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sessionId = req.getHeader("Session-Id");
        String username = LoginServlet.getUsername(sessionId);

        if (username == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        String requestBody = stringBuilder.toString();

        String city_data = requestBody.substring(1, requestBody.length() - 1);
        String[] cities = extractCities(city_data);
        System.out.println("Depart at " + cities[0]);
        System.out.println("arrival at " + cities[1]);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            int id = kuruma.trip_create(username, cities[0], cities[1]);
            resp.getWriter().write(String.format("{\"status\": \"Trip created\", \"tripDetails\": %s}", createTripJson(username, id, cities[0], cities[1])));
        } catch (Exception e) {
            return;
        }            
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sessionId = req.getParameter("sessionId");
        String username = LoginServlet.getUsername(sessionId);

        if (username == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String trips_json = "";
        try {
            List<Map<String, Object>> trips = kuruma.trip_get_driver(username);
            trips_json = convertListToJSON(trips);
        } catch (Exception e) {

        }

        resp.getWriter().write(String.format("{\"trips\": %s}", trips_json));
    }

    private static String convertListToJSON(List<Map<String, Object>> data) {
        // Convert the trip list to a json string
        String json = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        json = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
        throw new RuntimeException("Error converting accounts to JSON", e);
        }
        return json;
    }

    public static String[] extractCities(String input) {
        // Regular expression to match departure and destination cities
        String regex = "\"departure_city\":\"([^\"]+)\",\"destination_city\":\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Array to hold the extracted cities
        String[] cities = new String[2];

        if (matcher.find()) {
            cities[0] = matcher.group(1); // departure city
            cities[1] = matcher.group(2); // destination city
        } else {
            throw new IllegalArgumentException("Input string format is incorrect");
        }

        return cities;
    }

    public static String createTripJson(String driverName, int tripId, String departureCity, String destinationCity) {
        // Create a JSON object
        JSONObject tripJson = new JSONObject();
        
        // Add values to the JSON object
        tripJson.put("driver_name", driverName);
        tripJson.put("trip_id", tripId);
        tripJson.put("departure_city", departureCity);
        tripJson.put("destination_city", destinationCity);
        
        // Convert the JSON object to a string
        return tripJson.toString();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // New endpoint to get all trips
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String trips_json = "";
        try {
            Kuruma kuruma = new Kuruma();

            List<Map<String, Object>> trips = kuruma.trip_get_all();
            trips_json = convertListToJSON(trips);

            kuruma.close();
        } catch (Exception e) {

        }

        resp.getWriter().write(String.format("{\"trips\": %s}", trips_json));
    }
    
}