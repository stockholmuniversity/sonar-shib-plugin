package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.security.Authenticator;

import java.util.logging.Logger;

public class ShibAuthenticator extends Authenticator {

  static final Logger LOG = Logger.getLogger(ShibAuthenticationFilter.class.getName());

  @Override
  public boolean doAuthenticate(Context context) {
    Object username = context.getRequest().getAttribute(ShibValidationFilter.USER_ATTRIBUTE);

    LOG.info("doAuthenticate called for user " + username);

    return username != null;
  }
}
