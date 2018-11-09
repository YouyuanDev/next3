<html>
<head>
<meta name="layout" content="main_blank"/>
<script type="text/javascript">
	function resize_the_grid(){
	  $('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
	}
	var grid;
    var caption = "";var rowNum = 20;
    var listurl = "${resource(dir:'')}/${controllerName}/listPOsJson";
    var colNames = ["Id","BP","ReInvoice","PO","Create Date","ToScala","Supplier","Customer","WH","LOC","Packing Export","Packing Valid","Shipment Submit"];
    var colModel = [
      {name:'id',index:'id',hidden:true,sortable:false},
      {name:'code',index:'code',formatter:linkformatter,sortable:true},
      {name:'invoice',index:'reInvoice',sortable:true},
      {name:'po',index:'po',sortable:false},
      //{name:'so',index:'so',sortable:false},
      {name:'date',index:'deliveryDate',sortable:true},
      {name:'export',index:'supplier',sortable:false},
      {name:'supplier',index:'supplier',sortable:false},
      {name:'customer',index:'customer',sortable:false},
      {name:'wh',index:'wh',sortable:false},
      {name:'loc',index:'loc',sortable:true},
      {name:'export',index:'export',sortable:false},
      {name:'valid',index:'valid',sortable:false},
      {name:'shipmentSubmit',index:'submt',sortable:true}
    ];
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showPO/" + rowObject[0]
	   + "'>" + cellvalue + "</a>"
      return linkurl;
    }
	var navOptions = {view:false,del:false,edit:false,add:false,search:false};
	jQuery(document).ready(function() {
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,true,true);
		$("#reInvoice").autocomplete('${resource(dir:'')}/${controllerName}/listBusinessJson', {
			multiple: true,dataType: "json",
			parse: function(data) {
				return $.map(data, function(row) {
					return {
						data:row,value:row.code,result:row.desc
					}
				});
			},
			formatItem:formatItem,
			formatResult: formatResult
		});
		$("#reInvoice").result(function(event, data, formatted) {
			var hidden = $('#reInvoiceHidden');
			hidden.val(data.code);
		});
		$("#exportDate").datepicker({ dateFormat: 'yy-mm-dd' });
	});
	function formatItem(row) {
		return row.desc;
	}
	function formatResult(row) {
		return row[0].replace(/(<.+?>)/gi,'');
	}
	$(window).resize(resize_the_grid);
    function gridReload(){
		var query = $('#searchForm').formSerialize(); 
		grid.jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid"); 
    }  
	function exportProduct(){
		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}
		if(items==null||items==''){
			alert("nothing to export")
			return;
			}
		var url = "${resource(dir:'')}/interface/exportPOProduct"+"?items="+items;
		window.location.href=url
		//window.open(url); 
		//self.focus();
	}

	function SubmitShipment(){
		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}
		//alert(items)
		if(items==null||items==''){
			alert("nothing to submit")
			return;
			}
		 
		
		var url = "${resource(dir:'')}/${controllerName}/submitShipment"+"?items="+items;
		 if (confirm("Do you want to submit this shipment?")){
			 document.forms["poForm"].action = url;
				document.forms["poForm"].submit();
			        	 
	        }


	}


	
	function UndoShipment(){

		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}
		//alert(items)
		if(items==null||items==''){
			alert("nothing to undo")
			return;
			}
		 
		
		var url = "${resource(dir:'')}/${controllerName}/undoShipment"+"?items="+items;
		 if (confirm("Do you want to undo this shipment?")){
			 document.forms["poForm"].action = url;
				document.forms["poForm"].submit();
			        	 
	        }
		

	}
	
	function exportPO(){
		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}
		//alert(items)
		if(items==null||items==''){
			alert("nothing to export")
			return;
			}
		var orderdate=	document.searchForm.exportDate.value;
		
		var url = "${resource(dir:'')}/interface/exportPO"+"?items="+items+"&orderdate="+orderdate;
		//return;
		window.location.href=url
		//window.open(url); 
		//self.focus();
	}	
	
	function createPO(){
		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id
			
			items.push(item);
		}
		if(items==null||items==''){
			alert("nothing to createPO ")
			return;
			}
		var url = "${resource(dir:'')}/${controllerName}/createPO"+"?items="+items;
		document.forms["poForm"].action = url;
		document.forms["poForm"].submit();
		//gridReload();
		//window.open(url); 
		//self.focus();
	}	
