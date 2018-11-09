

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'invoiceHeader.label', default: 'InvoiceHeader')}" />
        <title>Report Details</title>
        
         <script type="text/javascript">
  function pickdates(id){ jQuery("#"+id+"_sdate","#rowed6").datepicker({dateFormat:"yy-mm-dd"}); } 
  function resize_the_grid(){
		$('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
	}
	//var grid;   
  var caption = "";
  var editurl = "" 
  var listurl = "${resource(dir:'')}/${controllerName}/listDeliveryReport";
  var colNames = [
                  "Dept",
        		  "Podium",
        	      "Qty"
        	    ];
        	    var colModel = [
			{name:'dept',hidden:false,editable:true},
			{name:'podium',hidden:false,editable:false},
			{name:'qty',hidden:false,editable:false}
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
			rowNum:-1,
			viewrecords: false,//Kurt Updated 
			autowidth: true,
			multiselect: false, //Kurt Updated 
			height:'auto',
			rownumbers:true,
			//Kurt Added
			 onSelectRow: function(id) {
				if (id) {
					jQuery('#grid').jqGrid('restoreRow', lastsel);
					jQuery('#grid').jqGrid('editRow', id, true);
					lastsel = id;
				}
			}
		});
		jQuery("#grid").jqGrid('navGrid',"#gridnav",{view:false,edit:false,add:false,del:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true}); 
		
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
	/*	var datas=jQuery("#grid").jqGrid('getDataIDs');
		var invoiceIds = [];
		for(var i in datas){
			var rowNum = datas[i];
			var item = $("#grid").getRowData(rowNum).id;
			invoiceIds.push(item);
		}*/
		var store=document.getElementById("store").value
		var iscocitis=document.getElementById("cocitis").checked
		var cocitis=0
		// alert(store)
		// alert(inYear)
		// alert(cocitis)
		if(iscocitis)
			cocitis=1;
		//alert(cocitis)
		var url = "${resource(dir:'')}/interface/exportDeliveryReport"+"?store="+store+"&cocitis="+cocitis;
		 
	/*	if(invoiceIds==null||invoiceIds=='')
			{
		alert("no data to export!");
			}
		else
			{*/
			window.location.href=url;
		//	}
		
		
		//self.focus();  
	}	
	
 

	 function gridReload(){
		 var query = $('#searchForm').formSerialize(); 
			jQuery("#grid").jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid");
	    }  
  </script>  
        
    </head>
    <body>
    		<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/report7/" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-refresh"></span>r
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
                  <div class="ui-dialog-content ui-widget-content">
      
                   <table  class="userForm">  
 
                     <tr class='prop'>  
                         <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='middle' style='text-align:left;' width='20%'>  
                             <g:select id="store" from="${getStoreListWithoutAll()}" name="store" ></g:select> 
     
                         </td> 
			 <td class="name"><g:message code="invoice.co.label" default="CO@CITIS"/></td>
						<td class="value"><g:checkBox id="cocitis" name="cocitis"/></td>
                         <td valign=''middle'' style='text-align:left;' width='20%'>  
                         
                         </td> 
			   <td valign='middle' style='text-align:left;' width='10%'>  
                             
                         </td>  
                         <td valign='middle' style='text-align:left;' width='20%'>  
                           
                         </td> 
                     </tr>
		   
                           
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="Search"/></span>
 
			        <span class="button"><input type="button" class="save" onclick="exportExcel()" value="Export"/></span>
				</div>
               </form>  
            </div>
<table id="grid"></table>
	<div id="gridnav"></div>
</div>
    </body>
</html>
