package config;

import ga.rugal.searchengine.common.CommonLogContent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.FileCopyUtils;

/**
 * Java based application context configuration class.
 * <p>
 * Including data source and transaction manager configuration.
 *
 * @author Rugal Bernstein
 * @since 0.2
 */
@Configuration
@PropertySource(
    {
        "classpath:search.properties"
    })
@ComponentScan(basePackageClasses = ga.rugal.searchengine.core.service.PackageInfo.class)
public class ApplicationContext
{

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class.getName());

    private static final String INDEX_PATH_NAME = "index.path";

    private static final String ORIGIN_FOLDER_PATH_NAME = "origin.path";

    @Autowired
    private Environment env;

    @Bean
    public Analyzer analyzer()
    {
        return new StandardAnalyzer();
    }

    @Bean
    public File originFolderPath()
    {
        return new File(env.getProperty(ORIGIN_FOLDER_PATH_NAME, SystemDefaultProperties.ORIGIN_FOLDER_PATH));
    }

    @Bean
    public File indexPath()
    {
        return new File(env.getProperty(INDEX_PATH_NAME, SystemDefaultProperties.INDEX_PATH));
    }

    @Autowired
    @Bean(destroyMethod = "close")
    public Directory directory(File indexPath, Analyzer analyzer) throws IOException
    {
        if (indexPath.exists())
        {
            LOG.info(CommonLogContent.DELETE_OBSOLETE_INDEX);
            FileUtils.deleteDirectory(indexPath);
        }
        Directory directory = FSDirectory.open(Paths.get(indexPath.getPath()));
        //Create index once directory initialized
        try (IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer)))
        {
            this.createIndex(writer, indexPath);
        }
        return directory;
    }

    @Autowired
    @Bean(destroyMethod = "close")
    public DirectoryReader reader(Directory directory) throws IOException
    {
        DirectoryReader reader = DirectoryReader.open(directory);
        return reader;
    }

    @Autowired
    @Bean
    public IndexSearcher searcher(DirectoryReader reader) throws IOException
    {
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    @Bean
    @Autowired
    public QueryParser parser(Analyzer analyzer)
    {
        QueryParser parser = new QueryParser(SystemDefaultProperties.DEFAULT_CONTENT_NAME, analyzer);
        return parser;
    }

    @Bean
    public SimpleHTMLFormatter htmlFormatter()
    {
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        return htmlFormatter;
    }

    /**
     * Create Lucene index for given folder.<BR>
     * Parameter file must be a valid folder that contains some files to be indexed.
     *
     * @param folderPath path that files to be indexed
     *
     * @throws IOException Get this exception when unable to open files.
     */
    private void createIndex(IndexWriter writer, File folderPath) throws IOException
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

    /**
     * Read content of a file and store it into a string.
     *
     * @param file
     *
     * @return
     *
     * @throws IOException
     */
    private String readFile(File file) throws IOException
    {
        byte[] encoded = FileCopyUtils.copyToByteArray(file);
        return new String(encoded, SystemDefaultProperties.ENCODING);
    }
}
