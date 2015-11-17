package ga.rugal.searchengine.core.service.impl;

import ga.rugal.DBTestBase;
import ga.rugal.searchengine.core.service.SpellCheckService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rugal Bernstein
 */
public class SpellCheckServiceImplTest extends DBTestBase
{

    @Autowired
    private SpellCheckService spellCheckService;

    public SpellCheckServiceImplTest()
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
    public void testCheckAll()
    {
        System.out.println("checkAll");
        String[] words = null;
        SpellCheckServiceImpl instance = new SpellCheckServiceImpl();
        String[] expResult = null;
        String[] result = spellCheckService.checkAll(words);
    }

}
