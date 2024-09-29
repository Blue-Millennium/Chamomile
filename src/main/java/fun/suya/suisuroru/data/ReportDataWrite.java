package fun.suya.suisuroru.data;

import java.io.*;
import java.util.List;

import static xd.suka.Main.LOGGER;

/**
 * @author Suisuroru
 * Date: 2024/9/29 16:02
 * function: Save data of report
 */
public class ReportDataWrite {

    public static final File BASE_DIR = new File("BasePlugin");
    public static final File REPORT_DATA_FILE = new File(BASE_DIR, "report.csv");

    /**
     * 读取现有CSV文件的内容，追加新信息，并保存回CSV文件。
     * @param newData 新的数据行
     */
    public void writeNewData(List<String> newData) {
        ReportDataRead reader = new ReportDataRead();
        // 读取现有CSV文件的内容
        List<List<String>> existingData = reader.readCsvToList();

        // 追加新信息
        existingData.add(newData);

        // 保存修改后的内容回CSV文件
        saveToCsv(existingData);
    }


    private void saveToCsv(List<List<String>> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_DATA_FILE))) {
            for (List<String> row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to write to CSV file: " + e.getMessage());
        }
    }
}
