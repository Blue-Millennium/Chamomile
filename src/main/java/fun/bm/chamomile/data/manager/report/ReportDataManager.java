package fun.bm.chamomile.data.manager.report;

import fun.bm.chamomile.util.Environment;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fun.bm.chamomile.data.processor.data.DataStringBuilder.transformTime;
import static fun.bm.chamomile.util.Environment.LOGGER;
import static fun.bm.chamomile.util.TimeUtil.getUnixTimeS;

/**
 * @author Suisuroru
 * Date: 2024/9/29 16:02
 * function: Manage report data
 */
public class ReportDataManager {
    final File reportDataFile = new File(Environment.BASE_DIR, "report.csv");

    public boolean deleteData(String timestamp) {
        List<List<String>> reportData = ReadReportFile();
        try {
            reportData.removeIf(row -> row.get(0).equals(timestamp));
            saveToCsv(reportData);
            return true;
        } catch (Exception e) {
            LOGGER.warning("Failed to delete data from CSV file: " + e.getMessage());
            return false;
        }
    }

    /**
     * 接收列表并将数据追加到CSV文件中。
     *
     * @param data 新的数据行
     */
    public void appendDataToCsv(List<String> data) {
        writeNewData(data);
    }

    public void ProcessData(@NotNull CommandSender sender, @NotNull String[] args) {
        List<String> ProcessData = new ArrayList<>();

        long time = getUnixTimeS();
        ProcessData.add(String.valueOf(time));
        ProcessData.add(transformTime(time));
        ProcessData.add(sender.getName());

        // 提取被举报人的名字和举报原因
        if (args.length > 0) {
            String reportedPlayerName = args[0];
            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }
            ProcessData.add(reportedPlayerName);
            ProcessData.add(reason.toString().trim());
        }

        appendDataToCsv(ProcessData);
    }

    public List<List<String>> ReadReportFile() {
        writeData();
        return readCsvToList();
    }

    private void writeData() {
        if (!reportDataFile.exists()) {
            try {
                List<String> defaultRows = new ArrayList<>();
                defaultRows.add("Unix时间戳");
                defaultRows.add("举报时间");
                defaultRows.add("举报人");
                defaultRows.add("被举报人");
                defaultRows.add("举报理由");
                reportDataFile.createNewFile();
                writeNewData(defaultRows);
            } catch (Exception exception) {
                LOGGER.warning("Failed to create data file: " + exception.getMessage());
            }
        }
    }

    public void writeNewData(List<String> newData) {
        List<List<String>> existingData = readCsvToList();
        existingData.add(newData);
        saveToCsv(existingData);
    }

    private void saveToCsv(List<List<String>> data) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportDataFile), Charset.forName("GBK")))) {
            for (List<String> row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            LOGGER.warning("Failed to write to CSV file: " + e.getMessage());
        }
    }

    private List<List<String>> readCsvToList() {
        List<List<String>> allRows = new ArrayList<>();
        writeData();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(reportDataFile), Charset.forName("GBK")))) {
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
