public class Main {

    public static double totalBonusBudget(Employee[] staff) {
        double total = 0;
        for (Employee item : staff) {
            total += item.calculateBonus();
        }
        return total;
    }

    public static void main(String[] args) {
        Employee[] staff = new Employee[4];
        staff[0] = new Employee("Иванов И.И.", "Разработчик", 90000);
        staff[1] = new Employee("Петрова А.С.", "Тестировщик", 70000);
        staff[2] = new Manager("Сидоров П.П.", "Руководитель отдела", 120000, 5);
        staff[3] = new Manager("Козлова Е.В.", "Тимлид", 100000, 3);

        System.out.println("=== Все сотрудники и их премии ===");
        for (Employee item : staff) {
            System.out.println(item);
            System.out.println("Премия: " + item.calculateBonus());
        }

        System.out.println();
        System.out.println("=== Менеджеры ===");
        int managerCount = 0;
        for (Employee item : staff) {
            if (item instanceof Manager) {
                Manager m = (Manager) item;
                managerCount++;
                System.out.println(m.getName() + ", размер команды: " + m.getTeamSize());
            }
        }
        System.out.println("Всего менеджеров: " + managerCount);
        System.out.println();
        System.out.println("Создано сотрудников: " + Employee.getEmployeeCount());
        System.out.println("Общий бюджет на премии: " + totalBonusBudget(staff));
    }
}
