package hermes

class HermesProductController {

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
                        if(params.dept && params.dept != ''){
                            like("dept", '%' + params.dept + '%')
                        }
			if(params.productCode && params.productCode != ''){
                            like("productCode", '%' + params.productCode + '%')
                        }
                        if(params.material && params.material != ''){
                            //like("material", '%' + params.material + '%')
							and{
								category{
									or{
									   like("description", '%' + params.material + '%')
									   like("description2", '%' + params.material + '%')
									}
									}
								
								}
                        }
                        if(params.magnitude && params.magnitude != ''){
                            like("magnitude", '%' + params.magnitude + '%')
                        }
                        if(params.styleCode && params.styleCode != ''){
                            like("styleCode",'%' + params.styleCode + '%')
                        }
                        if(params.colorCode && params.colorCode != ''){
                            like("colorCode", '%' + params.colorCode + '%')
                        }
			if(params.colorName && params.colorName != ''){
                            like("colorName", '%' + params.colorName + '%')
                        }
                        if(params.sizeCode && params.sizeCode != ''){
                            like("sizeCode", '%' + params.sizeCode + '%')
                        }
                        if(params.sizeName && params.sizeName != ''){
                            like("sizeName", '%' + params.sizeName + '%')
                        }                        
                    }
                    
		}
    
     def total = HermesProduct.createCriteria().count(query)  
     def results = HermesProduct.createCriteria().list(params, query)
	 //  def product=HermesProduct.get(3)
	 //  product.category.description
//	 results.each{elem->
//		  elem.category
		 
	 

		return [count:total,hermesProductInstanceList:results]   
    } 
   
  
   
   

    def listold = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [hermesProductInstanceList: HermesProduct.list(params), hermesProductInstanceTotal: HermesProduct.count()]
    }

    def create = {
        def hermesProductInstance = new HermesProduct()
        hermesProductInstance.properties = params
        return [hermesProductInstance: hermesProductInstance]
    }

    def save = {
        def hermesProductInstance = new HermesProduct(params)
        if (hermesProductInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), hermesProductInstance.id])}"
            redirect(action: "show", id: hermesProductInstance.id)
        }
        else {
            render(view: "create", model: [hermesProductInstance: hermesProductInstance])
        }
    }

    def show = {
        def hermesProductInstance = HermesProduct.get(params.id)
        if (!hermesProductInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), params.id])}"
            redirect(action: "list")
        }
        else {
            [hermesProductInstance: hermesProductInstance]
        }
    }

    def edit = {
        def hermesProductInstance = HermesProduct.get(params.id)
        if (!hermesProductInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [hermesProductInstance: hermesProductInstance]
        }
    }

    def update = {
        def hermesProductInstance = HermesProduct.get(params.id)
        if (hermesProductInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (hermesProductInstance.version > version) {
                    
                    hermesProductInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'hermesProduct.label', default: 'HermesProduct')] as Object[], "Another user has updated this HermesProduct while you were editing")
                    render(view: "edit", model: [hermesProductInstance: hermesProductInstance])
                    return
                }
            }
            hermesProductInstance.properties = params
            if (!hermesProductInstance.hasErrors() && hermesProductInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), hermesProductInstance.id])}"
                redirect(action: "show", id: hermesProductInstance.id)
            }
            else {
                render(view: "edit", model: [hermesProductInstance: hermesProductInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def hermesProductInstance = HermesProduct.get(params.id)
        if (hermesProductInstance) {
            try {
                hermesProductInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesProduct.label', default: 'HermesProduct'), params.id])}"
            redirect(action: "list")
        }
    }
}
