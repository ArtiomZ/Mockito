package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalizationServiceImplTest {
    @ParameterizedTest
    @MethodSource("getCountry")
    void testLocale(Country country, String expected) {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String message = localizationService.locale(country);
        assertEquals(expected, message);
    }

    private static Stream<Arguments> getCountry() {
        return Stream.of(Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.USA, "Welcome"),
                Arguments.of(Country.GERMANY, "Welcome"));

    }
}
