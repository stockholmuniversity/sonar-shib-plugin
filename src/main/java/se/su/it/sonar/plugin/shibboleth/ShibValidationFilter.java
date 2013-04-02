package se.su.it.sonar.plugin.shibboleth;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Settings;
import org.sonar.api.security.UserDetails;
import org.sonar.api.web.ServletFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

public class ShibValidationFilter extends ServletFilter {

  static final Logger LOG = Logger.getLogger(ShibValidationFilter.class.getName());
  static final String USER_ATTRIBUTE = "shibboleth_user";

  private Settings settings;

  public ShibValidationFilter(Settings settings) {
    this.settings = settings;
  }

  @Override
  public UrlPattern doGetPattern() {
    return UrlPattern.create("/shibboleth/validate");
  }

  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.info(this.getClass().getName() + " initialized.");
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    LOG.info("In validation");
    UserDetails user = null;
    try {
      String username = httpRequest.getRemoteUser();
      if (username == null && httpRequest.getUserPrincipal() != null)
        username = httpRequest.getUserPrincipal().getName();
      if(username == null)
        username = httpRequest.getHeader("REMOTE_USER");
      if(username == null)
        username = httpRequest.getHeader("eppn");

      if (username != null) {
        user = new UserDetails();

        user.setName(username);
        user.setEmail((String) httpRequest.getAttribute("mail"));
        if (user.getEmail() == null)
          user.setEmail(httpRequest.getHeader("mail"));

        LOG.info("displayName: " + user.getName() + ", mail: " + user.getEmail());
      }
    } catch (RuntimeException e) {
      LOG.severe("Fail to verify shibboleth request" + e.getMessage());
      throw e;
    }
    if (user == null) {
      LOG.info("Unauthorized");
      httpResponse.sendRedirect(httpRequest.getContextPath() + "/shibboleth/unauthorized");
    } else {
      LOG.info("Authorized");
      request.setAttribute(USER_ATTRIBUTE, user);

      filterChain.doFilter(request, response);
    }
  }

  public void destroy() { }
}
