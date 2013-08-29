package se.su.it.sonar.plugin.shibboleth;

import org.sonar.api.security.ExternalGroupsProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShibGroupProvider extends ExternalGroupsProvider {
  @Override
  public Collection<String> doGetGroups(String username) {
    List<String> groups = new ArrayList<String>();
    groups.add("sonar-users");
    groups.add("sonar-administrators");
    return groups;
  }
}
