package rt;

import java.util.Date;

public class BusinessProperty {

	public String business_id;
	public String bp_code;
	public String reInvoice;
	public String PO;
	public String deliveryDate;
	public String to_scala;
	public String supplier;
	

	public String customer;
	public String WH;
	public String LoC;
	public String packingExport;
	public String packingValid;
	public String shipmentSubmit;
	
	
    public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String sortOrderCode;
    
    public String getSortOrderCode() {
		return sortOrderCode;
	}
	public void setSortOrderCode(String sortOrderCode) {
		this.sortOrderCode = sortOrderCode;
	}
	
 
	public String getBp_code() {
		return bp_code;
	}
	public void setBp_code(String bp_code) {
		this.bp_code = bp_code;
	}
	public String getReInvoice() {
		return reInvoice;
	}
	public void setReInvoice(String reInvoice) {
		this.reInvoice = reInvoice;
	}
	public String getPO() {
		return PO;
	}
	public void setPO(String pO) {
		PO = pO;
	}
 
	public String getTo_scala() {
		return to_scala;
	}
	public void setTo_scala(String to_scala) {
		this.to_scala = to_scala;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getWH() {
		return WH;
	}
	public void setWH(String wH) {
		WH = wH;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getPackingExport() {
		return packingExport;
	}
	public void setPackingExport(String packingExport) {
		this.packingExport = packingExport;
	}
	public String getPackingValid() {
		return packingValid;
	}
	public void setPackingValid(String packingValid) {
		this.packingValid = packingValid;
	}
	public String getLoC() {
		return LoC;
	}
	public void setLoC(String loC) {
		LoC = loC;
	}
 
	public String getShipmentSubmit() {
		return shipmentSubmit;
	}
	public void setShipmentSubmit(String shipmentSubmit) {
		this.shipmentSubmit = shipmentSubmit;
	}

	public int compareTo(BusinessProperty arg0) {   
        return this.getSortOrderCode().compareTo(arg0.getSortOrderCode());   
     } 
}
	
 
