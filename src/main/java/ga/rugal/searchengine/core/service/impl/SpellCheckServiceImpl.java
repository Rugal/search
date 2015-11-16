package ga.rugal.searchengine.core.service.impl;

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
public class SpellCheckServiceImpl
{

    private static final Logger LOG = LoggerFactory.getLogger(SpellCheckServiceImpl.class.getName());

    @Autowired
    private Trie trie;

    public String check(String word)
    {
        return trie.bestMatch(word, SystemDefaultProperties.DEFAULT_MAX_WAIT_TIME);
    }

}
