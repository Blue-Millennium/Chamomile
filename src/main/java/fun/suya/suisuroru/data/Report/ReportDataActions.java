package fun.suya.suisuroru.data.Report;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fun.xd.suka.Main.LOGGER;
import static fun.xd.suka.Main.REPORT_DATA_FILE;

/**
 * @author Suisuroru
 * Date: 2024/9/29 14:30
 * function: Read data of report
 */
public class ReportDataActions {
    public static List<List<String>> ReadReportFile() {
        ReportDataActions reader = new ReportDataActions();
        EnsureFileExist();
        return reader.readCsvToList();
    }

    private static void EnsureFileExist() {
        if (!REPORT_DATA_FILE.exists()) {
            try {
                if (!REPORT_DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                } else {
                    List<String> defaultRows = new ArrayList<>();
                    defaultRows.add("时间戳");
                    defaultRows.add("举报人");
                    defaultRows.add("被举报人");
                    defaultRows.add("举报理由");
                    writeNewData(defaultRows);
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file: " + exception.getMessage());
            }
        }
    }

    public static void writeNewData(List<String> newData) {
        ReportDataActions reader = new ReportDataActions();
        List<List<String>> existingData = reader.readCsvToList();
        existingData.add(newData);
        saveToCsv(existingData);
    }

    private static void saveToCsv(List<List<String>> data) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(REPORT_DATA_FILE), StandardCharsets.UTF_8))) {
            for (List<String> row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to write to CSV file: " + e.getMessage());
        }
    }

    List<List<String>> readCsvToList() {
        List<List<String>> allRows = new ArrayList<>();
        EnsureFileExist();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(REPORT_DATA_FILE), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) { // 逐行读取
                String[] values = line.split(","); // 根据逗号分割
                List<String> row = new ArrayList<>();
                Collections.addAll(row, values);
                allRows.add(row);
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to read CSV file: " + e.getMessage());
        }

        return allRows;
    }
}
