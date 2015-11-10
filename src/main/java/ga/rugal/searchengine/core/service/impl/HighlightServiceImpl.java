package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.searchengine.common.CommonLogContent;
import ga.rugal.searchengine.core.service.HighlightService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
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
public class HighlightServiceImpl implements HighlightService
{

    private static final Logger LOG = LoggerFactory.getLogger(HighlightServiceImpl.class.getName());

    @Autowired
    private IndexSearcher searcher;

    @Autowired
    private Analyzer analyzer;

    /**
     * {@inheritDoc}
     *
     * @throws java.io.IOException
     * @throws org.apache.lucene.search.highlight.InvalidTokenOffsetsException
     */
    @Override
    public String highlight(ScoreDoc docs, Highlighter highlighter) throws IOException, InvalidTokenOffsetsException
    {
        //The document from searcher
        Document doc = searcher.doc(docs.doc);
        LOG.debug(CommonLogContent.HIGHLIGHTING, doc.getField(SystemDefaultProperties.DEFAULT_PATH_NAME));
        //the target content field
        String text = doc.get(SystemDefaultProperties.DEFAULT_CONTENT_NAME);
        //tokenize target content
        TokenStream tokenStream = analyzer.tokenStream(SystemDefaultProperties.DEFAULT_CONTENT_NAME, text);
        //Get the most relevant fragment in string format
        return highlighter.getBestFragment(tokenStream, text);
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.io.IOException
     * @throws org.apache.lucene.search.highlight.InvalidTokenOffsetsException
     */
    @Override
    public List<String> highlightAll(ScoreDoc[] docs, Highlighter highlighter) throws IOException, InvalidTokenOffsetsException
    {
        List<String> list = new ArrayList(docs.length);
        for (ScoreDoc doc : docs)
        {
            list.add(this.highlight(doc, highlighter));
        }
        return list;
    }

}
