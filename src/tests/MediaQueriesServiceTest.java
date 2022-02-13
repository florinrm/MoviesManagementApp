package tests;

import database.Database;
import domain.Movie;
import domain.Show;
import exceptions.NoMediaPresentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.MediaQueriesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaQueriesServiceTest {
    @BeforeEach
    private void setup() {
        Database.getInstance().getMediaTable().clear();

        Database.getInstance().getMediaTable().add(new Show(
                "Peaky Blinders",
                List.of("Cillian Murphy"),
                "Drama",
                2013,
                new HashMap<>(Map.of(0, 9.5, 1, 8.9))));

        Database.getInstance().getMediaTable().add(new Show(
                "The Sopranos",
                List.of("James Gandolfini", "Michael Imperioli"),
                "Drama",
                1999,
                new HashMap<>(Map.of(0, 9.5, 1, 8.9, 2, 9.2))));

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
    public void testModifyRatingMovie() throws NoMediaPresentException {
        MediaQueriesService.modifyRatingMovie("The Godfather", 10.0);
        var media = MediaQueriesService.getMediaByTitle("The Godfather");

        Assertions.assertEquals(10.0, media.getRating());
    }

    @Test
    public void testModifyRatingMovieNotPresent() {
        Assertions.assertThrows(NoMediaPresentException.class,
                () -> MediaQueriesService.modifyRatingMovie("The Godfather 4", 10.0));
    }

    @Test
    public void testGetMediaByActor() {
        var media = MediaQueriesService.getMediaByActor("Al Pacino");

        Assertions.assertEquals(3, media.size());
        Assertions.assertEquals(3, (int) media.stream()
                .filter(e -> e.getCasting().contains("Al Pacino"))
                .count());
    }

    @Test
    public void testModifyRatingShow() throws NoMediaPresentException {
        MediaQueriesService.modifyRatingShow("Peaky Blinders", 10.0, 1);
        var media = MediaQueriesService.getMediaByTitle("Peaky Blinders");

        Assertions.assertEquals(10.0, ((Show) media).getSeasonRatings().get(1));
    }

    @Test
    public void testModifyRatingShowNotPresent() {
        Assertions.assertThrows(NoMediaPresentException.class,
                () -> MediaQueriesService.modifyRatingShow("The Godfather 4", 10.0, 1));
    }

    @Test
    public void testGetMediaByGenre() throws NoMediaPresentException {
        var media = MediaQueriesService.getMediaByGenre("Drama");
        Assertions.assertEquals(5, media.size());
        Assertions.assertEquals(5, media.stream().filter(e -> e.getGenre().equals("Drama")).count());
    }

    @Test
    public void testGetMediaOrderedByRating() throws NoMediaPresentException {
        var media = MediaQueriesService.getMediaOrderedByRating();
        Assertions.assertEquals(5, media.size());
    }
}
