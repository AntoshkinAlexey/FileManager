package FileProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
    /**
     * Хранение списка смежности графа.
     */
    private final HashMap<String, ArrayList<String>> edges;

    /**
     * Корневая папка.
     */
    private final File root;

    /**
     * Создание графа.
     * @param root Корневая папка.
     */
    Graph(File root) {
        edges = new HashMap<>();
        this.root = root;
        getEdges(root);
    }

    /**
     * Получение всех рёбер графа.
     * @param vertex Текущая вершина графа (файл).
     */
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

    /**
     * Поиск рёбер в тексте.
     * @param text Текст, в котором нужно найти рёбра.
     * @return Массив рёбер.
     */
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

    /**
     * Класс, реализующий топологическую сортировку графа.
     */
    class TopSort {
        /**
         * Хранение состояния вершины:
         * 0 - не обработана
         * 1 - начата обработка
         * 2 - обработка закончена
         */
        private final HashMap<String, Integer> state = new HashMap<>();

        /**
         * Массив, храящий топологическую сортировку графа.
         */
        private final ArrayList<String> sortedFiles = new ArrayList<>();

        /**
         * Флаг, отвечающий за наличие циклов в графе.
         * True, если есть цикл, false иначе.
         */
        public boolean isCycle = false;

        /**
         * Обход графа в глубину.
         * @param fileName Название файла.
         */
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

        /**
         * Получение топологической сортировки графа.
         * @return Массив названий файлов, которые отсортированы в топологическом порядке.
         */
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
