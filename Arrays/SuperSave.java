package Arrays;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SuperSave {

    protected String csvFile;

    public SuperSave(String csvFile) {
        this.csvFile = "Arrays/" + csvFile;
    }

    protected void writeToCSV(String[] itemsToSave, int index) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFile), StandardCharsets.UTF_8);

            if (index == -1) {
                lines.add(String.join(",", itemsToSave));
            } 
            else {

                while (lines.size() <= index) {
                    lines.add("");
                }
                lines.set(index, String.join(",", itemsToSave));
            }

            Files.write(Paths.get(csvFile), lines, StandardCharsets.UTF_8);

        } 
        catch (IOException e) {
            System.out.println("Error writing data to CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
