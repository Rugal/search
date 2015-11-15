package ga.rugal.searchengine.core.dao;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Rugal Bernstein
 */
public interface SearchDAO
{

    /**
     * Get top documents for given keywords.
     *
     * @param query
     *
     * @return
     *
     * @throws ParseException
     * @throws IOException
     */
    TopDocs search(Query query) throws ParseException, IOException;

    /**
     * Create query object from an array of keyword
     *
     * @param keywords
     *
     * @return
     *
     * @throws ParseException
     */
    Query createQuery(String[] keywords) throws ParseException;

}
