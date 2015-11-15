package ga.rugal.searchengine.core.dao;

import java.io.IOException;
import java.util.List;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

/**
 *
 * @author Rugal Bernstein
 */
public interface HighlightDAO
{

    /**
     * Highlight one document.
     *
     * @param docs        The document indicator.
     * @param highlighter Given high-lighter
     *
     * @return The best fragment that highlighted
     *
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    String highlight(ScoreDoc docs, Highlighter highlighter) throws IOException, InvalidTokenOffsetsException;

    /**
     * Highlight all given documents in given array.
     *
     * @param docs
     * @param highlighter
     *
     * @return a list of best highlighted fragments that corresponding to given documents array.
     *
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    List<String> highlightAll(ScoreDoc[] docs, Highlighter highlighter) throws IOException, InvalidTokenOffsetsException;

}
