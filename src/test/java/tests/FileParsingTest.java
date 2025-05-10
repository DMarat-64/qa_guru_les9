package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@DisplayName("Проверка наличия в zip папке файлов разного формата")
public class FileParsingTest {
    private  ClassLoader cl = FileParsingTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка наличия файлов в zip папке")
    void zipFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }

    }

    @Test
    @DisplayName("Проверка наличия в zip папке файла формата pdf")
    void pdfFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")){
                    PDF pdf = new PDF(zis);
                    Assertions.assertEquals(2, pdf.numberOfPages);
                }
            }
        }

    }

    @Test
    @DisplayName("Проверка наличия в zip папке файла формата xls")
    void xlsFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xls")){
                    XLS xls = new XLS(zis);
                    String actualValue =  xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
                    Assertions.assertTrue(actualValue.contains("Город"));
                }
            }
        }

    }

    @Test
    @DisplayName("Проверка наличия в zip папке файла формата csv")
    void csvFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")){
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll();
                    Assertions.assertEquals(3, data.size());
                    Assertions.assertArrayEquals(new String[] {"Comment1", "Anna"},
                            data.get(0)
                    );
                    Assertions.assertArrayEquals(new String[] {"Comment2", "Dmitry"},
                            data.get(1)
                    );
                    Assertions.assertArrayEquals(new String[] {"Comment3", "Ivan"},
                            data.get(2)
                    );

                }

                }
            }
        }

    }

