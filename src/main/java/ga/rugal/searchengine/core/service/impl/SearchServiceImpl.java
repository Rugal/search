package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.searchengine.core.service.SearchService;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Do search job.
 *
 * @author Rugal Bernstein
 */
@Service
public class SearchServiceImpl implements SearchService
{

    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class.getName());

    @Autowired
    private IndexSearcher searcher;

    @Autowired
    private QueryParser parser;

    @Autowired
    private SimpleHTMLFormatter htmlFormatter;

    /**
     * {@inheritDoc}
     *
     * @throws org.apache.lucene.queryparser.classic.ParseException
     */
    @Override
    public Query createQuery(String[] keywords) throws ParseException
    {
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords)
        {
            sb.append(keyword).append(" ");
        }
        LOG.debug(sb.toString());
        Query query = parser.parse(sb.toString());
        return query;
    }

    /**
     * {@inheritDoc}
     *
     * @throws org.apache.lucene.queryparser.classic.ParseException
     * @throws java.io.IOException
     */
    @Override
    public TopDocs search(Query query) throws ParseException, IOException
    {
        return searcher.search(query, SystemDefaultProperties.DEFAULT_HIT_NUMBER);
    }
}
