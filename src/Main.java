import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {

        GetGroupedCategory();
        GetMostAppsDevelopedByACompany();
        GetTop3Developers();
        HowManyAppsWIth1000();
        HowManyAppsWIth10000();
        TotalNumberOfDonwloadedPaidAppsVsFreeApps();

    }

    private static void TotalNumberOfDonwloadedPaidAppsVsFreeApps() {
        Map<Boolean, PaidAppInfo> PaidAppsVsFreeApps = new HashMap<>();
        File FileInput = new File("src/GooglePlayTest.csv");

        try {
            Scanner s = new Scanner(FileInput);
            s.nextLine();

            while (s.hasNextLine()) {
                String line = s.nextLine();

                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                    String installStringPrice = parts[9].replaceAll("[^\\d]", "");
                    Integer price = Integer.parseInt(installStringPrice);
                    String installStringDownloads = parts[5].replaceAll("[^\\d]", "");
                    Integer downloads = Integer.parseInt(installStringDownloads);

                    boolean isPaid = price > 0;

                    PaidAppsVsFreeApps.put(isPaid, PaidAppsVsFreeApps.getOrDefault
                            (isPaid, new PaidAppInfo(0, 0)).increment(downloads));
                } catch (Exception e) {
                    System.out.println("Error processing line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + FileInput);
        }

        WriteToFilePaidApps(PaidAppsVsFreeApps, "PaidAppsVsFreeApps.csv");
    }

    private static void WriteToFilePaidApps(Map<Boolean, PaidAppInfo> paidAppsVsFreeApps, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("Paid,Count,TotalDownloads\n");

            for (Map.Entry<Boolean, PaidAppInfo> entry : paidAppsVsFreeApps.entrySet()) {
                Boolean isPaid = entry.getKey();
                PaidAppInfo info = entry.getValue();
                writer.write(isPaid + "," + info.getCount() + "," + info.getTotalDownloads() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + filename);
        }
    }


    private static void HowManyAppsWIth10000() {
        Map<String, Integer> AppsWith10000 = new HashMap<>();
        File FileInput = new File("src/GooglePlayTest.csv");
        final int[] budget = {10000};

        try {
            Scanner s = new Scanner(FileInput);
            s.nextLine();

            while (s.hasNextLine()) {
                String line = s.nextLine();
                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    Integer price = Integer.parseInt(parts[9]);
                    String name = parts[0];
                    AppsWith10000.put(name, price);

                } catch (Exception e) {
                    System.out.println("Error processing line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + FileInput);
        }

        writeAppsPurchased(AppsWith10000, "AppsPurchasedWith10000.csv", budget);
    }

    private static void HowManyAppsWIth1000() {
        Map<String, Integer> AppsWith1000 = new HashMap<>();
        File FileInput = new File("src/GooglePlayTest.csv");
        final int budget[] = {1000};

        try {
            Scanner s = new Scanner(FileInput);
            s.nextLine();

            while (s.hasNextLine()) {
                String line = s.nextLine();
                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    Integer price = Integer.parseInt(parts[9]);
                    String name = parts[0];
                    AppsWith1000.put(name, price);

                } catch (Exception e) {
                    System.out.println("Error processing line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + FileInput);
        }

        writeAppsPurchased(AppsWith1000, "AppsPurchasedWith1000.csv", budget);
    }

    private static void writeAppsPurchased(Map<String, Integer> appsWith, String filename, int[] budget) {
        Map<String, Integer> purchasedApps = new HashMap<>();
        try  {
            FileWriter writer = new FileWriter(filename);
            writer.write("Name,Price\n");
            appsWith.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .forEach(entry -> {
                        if (entry.getValue() <= budget[0] && entry.getValue() > 0) {
                            purchasedApps.put(entry.getKey(), entry.getValue());
                            budget[0] -= entry.getValue();
                        }
                    });

            for (Map.Entry<String, Integer> entry : purchasedApps.entrySet()) {
                String name = entry.getKey();
                Integer price = entry.getValue();
                writer.write(name + "," + price + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + filename);
        }
    }

    private static void GetTop3Developers() {
        Map<String, Integer> companyAppCount = new HashMap<>();
        File FileInput =new File( "src/GooglePlayTest.csv");

        try {
            Scanner s = new Scanner(FileInput);
            s.nextLine();
            while(s.hasNextLine()){
                String line = s.nextLine();
                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String email = parts[15];
                    List<String> domains = Arrays.asList("gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "icloud.com");

                    if (email.contains("@") && domains.contains(email.split("@")[1])) {
                        companyAppCount.put(email, companyAppCount.getOrDefault(email, 0) + 1);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing line: " + line);
                }
            }

            writeTopDevelopersToFile(companyAppCount, "top3Developers.csv", 3);

        } catch (IOException e) {
            System.out.println("Error reading file: " + FileInput);
        }
    }

    private static void writeTopDevelopersToFile(Map<String, Integer> companyAppCount, String fileName, int topCount) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Email,Number of Apps\n");

            companyAppCount.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .limit(topCount)
                    .forEach(entry -> {
                        try {
                            writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                        } catch (IOException e) {
                            System.out.println("Error writing file: " + fileName);
                        }
                    });

        } catch (IOException e) {
            System.out.println("Error creating FileWriter for file: " + fileName);
        }
    }





    private static void GetMostAppsDevelopedByACompany() throws IOException {
        Map<String, Integer> companyAppCount = new HashMap<>();
        File FileInput = new File("src/GooglePlayTest.csv");
        try{
            Scanner s = new Scanner(FileInput);
            s.nextLine();
            while(s.hasNextLine()){
                String line = s.nextLine();
                try{
                    String[] lines = line.split ( ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String appId = lines[1];
                    String companyName = appId.split("\\.")[1];

                    companyAppCount.put(companyName, companyAppCount.getOrDefault(companyName, 0) + 1);

                } catch (Exception e) {
                  System.out.println(line);
                  }
                }

        } catch (IOException e) {
            System.out.println("Error reading file");
        }


        writeTopCompaniesToFile(companyAppCount, "AppCount.csv", 100);
    }

    private static void writeTopCompaniesToFile(Map<String, Integer> companyAppCount, String fileName, int topCount) {
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write("Company Name,Number of Apps\n");

                companyAppCount.entrySet().stream()
                        .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                        .limit(topCount)
                        .forEach(entry -> {
                            try {
                                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                            } catch (IOException e) {
                                System.out.println("Error writing file");
                            }
                        });

            } catch (IOException e) {
                System.out.println("Error writing file");
            }
        }





    private static void GetGroupedCategory() {
        Map<String, ArrayList<String>> GroupedCategorys = new HashMap<>();
        File FileInput = new File("src/GooglePlayTest.csv");
        try{
            Scanner s = new Scanner(FileInput);
            s.nextLine();
            while(s.hasNextLine()){
                String line = s.nextLine();
                try{
                    String[] lines = line.split ( ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    String name = lines[0];
                    String category = lines[2];
                    if(!GroupedCategorys.containsKey(category)) {
                        GroupedCategorys.put(category, new ArrayList<>());
                    }
                    GroupedCategorys.get(category).add(name);
                }catch (Exception e){
                    System.out.println(line);
                }
            }

        }catch (Exception innerE){
            System.out.println("Error: reading file");
        }

        WriteToFileCategory(GroupedCategorys, "GroupedCategory.csv");
    }

    private static void WriteToFileCategory(Map<String, ArrayList<String>> groupedCategorys, String filename) {

        try ( FileWriter writer = new FileWriter(filename)) {
            writer.write("Name,Category" + "\n");
            for (Map.Entry<String, ArrayList<String>> entry : groupedCategorys.entrySet()) {
                String category = entry.getKey();
                ArrayList<String> names = entry.getValue();
                for (String name : names) {
                    writer.write(name + "," + category + "\n");
                }

            }
        }catch (Exception e){
            System.out.println("Error: writing file");
        }
    }

}

