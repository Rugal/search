package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.searchengine.core.dao.HighlightDAO;
import ga.rugal.searchengine.core.dao.SearchDAO;
import ga.rugal.searchengine.core.entity.SearchResult;
import ga.rugal.searchengine.core.service.EncapsulationService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
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
public class EncapsulationServiceImpl implements EncapsulationService
{

    private static final Logger LOG = LoggerFactory.getLogger(EncapsulationServiceImpl.class.getName());

    @Autowired
    private IndexSearcher searcher;

    @Autowired
    private SearchDAO searchService;

    @Autowired
    private HighlightDAO highlightService;

    @Autowired
    private SimpleHTMLFormatter htmlFormatter;

    /**
     * {@inheritDoc }
     *
     * @throws org.apache.lucene.queryparser.classic.ParseException
     * @throws java.io.IOException
     * @throws org.apache.lucene.search.highlight.InvalidTokenOffsetsException
     */
    @Override
    public List<SearchResult> search(String[] keywords) throws ParseException, IOException, InvalidTokenOffsetsException
    {
        Query query = searchService.createQuery(keywords);
        TopDocs topDocs = searchService.search(query);
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
        List<String> list = highlightService.highlightAll(topDocs.scoreDocs, highlighter);

        List<SearchResult> results = new ArrayList<>(list.size());
        for (int i = 0; i < topDocs.scoreDocs.length; i++)
        {
            //get path of document to encapsulate it with fragment
            Document document = searcher.doc(topDocs.scoreDocs[i].doc);
            String path = document.getField(SystemDefaultProperties.DEFAULT_PATH_NAME).stringValue();
            String fragment = list.get(i);
            results.add(new SearchResult(path, fragment));
        }
        return results;
    }

}
