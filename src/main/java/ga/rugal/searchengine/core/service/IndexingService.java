package ga.rugal.searchengine.core.service;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Rugal Bernstein
 */
public interface IndexingService
{

    /**
     * Create Lucene index for given folder.<BR>
     * Parameter file must be a valid folder that contains some files to be indexed.
     *
     * @param folderPath path that files to be indexed
     *
     * @throws IOException Get this exception when unable to open files.
     */
    void createIndex(File folderPath) throws IOException;

}
