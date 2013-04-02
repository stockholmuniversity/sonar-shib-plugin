package se.su.it.sonar.plugin.shibboleth;

import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.web.ServletFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

public class ShibLogoutFilter extends ServletFilter {

  static final String PROPERTY_LOGOUT_URL = "sonar.shibboleth.logoutUrl";
  static final Logger LOG = Logger.getLogger(ShibLogoutFilter.class.getName());

  private final Settings settings;

  public ShibLogoutFilter(Settings settings) {
    this.settings = settings;
  }

  @Override
  public UrlPattern doGetPattern() {
    return UrlPattern.create("/sessions/logout");
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    String logoutUrl = getLogoutUrl();
    if (logoutUrl == null || logoutUrl.length() == 0) {
      LOG.info("No Shibboleth logout URL (" + PROPERTY_LOGOUT_URL + ")");
    } else {
      LOG.info("Shibboleth logout URL: " + logoutUrl);
    }
    LOG.info(this.getClass().getName() + " initialized.");
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    String logoutUrl = getLogoutUrl();

    LOG.info("Invalidating session");

    if (logoutUrl != null && logoutUrl.length() > 0) {
      HttpSession session = ((HttpServletRequest) request).getSession(false);
      if (session != null) {
        session.invalidate();
      }
      ((HttpServletResponse) response).sendRedirect(logoutUrl);
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private String getLogoutUrl() {
    return settings.getString(PROPERTY_LOGOUT_URL);
  }

  public void destroy() { }
}
