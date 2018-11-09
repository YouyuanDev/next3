package next
class Person extends Party {
  String address
  String postal
  String telephone
  String mobilephone
  String website
  String email
  Date birthday

  static hasMany = [users: User]

  public Person() {
    type = 'Person'
  }

  static constraints = {
    address(maxSize: 200, nullable: true)
    postal(maxSize: 20, nullable: true)
    telephone(maxSize: 50, nullable: true)
    mobilephone(maxSize: 50, nullable: true)
    website(maxSize: 50, nullable: true)
    email(maxSize: 50, nullable: true)
    birthday(nullable: true)
  }
}
