<html>
<head>
<meta name="layout" content="main_blank"/>
	<script type="text/javascript">
		function resize_the_grid(){
			$('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
		}
		//var grid;   
	    var caption = "";
	    var listurl = "${resource(dir:'')}/${controllerName}/listBpInvoicesJson";
	    var editurl = "${resource(dir:'')}/${controllerName}/listBpInvoicesJson";
	  	
	    var colNames = [
		  "Key",
	      "${message(code: 'invoice.bpcode.label', default: 'BP Code')}",
	      "${message(code: 'invoice.code.label', default: 'Invoice Code')}",
	      "${message(code: 'invoice.reinvoice.label', default: 'Re Invoice')}",
	      "${message(code: 'invoice.invoiceDate.label', default: 'Invoice Date')}"
	    ];
	    var colModel = [
	      {name:'key', index:'key',hidden:true},
	      {name:'business.code',index:'business.code',width:130,formatter:linkformatterbp,sortable:true},
	      {name:'invoice.code',index:'invoice.code',width:170,sortable:true},
	      {name:'invoice.reInvoice',index:'invoice.reInvoice',width:130,sortable:true},
	      {name:'invoice.invoiceDate',index:'invoice.invoiceDate',width:130,sortable:true}
	    ];
		var navOptions = {view:false,del:false,edit:false,add:false,search:false};
	    function linkformatterbp(cellvalue, options, rowObject)
	    {
	      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showBP/" + cellvalue + "'>" + cellvalue + "</a>"
	      return linkurl;
	    }
	    function linkformatterinvoice(cellvalue, options, rowObject)
	    {
	      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showInvoice/" + cellvalue + "'>" + cellvalue + "</a>"
	      return linkurl;
	    }
	    jQuery(document).ready(function() {
			var lastsel;
			//grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,false,false);
			jQuery("#grid").jqGrid({
					caption:"",
					url:listurl,editurl:editurl,delurl:editurl,datatype: "json",mtype: 'GET',
					colNames:colNames,colModel :colModel,pager: '#gridnav',
					viewrecords: true,rowNum:20,//rowList:[10,20,30],sortname: 'code',sortorder: 'code',
					autowidth: true,height:'auto',multiselect:false,rownumbers:true,rownumWidth:40 
			});
			jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:false,edittext:'Modify'},{addCaption: "Add Record",
			     editCaption: "Edit InvoiceItem",
			     bSubmit: "Save",
			     bCancel: "Cancel",
			     bClose: "Close",
			     saveData: "Data has been changed! Save changes?",
			     bYes : "Yes",
			     bNo : "No",
			     bExit : "Cancel"},{},{},{multipleSearch : true},{closeOnEscape:true});
			$('#searchForm').ajaxForm(); 
	    });
	    $(window).resize(resize_the_grid);
	    function gridReload(){
			var query = $('#searchForm').formSerialize(); 
			jQuery("#grid").jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid");
	    }  

	    function export_invoice_packing(){
	    	var store=document.getElementById("store").value
	    	var bpCode=document.getElementById("bpCode").value
	    	var invoiceCode=document.getElementById("invoiceCode").value
	    	var hawb=document.getElementById("hawb").value
			var inYear=document.getElementById("inYear").value
			var inMonth=document.getElementById("inMonth").value
			var IsemptyHawb=document.getElementById("emptyHawb").checked
			var emptyHawb=0
			// alert(store)
			// alert(inYear)
			// alert(cocitis)
			if(IsemptyHawb)
				emptyHawb=1;

		    
	    	 var url="${resource(dir:'')}/${controllerName}/exportInvoicePackingPerHawb"+"?bpCode="+bpCode+"&invoiceCode="+invoiceCode+"&hawb="+hawb+"&emptyHawb="+emptyHawb+"&store="+store+"&inYear="+inYear+"&inMonth="+inMonth
			
 			window.location.href=url
              
		    }
	    function DeleteOldBP(){

	    	var url="${resource(dir:'')}/${controllerName}/deleteOLDBP"
	    		window.location.href=url

		    }
	    
	    function export1(){

	   	 var url="${resource(dir:'')}/${controllerName}/exportAllBPReinvoiceData"
		window.location.href=url

	   }
	</script>
</head>
<body>
<div class="main">
	<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
		<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
		  <span class="ui-jqgrid-title">${message(code: 'invoice.invoice.label', default: 'Invoice')}</span>
		</div>
		<form name="searchForm" id="searchForm">
		<div class="ui-dialog-content ui-widget-content">
		  <table>
		    <tbody>
		    <tr class="prop">
		      <td class="name"><g:message code="business.code.label" default="BP Code"/></td>
		      <td class="value"><g:textField name="bpCode"/></td>  		    
		      <td class="name"><g:message code="invoice.code.label" default="Invoice Code"/></td>
		      <td class="value"><g:textField name="invoiceCode"/></td> 
		      
		    </tr>
		    <tr class="prop">
		    <td class="name"><g:message code="invoice.code.label" default="Hawb"/></td>
		      <td class="value"><g:textField name="hawb"/></td> 
		    </tr>
		    <tr class="prop">
		    <td class="name"><g:message code="store.code.label" default="Store"/></td>
		       <td valign='middle' style='text-align:left;' width='20%'>  
                             <g:select id="store" from="${getStoreList()}" name="store" ></g:select> 
                         </td> 
   
		    <td class="name"><g:message code="invoice.co.label" default="Empty Hawb"/></td>
			<td class="value"><g:checkBox name="emptyHawb" /></td>
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
                          <g:select  from="${['this month','1','2','3','4','5','6','7','8','9','10','11','12','All']}" name="inMonth" value="${inMonth}">

                          </g:select> 
                         </td> 
	    </tr>
		    
<!-- 		    <tr class="prop">
		      <td class="name"><g:message code="category.code.label" default="Product Short"/></td>
		      <td class="value"><g:textField name="categoryCode"/></td>
		      <td class="name"><g:message code="product.code.label" default="Product Full"/></td>
		      <td class="value"><g:textField name="productCode"/></td>
		    </tr>
		    <tr class="prop">
		      <td class="name"><g:message code="product.dept.label" default="Dept"/></td>
		      <td class="value"><g:textField name="productDept"/></td>
		      <td class="name"><g:message code="product.material.label" default="Material"/></td>
		      <td class="value"><g:textField name="productMaterial"/></td>
		    </tr>
		    <tr class="prop">
		      <td class="name"><g:message code="common.sort.label" default="Sort"/></td>
		      <td class="value">
		        <g:radioGroup name="searchSort" labels="['Dept','Material','Product']" values="['dept','material','product']" value="Dept">
		         ${it.radio} ${it.label} 
		        </g:radioGroup>
		      </td>
		      <td class="name"></td>
		      <td class="value"></td>
		    </tr> -->
		    </tbody>
		  </table>
		</div>
		<div class="buttons">
		  <span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
		  <span class="button"><input type="button" class="save" onclick="export_invoice_packing()" value="Export_Invoice&Packing"/></span>
		  
		  <span class="button"><input type="button" class="save" onclick="export1()" value="Export All"/></span><%--
		  <span class="button"><input type="button" class="save" onclick="DeleteOldBP()" value="DeleteOldBP"/></span>
		--%></div>
		</form>
	</div>
	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
</html>