</script>
</head>
<body>
	
<div class="main">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">Purchase Order</span>
  </div>
	<form name="searchForm" id="searchForm">
	<div class="ui-dialog-content ui-widget-content">
	  <table>
	    <tbody>
	    <tr class="prop">
	      <td class="name">Purchase Order</td>
	      <td class="value"><g:textField name="poCode"/></td>
	      <td class="name"><!-- Sales Order --></td>
	      <td class="value"><!-- <g:textField name="soCode"/>--></td>
	    </tr>
		<!--
	    <tr class="prop">
	      <td class="name">Supplier Code</td>
	      <td class="value">
				<g:radio name="suppier" value="1800" checked="true"/>LMH
				<g:radio name="suppier" value="1200"/>GULI
				<g:radio name="suppier" value="1100"/>BL
				<g:radio name="suppier" value="1500"/>HZ
		  </td>
	      <td class="name">Customer Code</td>
	      <td class="value"><g:textField name="customer"/></td>
	    </tr>
		-->
	    <tr class="prop">
	      <td class="name">BP</td>
	      <td class="value"><g:textField name="bpCode"/></td>
	      <td class="name">Re Invoice</td>
	      <td class="value">
		    <g:textField name="reInvoice" id="reInvoice"/>
			<input id="reInvoiceHidden" name="reInvoiceHidden" type="hidden">
		  </td>
	    </tr>
	     <tr class="prop">
	      <td class="name">Delivery Year</td>
	      <td class="value">
	      <g:select  from="${['this year','2010','2011','2012','2013','2014','2015','2016','2017','2018','2019','2020','2021','2022','2023','2024','2025','2026','2027','2028','2029','2030']}" name="inYear" value="${inYear}">
			</g:select> </td>
	      <td class="name">Delivery Month</td>
	      <td class="value">
		     <g:select  from="${['this month','1','2','3','4','5','6','7','8','9','10','11','12']}" name="inMonth" value="${inMonth}">
			 </g:select> 
		  </td>
	    </tr>
	     <tr class="prop">
	    <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='middle' style='text-align:left;' width='20%'>  
                             <g:select id="store" from="${getStoreList()}" name="store" ></g:select> 
     
                         </td> 
	    </tr>
	    <tr class="prop">
	      <td class="name">Others</td>
	      <td class="value">
			export:<g:checkBox name="export" />
	      </td>
	       
	      <td class="name">Input export excel date</td>
	      <td class="value"><input id="exportDate" name="exportDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.exportDate)}">
	       
	      </td>
	    </tr>
	     <tr class="prop">
	      <td class="name">shipment submit</td>
	      <td class="value">
	      <g:select  from="${['Yes','No','Not Exist']}" name="shipmentsubmit" value="${shipmentsubmit}">
			 </g:select> 
	      </td>
	      <td >
			 
	      </td>
	      </tr>
	    </tbody>
	  </table>
	</div>
	<div class="buttons">
	  <span class="button"><input type="button" class="save" onclick="gridReload()" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
	  <span class="button"><input type="button" class="save" onclick="createPO()" value="CreatePO"/></span>
	   <span class="button"><input type="button" class="save" onclick="UndoShipment()" value="Undo Shipment"/></span>
	   <span class="button"><input type="button" class="save" onclick="SubmitShipment()" value="Submit Shipment"/></span>
		<span><a href="javascript:exportPO();" class="save">ExportPO</a></span>
		<span><a href="javascript:exportProduct();" class="save">ExportProducts</a></span>
	</div>
	</form>
	<form name="poForm" id="poForm" method="POST" action="${resource(dir:'')}/${controllerName}/createPO">
	</form>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
</div>
</body>
</html>
