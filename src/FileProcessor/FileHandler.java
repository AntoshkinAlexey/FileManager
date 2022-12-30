package FileProcessor;

import java.io.*;
import java.util.ArrayList;

/**
 * Обработка файлов.
 */
public class FileHandler {
    /**
     * Корневая папка.
     */
    private final File root;

    /**
     * Конструктор обработчика директорий.
     * @param rootPath Путь до корневой папки.
     */
    public FileHandler(String rootPath) {
        root = new File(rootPath);
    }

    /**
     * Проверка корневой папки на корректность.
     * @return true, если корневая папка существует и до неё есть доступ, false иначе.
     */
    public boolean checkRoot() {
        try {
            if (!root.exists()) {
                System.out.println("There is no such path.");
                return false;
            } else if (!root.isDirectory()) {
                System.out.println("You must enter the path to the folder, not to the file.");
                return false;
            }
        } catch (SecurityException error) {
            System.out.println("The program does not have access to the folder at this path.");
            return false;
        }
        return true;
    }


    /**
     * Чтение текста из файла.
     * @param file Файл, из которого нужно считать текст.
     * @return Массив строк текста, если получилось открыть файл для чтения, null иначе.
     */
    public static ArrayList<String> readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            ArrayList<String> text = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                text.add(line + System.lineSeparator());
            }
            return text;
        } catch (SecurityException error) {
            System.out.println("Unable to read file: " + file.getAbsolutePath());
        } catch (IOException error) {
            assert true;
        }
        return null;
    }

    /**
     * Конкатенация файлов в топологическом порядке обхода.
     */
    public void processFiles() {
        Graph graph = new Graph(root);
        ArrayList<String> sortedFiles = graph.new TopSort().getSorted();
        if (sortedFiles == null) {
            System.out.println("The graph contains cycles, concatenation is not possible.");
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            for (var fileName : sortedFiles) {
                var text = readFile(new File(fileName));
                if (text != null) {
                    for (var line : text) {
                        bw.write(line);
                    }
                }
            }
            System.out.println("\nSee the results in the output.txt\n");
        } catch (IOException ex) {
            System.out.println("Unable to open file for writing.");
        }
    }
}
