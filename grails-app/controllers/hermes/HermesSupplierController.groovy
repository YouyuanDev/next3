package hermes

class HermesSupplierController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [hermesSupplierInstanceList: HermesSupplier.list(params), hermesSupplierInstanceTotal: HermesSupplier.count()]
    }

    def create = {
        def hermesSupplierInstance = new HermesSupplier()
        hermesSupplierInstance.properties = params
        return [hermesSupplierInstance: hermesSupplierInstance]
    }

    def save = {
        def hermesSupplierInstance = new HermesSupplier(params)
        if (hermesSupplierInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), hermesSupplierInstance.id])}"
            redirect(action: "show", id: hermesSupplierInstance.id)
        }
        else {
            render(view: "create", model: [hermesSupplierInstance: hermesSupplierInstance])
        }
    }

    def show = {
        def hermesSupplierInstance = HermesSupplier.get(params.id)
        if (!hermesSupplierInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), params.id])}"
            redirect(action: "list")
        }
        else {
            [hermesSupplierInstance: hermesSupplierInstance]
        }
    }

    def edit = {
        def hermesSupplierInstance = HermesSupplier.get(params.id)
        if (!hermesSupplierInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [hermesSupplierInstance: hermesSupplierInstance]
        }
    }

    def update = {
        def hermesSupplierInstance = HermesSupplier.get(params.id)
        if (hermesSupplierInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (hermesSupplierInstance.version > version) {
                    
                    hermesSupplierInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'hermesSupplier.label', default: 'HermesSupplier')] as Object[], "Another user has updated this HermesSupplier while you were editing")
                    render(view: "edit", model: [hermesSupplierInstance: hermesSupplierInstance])
                    return
                }
            }
            hermesSupplierInstance.properties = params
            if (!hermesSupplierInstance.hasErrors() && hermesSupplierInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), hermesSupplierInstance.id])}"
                redirect(action: "show", id: hermesSupplierInstance.id)
            }
            else {
                render(view: "edit", model: [hermesSupplierInstance: hermesSupplierInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def hermesSupplierInstance = HermesSupplier.get(params.id)
        if (hermesSupplierInstance) {
            try {
                hermesSupplierInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'hermesSupplier.label', default: 'HermesSupplier'), params.id])}"
            redirect(action: "list")
        }
    }
}
