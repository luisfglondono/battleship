package com.example.battleship.models.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class fileCRUD {

    private final String filePath;

    public fileCRUD(String filePath) {
        this.filePath = filePath;
    }

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

