package next
class Category {
  String code
  String name
  String description
  String description2=''
  Category parent
  static hasMany = [childs: Category]

  Date dateCreated
  Date lastUpdated
  static constraints = {
    code(maxSize: 50, unique: true)
    name(maxSize: 200, nullable: false)

    description(maxSize: 500, nullable: true)
	description2(maxSize: 200, nullable: true)
    parent(nullable: true)
    childs(nullable: true)
  }

}
