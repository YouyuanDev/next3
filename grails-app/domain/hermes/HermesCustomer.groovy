package hermes

import next.*

class HermesCustomer extends Party {
	String customerDevIndex
	String customerShortCode
	String customerLongCode
	String customerCateCode
	String customerCateName
	String xmag
	
	Date dateCreated
	Date lastUpdated
	String reInvoicePreCode = ''
	Integer curNum = 1
	String whId=''
	String locId=''
	String parentCompany=''
	
	static constraints = {
		customerLongCode(maxSize: 20, nullable: false)
		customerShortCode(maxSize: 20, nullable: false)
		customerDevIndex(maxSize: 20, nullable: false)
		customerCateCode(maxSize: 20, nullable: false)
		customerCateName(maxSize: 200, nullable: false)
		xmag(maxSize: 2, nullable: false)
		whId(maxSize: 200, nullable: false)
		locId(maxSize: 200, nullable: false)
		locId(maxSize: 100, nullable: true)
	}
	static mapping = {
	  table 'hermes_customer'
	}
}
