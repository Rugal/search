package ga.rugal.searchengine.springmvc.controller;

import ga.rugal.searchengine.common.CommonLogContent;
import ga.rugal.searchengine.common.CommonMessageContent;
import ga.rugal.searchengine.core.entity.SearchResult;
import ga.rugal.searchengine.core.service.EncapsulationService;
import ga.rugal.searchengine.core.service.SpellCheckService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    private SpellCheckService spellCheckService;

    /**
     * Showing index page.
     *
     * @return the page of index
     *
     */
    @RequestMapping(value = "/",
                    method = RequestMethod.GET
    )
    @ResponseBody
    public ModelAndView index()
    {
        return new ModelAndView("/page/index.html");
    }

    /**
     * Search pages for given key words.
     *
     * @param queryString a list of key word.
     * @param correctable indicate if current query is correctable. true means keywords will be
     *                    corrected by spell checker if spelling wrongly; otherwise, to force search
     *                    the given keywords even they are incorrect in spelling.
     *
     * @return A Message object that contains serialized content of query result.
     *
     * @throws ParseException
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    @RequestMapping(value = "/",
                    method = RequestMethod.GET,
                    params =
                    {
                        "q"
                    }
    )
    @ResponseBody
    public Message search(@RequestParam(value = "q",
                                        defaultValue = "",
                                        required = true) String queryString,
                          @RequestParam(value = "c",
                                        defaultValue = "true",
                                        required = false) boolean correctable)
        throws ParseException, IOException, InvalidTokenOffsetsException
    {
        if (queryString.isEmpty())
        {
            LOG.debug(CommonLogContent.EMPTY_QUERY);
            return Message.failMessage(CommonMessageContent.EMPTY_QUERY);
        }
        String[] keywords = queryString.split("\\s+");
        Message message;
        try
        {
            String printString = this.catString(keywords);
            String messageText = String.format(CommonMessageContent.GET_RESULTS, printString);
            //If correction allowed
            if (correctable)
            {
                //if correct needed
                String[] correctedKeywords = spellCheckService.checkAll(keywords);
                if (null != correctedKeywords)
                {
                    keywords = correctedKeywords;
                    //also indicate this search has been corrected
                    messageText = String.format(CommonMessageContent.CORRECTED_WITH, this.catString(keywords));
                }
            }
            List<SearchResult> results = encapsulationService.search(keywords);
            //what if no matched result?
            if (results.isEmpty())
            {
                messageText = CommonMessageContent.NOTHING_MATCHED;
                results = null;
            }
            message = Message.successMessage(messageText, results);
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

    /**
     * Try to check spelling.
     *
     * @param queryString a list of key word.
     *
     * @return A Message object that contains checked words, or null if no check needed.
     *
     * @throws ParseException
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    @RequestMapping(value = "/spell",
                    method = RequestMethod.GET,
                    params =
                    {
                        "q"
                    }
    )
    @ResponseBody
    public Message spellCheck(@RequestParam(value = "q",
                                            defaultValue = "",
                                            required = true) String queryString)
        throws ParseException, IOException, InvalidTokenOffsetsException
    {
        if (queryString.isEmpty())
        {
            LOG.debug(CommonLogContent.EMPTY_QUERY);
            return Message.failMessage(CommonMessageContent.EMPTY_QUERY);
        }
        String[] oldKeywords = queryString.split("\\s+");
        String[] newKeywords = spellCheckService.checkAll(oldKeywords);
        String messageText = CommonMessageContent.NO_CORRECTION_NEED;
        if (null != newKeywords)
        {
            String printString = this.catString(newKeywords);
            messageText = String.format(CommonMessageContent.CORRECTED_WITH, printString);
        }
        return Message.successMessage(messageText, newKeywords);

    }

    /**
     * Simply concatenate string array into a string.
     *
     * @param keywords
     *
     * @return
     */
    private String catString(String[] keywords)
    {
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords)
        {
            sb.append(keyword).append(" ");
        }
        return sb.toString().trim();
    }
}
