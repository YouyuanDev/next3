package next

import grails.converters.*
import groovy.sql.Sql

class MenuController {
  def dataSource
  def index = { redirect(action: list, params: params) }

  // the delete, save and update actions only accept POST requests
  static allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

  def list = {
    log.info params
    params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
    [menuInstanceList: Menu.list(params), menuInstanceTotal: Menu.count()]
  }
  def listjson = {
    params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
    def menuInstanceList = Menu.list(params)
    def menuInstanceTotal = Menu.count()
    def json = [:]
    json.page = 0
    json.total_pages = 0
    json.count = 0

    def i = 1;
    def rows = new ArrayList()
    menuInstanceList = Menu.getAll()

    for (menuInstance in menuInstanceList) {
      log.info menuInstance
    }
    menuInstanceList.each {
      log.info it
      /*
             def row=[:]
             row.id=i
             def cell=[:]
             cell.code=it.code
             cell.type=it.type
             cell.name=it.name
             cell.description=it.description
             cell.parentcode=it.parent.code
             cell.parentname=it.parent.name
             cell.controller=it.grails_controller
             cell.action=it.action
             cell.param=it.param
             row.cell=cell
             rows.add(row)
             i=i+1
             */
    }
    json.rows = rows
    render json as JSON
  }

  def show = {
    def menuInstance = Menu.get(params.id)

    if (!menuInstance) {
      flash.message = "Menu not found with id ${params.id}"
      redirect(action: list)
    }
    else { return [menuInstance: menuInstance] }
  }

  def delete = {
    def menuInstance = Menu.get(params.id)
    if (menuInstance) {
      try {
        menuInstance.delete(flush: true)
        flash.message = "Menu ${params.id} deleted"
        redirect(action: list)
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "Menu ${params.id} could not be deleted"
        redirect(action: show, id: params.id)
      }
    }
    else {
      flash.message = "Menu not found with id ${params.id}"
      redirect(action: list)
    }
  }

  def edit = {
    log.info params
    def menuInstance = Menu.get(params.id)

    if (!menuInstance) {
      flash.message = "Menu not found with id ${params.id}"
      redirect(action: list)
    }
    else {
      return [menuInstance: menuInstance]
    }
  }

  def update = {
    def menuInstance = Menu.get(params.id)
    if (menuInstance) {
      if (params.version) {
        def version = params.version.toLong()
        if (menuInstance.version > version) {

          menuInstance.errors.rejectValue("version", "menu.optimistic.locking.failure", "Another user has updated this Menu while you were editing.")
          render(view: 'edit', model: [menuInstance: menuInstance])
          return
        }
      }
      menuInstance.properties = params
      if (!menuInstance.hasErrors() && menuInstance.save()) {
        flash.message = "Menu ${params.id} updated"
        redirect(action: show, id: menuInstance.id)
      }
      else {
        render(view: 'edit', model: [menuInstance: menuInstance])
      }
    }
    else {
      flash.message = "Menu not found with id ${params.id}"
      redirect(action: list)
    }
  }

  def create = {
    def menuInstance = new Menu()
    menuInstance.properties = params
    return ['menuInstance': menuInstance]
  }

  def save = {
    def menuInstance = new Menu(params)
    if (!menuInstance.hasErrors() && menuInstance.save()) {
      flash.message = "Menu ${menuInstance.id} created"
      redirect(action: show, id: menuInstance.id)
    }
    else {
      render(view: 'create', model: [menuInstance: menuInstance])
    }
  }

