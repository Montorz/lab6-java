import java.util.ArrayList;
import java.util.List;

class InvalidBalanceException extends RuntimeException {
    public InvalidBalanceException() {
        super("Недопустимый баланс!");
    }
}

// Интерфейс для объектов, которые могут принимать ставки
interface Bettable {
    void placeBet(int amount);
}

// Абстрактный класс для слотов
abstract class Slot implements Bettable {
    protected String name;
    protected int maxBet;
    protected int minBet;

    public Slot(String name, int maxBet, int minBet) {
        this.name = name;
        this.maxBet = maxBet;
        this.minBet = minBet;
    }

    // Абстрактный метод для вывода информации о слоте
    public abstract void displaySlotInfo();

    // Реализация интерфейса Bettable
    public void placeBet(int amount) {
        if (amount < minBet || amount > maxBet) {
            System.out.println("Ставка должна быть между " + minBet + " и " + maxBet);
        } else {
            System.out.println("Ставка размещена на слот " + name + ": " + amount);
        }
    }
}

// Класс SlotGame - производный от Slot
class SlotGame extends Slot {
    public SlotGame(String name, int maxBet, int minBet) {
        super(name, maxBet, minBet);
    }

    @Override
    public void displaySlotInfo() {
        System.out.println("Слот: " + this.name + ", Макс. ставка: " + maxBet + ", Мин. ставка: " + minBet);
    }
}

// Класс User
class User {
    private int id;
    private String name;
    private float balance;

    // Статическое поле
    private static int userCount = 0;

    public User(int id, String name, float balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        userCount++;
    }

    public void displayUserInfo() {
        System.out.println("ID: " + this.id + ", Имя: " + this.name + ", Баланс: " + this.balance);
    }

    public static int getUserCount() {
        return userCount;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        if (balance < 0) {
            throw new InvalidBalanceException(); // Инициализация исключения
        }
        this.balance = balance;
    }
}

// Класс VIPUser - производный от User
class VIPUser extends User {
    private float cashbackRate;

    public VIPUser(int id, String name, float balance, float cashbackRate) {
        super(id, name, balance); // Вызов конструктора базового класса
        this.cashbackRate = cashbackRate;
    }

    // Перегрузка метода для отображения информации
    @Override
    public void displayUserInfo() {
        super.displayUserInfo(); // Вызов метода базового класса
        System.out.println("Кэшбэк: " + cashbackRate * 100 + "%");
    }

    public void addCashback() {
        float balance = this.getBalance();
        setBalance(balance + balance * cashbackRate);
    }
}

// Абстрактный класс GameSlot (для примера использования абстракции)
abstract class GameSlot {
    abstract void startGame();
}

// Класс для реализации абстракции
class BonusSlot extends GameSlot {
    @Override
    void startGame() {
        System.out.println("Запуск бонусного слота!");
    }
}

// Реализация клонирования
class ClonableUser extends User implements Cloneable {
    public ClonableUser(int id, String name, float balance) {
        super(id, name, balance);
    }

    @Override
    public ClonableUser clone() throws CloneNotSupportedException {
        return (ClonableUser) super.clone(); // Мелкое клонирование
    }
    
    // Глубокое клонирование
    public ClonableUser deepClone() throws CloneNotSupportedException {
        ClonableUser clone = (ClonableUser) super.clone();
        clone.setBalance(this.getBalance()); // Клонирование вложенных данных (баланса)
        return clone;
    }
}

public class Main {    
    public static void main(String[] args) {
    try {
        System.out.println("Работа с VIPUser:");
        VIPUser vipUser = new VIPUser(1, "Алексей", 1000.0f, 0.1f);
        vipUser.displayUserInfo();
        vipUser.addCashback();
        vipUser.displayUserInfo();

        System.out.println("\nИспользование абстрактного класса и интерфейсов:");
        Slot slot = new SlotGame("Lucky 7", 500, 50);
        slot.displaySlotInfo();
        slot.placeBet(100); // Использование интерфейса Bettable

        System.out.println("\nИспользование клонирования:");
        ClonableUser user1 = new ClonableUser(2, "Мария", 2000.0f);
        ClonableUser shallowClone = user1.clone(); // Мелкое клонирование
        ClonableUser deepClone = user1.deepClone(); // Глубокое клонирование

        System.out.println("Оригинал: ");
        user1.displayUserInfo();
        System.out.println("Мелкое клонирование: ");
        shallowClone.displayUserInfo();
        System.out.println("Глубокое клонирование: ");
        deepClone.displayUserInfo();

    } catch (CloneNotSupportedException e) {
        System.err.println("Ошибка клонирования: " + e.getMessage());
    }
}
}
