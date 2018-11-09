package hermes

import next.*

class CustomerAccount {
  String code
  String description
  static constraints = {
    code(maxSize: 50, unique: true)
  }
  static mapping = {
    table 'hermes_customeraccount'
  }
}
 