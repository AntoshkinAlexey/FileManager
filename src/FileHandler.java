import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class FileHandler {
    private File root;

    public FileHandler(String fileName) {
        root = new File(fileName);
    }

    public boolean CheckRoot() {
        try {
            if (!root.exists()) {
                System.out.print("Такого пути не существует.");
                return false;
            } else if (!root.isDirectory()) {
                System.out.print("Необходимо ввести путь до папки, а не до файла.");
                return false;
            }
        } catch (SecurityException error) {
            System.out.println("У программы нет доступа к папке по данному пути.");
            return false;
        }
        return true;
    }
}
