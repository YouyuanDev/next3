<html>
<head>
<meta name="layout" content="main_blank"/>
<script type="text/javascript">
	function resize_the_grid(){
	  $('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
	}
	var grid;
    var caption = "";var rowNum = 20;
    var listurl = "${resource(dir:'')}/${controllerName}/listPOItemToJson";
    var colNames = ["Id","BP","ReInvoice","PO","SO","Delivery Date","ToScala","Supplier","Customer","WH","LOC","Export","Valid"];
    var colModel = [
      {name:'id',index:'id',hidden:true,sortable:false},
      {name:'code',index:'code',formatter:linkformatter,sortable:false},
      {name:'invoice',index:'reInvoice',sortable:false},
      {name:'po',index:'po',sortable:false},
      {name:'so',index:'so',sortable:false},
      {name:'date',index:'date',sortable:true},
      {name:'export',index:'supplier',sortable:false},
      {name:'supplier',index:'supplier',sortable:false},
      {name:'customer',index:'customer',sortable:false},
      {name:'wh',index:'wh',sortable:false},
      {name:'loc',index:'loc',sortable:true},
      {name:'export',index:'export',sortable:false},
      {name:'valid',index:'valid',sortable:false}
    ];
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showPO/" + rowObject[0]
	   + "'>" + cellvalue + "</a>"
      return linkurl;
    }
	var navOptions = {view:false,del:false,edit:false,add:false,search:false};
	jQuery(document).ready(function() {
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,false,true);
		$("#reInvoice").autocomplete('${resource(dir:'')}/${controllerName}/listBusinessJson', {
			multiple: true,dataType: "json",mtype: 'GET',
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

		$("#startDate").datepicker({ dateFormat: 'yy-mm-dd' });
		$("#endDate").datepicker({ dateFormat: 'yy-mm-dd' });
		//alert(document.getElementById('jqp_0').checked)
		//$("#cb_grid").attr("checked", "checked");
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
		var date1 = document.searchForm.startDate.value;
		var date2 = document.searchForm.endDate.value;
		 
		var url = "${resource(dir:'')}/interface/exportPOItem"+"?date1="+date1+"&date2="+date2;
		window.location.href=url
		//window.open(url); 
		//self.focus();
	}
	
	function exportPO(){
		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}
		var url = "${resource(dir:'')}/interface/exportPO"+"?items="+items;
		window.open(url); 
		self.focus();
	}	
	
	function createPO(){
		var selects = grid.jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
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
	<form name="searchForm" id="searchForm" method="GET" >
	<div class="ui-dialog-content ui-widget-content">
	  <table>
	    <tbody>
	    <tr class="prop">
	      <td class="name">Start Date</td>
	      <td class="value"><input id="startDate" name="startDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:startDate)}"></td>
	      <td class="name">End Date</td>
	      <td class="value"><input id="endDate" name="endDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:endDate)}"></td>
	    </tr>

	    </tbody>
	  </table>
	</div>
	<div class="buttons">
	  <span class="button"><input type="button" class="save" onclick="gridReload()" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
		<span><a href="javascript:exportProduct();" class="save">ExportItems</a></span>
	</div>
	</form>
	<form name="poForm" id="poForm" method="GET" action="${resource(dir:'')}/${controllerName}/createPO">
	</form>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
</div>
</body>
</html>
