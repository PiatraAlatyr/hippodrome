import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class HorseTest {
    Horse horse = new Horse("Spirit", 5.0, 2.0);
    Horse horseWithoutDistance = new Horse("Spirit", 5.0);

    @Test
    void nullHorseName() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse(null, 1.0, 1.0)
        );
        assertEquals("Name cannot be null.", exception.getMessage());
    }

    static Stream<String> argsProviderFactory() {
        return Stream.of("", " ",  "   ");
    }
    @ParameterizedTest
    @MethodSource("argsProviderFactory")
    void emptyHorseName(String name) {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse(name, 1.0, 1.0)
        );
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    void negativeSpeed() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse("name", -1.0, 1.0)
        );
        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    void negativeDistance() {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse("name", 1.0, -1.0)
        );
        assertEquals("Distance cannot be negative.", exception.getMessage());
    }

    @Test
    void getName() {
        assertEquals("Spirit", horse.getName());
    }

    @Test
    void getSpeed() {
        assertEquals(5.0, horse.getSpeed());
    }

    @Test
    void getDistance() {
        assertAll("Distance checking",
                () -> assertEquals(2.0, horse.getDistance()),
                () -> assertEquals(0, horseWithoutDistance.getDistance())
        );
    }

    @Test
    void move_CallsGetRandomDoubleWithCorrectParameters() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            // Задаем поведение мока
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(0.5);

            Horse testHorse = new Horse("TestHorse", 2.0, 10.0);
            testHorse.move();

            // Проверяем, что getRandomDouble был вызван с (0.2, 0.9)
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0.2, 10.0, 2.0, 10.4",   // distance = 10 + 2 * 0.2 = 10.4
            "0.5, 5.0, 3.0, 6.5",       // distance = 5 + 3 * 0.5 = 6.5
            "0.9, 0.0, 10.0, 9.0"       // distance = 0 + 10 * 0.9 = 9.0
    })
    void move_UpdatesDistanceCorrectly(double randomValue, double initialDistance, double speed, double expectedDistance) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            // Мокаем getRandomDouble, чтобы возвращал заданное значение
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(randomValue);

            Horse testHorse = new Horse("TestHorse", speed, initialDistance);
            testHorse.move();

            assertEquals(expectedDistance, testHorse.getDistance());
        }
    }
}
