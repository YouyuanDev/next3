package next
class Product {
  String code
  String name
  String description
  //String type='Goods'//Gifts

  String brand
  String year
  String color
  BigDecimal price = 0
  BigDecimal point = 0

  Category category

  Currency currency
  String status = 'Enable'

  Date dateCreated
  Date lastUpdated
  static constraints = {
    code(maxSize: 50, unique: true)
    name(maxSize: 200, nullable: false)
    //type(nullable:false)
    status(nullable: false)

    description(maxSize: 500, nullable: true)
    brand(maxSize: 500, nullable: true)
    year(maxSize: 500, nullable: true)
    color(maxSize: 500, nullable: true)
    price(scale: 2, nullable: true)
    point(scale: 2, nullable: true)
    currency(nullable: true)
    category(nullable: true)
  }
  static mapping = {
    tablePerHierarchy false
    version false
  }
}
