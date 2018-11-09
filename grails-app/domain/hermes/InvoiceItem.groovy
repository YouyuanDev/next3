package hermes

import next.*

class InvoiceItem {
	Product product
	String productName
	BigDecimal quantity = 0
	BigDecimal amount = 0
	String podium
	/* logistic sections */
	String country = '' //Jenny Sep.8 2011 required to edit to ''
	Integer packingSeq=0
	BigDecimal logsAdjAmount = 0
	BigDecimal logisticIssuance = 0
	BigDecimal logisticFreight = 0
	Date receiveDate //TODO:here ?

	/* finance sections */
	Integer shipmentType = 0
	BigDecimal shipmentDuty = 0
	BigDecimal shipmentYZ = 0
	BigDecimal shipmentDL = 0
	BigDecimal shipmentCost = 0
	BigDecimal shipmentBFDF = 0
	BigDecimal finiAdjAmount
	String magitudem=''
	String poCode=''
	//TODO: setting to application config parameter
	static BigDecimal rate=1
	static BigDecimal dutyDiff=0.5 

	InvoiceHeader invoiceHeader
	static constraints = {
		invoiceHeader(nullable: false)
		product(nullable: false)
		productName(maxSize: 200, nullable: true)
		poCode(maxSize: 50, nullable: false)
		podium(maxSize: 50, nullable: true)
		amount(scale: 2, nullable: false)
		quantity(scale: 2, nullable: false)
		logsAdjAmount(scale: 2, nullable: true)
		logisticIssuance(scale: 2, nullable: false)
		logisticFreight(scale: 2, nullable: false)
		shipmentDuty(scale: 2, nullable: false)
		shipmentYZ(scale: 2, nullable: false)
		shipmentDL(scale: 2, nullable: false)
		shipmentCost(scale: 2, nullable: false)
		shipmentBFDF(scale: 2, nullable: true)
		finiAdjAmount(scale: 2, nullable: true)
		magitudem(nullable:true)
		receiveDate(nullable: true)
	}
	static mapping = {
	  table 'hermes_invoiceitem'
	}

	def getLogisticTotal(){
	    return getLogisticAmount()+logisticIssuance+logisticFreight
	}
	def getLogisticRateTotal(rate){
	    return getLogisticTotal()*rate
	}
	def getLogisticRateAmount(){
	    return getLogisticAmount()*rate
	}
	def getLogisticUnit(){
		return getLogisticTotal()/quantity
	}
	
	/* logistic price methods */
	def getLogisticAmount(){
	    if(logsAdjAmount!=null && logsAdjAmount!=0){
			//return logsAdjAmount
	       //return logsAdjAmount.setScale(2, BigDecimal.ROUND_HALF_UP)
			return amount.setScale(2, BigDecimal.ROUND_HALF_UP)
	    }else{
	        return amount.setScale(2, BigDecimal.ROUND_HALF_UP)
	    }
	}
	
	def getAdjLogisticAmount(){
		 
			return logsAdjAmount.setScale(2, BigDecimal.ROUND_HALF_UP)
		 
	}
	
	def getAdjLogisticAmountTotal(){
		return getAdjLogisticAmount()*quantity+logisticIssuance+logisticFreight
	}
	
	def getLogisticAmountTotal(){
		return getLogisticAmount()*quantity+logisticIssuance+logisticFreight
	}
	/* shipment price methods */
	def getShipmentAmt(rate){
		return (getLogisticAmountTotal()*rate).setScale(2, BigDecimal.ROUND_HALF_UP)
	}
	def getAdjShipmentAmt(rate){
		return (getAdjLogisticAmountTotal()*rate).setScale(2, BigDecimal.ROUND_HALF_UP)
	}
	
	def getShipmentUP(rate){
		def result = getShipmentAmt(rate)
		if(shipmentDuty) result = result + shipmentDuty
		if(shipmentYZ) result = result + shipmentYZ
		if(shipmentDL) result = result + shipmentDL
	    return result.setScale(2, BigDecimal.ROUND_HALF_UP)
	}
	
	def getAdjShipmentUP(rate){
		def result = getAdjShipmentAmt(rate)
		if(shipmentDuty) result = result + shipmentDuty
		if(shipmentYZ) result = result + shipmentYZ
		if(shipmentDL) result = result + shipmentDL
		return result.setScale(2, BigDecimal.ROUND_HALF_UP)
	}
	
	def getShipmentUPS(rate){
		return (getShipmentUP(rate)/quantity).setScale(4, BigDecimal.ROUND_HALF_UP)
	}
	
	def getAdjShipmentUPS(rate){
		return (getAdjShipmentUP(rate)/quantity).setScale(4, BigDecimal.ROUND_HALF_UP)
	}
	
	def getShipmentDiff(rate){
		if(finiAdjAmount){
			return (getShipmentUPS(rate)-shipmentCost+finiAdjAmount).setScale(2, BigDecimal.ROUND_HALF_UP)
		}else{
			return (getShipmentUPS(rate)-shipmentCost).setScale(2, BigDecimal.ROUND_HALF_UP)
		}
	}
	def getShipmentDuty(rate){
		return ((shipmentDuty/getShipmentAmt(rate))*100).setScale(2,BigDecimal.ROUND_HALF_UP)
	}
	
	
	
	/* todo delete methods */
	def getShipmentTotal(rate){
		def result = getLogisticRateTotal(rate)
		if(shipmentDuty) result = result + shipmentDuty
		if(shipmentYZ) result = result + shipmentYZ
		if(shipmentDL) result = result + shipmentDL
	    return result
	}
	def getShipmentUnit(rate){
	    //return getShipmentTotal(rate)/quantity
		if(!rate) rate = 1
		return getLogisticAmount()*rate
	}
	def getFinanceAmount(){
	    if(finiAdjAmount!=0){
	        return finiAdjAmount
	    }else{
	        return amount
	    }
	}
	def getFinanceRateAmount(){
	    return getFinanceAmount()*rate
	}
}
