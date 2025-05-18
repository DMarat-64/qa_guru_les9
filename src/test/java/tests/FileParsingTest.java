package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Проверка наличия в zip папке файлов разного формата")
public class FileParsingTest {
    private  final ClassLoader cl = FileParsingTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка наличия файлов в zip папке")
    void zipFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            List<String> fileNames = new ArrayList<>();
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                fileNames.add(entry.getName());
                System.out.println(entry.getName());
            }
            assertThat(fileNames)
                    .as("Проверка что архив содержит файлы")
                    .isNotEmpty()
                    .hasSizeGreaterThan(0);
            }
    }

    @Test
    @DisplayName("Проверка наличия в zip папке файла формата pdf")
    void pdfFileParsingTest() throws Exception{
        boolean pdfFound = false;
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")){
                    pdfFound = true;
                    PDF pdf = new PDF(zis);
                    assertThat(pdf.numberOfPages)
                            .as("Проверка страницы")
                            .isEqualTo(2);
                }
            }
            assertThat(pdfFound)
                    .as("Проверка что в архиве найден PDF файл")
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка наличия в zip папке файла формата xls")
    void xlsFileParsingTest() throws Exception{
        boolean xlsFound = false;
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xls")){
                    xlsFound = true;
                    XLS xls = new XLS(zis);
                    String actualValue =  xls.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
                   assertThat(actualValue)
                           .as("Проверка содержимого")
                           .contains("Город");
                }
            }
            assertThat(xlsFound)
                    .as("Проверка что в архиве найден xls файл")
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка наличия в zip папке файла формата csv")
    void csvFileParsingTest() throws Exception{
        boolean csvFound = false;
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("lesson9.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")){
                    csvFound = true;
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll();
                    assertThat(data)
                            .as("Проверка количества строк")
                            .hasSize(3);
                    assertThat(data.get(0))
                            .as("Проверка первой строки")
                            .containsExactly("Comment1", "Anna");
                    assertThat(data.get(1))
                            .as("Проверка второй строки")
                            .containsExactly("Comment2", "Dmitry");
                    assertThat(data.get(2))
                            .as("Проверка третьей строки")
                            .containsExactly("Comment3", "Ivan");
                }
            }
            assertThat(csvFound)
                    .as("Проверка что в архиве найден csv файл")
                            .isTrue();
        }
    }
}