package sample;

import com.scalar.db.api.DistributedTransaction;
import com.scalar.db.api.DistributedTransactionManager;
import com.scalar.db.api.Get;
import com.scalar.db.api.Put;
import com.scalar.db.api.Result;
import com.scalar.db.exception.transaction.TransactionException;
import com.scalar.db.io.Key;
import com.scalar.db.service.TransactionFactory;
import java.io.IOException;
import java.util.Optional;
import java.io.File;

public class Kuruma {

  private static final String NAMESPACE = "bdd";

  private static final String TABLE_ACCOUNT = "account";
  private static final String ACCOUNT_PASSWORD = "password";
  private static final String ACCOUNT_USERNAME = "username";

  private static final String TABLE_TRIPS = "trips";
  private static final String TRIPS_ID = "trip_id";
  private static final String TRIPS_DESTINATION = "destination_city";
  private static final String TRIPS_DEPARTURE = "departure_city";
  private static final String TRIPS_DRIVER = "driver_id";
  
  private static final String TABLE_TRIPS_PASSENGERS = "trips_passengers";
  private static final String TRIPS_PASSENGERS_TRIP = "trip_id";
  private static final String TRIPS_PASSENGERS_USER = "user_id";

  private final DistributedTransactionManager manager;

  public Kuruma() throws IOException { //TODO close kuruma
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


  // public String account_get_name(String username) throws TransactionException {
  //   // Start a transaction
  //   DistributedTransaction tx = manager.start();

  //   try {

  //     // Retrieve the username for id
  //     Get get =
  //         Get.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLE_ACCOUNT)
  //             .partitionKey(Key.ofText(ACCOUNT_USERNAME, username))
  //             .build();
  //     Optional<Result> result = tx.get(get);

  //     String username = "None";
  //     if (result.isPresent()) {
  //       username = result.get().getText(ACCOUNT_USERNAME);
  //     }

  //     tx.commit();
  //     return username;
  //   } catch (Exception e) {
  //     tx.abort();
  //     throw e;
  //   }
  // }

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


  // public void account_login(String username, String password) throws TransactionException {
  //   // Start a transaction
  //   DistributedTransaction tx = manager.start();

  //   try {


  //     tx.commit();
  //   } catch (Exception e) {
  //     tx.abort();
  //     throw e;
  //   }
  // }



  // public void charge(String id, int amount) throws TransactionException {
  //   // Start a transaction
  //   DistributedTransaction tx = manager.start();

  //   try {
  //     // Retrieve the current balance for id
  //     Get get =
  //         Get.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, id))
  //             .build();
  //     Optional<Result> result = tx.get(get);

  //     // Calculate the balance
  //     int balance = amount;
  //     if (result.isPresent()) {
  //       int current = result.get().getInt(BALANCE);
  //       balance += current;
  //     }

  //     // Update the balance
  //     Put put =
  //         Put.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, id))
  //             .intValue(BALANCE, balance)
  //             .build();
  //     tx.put(put);

  //     // Commit the transaction (records are automatically recovered in case of failure)
  //     tx.commit();
  //   } catch (Exception e) {
  //     tx.abort();
  //     throw e;
  //   }
  // }

  // public void pay(String fromId, String toId, int amount) throws TransactionException {
  //   // Start a transaction
  //   DistributedTransaction tx = manager.start();

  //   try {
  //     // Retrieve the current balances for ids
  //     Get fromGet =
  //         Get.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, fromId))
  //             .build();
  //     Get toGet =
  //         Get.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, toId))
  //             .build();
  //     Optional<Result> fromResult = tx.get(fromGet);
  //     Optional<Result> toResult = tx.get(toGet);

  //     // Calculate the balances (it assumes that both accounts exist)
  //     int newFromBalance = fromResult.get().getInt(BALANCE) - amount;
  //     int newToBalance = toResult.get().getInt(BALANCE) + amount;
  //     if (newFromBalance < 0) {
  //       throw new RuntimeException(fromId + " doesn't have enough balance.");
  //     }

  //     // Update the balances
  //     Put fromPut =
  //         Put.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, fromId))
  //             .intValue(BALANCE, newFromBalance)
  //             .build();
  //     Put toPut =
  //         Put.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, toId))
  //             .intValue(BALANCE, newToBalance)
  //             .build();
  //     tx.put(fromPut);
  //     tx.put(toPut);

  //     // Commit the transaction (records are automatically recovered in case of failure)
  //     tx.commit();
  //   } catch (Exception e) {
  //     tx.abort();
  //     throw e;
  //   }
  // }

  // public int getBalance(String id) throws TransactionException {
  //   // Start a transaction
  //   DistributedTransaction tx = manager.start();

  //   try {
  //     // Retrieve the current balances for id
  //     Get get =
  //         Get.newBuilder()
  //             .namespace(NAMESPACE)
  //             .table(TABLENAME)
  //             .partitionKey(Key.ofText(ID, id))
  //             .build();
  //     Optional<Result> result = tx.get(get);

  //     int balance = -1;
  //     if (result.isPresent()) {
  //       balance = result.get().getInt(BALANCE);
  //     }

  //     // Commit the transaction
  //     tx.commit();

  //     return balance;
  //   } catch (Exception e) {
  //     tx.abort();
  //     throw e;
  //   }
  // }

  public void close() {
    manager.close();
  }
}
