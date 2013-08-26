package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.config.Settings;
import org.sonar.api.web.ServletFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class ShibAuthenticationFilter extends ServletFilter {

  static final Logger LOG = Logger.getLogger(ShibAuthenticationFilter.class.getName());
  private static final String PROPERTY_SESSION_INITIALIZER = "sonar.shibboleth.sessionInitializer";

  private Settings settings;

  public ShibAuthenticationFilter(Settings settings) {
    this.settings = settings;
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.info(this.getClass().getName() + " initialized.");
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

    String url = settings.getString(PROPERTY_SESSION_INITIALIZER);

    if (url != null && !url.startsWith("http://") && !url.startsWith("https://"))
      url = httpServletRequest.getContextPath() + url;

    LOG.info("Redirecting to " + url);
    httpServletResponse.sendRedirect(url);
  }

  @Override
  public UrlPattern doGetPattern() {
    return UrlPattern.create("/sessions/new");
  }

  public void destroy() { }
}
