package parsing;

import constants.Constants;
import database.Database;
import domain.Movie;
import domain.Show;
import exceptions.IncorrectFormatException;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InputParsing {
    public static void parse(String inputPath) throws IOException, ParseException, IncorrectFormatException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputPath));

        JSONObject database = (JSONObject) jsonObject.get(Constants.DATABASE);
        JSONArray jsonMovies = (JSONArray)
                database.get(Constants.MOVIES);
        JSONArray jsonShows = (JSONArray)
                database.get(Constants.SHOWS);

        if (jsonMovies != null) {
            for (Object jsonMovie : jsonMovies) {
                Database.getInstance().getMediaTable().add(new Movie(
                        (String) ((JSONObject) jsonMovie).get(Constants.TITLE),
                        convertJSONArray((JSONArray)((JSONObject) jsonMovie).get(Constants.CASTING)),
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

        System.out.println(Database.getInstance().getMediaTable());
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

    public static Map<Integer, Double> toMap(JSONObject jsonObj) throws JSONException, IncorrectFormatException {
        Map<Integer, Double> map = new HashMap<>();
        for (var element: jsonObj.entrySet()) {
            String[] tokens = element.toString().split("=");
            if (tokens.length != 2) {
                throw new IncorrectFormatException();
            }
            map.put(Integer.parseInt(tokens[0]), Double.parseDouble(tokens[1]));
        }
        return map;
    }

}
