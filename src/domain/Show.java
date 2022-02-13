package domain;

import java.util.List;
import java.util.Map;

public class Show extends Media {
    private Map<Integer, Double> seasonRatings;

    public Show(String title, List<String> casting, String genre, int year, Map<Integer, Double> seasonRatings) {
        super(title, casting, genre, year);
        this.seasonRatings = seasonRatings;
    }

    @Override
    public double getRating() {
        return seasonRatings.values().stream().reduce(0.d, Double::sum);
    }

    public Map<Integer, Double> getSeasonRatings() {
        return seasonRatings;
    }

    public void setSeasonRatings(Map<Integer, Double> seasonRatings) {
        this.seasonRatings = seasonRatings;
    }

    @Override
    public String toString() {
        return "Show{" +
                "seasonRatings=" + seasonRatings +
                "} " + super.toString();
    }

    public void setSeasonRating(int season, double rating) {
        seasonRatings.put(season, rating);
    }
}
