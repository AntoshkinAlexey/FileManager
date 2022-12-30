import FileProcessor.FileHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to File Manager!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter the path to the root folder you want to process: ");
            try {
                String fileName = scanner.nextLine();
                var fileHandler = new FileHandler(fileName);
                if (!fileHandler.checkRoot()) {
                    System.out.println("Try again.");
                    continue;
                }
                fileHandler.processFiles();
                System.out.println("The END...");
                break;
            } catch (Exception error) {
                System.out.println("An error occurred while typing, please try again.");
            }
        }
    }
}