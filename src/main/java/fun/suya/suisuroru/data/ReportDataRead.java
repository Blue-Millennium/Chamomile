package fun.suya.suisuroru.data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/29 14:30
 * function: Read data of report
 */
public class ReportDataRead {

    public static final File BASE_DIR = new File("BasePlugin");
    public static final File REPORT_DATA_FILE = new File(BASE_DIR, "report.csv");

    public static List<List<String>> ReadReportFile() {
        ReportDataRead reader = new ReportDataRead();
        if (!REPORT_DATA_FILE.exists()) {
            try {
                if (!REPORT_DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file: " + exception.getMessage());
            }
        }
        return reader.readCsvToList();
    }

    List<List<String>> readCsvToList() {
        List<List<String>> allRows = new ArrayList<>();
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
                    ReportDataWrite reportDataWrite = new ReportDataWrite();
                    reportDataWrite.writeNewData(defaultRows);
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file: " + exception.getMessage());
            }
        }

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
