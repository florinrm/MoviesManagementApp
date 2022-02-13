package parsing;

import constants.Constants;
import database.Database;
import domain.Movie;
import domain.Show;
import exceptions.IncorrectFormatException;
import exceptions.NoMediaPresentException;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.ActorsQueriesService;
import service.MediaQueriesService;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputParsing {
    public static void parse(String inputPath) throws IOException, ParseException, IncorrectFormatException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));

        JSONObject database = (JSONObject) jsonObject.get(Constants.DATABASE);
        JSONArray commands = (JSONArray) jsonObject.get(Constants.COMMANDS);
        JSONArray jsonMovies = (JSONArray)
                database.get(Constants.MOVIES);
        JSONArray jsonShows = (JSONArray)
                database.get(Constants.SHOWS);

        parseData(jsonMovies, jsonShows);
        parseCommands(commands);
    }

    private static List<String> convertJSONArray(final JSONArray array) {
        if (array != null) {
            List<String> finalArray = new ArrayList<>();
            for (Object object : array) {
                finalArray.add((String) object);
            }
            return finalArray;
        } else {
            return null;
        }
    }

    private static Map<Integer, Double> toMap(JSONObject jsonObj) throws JSONException, IncorrectFormatException {
        Map<Integer, Double> map = new HashMap<>();
        for (var element : jsonObj.entrySet()) {
            String[] tokens = element.toString().split("=");
            if (tokens.length != 2) {
                throw new IncorrectFormatException();
            }
            map.put(Integer.parseInt(tokens[0]), Double.parseDouble(tokens[1]));
        }
        return map;
    }

    private static void parseData(JSONArray jsonMovies, JSONArray jsonShows) throws IncorrectFormatException {
        if (jsonMovies != null) {
            for (Object jsonMovie : jsonMovies) {
                Database.getInstance().getMediaTable().add(new Movie(
                        (String) ((JSONObject) jsonMovie).get(Constants.TITLE),
                        convertJSONArray((JSONArray) ((JSONObject) jsonMovie).get(Constants.CASTING)),
                        (String) ((JSONObject) jsonMovie).get(Constants.GENRE),
                        Math.toIntExact((Long) ((JSONObject) jsonMovie).get(Constants.YEAR)),
                        (Double) ((JSONObject) jsonMovie).get(Constants.RATING)
                ));
            }
        }

        if (jsonShows != null) {
            for (Object jsonShow : jsonShows) {
                Database.getInstance().getMediaTable().add(new Show(
                        (String) ((JSONObject) jsonShow).get(Constants.TITLE),
                        convertJSONArray((JSONArray) ((JSONObject) jsonShow).get(Constants.CASTING)),
                        (String) ((JSONObject) jsonShow).get(Constants.GENRE),
                        Math.toIntExact((Long) ((JSONObject) jsonShow).get(Constants.YEAR)),
                        toMap((JSONObject) ((JSONObject) jsonShow).get(Constants.SEASONS)))
                );
            }
        }
    }

    private static void parseCommands(JSONArray commands) {
        ExecutorService commandsExecutorService = Executors.newCachedThreadPool();
        List<Runnable> commandsTasks = new ArrayList<>();

        for (var item : commands) {
            var command = (JSONObject) item;
            switch ((String) command.get(Constants.NAME)) {
                case Constants.MODIFY_RATING_SHOW -> commandsTasks.add(() -> {
                    try {
                        MediaQueriesService.modifyRatingShow(
                                (String) command.get(Constants.SHOW),
                                (Double) command.get(Constants.RATING),
                                Math.toIntExact((Long) command.get(Constants.YEAR))
                        );
                    } catch (NoMediaPresentException e) {
                        e.printStackTrace();
                    }
                });
                case Constants.MODIFY_RATING_MOVIE -> commandsTasks.add(() -> {
                    try {
                        MediaQueriesService.modifyRatingMovie(
                                (String) command.get(Constants.MOVIE),
                                (Double) command.get(Constants.RATING)
                        );
                    } catch (NoMediaPresentException e) {
                        e.printStackTrace();
                    }
                });
                case Constants.GET_ACTORS -> commandsTasks.add(() ->
                        System.out.println(ActorsQueriesService.getActors()));
                case Constants.GET_MEDIA_BY_ACTORS -> commandsTasks.add(() ->
                        System.out.println(ActorsQueriesService.getMediaByActors()));
                case Constants.GET_MEDIA_BY_ACTOR -> commandsTasks.add(() ->
                        System.out.println(MediaQueriesService.getMediaByActor((String) command.get(Constants.ACTOR))));
                case Constants.GET_MEDIA_BY_GENRE -> commandsTasks.add(() ->
                        System.out.println(MediaQueriesService.getMediaByGenre((String) command.get(Constants.TITLE))));
                case Constants.GET_MEDIA_BY_TITLE -> commandsTasks.add(() ->
                {
                    try {
                        System.out.println(MediaQueriesService.getMediaByTitle((String) command.get(Constants.TITLE)));
                    } catch (NoMediaPresentException e) {
                        e.printStackTrace();
                    }
                });
                case Constants.GET_MEDIA_ORDERED_BY_RATING -> commandsTasks.add(() -> System.out.println(MediaQueriesService.getMediaOrderedByRating()));
            }
        }

        for (var task : commandsTasks) {
            commandsExecutorService.submit(task);
        }

        commandsExecutorService.shutdown();
        while (!commandsExecutorService.isTerminated()) {
        }
    }

}
