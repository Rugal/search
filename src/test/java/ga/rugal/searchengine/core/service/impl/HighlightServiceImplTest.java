package ga.rugal.searchengine.core.service.impl;

import ga.rugal.DBTestBase;
import ga.rugal.searchengine.core.service.HighlightService;
import ga.rugal.searchengine.core.service.SearchService;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Rugal Bernstein
 */
public class HighlightServiceImplTest extends DBTestBase
{

    @Autowired
    private HighlightService highlightService;

    @Autowired
    private SearchService searchService;

    @Resource(name = "keywords")
    private String[] keywords;

    @Autowired
    private SimpleHTMLFormatter htmlFormatter;

    private Highlighter highlighter;

    private TopDocs topDocs;

    public HighlightServiceImplTest()
    {
    }

    @Before
    public void setUp() throws ParseException, IOException
    {
        Query query = searchService.createQuery(keywords);
        topDocs = searchService.search(query);
        highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testHighlightAll() throws Exception
    {
        System.out.println("highlightAll");
        List<String> result = highlightService.highlightAll(topDocs.scoreDocs, highlighter);
        Assert.assertEquals(topDocs.scoreDocs.length, result.size());
    }

}
