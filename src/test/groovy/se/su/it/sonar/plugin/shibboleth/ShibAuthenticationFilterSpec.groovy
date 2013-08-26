package se.su.it.sonar.plugin.shibboleth

import org.sonar.api.config.Settings
import spock.lang.Specification
import spock.lang.Unroll

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ShibAuthenticationFilterSpec extends Specification {

  def "Test that the constructor sets the settings"() {
    given:
    def settings = new Settings(properties: [foo: "bar"])

    when:
    def filter = new ShibAuthenticationFilter(settings)

    then:
    assert filter.@settings == settings
    assert filter.@settings.properties.foo == "bar"
  }

  def "Test that init don't throw exception"() {
    when:
    new ShibAuthenticationFilter(null).init(null)

    then:
    notThrown(Exception)
  }

  def "Test that destroy don't throw exception"() {
    when:
    new ShibAuthenticationFilter(null).destroy()

    then:
    notThrown(Exception)
  }

  def "Test that doGetPattern returns a UrlPattern for '/sessions/new'"() {
    when:
    def pattern = new ShibAuthenticationFilter(null).doGetPattern()

    then:
    pattern.url == '/sessions/new'
    pattern.matches '/sessions/new'
  }

  @Unroll
  def "Test that doFilter redirects to #redirect for #url"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)

    httpServletRequest.contextPath >> 'http://foo/'

    def settings = new Settings()
    settings.appendProperty(ShibAuthenticationFilter.@PROPERTY_SESSION_INITIALIZER, url)

    when:
    new ShibAuthenticationFilter(settings).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpServletResponse.sendRedirect(redirect)

    where:
    url                | redirect
    ''                 | 'http://foo/'
    'url'              | 'http://foo/url'
    'http://url'       | 'http://url'
    'http://bar/url'   | 'http://bar/url'
    'https://url'      | 'https://url'
    'https://bar/url'  | 'https://bar/url'
  }
}
