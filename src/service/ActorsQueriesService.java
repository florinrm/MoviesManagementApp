package service;

import database.Database;
import domain.Media;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActorsQueriesService {
    public static List<String> getActors() {
        return Database.getInstance()
                .getMediaTable()
                .stream()
                .map(Media::getCasting).flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    public static Map<String, List<String>> getMediaByActors() {
        List<String> actors = getActors();
        Map<String, List<String>> mediaByActors = new HashMap<>();

        actors.forEach(actor -> mediaByActors.put(actor, Database.getInstance()
                .getMediaTable()
                .stream()
                .filter(e -> e.getCasting().contains(actor))
                .map(Media::getTitle)
                .collect(Collectors.toList())));

        return mediaByActors;
    }
}
