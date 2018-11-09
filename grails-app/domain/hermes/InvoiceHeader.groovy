package hermes

import next.*

class InvoiceHeader {
  String code
  String cusCode
  int recode
  //String fullReCode
  String reInvoice = ''
  Date invoiceDate
  
    
  String type
  String invoiceType	//add by qxd
  String description
  String podium

  String currency = 'EUR'
  BigDecimal totalQuantity = 0
  BigDecimal totalAmount = 0
  BigDecimal totalIssuance = 0
  BigDecimal totalFreight = 0
  BigDecimal totalDifference = 0
  //AP-HL的运费 ，由结算表L4决定  =L4
  BigDecimal totalAphlForThisContractCode = 0
  
  Date importDate
  Date exportDate
  
  /* coCitis*/
  Date coCitisImportDate 
  Date coCitisExportDate 
  Date coCitisSendingImportDate 
  
  /* BL input*/
  Integer qADuration=0
  Date dOExchangeDate
  Date dutyPaidDate
  Date inspectionDate
  String vatDutyAmount
  String directToStore
  Date warehouseInDate
  Integer rcvdNumersOfCartons=0
  String damagedCartonNo
  String shortagePcs
  Date rcvDOInstructionFromStoreDate
  Date requiredDeliveryByStoreDate
  
  String remark
  
  String contractCode
  
  /*logistic report*/
  Date receiveDate
  Date fromPariseDate
  Date arriveDate
  Date baoLongReceiveDate
  Date clearanceDate
  Date deliveryDate
  String invoiceYear
  
  String report1Depts=''
  BigDecimal report1Apbl=0 
  BigDecimal report1Bfdf=0
  
  
  
  Business business
  static hasMany = [invoiceItems: InvoiceItem]
  static constraints = {
    code(maxSize: 50, unique: true)
    reInvoice(maxSize: 50, nullable: false)
    cusCode(maxSize: 20, nullable: true)
    invoiceType(maxSize: 20, nullable: true)
    //recode(nullable: true)
    type(maxSize: 20, nullable: true)
    description(maxSize: 500, nullable: true)
    podium(maxSize: 50, nullable: false)
    currency(inList: ["EUR", "HKD"], nullable: false)
    invoiceDate(nullable: false)
    importDate(nullable: true)
    exportDate(nullable: true)
    receiveDate(nullable: true)
	coCitisImportDate(nullable: true)
	coCitisExportDate(nullable: true)
	coCitisSendingImportDate(nullable: true)
	remark(maxSize: 500, nullable: true)
	contractCode(maxSize: 50, nullable: true)
	
    fromPariseDate(nullable: true)
    arriveDate(nullable: true)
    clearanceDate(nullable: true)
    deliveryDate(nullable: true)
    invoiceYear(nullable: true)
    
	 dOExchangeDate(nullable: true)
	 dutyPaidDate(nullable: true)
	 inspectionDate(nullable: true)
	 warehouseInDate(nullable: true)
	 rcvDOInstructionFromStoreDate(nullable: true)
	 requiredDeliveryByStoreDate(nullable: true)
	 baoLongReceiveDate(nullable: true)
	 
	 qADuration(nullable: true)
	 rcvdNumersOfCartons(nullable: true)
	 vatDutyAmount(nullable: true)
	 
	directToStore(maxSize: 500, nullable: true)
	damagedCartonNo(maxSize: 500, nullable: true)
	shortagePcs(maxSize: 500, nullable: true)
	report1Depts(maxSize: 500, nullable: true)

    totalQuantity(scale: 4, nullable: false)
    totalAmount(scale: 4, nullable: false)
    totalIssuance(scale: 4, nullable: false)
    totalFreight(scale: 4, nullable: false)
    totalDifference(scale: 4, nullable: false)
	totalAphlForThisContractCode(scale: 2, nullable: false)
    business(nullable: false)
  }
  static mapping = {
    table 'hermes_invoiceheader'
	invoiceItems cascade: "all,delete-orphan"
  }
  def getReInvoiceCode() {
      return this.business.customer.reInvoicePreCode+invoiceYear+recode
  }
}
 