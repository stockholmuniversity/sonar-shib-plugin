package se.su.it.sonar.plugin.shibboleth

import org.sonar.api.config.Settings
import spock.lang.Specification

class SonarShibPluginSpec extends Specification {

  def "Test that getExtensions returns a list of Extensions"() {
    expect:
    new SonarShibPlugin().extensions instanceof List
  }

  def "Test that Extensions constructor sets settings"() {
    given:
    def settings = new Settings(properties: [foo: "bar"])

    when:
    def ret = new SonarShibPlugin.Extensions(settings)

    then:
    assert ret.@settings == settings
    assert ret.@settings.properties.foo == "bar"
  }

  def "Test that Extensions.isRealmEnabled returns true if key matches plugin key"() {
    given:
    def settings = new Settings()
    settings.appendProperty(SonarShibPlugin.SECURITY_REALM_KEY, ShibSecurityRealm.KEY)

    when:
    def ret = new SonarShibPlugin.Extensions(settings).isRealmEnabled()

    then:
    assert ret
  }

  def "Test that Extensions.isRealmEnabled returns false if key don't matches plugin key"() {
    given:
    def settings = new Settings()
    settings.appendProperty(SonarShibPlugin.SECURITY_REALM_KEY, 'foo')

    when:
    def ret = new SonarShibPlugin.Extensions(settings).isRealmEnabled()

    then:
    assert !ret
  }

  def "Test that Extensions.provide returns empty list if realm is not enabled"() {
    given:
    def settings = new Settings()
    settings.appendProperty(SonarShibPlugin.SECURITY_REALM_KEY, 'foo')

    when:
    def ret = new SonarShibPlugin.Extensions(settings).provide()

    then:
    assert !ret
  }

  def "Test that Extensions.provide returns a list containing the correct extensions"() {
    given:
    List expected = [
            ShibSecurityRealm,
            ShibAuthenticator,
            ShibValidationFilter,
            ShibAuthenticationFilter,
            ShibLogoutFilter
    ]
    def settings = new Settings()
    settings.appendProperty(SonarShibPlugin.SECURITY_REALM_KEY, ShibSecurityRealm.KEY)
    settings.setProperty(SonarShibPlugin.SETTINGS_CREATE_USERS, true)

    when:
    def ret = new SonarShibPlugin.Extensions(settings).provide()

    then:
    assert ret == expected
  }

  def "Extensions.provide should throw exception if createUsers setting != true"() {
    given:
    def settings = new Settings()
    settings.appendProperty(SonarShibPlugin.SECURITY_REALM_KEY, ShibSecurityRealm.KEY)
    settings.setProperty(SonarShibPlugin.SETTINGS_CREATE_USERS, false)

    when:
    new SonarShibPlugin.Extensions(settings).provide()

    then:
    thrown(IllegalStateException)
  }
}
