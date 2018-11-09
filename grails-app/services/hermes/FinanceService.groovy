package hermes

import groovy.sql.Sql

class FinanceService extends BaseService {
	static transactional = true
	def dataSource 

	//新建或更新shipment
	/* shipment section */
	def updateShipment(id,code,description,totalAdjDiff,blGS,blYZ,blDL,rate,bfdf,bal,bps,check='0'){
		def shipment
		if(id){
			shipment = Shipment.get(id)
		}else{
			shipment = new Shipment()
			shipment.code = code
			shipment.bfdf = new BigDecimal(0)
			shipment.blGS = new BigDecimal(0)
			shipment.blYZ = new BigDecimal(0)
			shipment.blDL = new BigDecimal(0)
			
		}
		shipment.description = description
		shipment.rate = new BigDecimal(rate)
		//shipment.bfdf = new BigDecimal(bfdf)
		//shipment.bal = new BigDecimal(bal)  //关税

		//shipment.blGS = new BigDecimal(blGS)
		//shipment.blYZ = new BigDecimal(blYZ)
		//shipment.blDL = new BigDecimal(blDL)
		shipment.submit = Integer.parseInt(check)
		//bps.grep(currentBps);
		//log.info bps
		//list.grep(['a', 'cde', 'ab'])
		/*
		bps.each{
			def business = Business.findByCode(it)
			if(business)
				shipment.addToBusiness(business)
		}
		*/
		if(!shipment.save(flush:true)){
			shipment.errors.each{
				log.info it
			}
		}
       // System.err.println("update shipment bfdf="+shipment.bfdf)
		updateSummaryShipment(shipment)
		//edited by kurt
		updateShipmentAPBLBfdf(shipment)
		return shipment
	}	

	def getShipmentItems(shipment){
		def ids = []
		shipment.business.each{
			ids.add(it.code)
		}
		def items = InvoiceItem.withCriterial{
			invoiceHeader{
				business{
					'in'("code",ids)
				}
			}
		}		
		return items
	}
	def getAdjustInvoiceItem(shipment){
		//TODO: the logic is error, because teh projection get property id is error.
		def ids = []
		shipment.business.each{
			ids.add(it.code)
		}
		if(ids.size()){
			def ii = InvoiceItem.withCriteria{
				projections{
					property('amount')
					property("id")
				}
				invoiceHeader{
					business{
						'in'("code",ids)
					}
				}
				maxResults(1)
				order("amount", "desc")						
			}	
			def item = InvoiceItem.withCriteria{
				eq("amount",ii[0][0])
				eq("quantity",new BigDecimal(1))
				invoiceHeader{
					business{
						'in'("code",ids)
					}
				}
			}
			return item[0]
		}else{
			return null
		}
	}
	
