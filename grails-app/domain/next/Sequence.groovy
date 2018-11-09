package next


class Sequence {

    
	String name;
	Integer currentValue;
	Integer increment;
	String inYear;
    static constraints = {
		name(maxSize: 50, nullable: false)
		currentValue(nullable: false)
		increment(nullable: false)
		inYear(nullable: false)
    }
	static mapping = {
		table 'sequence'
	  }
}
