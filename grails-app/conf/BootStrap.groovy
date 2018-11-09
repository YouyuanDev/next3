import grails.util.GrailsUtil
import org.grails.plugins.lookups.*
import next.*

class BootStrap {
  def grailsApplication
  static def doneInit = false
  def init = {servletContext ->
    switch (GrailsUtil.environment) {
      case "development":
        break
    }
    def timezone = grailsApplication.getConfig().next.timezone
    if (timezone) {
      TimeZone.setDefault(TimeZone.getTimeZone(timezone))
      log.info timezone
    }
    servletContext.appName = "next3"
    servletContext.appVersion = "1.0"

  }
  def destroy = {
  }

} 