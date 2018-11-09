package hermes

import groovy.sql.Sql
import java.text.SimpleDateFormat
import java.text.DecimalFormat

class LogisticService extends BaseService {
  	static transactional = true
	def updateBusiness(id,issuance,freight,totalAdjDiff,hawb,currency,cocitis,cocitisRemark){
		def business = Business.get(id)
		business.totalIssuance = issuance
		business.totalFreight = freight
		business.hawb = hawb
		if(totalAdjDiff!=null)
		business.totalAdjDiff = totalAdjDiff
		business.currency = currency
		//Kurt Edited
		business.cocitisRemark=cocitisRemark
		//END
		if(cocitis){
			business.cocitis = cocitis
		}else{
			business.cocitis = false
		}
		//invoiceHeader.recode = recode
		//invoiceHeader.reInvoice = cusCode + recode
		//invoiceHeader.reInvoice = reInvoice //TODO:cusCode + recode
		summeryBusiness(business)
		updateBPItemsPrice(business)
		business.save(flush: true)
	}
	def updateBPItemsPrice(bp){
		def totalAmount = bp.totalAmount
		def totalIssuance = bp.totalIssuance
		def totalFreight = bp.totalFreight	
		def adjItem = getAdjustInvoiceItem(bp.id)
		def proportionIssuance = 0
		def proportionFreight = 0
		bp.invoiceHeaders.each{invoice->
			 def deptslist=new ArrayList();
			 def depts=''
			invoice.invoiceItems.each {
				
				if(!deptslist.contains(it.product.dept)){
					deptslist.add(it.product.dept)
					depts=depts+it.product.dept
				}
				def rate = 0
				if(totalAmount>0){
					if(bp.totalAdjDiff!=null)
                 		rate=(it.amount*it.quantity) / (totalAmount-bp.totalAdjDiff)
						 //因为totalAmount已经算上totalAdjDiff了
				    else
					    rate=(it.amount*it.quantity) / totalAmount
				}
				def df = new DecimalFormat("#.00")
				def unitIssuance=df.format(totalIssuance * (rate)/it.quantity)
				//it.logisticIssuance = totalIssuance * (rate)
				it.logisticIssuance = new BigDecimal(unitIssuance) * it.quantity
				def unitFreight=df.format(totalFreight * (rate)/it.quantity)
				it.logisticFreight=new BigDecimal(unitFreight) * it.quantity
				//it.logisticFreight = totalFreight * (rate)
				if( adjItem && it.id == adjItem.id ){
					it.logsAdjAmount=it.amount+bp.totalAdjDiff / it.quantity
				}
				else
				it.logsAdjAmount=it.amount
				
				it.save(flush:true)  
				proportionIssuance = proportionIssuance + it.logisticIssuance
				proportionFreight = proportionFreight + it.logisticFreight
			}	
			invoice.report1Depts=depts
			if(invoice.report1Apbl==null)
			invoice.report1Apbl=0
			if(invoice.report1Bfdf==null)
			invoice.report1Bfdf=0
			//System.err.println("depts="+depts)
			invoice.save(flush:true)  
		}
		bp.totalDifference = (proportionIssuance + proportionFreight) - (totalIssuance + totalFreight) + bp.totalAdjDiff
		bp.totalFinalamount += bp.totalDifference
		bp.save(flush:true)
	}	
	
	def getCustomerByXMag(xmag){
		def cust=HermesCustomer.findByXmag(xmag);
		return cust;
	}
	
	
	def getInvoiceAmountNotNull(bp){
		def resu = "1"
		bp.invoiceHeaders.each{invoice->
			invoice.invoiceItems.each {
				if ((it.amount==0)||(it.amount==null)) {
					resu = "0"
				}
			}			
		}
		return resu
	}	
	
 
	
