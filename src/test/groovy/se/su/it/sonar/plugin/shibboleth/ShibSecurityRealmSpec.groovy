package se.su.it.sonar.plugin.shibboleth

import spock.lang.Specification

class ShibSecurityRealmSpec extends Specification {

  def "Test that doGetAuthenticator returns a new ShibAuthenticator"() {
    expect:
    new ShibSecurityRealm().doGetAuthenticator().class == ShibAuthenticator
  }

  def "Test that getUsersProvider returns a new ShibUserProvider"() {
    expect:
    new ShibSecurityRealm().getUsersProvider().class == ShibUserProvider
  }

  def "Test that getName returns the correct key"() {
    expect:
    new ShibSecurityRealm().getName() == ShibSecurityRealm.KEY
  }
}
