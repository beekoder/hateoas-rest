package programming.exercise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import programming.exercise.data.MapRepository;
import programming.exercise.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@Service
public class MapService {

    private MapRepository mapRepository;

    @Autowired
    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    public Map<String, String> get(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("Missing id");
        }
        Map<String, String> content = mapRepository.get(id);
        if (content == null) {
            throw new ResourceNotFoundException("Resource not found for key: " + id);
        }
        return content;
    }

    public String create(Map<String, String> content) {
        if (CollectionUtils.isEmpty(content)) {
            throw new IllegalArgumentException("Content is missing");
        }
        return mapRepository.create(content);
    }

    public void update(String id, Map<String, String> content) {
        if (CollectionUtils.isEmpty(content)) {
            throw new IllegalArgumentException("Content is missing");
        }
        get(id);
        mapRepository.update(id, content);
    }

    public void delete(String id) {
        mapRepository.delete(id);
    }

    public Map<String, Map<String, String>> getAll() {
        return mapRepository.getAll();
    }
}
