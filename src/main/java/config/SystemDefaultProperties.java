package config;

/**
 *
 * Some system level properties.
 *
 * @author Rugal Bernstein
 */
public interface SystemDefaultProperties
{

    final String ORIGIN_FOLDER_PATH = "data";

    final String INDEX_PATH = "index";

    final String DICTIONARY_PATH = "dict";

    final String DEFAULT_CONTENT_NAME = "content";

    final String DEFAULT_PATH_NAME = "path";

    final String ENCODING = "UTF-8";

    final int DEFAULT_HIT_NUMBER = 6;

    final int DEFAULT_MAX_FRAGMENTS = 3;

    final int DEFAULT_MAX_WAIT_TIME = 50000;
}
