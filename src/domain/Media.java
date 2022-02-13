package domain;

import java.util.List;

public abstract class Media {
    private String title;
    private List<String> casting;
    private int year;
    private String genre;

    public Media(String title, List<String> casting, String genre, int year) {
        this.title = title;
        this.casting = casting;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCasting() {
        return casting;
    }

    public void setCasting(List<String> casting) {
        this.casting = casting;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public abstract double getRating();

    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", casting=" + casting +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                '}';
    }
}
