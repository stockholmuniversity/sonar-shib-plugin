package se.su.it.sonar.plugin.shibboleth

import org.sonar.api.security.Authenticator
import org.sonar.api.security.UserDetails
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class ShibAuthenticatiorSpec extends Specification {

  def "Test that doAuthenticate gets the correct attribute from the request"() {
    given:
    def request = Mock(HttpServletRequest)
    def userDetails = new UserDetails()
    def context = new Authenticator.Context("", "", request)

    request.getAttribute(ShibValidationFilter.USER_ATTRIBUTE) >> userDetails

    when:
    def ret = new ShibAuthenticator().doAuthenticate(context)

    then:
    assert ret
  }
}
