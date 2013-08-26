package se.su.it.sonar.plugin.shibboleth

import org.sonar.api.config.Settings
import spock.lang.Specification

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.security.Principal

class ShibValidationFilterSpec extends Specification {

  def "Test that the constructor sets the settings"() {
    given:
    def settings = new Settings(properties: [foo: "bar"])

    when:
    def filter = new ShibValidationFilter(settings)

    then:
    assert filter.@settings == settings
    assert filter.@settings.properties.foo == "bar"
  }

  def "Test that init don't throw exception"() {
    when:
    new ShibValidationFilter(null).init(null)

    then:
    notThrown(Exception)
  }

  def "Test that destroy don't throw exception"() {
    when:
    new ShibValidationFilter(null).destroy()

    then:
    notThrown(Exception)
  }

  def "Test that doGetPattern returns a UrlPattern for '/shibboleth/validate'"() {
    when:
    def pattern = new ShibValidationFilter(null).doGetPattern()

    then:
    pattern.url == '/shibboleth/validate'
    pattern.matches '/shibboleth/validate'
  }

  def "Test that doFilter redirects to unauthorized url on no username"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    httpServletRequest.contextPath >> 'http://foo/'

    when:
    new ShibValidationFilter(null).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletResponse.sendRedirect('http://foo/' + '/shibboleth/unauthorized')
  }

  def "Test that doFilter tries to get username from the request"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    when:
    new ShibValidationFilter(null).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletRequest.remoteUser >> null
    1 * httpServletRequest.userPrincipal >> null
    1 * httpServletRequest.getHeader('REMOTE_USER') >> null
    1 * httpServletRequest.getAttribute('eppn') >> null
  }

  def "Test that doFilter sets the USER_ATTRIBUTE variable if a username is supplied via remote user"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    httpServletRequest.remoteUser >> 'user'

    when:
    new ShibValidationFilter(null).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletRequest.setAttribute(ShibValidationFilter.USER_ATTRIBUTE, _)
  }

  def "Test that doFilter sets the USER_ATTRIBUTE variable if a username is supplied via user principal"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    def principal = Mock(Principal)

    httpServletRequest.remoteUser >> principal
    principal.name >> 'user'

    when:
    new ShibValidationFilter(null).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletRequest.setAttribute(ShibValidationFilter.USER_ATTRIBUTE, _)
  }

  def "Test that doFilter sets the USER_ATTRIBUTE variable if a username is supplied via REMOTE_USER"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    httpServletRequest.getHeader('REMOTE_USER') >> 'user'

    when:
    new ShibValidationFilter(null).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletRequest.setAttribute(ShibValidationFilter.USER_ATTRIBUTE, _)
  }

  def "Test that doFilter sets the USER_ATTRIBUTE variable if a username is supplied via eppn"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    httpServletRequest.getAttribute('eppn') >> 'user'

    when:
    new ShibValidationFilter(null).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletRequest.setAttribute(ShibValidationFilter.USER_ATTRIBUTE, _)
  }
}
