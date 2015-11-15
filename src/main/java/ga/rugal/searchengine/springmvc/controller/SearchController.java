package ga.rugal.searchengine.springmvc.controller;

import ga.rugal.searchengine.common.CommonLogContent;
import ga.rugal.searchengine.common.CommonMessageContent;
import ga.rugal.searchengine.core.service.HighlightService;
import ga.rugal.searchengine.core.service.SearchService;
import java.io.IOException;
import java.util.List;
import ml.rugal.sshcommon.springmvc.util.Message;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class SearchController
{

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class.getName());

    @Autowired
    private SearchService searchService;

    @Autowired
    private HighlightService highlightService;

    @Autowired
    private SimpleHTMLFormatter htmlFormatter;

    @RequestMapping(params =
    {
        "q"
    }
    )

    public Message search(@RequestParam(value = "q", defaultValue = "") String q) throws ParseException, IOException, InvalidTokenOffsetsException
    {
        if (q.isEmpty())
        {
            LOG.debug(CommonLogContent.EMPTY_QUERY);
            return Message.failMessage(CommonMessageContent.EMPTY_QUERY);
        }
        String[] keywords = q.split("\\s+");
        try
        {
            Query query = searchService.createQuery(keywords);
            TopDocs topDocs = searchService.search(query);
            Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
            List<String> list = highlightService.highlightAll(topDocs.scoreDocs, highlighter);
        }
        catch (ParseException | IOException e)
        {
            LOG.error("Error while searching", e);
            throw e;
        }
        catch (InvalidTokenOffsetsException itoe)
        {
            LOG.error(itoe.getMessage());
            throw itoe;
        }
        return null;
    }
}
