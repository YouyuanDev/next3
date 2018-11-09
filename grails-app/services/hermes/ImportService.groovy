package hermes

import next.*
import groovy.sql.Sql

class ImportService extends BaseService {
	def logisticService
	static transactional = true
	def cleanInterfaceBpInvoice(){
		def sql = new Sql(dataSource);
		sql.execute("truncate table interface_bpinvoice")
	}
	
	//检查Product表和HermesProduct表的最大的ID是否相同，若相同则true，若不相同返回false
	def CheckProduct_HermesProductIDisEqual(){
		def sql1 = new Sql(dataSource);
		def sql2 = new Sql(dataSource);
		
	   def query = "   select top 1 id    from hermes_product order by id desc "

       def query2=  " select top 1 id  from  product order by id desc  "
		 

	   def hermesProductId = sql1.rows(query,[])
	   def productId = sql2.rows(query2,[])
	   
	   
	   //System.err.println(query);
	   //System.err.println(results);
	   if(hermesProductId.size()>0&&productId.size()>0){
	      if(hermesProductId[0][0].equals(productId[0][0]))
		   {
			   System.err.println("hermesProductId==productId")
			   return true;
		   }
	   }
	   
	   
	   return false;
		
		
	}
	
	
	
	def loadHermesData(inYear=null,overWrite) {
		//todo: to support the france bp data reimport
		//0. from the interface table load status = open bp data
		//System.err.println("into loadHermesData")
		def sql = new Sql(dataSource)
		def bps = sql.rows("select distinct(bp_code) as bpCode from interface_bpinvoice")
		def msg = 'Success'
		System.err.println("overWrite="+overWrite);
		//System.err.println("111");
		
		if(!CheckProduct_HermesProductIDisEqual()){
			msg='Product Reference data error, BP can not be uploaded into system. please call Kurt to fix it'
			return msg
			}
		
		bps.each{bpit->
			def bpCode = bpit.bpCode
			//System.err.println("bpCode ==="+bpCode)
			InvoiceItem.withTransaction{status->
				def bp = Business.findByCode(bpCode)
				//1. delete old bp invoiceheader invoiceitem data
				if(bp){
					if(overWrite!='Yes'){
						msg='The BP '+bpCode+' already exists , failed to upload BP.'
						System.err.println("already exists , failed to upload BP");
						return msg
						}
					  
					//System.err.println("logisticService.deleteBusiness(bp)");
					msg = logisticService.deleteBusiness(bp)
					//System.err.println("logisticService.delete")
					if(msg){
						return
					}
				}
				//2. import the bpdata from interface table
				//Kurt updated
				//System.err.println("2222");
				//def openRes = InterfaceBPInvoice.withCriteria{
				//		eq("bpCode",bpCode)
				//}
				//System.err.println("333");
				//System.err.println("InterfaceBPInvoice.findAllByBpCode(bpCode)");
				def openRes = InterfaceBPInvoice.findAllByBpCode(bpCode)
				System.err.println("InterfaceBPInvoice.withCritria finished openRes.size()="+openRes.size())
				openRes.each{//每一个物品导更新customer，business，InvoiceHead，InvoiceItem表
					//msg = loadBusiness(it,inYear)  old version
					msg = loadBusiness_new(it,inYear)  //new version
					
					return
				}
			}
			
			if(!CheckProduct_HermesProductIDisEqual()){
				msg='Product Reference data error, BP can not be uploaded into system. please call Kurt to fix it'
				def bp = Business.findByCode(bpCode)
				if(bp){
					msg = logisticService.deleteBusiness(bp)
					msg='Product Reference data error, BP '+bpCode+'can not be uploaded into system. please call Kurt to fix it'
				}
				return msg
				}
			
			//3. summery the bp quantity and amount from bp items
		}
		return msg
	}  
	
	//插入hermesCustomer
	def insertHermesCustomerInfo(customer){
		def sql = new Sql(dataSource);

		def query1 = "declare @a int    insert into party (code, type,description,last_updated,status,[name],date_created) "
			query1 = query1+" values ('"+customer.code+"','"+customer.type+"',null,getdate(),'"+customer.status+"','"+customer.name+"',getdate()); select  @a =@@identity; "
		    query1 = query1+ " insert into hermes_customer (cur_num, customer_cate_code, customer_cate_name, customer_dev_index, customer_long_code, customer_short_code, loc_id, re_invoice_pre_code, wh_id, xmag,parent_company) "
			query1 = query1+" values ("+customer.curNum+",'"+customer.customerCateCode+"','"+customer.customerCateName+"',"+customer.customerDevIndex+",'"+customer.customerLongCode+"','"+customer.customerShortCode+"','"+customer.locId+"','"+customer.reInvoicePreCode+"','"+customer.whId+"', '"+customer.xmag+"', '"+customer.parentCompany+"');"
			if(customer.type=='HermesSupplier')
			{
				query1 = query1+" insert into hermes_supplier (id) values(@a) "; 
			}
			
			def results
			try{
			  results = sql.executeUpdate(query1,[])
			   
			  return results;
			}
			catch(Exception e){
				return false;
			}
			
		

	}
	
	
	
