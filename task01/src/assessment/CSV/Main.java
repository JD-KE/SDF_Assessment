package assessment.CSV;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.err.println("Error, no file provided");
            System.exit(1);
        } else {
            System.out.println(args[0]);
        }

        Map<String,List<String[]>> categories;
        long totalLinesRead = 0;

        try (FileReader fr = new FileReader(args[0].trim())) {
            BufferedReader br = new BufferedReader(fr);

            categories = br.lines()
                .skip(1)
                .map(line -> line.trim().split(","))
                .map(fields -> {
                    return Arrays.copyOfRange(fields,0,3);
                })
                .collect(Collectors.groupingBy(field -> field[1].toUpperCase()))
                ;
            
        }

        for (String catagory : categories.keySet()) {
            List<String[]> apps = categories.get(catagory);
            int numTotalApps = apps.size();
            totalLinesRead += numTotalApps;
            int numDisRec = 0;
            double sumRating = 0.0;
            String highestName = "";
            double highestRating = 0.0;
            String lowestName = "";
            double lowestRating = 0.0;
            boolean first = true;

            for (String[] app : apps) {
                // System.out.printf("%s, %s, %s%n", app[0], app[1], app[2]);
                if (!"NaN".equals(app[2].trim())) {
                    String name = app[0].trim();
                    double rating = Double.parseDouble(app[2].trim());
                    sumRating += rating;
                    if (first) {
                        highestName = lowestName = name;
                        highestRating = lowestRating = rating;
                        first = false;
                    } else if (rating > highestRating) {
                        highestRating = rating;
                        highestName = name;
                    } else if (rating < lowestRating) {
                        lowestRating = rating;
                        lowestName = name;
                    }
                } else {
                    numDisRec++;
                }
            }

            double averageRating = sumRating / (numTotalApps - numDisRec);

            //all double values fixed to 3 digits total, 2 d.p.

            System.out.printf("Category: %s%n", catagory);
            System.out.printf("\tHighest: %s, %3.2f%n", highestName, highestRating);
            System.out.printf("\tLowest: %s, %3.2f%n", lowestName, lowestRating);
            System.out.printf("\tAverage: %3.2f%n", averageRating);
            System.out.printf("\tCount: %d%n", numTotalApps);
            System.out.printf("\tDiscarded: %d%n", numDisRec);
            System.out.println("");
            
        }

        // should be 10841 as header is skipped
        System.out.printf("Total lines in file: %d%n",totalLinesRead);
    }
}