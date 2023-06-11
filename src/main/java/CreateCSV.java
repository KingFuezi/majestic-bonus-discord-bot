import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CreateCSV {

    public static File writeDataLineByLine(String filePath) {

        File file = new File(filePath);
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile, ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);


            String[] header = {"staticId","amount","comment"};
            writer.writeNext(header);

            Variables.persons.forEach((key, value) -> {
                if (value.contains("|")) {
                    String[] split = value.split("\\|");
                    writer.writeNext(new String[]{split[1].strip(), Variables.payment, Variables.comment});
                } else if (value.equals(".Sniker")) {
                    writer.writeNext(new String[]{"5177", Variables.payment, Variables.comment});
                }
            });
            writer.close();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