    //导入香水或手表信息
	def loadPerfumeAndWatchData(datas,supplierCode){
		log.info datas
		datas.each{
			//System.err.println("it.customer:"+it.customer);
			def customer = it.customer;//HermesCustomer.findByCode(it.customer)
			//System.err.println("it.cod:"+it.code);
			if(customer){
				def bp = Business.findByCode(it.code)
				if(!bp){
					bp = new Business()
					bp.code = it.code
					bp.poCode = it.code
					bp.soCode = it.soCode
					bp.customer = customer
					bp.deliveryDate = new Date();
					//System.err.println("before HermesSupplier.findByCode(supplierCode)");
					def supplier = HermesSupplier.findByCode(supplierCode)
					if(supplier)
						bp.supplier = supplier
						//System.err.println("bp info "+bp.poCode);
					if(!bp.save(flush:true)){
						//System.err.println("bp save error");
						bp.errors.each{
							log.info it
						}
					}
				}
				//System.err.println("before BusinessItem.findByBusinessAndCode(bp,it.prodCode)");
				def item = BusinessItem.findByBusinessAndCode(bp,it.prodCode)
				//System.err.println("after BusinessItem.findByBusinessAndCode(bp,it.prodCode)")
				if(!item){
					item = new BusinessItem()
					item.code = it.prodCode
					def product = HermesProduct.findBySkuCode(it.prodCode)
					//System.err.println("after HermesProduct.findBySkuCode(it.prodCode) product:"+product);
					if(!product){
						product = new HermesProduct()
						product.skuCode = it.prodCode
						product.colorName=''
						product.familyName=''
						product.modelCode=''
						product.sizeCode=''
						product.name=''
						product.materialName=''
						product.styleCode=''
						product.productCode=''	
						product.familyCode=''
						product.sizeName=''
						product.supplyChainModel=''
						product.skuName=''
						product.code=it.prodCode
						product.modelName=''
						product.materialCode=''
						product.productName=''
						product.colorCode=''
						product.ean13=''	
						//System.err.println("product.skuCode="+product.skuCode);
						def sql = new Sql(dataSource);
						//sql.execute("SET IDENTITY_INSERT hermes_product  ON  ", [])
			
						if (!product.save(flush:true)){
							//System.err.println("error");
							product.errors.each{
								log.info it
							}
							
						}
						//sql.execute("SET IDENTITY_INSERT hermes_product  OFF  ", [])
					}
					item.product = product
				}
				//System.err.println("after item.product = product");
				item.quantity = new BigDecimal(it.quantity)
				item.amount = new BigDecimal(it.amount)
				item.business = bp
               // System.err.println("before if(!item.save(flush:true))");
				if(!item.save(flush:true)){
					item.errors.each{
						log.info it
					}
				
				}
			}else{
				log.info "customer:"+it.customer+" is not found!"
				
			}
		}
	}
	
	
	
	
	
