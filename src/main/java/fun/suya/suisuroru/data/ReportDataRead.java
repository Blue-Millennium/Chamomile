package fun.suya.suisuroru.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    List<List<String>> readCsvToList() {
        List<List<String>> allRows = new ArrayList<>();
        if (!REPORT_DATA_FILE.exists()) {
            try {
                if (!REPORT_DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                } else {
                    List<String> defaulyRows =  new ArrayList<>();
                    defaulyRows.add("时间戳");
                    defaulyRows.add("举报人");
                    defaulyRows.add("被举报人");
                    defaulyRows.add("举报理由");
                    ReportDataWrite reportDataWrite = new ReportDataWrite();
                    reportDataWrite.writeNewData(defaulyRows);
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file " + exception.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(REPORT_DATA_FILE))) {
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

    public static List<List<String>> ReadReportFile() {
        ReportDataRead reader = new ReportDataRead();
        if (!REPORT_DATA_FILE.exists()) {
            try {
                if (!REPORT_DATA_FILE.createNewFile()) {
                    LOGGER.warning("Failed to create data file");
                }
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file " + exception.getMessage());
            }
        }
        return reader.readCsvToList();
    }
}
