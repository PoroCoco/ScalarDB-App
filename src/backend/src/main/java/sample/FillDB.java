package sample;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class FillDB {

    private static final String[] FIRST_NAMES = {
        "John", "Jane", "Alex", "Emily", "Chris", "Katie", "Michael", "Sarah", "David", "Laura",
        "Daniel", "Emma", "James", "Olivia", "Joshua", "Sophia", "Matthew", "Isabella", "Andrew", "Mia",
        "Ryan", "Charlotte", "Jacob", "Amelia", "Nicholas", "Harper", "Ethan", "Abigail", "Benjamin", "Avery",
        "Logan", "Ella", "Samuel", "Scarlett", "William", "Madison", "Joseph", "Lily", "Henry", "Sofia",
        "Nathan", "Grace", "Luke", "Aria", "Christian", "Chloe", "Jack", "Aubrey", "Oliver", "Zoey",
        "Thomas", "Hannah", "Sebastian", "Addison", "Zachary", "Evelyn", "Gabriel", "Natalie", "Elijah", "Lillian",
        "Hunter", "Stella", "Levi", "Leah", "Isaac", "Lucy", "Jonathan", "Maya", "Aaron", "Nora",
        "Charles", "Penelope", "Cameron", "Layla", "Connor", "Lillian", "Adrian", "Riley", "Jordan", "Victoria",
        "Brandon", "Nina", "Adam", "Piper", "Robert", "Alice", "Tyler", "Violet", "Austin", "Claire",
        "Dylan", "Hazel", "Jason", "Skylar", "Kevin", "Samantha", "Evan", "Ellie", "Brian", "Bella"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
        "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson",
        "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young", "Hernandez", "King",
        "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson", "Carter",
        "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans", "Edwards", "Collins",
        "Stewart", "Sanchez", "Morris", "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey",
        "Rivera", "Cooper", "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez",
        "James", "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross",
        "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores", "Washington",
        "Butler", "Simmons", "Foster", "Gonzales", "Bryant", "Alexander", "Russell", "Griffin", "Diaz", "Hayes"
    };

    private static final String[] PASSWORDS = {
    "P@ssw0rd123", "Secure#2021", "Adm1n!Acc3ss", "MyPassw0rd!", "L0gin@Safe", 
    "Qwerty@123", "S3cur3Pass!", "Admin#Acc3ss", "Pass1234!", "S@f3tyFirst", 
    "P@ssw0rd!123", "Myp@ssw0rd", "SecureMe!2021", "AdminP@ss!", "L0ckItUp!", 
    "P@55w0rd123", "S3cur3!Passw0rd", "Adm!nAccess123", "P@ssword#1", "S@feL0gin", 
    "Pass!word123", "S@f3Passw0rd", "Adm1nAcc3ss!", "MyP@ss1234", "Secur3!2021", 
    "P@ss!word123", "SafeAcc3ss#", "Admin2021#", "LockIt@Up", "P@ssw0rdSecure", 
    "S3curePassw0rd!", "Adm!n1234", "MyPass#Secure", "P@ss!1234", "SecureAcc3ss!", 
    "L0ginP@ssw0rd", "AdminS@f3ty", "Passw0rd@123", "P@ssS3cur3", "Safe!L0gin", 
    "Admin#Secur3", "S@f3tyAcc3ss", "P@ss123!word", "Secure#P@ss", "MyP@ssw0rd2021", 
    "Adm1n#Access", "P@ssw0rd!Safe", "S3cur3@Acc3ss", "AdminP@ssword", "L0ckIt@123", 
    "Passw0rdSecure!", "S3cureAdm!n", "Myp@ss#123", "P@ssw0rd2021!", "SafeLogin#1", 
    "S@f3tyFirst!", "Admin!Acc3ss", "P@ssw0rd#1", "S3cur3!Pass", "Adm1n2021#", 
    "P@ss123Secure", "L0ginAcc3ss!", "AdminSecure#", "Passw0rd@2021", "S@f3tyP@ss", 
    "SecureMe@2021", "Admin!S3cur3", "P@ssw0rd123!", "SafeL0gin#", "S3cur3@Pass", 
    "Adm1nPassw0rd", "MyPassw0rd#1", "L0ckItSecure!", "P@ssword@123", "S3cur3Pass!", 
    "Adm1nS@fe", "P@ss1234!", "SecureAcc3ss#1", "AdminP@ssw0rd", "L0gin@S3cur3", 
    "Passw0rd!Safe", "S3cur3Adm1n", "MyP@ss123", "P@ss!word2021", "SafeAcc3ss@", 
    "Admin@2021#", "P@sswordSecur3", "S3cur3@L0gin", "Adm1nPass!word", "MyP@ssw0rd!", 
    "L0ckItUp@123", "P@sswordSecure!", "S3cur3@Passw0rd", "AdminS@f3!", "P@ss123!word", 
    "Secure#Acc3ss", "MyPassw0rd@2021", "Adm1nSecure!", "P@ssw0rd#Secure"
};


    private static final String[] TOWN = {
        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
        "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte", "San Francisco", "Indianapolis", "Seattle", "Denver", "Washington",
        "Boston", "El Paso", "Nashville", "Detroit", "Oklahoma City", "Portland", "Las Vegas", "Memphis", "Louisville", "Baltimore",
        "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento", "Kansas City", "Mesa", "Atlanta", "Omaha", "Colorado Springs",
        "Raleigh", "Miami", "Virginia Beach", "Oakland", "Minneapolis", "Tulsa", "Arlington", "Tampa", "New Orleans", "Wichita",
        "Cleveland", "Bakersfield", "Aurora", "Anaheim", "Honolulu", "Santa Ana", "Riverside", "Corpus Christi", "Lexington", "Stockton",
        "Henderson", "Saint Paul", "St. Louis", "Cincinnati", "Pittsburgh", "Greensboro", "Anchorage", "Plano", "Lincoln", "Orlando",
        "Irvine", "Newark", "Toledo", "Durham", "Chula Vista", "Fort Wayne", "Jersey City", "St. Petersburg", "Laredo", "Madison",
         "Chandler", "Buffalo", "Lubbock", "Scottsdale", "Reno", "Glendale", "Gilbert", "Winston-Salem", "North Las Vegas", "Norfolk",
        "Chesapeake", "Garland", "Irving", "Hialeah", "Fremont", "Boise", "Richmond", "Baton Rouge", "Spokane", "Des Moines"
    };

    public static String getRandomValue(String[] myList) {
        Random random = new Random();
        return myList[random.nextInt(myList.length)];
    }

    public static String[] getRandomTraject(List<String> myList) {
        String departure = getRandomValue(TOWN);
        String destination;
        do {
            destination = getRandomValue(TOWN);
        } while (destination.equals(departure));

        String driver = getRandomValue(myList.toArray(new String[0]));
        return new String[]{driver,departure, destination};
    }

    public static void main(String[] args) throws Exception {
        
        Kuruma kuruma = new Kuruma();
        
        int NUM_PEOPLE = 10;
        int NUM_TRAJECT = 10;

        if (args.length >= 2) {
            try {
                NUM_PEOPLE = Integer.parseInt(args[0]);
                NUM_TRAJECT = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default values.");
            }
        }

        List<String> listAccount = new ArrayList<>();

        for (int i = 0; i < NUM_PEOPLE; i++) {
            String username = getRandomValue(FIRST_NAMES) + getRandomValue(LAST_NAMES);
            listAccount.add(username);
            System.out.println(username); //display
            String password = getRandomValue(PASSWORDS);
            System.out.println(password); //display
            kuruma.account_create(username,password);
        }

        for (int i = 0; i < NUM_TRAJECT; i++) {
            String[] trip = getRandomTraject(listAccount);
            kuruma.trip_create(trip[0], trip[1], trip[2]);

            String tripstr = trip[0] + " " + trip[1] + " " + trip[2];
            System.out.println(tripstr.toString()); //display
        }
        
        kuruma.close();
        return;


    }


}