import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Graph {
    private HashMap<String, ArrayList<String>> edges;

    Graph(File root) {
        edges = new HashMap<>();
        GetEdges(root);
    }

    private void GetEdges(File vertex) {
        File[] files = vertex.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    GetEdges(file);
                } else {
                    ArrayList<String> text = ReadFile(file);
                    if (text != null) {
                        edges.put(file.getAbsolutePath(), ParseEdges(text));
                    }
                }
            }
        }
    }

    private ArrayList<String> ParseEdges(ArrayList<String> text) {
        ArrayList<String> files = new ArrayList<>();
        for (String line : text) {
            int position = 0;
            while ((position = line.indexOf("require '", position)) != -1) {
                position += "require '".length();
                int lastPosition = line.indexOf("'", position);
                if (lastPosition != -1) {
                    try {
                        String filePath = line.substring(position, lastPosition);
                        var file = new File(filePath);
                        if (file.exists()) {
                            files.add(file.getAbsolutePath());
                        }
                    } catch (IndexOutOfBoundsException error) {
                        System.out.println("Something went wrong while processing the file's text.");
                    }
                    position = lastPosition + 1;
                }
            }
        }
        return files;
    }

    private ArrayList<String> ReadFile(File file) {
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

    ArrayList<File> TopSort() {
        return new ArrayList<>();
    }
}
