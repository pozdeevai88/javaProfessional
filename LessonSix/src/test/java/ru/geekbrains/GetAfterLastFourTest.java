package ru.geekbrains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

class GetAfterLastFourTest implements ArgumentConverter {

    @CsvSource(value = {
            "1,2,3:3,2,1,4,1,2,3",
            "0:0,0,0,4,0",
            "0,0,0:4,0,0,0",},
            delimiter = ':')

    @ParameterizedTest
    void getAfterLastFour(@ConvertWith(GetAfterLastFourTest.class) int[] result,
                          @ConvertWith(GetAfterLastFourTest.class) int[] array) {
        Assertions.assertArrayEquals(result, TempClass.getAfterLastFour(array));
    }

    @Test
    void getAfterLastFourExceptionTest() {
        Assertions.assertThrows(RuntimeException.class, () -> TempClass.getAfterLastFour(new int[]{1, 2, 3, 5, 6}));
    }

    @Override
    public Object convert(Object o, ParameterContext parameterContext) {
        String[] parts = ((String) o).split(",");
        return Stream.of(parts).mapToInt(Integer::parseInt).toArray();
    }
}
