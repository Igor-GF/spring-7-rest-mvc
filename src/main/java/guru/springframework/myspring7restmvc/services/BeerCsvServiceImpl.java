package guru.springframework.myspring7restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import guru.springframework.myspring7restmvc.model.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertCSV(File csvFile) {

        try {
            return new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
