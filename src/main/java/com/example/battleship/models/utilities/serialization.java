package com.example.battleship.models.utilities;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class serialization {

    private static Path relativePath = Paths.get("objectsSerialization.txt");
    public static String getRelativePath() {return relativePath.toString();}

    public serialization() {
    }

    public static void serializeObjects(String fileName, Object... objects) {
        String path = getRelativePath();
        File file = new File(path);

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Object obj : objects) {
                oos.writeObject(obj);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Object> deserializeObjects(String fileName) {
        List<Object> objects = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    objects.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return objects;
    }
    public static void clearFile(String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {

        }
    }
}
