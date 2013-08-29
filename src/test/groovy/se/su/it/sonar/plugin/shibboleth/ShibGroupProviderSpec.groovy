package se.su.it.sonar.plugin.shibboleth

import spock.lang.Specification

class ShibGroupProviderSpec extends Specification {

  def "Test that the correct groups are returned"() {
    when:
    def groups = new ShibGroupProvider().doGetGroups("")

    then:
    assert groups.contains("sonar-users")
    assert groups.contains("sonar-administrators")
  }
}
