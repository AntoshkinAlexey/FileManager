import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    private HashMap<String, ArrayList<String>> edges;
    private File root;

    Graph(File root) {
        edges = new HashMap<>();
        this.root = root;
        GetEdges(root);
    }

    private void GetEdges(File vertex) {
        File[] files = vertex.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    GetEdges(file);
                } else {
                    ArrayList<String> text = FileHandler.ReadFile(file);
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
                        var file = new File(root.getAbsolutePath() + File.pathSeparator + filePath);
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

    class TopSort {
        private HashMap<String, Integer> state = new HashMap<String, Integer>();
        private ArrayList<String> sortedFiles = new ArrayList<>();
        public boolean isCycle = false;

        void DFS(String fileName) {
            state.put(fileName, 1);
            for (var ancestorFile : edges.get(fileName)) {
                if (state.get(ancestorFile) == 0) {
                    DFS(ancestorFile);
                } else if (state.get(ancestorFile) == 1) {
                    isCycle = true;
                    System.out.println("Cycle was found: " + fileName + " " + ancestorFile);
                }
            }

            sortedFiles.add(fileName);
        }

        public ArrayList<String> GetSorted() {
            for (var fileName : edges.keySet()) {
                state.put(fileName, 0);
            }
            for (var fileName : edges.keySet()) {
                if (state.get(fileName) == 0) {
                    DFS(fileName);
                }
            }
            if (isCycle) {
                return null;
            }
            return sortedFiles;
        }
    }
}
