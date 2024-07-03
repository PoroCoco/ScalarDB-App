package sample;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> sessions = new ConcurrentHashMap<>();

    static {
        users.put("user1", "password1");
        users.put("user2", "password2");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        String[] credentials = stringBuilder.toString().split(":");
        String username = credentials[0];
        String password = credentials[1];

        if (users.containsKey(username) && users.get(username).equals(password)) {
            String sessionId = req.getSession(true).getId();
            sessions.put(sessionId, username);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"sessionId\": \"" + sessionId + "\", \"username\": \"" + username + "\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public static String getUsername(String sessionId) {
        return sessions.get(sessionId);
    }
}
