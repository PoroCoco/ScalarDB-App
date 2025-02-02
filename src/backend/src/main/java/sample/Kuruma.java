package sample;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Scan;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import java.io.IOException;
import java.util.Optional;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kuruma {

  private static final String NAMESPACE = "bdd";

  private static final String TABLE_ACCOUNT = "account";
  private static final String ACCOUNT_PASSWORD = "password";
  private static final String ACCOUNT_USERNAME = "username";

  private static final String TABLE_TRIPS = "trips";
  private static final String TRIPS_ID = "trip_id";
  private static final String TRIPS_DESTINATION = "destination_city";
  private static final String TRIPS_DEPARTURE = "departure_city";
  private static final String TRIPS_DRIVER = "driver_name";
  
  private static final String TABLE_TRIPS_PASSENGERS = "trips_passengers";
  private static final String TRIPS_PASSENGERS_TRIP = "trip_id";
  private static final String TRIPS_PASSENGERS_USER = "username";

  private final DistributedTransactionManager manager;

  public Kuruma() throws IOException {
    String scalarDBProperties = System.getProperty("user.dir") + File.separator + "scalardb.properties";
    TransactionFactory factory = TransactionFactory.create(scalarDBProperties);
    manager = factory.getTransactionManager();
  }


  public void account_create(String username, String password) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();

    try {
      Put put =
          Put.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_ACCOUNT)
              .partitionKey(Key.ofText(ACCOUNT_USERNAME, username))
              .textValue(ACCOUNT_PASSWORD, password)
              .build();
      tx.put(put);

      tx.commit();
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public int trip_create(String driver, String departureCity, String destinationCity) throws TransactionException {
    // Create random id for the trip
    int min = 1;
    int max = 999999999;
    int id = min + (int)(Math.random() * ((max - min) + 1));

    // Start a transaction
    DistributedTransaction tx = manager.start();

    // TODO : make sure that the driver is valid
    try {
      Put put =
          Put.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS)
              .partitionKey(Key.ofInt(TRIPS_ID, id))
              .textValue(TRIPS_DESTINATION, destinationCity)
              .textValue(TRIPS_DEPARTURE, departureCity)
              .textValue(TRIPS_DRIVER, driver)
              .build();
      tx.put(put);

      tx.commit();
      return id;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public List<Map<String, Object>> trip_get_all() throws TransactionException {
    List<Map<String, Object>> trips = new ArrayList<>();

    // Start a transaction
    DistributedTransaction tx = manager.start();

    try {
      Scan scan =
              Scan.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS)
              .all()
              .build();

      // Execute the scan operation
      List<Result> results = tx.scan(scan);

      // Process the results
      for (Result result : results) {
        Map<String, Object> trip = new HashMap<>();
        trip.put("trip_id", result.getInt("trip_id"));
        trip.put("destination_city", result.getText("destination_city"));
        trip.put("departure_city", result.getText("departure_city"));
        trip.put("driver_name", result.getText("driver_name"));

        // Add the trip to the list
        trips.add(trip);
      }

      tx.commit();
      return trips;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

    public List<Map<String, Object>> account_get_all() throws TransactionException {
    List<Map<String, Object>> accounts = new ArrayList<>();

    // Start a transaction
    DistributedTransaction tx = manager.start();

    try {
      Scan scan =
              Scan.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_ACCOUNT)
              .all()
              .build();

      // Execute the scan operation
      List<Result> results = tx.scan(scan);

      // Process the results
      for (Result result : results) {
        Map<String, Object> account = new HashMap<>();
        account.put(ACCOUNT_USERNAME, result.getText(ACCOUNT_USERNAME));
        account.put(ACCOUNT_PASSWORD, result.getText(ACCOUNT_PASSWORD));

        // Add the trip to the list
        accounts.add(account);
      }

      tx.commit();
      return accounts;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public List<Map<String, Object>> trip_get_driver(String driver_name) throws TransactionException {
    List<Map<String, Object>> trips = new ArrayList<>();

    // Start a transaction
    DistributedTransaction tx = manager.start();

    try {
      Scan scan =
              Scan.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS)
              .all()
              .build();

      // Execute the scan operation
      List<Result> results = tx.scan(scan);

      // Process the results
      for (Result result : results) {
        if (result.getText("driver_name").equals(driver_name)){
          Map<String, Object> trip = new HashMap<>();
          trip.put("trip_id", result.getInt("trip_id"));
          trip.put("destination_city", result.getText("destination_city"));
          trip.put("departure_city", result.getText("departure_city"));
          trip.put("driver_name", result.getText("driver_name"));

          // Add the trip to the list
          trips.add(trip);
        }
      }

      tx.commit();
      return trips;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public Boolean account_login(String username, String password) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();

    try {

      // Retrieve the username for id
      Get get =
          Get.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_ACCOUNT)
              .partitionKey(Key.ofText(ACCOUNT_USERNAME, username))
              .build();
      Optional<Result> result = tx.get(get);

      Boolean worked = false;
      if (result.isPresent()) {
        worked = result.get().getText(ACCOUNT_PASSWORD).equals(password);
      }

      tx.commit();
      return worked;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public void passenger_register(String username, int tripId) throws TransactionException {
    // Start a transaction
    DistributedTransaction tx = manager.start();
    try {
      Put put =
          Put.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS_PASSENGERS)
              .partitionKey(Key.ofInt(TRIPS_PASSENGERS_TRIP, tripId))
              .textValue(TRIPS_PASSENGERS_USER, username)
              .build();
      tx.put(put);

      tx.commit();
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public List<String> passenger_get_from_trip(int tripId) throws TransactionException {
    List<String> passengers = new ArrayList<>();
    DistributedTransaction tx = manager.start();
    try {
      Scan scan =
              Scan.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS_PASSENGERS)
              .all()
              .build();

      // Execute the scan operation
      List<Result> results = tx.scan(scan);

      // Process the results
      for (Result result : results) {
        if (result.getInt(TRIPS_PASSENGERS_TRIP) == tripId){
          passengers.add(result.getText(TRIPS_PASSENGERS_USER));
        }
      }

      tx.commit();
      return passengers;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

    public List<Integer> passenger_get_from_username(String username) throws TransactionException {
    List<Integer> trips = new ArrayList<>();
    DistributedTransaction tx = manager.start();
    try {
      Scan scan =
              Scan.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS_PASSENGERS)
              .all()
              .build();

      // Execute the scan operation
      List<Result> results = tx.scan(scan);

      // Process the results
      for (Result result : results) {
        if (result.getText(TRIPS_PASSENGERS_USER).equals(username)){
          trips.add(result.getInt(TRIPS_PASSENGERS_TRIP));
        }
      }

      tx.commit();
      return trips;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }

  public Map<String, Object> trip_get_from_id(int tripId) throws TransactionException {
    Map<String, Object> trip = new HashMap<>();
    DistributedTransaction tx = manager.start();
    try {
      // Retrieve the username for id
      Get get =
          Get.newBuilder()
              .namespace(NAMESPACE)
              .table(TABLE_TRIPS)
              .partitionKey(Key.ofInt(TRIPS_ID, tripId))
              .build();
      Optional<Result> result = tx.get(get);

      if (result.isPresent()) {
        trip.put("trip_id", result.get().getInt("trip_id"));
        trip.put("destination_city", result.get().getText("destination_city"));
        trip.put("departure_city", result.get().getText("departure_city"));
        trip.put("driver_name", result.get().getText("driver_name"));
      }

      tx.commit();
      return trip;
    } catch (Exception e) {
      tx.abort();
      throw e;
    }
  }



  public void close() {
    manager.close();
  }
}
