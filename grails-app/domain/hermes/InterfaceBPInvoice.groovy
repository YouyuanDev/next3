package hermes
class InterfaceBPInvoice {
  String customerDevIndex
  String customerShortCode
  String customerLongCode
  String customerName
  String customerCateCode
  String customerCateName
  String materialCode
  String materialName
  String familyCode
  String familyName
  String productCode
  String productName
  String modelCode
  String modelName
  String styleCode
  String skuCode
  String skuName
  String supplyChainModel
  String ean13
  String colorCode
  String colorName
  String sizeCode
  String sizeName
  String podium
  String bpCode
  String orderType
  String orderCode
  String invoiceDate
  String productPrice
  String retailPriceEUR
  String retailDiscountPrice
  String shipQty
  String shipGrossPrice
  String shipDiscountPrice
  String countryCode
  
  
  //Kurt Edited
  String longEnglishName		//英语品名
  String compositionEnglish  //材质
  String customsCode //关税号
  
  String status = 'open'
  Date dateCreated
  Date lastUpdated
  static constraints = {
    customerDevIndex(maxSize: 10, nullable: false)
    customerShortCode(maxSize: 20, nullable: false)
    customerLongCode(maxSize: 20, nullable: false)
    customerName(maxSize: 200, nullable: false)
    customerCateCode(maxSize: 20, nullable: false)
    customerCateName(maxSize: 200, nullable: false)
    materialCode(maxSize: 20, nullable: false)
    materialName(maxSize: 200, nullable: false)
    familyCode(maxSize: 20, nullable: false)
    familyName(maxSize: 200, nullable: false)
    productCode(maxSize: 20, nullable: false)
    productName(maxSize: 200, nullable: false)
    modelCode(maxSize: 20, nullable: false)
    modelName(maxSize: 200, nullable: false)
    styleCode(maxSize: 20, nullable: false)
    skuCode(maxSize: 20, nullable: false)
    skuName(maxSize: 200, nullable: false)
    supplyChainModel(maxSize: 20, nullable: false)
    ean13(maxSize: 20, nullable: false)
    colorCode(maxSize: 20, nullable: false)
    colorName(maxSize: 200, nullable: false)
    sizeCode(maxSize: 20, nullable: false)
    sizeName(maxSize: 200, nullable: false)
    podium(maxSize: 20, nullable: false)
    bpCode(maxSize: 20, nullable: false)
    orderType(maxSize: 20, nullable: false)
    orderCode(maxSize: 2000, nullable: false)
    invoiceDate(maxSize: 200, nullable: false)
    productPrice(maxSize: 200, nullable: false)
    retailPriceEUR(maxSize: 200, nullable: false)
    retailDiscountPrice(maxSize: 200, nullable: false)
    shipQty(maxSize: 20, nullable: false)
    shipGrossPrice(maxSize: 20, nullable: false)
    shipDiscountPrice(maxSize: 20, nullable: false)
	countryCode(maxSize: 20, nullable: false)
	customsCode(maxSize: 50, nullable: true)
  }
  static mapping = {
    table 'interface_bpinvoice'
  }
}
           