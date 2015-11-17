package ga.rugal.searchengine.core.service.impl;

import ga.rugal.searchengine.core.service.SpellCheckService;
import config.SystemDefaultProperties;
import ga.rugal.trie.Trie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rugal Bernstein
 */
@Service
public class SpellCheckServiceImpl implements SpellCheckService
{

    private static final Logger LOG = LoggerFactory.getLogger(SpellCheckServiceImpl.class.getName());

    @Autowired
    private Trie trie;

    @Override
    public String check(String word)
    {
        String corrected = word;
        if (!trie.contains(word))
        {
            corrected = trie.bestMatch(word, SystemDefaultProperties.DEFAULT_MAX_WAIT_TIME);
        }
        return corrected;
    }

    @Override
    public String[] checkAll(String[] words)
    {
        String[] corrected = new String[words.length];
        for (int i = 0; i < words.length; i++)
        {
            corrected[i] = check(words[i]);
        }
        return corrected;
    }

}
