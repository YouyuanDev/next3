package next
class Permission {
  String code
  String name
  String description
  String type = 'Base'
  String grails_controller
  String grails_action
  String grails_param

  Date dateCreated
  Date lastUpdated

  static constraints = {
    code(maxSize: 20, unique: true)
    name(maxSize: 50, nullable: false)
    type(maxSize: 20, nullable: false)
    grails_controller(maxSize: 50, nullable: true)
    grails_action(maxSize: 50, nullable: true)
    grails_param(maxSize: 200, nullable: true)

    description(maxSize: 500, nullable: true)
  }
  static mapping = {
    tablePerHierarchy false
  }
}
