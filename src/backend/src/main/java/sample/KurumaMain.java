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
    }else if ("-register_passenger".equals(args[0]) && args.length == 3){
      String username = args[1]; 
      int tripId = Integer.parseInt(args[2]);
      kuruma.passenger_register(username, tripId);
      System.out.println("Registered user " + username + " on the trip n° " + tripId);
    }else if ("-get_passengers_from_trip".equals(args[0]) && args.length == 2){
      int tripId = Integer.parseInt(args[1]);
      List<String> passengers = kuruma.passenger_get_from_trip(tripId);
      System.out.println("For the trip n° " + tripId +", the registered passengers are : ");
      for(String passenger : passengers){
        System.out.println("\t" + passenger);
      }
    }else if ("-get_passengers_from_username".equals(args[0]) && args.length == 2){
      String username = args[1];
      List<Integer> trips = kuruma.passenger_get_from_username(username);
      System.out.println("For the user " + username +", the trips to which they are registred are : ");
      for(int trip : trips){
        System.out.println("\t" + trip);
      }
    }else{
      kuruma.close();
      System.out.println("Unknown command : " + args[0] + " with total count of args at " + args.length);
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
        "\n\n\u001B[36mKurumaMain different actions:\n"+
        "\t-create_account : args -> [username, password] Creates a new account.\n"+
        "\t-login : args -> [username, password] Verify if the username and password matches\n"+
        "\t-create_trip : args -> [driver, departure, destination] Creates a trip\n"+
        "\t-get_all_trips : args -> [] Returns all of the trips stored in the database.\n"+
        "\t-get_driver_trips : args -> [username] Returns all of the trips created by the driver 'username'.\n"+
        "\t-register_passenger : args -> [username, tripId] Registers the user 'username' to the specified trip.\n"+
        "\t-get_passengers_from_trip : args -> [tripId] Returns all of the passengers registred to the specified trip.\n"+
        "\t-get_passengers_from_username : args -> [username] Returns all of the trips where 'username' is registered.\n"+
        "\u001B[0m");
    System.exit(1);
  }
}
