package sample;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        String tripDetails = stringBuilder.toString();
        trips.computeIfAbsent(username, k -> new ArrayList<>()).add(tripDetails);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"status\": \"Trip created\", \"username\": \"" + username + "\", \"tripDetails\": \"" + tripDetails + "\"}");
    }
}
