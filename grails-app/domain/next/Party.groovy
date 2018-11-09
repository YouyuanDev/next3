package next
class Party {
  String code;
  String name;
  String description;
  String type = 'Party';
  String status = 'Enable';
  static hasMany = [roles: Role]
  Date dateCreated
  Date lastUpdated

  static constraints = {
    code(maxSize: 50, unique: true)
    name(maxSize: 50, nullable: false)
    type(nullable: false)
    status(nullable: false)

    description(maxSize: 500, nullable: true)
  }
  static mapping = {
    tablePerHierarchy false
    version false
  }
}
