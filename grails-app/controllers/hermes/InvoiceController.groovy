package hermes

import java.util.List;

import jxl.write.Label;



import next.*
import grails.converters.*
import groovy.sql.Sql

import org.apache.poi.hssf.usermodel.*
import org.apache.poi.hssf.util.Region

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import rt.*;
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.core.io.ClassPathResource

import com.sun.org.apache.xpath.internal.operations.Lt;

class InvoiceController extends BaseController {
	def dataSource;
	def logisticService
	def invoiceService
	def jqgridFilterService

	/* invoice section */
	def listBpInvoices = {
		render(view: "/hermes/logistic/bpInvoicesList")
	}
	def listBpInvoicesJson = {
		
		//temp
		/*  for(int i=6380;i<=6390;i++){
		def business = Business.get(i)
		if(business){
			System.err.println("delete BP id="+business.id)
			def msg = logisticService.deleteBusiness(business)
		}
		 }
		 */
		//
			
		
		
	    def instanceList = searchBusinessInvoiceBP(params,false)
	    log.info instanceList.size()
	    def pager = searchBusinessInvoiceBP(params,true)
	    JqgridJSON jqgridJSON = new JqgridJSON(pager)
		String DATE_FORMAT = "yyyy-MM-dd";
		def sdf = new SimpleDateFormat(DATE_FORMAT);
	    instanceList.eachWithIndex { elem,i->
			def cell = new ArrayList()
			cell.add(elem.id)
			
			cell.add(elem.business.code)
			cell.add(elem.code)
			cell.add(elem.reInvoice)
			
		   
			cell.add(sdf.format(elem.invoiceDate))
/*			cell.add(elem.invoiceHeader.business.customer.customerShortCode)
			cell.add(elem.invoiceHeader.business.customer.customerCateName)
			cell.add(elem.product.dept)
			cell.add(elem.product.category.description)
			cell.add(elem.product.category.code)
			cell.add(elem.product.category.name)
			cell.add(elem.product.name)
			cell.add(elem.quantity)
			cell.add(elem.amount)
			cell.add(elem.logisticIssuance)
			cell.add(elem.logisticFreight)
			cell.add(elem.getLogisticTotal())*/
			jqgridJSON.addRow(i, cell)
			cell=null;
	    }
	    render jqgridJSON.json as JSON
	}
	//Kurt Updated
	def searchBusinessInvoiceBP(params,doCount){
		log.info params
		def bpCode =  params['bpCode']
		def invoiceCode =  params['invoiceCode']
		def reInCode =  params['reInCode']
		def emptyHawb =  params['emptyHawb'] //Kurt added
		def store=params['store']
		def hawb =  params['hawb']  //Kurt added
		
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
				System.err.println(searchBegindate);
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
		//def sortfield = params['sort']
		//def order = params['order']
   		def sidx=params.sidx
		def sord=params.sord		                   
		
		//if (!sortfield) {
		//	sortfield="id"
			//order="asc"s
		//}
		def criteriaClosure = {
			  and{
                 business{
               
			   //Kurt updated
			   and{
					 if(bpCode) like ('code','%'+bpCode+'%')
			         or{
					 if(emptyHawb) isNull ('hawb')
					 if(emptyHawb) eq ('hawb','')
					 }
					 if(!emptyHawb)
					 like ('hawb','%'+hawb+'%')
					 customer{
						 if(store)
						 eq('reInvoicePreCode',store)
					 }
			    }
			     
			   
               }
				 between('invoiceDate',searchBegindate,searchEnddate)
                if(invoiceCode) like 'code','%'+invoiceCode+'%'
                if(reInCode) like 'reInvoice','%'+reInCode+'%'
				//if(store)like('reInvoice','%'+store+'%')
			  }
			
			if(sidx)
			{
				if(sord)
				{
					if (sidx=='invoice.code') {
						order('code',sord)
					}
					if (sidx=='invoice.reInvoice') {
						order('reInvoice',sord)
					}					
					if (sidx=='invoice.invoiceDate') {
						order('invoiceDate',sord)
					}	
					if (sidx=='business.code') {
						business{
							order('code',sord)
						}
					}						
				}
            }else{
            	order("code","desc")
            }
            
          }
		return jqgridFilterService.jqgridAdvFilter(params,InvoiceHeader,criteriaClosure,doCount)
	}
	
