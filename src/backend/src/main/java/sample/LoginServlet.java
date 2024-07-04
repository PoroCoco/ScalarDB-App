package sample;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> sessions = new ConcurrentHashMap<>();
    private Kuruma kuruma;

    public LoginServlet() {
        try {
            this.kuruma = new Kuruma();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            stringBuilder.append(line);
        }

        String[] credentials = stringBuilder.toString().split(":");
        String action = credentials[0];
        String username = credentials[1];
        String password = credentials[2];
        if ("login".equals(action)) {
            try {
                if (kuruma.account_login(username, password)) {
                    String sessionId = req.getSession(true).getId();
                    sessions.put(sessionId, username);

                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    System.out.println("{\"sessionId\": \"" + sessionId + "\", \"username\": \"" + username + "\"}");;

                    resp.getWriter()
                            .write("{\"sessionId\": \"" + sessionId + "\", \"username\": \"" + username + "\"}");
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (Exception e) {
            }
        }

        else if ("register".equals(action)) {
            try {

                kuruma.account_create(username, password);
                String sessionId = req.getSession(true).getId();
                sessions.put(sessionId, username);

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("{\"sessionId\": \"" + sessionId + "\", \"username\": \"" + username + "\"}");
            } catch (Exception e) {

            }

        }
    }

    public static String getUsername(String sessionId) {
        return sessions.get(sessionId);
    }
}
