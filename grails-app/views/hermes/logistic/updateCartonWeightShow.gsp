<html>
<head>
	<meta name="layout" content="main_blank"/>
	<title>Carton Weight</title>
    <script type="text/javascript">
    	jQuery(document).ready(function() {
            //alert("start");
    		var lastsel;
			jQuery("#grid").jqGrid({
				 
				url:"${resource(dir:'')}/${controllerName}/listCartonWeightListJson/${instance?instance.id:''}",
				
				editurl:"${resource(dir:'')}/${controllerName}/updateItemPackingQty/", //Kurt Added
				
				datatype: "json",
				mtype: 'POST', 
				colNames:["cartonId","Carton","nweight","gweight"],
				colModel:[
					{name:'cartonId',index:'cartonId',hidden:true,editable:false},
					{name:'code',hidden:false,editable:false},
					{name:'nweight',hidden:false,editable:true,sortable:false},
					{name:'gweight',hidden:false,editable:true,sortable:false}//Kurt Edited
				],
				pager: '#gridnav', 
				rowNum:1000,
				viewrecords: true,//Kurt Updated 
				autowidth: true,
				multiselect: false, //Kurt Updated 
				height:'auto',
				rownumbers:false,
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
			//var selects = jQuery("#grid").jqGrid('getGridParam','selarrrow'); 
			var items = [];
		 
			for(var i=0;i<=1000;i++){
				
				var cartonId = $("#grid").getRowData(i).cartonId;
				if(cartonId==null)break;
				var nweight=$("#grid").getRowData(i).nweight;//Kurt added
				var gweight=$("#grid").getRowData(i).gweight;//Kurt added
				items.push(cartonId+"-"+nweight+"-"+gweight);//Kurt added 可编辑的
				if(nweight==''||gweight==''||isNaN(nweight)||isNaN(gweight))
				{
                   alert("Please input numbers or press 'enter' after makeing changes");
                   return;
				}
			}
			 //alert(items);
			var query = $('#searchForm').formSerialize(); 
		    jQuery("#grid").jqGrid('setGridParam',{url:"${resource(dir:'')}/${controllerName}/editCartonWeights"+"?"+query+"&items="+items,page:1}).trigger("reloadGrid");
		    gridReload();
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
	<form id="searchForm" method="POST" action="">
	  <g:hiddenField name="id" value="${instance?instance.id:''}"/>
	   <!--
	  <div class="ui-dialog-content ui-widget-content">
	   
	    <table>
	      <tbody>
		 
	      <tr class="prop">
			<td class="name">Hard Copy</td>
	        <td class="value"><g:textField name="hardCopy" maxlength="50" value="{fieldValue(bean: instance, field:'hardCopy')}"/></td>
	      </tr>
		 
	      <tr class="prop">
	        <td class="name">Product</td>
	        <td class="value"><g:textField name="prodCode" maxlength="10" value=""/></td>
	      </tr>
	     
	      </tbody>
	    </table>
	    
	  </div>
	   -->
	  <div class="buttons">
	   <!--
		<span class="button"><input id="searchbutton" type="button" class="save" onclick="gridReload();" value="Search"/></span>
		 -->
		<span class="button"><input type="button" class="save" onclick="saveCartonDet();" value="${message(code: 'default.button.Save.label', default: 'Save Weight')}"/></span>
	  </div>
	</form>
	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
</html>
