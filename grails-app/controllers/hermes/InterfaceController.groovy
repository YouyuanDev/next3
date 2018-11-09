package hermes

import java.text.SimpleDateFormat
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.util.*;
import groovy.sql.Sql
import next.*
import rt.*;
import grails.converters.*
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.core.io.ClassPathResource


class InterfaceController {
	def importService
	def logisticService
	def financeService
	def dataSource 
	def jqgridFilterService
	/* upload bpinvoice from parise */
	def uploadInvoice = {
		//log.info params
		def userFile = params['userfile']
        def inYear = params['inYear']
		def overWrite =params['overWrite']
		if(overWrite==null)
		overWrite='No'
		//System.err.println("overWrite="+overWrite);
        //def magn = Magn.findByCode('A01')
		if (userFile) {
			importService.cleanInterfaceBpInvoice()
			//Kurt Edited  可导入 csv格式
			XlsProcessor xlsPr=XlsProcessor.getInstance();
			
			long   start   =   System.currentTimeMillis();
			if(!processBPInvoice(xlsPr.convert(userFile.getInputStream()))){
			render(text:'csv file format error')
			return;
			}
			long   end1   =   System.currentTimeMillis();
			long   costtime   =   end1   -   start;
			System.err.println("processBPinvoice time="+costtime);
			//processBPInvoice(userFile.getInputStream())
			System.err.println("finish processBPInvoice")
			def msg=importService.loadHermesData(inYear,overWrite) 
			long   end2   =   System.currentTimeMillis();
			long   costtime2   =   end2   -   end1;
			System.err.println("loadHermesData time="+costtime2);
			long total=costtime+costtime2;
			System.err.println("total time="+total);
			System.err.println("finish loadHermesData")
			log.info "msg"+msg
			render(text:msg)
		}else{
			Calendar cur = Calendar.getInstance()
			render(view: "/hermes/interface/uploadInvoice",model: [inYear:cur.get(Calendar.YEAR).toString().substring (2, 4)])
		}
	}
	
	
	
	
	
	
	
	
	def processBPInvoice(def is) {
		/*
		*	已知问题
		*	1)ean13(被当作字符串读取)
		*	2)数字类型都带有.0
		*	3)价格字段对应存在问题
		*	4)必须读取xls文件.
		*/
		HSSFWorkbook wb = new HSSFWorkbook(is);
		HSSFSheet sheet = wb.getSheetAt(0)
		def hSSFDataFormatter = new HSSFDataFormatter()
		def cell
		int rowNum = sheet.getLastRowNum()
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		 
			Pattern pattern_code = Pattern.compile(properties.getProperty("csvfilepattern"));
			 
			
		
			try {
		//log.info rowNum
		for (int i = 1; i <= rowNum; i++) {
			//System.err.println("i="+i)
			def row = sheet.getRow(i)
			def customerDevIndex, customerShortCode, customerLongCode, customerName, customerCateCode, customerCateName, materialCode, materialName, familyCode, familyName, productCode, productName, modelCode, modelName, styleCode, skuCode, skuName, supplyChainModel, ean13, colorCode, colorName, sizeCode, sizeName, podium, bpCode, orderType, orderCode, invoiceDate, productPrice, retailPriceEUR, retailDiscountPrice, shipQty, shipGrossPrice, shipDiscountPrice, countryCode,customsCode 
			//bpCode = hSSFDataFormatter.formatCellValue(row.getCell(47))
			bpCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("bpCode"))))
			//System.err.println("			bpCode="+row.getCell(47).toString())
			Matcher match = pattern_code.matcher(bpCode);
			if(!match.matches())
				return false;
			if (!bpCode) continue
			
						
			//System.err.println("1212121212")
			
			//customerShortCode = row.getCell(2)
			customerShortCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customerShortCode"))))
			match = pattern_code.matcher(customerShortCode);
			if(!match.matches())
				return false;
			
				//System.err.println("kkkk")
			
			
			//customerDevIndex = hSSFDataFormatter.formatCellValue(row.getCell(3))
			customerDevIndex = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customerDevIndex"))))
			match = pattern_code.matcher(customerDevIndex);
			if(!match.matches())
				return false;
			
			//	System.err.println("aaaaaa")
			
			
			//customerLongCode = hSSFDataFormatter.formatCellValue(row.getCell(4))
			customerLongCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customerLongCode"))))
			match = pattern_code.matcher(customerLongCode);
			if(!match.matches())
				return false;
			//CHECK 是否存在customer，如果不存在，不读取这条信息
			def cust=HermesCustomer.findByCustomerLongCode(customerLongCode)
			if(!cust){
				System.err.println("customer doesnt exist ：customerLongCode="+customerLongCode)
				continue;
			}
			
		//	System.err.println("bbbb")
			
			
			//customerName = hSSFDataFormatter.formatCellValue(row.getCell(5))
			customerName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customerName"))))
			//match = pattern_code.matcher(customerName);
			//if(!match.matches())
			//	return;
			
					
			
