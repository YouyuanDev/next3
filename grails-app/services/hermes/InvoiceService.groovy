package hermes

import groovy.sql.Sql

class InvoiceService extends BaseService {
  	static transactional = true

  	def getAdjustItem() {
	    def sql = new Sql(dataSource);
	    //the unitprice max
	    def query = "select ii.id hermes_invoiceitem ii where ii.amount = (select max(amount) from hermes_invoiceitem where quantity=1)"
	    def results = sql.rows(query);
	    if (results.size() > 0) {
	      return InvoiceItem.get(results[0].id)
	    }
	    //the issuance+feight max
  	}
	def getNextCartonCode(packingId){
	    def sql = new Sql(dataSource);
	    //the unitprice max
	    def query = "select max(code) as code from hermes_packing_carton where packing_id="+packingId
	    def results = sql.rows(query);
	    if (results.size() > 0) {
				log.info results[0]
	      return results[0].code+1
	    }else{
				return 1;
			}
	}
	def logisticAdjustItem(item, price) {
		//set unitprice
		if (item) {
		  item.logsAdj = item.amount
		  item.amount = price
		  item.save()
		  proportionPrice()
		}
	}
	def financeAdjustItem(item, price) {
		//set unitprice
		if (item) {
		  item.finiAdj = item.amount
		  item.amount = price
		  item.save()
		  proportionPrice()
		}
	}
	
	def proportionPrice(invoice) {
	    //proportion the issuance and feight (need the logic)
	    def totalAmount = invoice.totalAmount
	    def totalIssuance = invoice.totalIssuance
	    def totalFeight = invoice.totalFeight
	
	    def proportionIssuance = 0
	    def proportionFeight = 0
	    invoice.invoiceItems.each {
	      def rate = it.amount / totalAmount
	      it.issuance = totalIssuance * (rate)
	      it.feight = totalFeight * (rate)
	      it.save(flush: true)
	      proportionIssuance = proportionIssuance + it.issuance
	      proportionFeight = proportionFeight + it.feight
	    }
	    invoice.totalDifference = (proportionIssuance + proportionFeight) - (totalIssuance + totalFeight)
	    invoice.save(flush: true)
  }
		//得到还没packing的business下拉列表
	def getNoPackingBusiness(){
		def sql = new Sql(dataSource);
	    def query = "select code from hermes_business where id not in (select business_id from hermes_packing)"
	    def results = sql.rows(query);	
	    return results
	}
}
