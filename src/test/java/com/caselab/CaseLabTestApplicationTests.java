package com.caselab;

import com.caselab.entities.FileEntity;
import com.caselab.repositories.FileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CaseLabTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository repository;

    private final String BASE_ENDPOINT = "/api/files";
    private final String json1 = "{\"title\":\"Test File 1\",\"creationDate\":\"2023-10-01T12:00:00Z\",\"description\":\"This is a test file 1.\",\"fileData\":\"base64EncodedData1\"}";
    private final String json2 = "{\"title\":\"Test File 2\",\"creationDate\":\"2023-10-01T13:00:00Z\",\"description\":\"This is a test file 2.\",\"fileData\":\"base64EncodedData2\"}";

    private long addEntity1ToDB() throws Exception {
        return Long.parseLong(
                mockMvc.perform(post(BASE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json1))
                        .andReturn()
                        .getResponse()
                        .getContentAsString());
    }

    private long addEntity2ToDB() throws Exception {
        return Long.parseLong(
                mockMvc.perform(post(BASE_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json2))
                        .andReturn()
                        .getResponse()
                        .getContentAsString());
    }

    @AfterEach
    public void clearDB() {
        repository.deleteAll();
    }

    @Test
    public void testCreateFile() throws Exception {
        long id = Long.parseLong(
                mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString());

        assertTrue(repository.findById(id).isPresent());
    }

    @Test
    public void testGetExistingFile() throws Exception {
        long id = addEntity1ToDB();

        String response = mockMvc.perform(get(BASE_ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        FileEntity entity1 = mapper.readValue(response, FileEntity.class);

        FileEntity entity2 = repository.findById(id).orElse(new FileEntity());

        assertEquals(entity1.getCreationDate(), entity2.getCreationDate());
        assertEquals(entity1.getFileData(), entity2.getFileData());
        assertEquals(entity1.getTitle(), entity2.getTitle());
        assertEquals(entity1.getDescription(), entity2.getDescription());
    }

    @Test
    public void testGetNonExistingFile() throws Exception {
        mockMvc.perform(get(BASE_ENDPOINT + "/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllFiles() throws Exception {
        addEntity1ToDB();
        addEntity2ToDB();

        String response = mockMvc.perform(get(BASE_ENDPOINT, "page", "size", "sort"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        List<FileEntity> list = mapper.readValue(response, new TypeReference<>(){});

        assertEquals(2, list.size());
    }
}
