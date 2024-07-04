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

public class PassengerServlet extends HttpServlet {
    private Kuruma kuruma;

    public PassengerServlet() {
        try {
            this.kuruma = new Kuruma();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sessionId = req.getParameter("sessionId");
        String username = LoginServlet.getUsername(sessionId);

        if (username == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        String requestBody = stringBuilder.toString();
        
        System.out.println(requestBody);
        // int tripId = 0;
        // String trips_json = "";
        // try {
        //     kuruma.passenger_register(username, tripId);
        //     System.out.println("Added the user "+ username+" to trip " + tripId);
        //     // List<Integer> trip_ids = kuruma.passenger_get_from_username(username);

        //     // List<Map<String, Object>> trips = new ArrayList<>(); 
        //     // for (int id : trip_ids){
        //     //     trips.add(kuruma.trip_get_from_id(id));
        //     // }
        //     // trips_json = convertListToJSON(trips);
        //     // System.out.println(trips_json);
        // } catch (Exception e) {

        // }

        resp.getWriter().write(String.format("{\"trips\": ok}"));
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
}