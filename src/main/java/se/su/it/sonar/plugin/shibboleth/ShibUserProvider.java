package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.UserDetails;

import java.util.logging.Logger;

public class ShibUserProvider extends ExternalUsersProvider {

  static final Logger LOG = Logger.getLogger(ShibUserProvider.class.getName());

  @Override
  public UserDetails doGetUserDetails(Context context) {
    LOG.info("Getting user details");

    return (UserDetails) context.getRequest().getAttribute(ShibValidationFilter.USER_ATTRIBUTE);
  }
}
