package hermes

import next.*

class Packing {
	Business business
	String hardCopy
	String reinvoice
	Date date
	String description
	String export='N'
	Date exportDate
	String vaild='N'
	Date vaildDate
	static hasMany = [cartons: Carton]
	static constraints = {
		business(nullable: false, unique: true)
		hardCopy(maxSize: 200, nullable: false)
		reinvoice(maxSize: 200, nullable: true)
		date(nullable: false)
		exportDate(nullable: true)
		vaildDate(nullable: true)
		description(maxSize: 500, nullable: true)
	}
	static mapping = {
		table 'hermes_packing'
		cartons cascade: "all,delete-orphan"
		
	}
}
