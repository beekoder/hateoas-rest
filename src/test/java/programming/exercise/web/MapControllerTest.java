package programming.exercise.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import programming.exercise.data.MapRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MapControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MapRepository mapRepository;

    private static final String URL_TEMPLATE = "/maps/{key}";


    @Test
    public void testGet() throws Exception {
        String id = UUID.randomUUID().toString();
        Map<String, String> content = generateContent();
        when(mapRepository.get(id)).thenReturn(content);
        mockMvc.perform(get(URL_TEMPLATE, id))
               .andExpect(jsonPath("$.content").value(content))
               .andExpect(jsonPath("$._links.self.href").exists())
               .andExpect(status().isOk());
    }

    @Test
    public void testGetNonExistingResource() throws Exception {
        String id = UUID.randomUUID().toString();
        when(mapRepository.get(id)).thenReturn(null);
        mockMvc.perform(get(URL_TEMPLATE, id))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        Map<String, String> content = generateContent();
        mockMvc.perform(post(URL_TEMPLATE, "")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(toJson(content)))
               .andExpect(jsonPath("$.content").value(content))
               .andExpect(jsonPath("$._links.self.href").exists())
               .andExpect(status().isCreated());
        verify(mapRepository, times(1)).create(content);
    }

    @Test
    public void testCreateMissingContent() throws Exception {
        mockMvc.perform(post(URL_TEMPLATE, "")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(toJson(new HashMap())))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {
        String id = UUID.randomUUID().toString();
        Map<String, String> existingContent = generateContent();
        Map<String, String> newContent = new HashMap<>(existingContent);
        newContent.put("newKey", "newValue");
        when(mapRepository.get(id)).thenReturn(existingContent);
        when(mapRepository.update(id, newContent)).thenReturn(id);
        mockMvc.perform(put(URL_TEMPLATE, id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(toJson(newContent)))
               .andExpect(jsonPath("$.content").value(newContent))
               .andExpect(jsonPath("$._links.self.href").exists())
               .andExpect(status().isOk());
    }

    @Test
    public void testUpdateNonExistingResource() throws Exception {
        String id = UUID.randomUUID().toString();
        when(mapRepository.get(id)).thenReturn(null);
        mockMvc.perform(put(URL_TEMPLATE, id)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(toJson(generateContent())))
               .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        String id = "foo";
        mockMvc.perform(delete(URL_TEMPLATE, id))
               .andExpect(status().isNoContent());
        verify(mapRepository, times(1)).delete(id);
    }

    private String toJson(Map<String, String> content) throws Exception {
        return new ObjectMapper().writeValueAsString(content);
    }

    private Map<String, String> generateContent() {
        Map<String, String> content = new HashMap<>();
        content.put("foo", "bar");
        content.put("test", "me");
        return content;
    }


}
