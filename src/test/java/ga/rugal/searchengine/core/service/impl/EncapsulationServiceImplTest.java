package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.DBTestBase;
import ga.rugal.searchengine.core.entity.SearchResult;
import ga.rugal.searchengine.core.service.EncapsulationService;
import java.util.List;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rugal Bernstein
 */
public class EncapsulationServiceImplTest extends DBTestBase
{

    @Autowired
    private EncapsulationService encapsulationService;

    @Resource(name = "keywords")
    private String[] keywords;

    public EncapsulationServiceImplTest()
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
        List<SearchResult> list = encapsulationService.search(keywords);
        Assert.assertEquals(SystemDefaultProperties.DEFAULT_HIT_NUMBER, list.size());
        for (SearchResult result : list)
        {
            Assert.assertNotNull(result);
            System.out.println(result);
        }
    }

}
