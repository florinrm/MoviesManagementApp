package service;

import database.Database;
import domain.Media;
import domain.Movie;
import domain.Show;
import exceptions.NoMediaPresentException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MediaQueriesService {
    public static void modifyRatingMovie(String title, Double rating) throws NoMediaPresentException {
        checkIfTitleIsPresent(title);

        var media = Database.getInstance()
                .getMediaTable()
                .stream()
                .filter(e -> e instanceof Movie)
                .filter(e -> e.getTitle().equals(title))
                .collect(Collectors.toList());

        media.forEach(e -> ((Movie) e).setRating(rating));

        Database.getInstance().getMediaTable().removeIf(e -> (e instanceof Movie) && (e.getTitle().equals(title)));
        Database.getInstance().getMediaTable().addAll(media);
    }

    public static void modifyRatingShow(String title, Double rating, int season) throws NoMediaPresentException {
        checkIfTitleIsPresent(title);

        var media = Database.getInstance()
                .getMediaTable()
                .stream()
                .filter(e -> e instanceof Show)
                .filter(e -> e.getTitle().equals(title))
                .collect(Collectors.toList());

        media.forEach(e -> ((Show) e).setSeasonRating(season, rating));

        Database.getInstance().getMediaTable().removeIf(e -> (e instanceof Show) && (e.getTitle().equals(title)));
        Database.getInstance().getMediaTable().addAll(media);
    }

    private static void checkIfTitleIsPresent(String title) throws NoMediaPresentException {
        if (!Database.getInstance()
                .getMediaTable()
                .stream().map(Media::getTitle)
                .collect(Collectors.toList()).contains(title)) {
            throw new NoMediaPresentException(title);
        }
    }

    public static List<Media> getMediaByActor(String actor) {
        return Database.getInstance().getMediaTable()
                .stream()
                .filter(e -> e.getCasting().contains(actor))
                .collect(Collectors.toList());
    }

    public static List<Media> getMediaByGenre(String genre) {
        return Database.getInstance().getMediaTable()
                .stream()
                .filter(e -> e.getGenre().contains(genre))
                .collect(Collectors.toList());
    }

    public static List<Media> getMediaOrderedByRating() {
        return Database.getInstance().getMediaTable()
                .stream()
                .sorted(Comparator.comparingDouble(Media::getRating))
                .collect(Collectors.toList());
    }


    public static Media getMediaByTitle(String title) throws NoMediaPresentException {
        var mediaOptional = Database.getInstance()
                .getMediaTable()
                .stream()
                .filter(e -> e.getTitle().equals(title))
                .findFirst();

        if (mediaOptional.isEmpty()) {
            throw new NoMediaPresentException(title);
        } else {
            return mediaOptional.get();
        }
    }
}
