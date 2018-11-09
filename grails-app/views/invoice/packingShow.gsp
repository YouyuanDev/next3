<html>
<head>
  <meta name="layout" content="main_blank"/>
  <title>Invoice</title>
  <script type="text/javascript">		
		function loadCartons(){
			var cartons = ${cartons};
			for(carton in cartons){
				Carton(cartons[carton]);
			}
		}
		function addCarton() {
			var carton;
			var url = "${resource(dir:'')}/${controllerName}/updateCarton";
			var packingid = $("#id").val();
			if(packingid){
				$.post(url,{id:packingid},function(data){
						carton=data;
						if(carton){
							Carton(carton);
						}else{
							alert("error");
						}
					}, "text");
			}else{
				alert("packing id is empty");
			}
		}
		function Carton(i){
			var desc1="",desc2="";
			var table = $('div#cartons').append(
				'<div id=grid_wrapper_'+i+'>' +
				'<table id="'+i+'"></table><div id="pager_'+i+'"></div>'+
				'</div>&nbsp;</br>');
			var carton;
			var options = {
				dataType:"json",
				parse: function(data){
					var acd = new Array();
			        for(var i=0;i<data.length;i++){
						acd[acd.length] = { data:data[i], value:data[i].product, result:data[i].product };
			        }
			        return acd;
				},
				formatItem:function(row,i,max,value,term){
					return value;
				},
				formatMatch: function(row, i, max) {
					return row.product + " " + row.enname;
				},
				formatResult: function(row) {
					return row.product;
				}
			};
			var grid = $("#"+i);
			grid.jqGrid({
					url:"${resource(dir:'')}/${controllerName}/listCartonItemsJson/"+i,
					editurl:"${resource(dir:'')}/${controllerName}/updateCartonItem/",
					datatype: "json",mtype: 'POST',
					colNames:[
						"key",
						"cartonItemId",
						"Carton",
						"InvoiceItem",
						"${message(code: 'product.dept.label', default: 'Code')}",
						"${message(code: 'common.action.label', default: 'Dept')}",
						"${message(code: 'product.code.label', default: 'Desc ZH')}",
						"${message(code: 'category.code.label', default: 'Desc EN')}",
						"${message(code: 'carton.quantity.label', default: 'Quantity')}"],
					colModel:[
						{name:'key',hidden:true,editable:true},
						{name:'cartonItemId',hidden:true,editable:true},
						{name:'cartonId',hidden:true,editable:true},
						{name:'invoiceItemId',hidden:true,editable:true},
						{name:'cartonProduct',hidden:false,editable:true,editoptions: {
					            size:20,
					            maxlength:20,
					            dataInit:function (elem) {
				$(elem).autocomplete("${resource(dir:'')}/${controllerName}/listCartonInvoiceItemsJson?cartonId="+i,options).result(
							 		function(e, item){
							 			$("#cartonProduct").val(item.product);
							 			$("#cartonDept").val(item.dept);
							 			$("#cartonDescEn").val(item.enname);
							 			$("#cartonDescZh").val(item.zhname);
							 			$("#cartonQuantity").val(item.quantity);
										$("#cartonId").val(item.cartonId);
										$("#invoiceItemId").val(item.invoiceItemId);
									});     
					          }}},
						{name:'cartonDept',hidden:false,editable:true},
						{name:'cartonDescZh',hidden:false,editable:true},
						{name:'cartonDescEn',hidden:false,editable:true},
						{name:'cartonQuantity',hidden:false,editable:true}],
					pager:'#pager_'+i,
					rowNum:-1,
					//viewrecords:false,
					caption:"",
					autowidth: true,
					height:'auto',//reloadAfterSubmit:true,
					toolbar: [true,"top"]
					//multiselect:false,rownumbers:true,rownumWidth:40
			});
			grid.jqGrid('navGrid', '#pager_'+i, {view:false,del:false,edit:true,add:true,search:false});
			//grid.delGridRow("rowid",{delData:{myname:"myvalue"}});
			grid.jqGrid('navButtonAdd', '#pager_'+i, {
				caption: "Update",
				title: "Update Carton",
				onClickButton : function () {
					$('#dialogCartonId').val(i);
					$('#dialog').dialog('open');
				}
			});
			grid.jqGrid('navButtonAdd', '#pager_'+i, {
				caption: "DelItem",
				onClickButton : function (id) {
					var gsr = grid.jqGrid('getGridParam','selrow');
		            if(gsr){
		                var data = grid.jqGrid('getRowData',gsr);
		                var cartonItemId = data.cartonItemId;					
						var url = "${resource(dir:'')}/${controllerName}/updateCartonItem";
						$.post(url,{cartonItemId:cartonItemId,oper:'del'},function(data){
								if(data==="success"){
									//TODO: delete success
								}
								grid.trigger("reloadGrid"); 
						}, "text");
					}else{
						alert("please select item to delete!")
					}
				}
			});
			grid.jqGrid('navButtonAdd', '#pager_'+i, {
				caption: "DelCarton",
				onClickButton : function (id,rowId) {
					var ret = grid.getRowData(id);
					var url = "${resource(dir:'')}/${controllerName}/deleteCarton";
					$.post(url,{id:i},function(data){
							if(data==="success"){
								$("#grid_wrapper_"+i).remove();
							}
					}, "text");
				}
			});
			$.get("${resource(dir:'')}/${controllerName}/showCarton/"+i, null, function(data){   
			        var json = data;    
			       	//alert(json.desc1);
					$("#t_"+i).append("<strong>&nbsp;"+json.desc1+" "+json.desc2+"</strong>"); 
			    },"json");			
			
		}		
		$(document).ready(function(){ 
			$("input#addCarton").click(function(){
				addCarton(this,1);
			});
			//var data = "Core Selectors Attributes Traversing Manipulation CSS Events Effects Ajax Utilities".split(" ");
			var options1 = {
				dataType:"json",
				parse: function(data){
					var acd = new Array();
			        for(var i=0;i<data.length;i++){
						acd[acd.length] = { data:data[i], value:data[i].code, result:data[i].code };
						//alert(data[i].code);
			        }
			        return acd;
				},
				formatItem:function(row,i,max,value,term){
					return value;
				},
				formatMatch: function(row, i, max) {
					return row.code;
				},
				formatResult: function(row) {
					return row.code;
				}
			};			
			$("#bpCode").autocomplete("${resource(dir:'')}/${controllerName}/listBusinessCodeJson",options1);		
			
			$("#dialog").dialog({
				bgiframe: true,
				autoOpen: false,
				modal: true,
				buttons: {
					'Submit': function() {
						var query = $('#cartonForm').formSerialize(); 
						var url = "${resource(dir:'')}/${controllerName}/updateCarton?"+query;
						$.post(url,{},function(data){
							//alert(data);	
						}, "text");
						$(this).dialog('close');
					},
					Cancel: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
				}
			});
		
			loadCartons();
		});
  </script>
