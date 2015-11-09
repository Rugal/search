package ga.rugal.searchengine.core.service.impl;

import config.SystemDefaultProperties;
import ga.rugal.searchengine.common.CommonLogContent;
import ga.rugal.searchengine.core.service.IndexingService;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author Rugal Bernstein
 */
@Service
public class IndexingServiceImpl implements IndexingService
{

    private static final Logger LOG = LoggerFactory.getLogger(IndexingServiceImpl.class.getName());

    @Autowired
    private IndexWriter writer;

    /**
     * {@inheritDoc }
     *
     * @throws java.io.IOException
     */
    @Override
    public void createIndex(File folderPath) throws IOException
    {
        LOG.debug(CommonLogContent.START_INDEXING);
        for (File file : folderPath.listFiles())
        {
            Document doc = new Document();
            String text = this.readFile(file);
            //adding content
            doc.add(new Field(SystemDefaultProperties.DEFAULT_CONTENT_NAME,
                              Jsoup.parse(text).text(),
                              TextField.TYPE_STORED));
            //adding file name
            doc.add(new Field(SystemDefaultProperties.DEFAULT_PATH_NAME,
                              file.getName(),
                              TextField.TYPE_STORED));
            writer.addDocument(doc);
            LOG.debug(CommonLogContent.FILE_INDEXED, file.getName());
        }
        LOG.debug(CommonLogContent.INDEXING_SUCCEEDED);
    }

    private String readFile(File file) throws IOException
    {
        byte[] encoded = FileCopyUtils.copyToByteArray(file);
        return new String(encoded, SystemDefaultProperties.ENCODING);
    }

}
