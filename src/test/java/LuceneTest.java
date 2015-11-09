
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class LuceneTest
{

    private final String PATH = "../../dataset/W3C Web Pages/HTML";

    private final String INDEX = "../../dataset/Index";

    private final Analyzer analyzer = new StandardAnalyzer();

    private String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, "UTF-8");
    }

    @Test
    public void createIndex() throws IOException
    {
        try (Directory directory = FSDirectory.open(Paths.get(INDEX)))
        {
            try (IndexWriter iwriter = new IndexWriter(directory, new IndexWriterConfig(analyzer)))
            {
                System.out.println("Start");
                File folder = new File(PATH);
                for (File file : folder.listFiles())
                {
                    Document doc = new Document();
                    String text = this.readFile(file.getAbsolutePath());

                    doc.add(new Field("content", Jsoup.parse(text).text(), TextField.TYPE_STORED));
                    doc.add(new Field("path", file.getName(), TextField.TYPE_STORED));

                    iwriter.addDocument(doc);
                }
                System.out.println("Indexed");
            }
        }
    }

    @Test
    public void search() throws IOException, ParseException, InvalidTokenOffsetsException
    {
        try (Directory directory = FSDirectory.open(Paths.get(INDEX)))
        {
            // Now search the index:
            try (DirectoryReader ireader = DirectoryReader.open(directory))
            {
                // Parse a simple query that searches for "text":
                QueryParser parser = new QueryParser("content", analyzer);
                Query query = parser.parse("web");

                IndexSearcher isearcher = new IndexSearcher(ireader);

                ScoreDoc[] hits = isearcher.search(query, 1).scoreDocs;
                SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
                Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
                for (int i = 0; i < 1; i++)
                {
                    int id = hits[i].doc;
                    Document doc = isearcher.doc(id);
                    String text = doc.get("origin");
                    TokenStream tokenStream = analyzer.tokenStream("origin", text);
                    TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);
                    for (TextFragment frag1 : frag)
                    {
                        if ((frag1 != null) && (frag1.getScore() > 0))
                        {
                            System.out.println(frag1.toString());
                        }
                    }
                    //Term vector
//                    text = doc.get("content");
//                    tokenStream = TokenSources.getAnyTokenStream(isearcher.getIndexReader(), hits[i].doc, "content", analyzer);
//                    frag = highlighter.getBestTextFragments(tokenStream, text, false, 4);
//                    for (TextFragment frag1 : frag)
//                    {
//                        if ((frag1 != null) && (frag1.getScore() > 0))
//                        {
//                            System.out.println(frag1.toString());
//                        }
//                    }
                }

            }
        }

    }
}
