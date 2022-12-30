import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private final File root;

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

    public static ArrayList<String> ReadFile(File file) {
        try (FileInputStream fin = new FileInputStream(file.getAbsolutePath())) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
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

    public void ProcessFiles() {
        Graph graph = new Graph(root);
        ArrayList<String> sortedFiles = graph.new TopSort().GetSorted();
        if (sortedFiles == null) {
            System.out.println("The graph contains cycles, concatenation is not possible.");
            return;
        }
        for (var fileName : sortedFiles) {
            System.out.println(ReadFile(new File(fileName)));
        }
    }
}
