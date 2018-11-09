package next

class VersionController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    [versionInstanceList: Version.list(params), versionInstanceTotal: Version.count()]
  }

  def create = {
    def versionInstance = new Version()
    versionInstance.properties = params
    return [versionInstance: versionInstance]
  }

  def save = {
    def versionInstance = new Version(params)
    if (versionInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'version.label', default: 'Version'), versionInstance.id])}"
      redirect(action: "show", id: versionInstance.id)
    }
    else {
      render(view: "create", model: [versionInstance: versionInstance])
    }
  }

  def show = {
    def versionInstance = Version.get(params.id)
    if (!versionInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'version.label', default: 'Version'), params.id])}"
      redirect(action: "list")
    }
    else {
      [versionInstance: versionInstance]
    }
  }

  def edit = {
    def versionInstance = Version.get(params.id)
    if (!versionInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'version.label', default: 'Version'), params.id])}"
      redirect(action: "list")
    }
    else {
      return [versionInstance: versionInstance]
    }
  }

  def update = {
    def versionInstance = Version.get(params.id)
    if (versionInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (versionInstance.version > version) {

          versionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'version.label', default: 'Version')] as Object[], "Another user has updated this Version while you were editing")
          render(view: "edit", model: [versionInstance: versionInstance])
          return
        }
      }
      versionInstance.properties = params
      if (!versionInstance.hasErrors() && versionInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'version.label', default: 'Version'), versionInstance.id])}"
        redirect(action: "show", id: versionInstance.id)
      }
      else {
        render(view: "edit", model: [versionInstance: versionInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'version.label', default: 'Version'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def versionInstance = Version.get(params.id)
    if (versionInstance) {
      try {
        versionInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'version.label', default: 'Version'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'version.label', default: 'Version'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'version.label', default: 'Version'), params.id])}"
      redirect(action: "list")
    }
  }
}
