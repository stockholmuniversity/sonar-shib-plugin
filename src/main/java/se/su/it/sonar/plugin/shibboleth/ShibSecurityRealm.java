package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;

import java.util.logging.Logger;

public class ShibSecurityRealm extends SecurityRealm {

  public static final String KEY = "shibboleth";
  static final Logger LOG = Logger.getLogger(ShibSecurityRealm.class.getName());

  @Override
  public Authenticator doGetAuthenticator() {
    return new ShibAuthenticator();
  }

  @Override
  public ExternalUsersProvider getUsersProvider() {
    return new ShibUserProvider();
  }

  @Override
  public String getName() {
    return KEY;
  }
}
