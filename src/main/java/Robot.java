public class Robot {
    private double batteryCapacity; // 电池容量
    private double currentCharge;   // 当前电量
    private double chargingRate;    // 充电速率（每小时）
    private double dischargingRate; // 放电速率（每小时）

    public Robot(double batteryCapacity, double chargingRate, double dischargingRate) {
        this.batteryCapacity = batteryCapacity;
        this.chargingRate = chargingRate;
        this.dischargingRate = dischargingRate;
        this.currentCharge = batteryCapacity; // 初始时为满电状态
    }

    public double charge() {
        if (currentCharge + chargingRate <= batteryCapacity) {
            currentCharge += chargingRate;
            return chargingRate;
        } else {
            double actualCharge = batteryCapacity - currentCharge;
            currentCharge = batteryCapacity;
            return actualCharge;
        }
    }

    public double discharge() {
        if (currentCharge - dischargingRate >= 0) {
            currentCharge -= dischargingRate;
            return dischargingRate;
        } else {
            double actualDischarge = currentCharge;
            currentCharge = 0;
            return actualDischarge;
        }
    }

    public double getCurrentCharge() {
        return currentCharge;
    }

}
