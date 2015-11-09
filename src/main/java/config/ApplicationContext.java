package config;

import ga.rugal.searchengine.common.CommonLogContent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

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
    public Directory directory(File indexPath) throws IOException
    {
        if (indexPath.exists())
        {
            LOG.info(CommonLogContent.DELETE_OBSOLETE_INDEX);
            FileUtils.deleteDirectory(indexPath);

        }
        Directory directory = FSDirectory.open(Paths.get(indexPath.getPath()));
        return directory;
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

    @Autowired
    @Bean(destroyMethod = "close")
    public IndexWriter writer(Directory directory, Analyzer analyzer) throws IOException
    {
        return new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }
}
