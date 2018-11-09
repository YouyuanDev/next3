package next

import org.grails.plugins.lookups.*

class Store extends Party {
  String contract
  String address
  String postal
  String telephone
  String website
  String email
  static constraints = {
    contract(maxSize: 50, nullable: true)
    address(maxSize: 200, nullable: true)
    postal(maxSize: 20, nullable: true)
    telephone(maxSize: 50, nullable: true)
    website(maxSize: 50, nullable: true)
    email(maxSize: 50, nullable: true)
  }
}
