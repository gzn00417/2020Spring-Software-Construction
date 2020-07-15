package data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ActivityCalendarData {
    public static void main(String[] args) throws Exception {
        final int N = 1000;
        String content = "";
        for (int i = 1; i <= N; i++) {
            String planningDate = String.format("2020-04-0%d", i % 9 + 1);
            String activityNumber = String.valueOf(i);
            String room = "Zhengxin44";
            String beginningTime = String.format("2020-04-0%d 10:00", i % 9 + 1);
            String endingTime = String.format("2020-04-0%d 12:00", i % 9 + 1);
            String docName = "Software Construction";
            String publishDepartment = "HIT";
            String publishDate = "2000-01-01";
            content += String.format(
                    "Activity:%s,%s\n{\nRoom:%s\nBeginningTime:%s\nEndingTime:%s\nDocument:A4\n{\nDocName:%s\nPublishDepartment:%s\nPublishDate:%s\n}\n}\n",
                    planningDate, activityNumber, room, beginningTime, endingTime, docName, publishDepartment,
                    publishDate);
        }
        try {
            File file = new File("./data/ActivityCalendar/ActivityCalendar_1.txt");
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