package ga.rugal.searchengine.core.service;

import ga.rugal.searchengine.core.entity.SearchResult;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

/**
 *
 * @author Rugal Bernstein
 */
public interface EncapsulationService
{

    /**
     * Wrap searching, highlighting and encapsulation together in a service.
     *
     * @param keywords
     *
     * @return Give a list of search result objects that contains path and best fragment of matched
     *         document.
     *
     * @throws ParseException
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    List<SearchResult> search(String[] keywords) throws ParseException, IOException, InvalidTokenOffsetsException;

}
