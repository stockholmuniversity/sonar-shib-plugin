package se.su.it.sonar.plugin.shibboleth

import org.sonar.api.config.Settings
import spock.lang.Specification

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class ShibLogoutFilterSpec extends Specification {

  def "Test that the constructor sets the settings"() {
    given:
    def settings = new Settings(properties: [foo: "bar"])

    when:
    def filter = new ShibLogoutFilter(settings)

    then:
    assert filter.@settings == settings
    assert filter.@settings.properties.foo == "bar"
  }

  def "Test that init don't throw exception"() {
    given:
    def settings = new Settings()

    when:
    new ShibLogoutFilter(settings).init(null)

    then:
    notThrown(Exception)
  }

  def "Test that getLogoutUrl gets the url from the correct setting"() {
    given:
    def settings = new Settings()
    settings.appendProperty(ShibLogoutFilter.PROPERTY_LOGOUT_URL, "foo")

    when:
    def ret = new ShibLogoutFilter(settings).getLogoutUrl()

    then:
    ret == "foo"
  }

  def "Test that destroy don't throw exception"() {
    when:
    new ShibLogoutFilter(null).destroy()

    then:
    notThrown(Exception)
  }

  def "Test that doGetPattern returns a UrlPattern for '/sessions/logout'"() {
    when:
    def pattern = new ShibLogoutFilter(null).doGetPattern()

    then:
    pattern.url == '/sessions/logout'
    pattern.matches '/sessions/logout'
  }

  def "Test that doFilter invalidates session"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)
    def httpSession = Mock(HttpSession)
    def settings = new Settings()
    settings.appendProperty(ShibLogoutFilter.PROPERTY_LOGOUT_URL, 'foo')

    httpServletRequest.getSession(_) >> httpSession

    when:
    new ShibLogoutFilter(settings).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * httpSession.invalidate()
    1 * httpServletResponse.sendRedirect('foo')
  }

  def "Test that doFilter don't invalidate non existing session"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)
    def httpSession = Mock(HttpSession)
    def settings = new Settings()
    settings.appendProperty(ShibLogoutFilter.PROPERTY_LOGOUT_URL, 'foo')

    httpServletRequest.getSession(_) >> null

    when:
    new ShibLogoutFilter(settings).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    0 * httpSession.invalidate()
    0 * filterChain.doFilter(httpServletRequest, httpServletResponse)
    1 * httpServletResponse.sendRedirect('foo')
    notThrown(NullPointerException)
  }

  def "Test that doFilter continues in filterChain if no logout url"() {
    given:
    def httpServletRequest = Mock(HttpServletRequest)
    def httpServletResponse = Mock(HttpServletResponse)
    def filterChain = Mock(FilterChain)
    def settings = new Settings()

    when:
    new ShibLogoutFilter(settings).doFilter(httpServletRequest, httpServletResponse, filterChain)

    then:
    1 * filterChain.doFilter(httpServletRequest, httpServletResponse)
    0 * httpServletResponse.sendRedirect('foo')
    notThrown(NullPointerException)
  }
}