	def loadBusiness_new(it, inYear=null){
		def customer = loadCustomer_new(it)
		def bp = Business.findByCode(it.bpCode)
		if(!bp){
			bp = new Business()
			bp.code = it.bpCode
			bp.customer = customer
			bp.inYear = inYear
			def supplier = HermesSupplier.findByCode('1500')
			if(supplier)
				bp.supplier = supplier
		}
		if(!bp.save(flush:true)){
			bp.errors.each {
				log.info it
			}
			//System.err.println("ERROR: business.save");
		}
		def invoice = loadInvoice_new(it,customer,bp,inYear)
		
		
		return 'Success'
	}
	
	
	
	
	def loadCustomer_new(it){
		//System.err.println("it.customerDevIndex:"+it.customerLongCode);
		def customer = HermesCustomer.findByCode(it.customerLongCode)
		def isInsert=false
		//customer=null//强行设置原有数据库里没有这个customer
		if (!customer) {
			isInsert=true
			//System.err.println("没找到 customer customerLongCode="+it.customerLongCode)
			customer = new HermesCustomer()
			customer.code = it.customerLongCode//+'10'
			customer.name = it.customerName
			
			
		}
		//截取最后2位作为customer_dev_index
		//def lastTwoCode = customer.code.substring(customer.code.length()-2,customer.code.length())
		//def customerAccount = CustomerAccount.findByCode(lastTwoCode)
		//if(customerAccount){
		//	Calendar toDay = Calendar.getInstance();
		//	int year = toDay.get(Calendar.YEAR);
		//	customer.reInvoicePreCode = customerAccount.description + (year-2000)
		//}
		
		customer.customerShortCode = it.customerShortCode
		customer.customerLongCode = it.customerLongCode
		customer.customerDevIndex = it.customerDevIndex
		customer.customerCateCode = it.customerCateCode
		customer.customerCateName = it.customerCateName
		//System.err.println("it.customerDevIndex:"+it.customerDevIndex);
		//2014.12.1 由于后两位编码可能重复，因此reinvoice code只参考hermes_customer表的re_invoice_re_code字段 
		//以下去除
		/*
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		Integer strdevIndex=new Integer(it.customerDevIndex);
		customer.reInvoicePreCode=getCustomerReInovoicePrecode(format.format(strdevIndex))
		*/
		//
		//System.err.println("customer.reInvoicePreCode:"+customer.reInvoicePreCode);
		customer.type = 'HermesCustomer';
		customer.xmag ='';
		//System.err.println("customer.id="+customer.id);
		//System.err.println("before customer.save");
		 if (!isInsert&&!customer.save(flush:true)) {
			customer.errors.each {
				log.info it
			}
			//System.err.println("ERROR: customer.save");
		}
		else if(isInsert)
		{
			
			if(insertPartyInfo(customer))
			//System.err.println("insertPartyInfo success")
			if(insertCustomerInfo(customer)){
			//System.err.println("Success: customer.insert");
			}
			/*if(insertCustomerAccount(customer))
			System.err.println("Success: customerAccount.insert");*/
		}
		
		//System.err.println("after customer.save");
		return customer
	}
	
