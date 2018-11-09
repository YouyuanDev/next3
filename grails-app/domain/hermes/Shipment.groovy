package hermes

import next.*

class Shipment {
	String code
	String description

	BigDecimal blGS = 0
	BigDecimal blYZ = 0
	BigDecimal blDL = 0
	BigDecimal rate = 0
	BigDecimal bfdf = 0
	BigDecimal bal = 0
	BigDecimal totalAmount = 0
	
	Integer submit = 0
	Integer totalQuantity = 0
	
	BigDecimal totalBLGS = 0
	
	BigDecimal totalBLCost = 0
	BigDecimal totalHCost = 0
	
	BigDecimal totalDifference = 0
	BigDecimal totalAdjDiff = 0
	
	/* Todo delete files */
	BigDecimal totalDuty = 0
	BigDecimal totalCost = 0
	BigDecimal totalPrice = 0
	
	static hasMany = [business: Business]
	static constraints = {
		code(maxSize: 50, unique: true)
		business(nullable: false)
		blGS(scale: 2, nullable: true)
		blYZ(scale: 2, nullable: true)
		blDL(scale: 2, nullable: true)
		rate(scale: 4, nullable: true)
		bfdf(scale: 2, nullable: true)
		bal(scale: 2, nullable: true)
		totalAmount(scale: 2, nullable: true)

		totalDuty(scale: 2, nullable: true)
		totalCost(scale: 2, nullable: true)
		totalDifference(scale: 2, nullable: true)
		totalAdjDiff(scale: 2, nullable: true)
		totalPrice(scale: 2, nullable: true)
		business(nullable:true)
	}
	static mapping = {
	  table 'hermes_shipment'
	}

	def deleteShipmentItems(){
	    def message=''
	    business.each{
	        if('Y'.equals(it.toScala)){
	            message = message+' '+it.reInvoice+' toScala='+it.toScala+'</br>'
	        }
	    }
	    return message
	}
	def shipementItems(){
	  def reInvoices=[]
	  this.business.each{
	      it.invoiceHeaders.each{ih->
	          reInvoices.add(ih.getReInvoiceCode())
	      }
	  }
	  if(reInvoices.size()>0){
	      def results = InvoiceItem.findAll(" from InvoiceItem ii where ii.invoiceHeader in ? order by ii.product.dept,ii.product.magnitude,ii.duty ",[reInvoices])
	      return results
	  }else{
	      return []
	  }
	}
	def getAdjItem(){
	    def result = InvoiceItem.find(" max(ii.amount),max(ii.id) from InvoiceItem ii where ii.id in ? and quantity=1 ",[shipementItems()])
	    if(result){
	        return InvoiceItem.get(result[0].id)
	    }
	}
	def setAdjustItem(invoiceItem, price) {
	  if (invoiceItem) {
	    invoiceItem.finiAdjAmount = price
	    invoiceItem.save()
	    //proportionPrice()
	  }
	}
}
 