import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TestMessageSender {

    @BeforeEach
    public void init() {
        System.out.println("test started");
    }

    @BeforeAll
    public static void started() {
        System.out.println("tests started");
    }

    @AfterEach
    public void finished() {
        System.out.println("test completed");
    }

    @BeforeAll
    public static void finishedAll() {
        System.out.println("tests completed");
    }


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
