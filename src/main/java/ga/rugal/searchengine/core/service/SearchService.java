package ga.rugal.searchengine.core.service;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Rugal Bernstein
 */
public interface SearchService
{

    /**
     * Get top documents for given keywords.
     *
     * @param keywords
     *
     * @return
     *
     * @throws ParseException
     * @throws IOException
     */
    TopDocs search(String[] keywords) throws ParseException, IOException;

}