  def buildExtMenu = {
    def sql = new Sql(dataSource);
    def query = "SELECT u.name,p.code,p.name,p.grails_controller,p.grails_action,m.id,m.seq,m.cls,m.url FROM user_login u,user_login_security uls,security s,security_permission sp,permission p,menu m where u.id=uls.user_securitys_id and uls.security_id=s.id and sp.security_permissions_id = s.id and sp.permission_id=p.id and p.id=m.id and u.id=? order by m.seq "
    def results = sql.rows(query, [session.user.id]);
    def menusL1 = new ArrayList()
    for (result in results) {
      log.info result.name
      def menuL1 = [:]
      def parent = Menu.get(result.id)
      menuL1.id = parent.name
      menuL1.text = parent.name
      menuL1.url = parent.grails_controller + "/" + parent.grails_action
      menuL1.cls = parent.cls
      log.info parent.name + " " + parent.childs.size()
      if (parent.childs.size() > 0) {
        def childmenus = Menu.findAll("from Menu as m where m.parent=? order by m.seq ", [parent])
        def menusL2 = new ArrayList()
        for (child in childmenus) {
          def menuL2 = [:]
          menuL2.id = child.name
          menuL2.text = child.name
          menuL2.url = child.grails_controller + "/" + child.grails_action
          menuL2.cls = parent.cls
          menuL2.leaf = true
          menusL2.add(menuL2)
        }
        menuL1.leaf = false
        menuL1.children = menusL2
      } else {
        menuL1.leaf = true
      }
      menusL1.add(menuL1)
    }
    render menusL1 as JSON
    //render(text:'test')
  }
  def buildExt3Menu = {
    def sql = new Sql(dataSource);
    //def query = "SELECT u.name,p.code,p.name,p.grails_controller,p.grails_action,m.id,m.seq,m.cls,m.url FROM user_login u,user_login_security uls,security s,security_permission sp,permission p,menu m where u.id=uls.user_securitys_id and uls.security_id=s.id and sp.security_permissions_id = s.id and sp.permission_id=p.id and p.id=m.id and u.id=? order by m.seq "
    def query = "SELECT m.id FROM user_login u,user_login_security uls,security s,security_permission sp,permission p,menu m where u.id=uls.user_securitys_id and uls.security_id=s.id and sp.security_permissions_id = s.id and sp.permission_id=p.id and p.id=m.id and u.id=? order by m.seq "
    def results = sql.rows(query, [session.user.id])
    def menus = new ArrayList()
	results.each{
	  menus.add(buildMenuItem(Menu.get(it.id)))
	}
    render menus as JSON
  }
	def buildMenuItem(menu){
		def menuItem = [:]
		menuItem.id = menu.name
		menuItem.text = menu.name
		menuItem.cls = menu.cls

		if (menu.childs.size() > 0) {
			def results = Menu.findAll("from Menu as m where m.parent=? order by m.seq ", [menu])
			def menus = []
			results.each{
				menus.add(buildMenuItem(it))
			}
			menuItem.url = null
			menuItem.leaf = false
			menuItem.children = menus
		}else{
			menuItem.url = menu.grails_controller + "/" + menu.grails_action
			menuItem.leaf = true
		}
		
		menuItem
	}

  def buildmenu = {
    def results = User.findAll("from User as u join u.roles as r join r.menus as m where u.id=? order by m.seq", [session.user.id])
    def items = new ArrayList()
    def childitems = []
    for (result in results) {
      def menu = result[2]
      menutreechilds(childitems, menu)
      def menup = [:]
      menup.name = menu.name
      menup.type = "category"
      menup.url = menu.url
      if (menu.childs.size() > 0) {
        def menuChilds = new ArrayList()
        for (child in menu.childs) {
          def menuc = [:]
          menuc._reference = child.name
          menuChilds.add(menuc)
        }
        menup.children = menuChilds
      }
      items.add(menup)
    }

    /*
         for(role in user.roles){
             for (menu in role.menus){
                 menutreechilds(childitems,menu)
                 def menup = [:]
                 menup.name = menu.name
                 menup.type = "category"
                 menup.url = menu.url
                 if(menu.childs.size()>0){
                     def menuChilds = new ArrayList()
                     for(child in menu.childs){
                         def menuc = [:]
                         menuc._reference = child.name
                         menuChilds.add(menuc)
                     }
                     menup.children=menuChilds
                 }
                 items.add(menup)
             }
         }
         */
    def childmenus = Menu.findAll("from Menu as m where m.id in (:ids) order by m.seq ", [ids: childitems])
    for (child in childmenus) {
      def menuc = [:]
      menuc.name = child.name
      menuc.url = child.url
      menuc.type = "poptart"
      items.add(menuc)
    }
    def mymenu = [:]
    mymenu.label = "name"
    mymenu.identifier = "name"
    mymenu.items = items
    render mymenu as JSON
  }

  def menutreechilds(items, menu) {
    if (menu.childs.size()) {
      for (child in menu.childs) {
        items.add(child.id)
        menutreechilds(items, child)
      }
    }
  }

  def menutree(menu) {
    def menuParent = [:]
    menuParent.name = menu.name
    if (menu.childs.size() > 0) {
      menuParent.type = "poptart"
    } else {
      menuParent.type = "category"
      def menuChilds = new ArrayList()
      for (child in menu.childs) {
        def menuChild = [:]
        menuChild.name = child.name;
        if (menuChild.childs.size() > 0) {
          menuChild.type = "poptart"
        } else {
          menuChild.type = "category"
          menutree(items, menuchild)
        }
        menuChilds.add(menuChild)
      }
      menuParent.children = menuChilds
    }
    return menuParent
  }

}
