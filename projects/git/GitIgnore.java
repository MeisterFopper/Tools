package projects.git;

import java.io.*;
import java.util.*;

/**
 * Helper class for managing the .gitignore file.
 * This class provides methods to load, modify, and save .gitignore entries with associated comments.
 */
public class GitIgnore {
    private final File gitIgnoreFile;
    private Map<String, String> patternToCommentsMap;

    /**
     * Constructs a GitIgnoreHelper instance for the specified .gitignore file.
     *
     * @param gitIgnoreFile the .gitignore file to manage
     */
    public GitIgnore(File gitIgnoreFile) {
        this.gitIgnoreFile = gitIgnoreFile;
        this.patternToCommentsMap = new LinkedHashMap<>();
    }
    
    /**
     * Checks if the .gitignore file exists.
     *
     * @throws IllegalArgumentException if the .gitignore file does not exist.
     */
    private void checkFileExists() {
        if (!this.gitIgnoreFile.exists()) {
            throw new IllegalArgumentException(".gitignore-Datei existiert nicht.");
        }
    }

    /**
     * Loads the .gitignore file into the internal data structure.
     *
     * @throws IllegalArgumentException if the .gitignore file does not exist
     * @throws IOException if an error occurs while reading the file
     */
    public void loadGitIgnore() throws IllegalArgumentException, IOException {
        checkFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(this.gitIgnoreFile))) {
            String currentComment = "";
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    // Entfernt Muster wie # leerzeichen oder leerzeichen # sowie #
                    currentComment = line.replaceAll("(^#\\s*)|(\\s*#$)", "").trim();
                } else if (!line.isEmpty()) {
                    // Muster hinzufügen und zugehörigen Kommentar speichern
                    patternToCommentsMap.put(line, currentComment);
                }
            }
        }
    }

    /**
     * Returns all entries in the .gitignore as a map.
     *
     * @return a map containing patterns as keys and their associated comments as values
     */
    public Map<String, String> getGitIgnoreEntries() {
        return this.patternToCommentsMap;
    }

    /**
     * Sets the gitignore entries with the provided map of patterns and their associated comments.
     *
     * @param patternToCommentsMap a map containing the patterns as keys and their associated comments as values
     */
    public void setGitIgnoreEntries(Map<String, String> patternToCommentsMap) {
        this.patternToCommentsMap = patternToCommentsMap;
    }

    /**
     * Adds a pattern with an associated comment to the .gitignore.
     *
     * @param pattern the pattern to be added
     * @param comment the associated comment for the pattern
     */
    public void addPattern(String pattern, String comment) {
        if (pattern == null || pattern.isEmpty()) {
            return;
        }

        String newComment = comment;
        if (newComment == null) {
            newComment = "";
        }

        if (!patternToCommentsMap.containsKey(pattern)) {
            patternToCommentsMap.put(pattern, newComment);
        } else {
            throw new IllegalArgumentException("Eintrag existiert bereits: " + pattern);
        }
    }

    /**
     * Removes a pattern from the .gitignore.
     *
     * @param pattern the pattern to be removed
     */
    public void removePattern(String pattern) {
        if (patternToCommentsMap.remove(pattern) != null) {
            System.out.println("Muster entfernt: " + pattern);
        } else {
            System.out.println("Muster nicht gefunden: " + pattern);
        }
    }

    /**
     * Saves the .gitignore file based on the internal data structure.
     * If a comment is the same as the last one written, it will not be repeated.
     *
     * @throws IOException if an error occurs while writing to the .gitignore file
     */
    public void saveGitIgnore() throws IOException {
        checkFileExists();

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.gitIgnoreFile));
        String lastComment = "";  // Variable zum Speichern des letzten Kommentars
        try {
            for (Map.Entry<String, String> entry : patternToCommentsMap.entrySet()) {
                String comment = entry.getValue();
                if (!comment.isEmpty() && !comment.equals(lastComment)) {
                    writer.newLine();
                    writer.write(comment);
                    writer.newLine();
                    lastComment = comment;  // Aktualisiert den letzten Kommentar
                }
                writer.write(entry.getKey());
                writer.newLine();
            }
        } finally {
            writer.close(); // Stelle sicher, dass der Writer immer geschlossen wird
        }
    }

}
