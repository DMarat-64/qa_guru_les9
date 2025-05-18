package tests;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.model.FormaJsonTest;

import java.io.InputStreamReader;
import java.io.Reader;

public class CheckingJson {

    private  ClassLoader cl = FileParsingTest.class.getClassLoader();

    @Test
    @DisplayName("Проверка json")
    void jsonFileParsingImprovedTest() throws Exception{
        try (Reader reader = new InputStreamReader(cl.getResourceAsStream("test.json"))) {
            ObjectMapper objectMapper = new ObjectMapper();

            FormaJsonTest actual = objectMapper.readValue(reader, FormaJsonTest.class);

            Assertions.assertEquals("John", actual.getName());
            Assertions.assertEquals(30, actual.getAge());
            Assertions.assertEquals("Moscow", actual.getAddress().getCity());
            Assertions.assertEquals("Academician Petrovsky Street", actual.getAddress().getStreet());

        }

    }
}
