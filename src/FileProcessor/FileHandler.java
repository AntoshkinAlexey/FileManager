package FileProcessor;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private final File root;

    public FileHandler(String fileName) {
        root = new File(fileName);
    }

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
