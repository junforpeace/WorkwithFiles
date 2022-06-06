package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipParsedTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test

    void parsedZipTest() throws Exception {
        try(InputStream is = classLoader.getResourceAsStream("Files/filetest.zip");
            ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("file-sample_150kB.pdf")) {
                    PDF pdf = new PDF(zis);
                }
                else if (entry.getName().equals("file_example_CSV_5000.csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(0)).contains("First Name","Last Name","Gender","Country","Age","Date","Id");

                }
                else if (entry.getName().equals("file_example_XLSX_10.xlsx")) {
                    XLS xls = new XLS(zis);
                    assertThat(xls.excel
                            .getSheetAt(0)
                            .getRow(0)
                            .getCell(1)
                            .getStringCellValue().contains("First Name"));
                }
            }

        }
    }

}
