

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'invoiceHeader.label', default: 'InvoiceHeader')}" />
        <title>Report Details</title>
    <style>
        .FixedTitleRow
        {
            position: relative; 
            top: expression(this.offsetParent.scrollTop); 
            z-index: 10;
            background-color: #E6ECF0;
        }
        
        .FixedTitleColumn
        {
            position: relative; 
            left: expression(this.parentElement.offsetParent.scrollLeft);
        }
        
        .FixedDataColumn
        {
            position: relative;
            left: expression(this.parentElement.offsetParent.parentElement.scrollLeft);
            background-color: #E6ECF0;
        }
    </style>

        
  <script type="text/javascript">
  function pickdates(id){ jQuery("#"+id+"_sdate","#rowed6").datepicker({dateFormat:"yy-mm-dd"}); } 
  function resize_the_grid(){
		$('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
	}
	//var grid;   
  var caption = "";
  var editurl = "${resource(dir:'')}/${controllerName}/updateImportReport";
  var listurl = "${resource(dir:'')}/${controllerName}/listImportReport";
  
  var colNames = [
                  "Id",
        		  "HAWB",
        	      "ReInvoice",
        	      "InvoiceNo",
        	      "QTY",
        	      "CartonNum",
        	      "IssueInvoiceDate",
        	      "HreceivedInvoceDate",
        	      "coCitisImportDate",
        	      "coCitisExportDate",
        	      "coCitisSendingImportDate",
        	      "DepartParis",
        	      "ArrivalSH",
        	      "BLReceiveDate",
        	      "Q & A duration",
        	      "Date of D/O Exchange",
        	      "Duty paid",
        	      "Inspection",
        	      "VAT & duty amount",
        	      "FinCustCleaDate",
        	      "Direct to store",
        	      "Warehouse in date",
        	      "Rcvd numers of cartons",
        	      "Damaged carton No.",
        	      "shortage pcs",
        	      "Rcv the D/O instruction from store",
        	      "Required delivery date by store",
        	      "DeliveryToStoreDate",
        	      "Remark"
        	    ];
        	    var colModel = [
			{name:'invoiceId',hidden:true,editable:false},
			{name:'hawb',hidden:false,editable:false},
			{name:'reInvoice',hidden:false,editable:false},
			{name:'invoiceNo',hidden:false,editable:false,sortable:false},
			{name:'QTY',hidden:false,editable:false},
			{name:'CartonNum',hidden:false,editable:false},
			{name:'invoiceDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},//Kurt Edited
			{name:'HreceivedInvoceDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'CoCitisImport',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'CoCitisExport',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'CoCitisSendingImport',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'DepartParis',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'ArrivalSH',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'BLReceiveDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'qADuration',hidden:false,editable:false},
			{name:'dOExchangeDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'dutyPaidDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'inspectionDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'vatDutyAmount',hidden:false,editable:false},
			{name:'FinCustCleaDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'directToStore',hidden:false,editable:false},
			{name:'warehouseInDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'rcvdNumersOfCartons',hidden:false,editable:false},
			{name:'damagedCartonNo',hidden:false,editable:false},
			{name:'shortagePcs',hidden:false,editable:false},
			{name:'rcvDOInstructionFromStoreDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'requiredDeliveryByStoreDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'DeliveryToStoreDate',sortable:false,editable:false,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},
			{name:'remark',hidden:false,editable:false}
			 ];
        		var navOptions = {view:false,del:false,edit:false,add:false,search:false};

	
  jQuery(document).ready(function() {
		
		var lastsel;
		jQuery("#grid").jqGrid({
			 
			url:listurl,
			
			editurl:editurl, //Kurt Added
			
			datatype: "json",
			mtype: 'GET', 
			colNames:colNames,
			colModel:colModel,
			pager: '#gridnav', 
			rowNum:30,
			viewrecords: true,//Kurt Updated 
			autowidth: true,
			multiselect: true, //Kurt Updated 
			height:'auto',
			rownumbers:true,
			//Kurt Added
			 onSelectRow: function(id) {
				if (id) {
					jQuery('#grid').jqGrid('restoreRow', lastsel);
					jQuery('#grid').jqGrid('editRow', id, false);
					lastsel = id;
				}
			}
		});
		jQuery("#grid").jqGrid('navGrid',"#gridnav",{view:false,edit:false,add:false,del:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true}); 
		$("#cartonDetForm").validate({
			rules: {
		    	nWeight:{number: true},
		    	gWeight:{number: true}
		  	},
			messages: {
				nWeight: " Number",
				gWeight: " Number"
			}
		});
		$("#invoiceDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#receiveDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#coCitisImportDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#coCitisExportDate").datepicker({ dateFormat: 'yy-mm-dd' });
		
		$("#coCitisSendingImportDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#fromPariseDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#arriveDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#baoLongReceiveDate").datepicker({ dateFormat: 'yy-mm-dd' });

		
		$("#dOExchangeDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#dutyPaidDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#inspectionDate").datepicker({ dateFormat: 'yy-mm-dd' });

		$("#clearanceDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#warehouseInDate").datepicker({ dateFormat: 'yy-mm-dd' });
		
		$("#rcvDOInstructionFromStoreDate").datepicker({ dateFormat: 'yy-mm-dd' });
		
		$("#requiredDeliveryByStoreDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#deliveryDate").datepicker({ dateFormat: 'yy-mm-dd' });
		
	});



	function exportExcel(){
		//alert("aa");
		/*var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}*/
		//var datas;
		/*var datas=jQuery("#grid").jqGrid('getDataIDs');
		var invoiceIds = [];
		for(var i in datas){
			var rowNum = datas[i];
			var item = $("#grid").getRowData(rowNum).invoiceId;
			invoiceIds.push(item);
		}*/
		//
		/*if(invoiceIds==null||invoiceIds=='')
			{
		alert("no data to export!");
			}
		else
			{
			window.location.href=url;
			}*/
			var hawb=document.getElementById("hawb").value
			var store=document.getElementById("store").value
			var iscocitis=document.getElementById("cocitis").checked
			var cocitis=0
			// alert(store)
			// alert(inYear)
			// alert(cocitis)
			if(iscocitis)
				cocitis=1;
			var url = "${resource(dir:'')}/interface/exportImportReport"+"?hawb="+hawb+"&store="+store+"&cocitis="+cocitis;
			
			window.location.href=url;
		
		//self.focus();  
	}	
	
	function update(){
		alert("aa");
		alert($("#grid").getRowData(1)[1]);
		var selects = grid.jqGrid('getRowData'); 
		//var selects = jQuery("#grid").jqGrid('getRowData'); 
		//userForm
		alert("123"+selects);
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			alert("123"+i);
			items.push(item);
		}
		
		var url = "${resource(dir:'')}/${controllerName}/createPO"+"?items="+items;
		document.forms["poForm"].action = url;
		document.forms["poForm"].submit();
	}

	 function gridReload(){
		 var query = $('#searchForm').formSerialize(); 
			jQuery("#grid").jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid");
	    }
	function gridReloadAfterUpdateSelected(){
		
		//var selects = grid.jqGrid('getRowData'); 
		var selects = $("#grid").jqGrid('getGridParam','selarrrow'); 
		
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getCell(rowNum,'invoiceId');
			items.push(item);
		}
		//alert(url);
		
		if(items==null||items==""){
				alert("no items to update");
				return;
			}
			
		//alert("finish")
		
		var updateurl ="${resource(dir:'')}/${controllerName}/updateSelectedImportReport?items="+items;
		//alert(updateurl);
		document.getElementById("hiddenSelectedItem").value=items
		document.forms["searchForm"].action = updateurl;
		document.forms["searchForm"].submit();

		//var query = $('#searchForm').formSerialize(); 
		//jQuery("#grid").jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid");

		}

	      
  </script>        
    </head>
    <body>
    		<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/report5/${params?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-refresh"></span>r
			</a>
			<a href="${resource(dir:'')}/${controllerName}/report2" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-back"></span>b
			</a>
			</div>
		</div>

		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:hasErrors bean="${securityInstance}">
			<div class="errors">
		<g:renderErrors bean="${securityInstance}" as="list"/>
		</div>
		</g:hasErrors>
	
		<div class="main">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
					<span class="ui-jqgrid-title">Report</span>
				</div>
				<form id="searchForm" name="searchForm">
              <input id="hiddenSelectedItem" name="hiddenSelectedItem" type="hidden">
                  <div class="ui-dialog-content ui-widget-content">
      
                   <table>  
 
                     <tr class='prop'>  
                         <td valign='middle' style='text-align:center;' width='10%'>  
                             <label for='name'>HAWB No.:</label>  
                         </td>  
                         <td valign='middle' style='text-align:center;' width='20%'>  
                         <g:textField name="hawb" maxlength="20"/></td>
                             <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='middle' style='text-align:left;' width='20%'>  
                             <g:select id="store" from="${getStoreList()}" name="store" ></g:select> 
     
                         </td> 

                         <td class="name"><g:message code="invoice.co.label" default="CO@CITIS"/></td>
						<td class="value"><g:checkBox name="cocitis"/></td>
			  
                     </tr>
		    <tr  class="prop">
                          <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Year:</label>  
                         </td>  
          <td valign='middle' style='text-align:left;' width='20%'>  
                          <g:select  from="${['this year','2010','2011','2012','2013','2014','2015','2016','2017','2018','2019','2020','2021','2022','2023','2024','2025','2026','2027','2028','2029','2030','2031','2032','2033','2034','2035','2036','2037','2038','2039','2040','2041','2042','2043','2044','2045','2046','2047','2048','2049','2050','2051','2052','2053','2054','2055','2056','2057','2058','2059','2060','2061','2062','2063','2064','2065','2066','2067','2068','2069','2070','2071','2072','2073','2074','2075','2076','2077','2078','2079','2080','2081','2082','2083','2084','2085','2086','2087','2088','2089','2090','2091','2092','2093','2094','2095','2096','2097','2098','2099']}" name="inYear" value="${inYear}">

                          </g:select> 
                         </td> 
                          <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Month:</label>  
                         </td>  
                          <td valign='middle' style='text-align:left;' width='20%'>  
                          <g:select  from="${['this month','1','2','3','4','5','6','7','8','9','10','11','12']}" name="inMonth" value="${inMonth}">

                          </g:select> 
                         </td> 
	    </tr>
                           
                  </table>      
					</div>
				<div class="buttons">
					<span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="Search"/></span>
	  				<!-- <span class="button"><input type="button" class="save" onclick="update()" value="Update"/></span> -->
			        <span class="button"><input type="button" class="save" onclick="exportExcel()" value="Export(in all Years)"/></span>
					 
				</div>
				 <div class="ui-dialog-content ui-widget-content">
      
                   <table>  
 
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>invoiceDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="invoiceDate" name="invoiceDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.invoiceDate)}">
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>receiveDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="receiveDate" name="receiveDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.receiveDate)}">
                         </td>
      					
      					<td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>coCitisImportDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                          <input id="coCitisImportDate" name="coCitisImportDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.coCitisImportDate)}">
                         
                         </td>
                         
                        <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>coCitisExportDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="coCitisExportDate" name="coCitisExportDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.coCitisExportDate)}">
                         </td>
			  
                     </tr>
		    <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>coCitisSendingImportDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="coCitisSendingImportDate" name="coCitisSendingImportDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.coCitisSendingImportDate)}">
                         </td>
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>fromPariseDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="fromPariseDate" name="fromPariseDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.fromPariseDate)}">
                         </td>
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>arriveDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="arriveDate" name="arriveDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.arriveDate)}">
                         </td>
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>baoLongReceiveDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="baoLongReceiveDate" name="baoLongReceiveDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.baoLongReceiveDate)}">
                         </td>
                         
                         </tr>
                         
                         <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>qADuration:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="qADuration" name="qADuration" type="number">
                         </td>
                         
                          <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>dOExchangeDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="dOExchangeDate" name="dOExchangeDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.dOExchangeDate)}">
                         </td>
                         
                          <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>dutyPaidDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="dutyPaidDate" name="dutyPaidDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.dutyPaidDate)}">
                         </td>
                         
                          <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>inspectionDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="inspectionDate" name="inspectionDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.inspectionDate)}">
                         </td>

                         </tr>
                          </tr>
                         
                         <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>vatDutyAmount:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="vatDutyAmount" name="vatDutyAmount" type="number">
                         </td>
                         
                          <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>clearanceDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="clearanceDate" name="clearanceDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.clearanceDate)}">
                         </td>
                         
                          <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>directToStore:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="directToStore" name="directToStore" type="text">
                          </td>
                          
                           <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>warehouseInDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="warehouseInDate" name="warehouseInDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.warehouseInDate)}">
                         </td>

                         </tr>
                         
                         <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>rcvdNumersOfCartons:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="rcvdNumersOfCartons" name="rcvdNumersOfCartons" type="number">
                         </td>
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>damagedCartonNo:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="damagedCartonNo" name="damagedCartonNo" type="text">
                         </td>
                         
                          <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>shortagePcs:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="shortagePcs" name="shortagePcs" type="text">
                         </td>
                     
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>rcvDOInstructionFromStoreDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="rcvDOInstructionFromStoreDate" name="rcvDOInstructionFromStoreDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.rcvDOInstructionFromStoreDate)}">
                         </td>
                         </tr>
                         <tr class='prop'>  
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>requiredDeliveryByStoreDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="requiredDeliveryByStoreDate" name="requiredDeliveryByStoreDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.requiredDeliveryByStoreDate)}">
                         </td>
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>deliveryDate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="deliveryDate" name="deliveryDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.deliveryDate)}">
                         </td>
                         
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>remark:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                         <input id="remark" name="remark" length="300" type="text">
                         </td>
                         
                         
                         </tr>
                           
                  </table>      
					</div>
				<div class="buttons">
					<span class="button"><input type="button" class="save" onclick="gridReloadAfterUpdateSelected()"  action="update" value="Update"/></span>
	  				
				</div>
				
               </form>  
            </div>
            	<table id="grid" width="100%"></table>
	<div id="gridnav"></div>
        
</div>

    </body>
</html>


