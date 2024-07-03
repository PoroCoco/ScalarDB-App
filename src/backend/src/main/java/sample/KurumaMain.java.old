package sample;

import java.io.File;

public class KurumaMain {

  public static void main(String[] args) throws Exception {

    String scalarDBProperties = null;
    Kuruma kuruma;

    if (scalarDBProperties != null) {
      kuruma = new Kuruma(scalarDBProperties);
    } else {
      scalarDBProperties = System.getProperty("user.dir") + File.separator + "scalardb.properties";
      kuruma = new Kuruma(scalarDBProperties);
    }

    if ("-create".equals(args[0]) && args.length == 3){
      String username = args[1];
      String password = args[2];
      kuruma.account_create(username, password);
      System.out.println("Created account "+ username + " with password " + password);
    // }else if ("-get_username".equals(args[0]) && args.length == 2){
    //   int id = Integer.valueOf(args[1]);
    //   String username = kuruma.account_get_name(id);
    //   System.out.println("The username of the account with id "+id+" is : " + username);
    }else if ("-login".equals(args[0]) && args.length == 3){
      String username = args[1];
      String password = args[2];
      Boolean is_logged = kuruma.account_login(username, password);
      if (is_logged){
        System.out.println("Correct password, you are logged in");
      }else{
        System.out.println("Wrong password");
      }
    }else{
      kuruma.close();
      System.out.println("Unknown command : " + args[1] + " with total count of args at " + args.length);
      printUsageAndExit();
      return;
    }

    kuruma.close();
  }

  //   if (action.equalsIgnoreCase("charge")) {
  //     if (to == null || amount < 0) {
  //       printUsageAndExit();
  //       return;
  //     }
  //     eMoney.charge(to, amount);
  //   } else if (action.equalsIgnoreCase("pay")) {
  //     if (to == null || amount < 0 || from == null) {
  //       printUsageAndExit();
  //       return;
  //     }
  //     eMoney.pay(from, to, amount);
  //   } else if (action.equalsIgnoreCase("getBalance")) {
  //     if (id == null) {
  //       printUsageAndExit();
  //       return;
  //     }
  //     int balance = eMoney.getBalance(id);
  //     System.out.println("The balance for " + id + " is " + balance);
  //   }
  //   eMoney.close();
  // }

  private static void printUsageAndExit() {
    System.err.println(
        "KurumaMain -action charge/pay/getBalance [-amount number (needed for charge and pay)] [-to id (needed for charge and pay)] [-from id (needed for pay)] [-id id (needed for getBalance)]");
    System.exit(1);
  }
}
