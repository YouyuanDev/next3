package next
class Currency {
  String code
  String name
  String description

  Date dateCreated
  Date lastUpdated
  static constraints = {
    code(maxSize: 20, unique: true)
    name(maxSize: 50, nullable: false)

    description(maxSize: 500, nullable: true)
  }

}