			//customerCateCode = hSSFDataFormatter.formatCellValue(row.getCell(6))
			customerCateCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customerCateCode"))))
			match = pattern_code.matcher(customerCateCode);
			if(!match.matches())
				return false;
			
			
			//	System.err.println("2222")
			
			
			//customerCateName = hSSFDataFormatter.formatCellValue(row.getCell(7))
			customerCateName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customerCateName"))))
			/* new format is add 10 colu */
			//materialCode = hSSFDataFormatter.formatCellValue(row.getCell(20))
			materialCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("materialCode"))))
			match = pattern_code.matcher(materialCode);
			if(!match.matches())
				return false;
			
			
			
			
			//materialName = hSSFDataFormatter.formatCellValue(row.getCell(21))
			materialName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("materialName"))))
			
			//familyCode = hSSFDataFormatter.formatCellValue(row.getCell(22))
			familyCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("familyCode"))))
			match = pattern_code.matcher(familyCode);
			if(!match.matches())
				return false;
			
			
			//familyName = hSSFDataFormatter.formatCellValue(row.getCell(23))
			familyName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("familyName"))))
			
			//productCode = hSSFDataFormatter.formatCellValue(row.getCell(24))
			productCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("productCode"))))
			match = pattern_code.matcher(productCode);
			if(!match.matches())
				return false;
			
			
			
			//productName = hSSFDataFormatter.formatCellValue(row.getCell(25))
			productName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("productName"))))
			
			//modelCode = hSSFDataFormatter.formatCellValue(row.getCell(26))
			modelCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("modelCode"))))
			match = pattern_code.matcher(modelCode);
			if(!match.matches())
				return false;
			
			//	System.err.println("3333")
			
			//modelName = hSSFDataFormatter.formatCellValue(row.getCell(27))
			modelName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("modelName"))))
			
			//skuCode = hSSFDataFormatter.formatCellValue(row.getCell(29))
			skuCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("skuCode"))))
			match = pattern_code.matcher(skuCode);
			if(!match.matches())
				return false;
			
			
			
			skuCode = skuCode.toString().trim()
			//styleCode = hSSFDataFormatter.formatCellValue(row.getCell(28))
			styleCode=skuCode.substring (0, 8)
			styleCode = styleCode.toString().trim()
			//System.err.println("skuCode="+skuCode)
			//System.err.println("styleCode="+styleCode)
			
			//supplyChainModel = hSSFDataFormatter.formatCellValue(row.getCell(31))
			supplyChainModel = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("supplyChainModel"))))
			
			//ean13 = hSSFDataFormatter.formatCellValue(row.getCell(32))
			ean13 = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("ean13"))))
			
			
			
			
			//skuName = hSSFDataFormatter.formatCellValue(row.getCell(33))
			skuName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("skuName"))))
			
			//colorCode = hSSFDataFormatter.formatCellValue(row.getCell(35))
			colorCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("colorCode"))))
			match = pattern_code.matcher(colorCode);
			if(!match.matches())
				return false;
			
			
			
			//colorName = hSSFDataFormatter.formatCellValue(row.getCell(36))
			colorName = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("colorName"))))
			
			//sizeCode = row.getCell(31) ? hSSFDataFormatter.formatCellValue(row.getCell(37)) : ''
			sizeCode =  hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("sizeCode"))))
			match = pattern_code.matcher(sizeCode);
			if(!match.matches())
				return false;
			
			
			
			
			//sizeName = row.getCell(32) ? hSSFDataFormatter.formatCellValue(row.getCell(38)) : ''
			sizeName =  hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("sizeName")))) 
			
			//customsCode = hSSFDataFormatter.formatCellValue(row.getCell(43))
			customsCode =  hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("customsCode"))))
			match = pattern_code.matcher(customsCode);
			if(!match.matches())
				return false;
			
			
			
			
			
			//System.err.println("customsCode="+customsCode)
			//podium = hSSFDataFormatter.formatCellValue(row.getCell(44))
			podium = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("podium"))))
			match = pattern_code.matcher(podium);
			if(!match.matches())
				return false;
			
			
			
			
			//orderType = hSSFDataFormatter.formatCellValue(row.getCell(45))
			orderType = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("orderType"))))
			
			//orderCode = hSSFDataFormatter.formatCellValue(row.getCell(50))
			orderCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("orderCode"))))
			match = pattern_code.matcher(orderCode);
			if(!match.matches())
				return false;
			
			
			
			
			//invoiceDate = row.getCell(48)
			invoiceDate = row.getCell(new Integer(properties.getProperty("invoiceDate")))
			
			//productPrice = hSSFDataFormatter.formatCellValue(row.getCell(51))
			productPrice = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("productPrice"))))
			
			
			
			
			//retailPriceEUR = hSSFDataFormatter.formatCellValue(row.getCell(52))
			retailPriceEUR = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("retailPriceEUR"))))
			
			//retailDiscountPrice = hSSFDataFormatter.formatCellValue(row.getCell(53))
			retailDiscountPrice = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("retailDiscountPrice"))))
			
			//shipQty = hSSFDataFormatter.formatCellValue(row.getCell(54))
			shipQty = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("shipQty"))))
			
			//shipGrossPrice = hSSFDataFormatter.formatCellValue(row.getCell(55))
			shipGrossPrice = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("shipGrossPrice"))))
			
			//shipDiscountPrice = hSSFDataFormatter.formatCellValue(row.getCell(56))
			shipDiscountPrice = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("shipDiscountPrice"))))
			
			//countryCode = hSSFDataFormatter.formatCellValue(row.getCell(42))
			countryCode = hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("countryCode"))))
			match = pattern_code.matcher(countryCode);
			if(!match.matches())
				return false;
			
			
			
			//Kurt added //品名 与材质
			//def longEnglishName= hSSFDataFormatter.formatCellValue(row.getCell(34))
			def longEnglishName= hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("longEnglishName"))))
			
			//def compositionEnglish= hSSFDataFormatter.formatCellValue(row.getCell(40))
			def compositionEnglish= hSSFDataFormatter.formatCellValue(row.getCell(new Integer(properties.getProperty("compositionEnglish"))))
			
			
			
			
			
			//END
			updateProduct(skuCode,skuCode.substring(0,7),familyCode,materialCode)
			
			log.info customerShortCode
			log.info customerDevIndex
			log.info customerLongCode
			log.info customerName
			log.info customerCateCode
			log.info customerCateName
			log.info materialCode
			log.info materialName
			log.info familyCode
			log.info familyName
			log.info "productCode= "+productCode
			log.info "productName= "+productName
			log.info modelCode
			log.info modelName
			log.info styleCode
			log.info skuCode
			log.info supplyChainModel
			log.info ean13
			log.info skuName
			log.info colorCode
			log.info colorName
			log.info sizeCode
			log.info sizeName
			log.info podium
			log.info orderType
			log.info orderCode
			log.info invoiceDate
			log.info productPrice
			log.info retailPriceEUR
			log.info retailDiscountPrice
			log.info retailDiscountPrice
			log.info shipQty
			log.info shipGrossPrice
			log.info shipDiscountPrice
			log.info "====>"+i
			
			InterfaceBPInvoice bpi = new InterfaceBPInvoice()
			bpi.customerDevIndex = customerDevIndex
			bpi.customerShortCode = customerShortCode
			bpi.customerLongCode = customerLongCode
			bpi.customerName = customerName
			bpi.customerCateCode = customerCateCode
			bpi.customerCateName = customerCateName
			bpi.materialCode = materialCode
			bpi.materialName = materialName
			bpi.familyCode = familyCode
			bpi.familyName = familyName
			bpi.productCode = productCode
			bpi.productName = productName
			bpi.modelCode = modelCode
			bpi.modelName = modelName
			bpi.styleCode = styleCode
			bpi.skuCode = skuCode
			bpi.skuName = skuName
			bpi.supplyChainModel = supplyChainModel
			bpi.ean13 = ean13
			bpi.colorCode = colorCode
			bpi.colorName = colorName
			bpi.sizeCode = sizeCode
			bpi.sizeName = sizeName
			bpi.podium = podium
			bpi.bpCode = bpCode
			bpi.orderType = orderType
			bpi.orderCode = orderCode
			bpi.invoiceDate = invoiceDate
			bpi.productPrice = productPrice
			bpi.retailPriceEUR = retailPriceEUR
			bpi.retailDiscountPrice = retailDiscountPrice
			bpi.shipQty = shipQty
			bpi.shipGrossPrice = shipGrossPrice
			bpi.shipDiscountPrice = shipDiscountPrice
			bpi.countryCode=countryCode
			bpi.longEnglishName=longEnglishName
			bpi.compositionEnglish=compositionEnglish
			bpi.customsCode=customsCode
			if (!bpi.save(flush: true)) {
				bpi.errors.each {
					log.info it
				}
			}
		}
		//System.err.println("true")
		return true;
			}
		catch (Exception e) {
			return false;
		  }
	}

	def updateProduct(skucode,refno,familycode,deptcode){//def skucode,familycode,deptcode
		//System.err.println("updateProduct skucode="+skucode);
		//System.err.println("updateProduct refno="+refno);
		//System.err.println("updateProduct familycode="+familycode);
		//System.err.println("updateProduct deptcode="+deptcode);
		def sql = new Sql(dataSource);
		def query = "select * from hermes_magn where code = ?"
		def results = sql.rows(query,[familycode])
		def magn=''
		if (results.size()>0){
			def unitmap=results[0]
			magn=unitmap.magndept
			if (unitmap.kb==1){
				def q="select * from hermes_magnitem where code =? and refno= ?"
				def rs=sql.rows(q,[familycode,refno])
				if (rs.size()>0){
					magn=rs[0].magndept
				}else{
					magn='Other Leather'
				}
			}
		}
		if (magn!=''){
			sql.executeUpdate('update hermes_product set magnitude=? ,dept =? where sku_code=?',[magn,deptcode,skucode])
		}
			
	}
	
	
	
	//上传宝隆的shipment
	/* upload shipment from baolong */
	def uploadBaoLongShipment={
		log.info params
		def userFile = params['userfile']
		if (userFile) {
			/* 
			 *1. update the baolong data base id
			 *2. if reupload load can be update the data
			 *3. if don't find item by id , todo: the return message for user
			 *4. others
			 */
			
			
			def message = processBLShipment(userFile.getInputStream())
			flash.message = message
			log.info message
			return false
		}
		//render(view: "/hermes/interface/uploadInvoice")
	}
	
	//Kurt Edited   //读取结算表excel
	def processBLShipment(def is){
		HSSFWorkbook wb = new HSSFWorkbook(is);
		//bfdf 在第1个sheet上K2单元
		HSSFSheet sheet0 = wb.getSheetAt(0)
		
		//contractCode  在第1个sheetB2单元  合同号
		def contractCode=sheet0.getRow(1).getCell(2)

		def bfdf=sheet0.getRow(1).getCell(10)
		String str_bfdf=bfdf;


		//按照新格式，第2个sheet开始
		HSSFSheet sheet = wb.getSheetAt(1)
		def hSSFDataFormatter = new HSSFDataFormatter()
		HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(sheet,wb);
		
		//CIFtotal CIF总价   用于分摊运费做参考比例  I4单元
		def CIFtotal=hSSFDataFormatter.formatCellValue(sheet.getRow(3).getCell(8))
		
	     if(CIFtotal)
		 CIFtotal=new BigDecimal(CIFtotal)
		 else 
		 CIFtotal=null
		
		//System.err.println("CIFtotal="+CIFtotal);
		//YZtotal   运杂总价  用于分摊运费做     L4单元
		def YZtotal= hSSFDataFormatter.formatCellValue(sheet.getRow(3).getCell(11))
		def YZundistributed=YZtotal
		
		if(YZtotal){
			YZtotal=new BigDecimal(YZtotal)
		YZundistributed=new BigDecimal(YZtotal) //未分摊部分
		//System.err.println("YZtotal="+YZtotal);
		}
		
		
		int rowNum = sheet.getLastRowNum()
		def baolong = []
		def sumCIF=0
		for(int i=10;i<=rowNum;i++){ //按照新格式，第11行开始
			def row = sheet.getRow(i)
			def id = hSSFDataFormatter.formatCellValue(row.getCell(0))
			//System.err.println("id====="+id);
			def df = new DecimalFormat("#.00")
			//log.info "id="+id
			//判断是否到列表底部
			def type= hSSFDataFormatter.formatCellValue(row.getCell(3))
			if(!id&&!type)
				break;
			if (id){
				/* update the baolong data to item */
				/*关税 yunza daili*/
				/*
				def invoice = hSSFDataFormatter.formatCellValue(row.getCell(0))
				def seq = hSSFDataFormatter.formatCellValue(row.getCell(1))
				def prod = hSSFDataFormatter.formatCellValue(row.getCell(2))
				def prodName = hSSFDataFormatter.formatCellValue(row.getCell(3))
				def t = hSSFDataFormatter.formatCellValue(row.getCell(4))
				def gs = hSSFDataFormatter.formatCellValue(row.getCell(11))
				def yz = hSSFDataFormatter.formatCellValue(row.getCell(12))
				def dl = hSSFDataFormatter.formatCellValue(row.getCell(13))
				//evaluator.setRow(row)
				//def cost = evaluator.evaluate(row.getCell(17)).getNumberValue()
			 	//def cell = row.getCell(17)
				//def cost = cell.setCellFormula(hSSFDataFormatter.formatCellValue(cell))
				//def cost = hSSFDataFormatter.getNumericCellValue(row.getCell(17))
				def cellValue = evaluator.evaluate(row.getCell(17));
			 	def cost = cellValue.getNumberValue()
			 	*/
				//Kurt Edited
				def invoice = hSSFDataFormatter.formatCellValue(row.getCell(0))
				def prod = hSSFDataFormatter.formatCellValue(row.getCell(1))
				def prodName = hSSFDataFormatter.formatCellValue(row.getCell(2))
				def t = hSSFDataFormatter.formatCellValue(row.getCell(3))
				def qty=hSSFDataFormatter.formatCellValue(row.getCell(5))
				def gs = hSSFDataFormatter.formatCellValue(row.getCell(10))
				//System.err.println("row.getCell(0)"+row.getCell(0));
				//System.err.println("row.getCell(1)"+row.getCell(1));
			//	System.err.println("row.getCell(2)"+row.getCell(2));
			//	System.err.println("row.getCell(3)"+row.getCell(3));
			//	System.err.println("row.getCell(4)"+row.getCell(4));
			//	System.err.println("row.getCell(5)"+row.getCell(5));
			//	System.err.println("row.getCell(6)"+row.getCell(6));
			//	System.err.println("row.getCell(7)"+row.getCell(7));
				def CIF=hSSFDataFormatter.formatCellValue(row.getCell(8))
				//System.err.println("row.getCell(8)"+row.getCell(8));
			//	System.err.println("row.getCell(9)"+row.getCell(9));

			//	System.err.println("row.getCell(10)"+row.getCell(10));
			//	System.err.println("row.getCell(11)"+row.getCell(11));
			//	System.err.println("row.getCell(12)"+row.getCell(12));
			//	System.err.println("row.getCell(13)"+row.getCell(13));
			//	System.err.println("row.getCell(14)"+row.getCell(14));
			//	System.err.println("row.getCell(15)"+row.getCell(15));
				def yz = hSSFDataFormatter.formatCellValue(row.getCell(11))
				def dl = hSSFDataFormatter.formatCellValue(row.getCell(12))
				def cellValue = evaluator.evaluate(row.getCell(13));//总成本
				def cost = cellValue.getNumberValue()
				
				//临时增加 分摊运杂
				 
				 
				
				if(YZtotal&&CIFtotal){
					
					//System.err.println("CIF="+CIF)
					if(CIF)
					CIF=new BigDecimal(CIF)
					else
					CIF=0
					
				   sumCIF=sumCIF+CIF
				   def oldyz=new BigDecimal(yz)
				  // System.err.println("oldyz="+oldyz)
				   def extrayz=new BigDecimal(df.format(new BigDecimal(YZtotal)*new BigDecimal(CIF)/new BigDecimal(CIFtotal)))
					yz=new BigDecimal(extrayz+oldyz)
					//System.err.println("extrayz"+extrayz)
					YZundistributed=YZundistributed-extrayz
					//System.err.println("YYZZZ="+yz)
					if(CIFtotal-sumCIF<=1)
					{
						yz=YZundistributed+yz
						//System.err.println("YYYYYYYYZZZZZZZZZZZ="+yz)
					}
					yz=df.format(yz)
				}
				//
				
				def item = [:]
				item.contractCode=contractCode
				
				
				item.invoice = invoice
				//item.seq = seq
				item.prod = prod
				item.prodName = prodName
				item.t = t
				item.qty=qty
				item.gs = gs
				item.yz = yz
				item.dl = dl
				item.cost = cost
				item.bfdf=str_bfdf
				log.info item
				baolong.add(item)
			}else{
				continue	
			} 
		}
		
		return updateShipmentItem(baolong,contractCode,new BigDecimal(YZtotal))
	}
	
	
	
	//检查该business是否在存在的submitted 的 shipment里
	def checkBusinessShipmentIsSubmitted(business_id)
	{
		def sql = new Sql(dataSource);
		
	
	   def query = " select * from hermes_shipment sh inner join hermes_shipment_hermes_business  bsh on sh.id =bsh.shipment_business_id "
	   		query=query	+ " where sh.submit=1 and bsh.business_id=? "
	   def results =  sql.rows(query,[business_id]);
		 if(results.size()>0){
			 return true;
		 }
	   	else
		   return false;
		
	}
	
	
	//更新APHL的值
	def setAPHLToHigestInvoiceHeader(contractCode,APHL)
	{
		def sql = new Sql(dataSource);
		//contractCode="2013SJH-2029"
		def invoiceHeaderCode
	//找到contractcode中最大的InvoiceHeader的code
	   def query = " select Top 1 hdr.code,hdr.total_aphl_for_this_contract_code, hdr.contract_code, hdr.re_invoice, convert(decimal(15,2),sum(item.shipmentdl+ item.shipmentyz)) as FreightAgent "

		     query=query	+ " from hermes_invoiceheader hdr "
		     query=query	+ " inner join hermes_invoiceItem item on item.invoice_header_id=hdr.id "
		     query=query	+ " where hdr.contract_code ='"+contractCode+ "'"
		     query=query	+ " group by hdr.total_aphl_for_this_contract_code, hdr.contract_code, hdr.re_invoice,hdr.code "
		     query=query	+ "  order by contract_code,FreightAgent desc "
		     
	   		 
	   def results =  sql.rows(query,[]);
		 if(results.size()>0){
			  
			 invoiceHeaderCode=results[0][0] 
			 def updateQuery = " update hermes_invoiceheader set  total_aphl_for_this_contract_code =? "
			 
			 updateQuery= updateQuery+ " WHERE  code=? "

	        return  sql.executeUpdate(updateQuery,[APHL,invoiceHeaderCode])

		 }
	   	else
		   return false;
		
	}
	
	
	
	
	
	//更新宝隆传来的excel
	def updateShipmentItem(baolong,contractCode,APHL){
		def message = ""
		baolong.each{
			log.info it
			//def seq = it.seq
			def invoice = it.invoice
			def prod = it.prod
			def product = Product.findByCode(prod)
			def invoiceHeader = InvoiceHeader.findByCode(invoice)
			
			
			
			if(product && invoiceHeader){
				invoiceHeader.contractCode=it.contractCode
				//APHL的费用再此先每个invoiceHeader都清0，随后再更新
				invoiceHeader.totalAphlForThisContractCode=0
				//System.err.println("it.contractCode="+it.contractCode)
				if(!invoiceHeader.save(flush:true)){
					invoiceHeader.errors.each{ii->
						log.info ii
					}
				}
				def invoiceItem = InvoiceItem.findByInvoiceHeaderAndProduct(invoiceHeader,product)
				
				
				
				if(invoiceItem){
					//不需要sequence了，因为1个箱子只可能有1个cartonItem
					//def cartonItem = CartonItem.findByInvoiceItemAndSequence(invoiceItem,seq) 
					
					//检查该invoiceHeader是否是在submittd的shipment里，若是，不允许修改
					if(checkBusinessShipmentIsSubmitted(invoiceHeader.business.id)){
						message = message + "ERROR:  inoivce:${invoiceHeader.code} product:${product.code} can not be updated, because this shipment is already submitted!<br>"
						return message
					}
					
					def cartonItem = CartonItem.findByInvoiceItem(invoiceItem)
					if(cartonItem){
						log.info "=it.gs="+it.gs+" it.t"+it.t+" it.cost="+it.cost
						invoiceItem.shipmentDuty = new BigDecimal(it.gs)
						//System.err.println("it.gs"+it.gs);
						//System.err.println("11111  ttttttttt="+it.t);
						invoiceItem.shipmentType = new BigDecimal(it.t)
					//	System.err.println("11111  ttttttttt="+it.t);
						invoiceItem.shipmentCost = new BigDecimal(it.cost)
					//	System.err.println("222222");
						invoiceItem.shipmentYZ = new BigDecimal(it.yz)
					//	System.err.println("333333");
						invoiceItem.shipmentDL = new BigDecimal(it.dl)
					//	System.err.println("444444");
					//	System.err.println("BFDF=========="+it.bfdf)
						invoiceItem.shipmentBFDF= new BigDecimal(it.bfdf)
						
						if(!invoiceItem.save(flush:true)){
							invoiceItem.errors.each{ii->
								log.info ii
							}
						}
						message = message + "SUCCESS:inoivce:${invoiceHeader.code} product:${product.code}  is success!<br>"
					}else{
						message = message + "ERROR:inoivce:${invoiceHeader.code} product:${product} is not match!<br>"
					}
				}else{
					message = message + "ERROR:inoivceitem product:${product} is not match!<br>"
				}
			}else{
				message = message + "ERROR:inoivce:"+invoice+" product("+prod+"):${product} is not match!<br>"
			
			}
		}
		//
		setAPHLToHigestInvoiceHeader(contractCode,APHL)
		
		return message
	}
	
	/* upload other type invoice data */
	def uploadPerfumeData={
		log.info params
		def userFile = params['userfile']
		if (userFile) {
			HSSFWorkbook wb = new HSSFWorkbook(userFile.getInputStream())
			for(int j=0;j<wb.getNumberOfSheets();j++){			
				HSSFSheet sheet = wb.getSheetAt(j)
				def hSSFDataFormatter = new HSSFDataFormatter()
				int rowNum = sheet.getLastRowNum()
				def datas = []
				for(int i=12;i<=rowNum;i++){
					def data = [:]
					def row = sheet.getRow(i)
				/*	def po = hSSFDataFormatter.formatCellValue(row.getCell(0)).trim()
					def prod = hSSFDataFormatter.formatCellValue(row.getCell(6)).trim()
					def qty = hSSFDataFormatter.formatCellValue(row.getCell(7)).trim()
					def amount = hSSFDataFormatter.formatCellValue(row.getCell(9)).trim()
					def so = hSSFDataFormatter.formatCellValue(row.getCell(10)).trim()
					def customer = hSSFDataFormatter.formatCellValue(row.getCell(11)).trim()*/
									
					def po = hSSFDataFormatter.formatCellValue(row.getCell(0)).trim()
					def prod = hSSFDataFormatter.formatCellValue(row.getCell(1)).trim()
					//def qty = hSSFDataFormatter.formatCellValue(row.getCell(2)).trim()
					def qty = row.getCell(2).getNumericCellValue()
					def amount = row.getCell(4).getNumericCellValue()
					def so = ''//hSSFDataFormatter.formatCellValue(row.getCell(10)).trim()
					if (po==''){
						break;
					}
					def customer = logisticService.getCustomerByXMag(po.substring(0,2))								
					data.code = po
					data.soCode = so
					data.prodCode = prod
					data.quantity = qty
					data.amount = amount
					data.customer = customer
					
					datas.add(data)
				}
				if(datas.size()>0){
					importService.loadPerfumeAndWatchData(datas,'1200')
				}
			}
		}
		render(view: "/hermes/interface/uploadPerfume")
	}
	def uploadWatchData={
		log.info params
		def userFile = params['userfile']
		if (userFile) {
			HSSFWorkbook wb = new HSSFWorkbook(userFile.getInputStream())
			for(int j=0;j<wb.getNumberOfSheets();j++){
				HSSFSheet sheet = wb.getSheetAt(j)
				def hSSFDataFormatter = new HSSFDataFormatter()
				int rowNum = sheet.getLastRowNum()
				def datas = []
				for(int i=12;i<=rowNum;i++){
					def data = [:]
					def row = sheet.getRow(i)
				/*	def po = hSSFDataFormatter.formatCellValue(row.getCell(0)).trim()
					def prod = hSSFDataFormatter.formatCellValue(row.getCell(6)).trim()
					def qty = hSSFDataFormatter.formatCellValue(row.getCell(7)).trim()
					def amount = hSSFDataFormatter.formatCellValue(row.getCell(9)).trim()
					def so = hSSFDataFormatter.formatCellValue(row.getCell(10)).trim()
					def customer = hSSFDataFormatter.formatCellValue(row.getCell(11)).trim()*/
					
					def po = hSSFDataFormatter.formatCellValue(row.getCell(1)).trim()
					def prod = hSSFDataFormatter.formatCellValue(row.getCell(2)).trim()
					def qty = hSSFDataFormatter.formatCellValue(row.getCell(3)).trim()
					//row.getCell(5).setCellType(HSSFCell.CELL_TYPE_NUMERIC);    
					def amount = row.getCell(5).getNumericCellValue()
					def so = ''//hSSFDataFormatter.formatCellValue(row.getCell(10)).trim()
					
					if (po==''){
						break;
					}
					def customer = logisticService.getCustomerByXMag(po.substring(0,2))					
					data.code = po
					data.soCode = so
					data.prodCode = prod
					data.quantity = qty
					data.amount = amount
					data.customer = customer
					datas.add(data)
				}
				//Kurt Edited
				System.err.println("ready to loadPerfumeAndWatchData")
				if(datas.size()>0){//Kurt Edit 返回结果
					importService.loadPerfumeAndWatchData(datas,'1800')
				}
			}
		}
		render(view: "/hermes/interface/uploadWatch")
	}
	/* export packing and invoice */
	def exportPackingInvoice={
		log.info params
		def packing = Packing.get(params.id)
		//Kurt Eidted 检查是否全部打包，若没有，显示提示
		def isAllPack=logisticService.checkIfBusinessItemAllPacked(packing)
		if(!isAllPack)
		{
			//System.err.println("not all packed")
			flash.message = "Data can not be exported until all the items have be packed!"
			redirect(action:'showPacking',controller:'invoice','id':packing.id) 
			//isAllPack=true;
 		}
		
		if(isAllPack&&packing){
			flash.message = "Data has been exported!"
			def reInvoiceNumbers = logisticService.getReInvoice(packing.business)  
			response.setContentType("application/excel"); 
			response.setHeader("Content-disposition", "attachment;filename="+packing.business.code+"-"+reInvoiceNumbers+".xls")  
			//render(contextType:"application/vnd.ms-excel")
			HSSFWorkbook wb = new HSSFWorkbook() 
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
						
			def helper = wb.getCreationHelper()
			/*
			def sheet = wb.createSheet("invoice no "+packing.business.code)        
			def row = null  
			def cell = null  
			row = sheet.createRow(1)  
			row.createCell(0).setCellValue("To")
	        row.createCell(1).setCellValue("爱马仕(上海)商贸有限公司")  
	        row.createCell(5).setCellValue("發票 INVOICE NO:")  
	        row.createCell(6).setCellValue(reInvoiceNumbers)  
			row = sheet.createRow(2)  
	        row.createCell(1).setCellValue("HERMES (CHINA ) CO., LTD")  
			row = sheet.createRow(3)  
	        row.createCell(1).setCellValue("3010FLOOR, 1038 NAN JING ROAD (E),")  
	        row.createCell(5).setCellValue("日期 Date:")  
		    String DATE_FORMAT = "yyyy-MM-dd"
		    def sdf = new SimpleDateFormat(DATE_FORMAT)
	        row.createCell(6).setCellValue(sdf.format(packing.date))
			row = sheet.createRow(4)  
	        row.createCell(1).setCellValue("SHANGHAI 200041, P.R.C.")  
			//Kurt Edited
			row = sheet.createRow(5)
			cell = row.createCell(6)
			cell.setCellValue("CIF")
			cell.setCellStyle(cs)
			cell = row.createCell(7)
			cell.setCellValue("CIF")
			cell.setCellStyle(cs)
			
			row = sheet.createRow(6)
			cell = row.createCell(0)
			cell.setCellValue("分类")
			cell.setCellStyle(cs)
			
			cell = row.createCell(1)
			cell.setCellValue("INV.")
			cell.setCellStyle(cs)
			cell = row.createCell(2)
			cell.setCellValue("编号")
			cell.setCellStyle(cs)
			cell = row.createCell(3)
			cell.setCellValue("内容")
			cell.setCellStyle(cs)
			cell = row.createCell(4)
			cell.setCellValue("原产国")
			cell.setCellStyle(cs)
			cell = row.createCell(5)
			cell.setCellValue("数量")
			cell.setCellStyle(cs)
			cell = row.createCell(6)
			cell.setCellValue("单价")
			cell.setCellStyle(cs)
			cell = row.createCell(7)
			cell.setCellValue("总价")
			cell.setCellStyle(cs)
			
			
			/*cell = row.createCell(7)
			cell.setCellValue("序号")
			cell.setCellStyle(cs)
			cell = row.createCell(8)
			cell.setCellValue("发票号")
			cell.setCellStyle(cs)			
			*/
			
			
			//END
			/*
			def results = CartonItem.withCriteria{
				invoiceItem {
					order("productName","asc")
					invoiceHeader{
						eq("business",packing.business)
					}
					product{
						order("dept","asc")
						//order("material","asc")
						//order("name","asc")
						category{
							order("name","asc")
							//order("description","asc")
						}
					}					
				}
			}

	
			def rowi = 6
			def group = ''
			def groupIndex = 1
			def groupQty = 0
			def groupCIF = 0
			def groupQtyTotal = 0
			def groupCIFTotal = 0
			def first=true
			results.each{
				//def groupName =  it.invoiceItem.product.category.name + it.invoiceItem.product.name + ' '+it.invoiceItem.product.category.description
				def groupName
				def content
				if(it.invoiceItem.productName&&it.invoiceItem.productName.length()>0){ //不使用master
					 groupName =  it.invoiceItem.productName //+ it.invoiceItem.product.category.description
					 content= it.invoiceItem.productName +it.invoiceItem.product.name + it.invoiceItem.product.category.description
				}
				else{
					groupName =  it.invoiceItem.product.category.name //+ it.invoiceItem.product.category.description
					content= it.invoiceItem.product.category.name +it.invoiceItem.product.name + it.invoiceItem.product.category.description
				}
				
				
				if(!groupName.equals(group)){
					if(!group.equals('')){
						rowi = rowi+1
						//Kurt Edited
						row = sheet.createRow(rowi)
						cell=row.createCell(5)
						cell.setCellValue(groupQty)
						cell.setCellStyle(cs)
						
						cell=row.createCell(7)
						cell.setCellValue(groupCIF)
						cell.setCellStyle(cs)
							
						groupQtyTotal = groupQtyTotal + groupQty
						groupCIFTotal = groupCIFTotal + groupCIF
						groupQty=0
						groupCIF=0
						groupIndex=groupIndex+1
						group=groupName
						log.info "write total:"+groupCIF
					}
					rowi = rowi+1
					row = sheet.createRow(rowi)
					rowi = rowi+1
					row = sheet.createRow(rowi)
					cell=row.createCell(0)
					cell.setCellValue(groupIndex)
					cell.setCellStyle(cs)
					//Kurt edited
					cell=row.createCell(1)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.invoiceItem.invoiceHeader.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(it.invoiceItem.product.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					//cell.setCellValue(groupName)
					cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.invoiceItem.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.invoiceItem.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.invoiceItem.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.invoiceItem.amount*it.invoiceItem.quantity)
					cell.setCellStyle(cs)
							
					
					/*
					cell=row.createCell(7)
					cell.setCellValue(it.sequence)
					cell.setCellStyle(cs)
										
					cell=row.createCell(8)
					cell.setCellValue(it.invoiceItem.invoiceHeader.code)
					cell.setCellStyle(cs)
					*/
					
					
					//END
			/*
					groupQty = groupQty+it.invoiceItem.quantity
					groupCIF = groupCIF+it.invoiceItem.amount*it.invoiceItem.quantity
					group=groupName
					log.info "write:"+it.invoiceItem.product.code
				}else{
					rowi = rowi+1
					row = sheet.createRow(rowi)
					cell=row.createCell(0)
					cell.setCellValue(groupIndex)
					cell.setCellStyle(cs)
					//Kurt Edited
					cell=row.createCell(1)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.invoiceItem.invoiceHeader.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(it.invoiceItem.product.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					//cell.setCellValue(groupName)
					cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.invoiceItem.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.invoiceItem.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.invoiceItem.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.invoiceItem.amount*it.invoiceItem.quantity)
					cell.setCellStyle(cs)
					groupQty = groupQty+it.invoiceItem.quantity
					groupCIF = groupCIF+it.invoiceItem.amount*it.invoiceItem.quantity
					log.info "write same:"+it.invoiceItem.product.code
					
					/*cell=row.createCell(7)
					cell.setCellValue(it.sequence)
					cell.setCellStyle(cs)
										
					cell=row.createCell(8)
					cell.setCellValue(it.invoiceItem.invoiceHeader.code)
					cell.setCellStyle(cs)
					*/
					
					//END
					/*
				}
			}
			rowi = rowi+1
			row = sheet.createRow(rowi)
			cell=row.createCell(5)
			cell.setCellValue(groupQty)
			cell.setCellStyle(cs)

			cell=row.createCell(7)
			cell.setCellValue(groupCIF)
			cell.setCellStyle(cs)
			groupQtyTotal = groupQtyTotal + groupQty
			groupCIFTotal = groupCIFTotal + groupCIF
			rowi = rowi+1
			row = sheet.createRow(rowi)
			rowi = rowi+1
			row = sheet.createRow(rowi)

			cell=row.createCell(4)
			cell.setCellValue('TOTAL')
			cell.setCellStyle(cs)

			cell=row.createCell(5)
			cell.setCellValue(groupQtyTotal)
			cell.setCellStyle(cs)

			cell=row.createCell(6)
			cell.setCellValue('EUR')
			cell.setCellStyle(cs)

			cell=row.createCell(7)
			cell.setCellValue(groupCIFTotal)
			cell.setCellStyle(cs)
			*/
			
			//导出 packinglist
			writePackingSheet(wb,packing)
			if(isAllPack)
			packing.export = 'Y'
			else
			packing.export = 'N'
			
			packing.exportDate = new Date()
			packing.save(flush:true)
	        //def cellStyle = SummaryHSSF.createStyleCell(wb)  
			def os = response.outputStream
	        //OutputStream os = new FileOutputStream(new File("/Users/xpingxu/Test.xls"))  
			wb.write(os)  
	        os.close()
			
/*			def results = InvoiceItem.withCriteria{
				invoiceHeader{
		            eq("business",packing.business)
		        }
				product{
					//order("dept","asc")
					//order("material","asc")
					order("name","asc")
					category{
						order("name","asc")
					}
				}
			}
			def rowi = 6
			def group = ''
			def groupIndex = 1
			def groupQty = 0
			def groupCIF = 0
			def groupQtyTotal = 0
			def groupCIFTotal = 0
			def first=true
			results.each{
				def groupName =  it.product.category.name + it.product.name
				if(!groupName.equals(group)){
					if(!group.equals('')){
						rowi = rowi+1
						row = sheet.createRow(rowi)
						cell=row.createCell(4)
						cell.setCellValue(groupQty)
						cell.setCellStyle(cs)
						
						cell=row.createCell(6)
						cell.setCellValue(groupCIF)
						cell.setCellStyle(cs)
							
						groupQtyTotal = groupQtyTotal + groupQty
						groupCIFTotal = groupCIFTotal + groupCIF
						groupQty=0
						groupCIF=0
						groupIndex=groupIndex+1
						group=groupName
						log.info "write total:"+groupCIF
					}
					rowi = rowi+1
					row = sheet.createRow(rowi)
					rowi = rowi+1
					row = sheet.createRow(rowi)
					cell=row.createCell(0)
					cell.setCellValue(groupIndex)
					cell.setCellStyle(cs)
					
					cell=row.createCell(1)
					cell.setCellValue(it.product.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(groupName)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
					
					groupQty = groupQty+it.quantity
					groupCIF = groupCIF+it.amount*it.quantity
					group=groupName
					log.info "write:"+it.product.code
				}else{
					rowi = rowi+1
					row = sheet.createRow(rowi)
					cell=row.createCell(0)
					cell.setCellValue(groupIndex)
					cell.setCellStyle(cs)
					
					cell=row.createCell(1)
					cell.setCellValue(it.product.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(groupName)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
					groupQty = groupQty+it.quantity
					groupCIF = groupCIF+it.amount*it.quantity
					log.info "write same:"+it.product.code
				}
			}
			rowi = rowi+1
			row = sheet.createRow(rowi)
			cell=row.createCell(4)
			cell.setCellValue(groupQty)
			cell.setCellStyle(cs)

			cell=row.createCell(6)
			cell.setCellValue(groupCIF)
			cell.setCellStyle(cs)
			groupQtyTotal = groupQtyTotal + groupQty
			groupCIFTotal = groupCIFTotal + groupCIF
			rowi = rowi+1
			row = sheet.createRow(rowi)
			rowi = rowi+1
			row = sheet.createRow(rowi)

			cell=row.createCell(3)
			cell.setCellValue('TOTAL')
			cell.setCellStyle(cs)

			cell=row.createCell(4)
			cell.setCellValue(groupQtyTotal)
			cell.setCellStyle(cs)

			cell=row.createCell(5)
			cell.setCellValue('EUR')
			cell.setCellStyle(cs)

			cell=row.createCell(6)
			cell.setCellValue(groupCIFTotal)
			cell.setCellStyle(cs)

			
			writePackingSheet(wb,packing)
			packing.export = 'Y'
			packing.exportDate = new Date()
			packing.save(flush:true)
	        //def cellStyle = SummaryHSSF.createStyleCell(wb)  
			def os = response.outputStream
	        //OutputStream os = new FileOutputStream(new File("/Users/xpingxu/Test.xls"))  
			wb.write(os)  
	        os.close()*/
		}
		
		
	}
	def writePackingSheet(wb,packing){
		
		//得到公司抬头
		java.lang.String parent_Company=new String(params.parentCompany.toString());
		if(parent_Company)
		parent_Company=parent_Company.substring(0, 1)
		
		
		def reInvoiceNumbers = logisticService.getReInvoice(packing.business)  
		def sheet = wb.createSheet("packing-"+packing.business.code)        
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		def header = sheet.getHeader();
		def centertext=HSSFHeader.font("Arail", "regular")+ HSSFHeader.fontSize((short) 9)+ properties.getProperty("headerText")
		header.setCenter(centertext)
		sheet.setMargin(HSSFSheet.TopMargin,1.6)
		def row = null  
		def cell = null  

		def totalQuantity = 0
		def i=0
		sheet.setColumnWidth(3, (short) (15550));   	//1
		//packing表头信息
		row = sheet.createRow(i++)
		HSSFCellStyle cs2 = wb.createCellStyle();
		cs2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cell=row.createCell(0)
		cell.setCellValue("裝箱單 PACKING LIST")
		cell.setCellStyle(cs2)
		sheet.addMergedRegion(new Region((short)0,(short)0,(short)0,(short)3))
		row = sheet.createRow(i)
		row = sheet.createRow(++i)
		row.createCell(0).setCellValue("To")
		
		
		if(parent_Company[0]=='A'){
			row.createCell(1).setCellValue("爱马仕(上海)商贸有限公司")
			}
			else
			{
				row.createCell(1).setCellValue("爱马仕(上海)贸易有限公司")
			}
			//row.createCell(1).setCellValue( properties.getProperty("companyNameCN") )
			row.createCell(5).setCellValue("發票 INVOICE NO:")
			row.createCell(6).setCellValue(reInvoiceNumbers)
			row = sheet.createRow(++i)
			//row.createCell(1).setCellValue("HERMES (CHINA ) CO., LTD")
			
			if(parent_Company[0]=='A'){
			row.createCell(1).setCellValue(properties.getProperty("companyNameEN_A") )
			}
			else
			{
				row.createCell(1).setCellValue(properties.getProperty("companyNameEN_B") )
			}
			row = sheet.createRow(++i)
			if(parent_Company[0]=='A'){
				row.createCell(1).setCellValue(properties.getProperty("companyAddress1_A") )
				}
				else
				{
					row.createCell(1).setCellValue(properties.getProperty("companyAddress1_B") )
				}
		///////////////////////////////////////////////////////////////////////
       // row.createCell(1).setCellValue("爱马仕(上海)商贸有限公司")  
        //row.createCell(5).setCellValue("發票 INVOICE NO:")  
        //row.createCell(6).setCellValue(reInvoiceNumbers)  
		//row = sheet.createRow(++i)
        //row.createCell(1).setCellValue("HERMES (CHINA ) CO., LTD")  
		//row.createCell(1).setCellValue(properties.getProperty("companyNameEN") )
		//row = sheet.createRow(++i)
        //row.createCell(1).setCellValue("3010FLOOR, 1038 NAN JING ROAD (E),")  
		//row.createCell(1).setCellValue(properties.getProperty("companyAddress1"))
		
		
		
		//////////////////////////////////////////////////////////////////////
        row.createCell(5).setCellValue("日期 Date:")  
	    String DATE_FORMAT = "yyyy-MM-dd"
	    def sdf = new SimpleDateFormat(DATE_FORMAT)
        row.createCell(6).setCellValue(sdf.format(packing.date))

		row = sheet.createRow(++i)
        //row.createCell(1).setCellValue("SHANGHAI 200041, P.R.C.")  
		if(parent_Company[0]=='A'){
		row.createCell(1).setCellValue(properties.getProperty("companyAddress2_A"))
		}
		else
		{
			row.createCell(1).setCellValue(properties.getProperty("companyAddress2_B") )
		}
		row = sheet.createRow(++i)
		
		//packing cartons信息 (按照carton顺序排列)
		def cartons = Carton.findAllByPacking(packing,[sort:"code",order:"asc"])
		cartons.each{
			//carton头信息
			row = sheet.createRow(++i)
			def cartonHeaderTitle = logisticService.getCartonTitleH(it)
			row.createCell(1).setCellValue(cartonHeaderTitle)
			  
			//carton表头行信息
			row = sheet.createRow(++i)
			cell=row.createCell(1)
			cell.setCellValue("数量")  
			cell.setCellStyle(cs)
						
			cell=row.createCell(2)
			cell.setCellValue("编号")  
			cell.setCellStyle(cs)			
			
			cell=row.createCell(3)
			cell.setCellValue("内容")
			cell.setCellStyle(cs)			
			
			//Kurt Edited
			/*cell=row.createCell(4)
			cell.setCellValue("序号")  
			cell.setCellStyle(cs)			

			cell=row.createCell(5)
			cell.setCellValue("发票")  
			cell.setCellStyle(cs)	
			cell=row.createCell(4)
			cell.setCellValue("INV.")
			cell.setCellStyle(cs)
			*/		
			//end
			
			
			//carton行信息
			def cartonItems = CartonItem.findAllByCarton(it, [sort:"sequence", order:"asc"])
			def quantity = 0
			cartonItems.each{ci->
				row = sheet.createRow(++i)
				cell=row.createCell(1)
				cell.setCellValue(ci.quantity)
				cell.setCellStyle(cs)			

				cell=row.createCell(2)
				cell.setCellValue(ci.invoiceItem.product.code)
				cell.setCellStyle(cs)			

				cell=row.createCell(3)
				cell.setCellValue(ci.invoiceItem.product.category.name+" "+ci.invoiceItem.product.name)
				cell.setCellStyle(cs)			
				//Kurt Edited
				/*
				cell=row.createCell(4)
				cell.setCellValue(ci.sequence)
				cell.setCellStyle(cs)			

				cell=row.createCell(5)
				cell.setCellValue(ci.invoiceItem.invoiceHeader.code)
				cell.setCellStyle(cs)			
				
				cell=row.createCell(4)
				cell.setCellValue(ci.invoiceItem.invoiceHeader.code+'-'+ci.sequence)
				cell.setCellStyle(cs)
				*/
				//END
				
				quantity = quantity + ci.quantity
			}
			//carton统计信息
			row = sheet.createRow(++i)
			cell=row.createCell(0)
			cell.setCellValue("Sub-Total")
			cell.setCellStyle(cs)			

			cell=row.createCell(1)
			cell.setCellValue(quantity)
			cell.setCellStyle(cs)			

			def cartonFooterTitle = logisticService.getCartonTitleF(it)
			cell=row.createCell(2)
			cell.setCellValue(cartonFooterTitle)
			cell.setCellStyle(cs)			
			
			row = sheet.createRow(++i)
			//统计总数
			totalQuantity = totalQuantity + quantity
		}
		//packing表尾统计信息
		row = sheet.createRow(++i)
		row = sheet.createRow(++i)
		cell=row.createCell(0)
		cell.setCellValue("TOTAL")
		cell.setCellStyle(cs)			

		cell=row.createCell(1)
		cell.setCellValue(totalQuantity)
		cell.setCellStyle(cs)			
	}
	
	
	//Kurt Edited
	def exportProductLabel={
		log.info  params
		def sql = new Sql(dataSource)
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		  
		def filename=properties.getProperty("exportTempFileFilePath")
		filename=filename+"exportProductLabel"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".xls"
		def procedurename="search_product_Label" //

		def key = " exportData "+"'"+filename+"'"+",'"+procedurename+"'"
		//System.err.println("key="+key)
		def result = sql.rows("exec "+key)
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=ProductLabel.xls")
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
	}
	
	
	
	def autoscriptFun={
		//更新logs_adj_amount
		/*def instance=Business.list();
		instance.each{item->
			System.err.println("business.id="+item.id)
			if(item.id>=1800&&item.id<=1000){  //需要注销updateBPItemsPrice里分摊的代码
			def adjItem =  logisticService.getAdjustInvoiceItem(item.id)
			 
				System.err.println("logsAdjAmount="+adjItem.logsAdjAmount)
				System.err.println("item.totalAdjDiff="+item.totalAdjDiff)
				System.err.println("adjItem.quantity="+adjItem.quantity)
			adjItem.logsAdjAmount=adjItem.amount+item.totalAdjDiff/adjItem.quantity
			
			
			adjItem.save(flush:true)
			 
			
			logisticService.summeryBusiness(item)
			//if(item.totalAmount==0)
			logisticService.updateBPItemsPrice(item) //更新depts
			}
			 
		}*/
		//更新report1bfdf report1apbl , shipment  分摊
		 def instance2=Shipment.list();
		instance2.each{  elem->
			if(elem.id>500){
				System.err.println(elem.id)
				financeService.updateSummaryShipment(elem)
			}
		}
	}
	
	//Kurt Edited
	def productStandardRef={
		
		render(view: "/hermes/interface/productStandardRef")
	}
	
	
	
	//Kurt Edited 
	def listProductStandardReference={
		System.err.println("listProductStandardReference")
		def productLabelNameZh=params.productLabelNameZh
		def executeStandard=params.executeStandard
		def validDate=params.validDate
		def saftyStandardType=params.saftyStandardType
		def remark=params.remark
		def criteriaClosure = {
				and{
					if(productLabelNameZh) 
						like("productLabelNameZh","%"+productLabelNameZh+"%")
					if(executeStandard)
						like("executeStandard","%"+executeStandard+"%")
					if(validDate)
						like("validDate","%"+validDate+"%")
					if(saftyStandardType)
						like("saftyStandardType","%"+saftyStandardType+"%")
					if(remark)
						like("remark","%"+remark+"%")
					 
				}
				order("id","asc")
			}
	 
	  
		
		
		def lt= jqgridFilterService.jqgridAdvFilter(params,StandardReference,criteriaClosure,false)
		def pager = jqgridFilterService.afterPager(params,lt.size())
		JqgridJSON jqgridJSON = new JqgridJSON(pager)
		def i=0
		lt.each { elem->
			while(i<elem.id){
				i=i+1
			}
			if(i==elem.id){
			def cell = new ArrayList()
			cell.add(elem.id)
			
			cell.add(elem.productLabelNameZh)
			cell.add(elem.executeStandard)
			cell.add(elem.validDate)
			cell.add(elem.saftyStandardType)
			cell.add(elem.remark)

			jqgridJSON.addRow(i, cell)
			}
			
		}
		render jqgridJSON.json as JSON
 
	}
	
	 
	//Kurt Edited
	def updateProductStandardReference={
		log.info  params
		if(params.oper&&params.oper=='del'){
		 
			def ref=StandardReference.get(params.id)
			if(ref)
			ref.delete();
		}
		else if(params.oper&&params.oper=='edit'){
			def sf= StandardReference.get(params.refid)
			sf.productLabelNameZh=params.productLabelNameZh
			sf.executeStandard=params.executeStandard
			sf.validDate=params.validDate
			sf.saftyStandardType=params.saftyStandardType
			sf.remark=params.remark
			sf.save(flush:true)
		}
		else if(params.oper&&params.oper=='add'){
			def sf=new StandardReference();
			
			sf.productLabelNameZh=params.productLabelNameZh
			//System.err.println("sf.productLabelNameZh="+sf.productLabelNameZh)
			sf.executeStandard=params.executeStandard
			sf.validDate=params.validDate
			sf.saftyStandardType=params.saftyStandardType
			sf.remark=params.remark
			sf.save(flush:true)
		}
		 
		System.err.println("updateProductStandardReference")
		render(view: "/hermes/interface/productStandardRef")
	}
	 
	
	
	
	
	
	//Kurt Edited
	def uploadProductLabel={
		 
		//autoscriptFun()
		//return
		 
		
		log.info params
		def userFile = params['userfile']
		XlsProcessor xlsPr=XlsProcessor.getInstance();
		 
		  
		if (userFile) {
			
            String path=request.getSession().getServletContext().getRealPath("");
			path=path.substring(0,path.lastIndexOf('\\'));
			String fileFullName=xlsPr.saveFileToLocal(userFile.getInputStream(),path)
			
			//String fileFullName=path+"\\"+filename;
			//System.err.println("fileFullName="+fileFullName)
			def sql = new Sql(dataSource)
			def query="if exists(select 1 from sysobjects where id=object_id('tmp_xxx') and type='U') drop table tmp_xxx "
                query =query+ " SELECT * INTO tmp_xxx FROM OPENROWSET('Microsoft.Jet.OLEDB.4.0', 'Excel 8.0;IMEX=1;Database="+fileFullName+ "', 'SELECT * FROM [upload\$]') "
				
				query =query+ " exec  upload_product_label_insert_newProduct  "
				query =query+ " exec  upload_product_label  " 
				query =query+ " exec update_product_label_advanced  "
				
	
		 
			//System.err.println("key="+key)
				def result=""
				try{
						result= sql.executeUpdate(query,[])
						result="Successfully"
				}
				catch(Exception e)
				{
					result="Failed"
				}
			
			def msg=result
			 
			log.info "filefullname="+fileFullName
			render(text:msg)
		}else{
		
			render(view: "/hermes/interface/uploadProductLabel")
		}
	}
	
	
	
	
	//Kurt Edited
	def uploadMagnitude={
		log.info params
		def userFile = params['userfile']
		
		//def magn = Magn.findByCode('A01')
		if (userFile) {
			
		 def msg=processMagnitudeInfo(userFile.getInputStream())
			 
			log.info "msg"+msg
			render(text:msg)
		}else{
		
			render(view: "/hermes/interface/uploadMagnitude")
		}

	}
	
	def processMagnitudeInfo(def is){
		HSSFWorkbook wb = new HSSFWorkbook(is);
		HSSFSheet sheet = wb.getSheetAt(0)
		def hSSFDataFormatter = new HSSFDataFormatter()
		def cell
		int rowNum = sheet.getLastRowNum()
		//log.info rowNum
		for (int i = 0; i <= rowNum; i++) {
			def row = sheet.getRow(i)
			def  styleCode, magnitude 
			styleCode = hSSFDataFormatter.formatCellValue(row.getCell(0))
			magnitude = hSSFDataFormatter.formatCellValue(row.getCell(1))
			if(styleCode&&magnitude&&styleCode!=""&&magnitude!=""){
				//System.err.println("styleCode="+styleCode+"=")
				//System.err.println("magnitude="+magnitude+"=")
				updateMagnitudeInfo(styleCode,magnitude)
				updateMagnitudeFinalRef(styleCode,magnitude)
				if(!HasMagnitudeFinalRef(styleCode)){
					insertMagnitudeFinalRef(styleCode,magnitude)
				}
			}
		}
		return "success"
	}
	
	
	def updateMagnitudeInfo(def styleCode, def magnitude){
		def sql = new Sql(dataSource);
		
		
	   def query = " UPDATE [next_devdb].[dbo].[hermes_product] "
			   query= query+" set magnitude = ? "
			   query= query+ " WHERE  style_code=? "

	   def results = sql.executeUpdate(query,[magnitude,styleCode])
	   //System.err.println(query);
	   //System.err.println(results);
	   //sql.commit();
	   return results
	}
	
	def updateMagnitudeFinalRef(def styleCode, def magnitude){
		def sql = new Sql(dataSource);

	   def query = " UPDATE [next_devdb].[dbo].[final] "
			   query= query+" set [Magnitude Dpt] = ? "
			   query= query+ " WHERE  REFERENCE=? "

	   def results = sql.executeUpdate(query,[magnitude,styleCode])
	   //System.err.println(query);
	   //System.err.println(results);
	   //sql.commit();
	   return results
	}
	
	def HasMagnitudeFinalRef(def styleCode){
		def sql = new Sql(dataSource);
		
	   def query = "select [Magnitude Dpt] from [next_devdb].[dbo].[final]  where REFERENCE= ?"

	   def results = sql.rows(query,[styleCode])
	   
	   if(results.size()>0)
	   return true;
	   else
	   return false;
	}
	
	def insertMagnitudeFinalRef(def styleCode, def magnitude){
		def sql = new Sql(dataSource);

	   def query = " insert into [next_devdb].[dbo].[final] (REFERENCE,[Magnitude Dpt]) "
		   query = query+" values (?,?)"
	   def results = sql.executeUpdate(query,[styleCode,magnitude])

	   return results
	}
	
	
	//Kurt Edited
	def exportStandardReference={
		System.err.println("exportStandardReference  began")
		log.info  params
		def sql = new Sql(dataSource)
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		  
		def filename=properties.getProperty("exportTempFileFilePath")
		filename=filename+"standardref"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".xls"
		def procedurename="search_standard_reference"

		def key = " exportData "+"'"+filename+"'"+",'"+procedurename+"'"
		//System.err.println("key="+key)
		def result = sql.rows("exec "+key)
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=standardref.xls")
		
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
		
		
		
	}
	
	
	//Kurt Eidted
	def	exportDeliveryReport={
		def cocitis=params.cocitis
		def store =  params['store']
		 
		def cocitisLabel='NORMAL'
		if(cocitis=='1'){
			cocitisLabel='CO&CITIS'
		}
		else{
			cocitisLabel='NORMAL'
		}
		
		//System.err.println("cocitis====="+cocitis)
		def podiumlist=getDeliveryReportlist_Podiumlist(store,cocitis)
		def deptList=getDeliveryReportlist_Deptlist(store,cocitis)
		def instanceList=getDeliveryReportlist(store,cocitis)
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=Delivery Report "+store+" "+cocitisLabel+".xls")
		HSSFWorkbook wb = new HSSFWorkbook()
		def helper = wb.getCreationHelper()
		HSSFSheet sheet = wb.createSheet(store+" "+cocitisLabel)
		def rowi = 0
		def data_x1=0
		def data_y1=0
		def data_x2=0
		def data_y2=0
		def row = null
		def cell = null
		row = sheet.createRow(rowi)
		row.createCell(0).setCellValue(cocitisLabel+" Shipment")
		rowi=rowi+4
		row = sheet.createRow(rowi)
		
		row.createCell(0).setCellValue("求和项:Qty")
		row.createCell(1).setCellValue("Podium")
		rowi=rowi+1
		row = sheet.createRow(rowi)
		row.createCell(0).setCellValue("DEPT")
		def i=1
		data_y1=i
		 podiumlist.each{elem->
			 row.createCell(i).setCellValue(elem.podium)
			   i=i+1
		 }
		 row.createCell(i).setCellValue("总计")
		 data_y2=i-1
		 rowi=rowi+1
		 data_x1=rowi
		 deptList.each{elem->
			 row = sheet.createRow(rowi)
			 row.createCell(0).setCellValue(elem.dept)
			   rowi=rowi+1
		 }
		 data_x2=rowi-1
		 row = sheet.createRow(rowi)
		 row.createCell(0).setCellValue("总计")
		 def hSSFDataFormatter = new HSSFDataFormatter()
		 for(int j=data_x1;j<=data_x2;j++){
			 def amount=0
		 	for(int k=data_y1;k<=data_y2;k++){
				 def row2 = sheet.getRow (j)
				 def param_dept=hSSFDataFormatter.formatCellValue(row2.getCell (data_y1-1))
				 def row3 = sheet.getRow(data_x1-1)
				 def param_podium=hSSFDataFormatter.formatCellValue(row3.getCell(k))
				 def qty=0
				 instanceList.each{elem->
					 if(elem.podium==param_podium&&elem.dept==param_dept){
					 qty=elem.total
					 
					 }
				 }
				 row2.createCell(k).setCellValue(qty)
				 amount=amount+qty
			 }
			 sheet.getRow(j).createCell(data_y2+1).setCellValue(amount)//设置右侧竖直总计
		 }
		 
		//设置最后一行总计
		 for(int k=data_y1;k<=data_y2;k++){
			 def amount=0
			 for(int j=data_x1;j<=data_x2;j++){
				 int qty=new Integer(hSSFDataFormatter.formatCellValue(sheet.getRow (j).getCell(k)))
				 amount=amount+qty
				 
			 }
			 sheet.getRow(data_x2+1).createCell(k).setCellValue(amount)
		 }
		 
		//计算总后总数
		 def totalqty=0
		 for(int k=data_y1;k<=data_y2;k++){
			 int qty=new Integer(hSSFDataFormatter.formatCellValue(sheet.getRow (data_x2+1).getCell(k)))
			 totalqty=totalqty+qty
		 }
		 sheet.getRow(data_x2+1).createCell(data_y2+1).setCellValue(totalqty)
		 
		 
		 //计算没有deliver的部分在第二个sheet上
		 
		 def instanceList_delivered=getDeliveryReportlistAlreadyDelivered(store,cocitis)
		 
		HSSFSheet sheet2 = wb.createSheet(store+"(Delivered)"+cocitisLabel)
		def II_rowi = 0
		def II_data_x1=0
		def II_data_y1=0
		def II_data_x2=0
		def II_data_y2=0
		def II_row = null
		def II_cell = null
		II_row = sheet2.createRow(II_rowi)
		II_row.createCell(0).setCellValue(cocitisLabel+" Shipment")
		II_rowi=II_rowi+4
		II_row = sheet2.createRow(II_rowi)
		
		II_row.createCell(0).setCellValue("求和项:Qty")
		II_row.createCell(1).setCellValue("Podium")
		II_rowi=II_rowi+1
		II_row = sheet2.createRow(II_rowi)
		II_row.createCell(0).setCellValue("DEPT")
		def II_i=1
		II_data_y1=II_i
		 podiumlist.each{elem->
			 II_row.createCell(II_i).setCellValue(elem.podium)
			   II_i=II_i+1
		 }
		 II_row.createCell(II_i).setCellValue("总计")
		 II_data_y2=II_i-1
		 II_rowi=II_rowi+1
		 II_data_x1=II_rowi
		 deptList.each{elem->
			 II_row = sheet2.createRow(II_rowi)
			 II_row.createCell(0).setCellValue(elem.dept)
			   II_rowi=II_rowi+1
		 }
		 II_data_x2=II_rowi-1
		 II_row = sheet2.createRow(II_rowi)
		 II_row.createCell(0).setCellValue("总计")
		 def II_hSSFDataFormatter = new HSSFDataFormatter()
		 for(int j=II_data_x1;j<=II_data_x2;j++){
			 def amount=0
		 	for(int k=II_data_y1;k<=II_data_y2;k++){
				 def row2 = sheet2.getRow (j)
				 def param_dept=II_hSSFDataFormatter.formatCellValue(row2.getCell (II_data_y1-1))
				 def row3 = sheet2.getRow(II_data_x1-1)
				 def param_podium=II_hSSFDataFormatter.formatCellValue(row3.getCell(k))
				 def qty=0
				 instanceList_delivered.each{elem->
					 //System.err.println("elem="+elem)
					 if(elem.podium&&elem.podium==param_podium&&elem.dept==param_dept){
					 qty=elem.total
					 
					 }
				 }
				 row2.createCell(k).setCellValue(qty)
				 amount=amount+qty
			 }
			 sheet2.getRow(j).createCell(II_data_y2+1).setCellValue(amount)//设置右侧竖直总计
		 }
		 
		//设置最后一行总计
		 for(int k=II_data_y1;k<=II_data_y2;k++){
			 def amount=0
			 for(int j=II_data_x1;j<=II_data_x2;j++){
				 int qty=new Integer(II_hSSFDataFormatter.formatCellValue(sheet2.getRow (j).getCell(k)))
				 amount=amount+qty
				 
			 }
			 sheet2.getRow(II_data_x2+1).createCell(k).setCellValue(amount)
		 }
		 
		//计算总后总数
		 def II_totalqty=0
		 for(int k=II_data_y1;k<=II_data_y2;k++){
			 int qty=new Integer(II_hSSFDataFormatter.formatCellValue(sheet2.getRow (II_data_x2+1).getCell(k)))
			 II_totalqty=II_totalqty+qty
		 }
		 sheet2.getRow(II_data_x2+1).createCell(II_data_y2+1).setCellValue(II_totalqty)
		 
		 
		 
		 
		 
		 def os = response.outputStream
		 wb.write(os)
		 os.close()
		
		
	}
	//Kurt Edited 得到deliveryreport  已经deliver的 
	def getDeliveryReportlistAlreadyDelivered(store,cocitis){
		def sql = new Sql(dataSource);
		
	
	   def query = " select dept, it.podium,sum(quantity) as total from hermes_invoiceitem it inner join hermes_product pro on pro.id=it.product_id "
		   query=query+" inner join hermes_invoiceheader hdr on hdr.id=it.invoice_header_id "
		   query=query+" inner join hermes_business bus on bus.id=hdr.business_id "
		   query=query+" inner join hermes_customer cust on cust.id=bus.customer_id "
		   query=query+" where dept<>'' and cust.re_invoice_pre_code= ? and bus.cocitis= ?  and hdr.required_delivery_by_store_date is not null and hdr.required_delivery_by_store_date<>'' group by dept,it.podium order by dept asc "

	   def Totallist = sql.rows(query,[store,cocitis]);

	   return Totallist
	}
	
	//Kurt Edited 得到deliveryreport 的DEPT列 数据 
	def getDeliveryReportlist_Deptlist(store,cocitis){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = " select distinct(dept) from hermes_product where dept in ( select dept  from hermes_invoiceitem it inner join hermes_product pro on pro.id=it.product_id "
			query=query+" inner join hermes_invoiceheader hdr on hdr.id=it.invoice_header_id "
			query=query+" inner join hermes_business bus on bus.id=hdr.business_id "
			query=query+" inner join hermes_customer cust on cust.id=bus.customer_id "
			query=query+" where dept<>'' and cust.re_invoice_pre_code= ? and bus.cocitis= ? ) order by dept asc "

		def Totallist = sql.rows(query,[store,cocitis]);

		return Totallist
	}
	
	//Kurt Edited 得到deliveryreport 的Podium列 数据
	def getDeliveryReportlist_Podiumlist(store,cocitis){
		
		def sql = new Sql(dataSource);

		def query = " select distinct(podium) from hermes_invoiceitem where podium in (select it.podium from hermes_invoiceitem it inner join hermes_product pro on pro.id=it.product_id "
			query=query+ " inner join hermes_invoiceheader hdr on hdr.id=it.invoice_header_id "
			query=query+ " inner join hermes_business bus on bus.id=hdr.business_id "
			query=query+ " inner join hermes_customer cust on cust.id=bus.customer_id "
			query=query+ " where dept<>'' and cust.re_invoice_pre_code= ? and bus.cocitis= ?   )  order by podium asc"

		def Totallist = sql.rows(query,[store,cocitis]);

		return Totallist
	}
	
	//Kurt edited 得到deliveryreport 的 数据
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
	

	/* export purchase order */
	def exportPurchaseOrder={
		def id = params.id
		def business = Business.get(id)
		//def supplier = business.supplier
		def po =business
		def items = params.items.split(",")
		//def po = Business.get(params.id)
		if(po){
			response.setContentType("application/excel"); 
			response.setHeader("Content-disposition", "attachment;filename=SHS_IMPO.xls")  
			HSSFWorkbook wb = new HSSFWorkbook() 
			def helper = wb.getCreationHelper()
			HSSFSheet sheet = wb.createSheet("SHEET")
			
			/*sheet.setColumnWidth(0, (short) (10*256))
			sheet.setColumnWidth(1, (short) (1*256));
			sheet.setColumnWidth(2, (short) (8*256));
			sheet.setColumnWidth(3, (short) (10*256));
			sheet.setColumnWidth(4, (short) (6*256));
			sheet.setColumnWidth(5, (short) (6*256));
			sheet.setColumnWidth(6, (short) (20*256));
			sheet.setColumnWidth(7, (short) (12*256));
			sheet.setColumnWidth(8, (short) (2*256));
			sheet.setColumnWidth(9, (short) (12*256));
			sheet.setColumnWidth(10, (short) (15*256));*/
		
			sheet.setColumnWidth(0, (short) (2750))
			sheet.setColumnWidth(1, (short) (450));
			sheet.setColumnWidth(2, (short) (2225));
			sheet.setColumnWidth(3, (short) (2750));
			sheet.setColumnWidth(4, (short) (1725));
			sheet.setColumnWidth(5, (short) (1725));
			sheet.setColumnWidth(6, (short) (5320));
			sheet.setColumnWidth(7, (short) (3250));
			sheet.setColumnWidth(8, (short) (700));
			sheet.setColumnWidth(9, (short) (3250));
			sheet.setColumnWidth(10, (short) (4025));
			//System.err.println("aaa")
			def row = null  
			def cell = null  
			def rowi = -1
			rowi=rowi+1
			row = sheet.createRow(rowi)  
			row.createCell(0).setCellValue("Purch order no")
			row.createCell(1).setCellValue("Order type")
			row.createCell(2).setCellValue("Order date")
			row.createCell(3).setCellValue("Supplier code buyer")
			row.createCell(4).setCellValue("WH ID")
			row.createCell(5).setCellValue("Location")
			row.createCell(6).setCellValue("Stock code")
			row.createCell(7).setCellValue("Qty order")
			row.createCell(8).setCellValue("Curr code")
			row.createCell(9).setCellValue("Unit price")
			row.createCell(10).setCellValue("Supplier sales order no.")
			
			def poc = po.poCode
			def so = po.soCode
			def ot = 1
			String DATE_FORMAT = "yyyyMMdd";
		    def sdf = new SimpleDateFormat(DATE_FORMAT);
			def od=''
			log.info po.deliveryDate
			if(po.deliveryDate){
				od = sdf.format(po.deliveryDate)
			}
			def supplier = po.supplier
			def wh = po.customer.whId
			def loc = po.customer.locId
			def cc = 0
			if(supplier && (supplier.code=='1800' || supplier.code == '1200')){
				def bis = BusinessItem.findAllByBusiness(po)
				bis.each{bi->
					if(items.count(bi.id.toString())<=0){
						rowi = rowi+1
						row = sheet.createRow(rowi)
						row.createCell(0).setCellValue(poc)
						row.createCell(1).setCellValue(ot)
						row.createCell(2).setCellValue(od)
						row.createCell(3).setCellValue(supplier)
						row.createCell(4).setCellValue(wh)
						row.createCell(5).setCellValue(loc)
						if(bi.product){
							row.createCell(6).setCellValue(bi.product.code)
						}else{
							row.createCell(6).setCellValue(bi.code)
						}
						//log.info it.quantity[0]
						row.createCell(7).setCellValue(bi.quantity)
						row.createCell(8).setCellValue(cc)
						row.createCell(9).setCellValue(bi.amount)
						row.createCell(10).setCellValue(so)
					}
				}
			}else{
				po.invoiceHeaders.each{ih->
					ih.invoiceItems.each{elem->
						if((items.count(elem.id.toString())<=0)&&(elem.magitudem.trim().toUpperCase()!="NONE")){
							rowi = rowi+1
							row = sheet.createRow(rowi)
							row.createCell(0).setCellValue(elem.poCode)
							row.createCell(1).setCellValue(ot)
							row.createCell(2).setCellValue(od)
							if(supplier)
							row.createCell(3).setCellValue(supplier.code)
							else
							row.createCell(3).setCellValue('')
							row.createCell(4).setCellValue(wh)
							row.createCell(5).setCellValue(loc)
							row.createCell(6).setCellValue(elem.product.code)
							row.createCell(7).setCellValue(elem.quantity)
							row.createCell(8).setCellValue(cc)
							def rate = financeService.getShipmentRate(elem.invoiceHeader)
							//def adjItem = logisticService.getAdjustInvoiceItem(elem.invoiceHeader.business.id)
							//row.createCell(9).setCellValue(elem.getShipmentUPS(rate))
							//if( adjItem && elem.id == adjItem.id ){
							//	def df = new DecimalFormat("#.0000")
							//	row.createCell(9).setCellValue(new BigDecimal(df.format(elem.getShipmentUPS(rate)+elem.invoiceHeader.business.totalAdjDiff/elem.quantity)))
							//}
							//else
							def df = new DecimalFormat("#.00")
							row.createCell(9).setCellValue(df.format(elem.getShipmentUPS(rate)+rate*(elem.logsAdjAmount-elem.amount)))
							
							//def shipment = Shipment.find("from Shipment s join s.business b where b=?",[elem.invoiceHeader.business])
							//if(shipment){
								//row.createCell(9).setCellValue(elem.getShipmentUPS(rate))
							//}
							row.createCell(10).setCellValue(so)
						}
					}
				}
			}
			def os = response.outputStream
			wb.write(os)  
	        os.close()
	
			po.toScala='Y'
			po.save(flush:true)
		}	
	}
	
	/* export purchase order product */
	def exportPOProduct={
		def items=params.items
		if(items){
			response.setContentType("application/excel"); 
			response.setHeader("Content-disposition", "attachment;filename=SHS_ITEM.xls")  
			HSSFWorkbook wb = new HSSFWorkbook()   
			def helper = wb.getCreationHelper()
			def sheet = wb.createSheet("product")   
			sheet.setColumnWidth(0, (short) (5320))		//20
			sheet.setColumnWidth(1, (short) (450));   	//1
			sheet.setColumnWidth(2, (short) (700));	    //2
			sheet.setColumnWidth(3, (short) (1204));	//4
			sheet.setColumnWidth(4, (short) (1204));	//4
			sheet.setColumnWidth(5, (short) (1204));    //4
			sheet.setColumnWidth(6, (short) (700));		//2
			sheet.setColumnWidth(7, (short) (3250));	//12
			sheet.setColumnWidth(8, (short) (3250));	//12
			sheet.setColumnWidth(9, (short) (3250));	//12
			sheet.setColumnWidth(10, (short) (3250));	//12
			sheet.setColumnWidth(11, (short) (450));	//1
			sheet.setColumnWidth(12, (short) (450));	//1
			sheet.setColumnWidth(13, (short) (700));	//2
			sheet.setColumnWidth(14, (short) (7820));	//30
			
			     
			def row = null  
			def cell = null  
			def rowi = -1

			def pos = items.split(',')
			pos.each{
				def po=Business.get(it)
				if(po){
					if(po.supplier&&(po.supplier.code=='1800' || po.supplier.code=='1200')){
						def bis = BusinessItem.findAllByBusiness(po)
						bis.each{
							rowi = rowi+1
							row = sheet.createRow(rowi)
							if(it.product){
								row.createCell(0).setCellValue(it.product.code)
							}else{
								row.createCell(0).setCellValue(it.code)
							}
							row.createCell(1).setCellValue('0')
							row.createCell(2).setCellValue('00')
							row.createCell(3).setCellValue('')
							row.createCell(4).setCellValue('')
							row.createCell(5).setCellValue('')
							row.createCell(6).setCellValue('00')
							def df = new DecimalFormat("#.00")
							
												 
							row.createCell(7).setCellValue(df.format(it.amount))
							row.createCell(8).setCellValue(df.format(it.amount))
							row.createCell(9).setCellValue(df.format(it.amount))
							row.createCell(10).setCellValue('')
							row.createCell(11).setCellValue('N')
							row.createCell(12).setCellValue('N')
							row.createCell(13).setCellValue('17')
							row.createCell(14).setCellValue(it.code)
						}
					}else{
						po.invoiceHeaders.each{ih->
							def rate = financeService.getShipmentRate(ih)
							ih.invoiceItems.each{ii->
								rowi = rowi+1
								row = sheet.createRow(rowi)
								def product = ii.product
								row.createCell(0).setCellValue(product.code)
								row.createCell(1).setCellValue('0')
								row.createCell(2).setCellValue('00')
								row.createCell(3).setCellValue(product.dept)
								row.createCell(4).setCellValue(product.speciality)
								row.createCell(5).setCellValue(product.productCode)
								row.createCell(6).setCellValue('00')
								
								def price = ii.getShipmentUPS(rate)
								def df = new DecimalFormat("#.00")
								row.createCell(7).setCellValue(df.format(price))
								row.createCell(8).setCellValue(df.format(price))
								row.createCell(9).setCellValue(df.format(price))
								row.createCell(10).setCellValue('')
								row.createCell(11).setCellValue('N')
								row.createCell(12).setCellValue('N')
								row.createCell(13).setCellValue('17')
								row.createCell(14).setCellValue(product.name)
							}
						}
					}
				}
			}
			def os = response.outputStream
			wb.write(os)  
	        os.close()
		}
	}
	
	def exportPOItem={
		log.info params
		response.setContentType("application/excel"); 
		response.setHeader("Content-disposition", "attachment;filename=SHS_ITEM.xls")  
		HSSFWorkbook wb = new HSSFWorkbook()   
		def helper = wb.getCreationHelper()
		def sheet = wb.createSheet("product")   
		sheet.setColumnWidth(0, (short) (5320))		//20
		sheet.setColumnWidth(1, (short) (450));   	//1
		sheet.setColumnWidth(2, (short) (700));	    //2
		sheet.setColumnWidth(3, (short) (1204));	//4
		sheet.setColumnWidth(4, (short) (1204));	//4
		sheet.setColumnWidth(5, (short) (1204));    //4
		sheet.setColumnWidth(6, (short) (700));		//2
		sheet.setColumnWidth(7, (short) (3250));	//12
		sheet.setColumnWidth(8, (short) (3250));	//12
		sheet.setColumnWidth(9, (short) (3250));	//12
		sheet.setColumnWidth(10, (short) (3250));	//12
		sheet.setColumnWidth(11, (short) (450));	//1
		sheet.setColumnWidth(12, (short) (450));	//1
		sheet.setColumnWidth(13, (short) (700));	//2
		sheet.setColumnWidth(14, (short) (7820));	//30
		
		     
		def row = null  
		def cell = null  
		def rowi = -1
		def date1=params.date1
		def date2=params.date2	
		//System.err.println("date1="+date1)
		//System.err.println("date2="+date2)
		
		DateFormat f   =   new   SimpleDateFormat( "yyyy-MM-dd"); 
		def startDate   =   f.parse(date1); 
		def endDate   =   f.parse(date2); 
		endDate=endDate+1
		System.err.println("startDate="+startDate)
		System.err.println("endDate="+endDate)
		def pos = Business.withCriteria{
				and{
					ge("deliveryDate",startDate)
					lt("deliveryDate",endDate)
				}
		}
		 
		//def pos = items.split(',')
		pos.each{po->
			if(po){
				if(po.supplier&&(po.supplier.code=='1800' || po.supplier.code=='1200')){
					def bis = BusinessItem.findAllByBusiness(po)
					bis.each{
						rowi = rowi+1
						row = sheet.createRow(rowi)
						if(it.product){
							row.createCell(0).setCellValue(it.product.code)
						}else{
							row.createCell(0).setCellValue(it.code)
						}
						row.createCell(1).setCellValue('')
						row.createCell(2).setCellValue('00')
						row.createCell(3).setCellValue('')
						row.createCell(4).setCellValue('')
						row.createCell(5).setCellValue('')
						row.createCell(6).setCellValue('00')
						row.createCell(7).setCellValue(it.amount)
						row.createCell(8).setCellValue(it.amount)
						row.createCell(9).setCellValue(it.amount)
						row.createCell(10).setCellValue('')
						row.createCell(11).setCellValue('N')
						row.createCell(12).setCellValue('N')
						row.createCell(13).setCellValue('17')
						row.createCell(14).setCellValue(it.code)
					}
				}else{
					po.invoiceHeaders.each{ih->
						//System.err.println("po"+po.code)
						def rate = financeService.getShipmentRate(ih)
						ih.invoiceItems.each{ii->
							rowi = rowi+1
							row = sheet.createRow(rowi)
							def product = ii.product
							row.createCell(0).setCellValue(product.code)
							row.createCell(1).setCellValue('')
							row.createCell(2).setCellValue('00')
							row.createCell(3).setCellValue(product.dept)
							row.createCell(4).setCellValue(product.speciality)
							row.createCell(5).setCellValue(product.productCode)
							row.createCell(6).setCellValue('00')
							
							def price = ii.getShipmentUPS(rate)
							def df = new DecimalFormat("#.00")
	 
							row.createCell(7).setCellValue(df.format(price))
							row.createCell(8).setCellValue(df.format(price))
							row.createCell(9).setCellValue(df.format(price))
							row.createCell(10).setCellValue('')
							row.createCell(11).setCellValue('N')
							row.createCell(12).setCellValue('N')
							row.createCell(13).setCellValue('17')
							row.createCell(14).setCellValue(product.name)
						}
					}
				}
			}
		}
		def os = response.outputStream
		wb.write(os)  
        os.close()

}
	//Kurt Edited 导出 follow up
	def exportFollowUpReport={
		def cocitis=params.cocitis
		def inYear =  params['inYear']
		def store =  params['store']
		def inMonth=params['inMonth']
		//System.err.println("store="+store);
		//System.err.println("cocitis="+cocitis);
		//System.err.println("inYear="+inYear);
		//System.err.println("inMonth="+inMonth);
		//Date searchdate=new Date();
		if(cocitis=='0')
		cocitis=null
		
		if(store=='All')
		store=null
		if(inYear=='this year')
		inYear=null
		if(inMonth=='this month')
		inMonth=null
		//System.err.println("inYear="+inYear);
		//System.err.println("inMonth="+inMonth);
		//System.err.println("store="+store);
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
		if(!inYear||inYear=='this year'){
			searchdate=new Date();
		}
		else{
			searchdate.setYear (new Integer(inYear)-1900);
		}
		 
		 String str=(searchdate.getYear()+1900).toString();
		 str=str.substring(2);
		 inYear=str;
		System.err.println("inYear="+str);
		 */
			
			def invoiceitems=InvoiceItem.withCriteria{
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
			
			//System.err.println("invoiceitems="+invoiceitems.size());
			
		if(invoiceitems){
			response.setContentType("application/excel");
			//System.err.println("in");
			
			//def invoiceIdList = invoiceIds.split(',')
			def sheetName=''
			String DATE_FORMAT = "yyyyMM";
			def sdf = new SimpleDateFormat(DATE_FORMAT);
			def invoiceItem =InvoiceItem.get(invoiceitems[0].id);
			if(invoiceItem){
				if(invoiceItem.invoiceHeader.business.cocitis==true){
					sheetName="CO&CITIS "+sdf.format(searchBegindate)
				}
				else{
					sheetName="NORMAL "+sdf.format(searchBegindate)
				}
				
			}
			response.setHeader("Content-disposition", "attachment;filename=Follow Up Report "+sheetName+".xls")
			HSSFWorkbook wb = new HSSFWorkbook()
			def helper = wb.getCreationHelper()
			HSSFSheet sheet = wb.createSheet(sheetName)
			sheet.setColumnWidth(0, (short) (2750))		//20
			sheet.setColumnWidth(1, (short) (2750));   	//1
			sheet.setColumnWidth(2, (short) (2750));	    //2
			sheet.setColumnWidth(3, (short) (2750));	//4
			sheet.setColumnWidth(4, (short) (2750));
			sheet.setColumnWidth(5, (short) (2750));	    //2
			sheet.setColumnWidth(6, (short) (2750));	//4
			sheet.setColumnWidth(7, (short) (2750));
			sheet.setColumnWidth(8, (short) (2750));	//4
			sheet.setColumnWidth(9, (short) (10750));
			sheet.setColumnWidth(10, (short) (2750));
			sheet.setColumnWidth(11, (short) (2750));
			sheet.setColumnWidth(12, (short) (2750));
			sheet.setColumnWidth(13, (short) (2750));
			sheet.setColumnWidth(14, (short) (2750));
			sheet.setColumnWidth(15, (short) (2750));
			sheet.setColumnWidth(16, (short) (2750));
			def row = null
			def cell = null
			def rowi = 0
			row = sheet.createRow(rowi)
			row.createCell(0).setCellValue("Status")
			row.createCell(1).setCellValue("Paris invoice no.")
			row.createCell(2).setCellValue("Podium")
			row.createCell(3).setCellValue("Creation date")
			row.createCell(4).setCellValue("DEPT")
			row.createCell(5).setCellValue("Speciality code")
			row.createCell(6).setCellValue("Short ref.")
			row.createCell(7).setCellValue("Short ref.")
			row.createCell(8).setCellValue("Full ref.")
			row.createCell(9).setCellValue("Description")
			row.createCell(10).setCellValue("Qty")
			row.createCell(11).setCellValue("Amount")
			row.createCell(12).setCellValue("Freight")
			row.createCell(13).setCellValue("Total Amount")
			row.createCell(14).setCellValue("Unit price")
			row.createCell(15).setCellValue("year")
			row.createCell(16).setCellValue("month")
			
			rowi=rowi+1;

			invoiceitems.each{
				//System.err.println("it"+it)
				def item =it;
				if(item){
				 
					row = sheet.createRow(rowi);
					rowi=rowi+1;
                    row.createCell(0).setCellValue(item.invoiceHeader.reInvoice);
					row.createCell(1).setCellValue(item.invoiceHeader.code)
					row.createCell(2).setCellValue(item.podium)
					String DATE_FORMAT_nyr = "yyyy-MM-dd"
					def sdf_nyr = new SimpleDateFormat(DATE_FORMAT_nyr)
					if(item.invoiceHeader.invoiceDate){
					row.createCell(3).setCellValue(sdf_nyr.format(item.invoiceHeader.invoiceDate))
					}
					else
					row.createCell(3).setCellValue(null)
					if( item.metaClass.hasProperty(item,'product')){
					row.createCell(4).setCellValue(item.product.dept)
					row.createCell(5).setCellValue(item.product.familyCode)
					row.createCell(6).setCellValue(item.product.productCode)
					row.createCell(7).setCellValue(item.product.styleCode)
					row.createCell(8).setCellValue(item.product.skuCode)
					}
					else{
						row.createCell(4).setCellValue('')
						row.createCell(5).setCellValue('')
						row.createCell(6).setCellValue('')
						row.createCell(7).setCellValue('')
						row.createCell(8).setCellValue('')

					}
					def content=''
					if(item.productName&&item.productName.length()>0){ //不使用master
						if( item.metaClass.hasProperty(item,'product'))
						content= item.productName +item.product.name + item.product.category.description
						else
						content= item.productName
				   }
				   else{
					   if( item.metaClass.hasProperty(item,'product'))
					   content= item.product.category.name +item.product.name + item.product.category.description
					   else
					   content= ''
				   }
					row.createCell(9).setCellValue(content)
					row.createCell(10).setCellValue(item.quantity)
					
					///
					def df = new DecimalFormat("#.00")
					def mf = new DecimalFormat("#.0000")
					if(item.logsAdjAmount){
						row.createCell(11).setCellValue(df.format(item.logsAdjAmount*item.quantity))
						row.createCell(12).setCellValue(item.logisticFreight)
					
						row.createCell(13).setCellValue(df.format(item.logsAdjAmount*item.quantity+item.logisticFreight))
					}
					else
					{
						row.createCell(11).setCellValue('')
						row.createCell(12).setCellValue('')
					
						row.createCell(13).setCellValue('')
					}
		
					if(item.quantity!=0&&item.logsAdjAmount){
						 row.createCell(14).setCellValue(mf.format((item.logsAdjAmount*item.quantity+item.logisticFreight)/item.quantity))
						 
					}
					else
				    row.createCell(14).setCellValue('') 
				   
					
					def sdf1 = new SimpleDateFormat(DATE_FORMAT);
					String DATE_FORMAT2 = "MM";
					def sdf2 = new SimpleDateFormat(DATE_FORMAT2);
					if(item.invoiceHeader.invoiceDate){
					row.createCell(15).setCellValue(sdf1.format(item.invoiceHeader.invoiceDate))
					row.createCell(16).setCellValue(sdf2.format(item.invoiceHeader.invoiceDate))
					}
					else{
					row.createCell(15).setCellValue(null)
					row.createCell(16).setCellValue(null)
					}

				}
								
			}			
			def os = response.outputStream
			wb.write(os)
			os.close()
			
		}
		else{
			response.setContentType("application/excel");
			response.setHeader("Content-disposition", "attachment;filename=Follow Up Report_Empty.xls")
			HSSFWorkbook wb = new HSSFWorkbook()
			def os = response.outputStream
			wb.write(os)
			os.close()
		}
		
		
	}
	
	//Kurt Edited  导出KB report
	def exportKBReport={
		
		 def inYear =  params['inYear']
		 def inMonth =  params['inMonth']
		 def store =  params['store']
		 if(store=='All')
		 store=null
		 if(inYear=='this year')
		 inYear=null
		 if(inMonth=='this month')
		 inMonth=null
		 //System.err.println("inYear="+inYear);
		// System.err.println("store="+store);
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

		 def invoiceitems=InvoiceItem.withCriteria{
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
		
		
		 //System.err.println("size=========="+invoiceitems.size())
		//Date searchBegindate=new Date();
		//Date searchEnddate=new Date();
		if(invoiceitems){
			response.setContentType("application/excel");
			
			HSSFWorkbook wb = new HSSFWorkbook()
			def helper = wb.getCreationHelper()
			
			//def invoiceIdList = invoiceIds.split(',')
			def invoiceItem =invoiceitems[0];
			if(invoiceItem){
				Date refdate=invoiceItem.invoiceHeader.invoiceDate;
				/*
				searchBegindate.setYear(refdate.getYear())
				searchBegindate.setMonth(refdate.getMonth());
				
				searchEnddate.setYear(refdate.getYear())
				searchEnddate.setMonth(refdate.getMonth()+1);

				searchBegindate.setDate(new Integer(1));
				searchBegindate.setHours(new Integer(0));
				searchBegindate.setMinutes (new Integer(0));
				searchBegindate.setSeconds (new Integer(0));
				
				
				searchEnddate.setDate(new Integer(0));
				searchEnddate.setHours(new Integer(23));
				searchEnddate.setMinutes (new Integer(59));
				searchEnddate.setSeconds (new Integer(59));
				System.err.println("searchBegindate:"+searchBegindate);
				System.err.println("searchEnddate:"+searchEnddate);*/
				response.setHeader("Content-disposition", "attachment;filename=Monthly Birkin,Kelly Report "+(refdate.getYear()+1900)+".xls")
				
				
				
				}
			String DATE_FORMAT = "MM";
			def sdf = new SimpleDateFormat(DATE_FORMAT);
			
			HSSFSheet sheet = wb.createSheet(sdf.format(searchBegindate))
			sheet.setColumnWidth(0, (short) (2750))		//20
			sheet.setColumnWidth(1, (short) (3750));   	//1
			sheet.setColumnWidth(2, (short) (4750));	    //2
			sheet.setColumnWidth(3, (short) (4750));	//4
			sheet.setColumnWidth(4, (short) (7750));
			sheet.setColumnWidth(5, (short) (4750));	    //2
			sheet.setColumnWidth(6, (short) (4750));	//4
			sheet.setColumnWidth(7, (short) (4750));
			sheet.setColumnWidth(8, (short) (4750));	//4
			sheet.setColumnWidth(9, (short) (7750));
			sheet.setColumnWidth(10, (short) (4750));
			sheet.setColumnWidth(11, (short) (4750));
			def row = null
			def cell = null
			def rowi = 0
			row = sheet.createRow(rowi)
			row.createCell(0).setCellValue("Store")
			row.createCell(1).setCellValue("Month")
			row.createCell(2).setCellValue("Birkin")
			row.createCell(7).setCellValue("Kelly")
			rowi=rowi+1;
			row = sheet.createRow(rowi)
			row.createCell(2).setCellValue("Invoice")
			row.createCell(3).setCellValue("Reference")
			row.createCell(4).setCellValue("Description")
			row.createCell(5).setCellValue("Qty")
			row.createCell(6).setCellValue("CO&CITIS")
			row.createCell(7).setCellValue("Invoice")
			row.createCell(8).setCellValue("Reference")
			row.createCell(9).setCellValue("Description")
			row.createCell(10).setCellValue("Qty")
			row.createCell(11).setCellValue("CO&CITIS")
			sheet.addMergedRegion(new Region((short)0,(short)0,(short)1,(short)0))
			sheet.addMergedRegion(new Region((short)0,(short)1,(short)1,(short)1))
			sheet.addMergedRegion(new Region((short)0,(short)2,(short)0,(short)6))
			sheet.addMergedRegion(new Region((short)0,(short)7,(short)0,(short)11))
			rowi=rowi+1;
			//def index=rowi;
			def Bindex=rowi;
			def Kindex=rowi;
			def oldStore;
			//System.err.println("invoiceitems.each")
			invoiceitems.each{
				def item =it;
				if(item){
				if(oldStore!=item.invoiceHeader.business.customer.reInvoicePreCode){
					oldStore=item.invoiceHeader.business.customer.reInvoicePreCode;
					Bindex=rowi;
					Kindex=rowi;
				}
				//System.err.println("123h")
				//def magitem=getMagnItemInfo(item.product.styleCode)
				String name_en=item.product.name;
				def Bag_style
				if(name_en.toLowerCase().contains("kelly")){
					Bag_style='KELLY'
				}
				else
					Bag_style='BIRKIN'
				//System.err.println("22h")

				if(Bag_style=='BIRKIN'&&Bindex==rowi||Bag_style=='KELLY'&&Kindex==rowi){
					row = sheet.createRow(rowi);
					rowi=rowi+1;
					row.createCell(0).setCellValue(item.invoiceHeader.business.customer.reInvoicePreCode);
				
				String DATE_FORMAT2 = "MM";
				def sdf2 = new SimpleDateFormat(DATE_FORMAT2);
				if(item.invoiceHeader.invoiceDate)
				row.createCell(1).setCellValue(sdf2.format(item.invoiceHeader.invoiceDate))
				}
				 
				if(Bag_style=='BIRKIN')
				{
				row=sheet.getRow (Bindex)
				row.createCell(2).setCellValue(item.invoiceHeader.code)
				//row.createCell(3).setCellValue(item.product.styleCode)
				row.createCell(3).setCellValue(item.product.skuCode)
				row.createCell(4).setCellValue(item.product.name)
				row.createCell(5).setCellValue(item.quantity)
				if(item.invoiceHeader.business.cocitis&&item.invoiceHeader.business.cocitis==true)
				row.createCell(6).setCellValue(item.invoiceHeader.business.cocitis)
				Bindex=Bindex+1;
				}
				else{
					row=sheet.getRow (Kindex)
					row.createCell(7).setCellValue(item.invoiceHeader.code)
					//row.createCell(8).setCellValue(item.product.styleCode)
					row.createCell(8).setCellValue(item.product.skuCode)
					row.createCell(9).setCellValue(item.product.name)
					
					row.createCell(10).setCellValue(item.quantity)
					//System.err.println("item.invoiceHeader.business.cocitis="+item.invoiceHeader.business.cocitis)
					if(item.invoiceHeader.business.cocitis&&item.invoiceHeader.business.cocitis==true)
					row.createCell(11).setCellValue(item.invoiceHeader.business.cocitis)
					Kindex=Kindex+1;
				}
				
				}
					
					
			}
			
			
			def os = response.outputStream
			wb.write(os)
			os.close()
			
		}
		
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
	
	
	
	//Kurt Edited 导出ImportReport
	def exportImportReport={
		def Hawb =  params['hawb']
		def store =  params['store']
		if(store==null){
			def res=getStoreList()
			store=res[0]
		}
		if(store&&store=='All')
		store=null;
		
		def cocitis=params.cocitis
		if(cocitis&&cocitis=="0")
		cocitis=null
		//System.err.println(cocitis);
		def invoiceheaders=InvoiceHeader.withCriteria{

				  and{
				
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
			}
				   order('receiveDate','asc')
		   }
		
		

		if(invoiceheaders){
			response.setContentType("application/excel");
			response.setHeader("Content-disposition", "attachment;filename=Import_Report.xls")
			HSSFWorkbook wb = new HSSFWorkbook()
			def helper = wb.getCreationHelper()
			HSSFCellStyle setBorder = wb.createCellStyle();
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER)
			//def invoiceIdList = invoiceIds.split(',')
			def invoicehdr =invoiceheaders[0];
			cocitis=false
			if(invoicehdr.business.cocitis)
			{
				cocitis=true;
			}
			if(cocitis){
				HSSFSheet sheet = wb.createSheet("cites&co")
				sheet.setColumnWidth(0, (short) (2750));	//20
				sheet.setColumnWidth(1, (short) (3750));   	//1
				sheet.setColumnWidth(2, (short) (4750));	    //2
				sheet.setColumnWidth(3, (short) (3750));	//4
				sheet.setColumnWidth(4, (short) (2750));	//4
				sheet.setColumnWidth(5, (short) (2750));    //4
				sheet.setColumnWidth(6, (short) (3750));		//2
				sheet.setColumnWidth(7, (short) (3750));	//12
				sheet.setColumnWidth(8, (short) (3750));	//12
				sheet.setColumnWidth(9, (short) (3750));	//12
				sheet.setColumnWidth(10, (short) (3750));	//12
				sheet.setColumnWidth(11, (short) (3750));	//1
				sheet.setColumnWidth(12, (short) (3750));	//1
				sheet.setColumnWidth(13, (short) (3750));	//2
				sheet.setColumnWidth(14, (short) (3750));	//30
				sheet.setColumnWidth(15, (short) (3750));	//12
				sheet.setColumnWidth(16, (short) (3750));	//1
				sheet.setColumnWidth(17, (short) (3750));	//1
				sheet.setColumnWidth(18, (short) (3750));	//2
				sheet.setColumnWidth(19, (short) (3750));	//30
				sheet.setColumnWidth(20, (short) (3750));	//12
				sheet.setColumnWidth(21, (short) (3750));	//1
				sheet.setColumnWidth(22, (short) (3750));	//1
				sheet.setColumnWidth(23, (short) (3750));	//2
				sheet.setColumnWidth(24, (short) (3750));	//30
				sheet.setColumnWidth(25, (short) (3750));	//12
				sheet.setColumnWidth(26, (short) (3750));	//1
				sheet.setColumnWidth(27, (short) (3750));	//1
				sheet.setColumnWidth(28, (short) (3750));	//2
				sheet.setColumnWidth(29, (short) (3750));	//30
				sheet.setColumnWidth(30, (short) (3750));	//12
				sheet.setColumnWidth(31, (short) (3750));	//1
				sheet.setColumnWidth(32, (short) (3750));	//1
				sheet.setColumnWidth(33, (short) (3750));	//2
				sheet.setColumnWidth(34, (short) (3750));	//30
				sheet.setColumnWidth(35, (short) (3750));	//12
				sheet.setColumnWidth(36, (short) (3750));	//1
				sheet.setColumnWidth(37, (short) (3750));	//1
				def row = null
				def cell = null
				def rowi = 0
				row = sheet.createRow(rowi)
				
				row.createCell(0).setCellValue("Import clearance report(cites&co)")
				rowi=1
				row = sheet.createRow(rowi)
				row.createCell(0).setCellValue("Item")
				row.createCell(1).setCellValue("HAWB No.")
				//Kurt added again add co citis remark
				row.createCell(2).setCellValue("Remark")
				//END
				row.createCell(3).setCellValue("Store/Re-invoice number")
				row.createCell(4).setCellValue("Invoice No.")
				row.createCell(5).setCellValue("QTY")
				row.createCell(6).setCellValue("Numbers of Carton")
				row.createCell(7).setCellValue("Issue Original Invoice Date")
				row.createCell(8).setCellValue("H China received Invoice Date")
				row.createCell(9).setCellValue("Duration of invoicing issue to rcving")
				row.createCell(10).setCellValue("Date of rcving export CITES&CO from Paris")
				row.createCell(11).setCellValue("Baolong start to apply for import CITES&CO")
				row.createCell(12).setCellValue("Date of sending import CITES&CO to Paris")
				row.createCell(13).setCellValue("Duration of CITES&CO collection")
				row.createCell(14).setCellValue("Departure from Paris")
				row.createCell(15).setCellValue("Arrival in Shanghai")
				row.createCell(16).setCellValue("Duration A of Invoicing to ATA")
				row.createCell(17).setCellValue("Baolong  received re-invoice & export-CITES&CO date")
				row.createCell(18).setCellValue("Q & A duration")
				row.createCell(19).setCellValue("Date of D/O Exchange")
				row.createCell(20).setCellValue("")
				row.createCell(21).setCellValue("Duty paid")
				row.createCell(22).setCellValue("Inspection")
				row.createCell(23).setCellValue("VAT & duty amount")
				row.createCell(24).setCellValue("Finish Custom Clearance date")
				row.createCell(25).setCellValue("")
				row.createCell(26).setCellValue("Duration of shipment arrival to custom clearance")
				row.createCell(27).setCellValue("Direct to store")
				row.createCell(28).setCellValue("Warehouse in date")
				row.createCell(29).setCellValue("Rcvd numers of cartons")
				row.createCell(30).setCellValue("Damaged carton No.")
				row.createCell(31).setCellValue("shortage pcs")
				row.createCell(32).setCellValue("Rcv the D/O instruction from store")
				row.createCell(33).setCellValue("Required delivery date by store")
				row.createCell(34).setCellValue("Date of delivery to store")
				row.createCell(35).setCellValue("Duration of Clearance to delivery")
				row.createCell(36).setCellValue("Duration B of shipment arrival to delivery")
				row.createCell(37).setCellValue("Total Days")		
				rowi=rowi+1
				def index=1;
				def labelHawb='#'
				def labelHawb_begin=2
				def labelHawb_end=2
				
				invoiceheaders.each{
						def inhdr =it;
					if(inhdr){
						row = sheet.createRow(rowi)
						row.createCell(0).setCellValue(index)
						index=index+1
						
						if(!labelHawb.equals(inhdr.business.hawb)){
							labelHawb_end=rowi
							
						row.createCell(1).setCellValue(inhdr.business.hawb)
						labelHawb=inhdr.business.hawb
						
						labelHawb_begin=rowi
						labelHawb_end=rowi
						}
						else{
							labelHawb_end=rowi
							if(labelHawb_end>labelHawb_begin)
							sheet.addMergedRegion(new Region((short)labelHawb_begin,(short)1,(short)labelHawb_end,(short)1))
						}
						
						row.createCell(2).setCellValue(inhdr.business.cocitisRemark)
						row.createCell(3).setCellValue(inhdr.reInvoice)
						row.createCell(4).setCellValue(inhdr.code)
						row.createCell(5).setCellValue(getInvoiceNoQuantity(inhdr.code))
						row.createCell(6).setCellValue(getInvoiceNoCartonQuantity(inhdr.code))
						def sdf = new SimpleDateFormat("yyyy-MM-dd");
						 
						if(inhdr.invoiceDate){
							 row.createCell(7).setCellValue(sdf.format(inhdr.invoiceDate));
						}
						else
						row.createCell(7).setCellValue('');
						if(inhdr.receiveDate)
						row.createCell(8).setCellValue(sdf.format(inhdr.receiveDate));
						else
						row.createCell(8).setCellValue('');
						if(inhdr.invoiceDate&&inhdr.receiveDate){
							row.createCell(9).setCellValue(inhdr.receiveDate-inhdr.invoiceDate+1)
						}
						else
						row.createCell(9).setCellValue('');
						
						if(inhdr.coCitisExportDate)
						row.createCell(10).setCellValue(sdf.format(inhdr.coCitisExportDate));
						else
						row.createCell(10).setCellValue('');
						
						if(inhdr.coCitisImportDate)
						row.createCell(11).setCellValue(sdf.format(inhdr.coCitisImportDate));
						else
						row.createCell(11).setCellValue('');
						
						if(inhdr.coCitisSendingImportDate)
						row.createCell(12).setCellValue(sdf.format(inhdr.coCitisSendingImportDate));
						else
						row.createCell(12).setCellValue('');
						
						if(inhdr.coCitisExportDate&&inhdr.coCitisImportDate){
							row.createCell(13).setCellValue(inhdr.coCitisImportDate-inhdr.coCitisExportDate+1)
						}
						else
						row.createCell(13).setCellValue('');
						
						if(inhdr.fromPariseDate)
						row.createCell(14).setCellValue(sdf.format(inhdr.fromPariseDate));
						else
						row.createCell(14).setCellValue('');
						
						if(inhdr.arriveDate)
						row.createCell(15).setCellValue(sdf.format(inhdr.arriveDate));
						else
						row.createCell(15).setCellValue('');
						
						if(inhdr.invoiceDate&&inhdr.arriveDate){
							row.createCell(16).setCellValue(inhdr.arriveDate-inhdr.invoiceDate+1)
						}
						else
						row.createCell(16).setCellValue('');
						
						if(inhdr.baoLongReceiveDate)
						row.createCell(17).setCellValue(sdf.format(inhdr.baoLongReceiveDate));
						else
						row.createCell(17).setCellValue('');
						
						row.createCell(18).setCellValue(inhdr.qADuration)
						if(inhdr.dOExchangeDate)
						row.createCell(19).setCellValue(sdf.format(inhdr.dOExchangeDate))
						else
						row.createCell(19).setCellValue('');
						
						
						row.createCell(20).setCellValue('')
						
						if(inhdr.dutyPaidDate)
						row.createCell(21).setCellValue(sdf.format(inhdr.dutyPaidDate))
						else
						row.createCell(21).setCellValue('');
						
						if(inhdr.inspectionDate)
						row.createCell(22).setCellValue(sdf.format(inhdr.inspectionDate))
						else
						row.createCell(22).setCellValue('');
						
						
						row.createCell(23).setCellValue(inhdr.vatDutyAmount)
											
						
						if(inhdr.clearanceDate)
						row.createCell(24).setCellValue(sdf.format(inhdr.clearanceDate));
						else
						row.createCell(24).setCellValue('');
						
						row.createCell(25).setCellValue('')
						
						if(inhdr.arriveDate&&inhdr.clearanceDate){
							row.createCell(26).setCellValue(inhdr.clearanceDate-inhdr.arriveDate+1)
						}
						else
						row.createCell(26).setCellValue('');
						
						row.createCell(27).setCellValue(inhdr.directToStore)
						
						if(inhdr.warehouseInDate)
						row.createCell(28).setCellValue(sdf.format(inhdr.warehouseInDate))
						else
						row.createCell(28).setCellValue('');
						
						row.createCell(29).setCellValue(inhdr.rcvdNumersOfCartons)
						row.createCell(30).setCellValue(inhdr.damagedCartonNo)
						row.createCell(31).setCellValue(inhdr.shortagePcs)
						
						if(inhdr.rcvDOInstructionFromStoreDate)
						row.createCell(32).setCellValue(inhdr.rcvDOInstructionFromStoreDate)
						else
						row.createCell(32).setCellValue('');
						
						if(inhdr.requiredDeliveryByStoreDate)
						row.createCell(33).setCellValue(sdf.format(inhdr.requiredDeliveryByStoreDate))
						else
						row.createCell(33).setCellValue('');
						
						if(inhdr.deliveryDate)
						row.createCell(34).setCellValue(sdf.format(inhdr.deliveryDate));
						else
						row.createCell(34).setCellValue('');
						
						if(inhdr.clearanceDate&&inhdr.deliveryDate){
							row.createCell(35).setCellValue(inhdr.deliveryDate-inhdr.clearanceDate+1)
						}
						else
						row.createCell(35).setCellValue('');
						
						if(inhdr.clearanceDate&&inhdr.deliveryDate&&inhdr.arriveDate&&inhdr.clearanceDate){
							def i1=inhdr.deliveryDate-inhdr.clearanceDate
							def i2=inhdr.clearanceDate-inhdr.arriveDate
							row.createCell(36).setCellValue(i1+i2+2)
						}
						else
						row.createCell(36).setCellValue('');
						
						if(inhdr.clearanceDate&&inhdr.deliveryDate&&inhdr.arriveDate&&inhdr.invoiceDate){
							def i1=inhdr.deliveryDate-inhdr.clearanceDate+1
							def i2=inhdr.clearanceDate-inhdr.arriveDate+1
							def i3=inhdr.arriveDate-inhdr.invoiceDate+1
							row.createCell(37).setCellValue(i1+i2+i3)
						}
						else
						row.createCell(37).setCellValue('');
						
						rowi=rowi+1
					}
					
				}

			}
			else{//若不是cocitis
				HSSFSheet sheet = wb.createSheet("Import report")
				sheet.setColumnWidth(0, (short) (2750))		//20
				sheet.setColumnWidth(1, (short) (3750));   	//1
				sheet.setColumnWidth(2, (short) (4750));	    //2
				sheet.setColumnWidth(3, (short) (3750));	//4
				sheet.setColumnWidth(4, (short) (2750));	//4
				sheet.setColumnWidth(5, (short) (2750));    //4
				sheet.setColumnWidth(6, (short) (3750));		//2
				sheet.setColumnWidth(7, (short) (3750));	//12
				sheet.setColumnWidth(8, (short) (3750));	//12
				sheet.setColumnWidth(9, (short) (3750));	//12
				sheet.setColumnWidth(10, (short) (3750));	//12
				sheet.setColumnWidth(11, (short) (3750));	//1
				sheet.setColumnWidth(12, (short) (3750));	//1
				sheet.setColumnWidth(13, (short) (3750));	//2
				sheet.setColumnWidth(14, (short) (3750));	//30
				sheet.setColumnWidth(15, (short) (3750));	//12
				sheet.setColumnWidth(16, (short) (3750));	//1
				sheet.setColumnWidth(17, (short) (3750));	//1
				sheet.setColumnWidth(18, (short) (3750));	//2
				sheet.setColumnWidth(19, (short) (3750));	//30
				sheet.setColumnWidth(20, (short) (3750));	//12
				sheet.setColumnWidth(21, (short) (3750));	//1
				sheet.setColumnWidth(22, (short) (3750));	//1
				sheet.setColumnWidth(23, (short) (3750));	//2
				sheet.setColumnWidth(24, (short) (3750));	//30
				sheet.setColumnWidth(25, (short) (3750));	//12
				sheet.setColumnWidth(26, (short) (3750));	//1
				sheet.setColumnWidth(27, (short) (3750));	//1
				sheet.setColumnWidth(28, (short) (3750));	//2
				sheet.setColumnWidth(29, (short) (3750));	//30
				sheet.setColumnWidth(30, (short) (3750));	//12
				sheet.setColumnWidth(31, (short) (3750));	//1
				sheet.setColumnWidth(32, (short) (3750));	//1
				sheet.setColumnWidth(33, (short) (3750));	//2
				sheet.setColumnWidth(34, (short) (3750));	//30
				sheet.setColumnWidth(35, (short) (3750));	//12

				def row = null
				def cell = null
				def rowi = 0
				row = sheet.createRow(rowi)
				row.createCell(0).setCellValue("Import clearance report")
				rowi=1
				row = sheet.createRow(rowi)
				row.createCell(0).setCellValue("Item")
				row.createCell(1).setCellValue("HAWB No.")
				row.createCell(2).setCellValue("Store/Re-invoice number")
				row.createCell(3).setCellValue("Invoice No.")
				row.createCell(4).setCellValue("QTY")
				row.createCell(5).setCellValue("Numbers of Carton")
				row.createCell(6).setCellValue("Issue Original Invoice Date")
				row.createCell(7).setCellValue("H China received Invoice Date")
				row.createCell(8).setCellValue("Duration of invoicing issue to rcving")
				row.createCell(9).setCellValue("Departure from Paris")
				row.createCell(10).setCellValue("Arrival in Shanghai")
				row.createCell(11).setCellValue("Duration A of Invoicing to ATA")
				row.createCell(12).setCellValue("Baolong  received re-invoice date")
				row.createCell(13).setCellValue("For gold-Baolong apply for the gold license date")
				row.createCell(14).setCellValue("Q & A duration")
				row.createCell(15).setCellValue("Date of D/O Exchange")
				row.createCell(16).setCellValue("")
				row.createCell(17).setCellValue("Duty paid")
				row.createCell(18).setCellValue("Inspection")
				row.createCell(19).setCellValue("VAT & duty amount")
				row.createCell(20).setCellValue("Finish Custom Clearance date")
				row.createCell(21).setCellValue("")
				row.createCell(22).setCellValue("Duration of shipment arrival to custom clearance")
				row.createCell(23).setCellValue("Direct to store")
				row.createCell(24).setCellValue("Warehouse in date")
				row.createCell(25).setCellValue("Rcvd numers of cartons")
				row.createCell(26).setCellValue("Damaged carton No.")
				row.createCell(27).setCellValue("shortage pcs")
				row.createCell(28).setCellValue("Rcv the D/O instruction from store")
				row.createCell(29).setCellValue("Required delivery date by store")
				row.createCell(30).setCellValue("Date of delivery to store")
				row.createCell(31).setCellValue('')
				row.createCell(32).setCellValue('')
				row.createCell(33).setCellValue("Duration of Clearance to delivery")
				row.createCell(34).setCellValue("Duration B of shipment arrival to delivery")
				row.createCell(35).setCellValue("Total Days")
				
				rowi=rowi+1
				def index=1;
				def labelHawb='#'
				def labelHawb_begin=2
				def labelHawb_end=2
				invoiceheaders.each{
						def inhdr =it;
						//System.err.println("it="+it);
					if(inhdr){
						row = sheet.createRow(rowi)
						row.createCell(0).setCellValue(index)
						index=index+1
						
						if(!labelHawb.equals(inhdr.business.hawb)){
							labelHawb_end=rowi
							
						row.createCell(1).setCellValue(inhdr.business.hawb)
						labelHawb=inhdr.business.hawb
						
						labelHawb_begin=rowi
						labelHawb_end=rowi
						}
						else{
							labelHawb_end=rowi
							if(labelHawb_end>labelHawb_begin)
							sheet.addMergedRegion(new Region((short)labelHawb_begin,(short)1,(short)labelHawb_end,(short)1))
						}
						
						
						
						row.createCell(2).setCellValue(inhdr.reInvoice)
						row.createCell(3).setCellValue(inhdr.code)
						row.createCell(4).setCellValue(getInvoiceNoQuantity(inhdr.code))
						row.createCell(5).setCellValue(getInvoiceNoCartonQuantity(inhdr.code))
						def sdf = new SimpleDateFormat("yyyy-MM-dd");
						 
						if(inhdr.invoiceDate){
							 row.createCell(6).setCellValue(sdf.format(inhdr.invoiceDate));
						}
						else
						row.createCell(6).setCellValue('');
						if(inhdr.receiveDate)
						row.createCell(7).setCellValue(sdf.format(inhdr.receiveDate));
						else
						row.createCell(7).setCellValue('');
						if(inhdr.invoiceDate&&inhdr.receiveDate){
							row.createCell(8).setCellValue(inhdr.receiveDate-inhdr.invoiceDate+1)
						}
						else
						row.createCell(8).setCellValue('');
						
						if(inhdr.fromPariseDate)
						row.createCell(9).setCellValue(sdf.format(inhdr.fromPariseDate));
						else
						row.createCell(9).setCellValue('');
						
						if(inhdr.arriveDate)
						row.createCell(10).setCellValue(sdf.format(inhdr.arriveDate));
						else
						row.createCell(10).setCellValue('');
						
						if(inhdr.invoiceDate&&inhdr.arriveDate){
							row.createCell(11).setCellValue(inhdr.arriveDate-inhdr.invoiceDate+1)
						}
						else
						row.createCell(11).setCellValue('');
						
						if(inhdr.baoLongReceiveDate)
						row.createCell(12).setCellValue(sdf.format(inhdr.baoLongReceiveDate));
						else
						row.createCell(12).setCellValue('');
						
						row.createCell(13).setCellValue('')
						row.createCell(14).setCellValue(inhdr.qADuration)
						
						if(inhdr.dOExchangeDate)
						row.createCell(15).setCellValue(sdf.format(inhdr.dOExchangeDate))
						else
						row.createCell(15).setCellValue('');
						
						
						row.createCell(16).setCellValue('')
						
						if(inhdr.dutyPaidDate)
						row.createCell(17).setCellValue(sdf.format(inhdr.dutyPaidDate))
						else
						row.createCell(17).setCellValue('');
						
						if(inhdr.inspectionDate)
						row.createCell(18).setCellValue(sdf.format(inhdr.inspectionDate))
						else
						row.createCell(18).setCellValue('');
						
						row.createCell(19).setCellValue(inhdr.vatDutyAmount)

						if(inhdr.clearanceDate)
						row.createCell(20).setCellValue(sdf.format(inhdr.clearanceDate));
						else
						row.createCell(20).setCellValue('');
						
						row.createCell(21).setCellValue('')
						
						if(inhdr.arriveDate&&inhdr.clearanceDate){
							row.createCell(22).setCellValue(inhdr.clearanceDate-inhdr.arriveDate+1)
						}
						else
						row.createCell(22).setCellValue('');
						
						row.createCell(23).setCellValue(inhdr.directToStore)
						
						if(inhdr.warehouseInDate)
						row.createCell(24).setCellValue(sdf.format(inhdr.warehouseInDate))
						else
						row.createCell(24).setCellValue('');
						
						
						row.createCell(25).setCellValue(inhdr.rcvdNumersOfCartons)
						row.createCell(26).setCellValue(inhdr.damagedCartonNo)
						row.createCell(27).setCellValue(inhdr.shortagePcs)
						
						if(inhdr.rcvDOInstructionFromStoreDate)
						row.createCell(28).setCellValue(sdf.format(inhdr.rcvDOInstructionFromStoreDate))
						else
						row.createCell(28).setCellValue('');
						
						if(inhdr.requiredDeliveryByStoreDate)
						row.createCell(29).setCellValue(sdf.format(inhdr.requiredDeliveryByStoreDate))
						else
						row.createCell(29).setCellValue('');
						
						if(inhdr.deliveryDate)
						row.createCell(30).setCellValue(sdf.format(inhdr.deliveryDate));
						else
						row.createCell(30).setCellValue('');
						
						row.createCell(31).setCellValue('')
						row.createCell(32).setCellValue('')
						
						if(inhdr.clearanceDate&&inhdr.deliveryDate){
							row.createCell(33).setCellValue(inhdr.deliveryDate-inhdr.clearanceDate+1)
						}
						else
						row.createCell(33).setCellValue('');
						
						if(inhdr.clearanceDate&&inhdr.deliveryDate&&inhdr.arriveDate&&inhdr.clearanceDate){
							def i1=inhdr.deliveryDate-inhdr.clearanceDate
							def i2=inhdr.clearanceDate-inhdr.arriveDate
							row.createCell(34).setCellValue(i1+i2+2)
						}
						else
						row.createCell(34).setCellValue('');
						
						if(inhdr.clearanceDate&&inhdr.deliveryDate&&inhdr.arriveDate&&inhdr.invoiceDate){
							def i1=inhdr.deliveryDate-inhdr.clearanceDate+1
							def i2=inhdr.clearanceDate-inhdr.arriveDate+1
							def i3=inhdr.arriveDate-inhdr.invoiceDate+1
							row.createCell(35).setCellValue(i1+i2+i3)
						}
						else
						row.createCell(35).setCellValue('');
						
						rowi=rowi+1
				
					}
					
				}
				
				
			
				
			}
			//xie
			def os = response.outputStream
			wb.write(os)
			os.close()
		}
		
		
		
		
	}
	def getInvoiceNoQuantity(invoiceNo){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = "select floor(sum(quantity)) from hermes_invoiceitem  it inner join hermes_invoiceheader hdr on it.invoice_header_id=hdr.id where hdr.code= '"+invoiceNo+"' "
		def TotalQuantity = sql.rows(query,[]);
		 if(!TotalQuantity)
		 TotalQuantity[0][0]=0
		return TotalQuantity[0][0]
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
	
	def updateReinvoiceSequence(name,currentvalue,id){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = " UPDATE  [sequence] SET  [current_value]=? where [name]=? and [id]=? and [current_value] < ? ";

		def results = sql.executeUpdate(query,[currentvalue,name,id,currentvalue]);
		
		return results
	}
	
	
	//Kurt Edited
	def updateStoreSequence={
		log.info params
				 def id=params['seqid']
				 def seq_name =  params['name']
				 def seq_current_value=params['currentValue']
				 //System.err.println("id"+id);
				// System.err.println("seq_name"+seq_name);
				 //System.err.println("seq_current_value"+seq_current_value);
				 if(!updateReinvoiceSequence(seq_name,seq_current_value,id))
				 	System.err.println("updateReinvoiceSequence error");
				 render("")
	}
	
	
	
	
	//Kurt Edited
	def listStoreSequence ={
		 log.info params
		 //def Hawb =  params['hawb']
		 // def cocitis=params.cocitis
		 def inYear=params['inYear']
		  //System.err.println("params.max="+params.max);
		  //System.err.println("params.page="+params.page);
		 def criteriaClosure = {
			 if(inYear)
			 eq("inYear",inYear)
			   
			 }
		 
		 def instanceList =jqgridFilterService.jqgridAdvFilter(params,Sequence,criteriaClosure,false);
		 def pager = jqgridFilterService.jqgridAdvFilter(params,Sequence,criteriaClosure,true);
		 JqgridJSON jqgridJSON = new JqgridJSON(pager);
		 //System.err.println("params.max="+params.max);
		 //System.err.println("params.page="+params.page);
		  def i=0
		  instanceList.each{elem->
			  def cell = new ArrayList()
			 // cell.add(i+1)
			  
			  cell.add(elem.id)
			  cell.add(elem.name)
			  cell.add(elem.name)
			  cell.add(elem.inYear)
			  cell.add(elem.currentValue)
			  cell.add(elem.increment)
			  
			  i=i+1
				jqgridJSON.addRow(i, cell)
		  }
		  //render("")
		  render jqgridJSON.json as JSON
	}
	
	
	
	

	def generateReInvoiceCode ={
		render(view: "/hermes/interface/generateReInvoiceCode")
	}
	
	//Kurt edited
	def getStoreReinvoiceSequence(){
		
		def sql = new Sql(dataSource);
		 
	 
		def query = " SELECT [name], [current_value], [increment] FROM  [sequence] "
		def results = sql.rows(query,[]);
		  
		return results
	}
	
	
	
	
	def exportPO={
			def items=params.items
			String orderdate=params.orderdate
			 
			if(orderdate){
				orderdate=orderdate.replace('-','');
			}
			def sCode=''
			if(items){
				response.setContentType("application/excel"); 
				response.setHeader("Content-disposition", "attachment;filename=SHS_IMPO.xls")  
				HSSFWorkbook wb = new HSSFWorkbook() 
				def helper = wb.getCreationHelper()
				HSSFSheet sheet = wb.createSheet("SHEET")
					
				sheet.setColumnWidth(0, (short) (2750))
				sheet.setColumnWidth(1, (short) (450));
				sheet.setColumnWidth(2, (short) (2225));
				sheet.setColumnWidth(3, (short) (2750));
				sheet.setColumnWidth(4, (short) (1725));
				sheet.setColumnWidth(5, (short) (1725));
				sheet.setColumnWidth(6, (short) (5320));
				sheet.setColumnWidth(7, (short) (3250));
				sheet.setColumnWidth(8, (short) (700));
				sheet.setColumnWidth(9, (short) (3250));
				sheet.setColumnWidth(10, (short) (4025));
				
				def row = null  
				def cell = null  
				def rowi = -1
				rowi=rowi+1
				row = sheet.createRow(rowi)  
				row.createCell(0).setCellValue("Purch order no")
				row.createCell(1).setCellValue("Order type")
				row.createCell(2).setCellValue("Order date")
				row.createCell(3).setCellValue("Supplier code buyer")
				row.createCell(4).setCellValue("WH ID")
				row.createCell(5).setCellValue("Location")
				row.createCell(6).setCellValue("Stock code")
				row.createCell(7).setCellValue("Qty order")
				row.createCell(8).setCellValue("Curr code")
				row.createCell(9).setCellValue("Unit price")
				row.createCell(10).setCellValue("Supplier sales order no.")
				
				def pos = items.split(',')
				pos.each{
					def po=Business.get(it)
					if(po){
						def poc = po.poCode
						def so = po.soCode
						def ot = 1
						String DATE_FORMAT = "yyyyMMdd";
					    def sdf = new SimpleDateFormat(DATE_FORMAT);
						def od=''				
						
						log.info po.deliveryDate
						if(po.deliveryDate){
							od = sdf.format(po.deliveryDate)
						}
						//Kurt Edited
						def supplier = po.supplier
						def wh = po.customer.whId
						def loc = po.customer.locId
						def cc = 0
						//Kurt Edited
						if(supplier && (supplier.code=='1800' || supplier.code == '1200')){
							def bis = BusinessItem.findAllByBusiness(po)
							bis.each{bi->
								if(items.count(bi.id.toString())<=0){
									rowi = rowi+1
									row = sheet.createRow(rowi)
									if(poc&&poc!='')
									row.createCell(0).setCellValue("0"+poc)
									else
									row.createCell(0).setCellValue(poc)
									row.createCell(1).setCellValue(ot)
									if(orderdate){
										row.createCell(2).setCellValue(orderdate)
									}
									else{
									row.createCell(2).setCellValue(od)
									}
									row.createCell(3).setCellValue(supplier.code)
									row.createCell(4).setCellValue(wh)
									row.createCell(5).setCellValue(loc)
									if(bi.product){
										row.createCell(6).setCellValue(bi.product.code)
									}else{
										row.createCell(6).setCellValue(bi.code)
									}
									//log.info it.quantity[0]
									row.createCell(7).setCellValue(bi.quantity)
									row.createCell(8).setCellValue(cc)
									def df = new DecimalFormat("#.00")
									row.createCell(9).setCellValue(df.format(bi.amount))
									row.createCell(10).setCellValue(so)
								}
							}
						}else{
							po.invoiceHeaders.each{ih->
								ih.invoiceItems.each{elem->
									if((items.count(elem.id.toString())<=0)&&(elem.magitudem.trim().toUpperCase()!="NONE")&&(elem.product.dept.toUpperCase()!="I")&&(elem.product.dept.toUpperCase()!="Y")&&(elem.product.dept.toUpperCase()!="X")&&(elem.product.dept.toUpperCase()!="K")){
										//System.err.println("elem.product.dept="+elem.product.dept)
										rowi = rowi+1
										row = sheet.createRow(rowi)
										if(elem.poCode&&elem.poCode!='')
										row.createCell(0).setCellValue("0"+elem.poCode)
										else
										row.createCell(0).setCellValue(elem.poCode)
										
										row.createCell(1).setCellValue(ot)
										//System.err.println("orderdate="+orderdate)
										if(orderdate){
											row.createCell(2).setCellValue(orderdate)
										}
										else{
											row.createCell(2).setCellValue(od)
										}
										if(supplier)
										row.createCell(3).setCellValue(supplier.code)
										else
										row.createCell(3).setCellValue('')
										row.createCell(4).setCellValue(wh)
										row.createCell(5).setCellValue(loc)
										row.createCell(6).setCellValue(elem.product.code)
										row.createCell(7).setCellValue(elem.quantity)
										row.createCell(8).setCellValue(cc)
										def shipment = Shipment.find("from Shipment s join s.business b where b=?",[elem.invoiceHeader.business])
										if(shipment){
											def df = new DecimalFormat("#.00")
											row.createCell(9).setCellValue(df.format(elem.getAdjShipmentUPS(shipment[0].rate)))
										}
										row.createCell(10).setCellValue(so)
									}
								}
							}
						}
				
						po.toScala='Y'
						po.save(flush:true)
					}
				}
				def os = response.outputStream
				wb.write(os)  
		        os.close()				
			}
		}		
}