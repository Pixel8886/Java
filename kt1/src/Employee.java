public class Employee {

    private String name;
    private String position;
    private double salary;

    private static int employeeCount = 0;

    public Employee(String name, String position, double salary) {
        this.name = name;
        this.position = position;
        if (salary >= 0) {
            this.salary = salary;
        } else {
            this.salary = 0;
        }

        employeeCount++;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary;
        } else {
            this.salary = 0;
        }
    }

    public static int getEmployeeCount() {
        return employeeCount;
    }

    public double calculateBonus() {
        return salary * 0.1;
    }

    @Override
    public String toString() {
        return name + " — " + position + ", оклад " + (int) salary;
    }
}
