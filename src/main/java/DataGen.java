import java.util.HashMap;
import java.util.Random;

public class DataGen {
    /*
     * This function generates data for the simulation
     * @param fileName: the name of the file to store the data
     * @param numCells: the number of cells in each floor
     * @param numFloors: the number of floors in the building
     * @return: void
     * 
     * The data is stored in a HashMap with the following structure:
     * {
     *    "floor-cell-1": {
     *       "Name": "AirConditioner",
     *      "Power": "1000",
     *     "Type": "1",
     *    "Frequency": "50",
     *   "Voltage": "220",
     * "Status": "OFF"
     * }...}
     * 
     * Type: 1 for AirConditioner and Refrigerator which work around the clock;
     *      2 for WashingMachine, DishWasher and ElectricCooker which work in a specific time period
     *     3 for Television, Computer and Lamp which work in a specific time period and can barely be modified
     */
    public static void main(String[] args) {
        String fileName = "data.txt";
        int numCells = 30;
        int numFloors = 7;
        DataStorage ds = new DataStorage();
        Random random = new Random();

        // Generate data
        for (int floor = 1; floor <= numFloors; floor++) {
            for (int cell = 1; cell <= numCells; cell++) {
                String position = String.format("%d-%02d-", floor, cell);
                int idx = 1;
                String key = position + idx;
                HashMap<String, String> value = new HashMap<String, String>();
                value.put("Name", "AirConditioner");
                value.put("Power", String.format("%.2f", (1 + Math.random()) * 2000));
                value.put("Type", String.valueOf(1));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "ON");
                value.put("TotalWorkTime", String.format("%.2f", (1 + Math.random()) * 9));
                ds.addData(key, value);

                idx += 1;
                key = position + idx;
                value = new HashMap<String, String>();
                value.put("Name", "Refrigerator");
                value.put("Power", String.format("%.2f", (1 + Math.random()) * 200));
                value.put("Type", String.valueOf(1));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "ON");
                value.put("TotalWorkTime", String.format("%.2f", (1 + Math.random()) * 9));
                ds.addData(key, value);

                idx += 1;
                key = position + idx;
                value = new HashMap<String, String>();
                value.put("Name", "WashingMachine");
                value.put("Power", String.format("%.2f", (1 + Math.random()) * 200));
                value.put("Type", String.valueOf(2));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", Math.random() > 0.3 ? "OFF" : "ON");
                value.put("StartTime", generateRandomTime(18, 1.5, random));
                value.put("TotalWorkTime", String.format("%.2f", (Math.random()) * 2));
                ds.addData(key, value);

                if (Math.random() < 0.2){
                    idx += 1;
                    key = position + idx;
                    value = new HashMap<String, String>();
                    value.put("Name", "DishWasher");
                    value.put("Power", String.format("%.2f", (5 + Math.random()) * 200));
                    value.put("Type", String.valueOf(2));
                    value.put("Frequency", String.valueOf(50));
                    value.put("Voltage", String.valueOf(220));
                    value.put("Status", Math.random() > 0.6 ? "OFF" : "ON");
                    value.put("StartTime", generateRandomTime(19, 1.0, random));
                    value.put("TotalWorkTime", String.format("%.2f", (1+Math.random())*0.5));
                    ds.addData(key, value);
                }

                idx += 1;
                key = position + idx;
                value = new HashMap<String, String>();
                value.put("Name", "ElectricCooker");
                value.put("Power", String.format("%.2f", (2 + Math.random()) * 200));
                value.put("Type", String.valueOf(2));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", Math.random() > 0.5 ? "OFF" : "ON");
                value.put("StartTime", generateRandomTime(18.5, 1.0, random));
                value.put("TotalWorkTime", String.format("%.2f", (1+Math.random())*0.5));
                ds.addData(key, value);

                idx += 1;
                key = position + idx;
                value = new HashMap<String, String>();
                value.put("Name", "Television");
                value.put("Power", String.format("%.2f", (1 + Math.random()) * 100));
                value.put("Type", String.valueOf(3));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", Math.random() > 0.4 ? "OFF" : "ON");
                value.put("StartTime", generateRandomTime(17, 2.5, random));
                value.put("TotalWorkTime", String.format("%.2f", (1 + Math.random()) * 3));
                ds.addData(key, value);

                idx += 1;
                key = position + idx;
                value = new HashMap<String, String>();
                value.put("Name", "Computer");
                value.put("Power", String.format("%.2f", (1 + Math.random()) * 300));
                value.put("Type", String.valueOf(3));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", Math.random() > 0.7 ? "OFF" : "ON");
                value.put("StartTime", generateRandomTime(19.5, 2.0, random));
                value.put("TotalWorkTime", String.format("%.2f", (1 + Math.random()) * 2));
                ds.addData(key, value);

                idx += 1;
                key = position + idx;
                value = new HashMap<String, String>();
                value.put("Name", "Lamp");
                value.put("Power", String.format("%.2f", (1 + Math.random()) * 50));
                value.put("Type", String.valueOf(3));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", Math.random() > 0.95 ? "OFF" : "ON");
                value.put("StartTime", generateRandomTime(18, 0.5, random));
                value.put("TotalWorkTime", String.format("%.2f", (2 + Math.random()) * 2));
                ds.addData(key, value);

                if (Math.random() < 0.2){
                    idx += 1;
                    key = position + idx;
                    value = new HashMap<String, String>();
                    value.put("Name", "Robot");
                    value.put("Power", String.format("%.2f", (1 + Math.random()) * 100));
                    value.put("Type", String.valueOf(4));                       // Type 4 for Robot
                    value.put("Frequency", String.valueOf(50));
                    value.put("Voltage", String.valueOf(220));
                    value.put("Status", Math.random() > 0.2 ? "OFF" : "ON");
                    value.put("TotalWorkTime", String.format("%.2f", (1 + Math.random()) * 9));
                    value.put("Capacity", String.format("%.2f", (1 + Math.random()) * 200));
                    ds.addData(key, value);
                }
            }
        }
    
        ds.writeDataToFile(fileName);
    }
    private static String generateRandomTime(double targetTime, Double stdDeviation, Random random) {
        int time = (int) (((targetTime + (stdDeviation * random.nextGaussian())) * 3600));
        while(time < 0){
            time += 86400;
        }
        time = time % 86400;
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;
    
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
