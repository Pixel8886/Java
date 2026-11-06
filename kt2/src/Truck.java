public class Truck extends Vehicle implements Insurable {

    private double capacityTons;

    public Truck(String plateNumber, double dailyRate, double capacityTons) {
        super(plateNumber, dailyRate);
        this.capacityTons = capacityTons;
    }

    @Override
    public double calculateRentalCost(int days) {
        return dailyRate * days + 500 * days;
    }

    @Override
    public double getInsuranceFee() {
        return dailyRate * 0.08;
    }
}
