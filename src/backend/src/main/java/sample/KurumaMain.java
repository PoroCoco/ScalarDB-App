package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KurumaMain {

  public static void main(String[] args) throws Exception {

    Kuruma kuruma = new Kuruma();

    if ("-create_account".equals(args[0]) && args.length == 3){
      String username = args[1];
      String password = args[2];
      kuruma.account_create(username, password);
      System.out.println("Created account "+ username + " with password " + password);
    }else if ("-login".equals(args[0]) && args.length == 3){
      String username = args[1];
      String password = args[2];
      Boolean is_logged = kuruma.account_login(username, password);
      if (is_logged){
        System.out.println("Correct password, you are logged in");
      }else{
        System.out.println("Wrong password");
      }
    }else if ("-create_trip".equals(args[0]) && args.length == 4){
      String driver = args[1];
      String departure = args[2];
      String destination = args[3];
      int trip_id = kuruma.trip_create(driver, departure, destination);
      System.out.println("Created a new trip id("+ trip_id +")");
    }else if ("-get_all_trips".equals(args[0]) && args.length == 1){
      List<Map<String, Object>> trips = kuruma.trip_get_all();
      String trips_json = convertListToJSON(trips);
      System.out.println(trips_json);
    }else if ("-get_driver_trips".equals(args[0]) && args.length == 2){ //TODO Check JSON is fine when there is no trips 
      String username = args[1];
      List<Map<String, Object>> trips = kuruma.trip_get_driver(username);
      String trips_json = convertListToJSON(trips);
      System.out.println(trips_json);
    }else if ("-get_account_all".equals(args[0]) && args.length == 1){
      List<Map<String, Object>> accounts = kuruma.account_get_all();
      int accounts_count = accounts.size();
      String accounts_json = convertListToJSON(accounts);
      System.out.println("There are " + accounts_count + " accounts : ");
      System.out.println(accounts_json);
    }else{
      kuruma.close();
      System.out.println("Unknown command : " + args[1] + " with total count of args at " + args.length);
      printUsageAndExit();
      return;
    }

    kuruma.close();
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


  private static void printUsageAndExit() {
    System.err.println(
        "KurumaMain -action charge/pay/getBalance [-amount number (needed for charge and pay)] [-to id (needed for charge and pay)] [-from id (needed for pay)] [-id id (needed for getBalance)]");
    System.exit(1);
  }
}
