package next


class Country {
	String code;
	String name;
    static constraints = {
		code(maxSize: 10, unique: true)
		name(maxSize: 50, nullable: true)
    }
	static mapping = {
		table 'Country'
	  }
  
}
