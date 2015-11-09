package ga.rugal.searchengine.core.service.impl;

import org.apache.lucene.search.TopDocs;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class SearchServiceImplTest
{

    public SearchServiceImplTest()
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
    public void testSearch() throws Exception
    {
        System.out.println("search");
        String[] keywords = null;
        SearchServiceImpl instance = new SearchServiceImpl();
        TopDocs expResult = null;
        TopDocs result = instance.search(keywords);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

}
