package com.zygimantus.apiwebas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.karumi.marvelapiclient.model.Format;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author Zygimantus
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ApiControllerTests extends AbstractContextControllerTests {

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletRequest request;

    private MockMvc mockMvc;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void marvelComicsFormatsTest() throws Exception {

        String string = this.mockMvc.perform(get("/api/marvel/comics/formats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        Format[] formats = MAPPER.readValue(string, Format[].class);

        assert Arrays.equals(formats, Format.values());
    }

}
