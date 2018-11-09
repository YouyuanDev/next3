package next
class Card {
  String code
  String description

  Person person
  Store store
  BigDecimal price = 0
  BigDecimal point = 0
  String status = 'Enable'

  Date dateCreated
  Date lastUpdated
  static constraints = {
    code(maxSize: 20, unique: true)
    price(scale: 2)
    point(scale: 2)

    person(nullable: true)
    store(nullable: true)
    description(maxSize: 500, nullable: true)
  }

}
