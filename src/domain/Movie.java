package domain;

import java.util.List;

public class Movie extends Media {
    private double rating;

    public Movie(String title, List<String> casting, String genre, int year, double rating) {
        super(title, casting, genre, year);
        this.rating = rating;
    }

    @Override
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "rating=" + rating +
                "} " + super.toString();
    }
}
