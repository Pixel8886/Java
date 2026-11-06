import java.util.Objects;


public abstract class Vehicle {

    protected String plateNumber;
    protected double dailyRate;

    public Vehicle(String plateNumber, double dailyRate) {
        this.plateNumber = plateNumber;
        this.dailyRate = dailyRate;
    }

    public abstract double calculateRentalCost(int days);

    public String getPlateNumber() {
        return plateNumber;
    }

    @Override
    public String toString() {
        return plateNumber + " — " + dailyRate + " ₽/сутки";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle other = (Vehicle) o;
        return Objects.equals(plateNumber, other.plateNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber);
    }
}
