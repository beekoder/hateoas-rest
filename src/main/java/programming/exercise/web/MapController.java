package programming.exercise.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import programming.exercise.service.MapService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/maps")
public class MapController {

    private MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }


    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<MapResource> create(@RequestBody Map<String, String> content) {
        String id = mapService.create(content);
        return constructResponse(HttpStatus.CREATED, id, content);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<MapResource> update(@PathVariable String id, @RequestBody Map<String, String> content) {
        mapService.update(id, content);
        return constructResponse(HttpStatus.OK, id, content);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<MapResource> get(@PathVariable String id) {
        Map<String, String> content = mapService.get(id);
        return constructResponse(HttpStatus.OK, id, content);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HttpEntity<List<MapResource>> getAll() {
        Map<String, Map<String, String>> allContentMap = mapService.getAll();
        List<MapResource> mapResources = allContentMap.entrySet().stream().map(ctm -> toMapResource(ctm.getKey(), ctm.getValue())).collect(Collectors.toList());
        return ResponseEntity.ok(mapResources);
    }

    @DeleteMapping("/{key}")
    public HttpEntity<MapResource> delete(@PathVariable String key) {
        mapService.delete(key);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private HttpEntity<MapResource> constructResponse(HttpStatus httpStatus, String id, Map<String, String> content) {
        return new ResponseEntity<>(toMapResource(id, content), httpStatus);
    }

    private MapResource toMapResource(String id, Map<String, String> content) {
        MapResource resource = new MapResource(content);
        resource.add(linkTo(methodOn(MapController.class).get(id)).withSelfRel());
        return resource;
    }
}
