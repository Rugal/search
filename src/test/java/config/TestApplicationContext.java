package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java based application context configuration class.
 * <p>
 * Including data source and transaction manager configuration.
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
@Configuration
public class TestApplicationContext
{

    private static final Logger LOG = LoggerFactory.getLogger(TestApplicationContext.class.getName());

    @Bean
    public String[] keywords()
    {
        String[] keywords = new String[]
        {
            "web", "apache", "w3c"
        };
        return keywords;
    }

    @Bean
    public String wrongSpellingKeywords()
    {
        String keywords = "introductian servace string Standirds";
        return keywords;
    }
}