	def loadInvoice_new(it,customer,business,inYear=null) {
		//load product
		//System.err.println("loadInvoice 257 it="+it);
		def product = loadProduct_new(it)
		//def product = Product.get(productId);
		//System.err.println("product.id="+product.id);
		def invoice = InvoiceHeader.findByCode(it.orderCode)
		//System.err.println("loadInvoice 260");
		if (!invoice) {
			invoice = new InvoiceHeader()
			invoice.code = it.orderCode
			invoice.type = 'HermesInvoice'
			//System.err.println("loadInvoice 265");
			if(customer){
				//System.err.println("loadInvoice 267 customer="+customer);
				def precode = customer.reInvoicePreCode
				//System.err.println("loadInvoice precode="+precode);
				//def val = precode.substring(0,precode.length()-2)
				def val = precode
				//Kurt Edited reinvoice code 里的recode数字  reinvoice这里生成
				//System.err.println("loadInvoice inYear="+inYear);
				
				if(!logisticService.currval(val,inYear)){
					Sequence seq=new Sequence();
					seq.name=precode
					seq.currentValue=1
					seq.increment=1
					seq.inYear=inYear
					invoice.recode=1
					if( !seq.save(flush:true)){
						seq.errors.each {
							log.info it
						}
					}
					
				}
				else{
					
					invoice.recode = logisticService.nextval(val,inYear)
				}
				
				//invoice.reInvoice = precode+inYear+"("+invoice.recode+")" 改为
				java.text.DecimalFormat format = new java.text.DecimalFormat("0000");
				def fmt_recode=format.format(invoice.recode)
				   //System.out.println(format.format(invoice.recode));
				invoice.reInvoice = precode+inYear+"-"+fmt_recode
				//END
			}
		}
		invoice.invoiceYear = inYear
		invoice.podium = it.podium //podium 已挪到invoiceitem里，但这里仍保留
		invoice.invoiceDate = new Date(it.invoiceDate)
		invoice.description = ''
		invoice.business=business
		invoice.qADuration=0
		invoice.rcvdNumersOfCartons=0
		invoice.vatDutyAmount=0
		
		
		if( !invoice.save(flush:true)){
			invoice.errors.each {
				log.info it
			}
		}
		def country=getCountryNameByCountryCode(it.countryCode);
		def invoiceitem = InvoiceItem.find("from InvoiceItem as hii where hii.invoiceHeader.code=? and hii.product.code=?", [invoice.code, product.code])
		//Kurt edited  由于巴黎Invoice里会出现重复的product，若不存在，新增一行记录，存在更新数量
		if (!invoiceitem) {
			invoiceitem = new InvoiceItem()
			invoiceitem.product = product
			invoiceitem.quantity = new BigDecimal(it.shipQty)
			invoiceitem.amount = new BigDecimal(it.retailDiscountPrice)
			invoiceitem.logsAdjAmount=new BigDecimal(it.retailDiscountPrice)
			invoiceitem.shipmentBFDF=0
			invoiceitem.podium=it.podium
			//System.err.println("before insertInvoiceItem ");
			 if(insertInvoiceItem(invoiceitem,invoice.id,country)){
				//System.err.println("insertInvoiceItem SUCCESS");
			}
			else{
				//System.err.println("insertInvoiceItem Failed");
			} 
			//invoiceitem.price1=
			//invoiceitem.price2=
			//invoiceitem.productPrice=new BigDecimal(it.productPrice)
			//invoiceitem.retailPriceEUR=new BigDecimal(it.retailPriceEUR)
			//invoiceitem.retailDiscountPrice=new BigDecimal(it.retailDiscountPrice)
			//invoiceitem.shipQty=new BigDecimal(it.shipQty)
			//invoiceitem.shipGrossPrice=new BigDecimal(it.shipGrossPrice)
			//invoiceitem.shipDiscountPrice=new BigDecimal(it.shipDiscountPrice)
		}
		else{
			invoiceitem.quantity=invoiceitem.quantity+new BigDecimal(it.shipQty)
			if( !invoiceitem.save(flush:true)){
				invoice.errors.each {
					log.info it
				}
			}
		}
		
		
		/////////////////
			
		 
		/*def invoiceitem = InvoiceItem.find("from InvoiceItem as hii where hii.invoiceHeader.code=? and hii.product.code=?", [invoice.code, product.code])
		if (!invoiceitem) {
			invoiceitem = new InvoiceItem()
			invoiceitem.product = product
			invoiceitem.quantity = new BigDecimal(it.shipQty)
			invoiceitem.amount = new BigDecimal(it.retailDiscountPrice)
			//invoiceitem.price1=
			//invoiceitem.price2=
			//invoiceitem.productPrice=new BigDecimal(it.productPrice)
			//invoiceitem.retailPriceEUR=new BigDecimal(it.retailPriceEUR)
			//invoiceitem.retailDiscountPrice=new BigDecimal(it.retailDiscountPrice)
			//invoiceitem.shipQty=new BigDecimal(it.shipQty)
			//invoiceitem.shipGrossPrice=new BigDecimal(it.shipGrossPrice)
			//invoiceitem.shipDiscountPrice=new BigDecimal(it.shipDiscountPrice)
		}
		invoice.addToInvoiceItems(invoiceitem)
		/*
		if( !invoice.addToInvoiceItems(invoiceitem).save(flush:true)){
			invoice.errors.each {
				log.info it
			}
		}*/
		return invoice
	}
	
