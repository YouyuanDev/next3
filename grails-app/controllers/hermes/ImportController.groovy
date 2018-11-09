package hermes

import next.*
import org.apache.poi.hssf.usermodel.*
import rt.*;
class ImportController {
	//def uploadService
	def importService
	/*
	def uploadInvoice = {
		
		def userFile = params['userfile']
		
		if (userFile) {
			//importService.cleanInterfaceBpInvoice()
			//Kurt Edited
			
			//XlsProcessor xlsPr=XlsProcessor.getInstance();
			
			//processBPInvoice(userFile.getInputStream())

			//importService.loadHermesData() 
		}
		//render(text:"upload log message here")
		render(view: "uploadInvoice")
	}*/
	def processBPInvoice(def is) {
		/*
		*	已知问题
		*	1)ean13(被当作字符串读取)
		*	2)数字类型都带有.0
		*	3)价格字段对应存在问题
		*	4)必须读取xls文件.
		*/
		HSSFWorkbook wb = new HSSFWorkbook(is);
		//Kurt Edited 这里貌似没用， 在 interfacecontroller里
		FileOutputStream  fileout= new FileOutputStream("D:/333.xls");
		wb.write(fileout);
		fileout.close();
		//END
		HSSFSheet sheet = wb.getSheetAt(0)
		def hSSFDataFormatter = new HSSFDataFormatter()
		def cell
		int rowNum = sheet.getLastRowNum()
		log.info rowNum
		for (int i = 1; i <= rowNum; i++) {
			def row = sheet.getRow(i)
			def customerDevIndex, customerShortCode, customerLongCode, customerName, customerCateCode, customerCateName, materialCode, materialName, familyCode, familyName, productCode, productName, modelCode, modelName, styleCode, skuCode, skuName, supplyChainModel, ean13, colorCode, colorName, sizeCode, sizeName, podium, bpCode, orderType, orderCode, invoiceDate, productPrice, retailPriceEUR, retailDiscountPrice, shipQty, shipGrossPrice, shipDiscountPrice
			bpCode = hSSFDataFormatter.formatCellValue(row.getCell(26))
			if (!bpCode) continue
			customerShortCode = row.getCell(0)
			customerDevIndex = hSSFDataFormatter.formatCellValue(row.getCell(1))
			customerLongCode = hSSFDataFormatter.formatCellValue(row.getCell(2))
			customerName = hSSFDataFormatter.formatCellValue(row.getCell(3))
			customerCateCode = hSSFDataFormatter.formatCellValue(row.getCell(4))
			customerCateName = hSSFDataFormatter.formatCellValue(row.getCell(5))
			materialCode = hSSFDataFormatter.formatCellValue(row.getCell(6))
			materialName = hSSFDataFormatter.formatCellValue(row.getCell(7))
			familyCode = hSSFDataFormatter.formatCellValue(row.getCell(8))
			familyName = hSSFDataFormatter.formatCellValue(row.getCell(9))
			productCode = hSSFDataFormatter.formatCellValue(row.getCell(10))
			productName = hSSFDataFormatter.formatCellValue(row.getCell(11))
			modelCode = hSSFDataFormatter.formatCellValue(row.getCell(12))
			modelName = hSSFDataFormatter.formatCellValue(row.getCell(13))
			styleCode = hSSFDataFormatter.formatCellValue(row.getCell(14))
			styleCode = styleCode.toString().trim()
			skuCode = hSSFDataFormatter.formatCellValue(row.getCell(15))
			skuCode = skuCode.toString().trim()
			supplyChainModel = hSSFDataFormatter.formatCellValue(row.getCell(6))
			ean13 = hSSFDataFormatter.formatCellValue(row.getCell(17))
			skuName = hSSFDataFormatter.formatCellValue(row.getCell(18))
			colorCode = hSSFDataFormatter.formatCellValue(row.getCell(19))
			colorName = hSSFDataFormatter.formatCellValue(row.getCell(20))
			sizeCode = row.getCell(21) ? hSSFDataFormatter.formatCellValue(row.getCell(21)) : ''
			sizeName = row.getCell(22) ? hSSFDataFormatter.formatCellValue(row.getCell(22)) : ''
			podium = hSSFDataFormatter.formatCellValue(row.getCell(23))
			orderType = hSSFDataFormatter.formatCellValue(row.getCell(24))
			orderCode = hSSFDataFormatter.formatCellValue(row.getCell(25))
			invoiceDate = row.getCell(27)
			productPrice = hSSFDataFormatter.formatCellValue(row.getCell(28))
			retailPriceEUR = hSSFDataFormatter.formatCellValue(row.getCell(29))
			retailDiscountPrice = hSSFDataFormatter.formatCellValue(row.getCell(30))
			shipQty = hSSFDataFormatter.formatCellValue(row.getCell(32))
			shipGrossPrice = hSSFDataFormatter.formatCellValue(row.getCell(33))
			shipDiscountPrice = hSSFDataFormatter.formatCellValue(row.getCell(34))
			/*
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
			*/
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
			if (!bpi.save(flush: true)) {
				bpi.errors.each {
					log.info it
				}
			}
		}
	}

	def uploadBaoLongShipment={
		log.info paras
		def userFile = params['userfile']
		if (userFile) {
			/*
			importService.cleanInterfaceBpInvoice()
			processBPInvoice(userFile.getInputStream())
			importService.loadHermesData() 
			*/
			/* 
			 *1. update the baolong data base id
			 *2. if reupload load can be update the data
			 *3. if don't find item by id , todo: the return message for user
			 *4. others
			 */
			processBLShipment(userFile.getInputStream())
		}
		//render(view: "uploadInvoice")
	}
	def procesBLShipment(def is){
		HSSFWorkbook wb = new HSSFWorkbook(is);
		HSSFSheet sheet = wb.getSheetAt(0)
		def hSSFDataFormatter = new HSSFDataFormatter()
		def cell
		int rowNum = sheet.getLastRowNum()
		log.info rowNum
		for(int i=1;i<=rowNum;i++){
			def row = sheet.getRow(i)
			def id
			id = hSSFDataFormatter.formatCellValue(row.getCell(0))
			if (!id){
				/* update the baolong data to item */
				/*关税 yunza daili*/
				def gs = hSSFDataFormatter.formatCellValue(row.getCell(0))
				def yz = hSSFDataFormatter.formatCellValue(row.getCell(0))
				def dl = hSSFDataFormatter.formatCellValue(row.getCell(0))
				
				log.info "process the item:${id} is success"
			}else{
				continue	
			} 
			
		}
		
	}
}