	//Kurt added shipment  apbl - bfdf
	def updateShipmentAPBLBfdf(shipment){
		def report1bfdf=0
		def report1apbl=0
		def sellierinv=''
		def sql = new Sql(dataSource)
		def query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
		query3=query3+" inner join hermes_product product on product.id=item.product_id "
		query3=query3+" where "  //(not business.delivery_date is null)
		query3=query3+"  product.magnitude<>'Non Hermes & others' "
		query3=query3+" and shipment.id ="+shipment.id
		query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
		def ap_segalist = sql.rows(query3)
	 if(ap_segalist.size()>0){
		// if(ap_segalist[0].sellierinv==unitMap.sellierinv){
			 report1apbl=ap_segalist[0].apbl
			 report1bfdf=ap_segalist[0].bfdf
			// report1apbl=report1apbl-report1bfdf
			 sellierinv=ap_segalist[0].sellierinv
		// }
	 }
	 else{
		 query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
		 query3=query3+" inner join hermes_product product on product.id=item.product_id "
		 query3=query3+" where "//(not business.delivery_date is null)
		 query3=query3+"  product.magnitude='Non Hermes & others' "
		 query3=query3+" and shipment.id ="+shipment.id
		 query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
			  
		
		  ap_segalist = sql.rows(query3)
		  if(ap_segalist.size()>0){
			 report1apbl=ap_segalist[0].apbl
			 report1bfdf=ap_segalist[0].bfdf
			// report1apbl=report1apbl-report1bfdf
			 sellierinv=ap_segalist[0].sellierinv
		  }
		  
	 }
	 shipment=Shipment.get(shipment.id)
	 if(shipment){
	 shipment.business.each{bp->
		 bp.invoiceHeaders.each{invoice->
			 def apbl=0
			 
			 invoice.invoiceItems.each{
				// apbl=apbl+it.shipmentDuty+it.shipmentDL+ it.shipmentYZ
				// totalAmount = totalAmount + (it.logsAdjAmount)*it.quantity+it.logisticFreight+it.logisticIssuance
			//	 totalQuantity = totalQuantity + it.quantity
			 }
			 if(invoice.code==sellierinv){
				 //invoice.report1Apbl=report1apbl
				 invoice.report1Apbl=report1apbl - report1bfdf
				 invoice.report1Bfdf=report1bfdf
				 //invoice.report1Bfdf=shipment.bfdf
			 }
			 else{
				 invoice.report1Apbl=apbl
				 invoice.report1Bfdf=0
			 }
			 //System.err.println("invoice.report1Apbl="+invoice.report1Apbl)
			 //System.err.println("invoice.report1Bfdf="+invoice.report1Bfdf)
		 }
	 }
	// System.err.println("total3 bfdf="+shipment.bfdf)
	 shipment.save(flush:true)
	 }

	}
	
	
	//Kurt edited 更新shipment的summary
	def updateSummaryShipment(shipment){
		def totalAmount = 0
		def totalQuantity = 0
		/* from the shipment business to update shipment total amount and quantity */
		/* first the business must to update the totalamount and totalquantity */
		/*shipment.business.each{
			totalAmount = totalAmount + it.totalAmount
			totalQuantity = totalQuantity + it.totalQuantity
		}*/
		/*
		def report1bfdf=0
		def report1apbl=0 
		def sellierinv=''
		def sql = new Sql(dataSource)
		def query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
		query3=query3+" inner join hermes_product product on product.id=item.product_id "
		query3=query3+" where "  //(not business.delivery_date is null)
		query3=query3+"  product.magnitude<>'Non Hermes & others' "
		query3=query3+" and shipment.id ="+shipment.id
		query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
		def ap_segalist = sql.rows(query3)
	 if(ap_segalist.size()>0){
		// if(ap_segalist[0].sellierinv==unitMap.sellierinv){
		 	report1apbl=ap_segalist[0].apbl
			 report1bfdf=ap_segalist[0].bfdf
			// report1apbl=report1apbl-report1bfdf
			 sellierinv=ap_segalist[0].sellierinv
		// }
	 }
	 else{
		 query3=" select top 1 convert(decimal(15,2),sum(item.shipment_duty+item.shipmentdl+ item.shipmentyz)) as apbl,  item.po_code as sellierinv, shipment.bfdf as bfdf from hermes_business as business  inner join hermes_customer as customer on business.customer_id=customer.id left join party as supplier on business.supplier_id=supplier.id  inner join hermes_shipment_hermes_business as shipmentbusiness on shipmentbusiness.business_id=business.id  inner join hermes_invoiceheader hdr on hdr.business_id=business.id  inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id  inner join hermes_shipment as shipment on shipment.id=shipmentbusiness.shipment_business_id  "
		 query3=query3+" inner join hermes_product product on product.id=item.product_id "
		 query3=query3+" where "//(not business.delivery_date is null)
		 query3=query3+"  product.magnitude='Non Hermes & others' "
		 query3=query3+" and shipment.id ="+shipment.id
		 query3=query3+"  group by item.po_code,shipment.bfdf order by apbl desc"
		 	 
		
		  ap_segalist = sql.rows(query3)
		  if(ap_segalist.size()>0){
			 report1apbl=ap_segalist[0].apbl
			 report1bfdf=ap_segalist[0].bfdf
			// report1apbl=report1apbl-report1bfdf
			 sellierinv=ap_segalist[0].sellierinv
		  }
		  
	 }
	 */
	 //System.err.println("report1apbl="+report1apbl)
	//	System.err.println("report1bfdf="+report1bfdf)
	 /* proportion the blDL and blYZ total cost to invoice item by the shipment totalAmount percentage */

	 updateShipmentItemsPrice(shipment)
		
		shipment.business.each{bp->
			bp.invoiceHeaders.each{invoice->
				def apbl=0
				
				invoice.invoiceItems.each{
					apbl=apbl+it.shipmentDuty+it.shipmentDL+ it.shipmentYZ
					totalAmount = totalAmount + (it.logsAdjAmount)*it.quantity+it.logisticFreight+it.logisticIssuance
					totalQuantity = totalQuantity + it.quantity
				}
				/*if(invoice.code==sellierinv){
					//invoice.report1Apbl=report1apbl 
					invoice.report1Apbl=report1apbl - report1bfdf
					invoice.report1Bfdf=report1bfdf
					//invoice.report1Bfdf=shipment.bfdf
				}
				else{
					invoice.report1Apbl=apbl
					invoice.report1Bfdf=0
				}*/
				//System.err.println("invoice.report1Apbl="+invoice.report1Apbl)
				//System.err.println("invoice.report1Bfdf="+invoice.report1Bfdf)
			}
		}
		shipment.totalAmount = totalAmount
		shipment.totalQuantity = totalQuantity
		//System.err.println("total amount="+totalAmount)
		/* sum the baolong duty price from shipment->business->invoiceheader->invoiceitem */
		def ids = []
		shipment.business.each{
			ids.add(it.code)
		}
		log.info ids

		if(ids.size()>0){
			def c = InvoiceItem.createCriteria()
			def total = c.get{
				projections{
					sum('shipmentDuty')
					sum('shipmentCost')
				}
				invoiceHeader{
					business{
						'in'("code",ids)
					}
				}
			}		
			log.info "total1="+total[0]
			log.info "total2="+total[1]
			/* update the baolong and hermes internal total cost */
			shipment.totalBLGS= total[0]    //sum from the import data baolong guanshui column
			shipment.totalBLCost = total[1] //sum from the import data baolong cost column
		//	System.err.println("total bfdf="+shipment.bfdf)
			
			/* proportion the blDL and blYZ total cost to invoice item by the shipment totalAmount percentage */
			//Move to the beginning of this function
			//updateShipmentItemsPrice(shipment)
		}
		shipment.save(flush:true)
	}
	//Kurt edited 更新item 价格
	def updateShipmentItemsPrice(shipment){
		def totalAmount = shipment.totalAmount
		//Kurt removed
		//def totalYZ = shipment.blYZ
		//def totalDL = shipment.blDL
		//END
		def totalHermesLogisticRmb = totalAmount*shipment.rate
		 
		def proportionYZ = 0
		def proportionDL = 0
		def totalAdj = 0
		def bfdf=0
		
		shipment.business.each{bp->
			bp.invoiceHeaders.each{invoice->
				invoice.invoiceItems.each{
					def rate = 1
					/*
					if(it.finiAdjAmount){
						rate = (it.getLogisticAmountTotal() + it.finiAdjAmount)/ totalAmount
					}else{
						rate = it.getLogisticAmountTotal()/totalAmount
					}
					*/
					//Kurt removed
					//rate = it.getLogisticAmountTotal()/totalAmount
					//it.shipmentYZ = totalYZ * (rate)
					//it.shipmentDL = totalDL * (rate)
					
					//it.save() 
					//END
					proportionYZ = proportionYZ + it.shipmentYZ
					proportionDL = proportionDL + it.shipmentDL
					if(it.finiAdjAmount){
						totalAdj = totalAdj + it.finiAdjAmount
					}
					if(it.shipmentBFDF){
						if(it.shipmentBFDF>bfdf)
						bfdf=it.shipmentBFDF
					}
					else
					bfdf=0
				}			
			}
		}
		//Kurt added
		shipment.blYZ=proportionYZ;
		shipment.blDL=proportionDL;
		//shipment.blGS=
		//END
		shipment.bfdf=bfdf
		shipment.totalAdjDiff = totalAdj
		shipment.totalHCost = shipment.totalBLGS + (proportionYZ + proportionDL) + totalHermesLogisticRmb
		shipment.totalDifference = shipment.totalBLCost - shipment.totalHCost + shipment.totalAdjDiff
		//System.err.println("total 2 bfdf="+shipment.bfdf)
	
	}
	
