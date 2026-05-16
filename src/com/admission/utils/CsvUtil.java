package com.admission.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
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
    }

    public static void append(String filePath, String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] != null && data[i].contains(",")) {
                    throw new IllegalArgumentException("Commas are not allowed in input values: " + data[i]);
                }
            }
            bw.write(String.join(",", data));
            bw.newLine();
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                bw.write(String.join(",", headers));
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create CSV file: " + filePath, e);
            }
        }
    }
}
