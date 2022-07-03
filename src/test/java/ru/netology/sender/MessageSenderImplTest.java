package ru.netology.sender;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageSenderImplTest {
    @ParameterizedTest
    @MethodSource("argFor_Rus_Us_EmptyIp")
    void testSendMethod(GeoService geoService, LocalizationService localizationService, Map map, String expected) {
        MessageSenderImpl messageSender = new MessageSenderImpl(geoService, localizationService);
        String message = messageSender.send(map);
        assertEquals(expected, message);

    }

    private static Stream<Arguments> argFor_Rus_Us_EmptyIp() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.0.32.11"))
                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11");


        GeoService geoService1 = Mockito.mock(GeoService.class);
        Mockito.when(geoService1.byIp("96.44.183.149"))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        LocalizationService localizationService1 = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService1.locale(Country.USA))
                .thenReturn("Welcome");
        Map<String, String> headers1 = new HashMap<>();
        headers1.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");


        GeoService geoService2 = Mockito.mock(GeoService.class);
        Mockito.when(geoService2.byIp("96.44.183.149"))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        LocalizationService localizationService2 = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService2.locale(Country.USA))
                .thenReturn("Welcome");
        Map<String, String> headers2 = new HashMap<>();
        headers2.put(MessageSenderImpl.IP_ADDRESS_HEADER, "");


        return Stream.of(Arguments.of(geoService, localizationService, headers, "Добро пожаловать"),
                Arguments.of(geoService1, localizationService1, headers1, "Welcome"),
                Arguments.of(geoService2, localizationService2, headers2, "Welcome"));
    }
}