	def getInvoiceAmount(bp){
		def resu = 0
		bp.invoiceHeaders.each{invoice->
			invoice.invoiceItems.each {
				if ((it.logsAdjAmount!=null)&&(it.logsAdjAmount!=0)&&(it.quantity)&&(it.logisticIssuance!=null)&&(it.logisticFreight!=null)) {
					resu = resu + it.logsAdjAmount*it.quantity+it.logisticIssuance+it.logisticFreight
				}
			}			
		}
		return resu
	}	
	
	def updateInvoiceHeader(id,reInvoice,issuance,freight){
		def invoiceHeader = InvoiceHeader.get(id)
		invoiceHeader.totalIssuance = issuance
		invoiceHeader.totalFreight = freight
		//invoiceHeader.recode = recode
		//invoiceHeader.reInvoice = cusCode + recode
		invoiceHeader.reInvoice = reInvoice //TODO:cusCode + recode
		updateItemsPrice(invoiceHeader)
		invoiceHeader.save(flush: true)
	}
	//更新InvoiceItem 列表
	def updateInvoiceItem(id,material,material2,desczh,ismaster,country,amount,descen){
		def invoiceItem = InvoiceItem.get(id)
		if(invoiceItem){
			if (country.length() > 0)
				invoiceItem.country = country
			if (ismaster.equals("Yes")) {
				def category = invoiceItem.product.category
				category.name = desczh
				category.save()
				invoiceItem.productName = ''
			} else {
				invoiceItem.productName = desczh
			}
			//这里可能需要修改，满足材质可以包括Master和本数据
			//
			//
			def category = invoiceItem.product.category
			if (material.length() > 0) {
				//def product = invoiceItem.product
				//product.material = material
				//product.save()
				category.description = material
			}
			if (material2.length() > 0) {
				category.description2 = material2 //description2是材质2，Harry 2013年12月要求加入，用于本地更新
			}
			
			category.save()
			
			
			
			
			if(descen){
				def product = invoiceItem.product
				product.name=descen
				product.save()
			}
			
			
			//Kurt Edited
			if ((invoiceItem.amount==0)||(invoiceItem.amount==null)) {
				invoiceItem.amount=new BigDecimal(amount);
			}
			//可以修改amount
			//System.err.println("updateInvoiceItem amount="+amount)
			/*if (amount) {
				invoiceItem.amount=new BigDecimal(amount);
			}*/
			
			//END
			invoiceItem.save()
		}
	}
	def updateReceiveDate(items,receiveDate){
		items.each{
			def invoiceItem = InvoiceItem.get(it)
			if(invoiceItem){
				invoiceItem.receiveDate = receiveDate
				invoice.save()  
			}
		}
	}
	def updateHawb(items,hawb){
		items.each{
			def invoiceItem = InvoiceItem.get(it)
			if(invoiceItem){
				invoiceItem.hawb = hawb
				invoice.save()
			}
		}
	}
	def updateLogsAdjAmount(id,price) {
		/*
	     * 1. save adj price
	     * 2. re summery
	     * 3. how to see the origin price (hide columens 3.3:visible columns 3.6:column chooser
	     */		
		def business = Business.get(id)
	    def item = getAdjustInvoiceItem(id)
		item.logsAdjAmount = price
		item.save(flush:true)
		business.totalAdjDiff = price - item.logsAdjAmount
		summeryBusiness(business)
		updateBPItemsPrice(business)
	}
	def getAdjustInvoiceItem(id) {
		//the unitprice max
		//System.err.println("getAdjustInvoiceItem id="+id)
		def ii = InvoiceItem.withCriteria{
			projections{
				property('amount')
				property("id")
			}
			eq("quantity",new BigDecimal(1))
			invoiceHeader{
				business{
					eq("id",new Long(id))
				}
			}
			maxResults(1)
			order("amount", "desc")			
		}
		//System.err.println("avvasdf")
		//System.err.println("avvasdf ii="+ii)
		def item = InvoiceItem.withCriteria{
			if(ii)
			eq("amount",ii[0][0])
			//eq("quantity",new BigDecimal(1))
			invoiceHeader{
				business{
					eq("id",new Long(id))
				}
			}
			order("quantity","asc")
		}
		//System.err.println("avvasdf item="+item)
		return item[0]
	}
	//得到Packing的Reinvoice代码列表
	def getReInvoiceAllInString(business){
		def reinvoice = InvoiceHeader.withCriteria{
			
			eq('business',business)
			order("reInvoice","asc")
		}
		if(business.customer){
			  def str=''
			  reinvoice.each{
				  str=str+it.reInvoice+','
			  }
			  return str
		}else{
			return ''
		}
	}
	//invoice type+ 7位数字   AFE-7737213
	def getInvoiceCodeAllInString(business){
		def reinvoice = InvoiceHeader.withCriteria{
			
			eq('business',business)
			order("reInvoice","asc")
		}
		if(business.customer){
			  def str=''
			  reinvoice.each{
				  str=str+it.invoiceType+'-'+it.code+','
			  }
			  return str
		}else{
			return ''
		}
	}
	
	
	//Kurt edited 得到Packing的Reinvoice代码区间
	def getReInvoice(business){
		if(business.reInvoice!=null&&business.reInvoice!='')
		  return business.reInvoice
		else{
		
		def reinvoice = InvoiceHeader.withCriteria{
			projections{
				min('recode')
				max('recode')
			}
			eq('business',business)
		}
		def re_invoice
		if(business.customer){
			java.text.DecimalFormat format = new java.text.DecimalFormat("0000");
			
			if(reinvoice[0][0]!=reinvoice[0][1])
			re_invoice= business.customer.reInvoicePreCode+business.inYear+"-"+format.format(reinvoice[0][0])+" ~ "+business.customer.reInvoicePreCode+business.inYear+"-"+format.format(reinvoice[0][1])   
			else
			re_invoice= business.customer.reInvoicePreCode+business.inYear+"-"+format.format(reinvoice[0][0])   
			
			business.reInvoice=re_invoice
			business.save()
			}
			
		return re_invoice
		}
	}
	
