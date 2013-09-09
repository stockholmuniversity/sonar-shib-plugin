package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.UserDetails;

import java.util.logging.Logger;

public class ShibUserProvider extends ExternalUsersProvider {

  static final Logger LOG = Logger.getLogger(ShibUserProvider.class.getName());

  @Override
  public UserDetails doGetUserDetails(Context context) {
    UserDetails userDetails = (UserDetails) context.getRequest().getAttribute(ShibValidationFilter.USER_ATTRIBUTE);

    LOG.info("doGetUserDetails called for user " + (userDetails != null ? userDetails.getName() : null));

    return userDetails;
  }
}
