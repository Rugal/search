package ga;

import ga.rugal.trie.Trie;
import ga.rugal.trie.TrieImpl;
import java.io.UnsupportedEncodingException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
@Ignore
public class TestFile
{

    @Test
    public void test() throws UnsupportedEncodingException
    {
        Trie t = new TrieImpl(false);
        String s = "string";
        t.insert(s);
        Assert.assertTrue(t.contains(s));
    }
}
