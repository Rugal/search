package ga.rugal.searchengine.springmvc.controller;

import ga.rugal.searchengine.common.CommonLogContent;
import ga.rugal.searchengine.common.CommonMessageContent;
import ga.rugal.searchengine.core.entity.SearchResult;
import ga.rugal.searchengine.core.service.EncapsulationService;
import java.io.IOException;
import java.util.List;
import ml.rugal.sshcommon.springmvc.util.Message;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Rugal Bernstein
 */
@Controller
public class SearchController
{

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class.getName());

    @Autowired
    private EncapsulationService encapsulationService;

    @RequestMapping
    @ResponseBody
    public Message search(@RequestParam(value = "q", defaultValue = "") String q) throws ParseException, IOException, InvalidTokenOffsetsException
    {
        if (q.isEmpty())
        {
            LOG.debug(CommonLogContent.EMPTY_QUERY);
            return Message.failMessage(CommonMessageContent.EMPTY_QUERY);
        }
        String[] keywords = q.split("\\s+");
        Message message;
        try
        {
            List<SearchResult> results = encapsulationService.search(keywords);
            message = Message.successMessage(CommonMessageContent.GET_RESULTS, results);
        }
        catch (ParseException | IOException e)
        {
            LOG.error(CommonLogContent.ERROR_SEARCH, e);
            throw e;
        }
        catch (InvalidTokenOffsetsException itoe)
        {
            LOG.error(itoe.getMessage());
            throw itoe;
        }
        return message;
    }
}