	//Kurt edited 得到Packing的Reinvoice代码区间  BJ10(0001~0004)
	def getReInvoice_fmt2(business){
		def reinvoice = InvoiceHeader.withCriteria{
			projections{
				min('recode')
				max('recode')
			}
			eq('business',business)
		}
		if(business.customer){
			java.text.DecimalFormat format = new java.text.DecimalFormat("0000");
			if(reinvoice[0][0]!=reinvoice[0][1])
			return business.customer.reInvoicePreCode+business.inYear+"("+format.format(reinvoice[0][0])+" ~ "+format.format(reinvoice[0][1])+")"
			else
			return business.customer.reInvoicePreCode+business.inYear+"("+format.format(reinvoice[0][0])+")"
			
			}else{
			return ''
		}
	}
	
	//Kurt Edited  Carton title 上的Type of Invoice
	def getInvoiceHead_TypeofInvoice(business){
		def invoice_Head = InvoiceHeader.withCriteria{
			
			eq('business',business)
		}
		 if(invoice_Head)
		    return invoice_Head.invoiceType
		 else
			return ''
		 
	}
	//显示Carton的title
	def getCartonTitleH(carton){
		if(carton){
			//Kurt edited
			//def typeofInvoice = getInvoiceHead_TypeofInvoice(carton.packing.business)
			//System.err.println("typeofinvoice=="+typeofInvoice)
			def cartonHeaderTitle = 'Carton:'+carton.code+' ' //+typeofInvoice+' '
			
			def hardCopy = ''
			if(carton.packing.hardCopy)
				hardCopy = carton.packing.hardCopy
			def expedition="No d'expedition:"+hardCopy+","
			def bpstr="BP:"+carton.packing.business.code+","
			def colis = 'No Colis:'+carton.code
			
			/*def result = InvoiceHeader.withCriteria{
				projections{
					min('code')
					max('code')
				}
				eq('business',carton.packing.business)
			}
			def invoiceStr = result[0][0] + "-"+ result[0][1] + ","
			*/
			//def invoiceStr =getReInvoiceAllInString(carton.packing.business) 改为
			def invoiceStr =getInvoiceCodeAllInString(carton.packing.business)
			
			cartonHeaderTitle = cartonHeaderTitle + invoiceStr + expedition + bpstr + colis
			return cartonHeaderTitle
		}else{
			return ''
		}
	}
	def getCartonTitleF(carton){
		
		if(carton){
			def gWeightT = "毛重Gross weight: "+carton.gWeight+" Kgs"
			def nWeightT = "净重Net weight: "+carton.nWeight+" Kgs"
			
			return gWeightT +" "+ nWeightT
		}else{
			return ''
		}
	}
	