	def loadProduct_new(it) {
		//load category
		//System.err.println("loadProduct 206 it="+it)
		/*def categoryStr = it.skuCode
		if(it.skuCode!=null && it.skuCode.length()>=8){
			categoryStr = it.skuCode.substring(0,8)
		}*/
		def categoryStr = it.styleCode
		
		def category = Category.findByCode(categoryStr)
		if (!category) {
			category = new Category()
			category.code = categoryStr
			category.name = ''			//中文品名
			category.description2='' //第二种材质  Harry 要求
			
		}
		if(it.compositionEnglish&&it.compositionEnglish!="")
		category.description=it.compositionEnglish    //材质更新
		if(!category.save(flush: true)){
			category.errors.each{
				log.info it
			}
		}
		
		
		//load product
		//System.err.println("loadProduct 206 it.skuCode="+it.skuCode)
		
		/*
		def org_product=Product.findByCode(it.skuCode);
		if(!org_product){
			System.err.println("loadProduct no Original product");
			org_product=new Product()
			org_product.code=it.skuCode
			org_product.name = it.longEnglishName  //英语品名
			org_product.category = category
			org_product.id=insertProductInfo(org_product);
			if(org_product.id){
				System.err.println("insertProductInfo OK");
			}
		}
		else{
			//org_product=Product.get(org_product.id);
			org_product.code=it.skuCode
			org_product.name = it.longEnglishName  //英语品名
			org_product.category = category
			if (!org_product.save(flush: true)) {
				org_product.errors.each {
					log.info it
				}
				System.err.println("ERROR Original PRODUCT SAVE ")
			}
			//if(updateProductInfo(org_product)){
			//	System.err.println("Successfully SAVE Original PRODUCT!")
			//}
			//else{
			//	System.err.println("ERROR Original PRODUCT SAVE ")
			//} 
		}*/
		
		
		
		def product = HermesProduct.findBySkuCode(it.skuCode)
		def isInsert=false;
		if (!product) {
			//System.err.println("no HermesProduct record");
			isInsert=true;
			product = new HermesProduct()
			product.code = it.skuCode
			product.name = it.productName
			product.materialCode = it.materialCode
			product.materialName = it.materialName
		}
		
		product.category = category
		product.productCode = it.productCode
		product.productName = it.productName
		if(it.materialCode&&it.materialCode!="")
		product.materialCode = it.materialCode
		product.dept = it.materialCode
		if(it.materialName&&it.materialName!="")
		product.materialName = it.materialName
		product.familyCode = it.familyCode
		product.speciality = it.familyCode
		product.familyName = it.familyName
		product.modelCode = it.modelCode
		product.modelName = it.modelName
		product.styleCode = it.styleCode
		product.skuCode = it.skuCode
		product.skuName = it.skuName
		product.supplyChainModel = it.supplyChainModel
		product.ean13 = it.ean13
		product.colorCode = it.colorCode
		product.colorName = it.colorName
		product.sizeCode = it.sizeCode
		product.sizeName = it.sizeName
		product.customsCode=it.customsCode
		//System.err.println("loadProduct 250 skuCode="+it.skuCode)
		
		def org_product
		if(isInsert){
			if(!CheckProduct_HermesProductIDisEqual()){
				 
				 return null;
			}
			
			org_product=Product.findByCode(it.skuCode);
			if(org_product)
			org_product.delete();
			 org_product=new Product()
			org_product.code=it.skuCode
			org_product.name = it.longEnglishName  //英语品名
			org_product.category = category
			org_product.id=insertProductInfo(org_product);
			if(org_product.id){
				//System.err.println("insertProductInfo OK");
			}
			
			
			
			//System.err.println("product.styleCode="+product.styleCode);
			def deptAndMagnitude=getMagnitudeInfo(product.styleCode)
			if(deptAndMagnitude){
			//product.dept=deptAndMagnitude[0];
			product.magnitude=deptAndMagnitude[1];
			}
			//System.err.println("product.dept="+product.dept)
			//System.err.println("product.magnitude="+product.magnitude)
			def newProductId=insertHermesProductInfo(product)
			
			if(newProductId){
				product.id=newProductId;
				//System.err.println("insertHermesProductInfo OK");
			}
			
		}
		else{
			 org_product=Product.findByCode(it.skuCode);
			org_product.code=it.skuCode
				org_product.name = it.longEnglishName  //英语品名
			org_product.category = category
			if (!org_product.save(flush: true)) {
				org_product.errors.each {
					log.info it
				}
				//System.err.println("ERROR Original PRODUCT SAVE ")
			}
		
			if (!product.save(flush: true)) {
				product.errors.each {
					log.info it
				}
				//System.err.println("ERROR PRODUCT SAVE 255 ")
			}
			else{
				//System.err.println("PRODUCT save OK");
			}
		}
		//System.err.println("loadProduct 256 ")
		return org_product
	}
	
	def insertCustomerInfo(customer){
		def sql = new Sql(dataSource);
		 
		def query = " insert into hermes_customer (cur_num, customer_cate_code, customer_cate_name, customer_dev_index, customer_long_code, customer_short_code, loc_id, re_invoice_pre_code, wh_id, xmag) "
			query = query+" values ("+customer.curNum+",'"+customer.customerCateCode+"','"+customer.customerCateName+"',"+customer.customerDevIndex+",'"+customer.customerLongCode+"','"+customer.customerShortCode+"','"+customer.locId+"','"+customer.reInvoicePreCode+"','"+customer.whId+"', '"+customer.xmag+"'); select @@identity as id;"
		def results = sql.rows(query,[])
		//sql.commit();
		//System.err.println(query);
		//System.err.println(results);
		if(results.size()>0)
		
		return results[0][0]
		else
		return null
	}
	//Kurt edited  因为巴黎invoice里没有store的precode所以无法初始化customerAccount，导致无法插入customer
	/*def insertCustomerAccount(customer){
		def sql = new Sql(dataSource);
		
	   def query = " insert into hermes_customeraccount (code, description)  values ( ? ,? ) "
	   
	   java.text.DecimalFormat format = new java.text.DecimalFormat("00");
	   Integer strdevIndex=new Integer(customer.customerDevIndex);
	   
	   def results = sql.executeUpdate(query,[format.format(strdevIndex),'ddd'])

	   return results 
	} */
	
	
	
