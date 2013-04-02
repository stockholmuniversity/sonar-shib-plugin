package se.su.it.sonar.plugin.shibboleth;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.sonar.api.ExtensionProvider;
import org.sonar.api.ServerExtension;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.Settings;

import java.util.List;

public final class SonarShibPlugin extends SonarPlugin {

  public List getExtensions() {
    return ImmutableList.of(Extensions.class);
  }

  public static final class Extensions extends ExtensionProvider implements ServerExtension {
    private Settings settings;

    public Extensions(Settings settings) {
      this.settings = settings;
    }

    @Override
    public Object provide() {
      List<Class> extensions = Lists.newArrayList();

      if (isRealmEnabled()) {
        Preconditions.checkState(settings.getBoolean("sonar.authenticator.createUsers"), "Property sonar.authenticator.createUsers must be set to true.");
        extensions.add(ShibSecurityRealm.class);
        extensions.add(ShibAuthenticator.class);
        extensions.add(ShibValidationFilter.class);
        extensions.add(ShibAuthenticationFilter.class);
        extensions.add(ShibLogoutFilter.class);
      }

      return extensions;
    }

    private boolean isRealmEnabled() {
      return ShibSecurityRealm.KEY.equalsIgnoreCase(settings.getString("sonar.security.realm"));
    }
  }
}
