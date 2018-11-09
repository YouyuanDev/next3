package hermes
import groovy.sql.Sql
import org.apache.poi.hssf.usermodel.*
import next.*
import grails.converters.*
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import java.io.FileInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.core.io.ClassPathResource

class ReportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def dataSource 
	def logisticService
	def financeService
	def jqgridFilterService
    def index = {
        redirect(action: "report1", params: params)
    }
	
	//List of Hermes PO
    def report1 ={
		if(params.year1==null)
		params.year1=2011
		if(params.inMonth==null)
		  params.inMonth=1
	    def sql = new Sql(dataSource)
	    //def query = "select invoiceitem.id as invoiceItemId, business.to_scala, customer.id as customerid, shipment.rate,invoiceitem.shipment_duty as tariff ,(invoiceitem.shipmentdl+ invoiceitem.shipmentyz) as FreightAgent,invoiceitem.shipment_cost,shipment.bal as apbl,shipment.bfdf,customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.code as suppliercode ,invoiceheader.code as sellierinv,invoiceheader.code as reSellierinv, shipment.total_amount as sellieramt, invoiceitem.quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceheader.code,invoiceheader.recode from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join category  on product.id =category.id inner join hermes_customer as customer on business.customer_id=customer.id inner join party as supplier on business.supplier_id=supplier.id inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where 1=1"
	    //def query = "select customer.loc_id,business.total_difference as diff,(business.total_amount+business.total_freight+business.total_issuance) as cif,business.to_scala, customer.id as customerid, shipment.rate,shipment.totalBLGS as tariff ,(shipment.bldl+ shipment.blyz) as FreightAgent,shipment.totalBLCost,shipment.bal as apbl,shipment.bfdf,customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.code as suppliercode ,business.po_code as sellierinv,business.po_code as reSellierinv, shipment.total_amount as sellieramt, floor(business.total_quantity) as quantity,shipment.totalBLGS as duty,business.currency,business.so_code from hermes_business as business inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where (not business.delivery_date is null) "
	    def query= " select hdr.contract_code as contractcode, DATEPART(Month,business.delivery_date) as Month, DATENAME(Year,business.delivery_date) as year1, hdr.report1depts as dept,shipment.id as shipid, customer.loc_id,business.to_scala,customer.id as customerid, shipment.rate,  convert(decimal(15,2),(sum(item.quantity*item.logs_adj_amount+item.logistic_freight+item.logistic_issuance)) )as sellieramt, convert(decimal(15,2),(sum(item.quantity*item.logs_adj_amount+item.logistic_freight+item.logistic_issuance)*shipment.rate) ) as sellieramtRMB, sum(floor(item.quantity)) as quantity, convert(decimal(15,2), sum(item.shipment_duty)) as tariff , "
		query=query+" convert(decimal(15,2),sum(item.shipmentdl+ item.shipmentyz)) as FreightAgent,convert(decimal(15,2),(sum(shipment.rate*(item.quantity*item.logs_adj_amount+item.logistic_freight+item.logistic_issuance)+item.shipment_duty+item.shipmentdl+ item.shipmentyz))) as totalBLCost,convert(decimal(15,2),hdr.report1Apbl) as apbl, "
		query=query+" customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,supplier.code as suppliercode ,business.currency,business.so_code, item.po_code as sellierinv, hdr.report1bfdf as bfdf, hdr.total_aphl_for_this_contract_code as aphl from hermes_business as business "
		query=query+" inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id "
		query=query+" inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id "
		query=query+" inner join hermes_invoiceheader hdr on hdr.business_id=business.id "
		query=query+" inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id "
		query=query+" inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  where (not business.delivery_date is null)"
		 
		//query=query+"    and customer.re_invoice_pre_code NOT  like '%KM%'  AND customer.re_invoice_pre_code  NOT like '%NJ%' AND customer.re_invoice_pre_code  not like '%CD%' AND customer.re_invoice_pre_code not  like '%PR%' AND customer.re_invoice_pre_code not  like '%HB%' AND customer.re_invoice_pre_code  not like '%SU%' and customer.re_invoice_pre_code  not like '%QD%' and customer.re_invoice_pre_code  not like '%HZ%'  and  customer.re_invoice_pre_code  not like '%WX%' and customer.re_invoice_pre_code not  like '%GZ%' and  customer.re_invoice_pre_code not like '%SY%' AND customer.re_invoice_pre_code not  like '%BJ%' and customer.re_invoice_pre_code not  like '%SH%' and customer.re_invoice_pre_code not  like '%SZ%'  "
		
		def paramstring =''
	    if(params.store && params.store != '') paramstring = " and customer.re_invoice_pre_code like '%" +params.store +"%'" 
	    if(params.reinvoice && params.reinvoice != '') paramstring += " and hdr.re_invoice like '%" +params.reinvoice +"%'" 
	    if(params.suppliercode && params.suppliercode != '') paramstring += " and supplier.code like '%" +params.suppliercode +"%'" 
	    if(params.sellierinv && params.sellierinv != '') paramstring += " and business.po_code like '%" +params.sellierinv +"%'" 
	    if(params.year1 && params.year1 != ''&& params.year1 != 'All years') paramstring += " and year(business.delivery_date) = '" +params.year1 +"'" 
	    if(params.inMonth&&params.inMonth!='All')paramstring += " and month(business.delivery_date) = '" +params.inMonth +"'" 
		query=query + paramstring
		query=query+" group by hdr.total_aphl_for_this_contract_code,  hdr.contract_code, hdr.re_invoice, hdr.report1bfdf,hdr.report1apbl,hdr.report1depts,customer.loc_id,business.to_scala,customer.id, shipment.rate,customer.re_invoice_pre_code, customer.re_invoice_pre_code,business.delivery_date,supplier.code ,business.currency,business.so_code,item.po_code,shipment.id,business.total_adj_diff order by hdr.re_invoice asc "


		def unitMap
		def resultsMap = []
	    def results = sql.rows(query)
	    def subamt=0,subamtrmb=0,subqty=0,subap=0
	    def subcif=0,subtariff=0,subfa=0,subcost=0,subdiff=0,subapbl=0,subaphl=0
		log.info query
		//log.info 'testlog'
		//def df = new DecimalFormat("#.00")
		 
		
	    if (results.size() > 0) {
			for(int i=0;i<results.size();i++){
				unitMap=results.get(i)
				//def InvoiceItemInstance = InvoiceItem.get(unitMap.invoiceItemId)

				//unitMap.reinvoice = logisticService.getReInvoice(InvoiceItemInstance.invoiceHeader.business)
				subamt += unitMap.sellieramt
				subamtrmb += unitMap.sellieramtRMB
				subqty += unitMap.quantity
				subap += unitMap.bfdf
				
				//dept information here
			 	/*def query2="select distinct(product.dept) from hermes_product product inner join hermes_invoiceItem item on product.id=item.product_id "
					query2=query2+" where item.po_code ='"+unitMap.sellierinv+"'"
					def depts = sql.rows(query2)
					
					depts.each{
						unitMap.dept+=it.dept
					} */
					
					 /*
					def query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
						query3=query3+" inner join hermes_product product on product.id=item.product_id "
						query3=query3+" where (not business.delivery_date is null) "
						query3=query3+" and product.magnitude='Non Hermes & others' "
						query3=query3+" and shipment.id ="+unitMap.shipid 
						query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
						def ap_segalist = sql.rows(query3)
					 if(ap_segalist.size()>0){
						 if(ap_segalist[0].sellierinv==unitMap.sellierinv){
							 unitMap.bfdf=ap_segalist[0].bfdf
							 unitMap.apbl=unitMap.apbl-unitMap.bfdf
						 }
					 } 
					 else{
						 query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
						 query3=query3+" inner join hermes_product product on product.id=item.product_id "
						 query3=query3+" where (not business.delivery_date is null) "
						 query3=query3+" and product.magnitude<>'Non Hermes & others' "
						 query3=query3+" and shipment.id ="+unitMap.shipid
						 query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
						  ap_segalist = sql.rows(query3)
						  
						  if(ap_segalist.size()>0&&ap_segalist[0].sellierinv==unitMap.sellierinv){
							  unitMap.bfdf=ap_segalist[0].bfdf
							  unitMap.apbl=unitMap.apbl-unitMap.bfdf
						  }
						  
					 }
					 */
						
					
				//Calendar c = Calendar.getInstance();
				//c.setTime(unitMap.year1)
				//Calendar cc = Calendar.getInstance();
				//cc.setTime(unitMap.invoice_date)
				
				//unitMap.year1= c.get(Calendar.YEAR) 
				unitMap.store= unitMap.store.trim()
				
				 
				//unitMap.Month= c.get(Calendar.YEAR) +""+c.get(Calendar.MONTH)+1  

				//unitMap.Month = String.format('%tY%<tm', c)
			   // BigDecimal rate = 10
			   //if(unitMap.rate!=''){rate =new BigDecimal(Double.parseDouble(unitMap.rate))}
			    //rate = unitMap.rate
			    //rate = 10
				//unitMap.cif =InvoiceItemInstance.getShipmentUP(rate)
				//unitMap.diff=InvoiceItemInstance.getShipmentDiff(rate)
			    //subcif += unitMap.cif
			    subtariff +=unitMap.tariff
			    subfa += unitMap.FreightAgent
			    subcost += unitMap.totalBLCost
			    //subdiff += unitMap.diff
			    subapbl += unitMap.apbl
				subaphl+= unitMap.aphl
				resultsMap.add(unitMap)
				

			}
			[reportList:resultsMap,subamt:subamt,subamtrmb:subamtrmb,subqty:subqty,subap:subap,subcif:subcif,subtariff:subtariff,subfa:subfa,subcost:subcost,subdiff:subdiff,subapbl:subapbl,subaphl:subaphl]

	    }
    }
    
    def report2 ={
		def unitMap
		def resultsMap = []
		def sql = new Sql(dataSource)
	    def query = "select top 10000 convert(decimal(15,2), (shipment.rate*(invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight)+invoiceitem.shipmentyz+invoiceitem.shipmentdl+invoiceitem.shipment_duty)/invoiceitem.quantity) as Unitcost,convert(decimal(15,2), shipment.rate*(invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight))+invoiceitem.shipmentyz+invoiceitem.shipmentdl+invoiceitem.shipment_duty as TOTALRMB, invoiceitem.shipmentyz+invoiceitem.shipmentdl as OthercostRMB, business.total_adj_diff as total_adj_diff, business.id as bussiness_id,convert(decimal(15,2), shipment.rate*(invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight)) as sellieramtrmb, DATEPART(Month,business.delivery_date) as month1, DATENAME(Year,business.delivery_date) as year1, invoiceheader.invoice_year,invoiceheader.re_invoice,customer.loc_id,invoiceitem.podium,product.magnitude as magnitude,product.sku_code as longref,left(product.sku_code,8) as sr,shipment.rate, invoiceitem.id as invoiceItemId,business.delivery_date  as month2 ,supplier.name as supplier ,invoiceheader.code as invoiceheadercode,product.product_code as stockno,category.name as name_zh, category.code as shortref, product.dept as dep, product.magnitude as magnitude,convert(decimal(15,2),invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight) as sellieramt, floor(invoiceitem.quantity) as quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceitem.shipmentyz,invoiceitem.shipmentdl,customer.re_invoice_pre_code as store from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join product ori_product on ori_product.id =product.id inner join category  on ori_product.category_id =category.id inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where business.delivery_date is not null and 1=1" 
	    def paramstring =''  
		if(params.month==null)
		params.month=1
		if(params.year==null)
		params.year=2011
		//System.err.println("shortref:"+params.shortref)
		//System.err.println("dep:"+params.dep)
	    if(params.year && params.year != ''&& params.year != 'All years') paramstring = paramstring+" and year(business.delivery_date) = '" +params.year +"'"
	    if(params.id && params.id != '') paramstring = paramstring+" and customer.re_invoice_pre_code like '%" +params.id +"%'" 
	    if(params.month && params.month != ''&& params.month !='All months') paramstring = paramstring+" and month(business.delivery_date) = '" +params.month +"'"
	    if(params.invoiceno && params.invoiceno != '') paramstring = paramstring+" and invoiceheader.re_invoice like '%" +params.invoiceno +"%'"
	    if(params.supplier && params.supplier != '') paramstring = paramstring+" and supplier.name like '%" +params.supplier +"%'"
	    if(params.paris && params.paris != '') paramstring = paramstring+" and invoiceheader.code like '%" +params.paris +"%'"
	    if(params.podium && params.podium != '') paramstring = paramstring+" and invoiceitem.podium like '%" +params.podium +"%'"
	    

	    if(params.stockno && params.stockno != '') paramstring = paramstring+" and  product.sku_code like '%" +params.stockno +"%'" 
	    if(params.shortref && params.shortref != '') paramstring = paramstring+" and category.code like '%" +params.shortref +"%'"
	    if(params.dep && params.dep != '') paramstring = paramstring+" and product.dept like '%" +params.dep +"%'"
	    if(params.nature && params.nature == 'Seasonal') paramstring = paramstring+" and (product.dept like '%D%' or product.dept like '%G%' or product.dept like '%H%' or product.dept like '%N%' or product.dept like '%Z%' ) "
		if(params.nature && params.nature == 'Non-Seasonal') paramstring = paramstring+" and (product.dept not like '%D%' and product.dept not like '%G%' and  product.dept not like '%H%' and product.dept not like '%N%' and product.dept not like '%Z%' ) "
		
		if(params.magnitudecode && params.magnitudecode != '') paramstring = paramstring+" and product.magnitude like '%" +params.magnitudecode +"%'"
		if( params.bp&&params.bp!= '')paramstring = paramstring+" and business.code like '%"+params.bp +"%'"
	    if(params.rate && params.rate != '') paramstring = paramstring+" and shipment.rate like '%" +params.rate +"%'"	    
	    query=query + paramstring
		//System.err.println("search:"+query)
		log.info query
	    def results = sql.rows(query)
	    def totalMap
		def totalqty = 0 ,totalamt =0,totalrmb = 0
		def totalamtrmb=0, totaldutyrmb=0, totalother=0
	    if (results.size() > 0) {
	 
			for(int i=0;i<results.size();i++){
				unitMap=results.get(i)
//if(params.invoiceno && params.invoiceno != '')
				
				
				//def InvoiceItemInstance = InvoiceItem.get(unitMap.invoiceItemId)
				//unitMap.reinvoice = logisticService.getReInvoice(InvoiceItemInstance.invoiceHeader.business)
				
				def adjItem = null//logisticService.getAdjustInvoiceItem(unitMap.bussiness_id)
				def df = new DecimalFormat("#.00")
				def sf2 = new DecimalFormat("#.00")
				/*if( adjItem && unitMap.invoiceItemId == adjItem.id ){
					
					//unitMap.sellieramt=new BigDecimal(sf2.format(unitMap.sellieramt+unitMap.total_adj_diff))
					//unitMap.sellieramtrmb=unitMap.sellieramtrmb+unitMap.total_adj_diff*unitMap.rate
					//unitMap.TOTALRMB=unitMap.TOTALRMB+unitMap.total_adj_diff*unitMap.rate
					//if(unitMap.quantity!=0)
					//unitMap.Unitcost=unitMap.Unitcost+unitMap.total_adj_diff/unitMap.quantity
				}*/
				 
				//BigDecimal rate = 1
				//if(unitMap.rate!=''){rate =new BigDecimal(Double.parseDouble(unitMap.rate))} 
				//rate = unitMap.rate
				/*Calendar c = Calendar.getInstance();
				if(unitMap.month2)
				c.setTime(unitMap.month2)*/
				//unitMap.month1= c.get(Calendar.MONTH)+1
				//unitMap.year1=c.get(Calendar.YEAR)
				
				
				def dept = unitMap.dept.trim()
				if ('DGHNZ'.contains(dept)){
					unitMap.Nature='Seasonal'
				}else{
					unitMap.Nature='Non-Seasonal'
				}
				/*
				def adjusted_amount=0
				if(adjItem && InvoiceItemInstance.id == adjItem.id ){
					//adjusted_amount=InvoiceItemInstance.amount+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff/InvoiceItemInstance.quantity
					//unitMap.AMTRMB=InvoiceItemInstance.getShipmentAmt(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate
					//unitMap.DutyRMB=InvoiceItemInstance.shipmentDuty/(InvoiceItemInstance.getShipmentAmt(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate)*100
					//unitMap.TOTALRMB=InvoiceItemInstance.getShipmentUP(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate
					unitMap.Unitcost=InvoiceItemInstance.getShipmentUPS(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate/InvoiceItemInstance.quantity
					//unitMap.OthercostRMB=unitMap.shipmentyz+unitMap.shipmentdl
					}
					else{
						adjusted_amount=InvoiceItemInstance.amount
						unitMap.AMTRMB=InvoiceItemInstance.getShipmentAmt(rate)
						unitMap.DutyRMB=InvoiceItemInstance.getShipmentDuty()
						unitMap.TOTALRMB=InvoiceItemInstance.getShipmentUP(rate)
						unitMap.Unitcost=InvoiceItemInstance.getShipmentUPS(rate)
						unitMap.OthercostRMB=unitMap.shipmentyz+unitMap.shipmentdl
					}*/
					//unitMap.desc=InvoiceItemInstance.product.category.name
				 
 
				
				resultsMap.add(unitMap)


				totalqty += unitMap.quantity
				totalamt += unitMap.sellieramt
				totalrmb += unitMap.TOTALRMB
				totalamtrmb += unitMap.sellieramtrmb
				totaldutyrmb += unitMap.duty
				totalother += unitMap.OthercostRMB
				
				
				//unitMap.TOTALRMB=sf2.format(unitMap.TOTALRMB)
				//unitMap.sellieramtrmb=sf2.format(unitMap.sellieramtrmb)
				//unitMap.duty=sf2.format(unitMap.duty)
				//unitMap.OthercostRMB=sf2.format(unitMap.OthercostRMB)
				unitMap.Unitcost=df.format(unitMap.Unitcost)
			  }
				//def sf = new DecimalFormat("#.00")
				//totalamt=sf.format(totalamt)
				//totalrmb=sf.format(totalrmb)
				//totalamtrmb=sf.format(totalamtrmb)
				//totaldutyrmb=sf.format(totaldutyrmb)
				
			  [totalReport:totalMap,totalqtyReport:totalqty,totalamtReport:totalamt,totalrmbReport:totalrmb,reportList:resultsMap,totalamtrmb:totalamtrmb,totaldutyrmb:totaldutyrmb,totalother:totalother] 
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
   
   
      def report3 ={
	  
	    def sql = new Sql(dataSource)
	    
	    def query = "select customer.id as customerid, shipment.rate,invoiceitem.shipment_duty as tariff ,(invoiceitem.shipmentdl+ invoiceitem.shipmentyz) as FreightAgent,invoiceitem.shipment_cost,shipment.bal as apbl,shipment.bfdf,customer.wh_id as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.name as suppliercode ,product.product_code as sellierinv,category.code as reSellierinv, invoiceitem.amount as sellieramt, invoiceitem.quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceheader.code from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join category  on product.id =category.id inner join hermes_customer as customer on business.customer_id=customer.id inner join party as supplier on business.supplier_id=supplier.id inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where 1=1"
	    def results = sql.rows(query)
	    if (results.size() > 0) {
	    
		 [reportList:results]
	    }
    }

	 
 def report4 ={
	log.info params
	/* def unitMap
	def resultsMap = []
	def KELLYresultsMap = []
	def sql = new Sql(dataSource)

	def query = "SELECT cust.re_invoice_pre_code AS store,invoiceheader.code AS invoice,prod.product_code AS reference,prod.product_name AS description,business.delivery_date AS month2 FROM "
    query=query+"hermes_business AS business Inner Join hermes_invoiceheader AS invoiceheader ON invoiceheader.business_id = business.id Inner Join hermes_invoiceitem AS invoiceitem ON invoiceitem.invoice_header_id = invoiceheader.id Inner Join hermes_product AS prod ON invoiceitem.product_id = prod.id "
    query=query+"Inner Join hermes_customer AS cust ON business.customer_id = cust.id "
    query=query+"Inner Join hermes_magnitem AS magnitem ON prod.speciality = magnitem.code "
    query=query+"Inner Join category ON category.code = magnitem.refno "
    query=query+"Inner Join product ON prod.id = product.id AND product.category_id = category.id "			
	    
	    def paramstring ='' 
	    if(params.id && params.id != '') paramstring += " and customer.re_invoice_pre_code like '%" +params.id +"%'" 
	    if(params.month1 && params.month1 != '') paramstring += " and month(business.delivery_date) = '" +params.month1 +"'" 
	
	    query=query + paramstring
	    
	    def KELLYparamstring =" and magnitem.magndept like '%KELLY%' "
	    def BIRKINparamstring =" and magnitem.magndept like '%BIRKIN%' "
	    def results = sql.rows(query+BIRKINparamstring)
	     def KELLYresults = sql.rows(query+KELLYparamstring)
		log.info query+KELLYparamstring
	    def totalMap
	 def totalqty = 0 ,totalamt =0,totalrmb = 0
	
	    
	    
		  for(int i=0;i<results.size();i++){
		  unitMap=results.get(i)
		  Calendar c = Calendar.getInstance();c.setTime(unitMap.month2)
		  unitMap.store=  unitMap.store	//.substring(0,4).trim()
		  unitMap.month1= c.get(Calendar.MONTH)+1 
		  resultsMap.add(unitMap) 
		  if(i==(results.size()-1)){params.id=unitMap.store
		  params.month1=unitMap.month1}
		  }
	  
		  for(int i=0;i<KELLYresults.size();i++){
		  unitMap=KELLYresults.get(i)
		  Calendar c = Calendar.getInstance();c.setTime(unitMap.month2)
		  unitMap.store=  unitMap.store//.substring(0,4).trim()
		  unitMap.month1= c.get(Calendar.MONTH)+1 
		  KELLYresultsMap.add(unitMap) 
		   if(i==(results.size()-1)){params.id=unitMap.store
		  params.month1=unitMap.month1}
		  }
if(results.size()==0&&KELLYresults.size()==0){params.id='';params.month1=''}
[totalReport:totalMap,totalqtyReport:totalqty,totalamtReport:totalamt,totalrmbReport:totalrmb,reportList:resultsMap,KELLYreportList:KELLYresultsMap] 
 */
    }
 
 
 //Kurt Edited
 def updateSelectedImportReport={
	 log.info params
	 def items =  params['hiddenSelectedItem']
	 def invoiceId =  params['invoiceId']
	 def invoiceDate=params['invoiceDate']
	 def HreceivedInvoceDate=params['receiveDate']
	 def coCitisImportDate=params['coCitisImportDate']
	 def coCitisExportDate=params['coCitisExportDate']
	 def coCitisSendingImportDate=params['coCitisSendingImportDate']
	 def DepartParis=params['fromPariseDate']
	 def ArrivalSH=params['arriveDate']
	 def BLReceiveDate=params['baoLongReceiveDate']
	 def qADuration=params['qADuration']
	 def dOExchangeDate=params['dOExchangeDate']
	 def dutyPaidDate=params['dutyPaidDate']
	 def inspectionDate=params['inspectionDate']
	 def vatDutyAmount=params['vatDutyAmount']
	 def FinCustCleaDate=params['clearanceDate']
	 def directToStore=params['directToStore']
	 def warehouseInDate=params['warehouseInDate']
	 def rcvdNumersOfCartons=params['rcvdNumersOfCartons']
	 def damagedCartonNo=params['damagedCartonNo']
	 def shortagePcs=params['shortagePcs']
	 def rcvDOInstructionFromStoreDate=params['rcvDOInstructionFromStoreDate']
	 def requiredDeliveryByStoreDate=params['requiredDeliveryByStoreDate']

	 def DeliveryToStoreDate=params['deliveryDate']
	 def remark=params['remark']
	 
	 def Ids
	 if(items)
	 Ids=items.split(',')
	 Ids.each { 
		 def invoiceheader = InvoiceHeader.get(it)
		  //System.err.println("invoiceheader_Id="+it)
		  if(invoiceheader){
		  SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
		  if(invoiceDate){
		  Date date = formate.parse(invoiceDate);
		  invoiceheader.invoiceDate=date
		  }
		  if(HreceivedInvoceDate){
		  Date date2 = formate.parse(HreceivedInvoceDate);
		  invoiceheader.receiveDate=date2
		  }
		  
		  if(coCitisImportDate){
			  Date date_co1 = formate.parse(coCitisImportDate);
			  invoiceheader.coCitisImportDate=date_co1
		  }
		  if(coCitisExportDate){
			  Date date_co2 = formate.parse(coCitisExportDate);
			  invoiceheader.coCitisExportDate=date_co2
		  }
		  if(coCitisSendingImportDate){
			  Date date_co3 = formate.parse(coCitisSendingImportDate);
			  invoiceheader.coCitisSendingImportDate=date_co3
		  }
		  if(DepartParis){
		  Date date3 = formate.parse(DepartParis);
		  invoiceheader.fromPariseDate=date3
		  }
		  if(ArrivalSH){
		  Date date4 = formate.parse(ArrivalSH);
		  invoiceheader.arriveDate=date4
		  }
		  if(BLReceiveDate){//BL received date
		  Date date5 = formate.parse(BLReceiveDate);
		  invoiceheader.baoLongReceiveDate=date5
		  }
		  
		  if(qADuration){
		  invoiceheader.qADuration=new Integer(qADuration)
		  }
		  
		  if(dOExchangeDate){
			  Date date6 = formate.parse(dOExchangeDate);
			  invoiceheader.dOExchangeDate=date6
			  }
		   
		  if(dutyPaidDate){
			  Date date7 = formate.parse(dutyPaidDate);
			  invoiceheader.dutyPaidDate=date7
			  }
		  if(inspectionDate){
			  Date date8 = formate.parse(inspectionDate);
			  invoiceheader.inspectionDate=date8
			  }
		  
			  if(vatDutyAmount)
			  invoiceheader.vatDutyAmount=vatDutyAmount

		  
		  if(FinCustCleaDate){
		  Date date9 = formate.parse(FinCustCleaDate);
		  invoiceheader.clearanceDate=date9
		  }
		  
		  invoiceheader.directToStore=directToStore
		  if(warehouseInDate){
			  Date date10 = formate.parse(warehouseInDate);
			  invoiceheader.warehouseInDate=date10
			  }
		  if(rcvdNumersOfCartons){
			 // int recode = ().intValue()
		  invoiceheader.rcvdNumersOfCartons=new Integer(rcvdNumersOfCartons)
		  }

		  invoiceheader.damagedCartonNo=damagedCartonNo
	  
		   if(shortagePcs){
		  invoiceheader.shortagePcs=shortagePcs
		   }
		  if(rcvDOInstructionFromStoreDate){
			  Date date11 = formate.parse(rcvDOInstructionFromStoreDate);
			  invoiceheader.rcvDOInstructionFromStoreDate=date11
			  }
		  if(requiredDeliveryByStoreDate){
			  Date date12 = formate.parse(requiredDeliveryByStoreDate);
			  invoiceheader.requiredDeliveryByStoreDate=date12
			  }

		  if(DeliveryToStoreDate){
		  Date date13 = formate.parse(DeliveryToStoreDate);
		  invoiceheader.deliveryDate=date13
		  }
		  invoiceheader.remark=remark
		  
		  
		  if (invoiceheader&&!invoiceheader.save(flush: true)) {
			  invoiceheader.errors.each {
				log.info it
			  }
			}
		  
		  
		  }
		  
		  }
	 
	 //System.err.println("params="+params)

	 render(view: "/report/report5",params:params)

	//render("");
    }
 
 
 
 
 
 
 
 
 
 	//Kurt Edited
 	def UpdateImportReport={
		 log.info params

		 def invoiceId =  params['invoiceId']
		 def invoiceDate=params['invoiceDate']
		 def HreceivedInvoceDate=params['HreceivedInvoceDate']
		 def coCitisImportDate=params['CoCitisImport']
		 def coCitisExportDate=params['CoCitisExport']
		 def coCitisSendingImportDate=params['CoCitisSendingImport']
		 def DepartParis=params['DepartParis']
		 def ArrivalSH=params['ArrivalSH']
		 def BLReceiveDate=params['BLReceiveDate']
		 def qADuration=params['qADuration']
		 def dOExchangeDate=params['dOExchangeDate']
		 def dutyPaidDate=params['dutyPaidDate']
		 def inspectionDate=params['inspectionDate']
		 def vatDutyAmount=params['vatDutyAmount']
		 def FinCustCleaDate=params['FinCustCleaDate']
		 def directToStore=params['directToStore']
		 def warehouseInDate=params['warehouseInDate']
		 def rcvdNumersOfCartons=params['rcvdNumersOfCartons']
		 def damagedCartonNo=params['damagedCartonNo']
		 def shortagePcs=params['shortagePcs']
		 def rcvDOInstructionFromStoreDate=params['rcvDOInstructionFromStoreDate']
		 def requiredDeliveryByStoreDate=params['requiredDeliveryByStoreDate']
	
		 def DeliveryToStoreDate=params['DeliveryToStoreDate']
		 def remark=params['remark']

		 def invoiceheader = InvoiceHeader.get(invoiceId)
		 
		 def sameHawbInvoiceHeaderList
		 if(invoiceheader){
		 
		 
		 sameHawbInvoiceHeaderList = InvoiceHeader.withCriteria{
			 
			 business{
				 and{
					 eq('hawb',invoiceheader.business.hawb)
					 not{
						 eq('hawb','')
					 }
				 }
			 }
		 }
		 //System.err.println("sameHawbInvoiceHeaderList="+sameHawbInvoiceHeaderList.size())
		 }
		 if(sameHawbInvoiceHeaderList.size()==0)
		 sameHawbInvoiceHeaderList=invoiceheader
		 sameHawbInvoiceHeaderList.each {
			 invoiceheader=it

		 if(invoiceheader){
			 SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
			 if(invoiceDate){
			 Date date = formate.parse(invoiceDate);
			 invoiceheader.invoiceDate=date
			 }
			 if(HreceivedInvoceDate){
			 Date date2 = formate.parse(HreceivedInvoceDate);
			 invoiceheader.receiveDate=date2
			 }
			 
			 if(coCitisImportDate){
				 Date date_co1 = formate.parse(coCitisImportDate);
				 invoiceheader.coCitisImportDate=date_co1
			 }
			 if(coCitisExportDate){
				 Date date_co2 = formate.parse(coCitisExportDate);
				 invoiceheader.coCitisExportDate=date_co2
			 }
			 if(coCitisSendingImportDate){
				 Date date_co3 = formate.parse(coCitisSendingImportDate);
				 invoiceheader.coCitisSendingImportDate=date_co3
			 }
			 if(DepartParis){
			 Date date3 = formate.parse(DepartParis);
			 invoiceheader.fromPariseDate=date3
			 }
			 if(ArrivalSH){
			 Date date4 = formate.parse(ArrivalSH);
			 invoiceheader.arriveDate=date4
			 }
			 if(BLReceiveDate){//BL received date  
			 Date date5 = formate.parse(BLReceiveDate);
			 invoiceheader.baoLongReceiveDate=date5  
			 }
			 
			 if(qADuration){
			 invoiceheader.qADuration=new Integer(qADuration)
			 }
			 
			 if(dOExchangeDate){
				 Date date6 = formate.parse(dOExchangeDate);
				 invoiceheader.dOExchangeDate=date6
				 }
			  
			 if(dutyPaidDate){
				 Date date7 = formate.parse(dutyPaidDate);
				 invoiceheader.dutyPaidDate=date7
				 }
			 if(inspectionDate){
				 Date date8 = formate.parse(inspectionDate);
				 invoiceheader.inspectionDate=date8
				 }
			 
				 
				 invoiceheader.vatDutyAmount=vatDutyAmount

			 
			 if(FinCustCleaDate){
			 Date date9 = formate.parse(FinCustCleaDate);
			 invoiceheader.clearanceDate=date9
			 }
			 
			 invoiceheader.directToStore=directToStore
			 if(warehouseInDate){
				 Date date10 = formate.parse(warehouseInDate);
				 invoiceheader.warehouseInDate=date10
				 }
			 if(rcvdNumersOfCartons){
				// int recode = ().intValue()
			 invoiceheader.rcvdNumersOfCartons=new Integer(rcvdNumersOfCartons)
			 }

			 invoiceheader.damagedCartonNo=damagedCartonNo
		 
			 
			 
			 invoiceheader.shortagePcs=shortagePcs
			 if(rcvDOInstructionFromStoreDate){
				 Date date11 = formate.parse(rcvDOInstructionFromStoreDate);
				 invoiceheader.rcvDOInstructionFromStoreDate=date11
				 }
			 if(requiredDeliveryByStoreDate){
				 Date date12 = formate.parse(requiredDeliveryByStoreDate);
				 invoiceheader.requiredDeliveryByStoreDate=date12
				 }

			 if(DeliveryToStoreDate){
			 Date date13 = formate.parse(DeliveryToStoreDate);
			 invoiceheader.deliveryDate=date13
			 }
			 invoiceheader.remark=remark

		 }
		 if (invoiceheader&&!invoiceheader.save(flush: true)) {
			 invoiceheader.errors.each {
			   log.info it
			 }
		   }
		// System.err.println("invoiceId="+invoiceId);
		// System.err.println("invoiceDate="+invoiceDate);
		 //System.err.println("HreceivedInvoceDate="+HreceivedInvoceDate);
		// System.err.println("DepartParis="+DepartParis);
		// System.err.println("ArrivalSH="+ArrivalSH);
		// System.err.println("BLReceiveDate="+BLReceiveDate);
		// System.err.println("FinCustCleaDate="+FinCustCleaDate);
		// System.err.println("DeliveryToStoreDate="+DeliveryToStoreDate);
		 //System.err.println("remark="+remark);
		  }
		 render("")
	 }
	 
	 //Kurt Edited
	 def getStoreListWithoutAll={
		 def storelist =new ArrayList()
		 //def allStoreCode= HermesCustomer.findAll();
		 def sql = new Sql(dataSource);
		def query = " select re_invoice_pre_code as reInvoicePreCode from hermes_customer order by re_invoice_pre_code desc  ";
		def allStoreCode = sql.rows(query,[]);

		 allStoreCode.each{
			 storelist.add(it.reInvoicePreCode)
		 }
		 return storelist
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
	 
	 
	 //Kurt Edited
	 def listDeliveryReport={
		 def store =  params['store']
		 if(store==null){
			 def res=getStoreListWithoutAll()
			 store=res[0]
		 }
		 def cocitis=params.cocitis
		 if(!cocitis)
		 cocitis=0
		 else
		 cocitis=1
		 def i=0
		// System.err.println("cocitis="+cocitis)
		// System.err.println("store="+store)
		 def instanceList=getDeliveryReportlist(store,cocitis)
		 		 	  def pager = [:]
					  pager.page=1
					  pager.total=1
					  pager.records=instanceList.size()+1;
					  pager.max=instanceList.size()+1; 
  
		 JqgridJSON jqgridJSON = new JqgridJSON(pager);
		 instanceList.each{elem->
			 def cell = new ArrayList()
			 cell.add(elem.dept)
			  cell.add(elem.podium)
			  cell.add(elem.total)
						 
			 i=i+1
			   jqgridJSON.addRow(i, cell)
			 
		 }
		 
		 render jqgridJSON.json as JSON
 
	 }
	 
	 
	 //Kurt Edited
	 def listKBReport={
		 
		 def inYear =  params['inYear']
		 def inMonth =  params['inMonth']
		 def store =  params['store']
		 if(store==null){
			 def res=getStoreList()
			 store=res[0]
		 }
		 if(store=='All')
		 store=null
		 if(inYear=='this year')
		 inYear=null
		 if(inMonth=='this month')
		 inMonth=null
		// System.err.println("inYear="+inYear);
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
		 
		 
		// System.err.println("searchBegindate:"+searchBegindate);
		// System.err.println("searchEnddate:"+searchEnddate);
		 def criteriaClosure = {

				product{
					  
					    
						  or{
							  and{
							  like('name','%Kelly%')
							  not{ 
								  like('name','%So-Kelly%')
							  	}
							  }
							  like('name','%Birkin%')
						  }
					   
					 eq('dept','C')
				}
				
				invoiceHeader{
					and{
						between('invoiceDate',searchBegindate,searchEnddate)
						business{
							
								customer{
									if(store)
									eq('reInvoicePreCode',store)
									order('reInvoicePreCode','asc')
								}
						}
					}
					
				}
				
			
				 
		}
		 
		 def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,false);
		 def pager =jqgridFilterService.afterPager(params,instanceList.size())
		 //def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,true);
		 JqgridJSON jqgridJSON = new JqgridJSON(pager);
		 def i=-1
		// System.err.println(instanceList.size())
		 def num=0
		 instanceList.each{elem->
			// System.err.println("elem.product.styleCode="+elem.product.styleCode);
			// def mag= getMagnItemInfo(elem.product.styleCode)
			// System.err.println("mag="+mag);
			// if(mag)//&&(mag.magn=='KELLY'||mag.magn=='BIRKIN')
			// {
				 num=num+1
				 
				 def cell = new ArrayList()

			 cell.add(elem.id)
			// System.err.println("elem.id="+elem.id);
			// System.err.println(elem.invoiceHeader.business.customer.reInvoicePreCode);
			 cell.add(elem.invoiceHeader.business.customer.reInvoicePreCode)
			 String DATE_FORMAT = "yyyy-MM-dd";
			 def sdf = new SimpleDateFormat(DATE_FORMAT);
			 if(elem.invoiceHeader.invoiceDate)
			 cell.add(sdf.format(elem.invoiceHeader.invoiceDate))
			 else
			 cell.add(null)

			 cell.add(elem.invoiceHeader.code)
			 //cell.add(elem.product.styleCode)
			 cell.add(elem.product.skuCode)
			 // cell.add(mag[0])
			 // cell.add(mag[1])
			 cell.add(elem.product.name)
			 String name_en=elem.product.name;
			 if(name_en.toLowerCase().contains("kelly"))
			 cell.add('KELLY')
			 else
			 cell.add('BIRKIN')
			 
			  cell.add(elem.quantity)
			  cell.add(elem.invoiceHeader.business.cocitis)
						 
			 i=i+1
			   jqgridJSON.addRow(i, cell)
			// }
		 }
		 jqgridJSON.json().records =  num
		 render jqgridJSON.json as JSON
		 
		 
	 }
	 
	 //Kurt edited  得到magnItem 表中相关的refno的信息
	 def getMagnItemInfo(refno){
		 
		 def sql = new Sql(dataSource);
		  
	  
		 def query = "select description, magndept from hermes_magnitem where refno=? and (magndept='KELLY' or magndept='BIRKIN')"
		 def result = sql.rows(query,[refno]);
		  if(result.size()>0)
		   
		 return result[0]
		 else
		 return null;
	 }
	 
	 
	 
	 //Kurt Edited
	 def hiddenCoCitis={
		 def cocitis=params.cocitis
		 if(cocitis)
		 return false
		 else
		 return true
		 }
	 
	 // Kurt Edited
	 def listFollowupReport={
		 log.info params
		 def cocitis=params.cocitis
		 def inYear =  params['inYear']
		 def store =  params['store']
		 def inMonth=params['inMonth']
		 if(store==null){
			 def res=getStoreList()
			 store=res[0]
		 }
		 //System.err.println("cocitis="+cocitis)
		 if(store=='All')
		 store=null
		 if(inYear=='this year')
		 inYear=null
		 if(inMonth=='this month')
		 inMonth=null
		 //System.err.println("inYear="+inYear);
		 //System.err.println("store="+store);
		// System.err.println("inMonth="+inMonth);
		 Date searchBegindate=new Date();
		 Date searchEnddate=new Date();
		// System.err.println(searchBegindate);
		 if(inYear){
			 searchBegindate.setYear (new Integer(inYear)-1900);
			 searchEnddate.setYear (new Integer(inYear)-1900);
		 }
		 if(inMonth&&inMonth!='All'){
			 searchBegindate.setMonth(new Integer(inMonth)-1);
			 searchEnddate.setMonth(new Integer(inMonth)-1);
			 //searchEnddate.setMonth(searchBegindate.getMonth()+1);
		 }
		 else if(inMonth&&inMonth=='All'){
			 searchBegindate.setMonth(new Integer(1)-1);
			 searchEnddate.setMonth(new Integer(12)-1);
			 
		 }
		 
		 searchBegindate.setDate(new Integer(1));
		 searchBegindate.setHours(new Integer(0));
		 searchBegindate.setMinutes (new Integer(0));
		 searchBegindate.setSeconds (new Integer(0));
		 
		 if(inMonth==null||inMonth&&inMonth!='All')
		 searchEnddate.setMonth(searchBegindate.getMonth()+1);
		 else if(inMonth&&inMonth=='All')
		 searchEnddate.setMonth(new Integer(12));
		 searchEnddate.setDate(new Integer(0));
		 searchEnddate.setHours(new Integer(23));
		 searchEnddate.setMinutes (new Integer(59));
		 searchEnddate.setSeconds (new Integer(59));

		 //System.err.println("searchBegindate:"+searchBegindate);
		 //System.err.println("searchEnddate:"+searchEnddate);
		 
		 
		 /*
		 Date searchdate=new Date();
		 if(!inYear||inYear=='this year'){
			 searchdate=new Date();
		 }
		 else{
			 searchdate.setYear (new Integer(inYear)-1900);
		 }
		  
		  String str=(searchdate.getYear()+1900).toString();
		  str=str.substring(2);
		  inYear=str;*/
		 //System.err.println("inYear="+str);
		 def criteriaClosure = {
			 
			 invoiceHeader{
				 and{
					 between('invoiceDate',searchBegindate,searchEnddate)
					 business{
						and{
						
						//eq('inYear',inYear)
					 
						if(cocitis)
						eq('cocitis',true)
						else
						eq('cocitis',false)
					
						customer{
						if(store)
						eq('reInvoicePreCode',store)
							
						}
						}
					 }
					order('reInvoice','asc')
				 }
			 }
			 
		}
		 def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,false);
		 def pager=jqgridFilterService.afterPager(params,instanceList.size())
		// def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,true);
		 JqgridJSON jqgridJSON = new JqgridJSON(pager);
		 //System.err.println("ok instanceList.size="+instanceList.size());
		 def i=-1
		 instanceList.each{elem->
			 def cell = new ArrayList()
			// cell.add(i+1)
			 //System.err.println("elem.id="+elem.id)
			 //System.err.println("elem.id="+elem.id)
			 cell.add(elem.id)
			 //System.err.println("elem.invoiceHeader.reInvoice="+elem.invoiceHeader.reInvoice)
			 cell.add(elem.invoiceHeader.reInvoice)
			 //System.err.println("elem.invoiceHeader.code="+elem.invoiceHeader.code)
			 cell.add(elem.invoiceHeader.code)
			 //System.err.println("aaaaaaaaaaa")
			 //System.err.println("elem.podium="+elem.podium)
			 cell.add(elem.podium)
			// System.err.println("elem.invoiceHeader.invoiceDate="+elem.invoiceHeader.invoiceDate)
			 cell.add(elem.invoiceHeader.invoiceDate)
			 //System.err.println("bbbbbb")
			 if( !elem.metaClass.hasProperty(elem,'product'))
			 {
				 System.err.println("product is null !!elem.id="+elem.id)
			 }
			 
			 else
			 {
				 //System.err.println("elem.product.dept="+elem.product.dept)
				 if( elem.product.metaClass.hasProperty(elem.product,'dept'))
				 	cell.add(elem.product.dept)
				 else
				 	cell.add('')
				 //System.err.println("elem.product.familyCode="+elem.product.familyCode)
				 if( elem.product.metaClass.hasProperty(elem.product,'familyCode'))
					 cell.add(elem.product.familyCode)
				 else
					 cell.add('')
				 
				 //System.err.println("elem.product.productCode="+elem.product.productCode)
				 if( elem.product.metaClass.hasProperty(elem.product,'productCode'))
				 	cell.add(elem.product.productCode)
				 else 
				 	cell.add('')
				
				 //System.err.println("elem.product.styleCode="+elem.product.styleCode)
					 
				 if( elem.product.metaClass.hasProperty(elem.product,'styleCode'))
				 	cell.add(elem.product.styleCode)
				 else
					 cell.add('')
				 //System.err.println("elem.product.skuCode="+elem.product.skuCode)
			     if( elem.product.metaClass.hasProperty(elem.product,'skuCode'))
				 	cell.add(elem.product.skuCode)
				 else
					 cell.add('')
				 def content=''
				 //System.err.println("111111")
				 if(elem.productName&&elem.productName.length()>0){ //不使用master
					  
					 content= elem.productName +elem.product.name + elem.product.category.description
				}
				else{
				 
					content= elem.product.category.name +elem.product.name + elem.product.category.description
				}
				//System.err.println("222222")
				cell.add(content)
				cell.add(elem.quantity)
				def df = new DecimalFormat("#.00")
				def mf = new DecimalFormat("#.0000")
				//def adjItem = logisticService.getAdjustInvoiceItem(elem.invoiceHeader.business.id)
				//if( adjItem && elem.id == adjItem.id ){
					//cell.add(elem.amount*elem.quantity+elem.invoiceHeader.business.totalAdjDiff)
					if(elem.logsAdjAmount){
					cell.add(df.format(elem.logsAdjAmount*elem.quantity))
					cell.add(elem.logisticFreight)
					//cell.add(elem.amount*elem.quantity+elem.logisticFreight+elem.invoiceHeader.business.totalAdjDiff)
					cell.add(df.format(elem.logsAdjAmount*elem.quantity+elem.logisticFreight))
					}
					else{
						cell.add('')
						cell.add('')
						//cell.add(elem.amount*elem.quantity+elem.logisticFreight+elem.invoiceHeader.business.totalAdjDiff)
						cell.add('')
					}
				//}
				/*else{
					cell.add(elem.amount*elem.quantity)
					cell.add(elem.logisticFreight)
					cell.add(elem.amount*elem.quantity+elem.logisticFreight)
				}*/
				//	System.err.println("333333")
				if(elem.quantity!=0&&elem.logsAdjAmount){
					//if( adjItem && elem.id == adjItem.id ){
						 
						//cell.add("<div style='background-color:red'>"+df.format((elem.amount*elem.quantity+elem.logisticFreight+elem.invoiceHeader.business.totalAdjDiff)/elem.quantity)+"</div>")
					//}
					//else{
					//def df = new DecimalFormat("#.00")
					cell.add(mf.format((elem.logsAdjAmount*elem.quantity+elem.logisticFreight)/elem.quantity))
					//}
				}
				else
				cell.add('')
				//System.err.println("44444")
				
				  
				 String DATE_FORMAT = "yyyy";
				 def sdf = new SimpleDateFormat(DATE_FORMAT);
				 String DATE_FORMAT2 = "MM";
				 def sdf2 = new SimpleDateFormat(DATE_FORMAT2);
				 if(elem.invoiceHeader.invoiceDate){
				 cell.add(sdf.format(elem.invoiceHeader.invoiceDate))
				 cell.add(sdf2.format(elem.invoiceHeader.invoiceDate))
				 }
				 else{
				 cell.add(null)
				 cell.add(null)
				 }
	
				 i=i+1
				   jqgridJSON.addRow(i, cell)
			 }
			 
		 }
		 
		 render jqgridJSON.json as JSON
		  
	 }
 
	 def refreshSaga={
		if(params.year1==null)
		params.year1=2011
		if(params.inMonth==null)
		  params.inMonth=1
		// System.err.println(params.year1)
		  
		//  params.year1=2012
		//  params.inMonth=5
		 def sql = new Sql(dataSource)
		  def query= " select distinct(shipment.id) as shipid "
		  			 query=query+" from hermes_business as business  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id "
					 query=query+" inner join hermes_customer as customer on business.customer_id=customer.id inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id "
				//	query=query+"  where not business.delivery_date is null and customer.re_invoice_pre_code NOT  like '%KM%'  AND customer.re_invoice_pre_code  NOT like '%NJ%' AND customer.re_invoice_pre_code  not like '%CD%' AND customer.re_invoice_pre_code not  like '%PR%' AND customer.re_invoice_pre_code not  like '%HB%' AND customer.re_invoice_pre_code  not like '%SU%' and customer.re_invoice_pre_code  not like '%QD%' and customer.re_invoice_pre_code  not like '%HZ%'  and  customer.re_invoice_pre_code  not like '%WX%' and customer.re_invoice_pre_code not  like '%GZ%' and  customer.re_invoice_pre_code not like '%SY%' AND customer.re_invoice_pre_code not  like '%BJ%' and customer.re_invoice_pre_code not  like '%SH%' and customer.re_invoice_pre_code not  like '%SZ%'  "
				 	    query=query+"  where not business.delivery_date is null   "
					 
					  if(params.year1 && params.year1 != ''&& params.year1 != 'All years') query = query+" and year(business.delivery_date) = '" +params.year1 +"'"
					  if(params.inMonth&&params.inMonth!='All')query = query+" and month(business.delivery_date) = '" +params.inMonth +"'"
		// System.err.println(query)
					  def result=sql.rows(query)
					  
					  
		//  System.err.println("shipment qty="+result.size())
		 for(int i=0;i<result.size();i++){
			 def unitMap=result.get(i)
			 def shipmentId=unitMap.shipid
		//	 System.err.println("num"+i)
			// shipmentId=3646
			 def shipment=Shipment.get(shipmentId)
			// financeService.updateSummaryShipment(shipment)
			 financeService.updateShipmentAPBLBfdf(shipment)
		 }
		// System.err.println("shipment qty="+result.size())
				/*	  def instance2=Shipment.list();
		 instance2.each{  elem->
			 if(elem.id>300){
				 System.err.println(elem.id)
				 financeService.updateSummaryShipment(elem)
			 }
		 }*/ //由于效率问题注释掉
		 render(text:" shipment refresh over",view:"/report/report1")
	 }
	 
	 
	 
 	//Kurt Edited
 	def listImportReport={
		 log.info params
		 def Hawb =  params['hawb']
		 def store = params['store']
		 
		 def inYear=params.inYear
		 def inMonth=params.inMonth
		 
		 if(inYear=='this year')
		 inYear=null
		 if(inMonth=='this month')
		 inMonth=null
		 //System.err.println("inYear="+inYear);
		// System.err.println("store="+store);
		 Date searchBegindate=new Date();
		 Date searchEnddate=new Date();
		// System.err.println(searchBegindate);
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
 
		// System.err.println("searchBegindate:"+searchBegindate);
		 //System.err.println("searchEnddate:"+searchEnddate);
 
		 
		 
		 
		 if(store==null){
			 def res=getStoreList()
			 store=res[0]
		 }
		 if(store&&store=='All')
		 store=null;
		 
		 def cocitis=params.cocitis
		 //System.err.println(cocitis);
		def criteriaClosure = {
			and{
				//if(store)
				//like('reInvoice','%'+store+'%')
					business{
						and{
						if(Hawb) 
						like('hawb','%'+Hawb+'%')
						//order('hawb','asc')
						if(cocitis)
						eq('cocitis',true)
						else
						eq('cocitis',false)
						if(store)
						customer{
							eq('reInvoicePreCode',store)
						}
						}
					} 
					
					between('invoiceDate',searchBegindate,searchEnddate)
			}
			order('receiveDate','asc')
			}
		
		 def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceHeader,criteriaClosure,false);
		 def pager=jqgridFilterService.afterPager(params,instanceList.size())
		// def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceHeader,criteriaClosure,true);
		 JqgridJSON jqgridJSON = new JqgridJSON(pager);
		 //System.err.println("bb");
		 def i=-1
		 //int index=0
		 instanceList.each{elem->
			/* index++;
			 if(index>=100){
				 render jqgridJSON.json as JSON
				 return;
			 }*/
			  
			 def cell = new ArrayList()
			// cell.add(i+1)
			 cell.add(elem.id)
			 cell.add(elem.business.hawb)
			 cell.add(elem.reInvoice)
			 cell.add(elem.code)
			// cell.add("1")
			// cell.add("2")
			// elem.invoiceItems.count
			 cell.add(getInvoiceNoQuantity(elem.code))
			 cell.add(getInvoiceNoCartonQuantity(elem.code))
			 String DATE_FORMAT = "yyyy-MM-dd";
			 def sdf = new SimpleDateFormat(DATE_FORMAT);
			 if(elem.invoiceDate)
			 cell.add(sdf.format(elem.invoiceDate))
			 else
			 cell.add(null)
			
			 if(elem.receiveDate)
			 cell.add(sdf.format(elem.receiveDate))
			 else
			 cell.add(null)
			 /*
			  * 
			  *  
			  */
			 
			 if(elem.coCitisImportDate)
			 cell.add(sdf.format(elem.coCitisImportDate))
			 else
			 cell.add(null)
			 if(elem.coCitisExportDate)
			 cell.add(sdf.format(elem.coCitisExportDate))
			 else
			 cell.add(null)
			 if(elem.coCitisSendingImportDate)
			 cell.add(sdf.format(elem.coCitisSendingImportDate))
			 else
			 cell.add(null)
			 
			 if(elem.fromPariseDate)
			 cell.add(sdf.format(elem.fromPariseDate))
			 else
			 cell.add(null)
			 if(elem.arriveDate)
			 cell.add(sdf.format(elem.arriveDate))
			 else
			 cell.add(null)
			 
			 if(elem.baoLongReceiveDate)
			 cell.add(sdf.format(elem.baoLongReceiveDate))
			 else
			 cell.add(null)
			 
			 cell.add(elem.qADuration)
			 
			 if(elem.dOExchangeDate)
			 cell.add(sdf.format(elem.dOExchangeDate))
			 else
			 cell.add(null)
			 
			 if(elem.dutyPaidDate)
			 cell.add(sdf.format(elem.dutyPaidDate))
			 else
			 cell.add(null)
			 
			 if(elem.inspectionDate)
			 cell.add(sdf.format(elem.inspectionDate))
			 else
			 cell.add(null)
			 
			 cell.add(elem.vatDutyAmount)
			 
			 if(elem.clearanceDate)
			 cell.add(sdf.format(elem.clearanceDate))
			 else
			 cell.add(null)
			 
			 cell.add(elem.directToStore)
			 
			 if(elem.warehouseInDate)
			 cell.add(sdf.format(elem.warehouseInDate))
			 else
			 cell.add(null)
			 
			 cell.add(elem.rcvdNumersOfCartons)
			 cell.add(elem.damagedCartonNo)
			 cell.add(elem.shortagePcs)

			 if(elem.rcvDOInstructionFromStoreDate)
			 cell.add(sdf.format(elem.rcvDOInstructionFromStoreDate))
			 else
			 cell.add(null)
			 
			 if(elem.requiredDeliveryByStoreDate)
			 cell.add(sdf.format(elem.requiredDeliveryByStoreDate))
			 else
			 cell.add(null)	 
			 
			 if(elem.deliveryDate)
			 cell.add(sdf.format(elem.deliveryDate))
			 else
			 cell.add(null)

			 cell.add(elem.remark)
			 
			 //System.err.println(elem.reInvoice);
			 i=i+1
			   jqgridJSON.addRow(i, cell)
		 }
		 
		 render jqgridJSON.json as JSON

}


     

    def report5={
		//System.err.println("report55");
    }
    
	def getInvoiceNoQuantity(invoiceNo){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = "select floor(sum(quantity)) from hermes_invoiceitem  it inner join hermes_invoiceheader hdr on it.invoice_header_id=hdr.id where hdr.code= '"+invoiceNo+"' "
		def TotalQuantity = sql.rows(query,[]);
		 if(!TotalQuantity)
		 TotalQuantity[0][0]=0
		return TotalQuantity[0][0]
	}
	
	def getBLImportInvoiceDate(invoiceNo){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = "select packing.export_date from hermes_invoiceheader hdr inner join hermes_business bs on hdr.business_id =bs.id inner join "
			query =query+ "  hermes_packing packing ON bs.id = packing.business_id where hdr.code='"+invoiceNo+"' "

		def TotalQuantity = sql.rows(query,[]);

		return TotalQuantity[0][0]
	}
	
	//Kurt edited  
	def getDeliveryReportlist(store,cocitis){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = " select dept, it.podium,sum(quantity) as total from hermes_invoiceitem it inner join hermes_product pro on pro.id=it.product_id "
			query=query+" inner join hermes_invoiceheader hdr on hdr.id=it.invoice_header_id "
			query=query+" inner join hermes_business bus on bus.id=hdr.business_id "
			query=query+" inner join hermes_customer cust on cust.id=bus.customer_id "
			query=query+" where dept<>'' and cust.re_invoice_pre_code= ? and bus.cocitis= ?  group by dept,it.podium order by dept asc "

		def Totallist = sql.rows(query,[store,cocitis]);

		return Totallist
	}
	
	
	
	
	
	def getInvoiceNoCartonQuantity(invoiceNo){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = "select count(*) from hermes_packing_carton pack where id in( select pkit.carton_id  from hermes_packingcarton_item pkit inner join hermes_invoiceitem it on pkit.invoice_item_id=it.id "
			query = query +"  inner join hermes_invoiceheader hdr on it.invoice_header_id=hdr.id "
			query = query +" where hdr.code='"+invoiceNo+"' group by pkit.carton_id )"
		def TotalQuantity = sql.rows(query,[]);
		 if(!TotalQuantity)
		 TotalQuantity[0][0]=0
		return TotalQuantity[0][0]
	}
	
	
    //Kurt edited 
    def exportReport1 ={
		//System.err.println("exportReport1")
	     
		if(params.year1==null)
		params.year1=2011
		if(params.inMonth==null)
		params.inMonth=1
	    //def query = "select invoiceitem.id as invoiceItemId, business.to_scala, customer.id as customerid, shipment.rate,invoiceitem.shipment_duty as tariff ,(invoiceitem.shipmentdl+ invoiceitem.shipmentyz) as FreightAgent,invoiceitem.shipment_cost,shipment.bal as apbl,shipment.bfdf,customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.code as suppliercode ,invoiceheader.code as sellierinv,invoiceheader.code as reSellierinv, shipment.total_amount as sellieramt, invoiceitem.quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceheader.code,invoiceheader.recode from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join category  on product.id =category.id inner join hermes_customer as customer on business.customer_id=customer.id inner join party as supplier on business.supplier_id=supplier.id inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where 1=1"
	   def sql = new Sql(dataSource)
	    //def query = "select invoiceitem.id as invoiceItemId, business.to_scala, customer.id as customerid, shipment.rate,invoiceitem.shipment_duty as tariff ,(invoiceitem.shipmentdl+ invoiceitem.shipmentyz) as FreightAgent,invoiceitem.shipment_cost,shipment.bal as apbl,shipment.bfdf,customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.code as suppliercode ,invoiceheader.code as sellierinv,invoiceheader.code as reSellierinv, shipment.total_amount as sellieramt, invoiceitem.quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceheader.code,invoiceheader.recode from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join category  on product.id =category.id inner join hermes_customer as customer on business.customer_id=customer.id inner join party as supplier on business.supplier_id=supplier.id inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where 1=1"
	    //def query = "select customer.loc_id,business.total_difference as diff,(business.total_amount+business.total_freight+business.total_issuance) as cif,business.to_scala, customer.id as customerid, shipment.rate,shipment.totalBLGS as tariff ,(shipment.bldl+ shipment.blyz) as FreightAgent,shipment.totalBLCost,shipment.bal as apbl,shipment.bfdf,customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,business.delivery_date  as year1 ,supplier.code as suppliercode ,business.po_code as sellierinv,business.po_code as reSellierinv, shipment.total_amount as sellieramt, floor(business.total_quantity) as quantity,shipment.totalBLGS as duty,business.currency,business.so_code from hermes_business as business inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where (not business.delivery_date is null) "
	   def query= " select   hdr.contract_code as contractcode,DATEPART(Month,business.delivery_date) as Month, DATENAME(Year,business.delivery_date) as year1, hdr.report1depts as dept,shipment.id as shipid, customer.loc_id,business.to_scala,customer.id as customerid, shipment.rate,  convert(decimal(15,2),(sum(item.quantity*item.logs_adj_amount+item.logistic_freight+item.logistic_issuance)) )as sellieramt, convert(decimal(15,2),(sum(item.quantity*item.logs_adj_amount+item.logistic_freight+item.logistic_issuance)*shipment.rate) ) as sellieramtRMB, sum(floor(item.quantity)) as quantity, convert(decimal(15,2), sum(item.shipment_duty)) as tariff , "
		query=query+" convert(decimal(15,2),sum(item.shipmentdl+ item.shipmentyz)) as FreightAgent,convert(decimal(15,2),(sum(shipment.rate*(item.quantity*item.logs_adj_amount+item.logistic_freight+item.logistic_issuance)+item.shipment_duty+item.shipmentdl+ item.shipmentyz))) as totalBLCost,convert(decimal(15,2),hdr.report1Apbl) as apbl, "
		query=query+" customer.re_invoice_pre_code as store, customer.re_invoice_pre_code as reinvoice,supplier.code as suppliercode ,business.currency,business.so_code, item.po_code as sellierinv, hdr.report1bfdf as bfdf , hdr.total_aphl_for_this_contract_code as aphl  from hermes_business as business "
		query=query+" inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id "
		query=query+" inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id "
		query=query+" inner join hermes_invoiceheader hdr on hdr.business_id=business.id "
		query=query+" inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id "
		query=query+" inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  where (not business.delivery_date is null)"
		 
		
		def paramstring =''
	    if(params.store && params.store != '') paramstring = " and customer.re_invoice_pre_code like '%" +params.store +"%'" 
	    if(params.reinvoice && params.reinvoice != '') paramstring += " and hdr.re_invoice like '%" +params.reinvoice +"%'" 
	    if(params.suppliercode && params.suppliercode != '') paramstring += " and supplier.code like '%" +params.suppliercode +"%'" 
	    if(params.sellierinv && params.sellierinv != '') paramstring += " and business.po_code like '%" +params.sellierinv +"%'" 
	    if(params.year1 && params.year1 != ''&& params.year1 != 'All years') paramstring += " and year(business.delivery_date) = '" +params.year1 +"'" 
		if(params.inMonth&&params.inMonth!='All')paramstring += " and month(business.delivery_date) = '" +params.inMonth +"'"
		query=query + paramstring
		query=query+" group by hdr.total_aphl_for_this_contract_code,  hdr.contract_code, hdr.re_invoice, hdr.report1bfdf,hdr.report1apbl,hdr.report1depts,customer.loc_id,business.to_scala,customer.id, shipment.rate,customer.re_invoice_pre_code, customer.re_invoice_pre_code,business.delivery_date,supplier.code ,business.currency,business.so_code,item.po_code,shipment.id,business.total_adj_diff order by hdr.re_invoice asc "


		def unitMap
		def resultsMap = []
	    def results = sql.rows(query)
	    def subamt=0,subamtrmb=0,subqty=0,subap=0
	    def subcif=0,subtariff=0,subfa=0,subcost=0,subdiff=0,subapbl=0,subaphl=0
		log.info query
		//log.info 'testlog'
		//def df = new DecimalFormat("#.00")
		 
		
	    if (results.size() > 0) {
			for(int i=0;i<results.size();i++){
				unitMap=results.get(i)
				//def InvoiceItemInstance = InvoiceItem.get(unitMap.invoiceItemId)

				subamt += unitMap.sellieramt
				subamtrmb += unitMap.sellieramtRMB
				subqty += unitMap.quantity
				subap += unitMap.bfdf
				
				//dept information here
				 /*def query2="select distinct(product.dept) from hermes_product product inner join hermes_invoiceItem item on product.id=item.product_id "
					query2=query2+" where item.po_code ='"+unitMap.sellierinv+"'"
					def depts = sql.rows(query2)
					
					depts.each{
						unitMap.dept+=it.dept
					} */
					
					 /*
					def query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
						query3=query3+" inner join hermes_product product on product.id=item.product_id "
						query3=query3+" where (not business.delivery_date is null) "
						query3=query3+" and product.magnitude='Non Hermes & others' "
						query3=query3+" and shipment.id ="+unitMap.shipid
						query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
						def ap_segalist = sql.rows(query3)
					 if(ap_segalist.size()>0){
						 if(ap_segalist[0].sellierinv==unitMap.sellierinv){
							 unitMap.bfdf=ap_segalist[0].bfdf
							 unitMap.apbl=unitMap.apbl-unitMap.bfdf
						 }
					 }
					 else{
						 query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
						 query3=query3+" inner join hermes_product product on product.id=item.product_id "
						 query3=query3+" where (not business.delivery_date is null) "
						 query3=query3+" and product.magnitude<>'Non Hermes & others' "
						 query3=query3+" and shipment.id ="+unitMap.shipid
						 query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
						  ap_segalist = sql.rows(query3)
						  
						  if(ap_segalist.size()>0&&ap_segalist[0].sellierinv==unitMap.sellierinv){
							  unitMap.bfdf=ap_segalist[0].bfdf
							  unitMap.apbl=unitMap.apbl-unitMap.bfdf
						  }
						  
					 }
					 */
						
					
				//Calendar c = Calendar.getInstance();
				//c.setTime(unitMap.year1)
				//Calendar cc = Calendar.getInstance();
				//cc.setTime(unitMap.invoice_date)
				
				//unitMap.year1= c.get(Calendar.YEAR)
				unitMap.store= unitMap.store.trim()
				//unitMap.Month= c.get(Calendar.YEAR) +""+c.get(Calendar.MONTH)+1

				//unitMap.Month = String.format('%tY%<tm', c)
			   // BigDecimal rate = 10
			   //if(unitMap.rate!=''){rate =new BigDecimal(Double.parseDouble(unitMap.rate))}
				//rate = unitMap.rate
				//rate = 10
				//unitMap.cif =InvoiceItemInstance.getShipmentUP(rate)
				//unitMap.diff=InvoiceItemInstance.getShipmentDiff(rate)
				//subcif += unitMap.cif
				subtariff +=unitMap.tariff
				subfa += unitMap.FreightAgent
				subcost += unitMap.totalBLCost
				//subdiff += unitMap.diff
				subapbl += unitMap.apbl
				subaphl += unitMap.aphl
				resultsMap.add(unitMap)
			}
			
	    }
			//[reportList:resultsMap,subamt:subamt,subamtrmb:subamtrmb,subqty:subqty,subap:subap,subcif:subcif,subtariff:subtariff,subfa:subfa,subcost:subcost,subdiff:subdiff,subapbl:subapbl]

				response.setContentType("application/excel"); 
				response.setHeader("Content-disposition", "attachment;filename=listOfPO.xls")  
				HSSFWorkbook wb = new HSSFWorkbook()   
				def helper = wb.getCreationHelper()
				def sheet = wb.createSheet("List of PO")   
				sheet.setColumnWidth(0, (short) (5320))		//20
				sheet.setColumnWidth(1, (short) (3250));	//12
				sheet.setColumnWidth(2, (short) (3250));	//12
				sheet.setColumnWidth(3, (short) (3250));	//12
				sheet.setColumnWidth(4, (short) (3250));	//12
				//sheet.setColumnWidth(5, (short) (3250));	//12
				sheet.setColumnWidth(5, (short) (3250));	//12
				sheet.setColumnWidth(6, (short) (3250));	//12
				sheet.setColumnWidth(7, (short) (3250));	//12
				sheet.setColumnWidth(8, (short) (3250));	//12
				//sheet.setColumnWidth(10, (short) (3250));	//12
				sheet.setColumnWidth(9, (short) (3250));	//12
				sheet.setColumnWidth(10, (short) (3250));	//12
				//sheet.setColumnWidth(13, (short) (3250));	//12
				sheet.setColumnWidth(11, (short) (7820));	//30
				sheet.setColumnWidth(12, (short) (3250));	//12
				//sheet.setColumnWidth(16, (short) (3250));	//12
				sheet.setColumnWidth(13, (short) (3250));	//12
				//sheet.setColumnWidth(18, (short) (3250));	//12
				sheet.setColumnWidth(14, (short) (3250));	//12
				sheet.setColumnWidth(15, (short) (3250));	//12
				
				     
				def row = null  
				def cell = null  
				def rowi = -1
				rowi=rowi+1
				row = sheet.createRow(rowi)  
				row.createCell(0).setCellValue("Store")
				row.createCell(1).setCellValue("Re-invoice")
				row.createCell(2).setCellValue("Year")
				row.createCell(3).setCellValue("Supplier Code")
				row.createCell(4).setCellValue("Sellier Inv")
				//row.createCell(5).setCellValue("Resellier Inv")
				row.createCell(5).setCellValue("Sellier Amt")
				row.createCell(6).setCellValue("@")
				row.createCell(7).setCellValue("Sellier Amt(RMB)")
				row.createCell(8).setCellValue("QTY")
				//row.createCell(10).setCellValue("CIF")
				row.createCell(9).setCellValue("Tariff")
				row.createCell(10).setCellValue("Freight +Agent")
				//row.createCell(13).setCellValue("diff")
				row.createCell(11).setCellValue("Cost")
				row.createCell(12).setCellValue("Month")
				//row.createCell(16).setCellValue("Status")
				row.createCell(13).setCellValue("Dpt")
				//row.createCell(18).setCellValue("Diff (<-10 or >10 will be Yellow)")
				row.createCell(14).setCellValue("AP-BL(RMB)")
				row.createCell(15).setCellValue("A-SEGA(RMB)")
				row.createCell(16).setCellValue("AP-HL(RMB)")
				row.createCell(17).setCellValue("Contract Code")
				
				resultsMap.each{
					rowi = rowi+1
					row = sheet.createRow(rowi)					
					row.createCell(0).setCellValue(it.loc_id)
					row.createCell(1).setCellValue(it.so_code)
					row.createCell(2).setCellValue(it.year1)
					row.createCell(3).setCellValue(it.suppliercode)
					row.createCell(4).setCellValue(it.sellierinv)
					//row.createCell(5).setCellValue('00'+it.reSellierinv)
					row.createCell(5).setCellValue(it.sellieramt)
					row.createCell(6).setCellValue(it.rate)
					row.createCell(7).setCellValue(it.sellieramtRMB)
					row.createCell(8).setCellValue(it.quantity)
					//row.createCell(10).setCellValue(it.cif)
					row.createCell(9).setCellValue(it.tariff)
					row.createCell(10).setCellValue(it.FreightAgent)
					//row.createCell(13).setCellValue('')
					row.createCell(11).setCellValue(it.totalBLCost)
					row.createCell(12).setCellValue(it.Month)
					//row.createCell(16).setCellValue(it.to_scala)
					row.createCell(13).setCellValue(it.dept)
					//row.createCell(18).setCellValue(it.diff)
					row.createCell(14).setCellValue(it.apbl)
					row.createCell(15).setCellValue(it.bfdf)
					row.createCell(16).setCellValue(it.aphl)
					row.createCell(17).setCellValue(it.contractcode)
				}
				def os = response.outputStream
				wb.write(os)  
		        os.close()
			
	     
    }    
    
	def exportReport5 ={
		//System.err.println("export");
		//System.err.println(params.issueDate9);
		render("")
	}
	
	def report7 ={
		
	   render(view: "/report/report7")
   }
	
	def report6 ={
		 
		render(view: "/report/report6")
	}
	
	
	def exportProductList={
		System.err.println("exportProductList  began")
		log.info  params
		def sql = new Sql(dataSource)
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		  
		def filename=properties.getProperty("exportTempFileFilePath")
		filename=filename+"database"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".xls"
		def procedurename="search_product_reference"

		def key = " exportData "+"'"+filename+"'"+",'"+procedurename+"'"
		//System.err.println("key="+key)
		def result = sql.rows("exec "+key)
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=ProductDatabase.xls")
		response.setCharacterEncoding("gb2312");
		File file=new File(filename);
		FileInputStream fis=new FileInputStream(file);
		BufferedInputStream buff=new BufferedInputStream(fis);

		byte [] b=new byte[1024];//相当于我们的缓存

		long k=0;//该值用于计算当前实际下载了多少字节

		//从response对象中得到输出流,准备下载

		OutputStream myout=response.getOutputStream();

		//开始循环下载

		while(k<file.length()){

			int j=buff.read(b,0,1024);
			k+=j;

			//将b中的数据写到客户端的内存
			myout.write(b,0,j);

		}

		//将写入到客户端的内存的数据,刷新到磁盘
		myout.flush();
		myout.close();
		/*if(file)
		file.delete();*/
		 
		
		
	 /*
		def criteriaClosure = {
			//not{
			//isNull('skuCode')
			//}
			order("id","desc")
		}
		 
		
		def productlist=HermesProduct.withCriteria(criteriaClosure)
				
		System.err.println(productlist.size())
		def totalsize=productlist.size()
		int sheetNum=totalsize/50000+1
		System.err.println(sheetNum)
		
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=ProductDatabase.xls")
		def os = response.outputStream
		HSSFWorkbook wb  = new HSSFWorkbook()
		def helper   = wb.getCreationHelper()
		int index=0;
		int sheet_index=1;
		int rowi = -1
		def sheet
		productlist.each{
			index=index+1
			if(index>100000){
				wb.write(os)
				os.close()
				return;
			}
			
			rowi=rowi+1;
			def row = null
			def cell = null
			if(rowi==0){
				sheet = wb.createSheet("ref"+sheet_index)
				sheet_index=sheet_index+1
				sheet.setColumnWidth(0, (short) (3250));	//12
				sheet.setColumnWidth(1, (short) (3250));	//12
				sheet.setColumnWidth(2, (short) (3250));	//12
				sheet.setColumnWidth(3, (short) (3250));	//12
				sheet.setColumnWidth(4, (short) (3250));	//12
				sheet.setColumnWidth(5, (short) (3250));	//12
				row = sheet.createRow(rowi)
				row.createCell(0).setCellValue("Dept")
				row.createCell(1).setCellValue("Speciality Code")
				row.createCell(2).setCellValue("Product Code")
				row.createCell(3).setCellValue("Short Ref.")
				row.createCell(4).setCellValue("Long Ref.")
				row.createCell(5).setCellValue("Item_Master_Description")
			}

			System.err.println("index"+index)
			row = sheet.createRow(rowi)
				row.createCell(0).setCellValue(it.dept)
				row.createCell(1).setCellValue(it.speciality)
				row.createCell(2).setCellValue(it.productCode)
				row.createCell(3).setCellValue(it.styleCode)
				row.createCell(4).setCellValue(it.skuCode)
				if(it.category)
				row.createCell(5).setCellValue(it.category.name+" "+it.name+" "+it.materialName)
				if(rowi==50000){
				rowi = -1				
				}
				
			
		}

		System.err.println("finish")
		wb.write(os)
		os.close()
		*/
		
		
	}
	
    def exportReport2 ={
		def unitMap
		def resultsMap = []
		def sql = new Sql(dataSource)
	    def query = "select convert(decimal(15,2), (shipment.rate*(invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight)+invoiceitem.shipmentyz+invoiceitem.shipmentdl+invoiceitem.shipment_duty)/invoiceitem.quantity) as Unitcost,convert(decimal(15,2), shipment.rate*(invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight))+invoiceitem.shipmentyz+invoiceitem.shipmentdl+invoiceitem.shipment_duty as TOTALRMB, invoiceitem.shipmentyz+invoiceitem.shipmentdl as OthercostRMB, business.total_adj_diff as total_adj_diff, business.id as bussiness_id,convert(decimal(15,2), shipment.rate*(invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight)) as sellieramtrmb, DATEPART(Month,business.delivery_date) as month1, DATENAME(Year,business.delivery_date) as year1, invoiceheader.invoice_year,invoiceheader.re_invoice,customer.loc_id,invoiceitem.podium,product.magnitude as magnitude,product.sku_code as longref,left(product.sku_code,8) as sr,shipment.rate, invoiceitem.id as invoiceItemId,business.delivery_date  as month2 ,supplier.name as supplier ,invoiceheader.code as invoiceheadercode,product.product_code as stockno,category.name as name_zh, category.code as shortref, product.dept as dep, product.magnitude as magnitude,convert(decimal(15,2),invoiceitem.logs_adj_amount*invoiceitem.quantity+invoiceitem.logistic_issuance+invoiceitem.logistic_freight) as sellieramt, floor(invoiceitem.quantity) as quantity,invoiceitem.shipment_duty as duty,product.dept,invoiceitem.shipmentyz,invoiceitem.shipmentdl,customer.re_invoice_pre_code as store from hermes_business as business  inner join hermes_invoiceheader as invoiceheader on invoiceheader.business_id=business.id inner join hermes_invoiceitem as invoiceitem on invoiceitem.invoice_header_id=invoiceheader.id inner join hermes_product as product on invoiceitem.product_id =product.id inner join product ori_product on ori_product.id =product.id inner join category  on ori_product.category_id =category.id inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id where business.delivery_date is not null and 1=1" 
	    def paramstring =''
		if(params.month==null)
		params.month=1
		if(params.year==null) 
		params.year=2011
		//System.err.println("shortref:"+params.shortref)
		//System.err.println("dep:"+params.dep)
	    if(params.year && params.year != ''&& params.year != 'All years') paramstring = paramstring+" and year(business.delivery_date) = '" +params.year +"'"
	    if(params.id && params.id != '') paramstring = paramstring+" and customer.re_invoice_pre_code like '%" +params.id +"%'" 
	    if(params.month && params.month != ''&& params.month !='All months') paramstring = paramstring+" and month(business.delivery_date) = '" +params.month +"'"
	    if(params.invoiceno && params.invoiceno != '') paramstring = paramstring+" and invoiceheader.re_invoice like '%" +params.invoiceno +"%'"
	    if(params.supplier && params.supplier != '') paramstring = paramstring+" and supplier.name like '%" +params.supplier +"%'"
	    if(params.paris && params.paris != '') paramstring = paramstring+" and invoiceheader.code like '%" +params.paris +"%'"
	    if(params.podium && params.podium != '') paramstring = paramstring+" and invoiceitem.podium like '%" +params.podium +"%'"
	    

	    if(params.stockno && params.stockno != '') paramstring = paramstring+" and  product.sku_code like '%" +params.stockno +"%'" 
	    if(params.shortref && params.shortref != '') paramstring = paramstring+" and category.code like '%" +params.shortref +"%'"
	    if(params.dep && params.dep != '') paramstring = paramstring+" and product.dept like '%" +params.dep +"%'"
	    if(params.nature && params.nature == 'Seasonal') paramstring = paramstring+" and (product.dept like '%D%' or product.dept like '%G%' or product.dept like '%H%' or product.dept like '%N%' or product.dept like '%Z%' ) "
		if(params.nature && params.nature == 'Non-Seasonal') paramstring = paramstring+" and (product.dept not like '%D%' and product.dept not like '%G%' and  product.dept not like '%H%' and product.dept not like '%N%' and product.dept not like '%Z%' ) "
		
		if(params.magnitudecode && params.magnitudecode != '') paramstring = paramstring+" and product.magnitude like '%" +params.magnitudecode +"%'"
		if( params.bp&&params.bp!= '')paramstring = paramstring+" and business.code like '%"+params.bp +"%'"
	    if(params.rate && params.rate != '') paramstring = paramstring+" and shipment.rate like '%" +params.rate +"%'"	    
	    query=query + paramstring
		//System.err.println("export:"+query)
	    def results = sql.rows(query)
	    def totalMap
		def totalqty = 0 ,totalamt =0,totalrmb = 0
		def totalamtrmb=0, totaldutyrmb=0, totalother=0
	    
	 
			for(int i=0;i<results.size();i++){
				unitMap=results.get(i)
//if(params.invoiceno && params.invoiceno != '')
				
				
				//def InvoiceItemInstance = InvoiceItem.get(unitMap.invoiceItemId)
				//unitMap.reinvoice = logisticService.getReInvoice(InvoiceItemInstance.invoiceHeader.business)
				
				def adjItem = null//logisticService.getAdjustInvoiceItem(unitMap.bussiness_id)
				def df = new DecimalFormat("#.00")
				def sf2 = new DecimalFormat("#.00")
				/*if( adjItem && unitMap.invoiceItemId == adjItem.id ){
					
					//unitMap.sellieramt=new BigDecimal(sf2.format(unitMap.sellieramt+unitMap.total_adj_diff))
					//unitMap.sellieramtrmb=unitMap.sellieramtrmb+unitMap.total_adj_diff*unitMap.rate
					//unitMap.TOTALRMB=unitMap.TOTALRMB+unitMap.total_adj_diff*unitMap.rate
					//if(unitMap.quantity!=0)
					//unitMap.Unitcost=unitMap.Unitcost+unitMap.total_adj_diff/unitMap.quantity
				}*/
				 
				//BigDecimal rate = 1
				//if(unitMap.rate!=''){rate =new BigDecimal(Double.parseDouble(unitMap.rate))} 
				//rate = unitMap.rate
				/*Calendar c = Calendar.getInstance();
				if(unitMap.month2)
				c.setTime(unitMap.month2)*/
				//unitMap.month1= c.get(Calendar.MONTH)+1
				//unitMap.year1=c.get(Calendar.YEAR)
				
				
				def dept = unitMap.dept.trim()
				if ('DGHNZ'.contains(dept)){
					unitMap.Nature='Seasonal'
				}else{
					unitMap.Nature='Non-Seasonal'
				}
				/*
				def adjusted_amount=0
				if(adjItem && InvoiceItemInstance.id == adjItem.id ){
					//adjusted_amount=InvoiceItemInstance.amount+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff/InvoiceItemInstance.quantity
					//unitMap.AMTRMB=InvoiceItemInstance.getShipmentAmt(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate
					//unitMap.DutyRMB=InvoiceItemInstance.shipmentDuty/(InvoiceItemInstance.getShipmentAmt(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate)*100
					//unitMap.TOTALRMB=InvoiceItemInstance.getShipmentUP(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate
					unitMap.Unitcost=InvoiceItemInstance.getShipmentUPS(rate)+InvoiceItemInstance.invoiceHeader.business.totalAdjDiff*rate/InvoiceItemInstance.quantity
					//unitMap.OthercostRMB=unitMap.shipmentyz+unitMap.shipmentdl
					}
					else{
						adjusted_amount=InvoiceItemInstance.amount
						unitMap.AMTRMB=InvoiceItemInstance.getShipmentAmt(rate)
						unitMap.DutyRMB=InvoiceItemInstance.getShipmentDuty()
						unitMap.TOTALRMB=InvoiceItemInstance.getShipmentUP(rate)
						unitMap.Unitcost=InvoiceItemInstance.getShipmentUPS(rate)
						unitMap.OthercostRMB=unitMap.shipmentyz+unitMap.shipmentdl
					}*/
					//unitMap.desc=InvoiceItemInstance.product.category.name
				 
 
				
				resultsMap.add(unitMap)


				totalqty += unitMap.quantity
				totalamt += unitMap.sellieramt
				totalrmb += unitMap.TOTALRMB
				totalamtrmb += unitMap.sellieramtrmb
				totaldutyrmb += unitMap.duty
				totalother += unitMap.OthercostRMB
				
				
				//unitMap.TOTALRMB=sf2.format(unitMap.TOTALRMB)
				//unitMap.sellieramtrmb=sf2.format(unitMap.sellieramtrmb)
				//unitMap.duty=sf2.format(unitMap.duty)
				//unitMap.OthercostRMB=sf2.format(unitMap.OthercostRMB)
				unitMap.Unitcost=df.format(unitMap.Unitcost)
			  }		
			  //[totalReport:totalMap,totalqtyReport:totalqty,totalamtReport:totalamt,totalrmbReport:totalrmb,reportList:resultsMap,totalamtrmb:totalamtrmb,totaldutyrmb:totaldutyrmb,totalother:totalother]
			
			response.setContentType("application/excel"); 
			response.setHeader("Content-disposition", "attachment;filename=Purchase.xls")  
			HSSFWorkbook wb = new HSSFWorkbook()   
			def helper = wb.getCreationHelper()
			def sheet = wb.createSheet("Purchase")   
			sheet.setColumnWidth(0, (short) (3250));	//12
			sheet.setColumnWidth(1, (short) (3250));	//12
			sheet.setColumnWidth(2, (short) (3250));	//12
			sheet.setColumnWidth(3, (short) (3250));	//12
			sheet.setColumnWidth(4, (short) (3250));	//12
			sheet.setColumnWidth(5, (short) (3250));	//12
			sheet.setColumnWidth(6, (short) (3250));	//12
			sheet.setColumnWidth(7, (short) (3250));	//12
			sheet.setColumnWidth(8, (short) (3250));	//12
			sheet.setColumnWidth(9, (short) (3250));	//12
			sheet.setColumnWidth(10, (short) (3250));	//12
			sheet.setColumnWidth(11, (short) (3250));	//12
			sheet.setColumnWidth(12, (short) (3250));	//12
			sheet.setColumnWidth(13, (short) (3250));	//12
			sheet.setColumnWidth(14, (short) (3250));	//30
			sheet.setColumnWidth(15, (short) (3250));	//12
			sheet.setColumnWidth(16, (short) (3250));	//12
			sheet.setColumnWidth(17, (short) (3250));	//12
			sheet.setColumnWidth(18, (short) (3250));	//12
			sheet.setColumnWidth(19, (short) (3250));	//12
			sheet.setColumnWidth(20, (short) (3250));	//12
			
			     
			def row = null  
			def cell = null  
			def rowi = -1
			rowi=rowi+1
			row = sheet.createRow(rowi)  
			row.createCell(0).setCellValue("Mon")
			row.createCell(1).setCellValue("Store")
			row.createCell(2).setCellValue("Year")
			row.createCell(3).setCellValue("Inv#")
			row.createCell(4).setCellValue("Sup")
			row.createCell(5).setCellValue("Podium")
			row.createCell(6).setCellValue("Paris#")
			row.createCell(7).setCellValue("Stock No.")
			row.createCell(8).setCellValue("Short")
			row.createCell(9).setCellValue("Dep")
			row.createCell(10).setCellValue("Nature")
			row.createCell(11).setCellValue("Magn")
			row.createCell(12).setCellValue("Qty")
			row.createCell(13).setCellValue("Amt(EUR/HKD)")
			row.createCell(14).setCellValue("Rate")
			row.createCell(15).setCellValue("Amt(RMB)")
			row.createCell(16).setCellValue("Duty(RMB)")
			row.createCell(17).setCellValue("Other cost(RMB)")
			row.createCell(18).setCellValue("Total(RMB)")
			row.createCell(19).setCellValue("Unit Cost(RMB)")

			resultsMap.each{
				rowi = rowi+1
				row = sheet.createRow(rowi)					
				row.createCell(0).setCellValue(it.month1)
				row.createCell(1).setCellValue(it.loc_id)
				row.createCell(2).setCellValue(it.year1)
				row.createCell(3).setCellValue(it.re_invoice)
				row.createCell(4).setCellValue(it.supplier)
				row.createCell(5).setCellValue(it.podium)
				row.createCell(6).setCellValue(it.invoiceheadercode)
				row.createCell(7).setCellValue(it.longref)
				
				row.createCell(8).setCellValue(it.sr)
				
				//如果是None Hermes 跳出部门，否则不显示  (现在又要求全部显示PER  YEFENG)
				row.createCell(9).setCellValue(it.dept)
				/*if(it.magnitude.equals("Non Hermes & others"))
				row.createCell(9).setCellValue(it.dept)
				else
				row.createCell(9).setCellValue('')
				*/
				//end
				def dept = it.dept.trim()
				def nature=''
				if ('DGHNZ'.contains(dept)){
					nature='Seasonal'
				}else{
					nature='Non-Seasonal'
				}
				row.createCell(10).setCellValue(nature)
				
				//row.createCell(10).setCellValue(it.magnitude)
				row.createCell(11).setCellValue(it.magnitude)
				row.createCell(12).setCellValue(it.quantity)
				row.createCell(13).setCellValue(it.sellieramt)
				row.createCell(14).setCellValue(it.rate)
				row.createCell(15).setCellValue(it.sellieramtrmb)
				row.createCell(16).setCellValue(it.duty)
				row.createCell(17).setCellValue(it.OthercostRMB)
				row.createCell(18).setCellValue(it.TOTALRMB)
				row.createCell(19).setCellValue(it.Unitcost)
				
			}
			def os = response.outputStream
			wb.write(os)  
	        os.close()			
			
			
			
	    
	}
    
    
}




