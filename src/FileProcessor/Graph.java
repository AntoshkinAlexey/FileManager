package FileProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
    private final HashMap<String, ArrayList<String>> edges;
    private final File root;

    Graph(File root) {
        edges = new HashMap<>();
        this.root = root;
        getEdges(root);
    }

    private void getEdges(File vertex) {
        File[] files = vertex.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getEdges(file);
                } else {
                    ArrayList<String> text = FileHandler.readFile(file);
                    if (text != null) {
                        edges.put(file.getAbsolutePath(), parseEdges(text));
                    }
                }
            }
        }
    }

    private ArrayList<String> parseEdges(ArrayList<String> text) {
        ArrayList<String> files = new ArrayList<>();
        for (String line : text) {
            int position = 0;
            while ((position = line.indexOf("require '", position)) != -1) {
                position += "require '".length();
                int lastPosition = line.indexOf("'", position);
                if (lastPosition != -1) {
                    try {
                        String filePath = line.substring(position, lastPosition);
                        var file = new File(root.getAbsolutePath() + File.separator + filePath);
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
        private final HashMap<String, Integer> state = new HashMap<>();
        private final ArrayList<String> sortedFiles = new ArrayList<>();
        public boolean isCycle = false;

        void dfs(String fileName) {
            state.put(fileName, 1);
            for (var ancestorFile : edges.get(fileName)) {
                if (state.get(ancestorFile) == 0) {
                    dfs(ancestorFile);
                } else if (state.get(ancestorFile) == 1) {
                    isCycle = true;
                    System.out.println("Cycle was found: <" + fileName + "> and <" + ancestorFile + ">");
                }
            }
            sortedFiles.add(fileName);
            state.put(fileName, 2);
        }

        public ArrayList<String> getSorted() {
            for (var fileName : edges.keySet()) {
                state.put(fileName, 0);
            }
            for (var fileName : edges.keySet()) {
                if (state.get(fileName) == 0) {
                    dfs(fileName);
                }
            }
            if (isCycle) {
                return null;
            }
            Collections.reverse(sortedFiles);
            return sortedFiles;
        }
    }
}
