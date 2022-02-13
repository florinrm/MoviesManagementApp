package tests;

import database.Database;
import domain.Movie;
import domain.Show;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ActorsQueriesService;

import java.util.List;
import java.util.Map;

public class ActorsQueriesServiceTest {
    @BeforeEach
    private void setup() {
        Database.getInstance().getMediaTable().clear();

        Database.getInstance().getMediaTable().add(new Show(
                "Peaky Blinders",
                List.of("Cillian Murphy"),
                "Drama",
                2013,
                Map.of(0, 9.5, 1, 8.9)));

        Database.getInstance().getMediaTable().add(new Show(
                "The Sopranos",
                List.of("James Gandolfini", "Michael Imperioli"),
                "Drama",
                1999,
                Map.of(0, 9.5, 1, 8.9, 2, 9.2)));

        Database.getInstance().getMediaTable().add(new Movie(
                "The Godfather",
                List.of("Al Pacino", "Marlon Brando"),
                "Drama",
                1972,
                9.8));

        Database.getInstance().getMediaTable().add(new Movie(
                "The Godfather 2",
                List.of("Al Pacino", "Robert De Niro"),
                "Drama",
                1974,
                9.8));

        Database.getInstance().getMediaTable().add(new Movie(
                "The Godfather 3",
                List.of("Al Pacino", "Andy Garcia"),
                "Drama",
                1990,
                8.0));
    }

    @AfterEach
    private void cleanUp() {
        Database.getInstance().getMediaTable().clear();
    }

    @Test
    public void testGetActors() {
        var actors = ActorsQueriesService.getActors();
        Assertions.assertEquals(7, actors.size());
    }

    @Test
    public void testGetMediaByActors() {
        var actors = ActorsQueriesService.getMediaByActors();
        Assertions.assertEquals(7, actors.size());
        Assertions.assertEquals(3, actors.get("Al Pacino").size());
    }
}
