package com.admission.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling CSV file operations.
 * <p>
 * Assumptions:
 * - Commas inside data values are NOT allowed, as we do not implement a full CSV parser with quotes.
 * - Files include a header row which is skipped when reading.
 * - Missing files are created automatically.
 * </p>
 */
public class CsvUtil {

    private CsvUtil() {
    }

    public static List<String[]> readAll(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip header
            if (line == null) {
                return data;
            }
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                data.add(values);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
        return data;
    }

    public static void writeAll(String filePath, String[] headers, List<String[]> data) {
        Path target = Path.of(filePath);
        Path temp = Path.of(filePath + ".tmp");
        try (BufferedWriter bw = Files.newBufferedWriter(temp, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write(String.join(",", headers));
            bw.newLine();
            for (String[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != null && row[i].contains(",")) {
                        throw new IllegalArgumentException("Commas are not allowed in input values: " + row[i]);
                    }
                }
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to CSV file: " + filePath, e);
        }
        try {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to atomically replace CSV file: " + filePath, e);
        }
    }

    public static void append(String filePath, String[] data) {
        Path target = Path.of(filePath);
        try {
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null && data[i].contains(",")) {
                    throw new IllegalArgumentException("Commas are not allowed in input values: " + data[i]);
                }
            }
            String row = String.join(",", data) + System.lineSeparator();
            Files.writeString(target, row, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to append to CSV file: " + filePath, e);
        }
    }

    public static void ensureFile(String filePath, String[] headers) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            Path target = file.toPath();
            try (BufferedWriter bw = Files.newBufferedWriter(target, StandardOpenOption.CREATE_NEW)) {
                bw.write(String.join(",", headers));
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create CSV file: " + filePath, e);
            }
        }
    }
}
