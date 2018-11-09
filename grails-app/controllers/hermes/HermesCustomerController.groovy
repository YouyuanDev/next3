package hermes
import groovy.sql.Sql

class HermesCustomerController {
	def dataSource
	def importService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index = {
        redirect(action: "list", params: params)
    }
  
   def list = { 
	if(!params.offset)params.offset=0
	if(!params.max)params.max=10
	params.sort = params.sort ?: "code"
	def query = {
                    and{
                        if(params.code && params.code != ''){
                            like("code",'%' + params.code + '%')
                        }
                        if(params.customerShortCode && params.customerShortCode != ''){
                            like("customerShortCode", '%' + params.customerShortCode + '%')
                        }
			if(params.customerLongCode && params.customerLongCode != ''){
                            like("customerLongCode", '%' + params.customerLongCode + '%')
                        }
                        if(params.customerCateName && params.customerCateName != ''){
                            like("customerCateName", '%' + params.customerCateName + '%')
                        }
                        if(params.reInvoicePreCode && params.reInvoicePreCode != ''){
                            like("reInvoicePreCode", '%' + params.reInvoicePreCode + '%')
                        }
                        if(params.curNum && params.curNum != ''){
                            eq("curNum",  Integer.parseInt(params.curNum))
                        }
                    }
                    
		}
	def total = HermesCustomer.createCriteria().count(query)  
     def results = HermesCustomer.createCriteria().list(params, query)  
		return [count:total,hermesCustomerInstanceList:results]  

           
	 
    } 
    def listold = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [hermesCustomerInstanceList: HermesCustomer.list(params), hermesCustomerInstanceTotal: HermesCustomer.count()]
    }

    def create = {
        def hermesCustomerInstance = new HermesCustomer()
        hermesCustomerInstance.properties = params
        return [hermesCustomerInstance: hermesCustomerInstance]
    }

    def save = {
        def hermesCustomerInstance = new HermesCustomer(params)
		def sql = new Sql(dataSource);
		if (hermesCustomerInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), hermesCustomerInstance.id])}"
            redirect(action: "show", id: hermesCustomerInstance.id)
        } 
        else {
            render(view: "create", model: [hermesCustomerInstance: hermesCustomerInstance])
        }
    }
	
	
	def saveMyCustomer={
		log.info params
		
		
		def hermesCustomerInstance = new HermesCustomer(params)
		hermesCustomerInstance.code=hermesCustomerInstance.customerLongCode
        
		if(importService.insertHermesCustomerInfo(hermesCustomerInstance)){
			  
			def custaccount=new CustomerAccount()
			custaccount.code=hermesCustomerInstance.customerDevIndex
			custaccount.description=hermesCustomerInstance.reInvoicePreCode
			custaccount.save(flush:true)
				 redirect(action: "show", id: hermesCustomerInstance.id)
			 
			  
		}
		else {
			render(view: "create", model: [hermesCustomerInstance: hermesCustomerInstance])
		}
		
	}

	 
	
	
	
	
	
    def show = {
        def hermesCustomerInstance = HermesCustomer.get(params.id)
        if (!hermesCustomerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), params.id])}"
            redirect(action: "list")
        }
        else {
            [hermesCustomerInstance: hermesCustomerInstance]
        }
    }

    def edit = {
        def hermesCustomerInstance = HermesCustomer.get(params.id)
        if (!hermesCustomerInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [hermesCustomerInstance: hermesCustomerInstance]
        }
    }

    def update = {
		//System.err.println("123")
		System.err.println(params)
        def hermesCustomerInstance = HermesCustomer.get(params.id)
		System.err.println(hermesCustomerInstance.customerDevIndex)
        if (hermesCustomerInstance) {
			 
			def custaccount=  CustomerAccount.findByCode(hermesCustomerInstance.customerDevIndex)
			if(custaccount)
			{
			custaccount.code=params.customerDevIndex
			custaccount.description=params.reInvoicePreCode
			custaccount.save(flush:true)
			}
			 
            if (params.version) {
                def version = params.version.toLong()
                if (hermesCustomerInstance.version > version) {
                    
                    hermesCustomerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'hermesCustomer.label', default: 'HermesCustomer')] as Object[], "Another user has updated this HermesCustomer while you were editing")
                    render(view: "edit", model: [hermesCustomerInstance: hermesCustomerInstance])
                    return
                }
            }
            hermesCustomerInstance.properties = params
            if (!hermesCustomerInstance.hasErrors() && hermesCustomerInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), hermesCustomerInstance.id])}"
                redirect(action: "show", id: hermesCustomerInstance.id)
            }
            else {
                render(view: "edit", model: [hermesCustomerInstance: hermesCustomerInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def hermesCustomerInstance = HermesCustomer.get(params.id)
        if (hermesCustomerInstance) {
            try {
				def custaccount=  CustomerAccount.findByCode(hermesCustomerInstance.customerDevIndex)
				if(custaccount)
				custaccount.delete(flush: true)
                hermesCustomerInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesCustomer.label', default: 'HermesCustomer'), params.id])}"
            redirect(action: "list")
        }
    }
}
