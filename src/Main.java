import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в файловый менеджер!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите путь до корневой папки, которую хотите обработать: ");
            try {
                String fileName = scanner.nextLine();
                var fileHandler = new FileHandler(fileName);
                if (fileHandler.CheckRoot()) {
                    System.out.println("Попробуйте ещё раз.");
                    continue;
                }
            } catch (Exception error) {
                System.out.println("При вводе произошла ошибка, попробуйте ещё раз.");
            }
        }
    }
}