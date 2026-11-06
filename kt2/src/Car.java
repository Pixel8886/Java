public class Car extends Vehicle implements Insurable {

    private int seats;

    public Car(String plateNumber, double dailyRate, int seats) {
        super(plateNumber, dailyRate);
        this.seats = seats;
    }


    @Override
    public double calculateRentalCost(int days) {
        double cost = dailyRate * days;
        if (days > 7) {
            cost = cost * 0.9;
        }
        return cost;
    }

    @Override
    public double getInsuranceFee() {
        return dailyRate * 0.05;
    }
}
