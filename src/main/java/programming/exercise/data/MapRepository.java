package programming.exercise.data;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository
public class MapRepository {
    private DB db;
    private Map<String, Map<String, String>> map;

    public String create(Map<String, String> content) {
        String id = UUID.randomUUID().toString();
        map.put(id, content);
        return id;
    }

    public String update(String id, Map<String, String> content) {
        map.put(id, content);
        return id;
    }

    public Map<String, String> get(String id) {
        return map.get(id);
    }

    public Map<String, Map<String, String>> getAll() {
        return Collections.unmodifiableMap(map);
    }

    public void delete(String id) {
        map.remove(id);
    }

    @PostConstruct
    private void init() {
        db = DBMaker.fileDB(getDBFile()).make();
        map = db.hashMap("map", Serializer.STRING, Serializer.JAVA).createOrOpen();
    }

    @PreDestroy
    private void shutdown() {
        try {
            db.close();
        }catch(Exception ignore) {

        }
    }

    private File getDBFile() {
        File f = new File(System.getProperty("user.dir") + "/data/mapFile.db");
        if (!f.exists()) {
            if (!f.getParentFile().mkdir()) {
                throw new RuntimeException("Cannot create directory: " + f.getParentFile());
            }
        }
        return f;
    }
}
