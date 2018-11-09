<html>
<head>
<meta name="layout" content="main_blank"/>
<title>Purchase Order</title>
<script type="text/javascript">
	var grid;
    var caption = "Details"; var rowNum = 200;
    var listurl = "${resource(dir:'')}/${controllerName}/listPOItemsJson/${fieldValue(bean: instance, field: "id")}";
    var editurl = "${resource(dir:'')}/${controllerName}/updatePO";
    //var colNames = ["Id","Invoice","ReInv","Short","Dept","Desc","Qty","Curr","UP(RMB)","AMT(RMB)"];
	var colNames = ["Id","PO","Long","Dept","Magnitude","Desc","Qty","Curr","UP(RMB)","AMT(RMB)"];
    var colModel = [
      {name:'id',hidden:true,editable:false,sortable:false},
      //{name:'invoice',hidden:false,editable:false,sortable:false},
      //{name:'reInv',hidden:false,editable:false,sortable:false},
      {name:'PO',hidden:false,editable:false,sortable:false},
      {name:'Long',hidden:false,editable:false,sortable:false},
      {name:'dept',hidden:false,editable:false,sortable:false},
      {name:'magnitude',hidden:false,editable:false,sortable:false},
      //{name:'zh',hidden:false,editable:false,sortable:false},
      {name:'desc',hidden:false,editable:false,sortable:false},
      {name:'qty',hidden:false,editable:false,sortable:false},
      {name:'curr',hidden:false,editable:false,sortable:false},
      {name:'up',hidden:false,editable:false,sortable:false},
      {name:'amt',hidden:false,editable:false,sortable:false}
    ];
	var navOptions = {view:false,del:false,edit:false,add:false,search:false};
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
    }
	$(window).resize(resize_the_grid);
    jQuery(document).ready(function(){
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,true,true);
		
		var autoOption = {
				dataType:"json",
				parse: function(data){
					var acd = new Array();
			        for(var i=0;i<data.length;i++){
						acd[acd.length] = { data:data[i], value:data[i].code, result:data[i].code };
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
		$("#supplier").autocomplete("${resource(dir:'')}/${controllerName}/listSupplierJson", {
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
		$("#shipmentForm").validate({
			rules: {
				poCode: {required:true},
				poCode: {required:true}
				//supplier: {required:true,number: true}
			}
		});
		$("#datepicker").datepicker({ dateFormat: 'yy-mm-dd' });
    });
	function exportPO(){
		var selects = jQuery("#grid").jqGrid('getGridParam','selarrrow'); 
		var items = [];
		for(var i in selects){
			var rowNum = selects[i];
			var item = $("#grid").getRowData(rowNum).id;
			items.push(item);
		}
		 
		
		var url = "${resource(dir:'')}/interface/exportPurchaseOrder?id=${instance?instance.id:''}&items="+items;
		window.location.href=url
		//window.open(url); 
	}

</script>
</head>

<body>

<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
	<div class="fg-buttonset ui-helper-clearfix">
	<a href="${resource(dir:'')}/${controllerName}/showPO/${instance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
	 <span class="ui-icon ui-icon-refresh"></span>r
	</a>
	<a href="${resource(dir:'')}/${controllerName}/listPO" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
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
			<span class="ui-jqgrid-title">Purchase Order</span>
		</div>
		<form id="shipmentForm" method="post" action="${resource(dir:'')}/${controllerName}/updatePO">
		<g:hiddenField name="id" value="${instance?.id}"/>
		<div class="ui-dialog-content ui-widget-content">
			<table><tbody>
			  <tr class="prop">
			    <td class="name">Purchase Order</td>
			    <td class="value">
			   		<select name="poCode" id="poCode" >
						<option value="${instance?.poCode}" >${instance?.poCode}</option>
						<g:each in="${lsPO}" status="i" var="PO">
						    <option value="${PO.code}" >${PO.code}</option>
						</g:each>
					</select>
					 
				</td>
			    <td class="name">Order Date</td>
			    <td class="value">
					<input id="datepicker" name="deliveryDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.deliveryDate)}">
					
				</td>
			  </tr>
			  <tr class="prop">
			    <td class="name">Supplier Code</td>
			    <td class="value">
					${instance?.supplier?.code}-${instance?.supplier?.name}
				</td>
			    <td class="name">Sales Code</td>
			    <td class="value">
					<g:if test="${instance?.supplier?.code == '1800'}">
						${instance?.soCode}
						<g:hiddenField id="soCode" name="soCode" maxlength="50" value="${instance?.soCode}"/>
					</g:if>
					<g:if test="${instance?.supplier?.code=='1200'}">
						${instance?.soCode}
						<g:hiddenField id="soCode" name="soCode" maxlength="50" value="${instance?.soCode}"/>
					</g:if>
					<g:else>
					${reInvoices?reInvoices:''}
						<g:hiddenField id="soCode" name="soCode" maxlength="50" value="${reInvoices?reInvoices:''}"/>
					</g:else>
				</td>
			  </tr>
 
			  <tr class="prop">
			    <td class="name">Customer Code</td>
			    <td class="value">
					${instance?.customer?.code}<br>
					${instance?.customer?.reInvoicePreCode}
				</td>
			    <td class="name">Location</td>
			    <td class="value">
					${instance?.customer?.whId}<br>${instance?.customer?.locId}<br>
				</td>
			  </tr>
			  <tr class="prop">
			    <td class="name">Status</td>
			    <td class="value">${instance?.toScala}</td>
			    <td class="name">Sub Total</td>
			    <td class="value">${amount} </td>
			  </tr>
			</tbody></table>
		</div>
		<div class="buttons">
			<span class="button"><input type="submit" class="save" value="${message(code: 'default.button.update.label', default: 'Save')}"/></span>
			<g:if test="${instance}">
			<span><a href="javascript:exportPO();" class="save">Export</a></span>
			</g:if>
		</div>
		</form>
	</div>
	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
</html>
	