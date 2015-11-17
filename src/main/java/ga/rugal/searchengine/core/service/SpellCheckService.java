package ga.rugal.searchengine.core.service;

/**
 *
 * @author Rugal Bernstein
 */
public interface SpellCheckService
{

    String check(String word);

    String[] checkAll(String[] words);

}
