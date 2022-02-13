package database;

import domain.Media;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private List<Media> mediaTable = new ArrayList<>();

    private Database() {

    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public List<Media> getMediaTable() {
        return mediaTable;
    }
}
