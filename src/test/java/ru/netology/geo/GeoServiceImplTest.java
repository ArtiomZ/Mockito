package ru.netology.geo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GeoServiceImplTest {
    @ParameterizedTest
    @MethodSource("location")
    void testByIp(String ip, Location expected) {
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location location = geoService.byIp(ip);
        assertEquals(expected, location);
    }

    private static Stream<Arguments> location() {
        return Stream.of(Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.", new Location("New York", Country.USA, null, 0)),
                Arguments.of("70.", null));
    }

    @Test
    public void byCoordinatesTestException() {
        GeoServiceImpl geoService = new GeoServiceImpl();
        double latitude = 54.42;
        double longitude = 20.3;
        Class<RuntimeException> expected = RuntimeException.class;
        assertThrows(expected, () -> geoService.byCoordinates(latitude, longitude));
    }
}
