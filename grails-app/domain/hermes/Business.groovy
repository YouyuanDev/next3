package hermes

import next.*

class Business {
	String code
	String description
	Party customer
	String currency = 'RMB'
	BigDecimal totalQuantity = 0
	BigDecimal totalAmount = 0
	
	/* logistic attribute */
	String reInvoice
	BigDecimal totalIssuance = 0
	BigDecimal totalFreight = 0
	BigDecimal totalDifference = 0
	BigDecimal totalAdjDiff = 0
	BigDecimal totalFinalamount = 0
	String hawb 
  	Boolean cocitis = false

	/* finance attribution */
	String poCode
	String soCode
	Date deliveryDate
	Party supplier
	String toScala = 'N'
	String inYear
	String cocitisRemark=''
	
	
	static hasMany = [invoiceHeaders: InvoiceHeader]
	static constraints = {
		code(maxSize: 20, unique: true)
		description(maxSize: 500, nullable: true)
		customer(nullable: true)
		totalQuantity(scale: 2, nullable: false)
		totalAmount(scale: 2, nullable: false)
		totalFinalamount(scale:2,nullable:false)
		
		reInvoice(maxSize: 50, nullable: true)
		totalIssuance(scale: 2, nullable: false)
		totalFreight(scale: 2, nullable: false)
		totalDifference(scale: 2, nullable: false)
		totalAdjDiff(scale: 2, nullable: false)
		hawb(nullable: true)
		inYear(nullable: true)
		cocitisRemark(nullable: true) //Kurt Edited
		invoiceHeaders(nullable: true)

		poCode(maxSize: 500, nullable: true)
		soCode(maxSize: 500, nullable: true)
		deliveryDate(nullable: true)
		supplier(nullable:true)
	}
	static mapping = {
		table 'hermes_business'
		invoiceHeaders cascade: "all,delete-orphan"
	}
	/*
	getReInvoiceCode(){
		def reinvoice = InvoiceHeader.withCriteria{
			projections{
				min('recode')
				max('recode')
			}
			eq('business',this)
		}
		return "("+reinvoice[0][0]+"-"+reinvoice[0][1]+")"		
	}
	*/
	/*
	getFinanceTotalPrice(){
		return 0
	}
	*/
}
 