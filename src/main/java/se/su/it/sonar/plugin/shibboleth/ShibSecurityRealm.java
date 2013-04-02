package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;

public class ShibSecurityRealm extends SecurityRealm {

  public static final String KEY = "shibboleth";

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