	def getMagnitudeInfo(referenceCode){
		
		def sql = new Sql(dataSource);
		
	   def query = " select DPMT,[magnitude Dpt]  from [final] where reference=? "
	   def results = sql.rows(query,[referenceCode])
	   //sql.commit();
	   //System.err.println(query);
	   //System.err.println(results);
	   if(results.size()>0)
	   
	   return results[0]
	   else
	   return null
	}
	
	def getCountryNameByCountryCode(CountryCode){
		def sql = new Sql(dataSource);
		
	   def query = "select [name] from country  where code= ?"

	   def results = sql.rows(query,[CountryCode])
	   //System.err.println(query);
	   //System.err.println(results);
	   //sql.commit();
	   if(results.size()>0)
	   return results[0][0]
	   else
	   return '';//原来是法国，Jenny 要求改为空
	}
	
	
	def insertPartyInfo(customer){
		def sql = new Sql(dataSource);
		 
		 
		def query = " insert into party (code, type,description,last_updated,status,[name],date_created) "
			query = query+" values (?,?,null,getdate(),?,?,getdate())"
		def results = sql.executeUpdate(query,[customer.code,customer.type,customer.status,customer.name])
		//System.err.println(query);
		//System.err.println(results);
		//sql.commit();
		return results
	}
	
	def insertProductInfo(product){
		def sql = new Sql(dataSource);
		
		
	   def query = " INSERT INTO [next_devdb].[dbo].[product]( [category_id],  [status], [date_created], [code], [last_updated], [name]) "
	   		query = query+"	VALUES(?,?, getDate(),?, getDate(),?); select @@identity as id; "

	   def results = sql.rows(query,[product.category.id,product.status,product.code,product.name])
	   //System.err.println(query);
	   //System.err.println(results);
	   if(results.size()>0)
	   return results[0][0]
	   else
	   return null;
	   
	}
	
	def updateProductInfo(product){
		def sql = new Sql(dataSource);
		
		
	   def query = " UPDATE [next_devdb].[dbo].[product] "
	   		query= query+" SET  [category_id]= ?, [status]=?, [date_created]=getdate(), [code]=? ,  [last_updated]=getdate(), [name]=?,  [version]=1 "
			   query= query+ " WHERE  id=? "

	   def results = sql.executeUpdate(query,[product.category.id,product.status,product.code,product.name,product.id])
	   //System.err.println(query);
	   //System.err.println(results);
	   //sql.commit();
	   return results
	}
	
	
	def insertHermesProductInfo(product){
		def sql = new Sql(dataSource);
		
	   def query = "INSERT INTO [hermes_product]([dept], [magnitude],[color_name], [family_code], [supply_chain_model], [size_name], [product_name], [model_name], [size_code], [ean13], [style_code], [product_code], [material_code], [material_name], [model_code], [color_code], [sku_name], [sku_code], [family_name], [retail_price], [wholsl_price],[speciality],[customs_code]) "
	   query = query+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,0,?,?); select @@identity as id;"