	def cloneCarton(id){
		def carton = Carton.get(id)
	    if(carton){
			def packing = carton.packing
			if(packing){
				//克隆箱子信息
				def new_carton = new Carton()
				def code = getCartonNextCode(packing)
				new_carton.code = code
				new_carton.packing = carton.packing
				new_carton.description1 = carton.description1
				new_carton.description2 = carton.description2
				new_carton.nWeight=carton.nWeight
				new_carton.gWeight=carton.gWeight
				
				if (!new_carton.save(flush: true)) {
					new_carton.errors.each {
						log.info it
					}
					return  null
				}
				else
				{
					log.info "clone Carton:"+carton+" success"
					//克隆物品列表//updateCartonItem
					def CartonItemList = CartonItem.withCriteria{
							eq("carton",carton)
							order("sequence","asc")
					}
					 CartonItemList.each {
						  def new_cartonItem=new CartonItem()
						  new_cartonItem.carton=new_carton
						  new_cartonItem.invoiceItem=it.invoiceItem
						  def restQty=checkRestQuantityExistCartonProductTobePacked(it.invoiceItem)
						  
						  if(it.quantity<restQty)//若存货大于打包数量
						  new_cartonItem.quantity=it.quantity
						  else
						  new_cartonItem.quantity=restQty
						  
						  new_cartonItem.sequence=it.sequence
						  if (restQty!=0&&!new_cartonItem.save(flush: true)) {
							  new_cartonItem.errors.each {
								  log.info it
							  }
						
						  }
					  }

					
					return new_carton;
				}
			}
			
			
		}
		
		
	}
	
	
	def deleteCarton(id){
		def carton = Carton.get(id)
		if(carton){
			/*
			log.info "
			=========="
			
			carton.cartonItems.each{ci->
				carton.removeFromCartonItems(ci)
			}
			*/
			//carton.save()
			
			
			//def packing = carton.packing
			//carton.packing.removeFromCartons(carton)
			carton.delete()
			log.info "delete carton:"+carton+" success"
		}
	}
	def deleteCartonItem(cartonItemId){
		def cartonItem = CartonItem.get(cartonItemId)
		if(cartonItem){
			cartonItem.carton.removeFromCartonItems(cartonItem)
			cartonItem.delete()
		}
	}
	
	
	def checkRestQuantityExistCartonProductTobePacked(invoiceItem){
		
		def sql1 = new Sql(dataSource);
		def sql2 = new Sql(dataSource);

		def query1 = "select quantity from hermes_invoiceitem where id= ? "
		def TotalQuantity = sql1.rows(query1,[invoiceItem.id]);
		
		def query2 ="select sum(quantity) as packed from hermes_packingcarton_item where invoice_item_id = ?  "
		def PackedQuantity= sql2.rows(query2,[invoiceItem.id]);
		
		def restQuantity=0
		if(PackedQuantity[0][0])
		{
		 restQuantity=TotalQuantity[0][0]-PackedQuantity[0][0]
		}
		else
		{
			restQuantity=TotalQuantity[0][0]
		}

		return restQuantity
	}
	//检查是否该packing完成所有物品打包
	def checkIfBusinessItemAllPacked(packing)
	{
		if(!packing)
		return false;
		def sql1 = new Sql(dataSource);
		def sql2 = new Sql(dataSource);

		//System.err.println("packing.business.code"+packing.business.code)
		def query1 = " select sum(quantity) from hermes_invoiceitem it inner join hermes_invoiceheader hdr on it.invoice_header_id=hdr.id inner join hermes_business bs on hdr.business_id=bs.id where bs.code= ? "
				 
		def TotalQuantity = sql1.rows(query1,[packing.business.code]);

		//System.err.println("TotalQuantity="+TotalQuantity)
		def query2 =" select sum(quantity) from hermes_packingcarton_item it inner join hermes_packing_carton car on it.carton_id=car.id  inner join hermes_packing pa on pa.id=car.packing_id inner join hermes_business bs on bs.id=pa.business_id  where bs.code= ? "

		def PackedQuantity= sql2.rows(query2,[packing.business.code]);
		//System.err.println("PackedQuantity="+PackedQuantity)
		if(TotalQuantity[0][0]==null)
		TotalQuantity[0][0]=0;
		if(PackedQuantity[0][0]==null)
		PackedQuantity[0][0]=0;
		
		 if(TotalQuantity[0][0]==PackedQuantity[0][0])
		return true;
		else
		return false;
		
	}
	
