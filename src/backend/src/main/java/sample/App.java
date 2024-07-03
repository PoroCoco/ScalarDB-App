package sample;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class App {
    public static void main(String[] args) throws Exception {
        Server server = new Server(7777);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        // Ajouter les servlets pour les API
        handler.addServlet(new ServletHolder(new LoginServlet()), "/api/login");
        handler.addServlet(new ServletHolder(new TripServlet()), "/api/trips");
        
        // Configurer le servlet pour servir les fichiers statiques
        ServletHolder staticServlet = new ServletHolder("default", DefaultServlet.class);
        staticServlet.setInitParameter("resourceBase", "public");
        staticServlet.setInitParameter("dirAllowed", "true");
        staticServlet.setInitParameter("pathInfoOnly", "true");
        handler.addServlet(staticServlet, "/");

        // Ajouter le filtre CORS
        FilterHolder cors = handler.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        server.start();
        server.join();
    }
}