</head>
<body>
<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
  <div class="fg-buttonset ui-helper-clearfix">
    <g:link class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" action="list" title="Search">
      <span class="ui-icon ui-icon-search"></span>Search
    </g:link>
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
      <span class="ui-jqgrid-title">Packing</span>
    </div>
		<g:form method="post">
		<g:hiddenField name="id" value="${fieldValue(bean: instance, field: 'id')}"/>
    <div class="ui-dialog-content ui-widget-content">
      <table>
        <tbody>
        <tr class="prop">
          <td class="name"><g:message code="business.code.label" default="BP Code"/></td>
          <td class="value">
			<g:if test="instance">
				${instance?instance.business.code:''}
			</g:if>
			<g:else>
				<g:textField name="bpCode" maxlength="50" value=""/>
			</g:else>
		  </td>
          <td class="name"><g:message code="business.reInvoice.label" default="Re Invoice"/></td>
          <td class="value">${reInvoiceNumbers?reInvoiceNumbers:''}</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="packing.hardcopy.label" default="Hard Copy"/></td>
          <td class="value"><g:textField name="hardCopy" maxlength="50" value="${fieldValue(bean: instance, field:'hardCopy')}"/></td>
          <td class="name"><g:message code="packing.date.label" default="Packing Date"/></td>
          <td class="value"><g:formatDate date="${fieldValue(bean: instance, field: "date")}" format="yyyy-MM-dd"/></td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="packing.description.label" default="Description"/></td>
          <td class="value"><g:textField name="description" maxlength="50" value="${fieldValue(bean: instance, field:'description')}"/></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" action="updatePacking" value="${message(code: 'default.button.update.label', default: 'Save')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="export" value="${message(code: 'default.button.save.label', default: 'Export')}"/></span>
			</g:form>
			<g:if test="${instance}">
      <span class="button"><input type="button" id="addCarton" class="delete" action="add" value="${message(code: 'default.button.add.label', default: 'Add')}"/></span>
			</g:if>
    </div>
  </div>
	
  <div id="dialog" title="${message(code: 'carton.select.label', default: 'Carton Item Select')}">
    <form id="cartonForm" action="${resource(dir:'')}/${controllerName}/updateCarton" method="post">
      <fieldset>
        <input type="hidden" name="dialogCartonId" id="dialogCartonId" value=""/>
        <label for="name">${message(code: 'carton.description1.label', default: 'Description1')}</label>
        <input type="text" name="description1" id="description1" class="text ui-widget-content ui-corner-all"/>
        <label for="name">${message(code: 'carton.description2.label', default: 'Description2')}</label>
        <input type="text" name="description2" id="description2" class="text ui-widget-content ui-corner-all"/>
      </fieldset>
    </form>
  </div>
	<div id="cartons">
	</div>
</div>
</body>
</html>