	//Kurt 范围剩余可装的物品数，不计算自己的箱子
	def checkRestQuantityExistCartonProductTobePackedExceptSelf(invoiceItem,carton){
		
		def sql1 = new Sql(dataSource);
		def sql2 = new Sql(dataSource);
		//System.err.println("111")
		//System.err.println("checkExistCartonProductIsPacked")
		def query1 = "select quantity from hermes_invoiceitem where id= ? "
		def TotalQuantity = sql1.rows(query1,[invoiceItem.id]);
		//System.err.println("2222")
		//System.err.println("TotalQuantity="+TotalQuantity)
		def query2 ="select sum(quantity) as packed from hermes_packingcarton_item where invoice_item_id = ? and  carton_id <> ? "
		def PackedQuantity= sql2.rows(query2,[invoiceItem.id,carton.id]);
		//System.err.println("PackedQuantity="+PackedQuantity)
		//System.err.println("333")
		def restQuantity=0
		
		if(PackedQuantity[0][0])
		{
		 restQuantity=TotalQuantity[0][0]-PackedQuantity[0][0]
		}
		else
		{
			restQuantity=TotalQuantity[0][0]
		}
		//System.err.println("444")
		//System.err.println("invoiceItem= "+invoiceItem.id+" restQuantity=="+restQuantity)
		return restQuantity
	}
	
	
	def updateCartonWeight(cartonId,nweight,gweight)
	{
		def sql = new Sql(dataSource);
		 	def query = "update hermes_packing_carton set n_weight ="+nweight +", g_weight="+gweight+" where id = "+cartonId
		log.info query
		def results = sql.rows(query)
		log.info results
		 
			 
		
	}
	
	
	//Kurt Edited
	def updateCartonItem(oper,cartonId,invoiceItemId,cartonItemId,quantity,sequence){
		
		if(quantity&&quantity.toString().toBigDecimal()<0)
		quantity=1
		
		if(oper.equals("add") || oper.equals("edit")){
	        def carton = Carton.get(cartonId)
			def invoiceItem = InvoiceItem.get(invoiceItemId)
			if(carton && invoiceItem){
				//Kurt Edited 
				//若本箱子中已有该物品，更新其数量
				//def cartonItem = CartonItem.findByInvoiceItem(invoiceItem)
				
				def cartonItems = CartonItem.withCriteria{
					 and{
						eq("invoiceItem",invoiceItem)
						eq("carton",carton)
					 }
				}
				 
				 def cartonItem =cartonItems[0]
				//System.err.println("cartonItem.id=="+cartonItem.id)
				if(cartonItem){
					if(oper.equals("add")){
						//System.err.println("cartonItem.quantity =="+cartonItem.quantity)
						//System.err.println("To be packed quantity =="+quantity)
						
						def restQty=checkRestQuantityExistCartonProductTobePacked(invoiceItem)
						//System.err.println("restQty quantity =="+restQty)
						if(restQty.toString().toBigDecimal()>quantity.toString().toBigDecimal())//若打包数小于剩余可打包数
						{//System.err.println("restQty>quantity");
						cartonItem.quantity = cartonItem.quantity+new BigDecimal(quantity)
						}
						else
						{
							//System.err.println("restQty<quantity");
							cartonItem.quantity = cartonItem.quantity+new BigDecimal(restQty)
						}
						
						 
						
					}
					else if(oper.equals("edit"))
					{//检查数量是否超范围
						
						def restqty=checkRestQuantityExistCartonProductTobePackedExceptSelf(invoiceItem,carton);
						//System.err.println("restqty=="+restqty)
						if(quantity.toString().toBigDecimal()<=restqty.toString().toBigDecimal())
						{
							//System.err.println("aaa")
							BigDecimal qty=new BigDecimal(quantity)
							if(qty==0)qty=1;
							cartonItem.quantity = qty
							}
						
						else{
							//System.err.println("bbb")
							cartonItem.quantity=new BigDecimal(restqty)
						}
						
					}
					  /*if(sequence){
						cartonItem.sequence = new Integer(sequence)
					}else{
						cartonItem.sequence = getNextCartonItemSeq(carton)
					}*/
				}else{//若本箱子还没装过该物品
		            cartonItem = new CartonItem()
		            cartonItem.invoiceItem = invoiceItem
		            cartonItem.carton = carton  
					def restQty=checkRestQuantityExistCartonProductTobePacked(invoiceItem)
					if(restQty.toString().toBigDecimal()>quantity.toString().toBigDecimal())//若打包数小于剩余可打包数
					cartonItem.quantity = new BigDecimal(quantity)
					else
					cartonItem.quantity = new BigDecimal(restQty)

					if(sequence){
						cartonItem.sequence = new Integer(sequence)
					}else{
						cartonItem.sequence = getNextCartonItemSeq(carton)
					}
				}
			    if (!cartonItem.save(flush: true)) {
					cartonItem.errors.each {
						log.info it
					}
			    }
			}
		}
		if(oper.equals("del")){
			//System.err.println("delete");
			def cartonItem = CartonItem.get(cartonItemId)
			if(cartonItem){
				cartonItem.carton.removeFromCartonItems(cartonItem)
				cartonItem.delete()
			}
		}		
	}
	def updateItemsPrice(invoice) {
	  def totalAmount = invoice.totalAmount
	  def totalIssuance = invoice.totalIssuance
	  def totalFeight = invoice.totalFeight

	  def proportionIssuance = 0
	  def proportionFeight = 0
	  invoice.invoiceItems.each {
	    def rate = it.amount / totalAmount
	    it.issuance = totalIssuance * (rate)
	    it.feight = totalFeight * (rate)
	    it.save()
	    proportionIssuance = proportionIssuance + it.issuance
	    proportionFeight = proportionFeight + it.feight
	  }
	  invoice.totalDifference = (proportionIssuance + proportionFeight) - (totalIssuance + totalFeight)
	  invoice.save()
	}
	def recalculate(invoice) {
	  def totalIssuance = invoice.totalIssuance
	  def totalFreight = invoice.totalFreight

	  def proportionIssuance = 0
	  def proportionFreight = 0
	  def totalQuantity = 0
	  def totalAmount = 0
	  invoice.invoiceItems.each {ii ->
	    totalAmount = totalAmount + ii.amount
	  }

	  invoice.invoiceItems.each {ii ->
	    totalQuantity = totalQuantity + ii.quantity
	    def rate = ii.amount / totalAmount
	    ii.logisticIssuance = totalIssuance * (rate)
	    ii.logisticFreight = totalFreight * (rate)
	    //ii.save()
	    proportionIssuance = proportionIssuance + ii.logisticIssuance
	    proportionFreight = proportionFreight + ii.logisticFreight
	  }
	  invoice.totalDifference = (proportionIssuance + proportionFreight) - (totalIssuance + totalFreight)
	  invoice.totalQuantity = totalQuantity
	  invoice.totalAmount = totalAmount
	}
	def generateReInvoice(business){
		def reCodes=[]
		business.invoiceHeaders.each{
			reCodes.add(it.recode)
		}
		if(reCodes.size()==1){
			return "("+reCodes.min()+")"
		}else if(reCodes.size()>1){
			return "("+reCodes.min()+"-"+reCodes.max()+")"
		}else{
			return ""
		}
	}

	
	def delteBusinessForcely(business){
		if(business){
			 
			//def shipment = Shipment.find("from Shipment as s join s.business as b where b=?",business)
			
			def packing = Packing.findByBusiness(business)
			//if(packing && packing.vaild('Y')
			if(packing){
				 
				packing.delete(flush:true)
				log.info "delete the packing success"
			}
			/*
			business.invoiceHeaders.each{ih->
				ih.invoiceItems.each{ii->
					ii.delete(flush:true)
				}
				//ih.delete()
				//log.info "delete the invoiceHeader success"
			}*/
			
			business.delete(flush:true)
			
			log.info "delete ${business} is success"
		}
		
		
		}
	
	
	
