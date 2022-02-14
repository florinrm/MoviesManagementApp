package database;

import domain.Media;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    private static Database instance;
    private final List<Media> mediaTable = Collections.synchronizedList(new ArrayList<>());

    private Database() {

    }

    public synchronized static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    public List<Media> getMediaTable() {
        return mediaTable;
    }
}
