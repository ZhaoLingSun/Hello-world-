import java.util.HashMap;

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

        // Generate data
        for (int floor = 1; floor <= numFloors; floor++) {
            for (int cell = 1; cell <= numCells; cell++) {
                String position = String.format("%d-%02d-", floor, cell);
                String key = position + 1;
                HashMap<String, String> value = new HashMap<String, String>();
                value.put("Name", "AirConditioner");
                value.put("Power", String.valueOf((1 + Math.random()) * 1000));
                value.put("Type", String.valueOf(1));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 2;
                value = new HashMap<String, String>();
                value.put("Name", "Refrigerator");
                value.put("Power", String.valueOf((1 + Math.random()) * 100));
                value.put("Type", String.valueOf(1));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 3;
                value = new HashMap<String, String>();
                value.put("Name", "WashingMachine");
                value.put("Power", String.valueOf((1 + Math.random()) * 200));
                value.put("Type", String.valueOf(2));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 4;
                value = new HashMap<String, String>();
                value.put("Name", "DishWasher");
                value.put("Power", String.valueOf((5 + Math.random()) * 200));
                value.put("Type", String.valueOf(2));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 5;
                value = new HashMap<String, String>();
                value.put("Name", "ElectricCooker");
                value.put("Power", String.valueOf((2 + Math.random()) * 200));
                value.put("Type", String.valueOf(2));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 6;
                value = new HashMap<String, String>();
                value.put("Name", "Television");
                value.put("Power", String.valueOf((1 + Math.random()) * 100));
                value.put("Type", String.valueOf(3));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 7;
                value = new HashMap<String, String>();
                value.put("Name", "Computer");
                value.put("Power", String.valueOf((1 + Math.random()) * 300));
                value.put("Type", String.valueOf(3));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);

                key = position + 8;
                value = new HashMap<String, String>();
                value.put("Name", "Lamp");
                value.put("Power", String.valueOf((1 + Math.random()) * 50));
                value.put("Type", String.valueOf(3));
                value.put("Frequency", String.valueOf(50));
                value.put("Voltage", String.valueOf(220));
                value.put("Status", "OFF");
                ds.addData(key, value);
            }
        }
    
        // Write data to file
        ds.writeDataToFile(fileName);
    }
}
