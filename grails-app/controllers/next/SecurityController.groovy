package next

import grails.converters.*

class SecurityController extends BaseController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
  def index = {
    redirect(action: "list", params: params)
  }

  def list = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    [securityInstanceList: Security.list(params), securityInstanceTotal: Security.count()]
  }
  def listjson = {
    params.max = Math.min(params.rows ? params.int('rows') : 20, 100)
    def instanceList = jqgridFilterService.jqgridFilter(params, Security, false)
    def pager = jqgridFilterService.jqgridFilter(params, Security, true)
    JqgridJSON jqgridJSON = new JqgridJSON(pager)
    def i = 1
    instanceList.each {
      def cell = new ArrayList()
      cell.add(it.id)
      cell.add(it.code)
      cell.add(it.name)
      cell.add(it.description)
      jqgridJSON.addRow(i, cell)
      i = i + 1
    }
    render jqgridJSON.json as JSON
  }

  def jqgridJSON() {

    def json = [:]
    json.page = pager.page
    json.total = pager.total
    json.records = pager.records
    def i = 1;
    def rows = new ArrayList()
    instanceList.each {
      def row = [:]
      def cell = new ArrayList()
      cell.add(it.id)
      cell.add(it.code)
      cell.add(it.name)
      cell.add(it.description)
      row.id = i
      row.cell = cell
      rows.add(row)
      i = i + 1
    }
    json.rows = rows
    render json as JSON
  }

  def create = {
    def securityInstance = new Security()
    securityInstance.properties = params
    return [securityInstance: securityInstance]
  }

  def save = {
    def securityInstance = new Security(params)
    if (securityInstance.save(flush: true)) {
      flash.message = "${message(code: 'default.created.message', args: [message(code: 'security.label', default: 'Security'), securityInstance.id])}"
      redirect(action: "show", id: securityInstance.id)
    }
    else {
      render(view: "create", model: [securityInstance: securityInstance])
    }
  }

  def show = {
    def securityInstance = Security.get(params.id)
    if (!securityInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'security.label', default: 'Security'), params.id])}"
      redirect(action: "list")
    }
    else {
      [securityInstance: securityInstance]
    }
  }

  def edit = {
    def securityInstance = Security.get(params.id)
    if (!securityInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'security.label', default: 'Security'), params.id])}"
      redirect(action: "list")
    }
    else {
      return [securityInstance: securityInstance]
    }
  }

  def update = {
    def securityInstance = Security.get(params.id)
    if (securityInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (securityInstance.version > version) {

          securityInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'security.label', default: 'Security')] as Object[], "Another user has updated this Security while you were editing")
          render(view: "edit", model: [securityInstance: securityInstance])
          return
        }
      }
      securityInstance.properties = params
      if (!securityInstance.hasErrors() && securityInstance.save(flush: true)) {
        flash.message = "${message(code: 'default.updated.message', args: [message(code: 'security.label', default: 'Security'), securityInstance.id])}"
        redirect(action: "show", id: securityInstance.id)
      }
      else {
        render(view: "edit", model: [securityInstance: securityInstance])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'security.label', default: 'Security'), params.id])}"
      redirect(action: "list")
    }
  }

  def delete = {
    def securityInstance = Security.get(params.id)
    if (securityInstance) {
      try {
        securityInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'security.label', default: 'Security'), params.id])}"
        redirect(action: "list")
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'security.label', default: 'Security'), params.id])}"
        redirect(action: "show", id: params.id)
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'security.label', default: 'Security'), params.id])}"
      redirect(action: "list")
    }
  }
}
