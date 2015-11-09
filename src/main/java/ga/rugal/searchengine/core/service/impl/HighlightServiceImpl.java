package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.searchengine.core.service.SearchService;
import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
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
public class HighlightServiceImpl implements SearchService
{

    private static final Logger LOG = LoggerFactory.getLogger(HighlightServiceImpl.class.getName());

    @Autowired
    private IndexSearcher searcher;

    @Autowired
    private QueryParser parser;

    /**
     * {@inheritDoc}
     *
     * @throws org.apache.lucene.queryparser.classic.ParseException
     * @throws java.io.IOException
     */
    @Override
    public TopDocs search(String[] keywords) throws ParseException, IOException
    {
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords)
        {
            sb.append(keyword).append(" ");
        }
        LOG.debug(sb.toString());
        Query query = parser.parse(sb.toString());
        return searcher.search(query, SystemDefaultProperties.DEFAULT_HIT_NUMBER);
    }

}
