package hermes
import next.*

class MagnItem {
  String code
  String refno
  String description
  String magn

  static constraints = {
    code(maxSize: 50, unique: true)
    refno(maxSize: 50, nullable: true)
    description(maxSize: 50, nullable: true)
    magn(maxSize: 50, nullable: true)
  }
  static mapping = {
    table 'hermes_magnitem'
  }
}