package hermes
import next.*

class Magn {
  String code
  String specialite
  String magndept
  Integer kb = 0
  
  static constraints = {
    code(maxSize: 50, unique: true)
    specialite(maxSize: 50, nullable: true)
    magndept(maxSize: 50, nullable: true)
    
  }
  
  static mapping = {
    table 'hermes_magn'
  }
}