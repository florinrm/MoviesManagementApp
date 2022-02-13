package service;

import database.Database;
import domain.Media;
import exceptions.NoMediaPresentException;

import java.util.List;
import java.util.stream.Collectors;

public class MediaQueriesService {
    public void modifyRating(String title) throws NoMediaPresentException {
        if (Database.getInstance()
                .getMediaTable()
                .stream().map(Media::getTitle)
                .collect(Collectors.toList()).contains(title)) {
            throw new NoMediaPresentException(title);
        }
    }
}
