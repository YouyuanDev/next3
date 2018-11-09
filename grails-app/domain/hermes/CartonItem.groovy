package hermes

import next.*

class CartonItem {
  Carton carton
  InvoiceItem invoiceItem
  Integer quantity = 0
  Integer sequence = 1

  //Product product
  static constraints = {
    //product(nullable: false)
	//cartonItem(unique:['carton','invoiceItem','sequence'])
    carton(nullable: false,unique:['carton','invoiceItem','sequence'])
    invoiceItem(nullable:false)
	//quantity(blank:false)
	//sequence(blank:false)
  }
  static mapping = {
    table 'hermes_packingcarton_item'
  }
}
