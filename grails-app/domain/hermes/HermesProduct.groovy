package hermes

import next.*

class HermesProduct extends Product {
  String dept = ''
  String material = ''
  String magnitude = ''
  
  String nameFr = ''
  String speciality = ''
  BigDecimal wholslPrice = 0
  BigDecimal retailPrice = 0
  String magnDept = ''
  
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
  String customsCode
  Date dateCreated
  Date lastUpdated
  static constraints = {
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
    material(maxSize: 200, nullable: true)
	nameFr(maxSize: 200, nullable: true)
	speciality(maxSize: 200, nullable: true)
	magnDept(maxSize: 200, nullable: true)
	wholslPrice(scale: 2, nullable: true)
	retailPrice(scale: 2, nullable: true)
	dept(maxSize: 200, nullable: true)
	customsCode(maxSize: 50, nullable: true)
	magnitude(maxSize: 200, nullable: true)
  
  }
  static mapping = {
    table 'hermes_product'
  }
}