	def getShipmentRate(invoiceHeader){
		def shipment = Shipment.findAll("from Shipment s join s.business b join b.invoiceHeaders ih where ih = ?",[invoiceHeader])
		if(shipment){
			return shipment[0][0].rate
		}else{
			return 1
		}
	}
	
	def getShipmentCode(user){
		def nid=user.id
		def id=String.valueOf(nid)
		def usercode=''
		if (nid<10) {
			usercode='000'+id
		}else{
			if (nid<100) {
				usercode='00'+id
			}else{
				if (id<1000) {
					usercode='0'+id
				}else{
					usercode=id
				}
			}
		}
		//精确到秒
		def sdate=String.format('%tY', Calendar.getInstance())+String.format('%tm', Calendar.getInstance())+String.format('%td', Calendar.getInstance())+String.format('%tH', Calendar.getInstance())+String.format('%tM', Calendar.getInstance())+String.format('%tS', Calendar.getInstance())
		def sql = new Sql(dataSource);
		def query = "select max(right(code,4)) as code from hermes_shipment where left(code,12)=?"
		def results = sql.rows(query,['SH'+usercode+sdate])
		if ((results.size()==0)||(results[0].code==null)){
			return 'SH'+usercode+sdate+'0001'
		}else{
			return 'SH'+usercode+sdate+String.valueOf(Integer.parseInt(results[0].code)+1)
		}
	}
	
	def deleteUnValidSubmittedShipment(){
		try{
			def sql = new Sql(dataSource);
			def query= "delete bus from hermes_shipment sh left join hermes_shipment_hermes_business bus on sh.id=bus.shipment_business_id  "
			query =query+" where submit=1 and ( bus.shipment_business_id  is null) "
			query =query+" delete sh from hermes_shipment sh left join hermes_shipment_hermes_business bus on sh.id=bus.shipment_business_id "
			query =query+" where submit=1 and ( bus.shipment_business_id  is null) "
			sql.execute(query)
			} catch (Exception e) {
				println e.getMessage()
			}
		
	}
	

	def deleteUnSubmitShipment(user){
		def nid=user.id
		def id=String.valueOf(nid)
		def usercode=''
		if (nid<10) {
			usercode='000'+id
		}else{
			if (nid<100) {
				usercode='00'+id
			}else{
				if (id<1000) {
					usercode='0'+id
				}else{
					usercode=id
				}
			}
		}
		//def sdate=String.format('%tY', Calendar.getInstance())+String.format('%tm', Calendar.getInstance())+String.format('%td', Calendar.getInstance())
		try{
		def sql = new Sql(dataSource);
		sql.execute("delete from hermes_shipment where submit=0 and left(code,6)=?",['SH'+usercode])
		} catch (Exception e) {
			println e.getMessage()
		}
	}	
}
