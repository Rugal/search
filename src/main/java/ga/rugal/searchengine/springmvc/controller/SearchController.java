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
            String me = CommonMessageContent.GET_RESULTS;
            if (correctable)
            {//If correction allowed
                keywords = spellCheckService.checkAll(keywords);
                StringBuilder sb = new StringBuilder();
                for (String keyword : keywords)
                {
                    sb.append(keyword).append(" ");
                }
                //also indicate that this search has been corrected
                me = String.format(CommonMessageContent.CORRECTED_WITH, sb.toString());
            }
            List<SearchResult> results = encapsulationService.search(keywords);
            //what if no matched result?
            if (results.isEmpty())
            {
                me = CommonMessageContent.NOTHING_MATCHED;
                results = null;
            }
            message = Message.successMessage(me, results);
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
