package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TrainScheduleData {
    public static void main(String[] args) throws Exception {
        final int N = 1000, M = 5;
        String[] cities = new String[] { "Harbin", "Beijing", "Shanghai", "Shenzhen", "Guangzhou" };
        String content = "";
        for (int i = 1; i <= N; i++) {
            String planningDate = "2020-01-01";
            String planningNumber = String.valueOf(i);
            String trainNumber = "A" + String.valueOf((int) (Math.random() * 1000));
            String trainType = "Business";
            String trainCapacity = String.valueOf(100);
            String stations = "";
            for (int j = 1; j <= M; j++) {
                stations += cities[j - 1] + " " + String.format("2020-01-01 10:%d", j * 10) + " "
                        + String.format("2020-01-01 10:%d", j * 10) + "\n";
            }
            content += String.format("Train:%s,%s\n{\n%sTrain:%s\n{\nTrainType:%s\nTrainCapacity:%s\n}\n}\n",
                    planningDate, planningNumber, stations, trainNumber, trainType, trainCapacity);
        }
        try {
            File file = new File("data/TrainSchedule/TrainSchedule_1.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}