Sonar Shibboleth plugin
=======================

Simple Shibboleth plugin for [Sonar](http://www.sonarsource.org/), implemented from the limited needs of Stockholm University.

The implementation is based on the [Sonar OpenID Plugin](http://docs.codehaus.org/display/SONAR/OpenID+Plugin).

## Installation

1. Compile the plugin with maven
2. Put it into the _SONARQUBE_HOME/extensions/plugins_ directory
3. Restart the SonarQube server

## Usage

The following properties must be added to _SONARQUBE_HOME/conf/sonar.properties_:

```
#------------------------
# Sonar Shibboleth Plugin
#------------------------

# Automatically create users
# Default is false.
sonar.authenticator.createUsers: true

# set security realm to use shibboleth
sonar.security.realm=shibboleth

# session initialisation url
sonar.shibboleth.sessionInitializer=/shibboleth/validate
```

