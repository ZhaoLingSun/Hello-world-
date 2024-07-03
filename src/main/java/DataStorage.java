import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * This class stores data for the simulation
 * The data is stored in a HashMap with the following structure:
 * {
 *      "floor-cell-1": {
 *        "Name": "AirConditioner",
 *       "Power": "1000",
 *     "Type": "1",
 *    "Frequency": "50",
 *   "Voltage": "220",
 *  "Status": "OFF"
 * }...}
 * 
 * Data can be written to and read from a file (writeDataToFile, readDataFromFile)
 * Data can be updated, removed, cleared, and printed (updateData, removeData, clearData, printData)
 * Data can be checked for key and value existence (containsKey, containsValue)
 * Data can be shown in the console (showData)
 */


public class DataStorage {
    HashMap<String, HashMap<String, String>> data;

    public DataStorage() {
        this.data = new HashMap<String, HashMap<String, String>>();
    }
    public void addData(String key, HashMap<String, String> value) {
        data.put(key, value);
    }
    public HashMap<String, HashMap<String, String>> getData() {
        return data;
    }
    public void updateData(String key, String field, String value) {
        if (data.containsKey(key)) {
            data.get(key).put(field, value);
        } else {
            HashMap<String, String> newValue = new HashMap<String, String>();
            newValue.put(field, value);
            data.put(key, newValue);
        }
    }
    public void removeData(String key) {
        data.remove(key);
    }
    public void clearData() {
        data.clear();
    }
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }
    public boolean containsValue(HashMap<String, String> value) {
        return data.containsValue(value);
    }
    public void printData() {
        for (String key : data.keySet()) {
            System.out.println(key + ": " + data.get(key));
        }
    }
    public void writeDataToFile(String fileName) {
        // Write data to file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            TreeSet<String> sortedKeys = new TreeSet<>(data.keySet());
            for (String key : sortedKeys) {
                bufferedWriter.write(key + ";");
                TreeMap<String, String> sortedValue = new TreeMap<>(data.get(key));
                for (String k : sortedValue.keySet()) {
                    bufferedWriter.write(k + "=" + sortedValue.get(k) + ",");
                }
                bufferedWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void readDataFromFile(String fileName) {
        // Read data from file
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            data.clear();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                String key = parts[0];
                HashMap<String, String> value = new HashMap<String, String>();
                String[] fields = parts[1].split(",");
                for (String field : fields) {
                    String[] pair = field.split("=");
                    value.put(pair[0], pair[1]);
                }
                data.put(key, value);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showData() {
        TreeSet<String> sortedKeys = new TreeSet<>(data.keySet());
        for (String key : sortedKeys) {
            System.out.println(key + ": " + data.get(key));
        }
    }
}
