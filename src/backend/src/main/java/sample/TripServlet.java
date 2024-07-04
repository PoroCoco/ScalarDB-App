package sample;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TripServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final ConcurrentMap<String, List<String>> trips = new ConcurrentHashMap<>();

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
        String tripDetails = String.format(
                "{\"driver_name\": \"%s\", \"trip_id\": %d, %s}",
                username,
                new Random().nextInt(1000000000),
                requestBody.substring(1, requestBody.length() - 1)
        );

        trips.computeIfAbsent(username, k -> new ArrayList<>()).add(tripDetails);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().write(String.format("{\"status\": \"Trip created\", \"tripDetails\": %s}", tripDetails));
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

        List<String> userTrips = trips.getOrDefault(username, new ArrayList<>());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder tripsJson = new StringBuilder("[");
        for (String trip : userTrips) {
            if (tripsJson.length() > 1) {
                tripsJson.append(",");
            }
            tripsJson.append(trip);
        }
        tripsJson.append("]");

        resp.getWriter().write(String.format("{\"trips\": %s}", tripsJson.toString()));
    }
}
