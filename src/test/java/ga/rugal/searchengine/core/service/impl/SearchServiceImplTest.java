package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.DBTestBase;
import ga.rugal.searchengine.core.service.SearchService;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rugal Bernstein
 */
public class SearchServiceImplTest extends DBTestBase
{

    @Autowired
    private SearchService searchService;

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
    public void testCreateQueryAndSearch() throws Exception
    {
        System.out.println("createQuery");
        String[] keywords = new String[]
        {
            "web", "apache", "w3c"
        };
        Query query = searchService.createQuery(keywords);
        TopDocs result = searchService.search(query);
        Assert.assertEquals(SystemDefaultProperties.DEFAULT_HIT_NUMBER, result.scoreDocs.length);
    }

}