	def deleteBusiness(business){
		if(business){
			if(business.toScala.equals('Y')){
				return "already is to scala system, don't support re-import!"
			}
			def shipment = Shipment.find("from Shipment as s join s.business as b where b=?",business)
			if(shipment){
				return "already is shipment, don't support re-import!"
			}
			def packing = Packing.findByBusiness(business)
			//if(packing && packing.vaild('Y')
			if(packing){
				/*
				packing.cartons.each{carton->
					if(!carton.delete(flush:true)){
						carton.errors.each{
							log.info it
						}
					}
				}
				*/
				log.info "delete the cartons success"
				packing.delete(flush:true)
				log.info "delete the packing success"
			}
			/*
			business.invoiceHeaders.each{ih->
				ih.invoiceItems.each{ii->
					ii.delete(flush:true)
				}
				//ih.delete()
				//log.info "delete the invoiceHeader success"
			}*/
			
			business.delete(flush:true)
			
			log.info "delete ${business} is success"
		}
	}
	def getAdjAmountTotal(bp){
		
	}
	def summeryBusiness(bp){
		if(bp){
			def sql = new Sql(dataSource);
			//def query = "select sum(quantity) as quantity,sum(ifnull(logs_Adj_Amount,amount)*quantity) as amount from businessinvoicemasterdetail where id = "+bp.id
			//def query = "select sum(quantity) as quantity,sum(isnull(logs_Adj_Amount,amount)*quantity) as amount from businessinvoicemasterdetail where id = "+bp.id
			def query = "select sum(quantity) as quantity,sum(   amount *quantity) as amount from businessinvoicemasterdetail where id = "+bp.id
			log.info query
			def results = sql.rows(query)
			log.info results
			if (results.size() > 0) {
				BigDecimal quantity = results[0].quantity
				BigDecimal amount = results[0].amount
				bp.totalQuantity = quantity
				if(bp.totalAdjDiff!=null)
				bp.totalAmount = amount+bp.totalAdjDiff
				else
				bp.totalAmount = amount
				//Kurt Edited
				//加上business.totalAdjDiff的值
				bp.totalFinalamount = amount+bp.totalIssuance+bp.totalFreight
				bp.save(flush:true);
			}
		}
	}
	
	
	def getNextCartonItemSeq(carton){
		if(carton){
			def ci = CartonItem.createCriteria()
			def maxNumber = ci.get{
				projections{
					max('sequence')
				}
				eq('carton',carton)
			}
			if(maxNumber){
				return maxNumber+1
			}else{
				return 1
			}
		}else{
			return 1
		}
	}
	/*
	def getNextReNumber() {
		//def sql = new Sql(dataSource);
		//the unitprice max
		//def query = "select max(seq) as seq id from hermes_customer"
		def results = sql.rows(query);
		if (results.size() > 0) {
			return results[0].seq
		}

		def customer = HermesCustomer.createCriteria()
		def maxNumber = customer.get{
			projections{
				max('currentNumber')
			}
		}
		return maxNumber+1
    }
	*/
	def checkReNumber(id,number) {
		def invoice = InvoiceHeader.get(id)
		log.info "id="+id
		log.info "invoice="+invoice	
		if(invoice){
		  def exists = InvoiceHeader.findByIdAndRecode(id,number)
		  if(exists){
		    return false
		  }else{
		    return true
		  }
		}else{
		  return true
		}
    }

