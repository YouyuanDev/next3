package next

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class RoleController {
  def exportService
  def filterService
  def index = { redirect(action: list, params: params) }

  // the delete, save and update actions only accept POST requests
  static allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

  def list = {

    params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
    if (params?.format && params.format != "html") {
      response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
      response.setHeader("Content-disposition", "attachment; filename=ExportFiles.${params.extension}")
      exportService.export(params.format, response.outputStream, Role.list(params), [:], [:])

      //List fields = ["author", "title"]
      //Map labels = ["author": "Author", "title": "Title"]
      // Formatter closure def upperCase = { value -> return value.toUpperCase() }
      //Map formatters = [author: upperCase]
      //Map parameters = [title: "Cool books"]
      //exportService.export(params.format, response.outputStream, Book.list(params), fields, labels, formatters, parameters)

    }

    [roleInstanceList: Role.list(params), roleInstanceTotal: Role.count()]

    //render(text:"hello")
  }

  def show = {
    def roleInstance = Role.get(params.id)

    if (!roleInstance) {
      flash.message = "Role not found with id ${params.id}"
      redirect(action: list)
    }
    else { return [roleInstance: roleInstance] }
  }

  def delete = {
    def roleInstance = Role.get(params.id)
    if (roleInstance) {
      try {
        roleInstance.delete(flush: true)
        flash.message = "Role ${params.id} deleted"
        redirect(action: list)
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "Role ${params.id} could not be deleted"
        redirect(action: show, id: params.id)
      }
    }
    else {
      flash.message = "Role not found with id ${params.id}"
      redirect(action: list)
    }
  }

  def edit = {
    def roleInstance = Role.get(params.id)

    if (!roleInstance) {
      flash.message = "Role not found with id ${params.id}"
      redirect(action: list)
    }
    else {
      return [roleInstance: roleInstance]
    }
  }

  def update = {
    def roleInstance = Role.get(params.id)
    if (roleInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (roleInstance.version > version) {

          roleInstance.errors.rejectValue("version", "role.optimistic.locking.failure", "Another user has updated this Role while you were editing.")
          render(view: 'edit', model: [roleInstance: roleInstance])
          return
        }
      }
      roleInstance.properties = params
      if (!roleInstance.hasErrors() && roleInstance.save()) {
        flash.message = "Role ${params.id} updated"
        redirect(action: show, id: roleInstance.id)
      }
      else {
        render(view: 'edit', model: [roleInstance: roleInstance])
      }
    }
    else {
      flash.message = "Role not found with id ${params.id}"
      redirect(action: list)
    }
  }

  def create = {
    def roleInstance = new Role()
    roleInstance.properties = params
    return ['roleInstance': roleInstance]
  }

  def save = {
    def roleInstance = new Role(params)
    if (!roleInstance.hasErrors() && roleInstance.save()) {
      flash.message = "Role ${roleInstance.id} created"
      redirect(action: show, id: roleInstance.id)
    }
    else {
      render(view: 'create', model: [roleInstance: roleInstance])
    }
  }

  def filter = {
    render(view: 'filter',
            model: [roleInstanceList: filterService.filter(params, Role),
                    roleInstanceTotal: filterService.count(params, Role),
                    filterParams: com.zeddware.grails.plugins.filterpane.FilterUtils.extractFilterParams(params),
                    params: params])
  }
}
