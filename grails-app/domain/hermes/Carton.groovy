package hermes

import next.*

class Carton {
	Integer code
	String description1
	String description2

	String hardCopy=''
	BigDecimal nWeight=0
	BigDecimal gWeight=0
	
	Packing packing
	static hasMany = [cartonItems: CartonItem]

	static constraints = {
		code(unique: ['code', 'packing'])
		description1(maxSize: 500, nullable: false)
		description2(maxSize: 500, nullable: false)
		packing(nullable: false)
	}
	static mapping = {
		table 'hermes_packing_carton'
		cartonItems cascade: "all,delete-orphan"
	}
}
