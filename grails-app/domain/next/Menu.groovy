package next
class Menu extends Permission {
  String cls
  Integer seq
  String url
  Menu parent
  static hasMany = [childs: Menu]

  static constraints = {
    seq(nullable: false)

    cls(maxSize: 20, nullable: true)
    url(maxSize: 200, nullable: true)
    parent(nullable: true)
    childs(nullable: true)
  }
  def beforeInsert = {
    type = 'Menu'
  }
  def beforeUpdate = {
    type = 'Menu'
  }

}
