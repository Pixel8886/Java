import java.util.*;

public class Main {
    public static void main(String[] args) {

        List<Vehicle> fleet = new ArrayList<>();
        fleet.add(new Car("А123БВ77", 2500.0, 5));
        fleet.add(new Car("В777ОР99", 3000.0, 4));
        fleet.add(new Motorcycle("М456КС50", 1500.0, 650.0));
        fleet.add(new Motorcycle("Т111ТТ77", 1800.0, 900.0));
        fleet.add(new Truck("Е555КХ78", 4000.0, 10.5));
        fleet.add(new Car("А123БВ77", 2600.0, 7));

        System.out.println("=== Автопарк ===");
        for (Vehicle v : fleet) {
            System.out.println(v);
            System.out.println("   аренда на 10 дней: " + v.calculateRentalCost(10) + " ₽");

            if (v instanceof Insurable) {
                System.out.println("   страховка: " + ((Insurable) v).getInsuranceFee() + " ₽");
            }
        }

        Set<String> uniquePlates = new HashSet<>();
        for (Vehicle v : fleet) {
            uniquePlates.add(v.getPlateNumber());
        }

        System.out.println("\n=== Уникальность номеров ===");
        System.out.println("fleet.size() = " + fleet.size()
                + ", uniquePlates.size() = " + uniquePlates.size());
        if (uniquePlates.size() != fleet.size()) {
            System.out.println("Есть дубликаты номеров!");
        } else {
            System.out.println("Дубликатов нет.");
        }

        Map<String, Vehicle> byPlate = new HashMap<>();
        for (Vehicle v : fleet) {
            byPlate.put(v.getPlateNumber(), v);
        }

        System.out.println("\nПоиск по номеру Е555КХ78: " + byPlate.get("Е555КХ78"));
        System.out.println("Поиск по номеру Я000ЯЯ00: " + byPlate.get("Я000ЯЯ00"));
        Set<Vehicle> set = new HashSet<>();
        set.add(new Car("К999КК99", 2000.0, 4));
        set.add(new Car("К999КК99", 9999.0, 2));
        System.out.println("\n=== equals/hashCode ===");
        System.out.println("set.size() = " + set.size() + " (ожидаем 1)");
        Queue<Vehicle> serviceQueue = new LinkedList<>();

        serviceQueue.offer(fleet.get(0));
        serviceQueue.offer(fleet.get(2));
        serviceQueue.offer(fleet.get(4));

        System.out.println("\n=== Очередь на обслуживание ===");
        while (!serviceQueue.isEmpty()) {
            Vehicle next = serviceQueue.poll();
            System.out.println("Обслуживается: " + next);
        }
    }
}
