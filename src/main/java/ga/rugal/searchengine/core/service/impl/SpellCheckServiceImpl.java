package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.searchengine.common.CommonLogContent;
import ga.rugal.searchengine.core.service.SpellCheckService;
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
        String corrected = null;
        if (!trie.contains(word))
        {
            corrected = trie.bestMatch(word, SystemDefaultProperties.DEFAULT_MAX_WAIT_TIME);
            LOG.debug(CommonLogContent.CORRECT_WORD, word, corrected);
        }
        return corrected;
    }

    @Override
    public String[] checkAll(String[] words)
    {
        String[] corrected = new String[words.length];
        boolean anyCorrected = false;
        for (int i = 0; i < words.length; i++)
        {
            String c = check(words[i]);
            if (null != c)
            {
                //if correction needed, put the new word
                anyCorrected = true;
                corrected[i] = c;
            }
            else
            {
                //otherwise, still the same to avoid NPE
                corrected[i] = words[i];
            }
        }
        if (anyCorrected)
        {
            return corrected;
        }
        else
        {
            return null;
        }

    }

}
