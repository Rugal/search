package ga.rugal.searchengine.springmvc.controller;

import ga.rugal.ControllerClientSideTestBase;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Rugal Bernstein
 */
public class SearchControllerTest extends ControllerClientSideTestBase
{

    @Resource(name = "wrongSpellingKeywords")
    private String keywords;

    public SearchControllerTest()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testNormalSearch() throws Exception
    {
        System.out.println("search");
        this.mockMvc.perform(get("/search")
            .param("q", keywords)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testSearchNothing() throws Exception
    {
        System.out.println("search");
        this.mockMvc.perform(get("/search")
            .param("q", "introductian")
            .param("c", "false")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testNoQuerySearch() throws Exception
    {
        System.out.println("search");
        this.mockMvc.perform(get("/search")
            .param("q", "")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