	def searchBusinessInvoice(params,doCount){
		log.info params
		def bpCode =  params['bpCode']
		def invoiceCode =  params['invoiceCode']
		def reInCode =  params['reInCode']
		def categoryCode = params['categoryCode']
		def productCode =  params['productCode']
		def productDept =  params['productDept']
		def productMaterial =  params['productMaterial']
		                             
		def searchSort = params.searchSort
		params.sort="id"
		def criteriaClosure = {
	        and{
	          invoiceHeader{
	            business{
	              if(bpCode) like 'code','%'+bpCode+'%'
	            }
	            if(invoiceCode) like 'code','%'+invoiceCode+'%'
	            if(reInCode) like 'reInvoice','%'+reInCode+'%'
	          }
	          product{
	            category{
	              if(categoryCode) like 'code', '%'+categoryCode+'%'
	            }
	            if(productCode)  like 'code', '%'+productCode+'%'
				if(searchSort && searchSort.equals("product")) order("code","asc")
	            if(productDept)  like 'dept', '%'+productDept+'%'
	            if(productMaterial)  like 'material', '%'+productMaterial+'%'
				if(searchSort && searchSort.equals("dept")) order("dept","asc")
				//if(searchSort && searchSort.equals("material")) order("material","asc")
	          }
	        }
	      }
		return jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,doCount)
	}
	
	def showBP = {
		log.info params
		//System.err.println("aabc")
		def instance = Business.findByCode(params.id)
		//System.err.println("ddc")
		if (!instance) {
		  flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'Business.label', default: 'Business'), params.id])}"
		  redirect(action: "list")
		}
		else {
			def reInvoiceNumbers = logisticService.getReInvoice(instance)  
			render(view: "/hermes/logistic/bpInvoicesShow", model: [instance: instance,invoices:instance.invoiceHeaders,reInvoiceNumbers:reInvoiceNumbers,notnull:params.notnull])
		}
	}	
	def listBPInvoiceItemsJson ={
		
		String str_id = params.id
		if(str_id)
		str_id=str_id.replace(',','');
		
		def id=str_id
		log.info params
		def sortitem=params.sidx //Kurt Edited
		def sord = params.sord  //Kurt Edited
		//System.err.println("1111")
		
		def business = Business.get(id)
		//System.err.println("business:"+business.id)
		def adjItem = logisticService.getAdjustInvoiceItem(id)
		//System.err.println("adjItem:"+business.id)
		def criteriaClosure = {
			invoiceHeader{
	            eq("business",business)
	        }
			//default from user requirement (product.dept )
			product{
				if(sortitem&&sortitem=='product.dept')
				order("dept",sord)    //Edited by Kurt
				
				category{
					if(sortitem&&sortitem=='product.desczh')
					order("description",sord)
				}
			}
			
		}
		def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,false);
		def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,true);
		JqgridJSON jqgridJSON = new JqgridJSON(pager)
		def i=0
		//System.err.println("2222")
		def df = new DecimalFormat("#.00")
		def df2 = new DecimalFormat("#.0000")
		instanceList.each{elem->  
			def cell = new ArrayList()
			cell.add(elem.id)
			//cell.add(elem.product.category.code)
			System.err.println("elem.invoiceHeader.code:"+elem.invoiceHeader.code)
			cell.add(elem.invoiceHeader.code)
			System.err.println("elem.product.id:"+elem.product.id)
			
			//System.err.println("elem.product.styleCode:"+elem.product.styleCode)
			if( elem.product.metaClass.hasProperty(elem.product,'styleCode'))
				cell.add(elem.product.styleCode)
			else
				cell.add('')
			//System.err.println("elem.product.code:"+elem.product.code)
			if( elem.product.metaClass.hasProperty(elem.product,'code'))
				cell.add(elem.product.code)
			else
				cell.add('')
			
			//System.err.println("elem.product.colorName:"+elem.product.colorName)
			if( elem.product.metaClass.hasProperty(elem.product,'colorName'))
				cell.add(elem.product.colorName)
			else
				cell.add('')
				
			if( elem.product.metaClass.hasProperty(elem.product,'sizeName'))
				cell.add(elem.product.sizeName)
			else
				cell.add('')
			if( elem.product.metaClass.hasProperty(elem.product,'familyName'))
				cell.add(elem.product.familyName)
			else
				cell.add('')
			
			//System.err.println("elem.product.productCode:"+elem.product.productCode)
			if( elem.product.metaClass.hasProperty(elem.product,'productCode'))
				cell.add(elem.product.productCode)
			else
				cell.add('')
			if( elem.product.metaClass.hasProperty(elem.product,'dept'))
				cell.add(elem.product.dept)
			else
				cell.add('')
			//System.err.println("elem.product.category.description:"+elem.product.category.description)
			if( elem.product.metaClass.hasProperty(elem.product,'category')){
				cell.add(elem.product.category.description)
				//增加材质2
				cell.add(elem.product.category.description2)
			}
			else{
				cell.add('')
				cell.add('')
			}
			
			//System.err.println("elem.product.category.description2:"+elem.product.category.description2)
			
			cell.add(elem.country)
			if (elem.productName && elem.productName.length() > 0) {
				cell.add('No')
				//System.err.println("elem.productName:"+elem.productName)
				cell.add(elem.productName)
			} else {
				cell.add('Yes')
				//System.err.println("elem.product.category.name:"+elem.product.category.name)
				if( elem.product.metaClass.hasProperty(elem.product,'category'))
					cell.add(elem.product.category.name)
				else 
					cell.add('')
				
			}
			//cell.add(elem.product.productName)
			if( elem.product.metaClass.hasProperty(elem.product,'name'))
				cell.add(elem.product.name)
			else
				cell.add('')
	      	cell.add(elem.quantity)
			
			if( adjItem && elem.id == adjItem.id ){
				
				def adjAmount = new BigDecimal(elem.getLogisticAmount())+new BigDecimal(business.totalAdjDiff)/elem.quantity
				//cell.add("<div style='background-color:red'>"+df.format(adjAmount*elem.quantity)+"</div>")
				cell.add("<div style='background-color:red'>"+df2.format(adjAmount)+"</div>")
				cell.add(df.format(elem.logisticIssuance/elem.quantity+elem.logisticFreight/elem.quantity))
				
			   cell.add(elem.getLogisticAmountTotal()+new BigDecimal(business.totalAdjDiff))
			}else{
				if ((elem.getLogisticAmount()==0) || (elem.getLogisticAmount()==null)){
					cell.add("<div style='background-color:yellow'>0</div>")
				}else{
					//cell.add(df.format(elem.getLogisticAmount()*elem.quantity))
					cell.add(df.format(elem.getLogisticAmount()))
				}
				cell.add(df.format(elem.logisticIssuance/elem.quantity+elem.logisticFreight/elem.quantity))
				cell.add(elem.getLogisticAmountTotal())
			}
			
			
			
			
			i=i+1
      		jqgridJSON.addRow(i, cell)
			  cell=null;
		}
		//System.err.println("5555")
		render jqgridJSON.json as JSON
		
	}
	
	//Kurt Edited delete BP
	
	def deleteBPInvoicesJson={
		log.info params
		def businessId = params['id']
		//System.err.println("businessId="+businessId)
		def business = Business.get(businessId)
		if(business){
			def msg = logisticService.deleteBusiness(business)
		}
		redirect(action: "listBpInvoices", params: params)
	}
	
	
	def deleteOLDBP={
		/* try{
				 

		   for(int i=0;i<=15689;i++){
		def business = Business.get(i)
		if(business){
			if(business.inYear==13)
			continue;
			System.err.println("delete BP id="+business.id)
			def msg = logisticService.delteBusinessForcely(business)
		}
		 }
		 }
		 catch(Exception e)
		 {}*/
	}
	
	
	def listBPInvoicesJson={
		String str_id = params.id
		//System.err.println("params.id="+str_id)
		if(str_id)
		str_id=str_id.replace(',','');
		//System.err.println("str_id="+str_id)
		def id=str_id
		def business = Business.get(id)
		def criteriaClosure = {
            if(id) eq 'business', business
			order("code","asc")
			order("recode","asc")
		}
		def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceHeader,criteriaClosure,false);
		def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceHeader,criteriaClosure,true);
		JqgridJSON jqgridJSON = new JqgridJSON(pager)
		def i=0
		instanceList.each{elem->
			def cell = new ArrayList()
			cell.add(elem.id)
			//Kurt updated
			
			cell.add(elem.invoiceType)
			cell.add(elem.code)
			
		    String DATE_FORMAT = "yyyy-MM-dd";
		    def sdf = new SimpleDateFormat(DATE_FORMAT);
			cell.add(sdf.format(elem.invoiceDate))
			if(elem.importDate){
				cell.add(sdf.format(elem.importDate))
			}else{
				cell.add('')
			}
			if(elem.exportDate){
				cell.add(sdf.format(elem.exportDate))
			}else{
				cell.add('')
			}
			
			cell.add(elem.business.customer.reInvoicePreCode)
			cell.add(elem.recode)
			i=i+1
      		jqgridJSON.addRow(i, cell)
		}
		render jqgridJSON.json as JSON		
	}
	
	//kurt Edited
	def exportInvoicePackingPerHawb={
		System.err.println("exportInvoicePackingPerHawb  began")
		log.info  params
		def bpCode =  params['bpCode']
		def invoiceCode =  params['invoiceCode']
		def reInCode =  params['reInCode']
		def emptyHawb =  params['emptyHawb'] //Kurt added
		def store=params['store']
		def hawb =  params['hawb']  //Kurt added
		def inYear=params['inYear']
		def inMonth=params['inMonth']
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
		
		
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=Invoice_Packinglist_Hawb_"+hawb+".xls")
		//render(contextType:"application/vnd.ms-excel")
		HSSFWorkbook wb = new HSSFWorkbook()
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		def helper = wb.getCreationHelper()
		def sheet = wb.createSheet("Invoice")
		def header = sheet.getHeader();
		def centertext=HSSFHeader.font("Arail", "regular")+ HSSFHeader.fontSize((short) 9)+ properties.getProperty("headerText")
		
		header.setCenter(centertext)
		sheet.setMargin(HSSFSheet.TopMargin,1.6)
		def row = null
		def cell = null
		
		row = sheet.createRow(1)
		row.createCell(0).setCellValue("To")
		row.createCell(1).setCellValue("爱马仕(上海)商贸有限公司")
		//row.createCell(1).setCellValue( properties.getProperty("companyNameCN") )
		row.createCell(7).setCellValue("發票 INVOICE NO:")
		row.createCell(8).setCellValue('')
		row = sheet.createRow(2)
		//row.createCell(1).setCellValue("HERMES (CHINA ) CO., LTD")
		row.createCell(1).setCellValue(properties.getProperty("companyNameEN") )
		row = sheet.createRow(3)
		//row.createCell(1).setCellValue("3010FLOOR, 1038 NAN JING ROAD (E),")
		row.createCell(1).setCellValue(properties.getProperty("companyAddress1"))
		row.createCell(7).setCellValue("日期 Date:")
		String DATE_FORMAT = "yyyy-MM-dd"
		def sdf = new SimpleDateFormat(DATE_FORMAT)
		row.createCell(8).setCellValue(sdf.format(new Date()))
		row = sheet.createRow(4)
		//row.createCell(1).setCellValue("SHANGHAI 200041, P.R.C.")
		row.createCell(1).setCellValue(properties.getProperty("companyAddress2"))
		//Kurt Edited
		row = sheet.createRow(5)
		cell = row.createCell(8)
		cell.setCellValue("CIP")
		cell.setCellStyle(cs)
		cell = row.createCell(9)
		cell.setCellValue("CIP")
		cell.setCellStyle(cs)
		
		row = sheet.createRow(6)
		cell = row.createCell(0)
		cell.setCellValue("分类")
		cell.setCellStyle(cs)
		
		cell = row.createCell(1)
		cell.setCellValue("BP Code")
		cell.setCellStyle(cs)
		
		cell = row.createCell(2)
		cell.setCellValue("INV.")
		cell.setCellStyle(cs)
		cell = row.createCell(3)
		cell.setCellValue("编号")
		cell.setCellStyle(cs)
		cell = row.createCell(4)
		cell.setCellValue("中文品名")
		cell.setCellStyle(cs)
		
		cell = row.createCell(5)
		cell.setCellValue("英文品名材质")
		cell.setCellStyle(cs)
		
		cell = row.createCell(6)
		cell.setCellValue("原产国")
		cell.setCellStyle(cs)
		cell = row.createCell(7)
		cell.setCellValue("数量")
		cell.setCellStyle(cs)
		cell = row.createCell(8)
		cell.setCellValue("单价")
		cell.setCellStyle(cs)
		cell = row.createCell(9)
		cell.setCellValue("总价")
		cell.setCellStyle(cs)
		
		cell = row.createCell(10)
		cell.setCellValue("DEPT")
		cell.setCellStyle(cs)
		cell = row.createCell(11)
		cell.setCellValue("PODIUM")
		cell.setCellStyle(cs)
		
		
		def results = InvoiceItem.withCriteria{
			order("productName","asc")
			
			invoiceHeader{
				and{
					business{
				  
				  //Kurt updated
				  and{
						if(bpCode) like ('code','%'+bpCode+'%')
						if(emptyHawb=='1')
						{
							or{
								isNull ('hawb')
								eq ('hawb','')
							}
						}
						else
						like ('hawb','%'+hawb+'%')
						customer{
							if(store)
							eq('reInvoicePreCode',store)
						}
				   }
					
				  
				  }
					between('invoiceDate',searchBegindate,searchEnddate)
				   if(invoiceCode) like 'code','%'+invoiceCode+'%'
				   if(reInCode) like 'reInvoice','%'+reInCode+'%'
				   //if(store)like('reInvoice','%'+store+'%')
				 }
			}
			product{
				order("dept","asc")
				//order("material","asc")
				//order("name","asc")
				category{
					order("name","asc")
				}
			}
		}
	
		def rowi = 6
		def group = '#'
		def groupIndex = 1
		def groupQty = 0
		def groupCIF = 0
		def groupQtyTotal = 0
		def groupCIFTotal = 0
		def first=true
		def currency='EUR'
		
		List<InvoiceItemProperty> list = new ArrayList<InvoiceItemProperty>();
		results.each{
		InvoiceItemProperty initem=new InvoiceItemProperty()
		if(it.productName&&it.productName.length()>0){ //不使用master
			initem.setProductName(it.productName)
			initem.setContent(it.productName +' '+it.product.name + ' '+it.product.category.description)
	   }
	   else{
		   initem.setProductName(it.product.category.name)
		   initem.setContent(it.product.category.name +' '+it.product.name +' '+ it.product.category.description)
		   
	   }
		   //initem.setSortOrderCode(initem.getProductName()+it.product.category.description)
		initem.setSortOrderCode(initem.getProductName()+it.country)  //按照品名和原产国排序
		initem.setProductEnglishName (it.product.name)
		initem.setProductCategoryDescription (it.product.category.description)
		initem.setInvoiceHeaderCode(it.invoiceHeader.code)
		initem.setProductCode(it.product.code)
		initem.setCountry(it.country)
		initem.setQuantity(it.quantity)
		initem.setBpCode(it.invoiceHeader.business.code)
		initem.setDept(it.product.dept)
		initem.setPodium(it.podium)
		
		currency=it.invoiceHeader.business.currency
		/*
		if( adjItem && it.id == adjItem.id ){
			 
		initem.setAmount(it.amount+(it.logisticIssuance+it.logisticFreight+new BigDecimal(business.totalAdjDiff))/it.quantity)
			
		}
		else
		initem.setAmount(it.amount+(it.logisticIssuance+it.logisticFreight)/it.quantity)
		*/
		//logs_adj_amount 为调完的价格
		initem.setAmount(it.logsAdjAmount+(it.logisticIssuance+it.logisticFreight)/it.quantity)
		
		list.add(initem);
		//it
		}
		Collections.sort(list);
		
		list.each{
			//Kurt Edited  现根据中文品名进行分类
			//def groupName =  it.product.category.name + it.product.name
			def groupName
			def content
			 
				 groupName =  it.productName
				 content= it.content
			 
		 
			
			if(!groupName.equals(group)){
				if(!group.equals('#')){
					rowi = rowi+1
					row = sheet.createRow(rowi)
					cell=row.createCell(7)
					cell.setCellValue(groupQty)
					cell.setCellStyle(cs)
					
					cell=row.createCell(9)
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
					cell.setCellValue(it.bpCode)
					cell.setCellStyle(cs)
					
					
					cell=row.createCell(2)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.invoiceHeaderCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					cell.setCellValue(it.productCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.productName)
					//cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.productEnglishName+' '+it.productCategoryDescription)
					//cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					
					
					cell=row.createCell(6)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(8)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(9)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
				
					cell=row.createCell(10)
					cell.setCellValue(it.dept)
					cell.setCellStyle(cs)
					
					cell=row.createCell(11)
					cell.setCellValue(it.podium)
					cell.setCellStyle(cs)
				
				groupQty = groupQty+it.quantity
				groupCIF = groupCIF+it.amount*it.quantity
				group=groupName
				//log.info "write:"+it.productCode
			}else{
				rowi = rowi+1
				//System.err.println("rowi="+rowi)
					row = sheet.createRow(rowi)
					cell=row.createCell(0)
					cell.setCellValue(groupIndex)
					cell.setCellStyle(cs)
					//Kurt Edited
					
					cell=row.createCell(1)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.bpCode)
					cell.setCellStyle(cs)
					
					
					cell=row.createCell(2)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.invoiceHeaderCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					cell.setCellValue(it.productCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.productName)
					//cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.productEnglishName+' '+it.productCategoryDescription)
					//cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(8)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(9)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
					cell=row.createCell(10)
					cell.setCellValue(it.dept)
					cell.setCellStyle(cs)
					
					cell=row.createCell(11)
					cell.setCellValue(it.podium)
					cell.setCellStyle(cs)
					groupQty = groupQty+it.quantity
					groupCIF = groupCIF+it.amount*it.quantity
					// log.info "write same:"+it.productCode
			}
		}
		rowi = rowi+1
			row = sheet.createRow(rowi)
			cell=row.createCell(7)
			cell.setCellValue(groupQty)
			cell.setCellStyle(cs)

			cell=row.createCell(9)
			cell.setCellValue(groupCIF)
			cell.setCellStyle(cs)
			groupQtyTotal = groupQtyTotal + groupQty
			groupCIFTotal = groupCIFTotal + groupCIF
			rowi = rowi+1
			row = sheet.createRow(rowi)
			rowi = rowi+1
			row = sheet.createRow(rowi)

			cell=row.createCell(6)
			cell.setCellValue('TOTAL')
			cell.setCellStyle(cs)

			cell=row.createCell(7)
			cell.setCellValue(groupQtyTotal)
			cell.setCellStyle(cs)

			cell=row.createCell(8)
			//cell.setCellValue('EUR')
			if(currency=='RMB')
			currency='CNY'
			cell.setCellValue(currency)
			
			
			cell.setCellStyle(cs)

			cell=row.createCell(9)
			cell.setCellValue(groupCIFTotal)
			cell.setCellStyle(cs)
		
		 
			sheet = wb.createSheet("Packing")
			 header = sheet.getHeader();
			// centertext=HSSFHeader.font("Arail", "regular")+ HSSFHeader.fontSize((short) 9)+ properties.getProperty("headerText")
			
			header.setCenter(centertext)
			sheet.setMargin(HSSFSheet.TopMargin,1.6)
			 row = null
		 cell = null

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
		row.createCell(1).setCellValue("爱马仕(上海)商贸有限公司")
		row.createCell(5).setCellValue("發票 INVOICE NO:")
		row.createCell(6).setCellValue('')
		row = sheet.createRow(++i)
		//row.createCell(1).setCellValue("HERMES (CHINA ) CO., LTD")
		row.createCell(1).setCellValue(properties.getProperty("companyNameEN") )
		row = sheet.createRow(++i)
		//row.createCell(1).setCellValue("3010FLOOR, 1038 NAN JING ROAD (E),")
		row.createCell(1).setCellValue(properties.getProperty("companyAddress1"))
		row.createCell(5).setCellValue("日期 Date:")
		 DATE_FORMAT = "yyyy-MM-dd"
		 sdf = new SimpleDateFormat(DATE_FORMAT)
		row.createCell(6).setCellValue('')

		row = sheet.createRow(++i)
		//row.createCell(1).setCellValue("SHANGHAI 200041, P.R.C.")
		row.createCell(1).setCellValue(properties.getProperty("companyAddress2"))
		row = sheet.createRow(++i)
			//System.err.println("emptyHawb="+emptyHawb)
			 def packingResults = Packing.withCriteria{

				 
					and{
						business{
					  
					  //Kurt updated
					  and{
							if(bpCode) like ('code','%'+bpCode+'%')
							if(emptyHawb=='1')
							{
								//System.err.println("emptyHawb==1")
								or{
								 isNull ('hawb')
								 eq ('hawb','')
								}
							}
							else if(emptyHawb=='0'){
							like ('hawb','%'+hawb+'%')
							//System.err.println("emptyHawb==0")
							}
							customer{
								if(store)
								eq('reInvoicePreCode',store)
							}
					   }
					 
					  invoiceHeaders{
						  between('invoiceDate',searchBegindate,searchEnddate)
					  }
						  if(invoiceCode) like 'code','%'+invoiceCode+'%'
						  if(reInCode) like 'reInvoice','%'+reInCode+'%'
					  
					  
					  }
						
					   //if(store)like('reInvoice','%'+store+'%')
					 }
				 
				 
			}
			 //System.err.println("packing list size="+packingResults.size())
			 
			 
			 Map hm = new HashMap();
			 PackingListExportParameter plep=new PackingListExportParameter();
			 def totalQuantityInAll=0
			 def netWeightInAll=0
			 def grossWeightInAll=0
			 packingResults.each{
				
				if(!hm.containsKey(it.business.code))
				{
				row = sheet.createRow(++i)
				hm.put(it.business.code, it.business.code)
				//System.err.println("packing bp Code="+it.business.code)
				
				plep=writePackingSheet(wb,sheet,row,it,i);
				i=plep.i+2;
				totalQuantityInAll=totalQuantityInAll+plep.quantity
				netWeightInAll=netWeightInAll+plep.netWeight
				grossWeightInAll=grossWeightInAll+plep.grossWeight
				
				}
				else
				{
				
				
				}
				 
				 
			 }
			 //System.err.println("hm size="+hm.size());
			
			 row = sheet.createRow(++i)
			 cell=row.createCell(0)
			 cell.setCellValue("")
			 cell.setCellStyle(cs)
	 
			 cell=row.createCell(1)
			 cell.setCellValue("Total Quantity")
			 cell.setCellStyle(cs)
			 
			 cell=row.createCell(2)
			 cell.setCellValue("Total Gross Weight (Kgs)")
			 cell.setCellStyle(cs)
			 
			 cell=row.createCell(3)
			 cell.setCellValue("Total Net Weight (Kgs)")
			 cell.setCellStyle(cs)
			 
			 row = sheet.createRow(++i)
			 cell=row.createCell(0)
			 cell.setCellValue("TOTAL")
			 cell.setCellStyle(cs)
			 def df = new DecimalFormat("#.0000")
			 cell=row.createCell(1)
			 cell.setCellValue(totalQuantityInAll)
			 cell.setCellStyle(cs)
			 
			 cell=row.createCell(2)
			 cell.setCellValue(df.format(grossWeightInAll)+'Kgs')
			 cell.setCellStyle(cs)
			 
			 cell=row.createCell(3)
			 cell.setCellValue(df.format(netWeightInAll)+'Kgs')
			 cell.setCellStyle(cs)
			 
			 
			def os = response.outputStream
			wb.write(os)
		    os.close()
				
				
	}
	
	def writePackingSheet(wb,sheet,row,packing,i){
		//def reInvoiceNumbers = logisticService.getReInvoice(packing.business)
		def reInvoiceNumbers=packing.business.reInvoice
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		def totalQuantity = 0
		def totalNetWeight=0
		def totalGrossWeight=0
		def cell = null
		//packing cartons信息 (按照carton顺序排列)
		def cartons = Carton.findAllByPacking(packing,[sort:"code",order:"asc"])
		row = sheet.createRow(++i)
		cell=row.createCell(0)
		cell.setCellValue("BP Code:")
		cell.setCellStyle(cs)
					
		cell=row.createCell(1)
		cell.setCellValue(packing.business.code)
		cell.setCellStyle(cs)
		
		cartons.each{
			//carton头信息
			//System.err.println("line 975 rowi="+i);
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
			totalNetWeight=totalNetWeight+it.nWeight
			totalGrossWeight=totalGrossWeight+it.gWeight
		}
		//packing表尾统计信息
		row = sheet.createRow(++i)
		cell=row.createCell(0)
		cell.setCellValue("")
		cell.setCellStyle(cs)

		cell=row.createCell(1)
		cell.setCellValue("Quantity")
		cell.setCellStyle(cs)
		
		cell=row.createCell(2)
		cell.setCellValue("Gross Weight (Kgs)")
		cell.setCellStyle(cs)
		
		cell=row.createCell(3)
		cell.setCellValue("Net Weight (Kgs)")
		cell.setCellStyle(cs)
		
		row = sheet.createRow(++i)
		cell=row.createCell(0)
		cell.setCellValue("TOTAL")
		cell.setCellStyle(cs)

		cell=row.createCell(1)
		cell.setCellValue(totalQuantity)
		cell.setCellStyle(cs)
		
		

		
		cell=row.createCell(2)
		cell.setCellValue(totalGrossWeight+'Kgs')
		cell.setCellStyle(cs)
		
		cell=row.createCell(3)
		cell.setCellValue(totalNetWeight+'Kgs')
		cell.setCellStyle(cs)
		
		PackingListExportParameter plep=new PackingListExportParameter();
		plep.i=i;
		plep.grossWeight=totalGrossWeight;
		plep.netWeight=totalNetWeight;
		plep.quantity=totalQuantity;
		return plep;
	}
	
	//Kurt Edited
	def exportAllBPReinvoiceData={
		System.err.println("exportAllBPReinvoiceData  began")
		log.info  params
		def sql = new Sql(dataSource)
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		  
		def filename=properties.getProperty("exportTempFileFilePath")
		filename=filename+"bplist"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".xls"
		def procedurename="next_devdb.dbo.search_bp_reinvoice_relationship"
		//System.err.println("111filename="+filename)
		def key = " exportData "+"'"+filename+"'"+",'"+procedurename+"'"
		
		//System.err.println("111key="+key)
		//System.err.println("key="+key)
		def result = sql.rows("exec "+key)
		//System.err.println("111result="+result)
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename=bp_Reinvoce.xls")
		
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
	
	
	
	
	def updateBusiness = {
		log.info params
	    
		String str_id = params.id
		if(str_id)
		str_id=str_id.replace(',','');
		
		def id=str_id
	    def issuance = params.totalIssuance
	    def freight = params.totalFreight
		def totalAdjDiff = params.totalAdjDiff
		def hawb = params.hawb
		def currency = params.currency
		def cocitis = params.cocitis
		//Kurt edited 增加了cocitis remark
		def cocitisremark=params.cocitisRemark
		def business = Business.get(id)
		
		if (logisticService.getInvoiceAmountNotNull(business).equals("0")){
			params.notnull="0"
			logisticService.updateBusiness(id,new BigDecimal(issuance),new BigDecimal(freight),new BigDecimal(totalAdjDiff),hawb,currency,cocitis,cocitisremark)
		}else{
			params.notnull="1"
			logisticService.updateBusiness(id,new BigDecimal(issuance),new BigDecimal(freight),new BigDecimal(totalAdjDiff),hawb,currency,cocitis,cocitisremark)
		}
		params.id = business.code
		log.info params
		redirect(action: "showBP", params: params)		
	}
	def updateInvoiceHeader = {
	    def id = params['id']
	    def reInvoice = params['reinvoice']
	    def issuance = params['issuance']
	    def freight = params['freight']
			logisticService.updateInvoiceHeader(id,reInvoice,issuance,freight)
	    redirect(action: "show", params: params)
  	}
	def updateInvoiceItem = {
		log.info params
		def id = params['invoiceitem.id']
		def material = params['product.material']
		def material2=params['product.material2']
		def desczh = params['product.desczh']
		def amount = params['invoiceitem.amount']
		//System.err.println("amount="+amount);
		amount=getPureAmount(amount);//Kurt Edited
		def descen=params['product.descen'];//Kurt edited
		//System.err.println("descen="+descen);
		def ismaster = params['product.ismaster']
		def country = params['invoiceitem.country']
			logisticService.updateInvoiceItem(id,material,material2,desczh,ismaster,country,amount,descen)
		render(text: "success")
	}
	def updateHawbByItems = {
		log.info params
		def ids = params.ids
		def hawb = params.hawb
		logisticService.updateHawb(ids.tokenize(','),hawb)
		render(text: "")
	}
	def updateReceiveDateByItems = {
		log.info params
		def ids = params.ids
		def receiveDate = params.receiveDate
		logisticService.updateReceiveDate(ids,receiveDate)
		render(text: "")
	}
	def updateReinvoiceById = {
		log.info params
		def id = params.invoiceId
		def number = params.number
		def importDate = params.importDate
		def exportDate = params.exportDate
		def invoiceType = params.invoiceType
		if(logisticService.updateInvoiceItemReNumber(id,importDate,exportDate,number,invoiceType)){
			render(text:"")
		}else{
			render(text:"error")
		}
	}
	def updateAdjAmount = {
		log.info params
		logisticService.updateLogsAdjAmount(params.id,new BigDecimal(params.adjAmount))
		//redirect(action:"showBP",params:params)
		render(text:"")
	}
	
	/* packing section */
	def listPacking={
		def map = [ busiList: Business.list(),storeList:getStoreList() ]
		render(view:"/hermes/logistic/packingList",model:map)
	}
	def listPackingJson={
	    def instanceList = searchPacking(params,false)
	    def pager = searchPacking(params,true)
	    JqgridJSON jqgridJSON = new JqgridJSON(pager)
		String DATE_FORMAT = "yyyy-MM-dd"
		def sdf = new SimpleDateFormat(DATE_FORMAT)
	    instanceList.eachWithIndex { elem,i->
			def cell = new ArrayList()
			cell.add(elem.id)
			cell.add(elem.business.code)
			def reInvoiceNumbers
			if(elem.business.reInvoice)
				 reInvoiceNumbers = elem.business.reInvoice//logisticService.getReInvoice(elem.business) 
			else
				reInvoiceNumbers=logisticService.getReInvoice(elem.business) 
				
			cell.add(reInvoiceNumbers) 
		    
		    
			//cell.add(sdf.format(elem.date))
			//cell.add(elem.date)
			//log.info elem.date
			cell.add(sdf.format(elem.date))
			jqgridJSON.addRow(i, cell)
			cell=null;
	    }
		
	    render jqgridJSON.json as JSON
	}
	def searchPacking(params,doCount){
		def bpCode =  params.bpCode
		def reInvoice =  params.reInvoice
		def store=params.store
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
			//System.err.println("123");
			def res=getStoreList()
			store=res[0]
		}
		if(store&&store=='All')
		store=null;
		//System.err.println("store="+store);
		def criteriaClosure = {
			and{
				business{
					and{
						if(bpCode) 
					like("code","%"+bpCode+"%")
					if(store)
					customer{
						eq('reInvoicePreCode',store)
					}
					}
					
				}
				between('date',searchBegindate,searchEnddate)
				order("date","desc")
			}
		}		
		return jqgridFilterService.jqgridAdvFilter(params,Packing,criteriaClosure,doCount)
	}
	
	
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
	
	def getStoreList(){
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
	
	
	
	
	//upload all cartons' weight from PDF
	
	def uploadPackingCartaonWeightPDF={
		System.err.println("start uploadPackingCartaonWeightPDF")
		log.info params
		def userFile = params['userfile']
		def packingId=params['packingId']
		
		if (userFile) {
			
			//Kurt Edited  可导入 txt格式
			RegExProcessor regPr=RegExProcessor.getInstance();
			String filename=regPr.savePackingWeightFileToLocalPDF(userFile.getInputStream());
			long   start   =   System.currentTimeMillis();
			
			long   end1   =   System.currentTimeMillis();
			long   costtime   =   end1   -   start;
			//System.err.println("uploadPackingSheet time="+costtime);
			//def msg="11"
			def msg=fetchAutoPackingWeightFromPDF(filename,packingId);
			//System.err.println("finish autoPacking")
			
			long   end2   =   System.currentTimeMillis();
			long   costtime2   =   end2   -   end1;
			//System.err.println("uploadPackingSheet time="+costtime2);
			long total=costtime+costtime2;
			//System.err.println("total time="+total);
			//System.err.println("finish uploadPackingSheet")
			log.info "msg"+msg
			render(text:msg)
			
		}else{
			Calendar cur = Calendar.getInstance()
		render(view:"/hermes/logistic/packingShow",model:[])
		}
		
		
		
		
		
	}
	
	
	
	
	//upload single carton PDF
	
	def uploadPackingCartonPDF={
		
		System.err.println("start uploadPackingCartonPDF")
		log.info params
		def userFile = params['userfile']
		def packingId=params['packingId']
		
		if (userFile) {
			
			//Kurt Edited  可导入 txt格式
			RegExProcessor regPr=RegExProcessor.getInstance();
			String filename=regPr.savePackingFileToLocalPDF(userFile.getInputStream());
			long   start   =   System.currentTimeMillis();
			
			long   end1   =   System.currentTimeMillis();
			long   costtime   =   end1   -   start;
			//System.err.println("uploadPackingSheet time="+costtime);
			//def msg="11"
			def msg=autoPackingFromPDF(filename,packingId);
			//System.err.println("finish autoPacking")
			
			long   end2   =   System.currentTimeMillis();
			long   costtime2   =   end2   -   end1;
			//System.err.println("uploadPackingSheet time="+costtime2);
			long total=costtime+costtime2;
			//System.err.println("total time="+total);
			//System.err.println("finish uploadPackingSheet")
			log.info "msg"+msg
			render(text:msg)
			
		}else{
			Calendar cur = Calendar.getInstance()
		render(view:"/hermes/logistic/packingShow",model:[])
		}
		
		
		
	}
	
	
	
	
	
	def uploadPackingSheet={
		log.info params
		def userFile = params['userfile']
		def packingId=params['packingId']
		
		if (userFile) {
			
			//Kurt Edited  可导入 txt格式
			RegExProcessor regPr=RegExProcessor.getInstance();
			String filename=regPr.savePackingFileToLocal(userFile.getInputStream());
			long   start   =   System.currentTimeMillis();
			
			long   end1   =   System.currentTimeMillis();
			long   costtime   =   end1   -   start;
			//System.err.println("uploadPackingSheet time="+costtime);
			//processBPInvoice(userFile.getInputStream())
			def msg=autoPacking(filename,packingId);
			//System.err.println("finish autoPacking")
			
			long   end2   =   System.currentTimeMillis();
			long   costtime2   =   end2   -   end1;
			//System.err.println("uploadPackingSheet time="+costtime2);
			long total=costtime+costtime2;
			//System.err.println("total time="+total);
			//System.err.println("finish uploadPackingSheet")
			log.info "msg"+msg
			render(text:msg)
			
		}else{
			Calendar cur = Calendar.getInstance()
		render(view:"/hermes/logistic/packingShow",model:[])
		}
	}
	
	def isBPShipmentExist(businessId){
		def sql = new Sql(dataSource);
	   def query = " select * from hermes_shipment sh inner join  hermes_shipment_hermes_business shb on   sh.id=shb.shipment_business_id where  shb.business_id= ? ";
	   def result = sql.rows(query,[businessId]);
	   if(result.size()>0)
		return  true
		else
		return false
	}
	
	def deleteCarton(cartonId){
		def sql = new Sql(dataSource);
		def query = " delete hermes_packingcarton_item where carton_id=?  delete hermes_packing_carton where id =?";
		def result = sql.executeUpdate(query,[cartonId,cartonId]);
		
	}
	
	
	//装箱重量导入来自PDF
	
	def fetchAutoPackingWeightFromPDF(filename,packingId){
		try{
			System.err.println("fetchAutoPackingWeightFromPDF start filename="+filename)
			
			
			def packing= Packing.get(packingId)
			def business=packing.business
			int BPtotalQuantity=business.totalQuantity
			if(isBPShipmentExist(business.id)){
				//System.err.println("Has Shipment Sheet")
				flash.message = "Can not start autoPacking, shipment does already exist!"
				return flash.message.toString();
			}
			
			
			FileReader fileReader = new FileReader(filename)
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String dataline=null;
			
			
			String notExistItemsCodeList="";
			while (bufferedReader.ready()) {
				dataline = bufferedReader.readLine();//读取一行
				 //判断是否为新箱子开始
				
				if(dataline.contains("#")){
					//新箱子
					//System.err.println("dataline=========="+dataline)
					String[] data = dataline.split("#");
					
					if(data.length<3)
					break;
					String Colis="";
					String GrossWeight="";
					String NetWeight="";
					Colis=data[0];
					GrossWeight=data[1];
					NetWeight=data[2];
				
					System.err.println("carton Colis==========="+Colis)
					System.err.println("carton GW==========="+GrossWeight)
					System.err.println("carton NW==========="+NetWeight)

					def cartons =packing.cartons
					cartons.each{
						//System.err.println("carton Colis="+Colis+"it.code="+it.code)
						if(Colis.toInteger()==it.code){
							System.err.println("Save carton Colis GW NW=====it.code")
							it.gWeight=Float.parseFloat(GrossWeight);
							it.nWeight=Float.parseFloat(NetWeight);
							if (!it.save(flush: true)) {
								carton.errors.each {
								 // log.info it
								}
							   
							  }
						}
					
						
						}
						
					}
					
				
			}

			fileReader.close();
			bufferedReader.close();
			//System.err.println("notExistItemQty="+notExistItemQty)
			

			return "BP: "+business.code +" Carton Weights uploaded";
		 
			
			
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
			
		}
		
		
	}
	
	
	
	// 单个装箱单 Packing PDF
	
	def autoPackingFromPDF(filename,packingId){
		try{
			System.err.println("autoPackingFromPDF start filename="+filename)
			
			
			def packing= Packing.get(packingId)
			def business=packing.business
			int BPtotalQuantity=business.totalQuantity
			if(isBPShipmentExist(business.id)){
				//System.err.println("Has Shipment Sheet")
				flash.message = "Can not start autoPacking, shipment does already exist!"
				return flash.message.toString();
			}
			
			
			FileReader fileReader = new FileReader(filename)
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String dataline=null;
			String Colis="";
			String expedition="";
			def cartonId=null
			int packingSheetquantity=0;
			int notExistItemQty=0;
			String notExistItemsCodeList="";
			while (bufferedReader.ready()) {
				dataline = bufferedReader.readLine();//读取一行
				 //判断是否为新箱子开始
				
				if(dataline.contains("#")){
					//新箱子
					//System.err.println("dataline=========="+dataline)
					String[] data = dataline.split("#");
					expedition=data[0];
					if(data.length<2)
					break;
					Colis=data[1];
				
					//System.err.println("carton expedition==========="+expedition)
					//System.err.println("carton Colis==========="+Colis)
					//delete old packing list
					def cartons =packing.cartons
					cartons.each{
						//System.err.println("carton Colis="+Colis+"it.code="+it.code)
						if(Colis.toInteger()==it.code){
							//System.err.println("carton Colis=====it.code")
							 deleteCarton(it.id)
						}
						
					}
					
					cartonId=autoAddCarton(packingId,Colis,expedition);
				}
				else if(cartonId){
					String[] data = dataline.split(",");
					String skucode=data[0];
					String quantity=data[1];
					packingSheetquantity=packingSheetquantity+quantity.toInteger()
					
					System.out.println("skucode="+skucode);
					System.out.println("quantity="+quantity);
					System.out.println(dataline);//在屏幕上输出
					
					def results = InvoiceItem.withCriteria{
						
						invoiceHeader{
							eq("business",business)
						}
						product{
							eq("skuCode",skucode)
							
							/*category{
								order("name","asc")
							}*/
						}
						
					}
						if(results[0]){
							def invoiceItem=results[0];
							logisticService.updateCartonItem("add",cartonId,invoiceItem.id,0,quantity,0)
						}
						else{// 无invoice相应item记录
							notExistItemQty=notExistItemQty+1
							
							notExistItemsCodeList =notExistItemsCodeList+" "+skucode
						}
					
				}
			
				
			}
			
			
			fileReader.close();
			bufferedReader.close();
			//System.err.println("notExistItemQty="+notExistItemQty)
			
			if(BPtotalQuantity!=packingSheetquantity){
				if(notExistItemQty==0)
			 return "[WARNING]Colis "+Colis+" uploaded with packing quantity: "+packingSheetquantity.toString() +" is not equal to BP Quantity: "+BPtotalQuantity.toString()
			 else
			 return "[WARNING]Colis "+Colis+" uploaded with packing quantity: "+packingSheetquantity.toString() +" is not equal to BP Quantity: "+BPtotalQuantity.toString()+ ".  SkuCode:"+notExistItemsCodeList +"not found in Invoice"
			 
			}
			else{
				if(notExistItemQty==0)
			 return packingSheetquantity.toString()+"items uploaded sucessfully, all items for this BP packed! "
			 else  "[WARNING]"+packingSheetquantity.toString()+"items uploaded sucessfully "+ ". SkuCode:"+notExistItemsCodeList +" not Found in Invoice"
			
			}
			
			
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
			
		}
		
		//return ""
	}
	
	
	
	
	def autoPacking(filename,packingId){
		
		try{
			def packing= Packing.get(packingId)
			def business=packing.business
			int BPtotalQuantity=business.totalQuantity
			if(isBPShipmentExist(business.id)){
				//System.err.println("Has Shipment Sheet")
				flash.message = "Can not start autoPacking, shipment does already exist!"
				return flash.message.toString();
			}
			
			/*def isAllPack=logisticService.checkIfBusinessItemAllPacked(packing)
			if(isAllPack)
			{
				System.err.println("All packed")
				flash.message = "All the items have be packed, can not upload new items!"
				return;
			}
			*/
			//delete old packing list
			def cartons =packing.cartons
			cartons.each{
				//System.err.println("carton ID==========="+it.id)
				deleteCarton(it.id)
				
				}
			
		 
		FileReader fileReader = new FileReader(filename)
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String myreadline;
		def cartonId=null;
		int notExistItemQty=0;
		String notExistItemsCodeList="";
		
		int packingSheetquantity=0;
		while (bufferedReader.ready()) {
			myreadline = bufferedReader.readLine();//读取一行
			 //判断是否为新箱子开始
			
			if(myreadline=="#"){
				//新箱子
				cartonId=autoAddCarton(packingId);
			}
			else{
				if(cartonId){
					
					String[] data = myreadline.split(",");
					String skucode=data[0];
					String quantity=data[1];
					
					packingSheetquantity=packingSheetquantity+quantity.toInteger()
					//System.out.println("skucode="+skucode);
					//System.out.println("quantity="+quantity);
					//System.out.println(myreadline);//在屏幕上输出
					
					def results = InvoiceItem.withCriteria{
						
						invoiceHeader{
							eq("business",business)
						}
						product{
							eq("skuCode",skucode)
							
							/*category{
								order("name","asc")
							}*/
						}
						
					}
						if(results[0]){
							def invoiceItem=results[0];
							logisticService.updateCartonItem("add",cartonId,invoiceItem.id,0,quantity,0)
						}
						else{// 无invoice相应item记录
							notExistItemQty=notExistItemQty+1
							
							notExistItemsCodeList =notExistItemsCodeList+" "+skucode
						}
					
					
					}
					
					
					
					
				}
			}
		fileReader.close();
		bufferedReader.close();
		//System.err.println("notExistItemQty="+notExistItemQty)
		
		if(BPtotalQuantity!=packingSheetquantity){
			if(notExistItemQty==0)
		 return "[WARNING]Packing quantity: "+packingSheetquantity.toString() +" is not equal to BP Quantity: "+BPtotalQuantity.toString()
		 else
		 return "[WARNING]Packing quantity: "+packingSheetquantity.toString() +" is not equal to BP Quantity: "+BPtotalQuantity.toString()+ ".  SkuCode:"+notExistItemsCodeList +"not found in Invoice"
		 
		}
		else{
			if(notExistItemQty==0)
		 return packingSheetquantity.toString()+"items uploaded sucessfully "
		 else  "[WARNING]"+packingSheetquantity.toString()+"items uploaded sucessfully "+ ". SkuCode:"+notExistItemsCodeList +" not Found in Invoice"
		
		}
		}
		
		catch (IOException e) {
            e.printStackTrace();
			
        }
		
		
	}
	
	
	def autoAddCarton(packingId,Colis,expedition){
		def carton
			def packing = Packing.get(packingId)
			//System.err.println("autoPacking packing id="+params.id)
			if(packing){
				
				packing.hardCopy=expedition
				if (!packing.save(flush: true)) {
					packing.errors.each {
					  log.info it
					}
					return null;
				  }
				
				
				carton = new Carton()
				carton.code = Colis.toInteger()
				carton.packing = packing
				carton.description1 = ''
				carton.description2 = ''
			}
		
		if (!carton.save(flush: true)) {
		  carton.errors.each {
			log.info it
		  }
		  return null;
		}
		else{
			return carton.id;
		}
		
		
	}
	
	
	
	
	def autoAddCarton(packingId){
		def carton
			def packing = Packing.get(packingId)
			//System.err.println("autoPacking packing id="+params.id)
			if(packing){
				carton = new Carton()
				def code = logisticService.getCartonNextCode(packing)
				carton.code = code
				carton.packing = packing
				carton.description1 = ''
				carton.description2 = ''
			}
		
		if (!carton.save(flush: true)) {
		  carton.errors.each {
			log.info it
		  }
		  return null;
		}
		else{
			return carton.id;
		}
		
		
	}
	
	
	
	
	//得到packing 信息
	def showPacking={
		//def id = params.id
		String str_id = params.id
		if(str_id)
		str_id=str_id.replace(',','');
		def id=str_id
		def packing = Packing.get(id);
		def cartonIds = []
		def reInvoiceNumbers=''
		def bpList
		
		
		
		if(packing){
			def cartons = Carton.findAllByPacking(packing,[sort:"code",order:"asc"])
			cartons.each{
				cartonIds.add(it.id)
			}
			
			
			reInvoiceNumbers = logisticService.getReInvoice(packing.business)  
			log.info reInvoiceNumbers
		}else{
			bpList=invoiceService.getNoPackingBusiness()
		}
		render(view:"/hermes/logistic/packingShow",model:[instance:packing,cartons:cartonIds,reInvoiceNumbers:reInvoiceNumbers,bpList:bpList])
	}
	
	
	def updatePacking ={
		log.info params
		//def id = params.id
		String str_id = params.id
		if(str_id)
		str_id=str_id.replace(',','');
		def id=str_id
		def bpCode = params.bpCode
//		 if(!bpCode)
//		 {
//			 render(view:"/hermes/logistic/packingShow",model:[instance:null,cartons:[],reInvoiceNumbers:''])
//			 return false
//			 }
		
		
		def packing = Packing.get(id)
		
		if(!packing){//新建Packing
			if(bpCode=='')
			{
				flash.message = "bp code: is empty"
				render(view:"/hermes/logistic/packingShow",model:[instance:null,cartons:[],reInvoiceNumbers:''])
				return false
				}
			
			def business = Business.findByCode(bpCode)
			def exist = Packing.findByBusiness(business)
			log.info exist
			if(!exist){
				packing = new Packing()
				packing.business = business
				packing.date = new Date()
			}else{
				flash.message = "bp code:"+bpCode+" packing is exists!"
				render(view:"/hermes/logistic/packingShow",model:[instance:null,cartons:[],reInvoiceNumbers:''])
				return false
			}
			
			
		}
		 //更新Packing
		//检查是否打包完全
		def isAllPack=logisticService.checkIfBusinessItemAllPacked(packing)
			if(!isAllPack)
			{
				System.err.println("not all packed")
				flash.message = "Not all the items have be packed!"
			}
			 
			System.err.println("All packed")
			packing.hardCopy = params.hardCopy
			packing.description = params.description
		
			if(params.vaildDate){
				SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
				Date date = formate.parse(params.vaildDate);
				packing.vaildDate = date
				packing.vaild = 'Y'
			}
			else  	//2015.2.12 要求 validdate 默认为今天
			{
				//SimpleDateFormat formate= new SimpleDateFormat("yyyy-MM-dd");
			
				//Date date = formate.parse(new Date());
				packing.vaildDate = new Date();
				packing.vaild = 'Y'
			}
			
			
			
			
			if (!packing.save(flush: true)) {
				packing.errors.each {
				  log.info it
				}
			  }
			 
		 
			def cartonIds = []
			if(packing){
				packing.cartons.each{
					cartonIds.add(it.id)
				}
			}
			def reInvoiceNumbers = logisticService.getReInvoice(packing.business)  
			render(view:"/hermes/logistic/packingShow",model:[instance:packing,cartons:cartonIds,reInvoiceNumbers:reInvoiceNumbers])
		
	}
	
	/* carton section */
	def listCartonItemsJson={
		def cartonId = params.id
		def carton = Carton.get(cartonId)
		def criteriaClosure = {
            eq("carton",carton)
			order("sequence","asc")
		}
		def instanceList = jqgridFilterService.jqgridAdvFilter(params,CartonItem,criteriaClosure,false);
		def pager = jqgridFilterService.jqgridAdvFilter(params,CartonItem,criteriaClosure,true);
		JqgridJSON jqgridJSON = new JqgridJSON(pager)
		def i=0
		instanceList.each{elem->  
			def cell = new ArrayList()
			cell.add(cartonId)
			cell.add(elem.id)
			cell.add(elem.invoiceItem.id)
			cell.add(elem.invoiceItem.product.code)
			cell.add(elem.sequence)
			cell.add(elem.invoiceItem.invoiceHeader.code)
			cell.add(elem.invoiceItem.product.dept)
			if(elem.invoiceItem.productName&&elem.invoiceItem.productName.length()>0) //不使用master
				cell.add(elem.invoiceItem.productName)
			else//使用master
			cell.add(elem.invoiceItem.product.category.name)
			//cell.add(elem.invoiceItem.product.productName)
			cell.add(elem.invoiceItem.product.name)
			cell.add(elem.quantity)
			i=i+1
      		jqgridJSON.addRow(i, cell)
			  cell=null;
		}
		render jqgridJSON.json as JSON
	}
	def showCarton={
		def id = params.id
		def carton = Carton.get(id);
		//System.err.println("showCarton")
		if(carton){
			def cartonJson = [:]
			cartonJson.desc1 = logisticService.getCartonTitleH(carton)
			cartonJson.desc2 = logisticService.getCartonTitleF(carton)
			//System.err.println("cartonJson.desc2="+cartonJson.desc2)
			render cartonJson as JSON
		}
	}
	def deleteCarton = {
		logisticService.deleteCarton(params.id)
		render(text:"success")
	}
	
	def cloneCarton ={
		def new_carton=logisticService.cloneCarton(params.id)
		render(text:new_carton.id)
	}
	
	
	def updateCarton = {
		log.info params
		def dialogCartonId = params.dialogCartonId
		def carton = Carton.get(dialogCartonId)

		if(!carton){
			def packing = Packing.get(params.id)
			if(packing){
				carton = new Carton()
				def code = logisticService.getCartonNextCode(packing)
				carton.code = code
				carton.packing = packing
				carton.description1 = ''
				carton.description2 = ''
			}
		}
	    if (!carton.save(flush: true)) {
	      carton.errors.each {
	        log.info it
	      }
	    }
		log.info "saved carton: code="+carton.code+" id="+carton.id
		render(text:carton.id)
	}
	def updateCartonItem={
		log.info params
		def oper = params.oper
		def cartonId = params.cartonId
		def cartonItemId = params.cartonItemId
		def invoiceItemId = params.invoiceItemId
		
		def quantity = params.cartonQuantity
		def sequence = params.sequence
		//System.err.println("updateCartonItem quantity="+quantity)
		logisticService.updateCartonItem(oper,cartonId,invoiceItemId,cartonItemId,quantity,sequence)
		//System.err.println("111111111111");
		render(text:"")		
	}
	

	
	
	
	def updateItemPackingQty={
			//System.err.println("lt"+params.id+params.itemId)
		//log.info params
		//def oper = params.oper
 
		//def quantity = params.qty
 
		//System.err.println("updateItemPackingQty oper="+oper)
		//System.err.println("updateItemPackingQty quantity="+quantity)
		//logisticService.updateCartonItem(oper,cartonId,invoiceItemId,cartonItemId,quantity,sequence)
		 
			render(text:"")	
	}
	
	
	
	
	
	def showCartonDet={
		log.info params
		def id = params.id
		def carton = Carton.get(id);
		if(carton){
			render(view:"/hermes/logistic/cartonShowDet",model:[instance:carton])
		}
	}
	
	def showCartonWeightList={
		System.err.println("333");
		log.info params
		def id = params.id
		def packing = Packing.get(id);
		if(packing){
			render(view:"/hermes/logistic/updateCartonWeightShow",model:[instance:packing])
		}
		
	}
	
	
	//批量修改carton的重量
	def editCartonWeights={
		 
			def items = params.items
			log.info "items="+items
			if(items){
				def invoiceitemids = items.split(',')
				invoiceitemids.each{
					
					java.lang.String str =it;
					
					int index1=str.indexOf('-')
					java.lang.String cartonId=str.substring(0,index1);
					java.lang.String str_2=str.substring(index1+1,str.length());
					int index2=str_2.indexOf('-')
					java.lang.String nweight=str_2.substring(0,index2);
					java.lang.String gweight=str_2.substring(index2+1,str_2.length());
					def carton = Carton.get(cartonId)
					
					
					if(carton){
						//System.err.println("cartonId="+cartonId)
						//System.err.println("nWeight="+nweight)
						//System.err.println("gWeight="+gweight)
						carton.nWeight= new BigDecimal(nweight)
						carton.gWeight= new BigDecimal(gweight)
						 
						if (!carton.save(flush: true)) {
							carton.errors.each {
								log.info it
							}
						}
						//logisticService.updateCartonWeight(cartonId,nweight,gweight)
					}
					
				 
				}
			}
			
		 
		render(view:"/hermes/logistic/updateCartonWeightShow",model:[])
		
	}
	
	
	
	//显示本BP所有carton的重量列表
	def listCartonWeightListJson={
		log.info params
		def packing =Packing.get(params.id)
		def cartonlist = []
		if(packing){
			
//			def cartons = Carton.findAllByPacking(packing,[sort:"code",order:"asc"])
//					cartons.each{
//							 
//							def ii = [:]
//							ii.id=it.id
//							ii.code=it.code
//							ii.nweight=it.nWeight
//							ii.gweight=it.gWeight
//							System.err.println("it.nWeigh="+it.nWeight+"\n");
//							cartonlist.add(ii)
//						}
			def criteriaClosure = {
				eq("packing",packing)

				order("code","asc")
			
		}
		 
			 
			def instanceList = jqgridFilterService.jqgridAdvFilter(params,Carton,criteriaClosure,false)
			def pager = jqgridFilterService.jqgridAdvFilter(params,Carton,criteriaClosure,true)
			JqgridJSON jqgridJSON = new JqgridJSON(pager)
			log.info instanceList.size()
			def ii = 0
			instanceList.each{elem->
				def cell = new ArrayList()
				//Kurt Edited
					cell.add(elem.id)
					cell.add(elem.code)
					cell.add(elem.nWeight)
					cell.add(elem.gWeight)
				
					jqgridJSON.addRow(ii, cell)
					ii=ii+1
					cell=null;
			}
			render jqgridJSON.json as JSON
		}
		
		

	}
	
	def updateCartonWeightShow={
		log.info params
		
//		if(packing){
//			def cartons = Carton.findAllByPacking(packing,[sort:"code",order:"asc"])
//			cartons.each{
//				cartonIds.add(it.id)
//			}
		
		
		render(text:"s")
	}
	
	
	
	
	
	
	//Kurt
	def updateCartonDet={
		//request.setCharacterEncoding("gb2312");
		log.info params
		def id = params.id
		def carton = Carton.get(id)
		//System.err.println("cartonID=="+id)
		if(carton){
			//def hardCopy = params.hardCopy
			//if(hardCopy) carton.hardCopy = hardCopy
			
			def nWeight = params.nWeight
			def gWeight = params.gWeight
			//System.err.println("nWeight=="+nWeight)
			//System.err.println("gWeight=="+gWeight)
			if(nWeight) carton.nWeight = new BigDecimal(nWeight)
			if(gWeight) carton.gWeight = new BigDecimal(gWeight)
			def items = params.items
			log.info "items="+items
			if(items){
				def invoiceitemids = items.split(',')
				invoiceitemids.each{
					
					java.lang.String str =it;
					
					int index1=str.indexOf('-')
					java.lang.String itemid=str.substring(0,index1);
					java.lang.String item_qty=str.substring(index1+1,str.length());
					log.info "invoiceItemid="+itemid
					def invoiceItem = InvoiceItem.get(itemid)
					if(invoiceItem){
						logisticService.updateCartonItem('add',id,invoiceItem.id,null,item_qty,null)
					}
				}
			}
			carton.save(flush:true)
			flash.message = "save success!"
		}else{
			flash.message = "carton id:="+id+" is not found"
		}
		render(view:"/hermes/logistic/cartonShowDet",model:[instance:carton])
		
	}
	
	//Kurt Edited 处理字符串 amount 去掉<div>标签
	def getPureAmount(amount)
	{
		if(amount==null)
		return 0;
		java.lang.String str =amount;
		
		int index1=str.indexOf('>')
		//System.err.println("index1="+index1);
		if(index1==-1)
		return amount
		java.lang.String item1=str.substring(index1+1,str.length());
		//System.err.println("item1="+item1);
		int index2=item1.indexOf('<')
		//System.err.println("index2="+index2);
		if(index2==-1)
		return amount
		java.lang.String item2=item1.substring(0,index2);
		//System.err.println("item2="+item2);
		return item2
	}
	
	//Kurt edited  导出invoice归类
	def exportInvoiceList={
		try{
		log.info params
		def business = Business.get(params.id)
		
		java.lang.String parent_Company=new String(params.parentCompany.toString());
		if(parent_Company)
		parent_Company=parent_Company.substring(0, 1)
		
		//def reInvoiceNumbers = logisticService.getReInvoice(business) 换一种格式
		def currency=business.currency
		def reInvoiceNumbers = logisticService.getReInvoice_fmt2(business)
		//def adjItem = logisticService.getAdjustInvoiceItem(params.id)
		response.setContentType("application/excel");
		response.setHeader("Content-disposition", "attachment;filename="+reInvoiceNumbers+".xls")
		//render(contextType:"application/vnd.ms-excel")
		HSSFWorkbook wb = new HSSFWorkbook()
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
		def helper = wb.getCreationHelper()
		def sheet = wb.createSheet("invoice no "+business.code)
		def header = sheet.getHeader();
		def centertext=HSSFHeader.font("Arail", "regular")+ HSSFHeader.fontSize((short) 9)+ properties.getProperty("headerText")
		
		header.setCenter(centertext)
		sheet.setMargin(HSSFSheet.TopMargin,1.6)
		def row = null
		def cell = null
		
		row = sheet.createRow(1)
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
		row = sheet.createRow(2)
		//row.createCell(1).setCellValue("HERMES (CHINA ) CO., LTD")
		
		if(parent_Company[0]=='A'){
		row.createCell(1).setCellValue(properties.getProperty("companyNameEN_A") )
		}
		else
		{
			row.createCell(1).setCellValue(properties.getProperty("companyNameEN_B") )
			}
		
		
		row = sheet.createRow(3)
		//row.createCell(1).setCellValue("3010FLOOR, 1038 NAN JING ROAD (E),")
		if(parent_Company[0]=='A'){
		row.createCell(1).setCellValue(properties.getProperty("companyAddress1_A"))
		}
		else
		{
			row.createCell(1).setCellValue(properties.getProperty("companyAddress1_B"))
			}
		row.createCell(5).setCellValue("日期 Date:")
		String DATE_FORMAT = "yyyy-MM-dd"
		def sdf = new SimpleDateFormat(DATE_FORMAT)
		row.createCell(6).setCellValue(sdf.format(new Date()))
		row = sheet.createRow(4)
		//row.createCell(1).setCellValue("SHANGHAI 200041, P.R.C.")
		if(parent_Company[0]=='A'){
		row.createCell(1).setCellValue(properties.getProperty("companyAddress2_A"))
		}
		else
		{
			row.createCell(1).setCellValue(properties.getProperty("companyAddress2_B"))
			}
		//Kurt Edited
		row = sheet.createRow(5)
		cell = row.createCell(6)
		cell.setCellValue("CIP")
		cell.setCellStyle(cs)
		cell = row.createCell(7)
		cell.setCellValue("CIP")
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
		cell = row.createCell(8)
		cell.setCellValue("DEPT")
		cell.setCellStyle(cs)
		cell = row.createCell(9)
		cell.setCellValue("PODIUM")
		cell.setCellStyle(cs)
		
		
		
		def results = InvoiceItem.withCriteria{
			order("productName","asc")
			invoiceHeader{
				eq("business",business)
			}
			product{
				order("dept","asc")
				//order("material","asc")
				//order("name","asc")
				category{
					order("name","asc")
				}
			}
		}
		def rowi = 6
		def group = '#'
		def groupIndex = 1
		def groupQty = 0
		def groupCIF = 0
		def groupQtyTotal = 0
		def groupCIFTotal = 0
		def first=true
		
		
		List<InvoiceItemProperty> list = new ArrayList<InvoiceItemProperty>();
		results.each{
		InvoiceItemProperty initem=new InvoiceItemProperty()
		def description2=''
		if( it.metaClass.hasProperty(it,'product'))
			description2=it.product.category.description2
		if( it.metaClass.hasProperty(it,'product')&&it.product.category.description2==null||params.merge=='0')
			description2=''
		
		if(it.productName&&it.productName.length()>0){ //不使用master
			
			initem.setProductName(it.productName)
			if( it.metaClass.hasProperty(it,'product'))
				initem.setContent(it.productName +' '+it.product.name + ' '+it.product.category.description+' '+description2)
			else
				initem.setContent(it.productName)
	   }
	   else{
		   if( it.metaClass.hasProperty(it,'product')){
		   		initem.setProductName(it.product.category.name)
				initem.setContent(it.product.category.name +' '+it.product.name +' '+ it.product.category.description+' '+description2)
		   }
		   else {
		   		initem.setProductName('')
				initem.setContent('')
		   }
		   
	   }
	   	//initem.setSortOrderCode(initem.getProductName()+it.product.category.description)
	    initem.setSortOrderCode(initem.getProductName()+it.country)  //Harry要求改为以中文品名和原产国作为合并条件
		 //如果中文名中含有以下的关键字,那么在执行完目前的合并条件后,再根据SHORT REF(8位)分开合计 
		//衬衫，长裤、T恤衫
		 
		java.lang.String tmp_s=new String(initem.getProductName().toString());
		java.lang.String skucode=new String(it.product.code.toString());
		 
		if(tmp_s.indexOf("衬衫")!=-1||tmp_s.indexOf("长裤")!=-1||tmp_s.indexOf("T恤衫")!=-1)
		{
			//System.err.println("333")
			if(skucode.length()>=8)					 
			skucode=skucode.substring(0, 8)
			else
			skucode=skucode.substring(0, skucode.length())
			
			
			initem.setSortOrderCode(skucode+initem.getProductName()+it.country)
		}
		 
		if( it.metaClass.hasProperty(it,'product')){
		initem.setProductEnglishName (it.product.name)
		initem.setProductCategoryDescription (it.product.category.description+' '+it.product.category.description2)//Harry要求材质1+材质2作为最终材质
		initem.setInvoiceHeaderCode(it.invoiceHeader.code)
		initem.setProductCode(it.product.code)
		initem.setCountry(it.country)
		initem.setQuantity(it.quantity)
		initem.setDept(it.product.dept)
		initem.setPodium(it.podium)
		}
		else
		{
		initem.setProductEnglishName ('')
		initem.setProductCategoryDescription ('')//Harry要求材质1+材质2作为最终材质
		initem.setInvoiceHeaderCode(it.invoiceHeader.code)
		initem.setProductCode('')
		initem.setCountry(it.country)
		initem.setQuantity(it.quantity)
		initem.setDept('')
		initem.setPodium(it.podium)
			
			
			}
		/*
		if( adjItem && it.id == adjItem.id ){
			 
		initem.setAmount(it.amount+(it.logisticIssuance+it.logisticFreight+new BigDecimal(business.totalAdjDiff))/it.quantity)
			
		}
		else
		initem.setAmount(it.amount+(it.logisticIssuance+it.logisticFreight)/it.quantity)
		*/
		//logs_adj_amount 为调完的价格
		initem.setAmount(it.logsAdjAmount+(it.logisticIssuance+it.logisticFreight)/it.quantity)
		
		list.add(initem);
		//it
		}
		Collections.sort(list);
		
		list.each{
			//Kurt Edited  现根据中文品名进行分类
			//def groupName =  it.product.category.name + it.product.name
			def groupName
			def content
			 
			
			//如果中文名中含有以下的关键字,那么在执行完目前的合并条件后,再根据SHORT REF(8位)分开合计
			//衬衫，长裤、T恤衫
			 
			
				 groupName =it.sortOrderCode+  it.productName+it.country  //Harry要求改为以中文品名和原产国作为合并条件
				 
				 
				 
				 content= it.content
			 
		 
			
			if(!groupName.equals(group)){
				if(!group.equals('#')){
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
					cell.setCellValue(it.invoiceHeaderCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(it.productCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					//cell.setCellValue(groupName)
					cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
				
					cell=row.createCell(8)
					cell.setCellValue(it.dept)
					cell.setCellStyle(cs)
					
					cell=row.createCell(9)
					cell.setCellValue(it.podium)
					cell.setCellStyle(cs)
					
				
				groupQty = groupQty+it.quantity
				groupCIF = groupCIF+it.amount*it.quantity
				group=groupName
				log.info "write:"+it.productCode
			}else{
				rowi = rowi+1
					row = sheet.createRow(rowi)
					cell=row.createCell(0)
					cell.setCellValue(groupIndex)
					cell.setCellStyle(cs)
					//Kurt Edited
					cell=row.createCell(1)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.invoiceHeaderCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(it.productCode)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					//cell.setCellValue(groupName)
					cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(8)
					cell.setCellValue(it.dept)
					cell.setCellStyle(cs)
					
					cell=row.createCell(9)
					cell.setCellValue(it.podium)
					cell.setCellStyle(cs)
					
					groupQty = groupQty+it.quantity
					groupCIF = groupCIF+it.amount*it.quantity
					log.info "write same:"+it.productCode
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
			//cell.setCellValue('EUR')
			if(currency=='RMB')
			currency='CNY'
			cell.setCellValue(currency)
			
			
			cell.setCellStyle(cs)

			cell=row.createCell(7)
			cell.setCellValue(groupCIFTotal)
			cell.setCellStyle(cs)
		
		
		
		
		////以下注释
		/*
		results.each{
			//Kurt Edited  现根据中文品名进行分类
			//def groupName =  it.product.category.name + it.product.name
			def groupName
			def content
			if(it.productName&&it.productName.length()>0){ //不使用master
				 groupName =  it.productName //+ it.product.category.description
				 content= it.productName +it.product.name + it.product.category.description
			}
			else{
				groupName =  it.product.category.name //+ it.product.category.description
				content= it.product.category.name +it.product.name + it.product.category.description
			}
			
			if(!groupName.equals(group)){
				if(!group.equals('')){
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
					cell.setCellValue(it.invoiceHeader.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(it.product.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					//cell.setCellValue(groupName)
					cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
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
					//Kurt Edited
					cell=row.createCell(1)
					//cell.setCellValue(it.invoiceItem.invoiceHeader.code+'-'+it.sequence)
					cell.setCellValue(it.invoiceHeader.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(2)
					cell.setCellValue(it.product.code)
					cell.setCellStyle(cs)
					
					cell=row.createCell(3)
					//cell.setCellValue(groupName)
					cell.setCellValue(content)
					cell.setCellStyle(cs)
					
					cell=row.createCell(4)
					cell.setCellValue(it.country)
					cell.setCellStyle(cs)
					
					cell=row.createCell(5)
					cell.setCellValue(it.quantity)
					cell.setCellStyle(cs)
					
					cell=row.createCell(6)
					cell.setCellValue(it.amount)
					cell.setCellStyle(cs)
					
					cell=row.createCell(7)
					cell.setCellValue(it.amount*it.quantity)
					cell.setCellStyle(cs)
					groupQty = groupQty+it.quantity
					groupCIF = groupCIF+it.amount*it.quantity
					log.info "write same:"+it.product.code
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


		def os = response.outputStream
		 
		wb.write(os)
		os.close()
		}
		catch(Exception ex)
		{}
	}
	
	
	
	/* auto complete */
	def listDistinctHawbJson={
		def results = Business.withCriteria{
			projections{
				distinct("hawb")
			}
		}
		def hawbs = []
		results.each{
			def hawb = [:]
			hawb.code = it
			hawbs.add(hawb)
		}
		render hawbs as JSON
	}
	def listBusinessCodeJson = {
		def results = Business.withCriteria{
			projections{
				distinct("code")
			}
		}
		log.info results
		def bps = []
		results.each{
			log.info it
			def bp = [:]
			bp.code = it
			bps.add(bp)
		}
		render bps as JSON
	}
	def listDistinctProductByBusiness = {
		def business = Business.findByCode(params.bpCode)
		def products = InvoiceItems.withCriteria{
			projections{
				distinct("product")
			}
			invoiceHeader{
				eq("business",business)
			}
		}
		log.info products
		//render products as JSON
	}
	def listCartonInvoiceItemsJson={
		def cartonId = params.cartonId
		def carton = Carton.get(cartonId)
		if(carton){
			def results = InvoiceItem.withCriteria{
				invoiceHeader{
					eq("business",carton.packing.business)
				}
			}
			def invoiceItems = []
			results.each{
			 	def ii = [:]
				ii.product = it.product.code
				ii.category = it.product.category.code
				ii.quantity = it.quantity
				ii.dept = it.product.dept
				ii.material = it.product.material
				
				if(it.productName&&it.productName.length()>0) //不使用master
				ii.zhname =  it.productName  
				else//使用master
				ii.zhname = it.product.category.name
				ii.enname = it.product.name
				ii.invoiceItemId = it.id
				ii.invoiceCode = it.invoiceHeader.code
				ii.cartonId = cartonId
				invoiceItems.add(ii)
			}
			render invoiceItems as JSON
		}
	}
	

	
	
	
	
	
	//未装物品选择列表
	def listCartonProductItemsJson={
		log.info params
		def cartonId = params.id
		//Kurt Edited
		def sortitem=params.sidx
		def sortOrder=params.sord
		def carton = Carton.get(cartonId)
		//System.err.println("333333333333333");
		if(carton){
			/*
			def results = InvoiceItem.withCriteria{
				invoiceHeader{
					eq("business",carton.packing.business)
				}
				product{
					if(params.prodCode && params.prodCode.length()>0) like('code','%'+params.prodCode+'%')
				}
			}*/
			def criteriaClosure = {
		        and{
					invoiceHeader{
						eq("business",carton.packing.business)
					}
					product{
						if(params.prodCode && params.prodCode.length()>0) like('code','%'+params.prodCode+'%')
						//Kurt Edited
						if(sortitem&&sortitem=='product')
						order("code",sortOrder)
						else
						order("dept",sortOrder) //暂不需要
						}
				}
				
			}
			def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,false)
		    def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,true)
		    JqgridJSON jqgridJSON = new JqgridJSON(pager)
			log.info instanceList.size()
			def ii = 0
			instanceList.each{elem->
				def cell = new ArrayList()
				//Kurt Edited
				def restQty=checkRestQuantityExistCartonProductTobePacked(elem)
				if(restQty>0){
					cell.add(elem.id)
					cell.add(elem.invoiceHeader.code)
					cell.add(elem.product.code)
					cell.add(elem.product.dept)
					//Kurt edited
					if(elem.productName&&elem.productName.length()>0) //不使用master
					cell.add(elem.productName)
					else//使用master
					cell.add(elem.product.category.name)
					//cell.add(elem.product.category.name)
					//END
					cell.add(elem.product.name)
					//Kurt Edited   显示剩下的未装数量
					//cell.add(elem.quantity)
					cell.add(restQty)
					//cell.add(restQty)//默认打包数
					jqgridJSON.addRow(ii, cell)
					ii=ii+1
					cell=null;
				}
				/*
				.category = it.product.category.code
				ii.quantity = it.quantity
				ii.dept = it.product.dept
				ii.material = it.product.material
				ii.zhname = it.product.category.name
				ii.enname = it.product.name
				ii.invoiceItemId = it.id
				ii.invoiceCode = it.invoiceHeader.code
				ii.cartonId = cartonId
				*/
				//invoiceItems.add(ii)
			}
			//log.info invoiceItems
			//render invoiceItems as JSON
			render jqgridJSON.json as JSON
		}
	}
	
	
	
	def checkExistCartonProduct(carton,invoiceItem){
		def result = false
		carton.cartonItems.each{
			if(it.invoiceItem.id==invoiceItem.id){
				result = true
			}
		}
		
		return result
	}
	//Kurt Edited  返回未装箱物品数量
	def checkRestQuantityExistCartonProductTobePacked(invoiceItem){
		
		def sql1 = new Sql(dataSource);
		def sql2 = new Sql(dataSource);
		//System.err.println("checkExistCartonProductIsPacked")
		def query1 = "select quantity from hermes_invoiceitem where id= ? "
		def TotalQuantity = sql1.rows(query1,[invoiceItem.id]);
		//System.err.println("TotalQuantity="+TotalQuantity)
		def query2 ="select sum(quantity) as packed from hermes_packingcarton_item where invoice_item_id = ? "
		def PackedQuantity= sql2.rows(query2,[invoiceItem.id]);
		//System.err.println("PackedQuantity="+PackedQuantity)
		def restQuantity=0
		if(PackedQuantity[0][0])
		{
		 restQuantity=TotalQuantity[0][0]-PackedQuantity[0][0]
		}
		else
		{
			restQuantity=TotalQuantity[0][0]
		}
		 
		//System.err.println("invoiceItem= "+invoiceItem.id+" restQuantity=="+restQuantity)
		return restQuantity
	}

	/* todo need to delete */
	def showInvoice = {
		def instance = InvoiceHeader.findByCode(params.id)
		if (!instance) {
		  flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'InvoiceHeader.label', default: 'InvoiceHeader'), params.id])}"
		  redirect(action: "list")
		}
		else {
		  log.info instance.code
		  render(view: "hermesshow", model: [instance: instance])
		}
	}
  	def listInvoiceItemsJson = {
		def adjItem = logisticService.getAdjustInvoiceItem()
		def invoiceHeader = InvoiceHeader.findByCode(params.id)
		def criteriaClosure = {
			eq 'invoiceHeader',invoiceHeader
		}
		def instanceList = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,false);
		def pager = jqgridFilterService.jqgridAdvFilter(params,InvoiceItem,criteriaClosure,true);
		
		JqgridJSON jqgridJSON = new JqgridJSON(pager)
		def i=0
		instanceList.each{elem->
			def cell = new ArrayList()
			cell.add(elem.id)
			cell.add(elem.product.category.code)
			cell.add(elem.product.colorName)
			cell.add(elem.product.sizeName)
			cell.add(elem.product.familyName)
			cell.add(elem.country)
			cell.add(elem.product.material)
			if (elem.productName && elem.productName.length() > 0) {
				cell.add('No')
				cell.add(elem.productName)
			} else {
				cell.add('Yes')
				cell.add(elem.product.category.name)
			}
			cell.add(elem.product.productName)
	      	cell.add(elem.quantity)
			if( elem.id == adjItem.id ){
				cell.add("<div class='redColor'>"+elem.getLogisticAmount()+"</div>")
			}else{
				cell.add(elem.getLogisticAmount())
			}
			cell.add(elem.logisticIssuance)
			cell.add(elem.logisticFreight)
			cell.add(elem.getLogisticTotal() * elem.quantity)
			i=i+1
      		jqgridJSON.addRow(i, cell)
			  cell=null;
		}
		render jqgridJSON.json as JSON
  	}
	
}