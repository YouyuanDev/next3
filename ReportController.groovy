package hermes
import hermes.XlsExportService
import groovy.sql.Sql
class ReportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
 def dataSource 
    def index = {
        redirect(action: "report1", params: params)
    }
    def exportXls = {  
  //        def excludedProps = ['id', 'version']  
  //        def column = []  
  //        def titles = []  
  //        def outProperties = []  
  //        GrailsDomainClass domainClass = ConverterUtil.getDomainClass("Person");  
  //        if (domainClass != null) {  
  //            domainClass.persistentProperties.each {p ->  
  //                outProperties << p.name  
  //            };  
  //  
  //        }  
  //        outProperties.each {  
  //            column << "${it}"  
  //            titles << message(code: "person.${it}")  
  //        }  
          response.setHeader("Content-disposition", "attachment; filename=person.xls")  
          response.setContentType("application/vnd.ms-excel")  
  //        ServletOutputStream f = response.getOutputStream();  
          XlsExportService.xlsExport(response.outputStream, request,"InvoiceHeader", InvoiceHeader.list())  
  //        render(contextType:"application/vnd.ms-excel")  
    
    
      }  


     
      def report1 ={
	  
	    def sql = new Sql(dataSource)
	    
	    def query = "select customer.id as customerid, customer.wh_id as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.name as suppliercode ,product.product_code as sellierinv,category.code as reSellierinv, invoiceitem.amount as sellieramt, invoiceitem.quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceheader.code from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join category  on product.id =category.id inner join hermes_customer as customer on business.customer_id=customer.id inner join party as supplier on business.supplier_id=supplier.id"
	    def results = sql.rows(query)
	    if (results.size() > 0) {
	     results.each{  
			     
			 } 
		 [reportList:results]
	    }
    }
          def report2 ={

	  
	  
	    def sql = new Sql(dataSource)
	    
	    def query = "select  business.delivery_date  as month1 ,supplier.name as supplier ,invoiceheader.code as invoiceheadercode,product.product_code as stockno, category.code as shortref, product.dept as dep, product.magnitude,invoiceitem.amount as sellieramt, invoiceitem.quantity,invoiceitem.shipment_duty as duty,product.dept from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join category  on product.id =category.id inner join hermes_customer as customer on business.customer_id=customer.id inner join party as supplier on business.supplier_id=supplier.id where customer.id = "+params.id 
	    def results = sql.rows(query)
	    if (results.size() > 0) {
	     results.each{  
			     
			 } 
		 [reportList:results]
	    }
    }
    def report10 ={
	   
	    def sql = new Sql(dataSource);
	    
	    def query = "select max(amount),max(id) as id from hermes_invoiceitem"
	    def results = sql.rows(query)
	    if (results.size() > 0) {
	     results.each{  
			     println " ${it.id}"  
			 } 
	    }
    }
    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [invoiceHeaderInstanceList: InvoiceHeader.list(params), invoiceHeaderInstanceTotal: InvoiceHeader.count()]
    }

    def create = {
        def invoiceHeaderInstance = new InvoiceHeader()
        invoiceHeaderInstance.properties = params
        return [invoiceHeaderInstance: invoiceHeaderInstance]
    }

    def save = {
        def invoiceHeaderInstance = new InvoiceHeader(params)
        if (invoiceHeaderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), invoiceHeaderInstance.id])}"
            redirect(action: "show", id: invoiceHeaderInstance.id)
        }
        else {
            render(view: "create", model: [invoiceHeaderInstance: invoiceHeaderInstance])
        }
    }

    def show = {
        def invoiceHeaderInstance = InvoiceHeader.get(params.id)
        if (!invoiceHeaderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
            redirect(action: "list")
        }
        else {
            [invoiceHeaderInstance: invoiceHeaderInstance]
        }
    }

    def edit = {
        def invoiceHeaderInstance = InvoiceHeader.get(params.id)
        if (!invoiceHeaderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [invoiceHeaderInstance: invoiceHeaderInstance]
        }
    }

    def update = {
        def invoiceHeaderInstance = InvoiceHeader.get(params.id)
        if (invoiceHeaderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (invoiceHeaderInstance.version > version) {
                    
                    invoiceHeaderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'invoiceHeader.label', default: 'InvoiceHeader')] as Object[], "Another user has updated this InvoiceHeader while you were editing")
                    render(view: "edit", model: [invoiceHeaderInstance: invoiceHeaderInstance])
                    return
                }
            }
            invoiceHeaderInstance.properties = params
            if (!invoiceHeaderInstance.hasErrors() && invoiceHeaderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), invoiceHeaderInstance.id])}"
                redirect(action: "show", id: invoiceHeaderInstance.id)
            }
            else {
                render(view: "edit", model: [invoiceHeaderInstance: invoiceHeaderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def invoiceHeaderInstance = InvoiceHeader.get(params.id)
        if (invoiceHeaderInstance) {
            try {
                invoiceHeaderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
            redirect(action: "list")
        }
    }



}




