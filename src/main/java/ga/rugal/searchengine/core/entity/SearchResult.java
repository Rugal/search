package ga.rugal.searchengine.core.entity;

/**
 *
 * @author Rugal Bernstein
 */
public class SearchResult
{

    private final String path;

    private final String fragment;

    public SearchResult(String path, String fragment)
    {
        this.path = path;
        this.fragment = fragment;
    }

    public String getPath()
    {
        return path;
    }

    public String getFragment()
    {
        return fragment;
    }

    @Override
    public String toString()
    {
        return "SearchResult{" + "path=" + path + ", fragment=" + fragment + '}';
    }

}
