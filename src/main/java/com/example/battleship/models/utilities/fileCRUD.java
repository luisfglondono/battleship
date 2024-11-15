package com.example.battleship.models.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for performing CRUD operations on a file.
 */
public class fileCRUD {

    /**
     * Path to the file on which CRUD operations will be performed.
     */
    private final String filePath;

    /**
     * Constructs a new fileCRUD instance with the specified file path.
     *
     * @param filePath the path to the file
     */
    public fileCRUD(String filePath) {
        this.filePath = filePath;
    }
    /**
     * Creates a new line in the file with the specified content.
     *
     * @param content the content to be written to the file
     */
    public void create(String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.filePath, true));
            bw.write(content);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Reads all lines from the file.
     *
     * @return a list of strings, each representing a line in the file
     */
    public List<String> read() {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.filePath));
            String line;
            while ((line = br.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    /**
     * Updates a specific line in the file with the specified content.
     *
     * @param lineIndex the index of the line to be updated
     * @param content the new content for the specified line
     */
    public void update(int lineIndex, String content) {
        List<String> lines = this.read();
        if (lineIndex >=  0 && lineIndex < lines.size()) {
            lines.set(lineIndex, content);
            try {
                BufferedWriter bfWriter = new BufferedWriter(new FileWriter(this.filePath));
                for (String line : lines) {
                    bfWriter.write(line);
                    bfWriter.newLine();
                }
                bfWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid Line Index.");
        }
    }
    /**
     * Deletes a specific line from the file.
     *
     * @param lineIndex the index of the line to be deleted
     */
    public void delete(int lineIndex) {
        List<String> lines = this.read();
        if (lineIndex >=  0 && lineIndex < lines.size()) {
            lines.remove(lineIndex);
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(this.filePath));
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid Line Index.");
        }
    }
}

