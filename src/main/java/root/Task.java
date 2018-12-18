package root;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Task {

    private String propsFile;
    private String inputFile;
    private String outputFile;

    private void readSource() throws IOException {
        propsFile = new String(Files.readAllBytes(Paths.get("example.properties")));
        System.out.println(propsFile + "\n");
    }

    private void readInput() throws IOException {
        inputFile = new String(Files.readAllBytes(Paths.get("input.xml")));
        System.out.println(inputFile);
    }

    private void readOutput() throws IOException {
        outputFile = new String(Files.readAllBytes(Paths.get("output.xml")));
        System.out.println(outputFile);
    }

    private void showHelp() {
        System.out.println(
                "Use 'i' parameter for reading input file" + "\n" +
                        "Use 'o' parameter for reading output file" + "\n" +
                        "Use 'p' parameter for reading properties file" + "\n" +
                        "Use 'i' and 'o' parameters for processing" + "\n" +
                        "properties file to input file and store it as output file");
    }

    public String createTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return now.format(formatter);
    }

    public String createUUID() {
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid);
    }

    public String createRequestID() {
        Random r = new Random();
        long mostSigBits = r.nextLong();
        long leastSigBits = r.nextLong();
        return String.valueOf(new UUID(mostSigBits, leastSigBits));
    }

    public void readSourceAndProcessData() throws IOException {

        propsFile = "example.properties";
        Map<String, String> propsMap = new HashMap();

        inputFile = new String(Files.readAllBytes(Paths.get("input.xml")));

        BufferedReader bufferedReader = new BufferedReader(new FileReader(propsFile));
        String line;
        while ((line = bufferedReader.readLine()) != null) {

            line = line.replace("\"", "");
            String[] parts = line.split("=");

            for (int i = 0; i < parts.length; i++) {
                if (parts[i].toUpperCase().substring(0, 2).contains("<C")) {
                    parts[i] = createTimeStamp();
                }

                if (parts[i].toUpperCase().substring(0, 2).contains("<U")) {
                    parts[i] = createUUID();
                }

                if (parts[i].toUpperCase().substring(0, 2).contains("<R")) {
                    parts[i] = createRequestID();
                }
            }

            String key = parts[0];
            String value = parts[1];
            propsMap.put(key, value);
        }

        for (Map.Entry<String, String> entry : propsMap.entrySet()) {
            outputFile = inputFile.replace("${" + entry.getKey() + "}", entry.getValue());
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.xml", false));
            writer.write(outputFile);
            writer.close();
        }
    }

    public static void main(String... args) throws IOException {

        Task task = new Task();
        
        task.readSourceAndProcessData();
//        task.readSource();
//        task.readInput();
//        task.readOutput();
//        task.showHelp();

        if (args.length == 1 && args[0].equalsIgnoreCase("i")) {
            task.readInput();
        } else if (args.length == 1 && args[0].equalsIgnoreCase("o")) {
            task.readOutput();
        } else if (args.length == 1 && args[0].equalsIgnoreCase("p")) {
            task.readSource();
        } else if (args.length == 2
                && args[0].equalsIgnoreCase("i")
                && args[1].equalsIgnoreCase("o")) {
            task.readSourceAndProcessData();
        } else if (args.length == 1 && args[0].equalsIgnoreCase("h")) {
            task.showHelp();
        } else {
            System.out.println(args.length);
        }
    }
}
