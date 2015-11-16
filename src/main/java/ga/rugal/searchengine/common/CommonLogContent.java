package ga.rugal.searchengine.common;

/**
 *
 * @author Rugal Bernstein
 */
public interface CommonLogContent
{

    //Index
    final String FILE_INDEXED = "File [{}] has been indexed";

    final String START_INDEXING = "Start creating index";

    final String INDEXING_SUCCEEDED = "Index creation succeeded";

    final String DELETE_OBSOLETE_INDEX = "Deleting obsolete index";

    final String OPEN_FOLDER = "Opening folder [{}]";

    //Highlight
    final String HIGHLIGHTING = "Highlighting document [{}], scored [{}]";

    //Search
    final String EMPTY_QUERY = "User fired empty query string";

    final String ERROR_SEARCH = "Error while searching";

    //Trie
    final String ADD_DICTIONARY = "Caching dictionary [{}]";

    final String NO_FILE_FOUND = "Unable to open file [{}]";

    final String BUILD_TRIE = "Start to build Trie";

    final String SIZE_OF_TRIE = "The size of trie is [{}]";

}
