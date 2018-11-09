package hermes

import next.*

class BusinessItem {
	String code
	Product product
	
	BigDecimal quantity = 0
	BigDecimal amount = 0
	
	Business business
	static constraints = {
		code(nullable: false)
		amount(scale: 2, nullable: false)
		quantity(scale: 2, nullable: false)
		product(nullable: true)
	}
	static mapping = {
	  table 'hermes_businessitem'
	}
}
