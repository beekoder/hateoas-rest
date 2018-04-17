package programming.exercise.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapResource extends ResourceSupport {

    private String id;
    private final Map<String, String> content;

    public MapResource(Map<String, String> content) {
        this.id = id;
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }
}
