package ru.geekbrains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

class CheckOneAndFourTest implements ArgumentConverter {

    @CsvSource(value = {
            "true:1,4,1,4",
            "false:1,1,1,1",
            "false:4,4,4,4",
            "false:5"},
            delimiter = ':')

    @ParameterizedTest
    void CheckOneAndFour(boolean result, @ConvertWith(CheckOneAndFourTest.class) int[] array) {
        Assertions.assertEquals(result, TempClass.checkOneAndFour(array));
    }

    @Override
    public Object convert(Object o, ParameterContext parameterContext) {
        String[] parts = ((String) o).split(",");
        return Stream.of(parts).mapToInt(Integer::parseInt).toArray();
    }

}