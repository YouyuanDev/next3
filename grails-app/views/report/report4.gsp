

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
  var editurl = "" //"${resource(dir:'')}/${controllerName}/updateImportReport";
  var listurl = "${resource(dir:'')}/${controllerName}/listKBReport";
  var colNames = [
                  "Id",
        		  "Store",
        	      "Month",
        	      "Invoice",
        	      "Reference",
        	      "Description",
        	      "Kelly/Birkin",
        	      "Qty",
        	      "CO&CITIS"
        	    ];
        	    var colModel = [
			{name:'id',hidden:true,editable:true},
			{name:'store',hidden:false,editable:false},
			{name:'month',hidden:false,editable:false},
			{name:'Invoice',hidden:false,editable:false,sortable:false},
			{name:'Reference',hidden:false,editable:false},
			{name:'Description',hidden:false,editable:false},
			{name:'KellyORBirkin',hidden:false,editable:false,sortable:false},
			{name:'Qty',hidden:false,editable:false,sortable:false},
			{name:'CO&CITIS',hidden:false,editable:false,sortable:false}
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
			rowNum:50,
			viewrecords: true,//Kurt Updated 
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
		/*var datas=jQuery("#grid").jqGrid('getDataIDs');
		var invoiceIds = [];
		for(var i in datas){
			var rowNum = datas[i];
			var item = $("#grid").getRowData(rowNum).id;
			invoiceIds.push(item);
		}*/
		//var url = "${resource(dir:'')}/interface/exportKBReport"+"?invoiceIds="+invoiceIds;
		/*if(invoiceIds==null||invoiceIds=='')
			{
		alert("no data to export!");
			}
		else
			{
			window.location.href=url;
			}*/
			var store=document.getElementById("store").value
			var inYear=document.getElementById("inYear").value
			var inMonth=document.getElementById("inMonth").value
			 //alert(inYear)
			 
			//alert(inMonth)
			var url = "${resource(dir:'')}/interface/exportKBReport"+"?store="+store+"&inYear="+inYear+"&inMonth="+inMonth;
			window.location.href=url;
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
			<a href="${resource(dir:'')}/${controllerName}/report4/${params?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
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
                  <div class="ui-dialog-content ui-widget-content">
      
                   <table  class="userForm">  
 
                     <tr class='prop'>  
                         <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='middle' style='text-align:left;' width='20%'>  
                             <g:select from="${getStoreList()}" name="store" ></g:select> 
     
                         </td> 
			 <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Year:</label>  
                         </td>  
                         <td valign=''middle'' style='text-align:left;' width='20%'>  
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
