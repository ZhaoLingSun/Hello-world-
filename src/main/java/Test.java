public class Test {
    public static void main(String[] args) {
        DataStorage ds = new DataStorage();
        ds.readDataFromFile("data.txt");
        ds.showData();
    }
}
