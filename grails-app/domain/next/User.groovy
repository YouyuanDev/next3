package next
class User {
  String login
  String password
  String name
  String email
  String status = 'Enable'

  static hasMany = [securitys: Security]
  Date dateCreated
  Date lastUpdated

  static constraints = {
    login(maxSize: 50, matches: "[a-zA-Z]+", unique: true)
    password(maxSize: 20, nullable: false, password: true)
    name(maxSize: 20, nullable: false)
    email(maxSize: 50, nullable: true, email: true)
  }
  static mapping = {
    table 'user_login'
  }
}
           