	def distinctHawbs(hawb){
		def sql = new Sql(dataSource);
		//the unitprice max
		def query = "select hawb as hawb fom (select distinct(hawb) as hawb from hermes_business) as distincthawb whee hawb like ?"
		def results = sql.rows(query,['%'+hawb+'%']);
		return results
	}
	def distinctReinvoices(reInvoice){
		def sql = new Sql(dataSource);
		//the unitprice max
		def query = "select re_invoice from hermes_business where re_invoice like ?"
		def results = sql.rows(query,['%'+reInvoice+'%']);
		return results
	}	
    //change re number
    def updateInvoiceItemReNumber(id,importDate,exportDate,number,invoicetype=null){
		def invoice = InvoiceHeader.get(id)
		int recode = (new Integer(number)).intValue()
		if(invoice.recode!= recode){
			if(checkReNumber(id,number)){
		        SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
				if(importDate) invoice.importDate = formate.parse(importDate);
				if(exportDate) invoice.exportDate = formate.parse(exportDate);
				if(invoicetype) invoice.invoiceType= invoicetype;
				def precode = invoice.business.customer.reInvoicePreCode
				def val = precode.substring(0,precode.length()-2)
				invoice.recode = setval(val,number)
			}
		}else{
	        SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
			if(importDate) invoice.importDate = formate.parse(importDate);
			if(exportDate) invoice.exportDate = formate.parse(exportDate);
			if(invoicetype) invoice.invoiceType= invoicetype;
		}
		if(!invoice.save(flush:true)){
			invoice.errors.each{
				log.info it
			}
			return false
		}else{
			return true
		}
    }	

    
	/* carton */
	def getCartonNextCode(packing){
		def currentIndex = Carton.withCriteria{
			projections{
				max("code")  
			}
			eq("packing",packing)
		}
		if(currentIndex[0]){
			return currentIndex[0]+1
		}else{
			return 1
		}
	}
	def currval(name,inyear){
		def sql = new Sql(dataSource)
		def key = "currval '"+name+"','"+inyear+"'"
		def result = sql.rows("exec "+key)
		if (result.size() > 0) {
			return result[0][""]
		}
		else
			return null
	}
	def nextval(name,inyear){
		def sql = new Sql(dataSource)
		def key = "nextval '"+name+"','"+inyear+"'"
		def result = sql.rows("exec "+key)
		if (result.size() > 0) {
			return result[0][""]
		}
		else
		return "1"
	}
	def setval(name,value,inyear){
		def sql = new Sql(dataSource)
		def key = "setval '"+name+"',"+value+",'"+inyear+"'"
		def result = sql.rows("exec "+key)
		if (result.size() > 0) {
			return result[0][""]
		}
		else
		return null
	}
	
	def getPONotBeUsed(){
		def sql = new Sql(dataSource);
		//the unitprice max
		def query = "selECT DISTINCT a.code FROM hermes_invoiceheader a LEFT JOIN hermes_business b on a.code=b.po_code WHERE po_code IS NULL"
		def results = sql.rows(query);	
		return results
	}
}
