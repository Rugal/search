package ga.rugal.searchengine.core.service.impl;

import ga.rugal.DBTestBase;
import ga.rugal.searchengine.core.service.IndexingService;
import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rugal Bernstein
 */
public class IndexingServiceImplTest extends DBTestBase
{

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private File originFolderPath;

    public IndexingServiceImplTest()
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
    public void testCreateIndex() throws Exception
    {
        System.out.println("createIndex");
        File folderPath = originFolderPath;
        indexingService.createIndex(folderPath);
    }

}
