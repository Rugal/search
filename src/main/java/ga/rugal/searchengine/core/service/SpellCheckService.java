package ga.rugal.searchengine.core.service;

/**
 *
 * @author Rugal Bernstein
 */
public interface SpellCheckService
{

    /**
     * Check if one word is in dictionary. If not, correct it with the best match by edit distance
     * algorithm.
     *
     * @param word
     *
     * @return the origin word if it is in dictionary; otherwise, give a corrected word.
     */
    String check(String word);

    /**
     * Batch checking.
     *
     * @param words
     *
     * @return An array of words that have been either corrected or kept.
     */
    String[] checkAll(String[] words);

}
