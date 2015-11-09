package config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This is servlet initializer class replace for web.xml file.
 *
 * @author Rugal Bernstein
 * @since 0.6
 */
public class ServletContainerInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException
    {
        servletContext.addFilter("characterEncodingFilter", characterEncodingFilter()).addMappingForUrlPatterns(null, true, "/*");
        servletContext.addFilter("hiddenHttpMethodFilter", hiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
        super.onStartup(servletContext);
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration)
    {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
    }

    /**
     * Common configuration.
     *
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class[]
        {
            ApplicationContext.class
        };
    }

    /**
     * SpringMVC configuration.
     *
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return new Class[]
        {
            SpringMVCApplicationContext.class
        };
    }

    @Override
    protected String[] getServletMappings()
    {
        return new String[]
        {
            "/"
        };
    }

    @Override
    protected String getServletName()
    {
        return "springmvc";
    }

    @Override
    protected boolean isAsyncSupported()
    {
        return true;
    }

    private Filter characterEncodingFilter()
    {
        CharacterEncodingFilter cef = new CharacterEncodingFilter();
        cef.setEncoding(SystemDefaultProperties.ENCODING);
        cef.setForceEncoding(true);
        return cef;
    }

    private Filter hiddenHttpMethodFilter()
    {
        return new HiddenHttpMethodFilter();
    }

}
