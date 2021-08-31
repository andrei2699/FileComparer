import Comparators.CSVFileComparator;
import Comparators.ContentFileComparator;
import Comparators.FileComparator;
import Comparators.JSONFileComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage <file_type> <path1> <path2>");
            System.exit(1);
        }

        try {
            List<String> text1 = Files.readAllLines(Paths.get(args[1]));
            List<String> text2 = Files.readAllLines(Paths.get(args[2]));

            FileComparator fileComparator;

            switch (args[0]) {
                case "csv" -> fileComparator = new CSVFileComparator();
                case "json" -> fileComparator = new JSONFileComparator();
                default -> fileComparator = new ContentFileComparator();
            }

            boolean compare = fileComparator.compare(text1, text2);
            if (!compare) {
                System.err.println("Files " + args[1] + ", " + args[2] + " are not equal!");
                System.exit(2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
