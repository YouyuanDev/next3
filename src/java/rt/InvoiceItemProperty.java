package rt;
import java.math.*;



public class InvoiceItemProperty implements Comparable<InvoiceItemProperty>{
	public String productName;   
    public String productEnglishName;
    public String productCategoryDescription;
    public String invoiceHeaderCode;
    public String productCode;
    public String country;
    public BigDecimal quantity;
    public BigDecimal amount;
    public String sortOrderCode;
    public String content;
    public String bpCode;
    public String dept;
    public String podium;
    
    public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getPodium() {
		return podium;
	}

	public void setPodium(String podium) {
		this.podium = podium;
	}

	public String getBpCode() {
		return bpCode;
	}

	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductEnglishName() {
		return productEnglishName;
	}

	public void setProductEnglishName(String productEnglishName) {
		this.productEnglishName = productEnglishName;
	}

	public String getProductCategoryDescription() {
		return productCategoryDescription;
	}

	public void setProductCategoryDescription(String productCategoryDescription) {
		this.productCategoryDescription = productCategoryDescription;
	}

	public String getInvoiceHeaderCode() {
		return invoiceHeaderCode;
	}

	public void setInvoiceHeaderCode(String invoiceHeaderCode) {
		this.invoiceHeaderCode = invoiceHeaderCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	 

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSortOrderCode() {
		return sortOrderCode;
	}

	public void setSortOrderCode(String sortOrderCode) {
		this.sortOrderCode = sortOrderCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int compareTo(InvoiceItemProperty arg0) {   
        return this.getSortOrderCode().compareTo(arg0.getSortOrderCode());   
     } 
}