	   def results = sql.rows(query,[product.dept,product.magnitude, product.colorName, product.familyCode,product.supplyChainModel,product.sizeName,product.productName,product.modelName,product.sizeCode,product.ean13, product.styleCode,product.productCode,product.materialCode,product.materialName,product.modelCode,product.colorCode,product.skuName,product.skuCode,product.familyName,product.speciality,product.customsCode])
	   //System.err.println(query);
	   //System.err.println(results);
	   //sql.commit();
	   if(results.size()>0)
	   return results[0][0]
	   else
	   return null;

	}
	
	def insertInvoiceItem(invoiceitem,invoiceHeaderId,country){
		def sql = new Sql(dataSource);
		
	   def query = "INSERT INTO [hermes_invoiceitem](  [version], [logs_adj_amount],[product_id], [quantity], [invoice_header_id],  [amount], [country],[logistic_freight],[shipment_duty],[logistic_issuance],[packing_seq],[shipmentdl],[shipmentyz],[shipment_type],[shipment_cost],[magitudem],[po_code],[podium]) "
	   query = query+" VALUES(1,?,?,?,?,?,?,?,?,?,0,0,0,0,0,'','',?); select @@identity as id;"
	   
	   //System.err.println("invoiceitem.product.id:"+invoiceitem.product.id);
	   //System.err.println("invoiceHeaderId:"+invoiceHeaderId);
	   
	   
	   def results = sql.rows(query,[invoiceitem.logsAdjAmount,invoiceitem.product.id, invoiceitem.quantity,invoiceHeaderId, invoiceitem.amount,country,invoiceitem.logisticFreight,invoiceitem.shipmentDuty,invoiceitem.logisticIssuance,invoiceitem.podium])
	   //System.err.println(query);
	   //System.err.println(results);
	   //sql.commit();
	   if(results.size()>0)
	   return results[0][0]
	   else
	   return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*===============================================================================================*/
	def loadBusiness(it,inYear=null) {
		System.err.println("loadBusiness 144 it.code"+it.bpCode);
		def customer = loadCustomer(it)
		System.err.println("loadBusiness 145");
		def bp = Business.findByCode(it.bpCode)
		if(!bp){
			bp = new Business()
			bp.code = it.bpCode
			bp.customer = customer
			bp.inYear = inYear
			def supplier = HermesSupplier.findByCode('1500')
			if(supplier)
				bp.supplier = supplier
		}
		System.err.println("loadBusiness 156");
		def invoice = loadInvoice(it,customer,inYear)		
		if (invoice) {
		  if (!bp.addToInvoiceHeaders(invoice).save(flush: true)) {
		    bp.errors.each {
		      log.info it
		    }
			System.err.println("ERROR:loadBusiness 163");
		  }
		  System.err.println("loadBusiness 165");
		}
		return 'Success'
	}
	
	//查找Customer 若没有，新建一个Customer存入数据库
	def loadCustomer(it) {
		def customer = HermesCustomer.findByCode(it.customerLongCode)
		//def isInsert=false
		//customer=null//强行设置原有数据库里没有这个customer
		if (!customer) {
			//isInsert=true
			System.err.println("没找到 customer customerLongCode="+it.customerLongCode)
			customer = new HermesCustomer()
			customer.code = it.customerLongCode//+'10'
			customer.name = it.customerName
			
			
		}
		//截取最后2位作为customer_dev_index
		def lastTwoCode = customer.code.substring(customer.code.length()-2,customer.code.length())
		//def customerAccount = CustomerAccount.findByCode(lastTwoCode)
		//if(customerAccount){
		//	Calendar toDay = Calendar.getInstance();
		//	int year = toDay.get(Calendar.YEAR);
		//	customer.reInvoicePreCode = customerAccount.description + (year-2000)
		//}
		
		customer.customerShortCode = it.customerShortCode
		customer.customerLongCode = it.customerLongCode
		customer.customerDevIndex = it.customerDevIndex
		customer.customerCateCode = it.customerCateCode
		customer.customerCateName = it.customerCateName
		/*
		java.text.DecimalFormat format = new java.text.DecimalFormat("00");
		Integer strdevIndex=new Integer(it.customerDevIndex);
		customer.reInvoicePreCode=getCustomerReInovoicePrecode(format.format(strdevIndex))
		*/
		System.err.println("it.customerDevIndex="+it.customerDevIndex);
		System.err.println("customer.reInvoicePreCode="+customer.reInvoicePreCode);
		customer.type = 'HermesCustomer';
		customer.xmag ='';
		System.err.println("customer.id="+customer.id);
		System.err.println("before customer.save");
		 if (!customer.save(insert:true)) {
			customer.errors.each {
				log.info it
			}
			System.err.println("ERROR: customer.save");
		} 
		/*else if(isInsert)
		{
			if(insertPartyInfo(customer))
			System.err.println("insertPartyInfo success")
			if(insertCustomerInfo(customer))
			System.err.println("Success: customer.insert");
		}*/
		
		System.err.println("after customer.save");
		return customer
	}	
	
	
	
	
	
	
	def getCustomerReInovoicePrecode(dev_index)
	{
		def sql = new Sql(dataSource);
		//System.err.println("dev_index:"+dev_index);
	   def query = " select [description] from hermes_customeraccount where code='"+dev_index+"' "
	   def results = sql.rows(query)
	   if(results)
	   return results[0][0]
	   else
	   return ''
		
	}
	
	
	
	
	def loadProduct(it) {
		//load category
		System.err.println("loadProduct 206 it="+it)
		def categoryStr = it.skuCode
		if(it.skuCode!=null && it.skuCode.length()>=8){
			categoryStr = it.skuCode.substring(0,8)
		}
		def category = Category.findByCode(categoryStr)
		if (!category) {
			category = new Category()
			category.code = categoryStr
			category.name = ''
			if(!category.save(flush: true)){
				category.errors.each{
					log.info it
				}
			}
		}
		//load product
		System.err.println("loadProduct 206 it.skuCode="+it.skuCode)
		
		def product = HermesProduct.findByCode(it.skuCode)
		if (!product) {
			product = new HermesProduct()
			product.code = it.skuCode
			product.name = it.productName
		}
		
		product.category = category
		product.productCode = it.productCode
		product.productName = it.productName
		product.materialCode = it.materialCode
		product.materialName = it.materialName
		product.familyCode = it.familyCode
		product.familyName = it.familyName
		product.modelCode = it.modelCode
		product.modelName = it.modelName
		product.styleCode = it.styleCode
		product.skuCode = it.skuCode
		product.skuName = it.skuName
		product.supplyChainModel = it.supplyChainModel
		product.ean13 = it.ean13
		product.colorCode = it.colorCode
		product.colorName = it.colorName
		product.sizeCode = it.sizeCode
		product.sizeName = it.sizeName
		System.err.println("loadProduct 250 skuCode="+it.skuCode)
		if (!product.save(flush: true)) {
			product.errors.each {
				log.info it
			}
			System.err.println("ERROR PRODUCT SAVE 255 ")
		}
		//System.err.println("loadProduct 256 ")
		return product
	}
	//载入invoicehead 
	def loadInvoice(it,customer,inYear=null) {
		//load product
		System.err.println("loadInvoice 257 it="+it);
		def product = loadProduct(it)
		def invoice = InvoiceHeader.findByCode(it.orderCode)
		System.err.println("loadInvoice 260");
		if (!invoice) {
			invoice = new InvoiceHeader()
			invoice.code = it.orderCode
			invoice.type = 'HermesInvoice'
			System.err.println("loadInvoice 265");
			if(customer){
				System.err.println("loadInvoice 267 customer="+customer);
				def precode = customer.reInvoicePreCode
				System.err.println("customer="+customer.id);
				System.err.println("precode="+precode);
				//def val = precode.substring(0,precode.length()-2)
				def val = precode
				//Kurt Edited reinvoice code 里的recode数字  reinvoice这里生成
				invoice.recode = logisticService.nextval(val)
				//invoice.reInvoice = precode+inYear+"("+invoice.recode+")" 改为
				java.text.DecimalFormat format = new java.text.DecimalFormat("0000");
				def fmt_recode=format.format(invoice.recode)
     			  System.out.println(format.format(invoice.recode));
				invoice.reInvoice = precode+inYear+"-"+fmt_recode
				//END
			}
		}
		invoice.invoiceYear = inYear
		invoice.podium = it.podium
		invoice.invoiceDate = new Date(it.invoiceDate)
		invoice.description = ''
		def invoiceitem = InvoiceItem.find("from InvoiceItem as hii where hii.invoiceHeader.code=? and hii.product.code=?", [invoice.code, product.code])
		if (!invoiceitem) {
			invoiceitem = new InvoiceItem()
			invoiceitem.product = product
			invoiceitem.quantity = new BigDecimal(it.shipQty)
			invoiceitem.amount = new BigDecimal(it.retailDiscountPrice)
			//invoiceitem.price1=
			//invoiceitem.price2=
			//invoiceitem.productPrice=new BigDecimal(it.productPrice)
			//invoiceitem.retailPriceEUR=new BigDecimal(it.retailPriceEUR)
			//invoiceitem.retailDiscountPrice=new BigDecimal(it.retailDiscountPrice)
			//invoiceitem.shipQty=new BigDecimal(it.shipQty)
			//invoiceitem.shipGrossPrice=new BigDecimal(it.shipGrossPrice)
			//invoiceitem.shipDiscountPrice=new BigDecimal(it.shipDiscountPrice)
		}
		invoice.addToInvoiceItems(invoiceitem)
		/*
		if( !invoice.addToInvoiceItems(invoiceitem).save(flush:true)){
			invoice.errors.each {
				log.info it
			}
		}*/
		return invoice
	}

}
