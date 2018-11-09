package hermes

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set
import java.text.*
import java.util.HashSet;
import org.hsqldb.lib.Set;
import rt.*;
import grails.converters.JSON

import next.BaseController
import next.JqgridJSON
import groovy.sql.Sql
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.core.io.ClassPathResource

class FinanceController extends BaseController {
	def dataSource                   
	def financeService
	def logisticService
	/* shipment section */
	def listShipment={
		//financeService.deleteUnSubmitShipment(session.user)
		financeService.deleteUnValidSubmittedShipment();
		render(view:"/hermes/finance/shipmentList")
	}
	def listShipmentsJson={
	    def instanceList = searchShipments(params,false)
	    //log.info instanceList.size()
		 def set = new HashSet();
		instanceList.each{ elem ->
			 	set.add(elem.id);
	    }
		//System.err.println("set.size()="+set.size())
		
		
	   // def pager = searchShipments(params,true)
		def pager=jqgridFilterService.afterPager(params,set.size())
	    JqgridJSON jqgridJSON = new JqgridJSON(pager)
		set.clear();
		def i=-1
	    instanceList.each{ elem->
			if(!set.contains(elem.id)){
			 i=i+1
				set.add(elem.id);
			def cell = new ArrayList()
			cell.add(elem.id)
			cell.add(elem.code)
			def bps=''
			elem.business.each{
				bps = bps+it.code+';'
			}
			cell.add(bps)
			cell.add(elem.blYZ)
			cell.add(elem.blDL)
			cell.add(elem.rate)
			cell.add(elem.bfdf)
			//Kurt edited
			//cell.add(elem.bal)
			cell.add(elem.totalBLGS)
			//END
			cell.add(elem.description)
			if(elem.submit==1)
			cell.add("Submitted")
			else
			cell.add("Not Submitted")
			jqgridJSON.addRow(i, cell)
			cell=null;
			}
		
	    }
		
	    render jqgridJSON.json as JSON
	}
	def searchShipments(params,doCount){
		//def code =  params['reInvoiceHidden']
		log.info params
		def code =  params['code']
		def para_reInvoice =  params['reInvoice']
		def store=params['store']
		def inYear=params.inYear
		def inMonth=params.inMonth
		
		if(inYear=='this year')
		inYear=null
		if(inMonth=='this month')
		inMonth=null
		//System.err.println("inYear="+inYear);
		//System.err.println("store="+store);
		Date searchBegindate=new Date();
		Date searchEnddate=new Date();
		//System.err.println(searchBegindate);
		if(inYear){
			searchBegindate.setYear (new Integer(inYear)-1900);
			searchEnddate.setYear (new Integer(inYear)-1900);
		}
		if(inMonth&&inMonth!='All'){
			 searchBegindate.setMonth(new Integer(inMonth)-1);
			 searchEnddate.setMonth(new Integer(inMonth)-1);
		 }
		
		searchBegindate.setDate(new Integer(1));
		searchBegindate.setHours(new Integer(0));
		searchBegindate.setMinutes (new Integer(0));
		searchBegindate.setSeconds (new Integer(0));
		
		searchEnddate.setMonth(searchBegindate.getMonth()+1);
		searchEnddate.setDate(new Integer(0));
		searchEnddate.setHours(new Integer(23));
		searchEnddate.setMinutes (new Integer(59));
		searchEnddate.setSeconds (new Integer(59));

		if(inMonth=='All'){
			searchBegindate.setMonth(0);
			searchEnddate.setMonth(12);
		}
		
		//System.err.println("searchBegindate:"+searchBegindate);
		//System.err.println("searchEnddate:"+searchEnddate);
		
		if(store==null){
			def res=getStoreList()
			store=res[0]
		}
		if(store&&store=='All')
		store=null;
		
		String s_reInvoice_min;
		if(para_reInvoice&&para_reInvoice!=''){
		 s_reInvoice_min=para_reInvoice
		 s_reInvoice_min=s_reInvoice_min.replace(',', '~')
		s_reInvoice_min=s_reInvoice_min.trim();
		if(s_reInvoice_min.indexOf('~')!=-1)
		s_reInvoice_min=s_reInvoice_min.substring(0,s_reInvoice_min.indexOf('~'));
		
		}
		//System.err.println("123")
		//System.err.println("code="+code)
		//System.err.println("para_reInvoice="+para_reInvoice)
		//System.err.println("s_reInvoice_min="+s_reInvoice_min)
		def criteriaClosure = {

			and{
			if(code) like("code",'%'+code+'%')	
			business{
				and{
				if(s_reInvoice_min&&s_reInvoice_min!=""){
				invoiceHeaders{
					and{
					eq("reInvoice",s_reInvoice_min)
					
					}
					
				}
				
				}
				invoiceHeaders{
				between('invoiceDate',searchBegindate,searchEnddate)
				}
				if(store){
					customer{
							 
							 eq('reInvoicePreCode',store)
						 }
					
					}
				}
				 
			}
			
			}
			order('id','desc')
		}
		return jqgridFilterService.jqgridAdvFilter(params,Shipment,criteriaClosure,doCount)
	}
	def showShipment={
		def instance = Shipment.get(params.id)
		def reInvoices = ''
		def scode=''
		if(instance){
			instance.business.each{
				reInvoices = reInvoices + it.code + ";"
			}
		}else{
			scode=financeService.getShipmentCode(session.user);
			log.info scode
			//instance.code=scode
		}
		render(view:"/hermes/finance/shipmentShow", model: [instance: instance,reInvoices:reInvoices,scode:scode])
	}
	
	
	def listShipmentItemsJson={
		////////////////////////////////////SSSS
		String str_id=params.id
		if(str_id)
		str_id=str_id.replace(',','');
		def id=str_id
		def shipment = Shipment.get(id)
		if(shipment){
		    def instanceList = searchShipmentItems(shipment,false)
		    def pager = searchShipmentItems(shipment,true)
		    JqgridJSON jqgridJSON = new JqgridJSON(pager)
			def item = financeService.getAdjustInvoiceItem(shipment)
			def df = new DecimalFormat(".00")
		    instanceList.eachWithIndex { elem,i->
				def cell = new ArrayList()
				cell.add(elem.id)
				cell.add(elem.id)
				cell.add(elem.invoiceHeader.code)
				cell.add(elem.product.category.code)
				cell.add(elem.product.code)
				cell.add(elem.product.dept)
				if(elem.magitudem && elem.magitudem.length()>0){
					cell.add(elem.magitudem)
				}else{
					cell.add(elem.product.magnitude)
				}
				cell.add(elem.shipmentType)
				
				def duty = elem.getShipmentDuty(shipment.rate)
				//Kurt Edited
				BigDecimal d_duty=new BigDecimal(duty);
				
				BigDecimal d_type=new BigDecimal(elem.shipmentType);
				if(d_duty>2+d_type||d_duty<d_type-2)
				{
					cell.add("<div style='background-color:red'>"+df.format(duty)+"</div>")
				}
				else{
					cell.add(df.format(duty))
				}
				//
				cell.add(elem.product.category.name)
				cell.add(elem.product.name)
				cell.add(elem.quantity)
				cell.add(elem.shipmentDuty)
				cell.add(elem.shipmentYZ+elem.shipmentDL)
				cell.add(elem.getAdjLogisticAmountTotal())
				cell.add(df.format(elem.getAdjShipmentAmt(shipment.rate)))
				cell.add(df.format(elem.getAdjShipmentUP(shipment.rate)))
				
				
				 //Kurt edited
				cell.add(elem.shipmentCost)
				 
				
				/*
				if(item && item.id == elem.id){
					cell.add("<div style='background-color:red'>"+elem.shipmentCost+"</div>")
				}else{
					cell.add(elem.shipmentCost)
				}*/
				
				//cell.add(elem.getShipmentDiff(shipment.rate))
				if(elem.finiAdjAmount)
					cell.add(elem.finiAdjAmount)
				else
					cell.add(0)
				jqgridJSON.addRow(i, cell)
				cell=null;
		    }
		    render jqgridJSON.json as JSON
		}else{
			render(text:"")
		}
	}
	def searchShipmentItems(shipment,doCount){
		def ids = []
		shipment.business.each{
			ids.add(it.code)
		}
		if(ids.size()>0){
			def criteriaClosure = {
				invoiceHeader{
					business{
						'in'("code",ids)
					}
				}
				product{
					order('dept','desc')
					category{
						order('name','desc')
					}
				}
			}
			return jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,doCount)
		}
		return []
	}
	
	//生成与修改shipment
	def updateShipment={
		def id = params.id
		def code = params.code
		def check = params.check
		//System.err.println(" code"+code);
		if(code){
			def exist = Shipment.findByCode(code)
			if(exist){
				flash.message = "shipment:${code} is not found!"
				redirect(action:"showShipment",params:params)
				//render(text:"")
				return true
			}
		}
		/*
		def bps = params.reInvoices.tokenize(';')
		if(bps){
			def save = true
			bps.each{
				def business = Business.findByCode(it)
				if(!business){
					flash.message = "bp:${it} is not found!"
					save = false
				}else{
					if(business.totalAmount<=0 || business.totalQuantity<=0){
						flash.message = "The Shipment can't Save: bp totalAmount<=0 or totalQuantity<=0"
						save = false
					}
					def packing = Packing.findByBusiness(business)
					if(packing && !packing.vaild.equals('Y')){
						flash.message = "The Shipment can't Save: bp ${business} is not vaild"
						save = false
					}
				}
			}
			if(save){
				def description = params.description
				def totalAdjDiff = params.totalAdjDiff
				def blGS = params.blGS
				def blYZ = params.blYZ
				def blDL = params.blDL
				def rate = params.rate
				def bfdf = params.bfdf
				def bal = params.bal
				def shipment = financeService.updateShipment(id,code,description,totalAdjDiff,blGS,blYZ,blDL,rate,bfdf,bal,bps)
				params.id = shipment.id
			}
		}*/
		
		def description = params.description
		def totalAdjDiff = params.totalAdjDiff
		//Kurt removed
		def blGS //= params.blGS  
		def blYZ //= params.blYZ
		def blDL //= params.blDL
		def rate = params.rate
		def bfdf //= params.bfdf
		def bal //= params.bal
		//System.err.println("rate="+rate);
		//END
		def shipment = financeService.updateShipment(id,code,description,totalAdjDiff,blGS,blYZ,blDL,rate,bfdf,bal,null,check)
		    shipment = financeService.updateShipment(shipment.id,code,description,totalAdjDiff,blGS,blYZ,blDL,rate,bfdf,bal,null,check)
		params.id = shipment.id
		
		//shipment.refresh()
		redirect(action:"showShipment",params:params)
	}
	//
	def updateInvoiceItem = {
		log.info params
		def id = params['invoiceitem.id']
		def adj = params['invoiceitem.adj']
		def magitudem = params['invoiceitem.magitudem']
		if(id){
			def invoiceItem = InvoiceItem.get(id)
			invoiceItem.finiAdjAmount = new BigDecimal(adj)
			if(magitudem)
				invoiceItem.magitudem = magitudem
			invoiceItem.save(flush:true)
		}
		render(text: "success")
	}

	def listPOItemTo={
			Calendar   cal1   =   Calendar.getInstance(); 
			Calendar   cal2   =   Calendar.getInstance(); 
			cal2.set(Calendar.DATE,   1); 
			render(view:"/hermes/finance/poItemList",model:[startDate:cal2.getTime(),endDate:cal1.getTime()])
		}
	
	/* purchaseorder section */
	def listPO={
		//System.err.println("search")
		render(view:"/hermes/finance/poList")
	}
	def listPOsJson={
		log.info params
		def supplierId = params.supplier
		if(params.shipmentsubmit==null)
			params.shipmentsubmit='Yes'
		//System.err.println("supplierId="+supplierId)
		def instanceList = searchPOs(params,false)
		ArrayList lt=new ArrayList();
		
		instanceList.each{ elem->
			if(params.shipmentsubmit=='Yes'){
				if(isBPShipmentSubmit(elem.id)){
					lt.add(elem.id)
				}
			}
			else if (params.shipmentsubmit=='No'){
				if(isBPShipmentExistNotSubmit(elem.id)){
					lt.add(elem.id)
				}
			}
			else if(params.shipmentsubmit=='Not Exist'){
				if(!isBPShipmentExist(elem.id)){
					lt.add(elem.id)
				}
				
			}
		}
		//System.err.println("params.shipmentsubmit="+params.shipmentsubmit)
		
		
		def pager=jqgridFilterService.afterPager(params,lt.size())
	    
	    JqgridJSON jqgridJSON = new JqgridJSON(pager)
		def i=-1
	    instanceList.each{ elem->
			
			def cell = new ArrayList()
			// reinvoice,purchaseorder,orderdate,salesorder,suppliercode,whid,locid,totalprice 
			
			if(lt.contains(elem.id)){
				i=i+1
			cell.add(elem.id)
			cell.add(elem.code)
			//System.err.println("elem.business.code="+elem)

			def supplier = elem.supplier
			if(supplier&&supplier.code=="1800" || supplier&&supplier.code=="1200"){
				cell.add('')
			}else{
			
				cell.add(logisticService.getReInvoice(elem))
			}
			//System.err.println("3333")
			cell.add(elem.poCode)
			//cell.add(elem.soCode)
			String DATE_FORMAT = "yyyy-MM-dd"
		    def sdf = new SimpleDateFormat(DATE_FORMAT)
			if(elem.deliveryDate)
				cell.add(sdf.format(elem.deliveryDate))
			else
				cell.add('')
			cell.add(elem.toScala)
			if(elem.supplier){
				cell.add(elem.supplier.code)
			}else{
				cell.add('')
			}
			if(elem.customer){
				cell.add(elem.customer.code)
				cell.add(elem.customer.whId)
				cell.add(elem.customer.locId)
			}else{
				cell.add('')
				cell.add('')
				cell.add('')
			}
			def packing = Packing.findByBusiness(elem)
			if(packing){
				if(packing.exportDate)
					cell.add(sdf.format(packing.exportDate))
					else
					cell.add('')
				if(packing.vaildDate)
					cell.add(sdf.format(packing.vaildDate))
					else
					cell.add('')
			}else{
				cell.add('')
				cell.add('')
			}
			
			
			cell.add(params.shipmentsubmit)
			//
			//if(isBPShipmentSubmit(elem.id)){
			/*if(params.shipmentsubmit){
				cell.add('Yes')
			}
			else{
				 
				cell.add('No')
			 
			}*/
			
			jqgridJSON.addRow(i, cell)
			}
			 
	    }			
		 
	    render jqgridJSON.json as JSON
	     
	}
	
	
	
	//Kurt Edited
	def isBPShipmentSubmit(businessId){
		def sql = new Sql(dataSource);
	   def query = " select * from hermes_shipment sh inner join  hermes_shipment_hermes_business shb on   sh.id=shb.shipment_business_id where  submit=1 and shb.business_id= ? ";
	   def result = sql.rows(query,[businessId]);
	   if(result.size()>0)
		return  true
		else 
		return false
	}
	
	//Kurt Edited 
	def isBPShipmentExist(businessId){
		def sql = new Sql(dataSource);
	   def query = " select * from hermes_shipment sh inner join  hermes_shipment_hermes_business shb on   sh.id=shb.shipment_business_id where  shb.business_id= ? ";
	   def result = sql.rows(query,[businessId]);
	   if(result.size()>0)
		return  true
		else
		return false
	}
	
	def isBPShipmentExistNotSubmit(businessId){
		def sql = new Sql(dataSource);
	   def query = " select * from hermes_shipment sh inner join  hermes_shipment_hermes_business shb on   sh.id=shb.shipment_business_id where submit=0 and shb.business_id= ? ";
	   def result = sql.rows(query,[businessId]);
	   if(result.size()>0)
		return  true
		else
		return false
	}
	//Kurt Edited
	def listPOItemToJson={
		log.info params
			def supplierId = params.supplier
			//System.err.println("supplierId="+supplierId)
			def instanceList = searchPOItemTos(params,false)
			def pager  =jqgridFilterService.afterPager(params,instanceList.size())
		  //  def pager = searchPOItemTos(params,true)
		    JqgridJSON jqgridJSON = new JqgridJSON(pager)
		    instanceList.eachWithIndex { elem,i->
				def cell = new ArrayList()
				// reinvoice,purchaseorder,orderdate,salesorder,suppliercode,whid,locid,totalprice 
				cell.add(elem.id)
				cell.add(elem.code)
				def supplier = elem.supplier
				if(supplier&&(supplier.code=="1800" || supplier.code=="1200")){
					cell.add('')
				}else{
					cell.add(logisticService.getReInvoice(elem))
				}
				cell.add(elem.poCode)
				cell.add(elem.soCode)
				String DATE_FORMAT = "yyyy-MM-dd"
			    def sdf = new SimpleDateFormat(DATE_FORMAT)
				if(elem.deliveryDate)
					cell.add(sdf.format(elem.deliveryDate))
				else
					cell.add('')
				cell.add(elem.toScala)
				if(elem.supplier){
					cell.add(elem.supplier.code)
				}else{
					cell.add('')
				}
				if(elem.customer){
					cell.add(elem.customer.code)
					cell.add(elem.customer.whId)
					cell.add(elem.customer.locId)
				}else{
					cell.add('')
					cell.add('')
					cell.add('')
				}
				def packing = Packing.findByBusiness(elem)
				if(packing){
					if(packing.exportDate)
						cell.add(sdf.format(packing.exportDate))
					if(packing.vaildDate)
						cell.add(sdf.format(packing.vaildDate))
				}else{
					cell.add('')
					cell.add('')
				}
				jqgridJSON.addRow(i, cell)
		    }			
		    render jqgridJSON.json as JSON
		}
	
	def searchPOItemTos(params,doCount){
		log.info params
		def date1=params.startDate
		def date2=params.endDate
		def startDate,endDate
		
		if (date1==null||date1==''){
			Calendar   cal2   =   Calendar.getInstance(); 
			cal2.set(Calendar.DATE,   1);
			cal2.set(Calendar.HOUR,   -12);
			cal2.set(Calendar.MINUTE,   0);
			cal2.set(Calendar.SECOND,   0);
			startDate = cal2.getTime();
		}else{
			DateFormat f   =   new   SimpleDateFormat( "yyyy-MM-dd"); 
			startDate   =   f.parse(date1); 
		}
		if (date2==null||date2==''){
			Calendar   cal1   =   Calendar.getInstance(); 
			//cal1.add(Calendar.DAY_OF_MONTH,1)
			cal1.set(Calendar.HOUR,   12);
			cal1.set(Calendar.MINUTE,   0);
			cal1.set(Calendar.SECOND,   0);
			endDate = cal1.getTime();
			
		}else{
			DateFormat f   =   new   SimpleDateFormat( "yyyy-MM-dd"); 
			endDate   =   f.parse(date2);
			endDate=endDate+1
		}
		//endDate=endDate+1
		//System.err.println("startDate="+startDate)
		//System.err.println("endDate="+endDate)
		def criteriaClosure = {
			and{
				if(startDate) ge("deliveryDate",startDate)
				if(endDate) lt("deliveryDate",endDate)
			}
			//order("deliveryDate","desc")
		}
		return jqgridFilterService.jqgridAdvFilter(params,Business,criteriaClosure,doCount)
	}	
	
	//Kurt Edited
	def getStoreList={
		def storelist =new ArrayList()
		//def allStoreCode= HermesCustomer.findAll();
		def sql = new Sql(dataSource);
	   def query = " select re_invoice_pre_code as reInvoicePreCode from hermes_customer order by re_invoice_pre_code desc  ";
	   def allStoreCode = sql.rows(query,[]);
		//System.err.println("allStoreCode="+allStoreCode);
		//System.err.println("size="+allStoreCode.size());
	  
		allStoreCode.each{
			storelist.add(it.reInvoicePreCode)
		}
		storelist.add ("All");
		return storelist
	}
	
	
	def searchPOs(params,doCount){
		def bpCode = params['bpCode']
		def soCode = params['soCode']
		def poCode = params['poCode']
		//def customerCode = params['customer']
		//def supplierCode = params['supplier']
		def bdate = params['bdate']
		def edate = params['edate']
		def todo = params['todo']
		def export = params['export']
		def reInvoice = params['reInvoiceHidden']
		def reInvoiceLabel = params['reInvoice']
		def inYear =  params['inYear']
		def inMonth =  params['inMonth']
		def store =  params['store']
		def sidx =params['sidx']
		def sord=params['sord']
		
		if(inYear=='this year')
		inYear=null
		if(inMonth=='this month')
		inMonth=null
		//System.err.println("store========="+store);
		if(store==null){
			 
			def res=getStoreList()
			if(res.size()>0)
			store=res[0]
		}
		
		if(store=='All')
		 store=null
		//System.err.println("reInvoice="+reInvoice);
		 
		Date searchBegindate=new Date();
		Date searchEnddate=new Date();
		
		if(inYear){
			searchBegindate.setYear (new Integer(inYear)-1900);
			searchEnddate.setYear (new Integer(inYear)-1900);
		}
		if(inMonth){
			searchBegindate.setMonth(new Integer(inMonth)-1);
			searchEnddate.setMonth(new Integer(inMonth)-1);
		}
		
		searchBegindate.setDate(new Integer(1));
		searchBegindate.setHours(new Integer(0));
		searchBegindate.setMinutes (new Integer(0));
		searchBegindate.setSeconds (new Integer(0));
		
		searchEnddate.setMonth(searchBegindate.getMonth()+1);
		searchEnddate.setDate(new Integer(0));
		searchEnddate.setHours(new Integer(23));
		searchEnddate.setMinutes (new Integer(59));
		searchEnddate.setSeconds (new Integer(59));
		//System.err.println("searchBegindate="+searchBegindate);
		//System.err.println("searchEnddate="+searchEnddate);
		def criteriaClosure = {
 
			and{
				//if(todo){
				//	isNull("poCode")
				//}else{
				or{
				between('deliveryDate',searchBegindate,searchEnddate)
				isNull('deliveryDate')
				}
				if(bpCode) like("code",'%'+bpCode+'%')
				if(reInvoice&&reInvoiceLabel) like("code",'%'+reInvoice+'%')   // 恢复
				//}
				if(export) eq("toScala",'Y')
				else eq("toScala",'N')
				//if(soCode) like("soCode",'%'+soCode+'%')
				if(poCode) like("poCode",'%'+poCode+'%')   
				
				
				
				 customer{
					if(store) eq("reInvoicePreCode",store)  // 恢复
					if(sidx&&sidx=='reInvoice')
					order("reInvoicePreCode",sord)
				} 
				//supplier{
				//	if(supplierCode) like("code",'%'+supplierCode+'%')
				//}
			}
			if(sidx&&sidx=='deliveryDate')
			order("deliveryDate",sord)
			 
		}
		
				
		
		return jqgridFilterService.jqgridAdvFilter(params,Business,criteriaClosure,doCount)
	}

	def showPO={
		//System.err.println(params.id)
		String str_id = params.id
		if(str_id)
		str_id=str_id.replace(',','');
		
		def business_id=str_id
	 
		def instance = Business.get(business_id)
		log.info instance
		if(instance){
			def lsPO = logisticService.getPONotBeUsed()
			def reInvoices = logisticService.getReInvoice(instance)
			def amount= logisticService.getInvoiceAmount(instance)
			render(view:"/hermes/finance/poShow", model: [instance: instance,reInvoices:reInvoices,lsPO:lsPO,amount:amount])
		}else{
			flash.message = "business ${params.id} is not found!"
			redirect(action:"listPO",params:params)
			return false
		}
	}
	def listPOItemsJson={
		//System.err.println("123"+params.id)
		String str_id = params.id
		if(str_id)
		str_id=str_id.replace(',','');
		
		def business_id=str_id
		def business = Business.get(business_id)
		def supplier = business.supplier
		if(supplier&&supplier.code.equals('1800') || supplier&&supplier.code.equals('1200')){
			def criteriaClosure1 = {
	            eq("business",business)
				order("code","asc")
			}
			def instanceList = jqgridFilterService.jqgridAdvFilter(params,BusinessItem,criteriaClosure1,false);
			def pager = jqgridFilterService.jqgridAdvFilter(params,BusinessItem,criteriaClosure1,true);
			JqgridJSON jqgridJSON = new JqgridJSON(pager)
			def i=0
			instanceList.each{elem->  
				def cell = new ArrayList()
				cell.add(elem.id)
				if(elem.product){
					cell.add(elem.product.code)
				}else{
					cell.add(elem.code)
				}
				
				cell.add('')
				cell.add('')
				/*
				def dept = elem.product.dept
				if(dept.equals('Non Hermes & others')){
					cell.add("<div class='redColor'>"+dept+"</div>")
				}else{
					cell.add(dept)
				}
				cell.add(elem.product.category.name)
				cell.add(elem.product.name)
				*/
				cell.add('')
				cell.add('')
		      	cell.add(elem.quantity)
				cell.add(0)
				cell.add(elem.amount)
				cell.add(elem.amount*elem.quantity)
				i=i+1
	      		jqgridJSON.addRow(i, cell)
			}
			render jqgridJSON.json as JSON		
		}else{
			def criteriaClosure1 = {
				invoiceHeader{
		            eq("business",business)
					order("code","asc")
		        }
			}
			def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure1,false);
			def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure1,true);
			JqgridJSON jqgridJSON = new JqgridJSON(pager)
			def i=-1
			instanceList.each{elem->  
				def cell = new ArrayList()
				cell.add(elem.id)
				//cell.add(elem.invoiceHeader.code)
				//cell.add(elem.invoiceHeader.getReInvoiceCode())
				cell.add(elem.poCode)
				cell.add(elem.product.code)
				def dept = elem.product.dept
				if(dept.equals('Non Hermes & others')){
					cell.add("<div class='redColor'>"+dept+"</div>")
				}else{
					cell.add(dept)
				}
				if(elem.magitudem && elem.magitudem.length()>0){
					cell.add(elem.magitudem)
				}else{
					cell.add(elem.product.magnitude)
				}
				cell.add(elem.product.category.name+" "+elem.product.name)
		      	cell.add(elem.quantity)
				cell.add(0)
				def rate = financeService.getShipmentRate(elem.invoiceHeader)
				//def adjItem = logisticService.getAdjustInvoiceItem(elem.invoiceHeader.business.id)
				def shipmentUPS=elem.getShipmentUPS(rate)
				def shipmentAmt=elem.getShipmentUP(rate)
				/*
				if( adjItem && elem.id == adjItem.id ){
					def df = new DecimalFormat("#.0000")
					cell.add("<div style='background-color:red'>"+df.format(shipmentUPS+elem.invoiceHeader.business.totalAdjDiff/elem.quantity)+"</div>")
					cell.add("<div style='background-color:red'>"+(shipmentAmt+elem.invoiceHeader.business.totalAdjDiff)+"</div>")
					
				}*/
				//else{
				def df = new DecimalFormat("#.00")
				cell.add(df.format(shipmentUPS+rate*(elem.logsAdjAmount-elem.amount)))
				cell.add(df.format(shipmentAmt+rate*(elem.logsAdjAmount-elem.amount)*elem.quantity))
				//}
				
				
				
				
				i=i+1
	      		jqgridJSON.addRow(i, cell)
			}
			render jqgridJSON.json as JSON		
		}
	}
	def updatePO={
		log.info params
		def poCode = params.poCode
		def business = Business.get(params.id)
		if(business){
			if(poCode){
				def check = Business.findByPoCode(poCode)
				if(check){
					if(check.id!=business.id){
						flash.message = "poCode: ${params.poCode} is  exists on bp:${check.code}"
						redirect(action:"showPO",params:params)
						return true
					}
				}
				//def supplier = HermesSupplier.findByCode(params.supplier)
				//if(supplier) business.supplier = supplier
				business.poCode = params.poCode
				business.soCode = params.soCode
				//System.err.println("params.deliveryDate="+params.deliveryDate)
				if(params.deliveryDate){
			        SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
			        Date date = formate.parse(params.deliveryDate);
					Date now=new Date();
					date.setHours (now.getHours())
					date.setMinutes (now.getMinutes())
					date.setSeconds (now.getSeconds())
					business.deliveryDate = date
				}
				if(!business.save(flush:true)){
					business.errors.each{
						log.info it
					}
				}
				business.invoiceHeaders.each{ih->
					def curCode=ih.code
					ih.invoiceItems.each{ii->
						ii.poCode = curCode
						ii.save(flush:true)
					}
				}
				redirect(action:"showPO",params:params)
			}else{
				flash.message = "poCode: ${poCode} is null!"
				redirect(action:"showPO",params:params)
				return true
			}
		}else{
			flash.message = "business ${params.id} is not found!"
			redirect(action:"showPO",params:params)
			return true
		}
	}
	
	/* ajax json */
	def shipmenHasBusinessJson={
		def shipment
		if(params.id) {
			String str_id = params.id
			if(str_id)
			str_id=str_id.replace(',','');
			def id=str_id
			shipment = Shipment.get(id)
			
		}
		def items = []
		if(shipment){
			shipment.business.each{b->
				def item = []
				def reInvoice = logisticService.getReInvoice(b)
				item.add(b.id)
				item.add(reInvoice)
				item.add(null)
				item.add(reInvoice)
				items.add(item)
			}
		}
		render items as JSON
	}	
	def shipmenNoHasBusinessJson={
		def shipment
		if(params.id) shipment = Shipment.get(params.id)
		def items = []
		//Kurt Edited   效率不好
		 
		 /*
		def businessResult= Business.list()
		def shipmentresult = Shipment.list()
		businessResult.each{ bu->
			def exist = false
			shipmentresult.each { ship->
				if(!exist){
					ship.business.each{b->
						if(!exist&&b.id==bu.id){
							exist = true
						}
					}
				}
			}
			if(!exist){
				def item = []
				//def reInvoice = logisticService.getReInvoice(bu)
				item.add(bu.id)
				item.add(bu.reInvoice)
				item.add(null)
				item.add(bu.reInvoice)
				items.add(item)
			}
		} */
		// 临时的，以后删除
		/*
		def businessResult= Business.list()
		businessResult.each{ bu->
			def reInvoice = logisticService.getReInvoice(bu)
			bu.reInvoice=reInvoice
			bu.save()
		}*/
		//以上删除
		
		
		def sql = new Sql(dataSource)
		def key = "availableBPs "
		def result = sql.rows("exec "+key)
		for(int i=0;i<result.size();i++)
		 {
			def item = []
			item.add(result[i][0])//id
			item.add(result[i][1])//reInvoice
			item.add(null)
			item.add(result[i][1])
			items.add(item)
		}
		 		 
	   
		
		
		
		render items as JSON
	}	
	//生成shipment
	def shipmentBusinessUpdate={
		def shipment = Shipment.get(params.shipmentid)
		def act = params.act
		if(shipment){
			if(act.equals('add')){
				def business = Business.get(params.businessid)
				if(business){
					if(business.totalAmount<=0 || business.totalQuantity<=0){
						render(text:"The Shipment can't Save: bp totalAmount<=0 or totalQuantity<=0")
						return
					}
					def packing = Packing.findByBusiness(business)
					if(packing && !packing.vaild.equals('Y')){
						render(text:"Can not add this BP: bp "+business.code+" is not valid, because packing is not vaild, please set packing valid Date")
						return
					}
					if(shipment.submit==0&&shipment.business.count(business)<=0){
						shipment.addToBusiness(business)
						log.info shipment
						if(!shipment.save(flush:true)){
							shipment.errors.each{
								log.info it
							}
						}
					}
					render(text:"success")
					return
				}else{
					render(text:"bp:${it} is not found!")
					return
				}
			}
			if(act.equals('del')){
				def business = Business.get(params.businessid)
				if(business&&shipment.submit==0){
					shipment.removeFromBusiness(business)
					shipment.save(flush:true)
					render(text:"success")
					return
				}else{
					render(text:"bp:${it} is not found!")
					return
				}
			}
		}else{
			render(text:"shipment ${params.shipmentid} not found!")
			return
		}
	}
	def autocompleteJson={
		def items = []
		def item = [:]
		item.code = 'code1'
		item.desc = 'description1'
		items.add(item)

		item = [:]
		item.code = 'code2'
		item.desc = 'description2'
		items.add(item)
		
		render items as JSON
	}
	def listSupplierJson = {
		def results = HermesSupplier.withCriteria{
			projections{
				distinct("code")
			}
		}
		log.info results
		def jsons = []
		results.each{
			def json = [:]
			json.code = it
			jsons.add(json)
		}
		render jsons as JSON
	}
	def listBusinessJson={
		def items = []
		log.info params
		def para_reinvoice=params.q
		//def result = Business.findByCodeLike('%'+params.q+'%')
		
		def result = Business.list()
		
		//System.err.println("Business.list()"+result.size())
		
		result.each{
			def item = [:]
			def reInvoice = logisticService.getReInvoice(it)
			if(reInvoice&&para_reinvoice){
				String s_reInvoice=reInvoice;
				String s_para_reinvoice=para_reinvoice;
			
				if(s_reInvoice.toLowerCase().contains(s_para_reinvoice.toLowerCase()))
				{
				item.code = it.code
				item.desc = reInvoice
				items.add(item)
			}
			}
		}
		render items as JSON
	}
	
	def listBPReInvoiceJson={
		log.info params
		//def result = Business.findByCodeLike('%'+params.q+'%')
		
		def result = Business.list()
		def business = []
		result.each{
			def item = []
			/*
			item.id = it.code
			item.name = it.code
			*/
			def reInvoices = logisticService.getReInvoice(it)
			item.add(it.code)
			item.add(reInvoices)
			item.add(null)
			item.add(reInvoices)
			business.add(item)
		}
		render business as JSON
	}
	
	//submitShipment 将shipment改为  submitted状态
	def submitShipment={
		def items=params.items
		if(items){
		 
			def businessList = items.split(',')
			businessList.each{
				//System.err.println(it)
				setShipmentStatusFromNotSubmittedToSubmitted(it)
			}
			
		}
		render(view:"/hermes/finance/poList")
		
	}
	
	
	//undoShipment 将shipment改为not submitted状态
	def undoShipment={
		
		def items=params.items
		if(items){
		 
			def businessList = items.split(',')
			businessList.each{
				//System.err.println(it)
				setShipmentStatusFromSubmittedToNotSubmitted(it)
			}
			
		}
		render(view:"/hermes/finance/poList")
	}
	//update shipment status from 0 to 1
	def setShipmentStatusFromNotSubmittedToSubmitted(business_id)
	{
		def sql = new Sql(dataSource);
		
	
	   def query = " update hermes_shipment  set hermes_shipment.submit=1 from hermes_shipment  sh inner join hermes_shipment_hermes_business shb on sh.id =shb.shipment_business_id "
	   query=query+" where sh.submit=0 and shb.business_id=? "
	   def results =  sql.executeUpdate (query,[business_id]);
		
	}
	
	
	//update shipment status from 1 to 0
	def setShipmentStatusFromSubmittedToNotSubmitted(business_id)
	{
		def sql = new Sql(dataSource);
		
	
	   def query = " update hermes_shipment  set hermes_shipment.submit=0 from hermes_shipment  sh inner join hermes_shipment_hermes_business shb on sh.id =shb.shipment_business_id "
	   query=query+" where sh.submit=1 and shb.business_id=? "
	   def results =  sql.executeUpdate (query,[business_id]);
		
	}
	
	def createPO={
			def items=params.items
			def sCode=''
			if(items){
				
				def pos = items.split(',')
				pos.each{
					sCode=''
					def po=Business.get(it)
					if(po){
						if (po.poCode==null || po.poCode==''){
							po.invoiceHeaders.each{ih->
								if (sCode!='') sCode+=',' 
								sCode+=ih.code
								def curCode=ih.code
								ih.invoiceItems.each{ii->
									ii.poCode = curCode
									ii.save(flush:true)
								}
							}
							def reInvoiceNumbers = logisticService.getReInvoice(po)  
							po.poCode = sCode
							po.soCode = reInvoiceNumbers
							po.deliveryDate=new Date();
							if(!po.save(flush:true)){
								po.errors.each{
									log.info it
								}
							}							
						}
					}
				}
			}
			render(view:"/hermes/finance/poList")
		}		
}