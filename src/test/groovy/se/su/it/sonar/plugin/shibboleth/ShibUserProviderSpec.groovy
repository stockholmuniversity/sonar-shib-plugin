package se.su.it.sonar.plugin.shibboleth

import org.sonar.api.security.ExternalUsersProvider
import org.sonar.api.security.UserDetails
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

public class ShibUserProviderSpec extends Specification {

  def "Test that doGetUserDetails gets the correct attribute from the request"() {
    given:
    def request = Mock(HttpServletRequest)
    def userDetails = new UserDetails()
    def context = new ExternalUsersProvider.Context("", request)

    request.getAttribute(ShibValidationFilter.USER_ATTRIBUTE) >> userDetails

    when:
    def ret = new ShibUserProvider().doGetUserDetails(context)

    then:
    assert ret == userDetails
  }
}
