<html>
<head>
	<meta name="layout" content="main_blank"/>
	<title>Carton</title>
    <script type="text/javascript">
    	jQuery(document).ready(function() {

    		var lastsel;
			jQuery("#grid").jqGrid({
				 
				url:"${resource(dir:'')}/${controllerName}/listCartonProductItemsJson/${instance?instance.id:''}",
				
				editurl:"${resource(dir:'')}/${controllerName}/updateItemPackingQty/", //Kurt Added
				
				datatype: "json",
				mtype: 'POST', 
				colNames:["Id","Invoice","Product","Dept","Zh","En","Qty"],
				colModel:[
					{name:'itemId',index:'itemId',hidden:true,editable:false},
					{name:'invoice',hidden:false,editable:false},
					{name:'product',hidden:false,editable:false,sortable:true},
					{name:'dept',hidden:false,editable:false,sortable:true},//Kurt Edited
					{name:'zh',hidden:false,editable:false},
					{name:'en',hidden:false,editable:false},
					{name:'qty',sortable:false,editable:true}//Kurt Edited
				],
				pager: '#gridnav', 
				rowNum:-1,
				viewrecords: true,//Kurt Updated 
				autowidth: true,
				multiselect: true, //Kurt Updated 
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
		});
		
		function saveCartonDet(){
			var selects = jQuery("#grid").jqGrid('getGridParam','selarrrow'); 
			var items = [];
			for(var i in selects){
				var rowNum = selects[i];
				var item = $("#grid").getRowData(rowNum).itemId;
				var qty=$("#grid").getRowData(rowNum).qty;//Kurt added
				items.push(item+"-"+qty);//Kurt added 可编辑的
			}
			var query = $('#searchForm').formSerialize(); 
			jQuery("#grid").jqGrid('setGridParam',{url:"${resource(dir:'')}/${controllerName}/updateCartonDet"+"?"+query+"&items="+items,page:1}).trigger("reloadGrid");
			
			window.parent.closeDialog();
	
		}
		
	    function gridReload(){
			var query = $('#searchForm').formSerialize(); 
jQuery("#grid").jqGrid('setGridParam',{url:"${resource(dir:'')}/${controllerName}/listCartonProductItemsJson"+"?"+query,page:1}).trigger("reloadGrid"); 

}  
	</script>
</head>
<body>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<g:hasErrors bean="${securityInstance}">
  <div class="errors">
    <g:renderErrors bean="${securityInstance}" as="list"/>
  </div>
</g:hasErrors>

<div class="main">
	<form id="searchForm" method="POST" action="${resource(dir:'')}/${controllerName}/updateCartonDet">
	  <g:hiddenField name="id" value="${instance?instance.id:''}"/>
	  <div class="ui-dialog-content ui-widget-content">
	    <table>
	      <tbody>
		  <!--
	      <tr class="prop">
			<td class="name">Hard Copy</td>
	        <td class="value"><g:textField name="hardCopy" maxlength="50" value="{fieldValue(bean: instance, field:'hardCopy')}"/></td>
	      </tr>
		  -->
	      <tr class="prop">
	        <td class="name">Product</td>
	        <td class="value"><g:textField name="prodCode" maxlength="10" value=""/></td>
	      </tr>
	      <tr class="prop">
	        <td class="name">NWeight</td>
	        <td class="value"><g:textField name="nWeight" maxlength="10" value="${instance.nWeight}"/></td>
	        <td class="name">GWeight</td>
	        <td class="value"><g:textField name="gWeight" maxlength="10" value="${instance.gWeight}"/></td>
	      </tr>
	      </tbody>
	    </table>
	  </div>
	  <div class="buttons">
		<span class="button"><input id="searchbutton" type="button" class="save" onclick="gridReload();" value="Search"/></span>
		<span class="button"><input type="button" class="save" onclick="saveCartonDet();" value="${message(code: 'default.button.update.label', default: 'Save Carton')}"/></span>
	  </div>
	</form>
	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
</html>
