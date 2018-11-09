<html>
<head>
	<meta name="layout" content="main_blank"/>
	<title>BP Invoice</title>
	<script type="text/javascript">
			if ("${notnull}"=="0"){
				alert("The amount of item cann't be null!");
			}
			function resize_the_grid(){
				//alert("hello");
				$('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
				$('#invoiceGrid').fluidGrid({base:'#grid_wrapper', offset:0});
			}


			
			function pickdates(id){ jQuery("#"+id+"_sdate","#rowed6").datepicker({dateFormat:"yy-mm-dd"}); } 	
			var listurl = "${resource(dir:'')}/${controllerName}/listBPInvoiceItemsJson/${fieldValue(bean: instance, field: "id")}";
			var editurl = "${resource(dir:'')}/${controllerName}/updateInvoiceItem";
			var colNames = [
				"${message(code: 'common.action.label', default: 'Action')}",
				"Invoice Code",
				"Style Code",
				"${message(code: 'product.short.label', default: 'Long')}",
				"${message(code: 'product.color.label', default: 'Color')}",
				"${message(code: 'product.size.label', default: 'Size')}",
				"${message(code: 'product.family.label', default: 'Family')}",
				"product Code",
				"${message(code: 'product.dept.label', default: 'Dept')}",
				"${message(code: 'product.material.label', default: 'Material')}",
				"${message(code: 'product.material2.label', default: 'Material2')}",
				"${message(code: 'invoiceitem.country.label', default: 'Country')}",
				"${message(code: 'common.master.label', default: 'Master')}",
				"${message(code: 'product.desczh.label', default: 'Desc ZH')}",
				"${message(code: 'product.descen.label', default: 'Desc EN')}",
				"${message(code: 'invoiceitem.quantity.label', default: 'Quantity')}",
				"${message(code: 'invoiceitem.amount.label', default: 'Unit Price')}",
				"${message(code: 'invoiceitem.issuance.label', default: 'Unit Iss.&Fre.')}",
				//"${message(code: 'invoiceitem.freight.label', default: 'Freight')}",
				"${message(code: 'invoiceitem.difference.label', default: 'Total')}"
			];
			var colModel = [
				//{name:'id', index:'id',formatter:linkformatter,hidden:true,key:true},
				{name:'invoiceitem.id',hidden:true,editable:true},
				{name:'invoiceheader.code',editable:false},
				{name:'product.styleCode',hidden:false,editable:true},
				{name:'product.short',align:'left',sortable:false,width:120},
				{name:'product.color',sortable:false,width:200},
				{name:'product.size',sortable:false},
				{name:'product.family',sortable:false},
				{name:'product.productCode',sortable:false},//Kurt Edited
				{name:'product.dept',sortable:true,editable:false},//Kurt Edited  
				{name:'product.material',sortable:false,editable:true},
				{name:'product.material2',sortable:false,editable:true},
				{name:'invoiceitem.country',sortable:false,editable:true},
				{name:'product.ismaster',align:'center',sortable:false,editable:true,edittype:"checkbox",editoptions: {value:"Yes:No"}     },
				{name:'product.desczh',sortable:true,editable:true},
				{name:'product.descen',sortable:false,editable:true},
				{name:'invoiceitem.quantity',align:'right',sortable:false},
				{name:'invoiceitem.amount',align:'right',sortable:false,editable:true},
				{name:'invoiceitem.issuance',align:'right',sortable:false},
				//{name:'invoiceitem.freight',align:'right',sortable:false},
				{name:'invoiceitem.total',align:'right',sortable:false}
			 ];
			 var caption = "${message(code:'invoice.invoiceitems.label',default:'Invoice Items')}";
			 var rowNum = 20;
			 function linkformatter(cellvalue, options, rowObject)
			 {
			   var linkurl = "<a href='${resource(dir:'')}/${controllerName}/show/" + cellvalue + "'>" + cellvalue + "</a>"
			   return linkurl;
			 }
		
			/* invoice grid */
			var invoicelisturl = "${resource(dir:'')}/${controllerName}/listBPInvoicesJson/${fieldValue(bean: instance, field: "id")}";
			var invoiceediturl = "${resource(dir:'')}/${controllerName}/updateReinvoiceById";
			var invoicecolNames = ["","Type of Invoice","Code","Date","Import","Export","Customer","Number"];
			var invoicecolModel = [
				{name:'invoiceId',hidden:true,sortable:false,editable:true},
                 				
				{name:'invoiceType',sortable:false,width:200,editable:true}, //Kurt edited
				{name:'Code',align:'left',sortable:false},//Kurt Edited
				{name:'Date',align:'left',sortable:false,width:120},
				{name:'importDate',align:'left',sortable:false,width:120,editable:true,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},//Kurt edited
				{name:'exportDate',align:'left',sortable:false,width:120,editable:true,editoptions:{dataInit:function(el){$(el).datepicker({dateFormat:'yy-mm-dd'});}}},//Kurt edited
				{name:'Customer',sortable:false,width:200},
				{name:'number',sortable:false,width:200,editable:true}
			];	
			jQuery(document).ready(function(){
				$("input#deleteBP").click(function(){
					if(window.confirm('Are you sure to delete the BP?')){
					 window.location.href="${resource(dir:'')}/${controllerName}/deleteBPInvoicesJson?id=${instance?instance.id:''}"
					}
								});
				$("input#exportBP").click(function(){
					//alert(document.getElementById('parentCompany').value);
					if(window.confirm('Do you want to have Two materials merged?')){
						
						
					 window.location.href="${resource(dir:'')}/${controllerName}/exportInvoiceList?id=${instance?instance.id:''}&parentCompany="+document.getElementById('parentCompany').value+"&merge=1"
					}
					else
					window.location.href="${resource(dir:'')}/${controllerName}/exportInvoiceList?id=${instance?instance.id:''}&parentCompany="+document.getElementById('parentCompany').value+"&merge=0"
					
								});


				
				var lastsel;
				/*inovice grid*/
				jQuery("#invoiceGrid").jqGrid({
					url:invoicelisturl,editurl:invoiceediturl,datatype: "json",mtype: 'GET',
					colNames:invoicecolNames,colModel :invoicecolModel,pager: '#invoiceGridnav',
					viewrecords: true,rowNum:100,//rowList:[10,20,30],sortname: 'code',sortorder: 'code',
					autowidth: true,height:'auto',multiselect:false,rownumbers:true,rownumWidth:40, 
					onSelectRow: function(id) {
						if (id) {
							jQuery('#invoiceGrid').jqGrid('restoreRow', lastsel);
							jQuery('#invoiceGrid').jqGrid('editRow', id, true);
							lastsel = id;
						}
					}
					/*
					cellEdit: true, 
					afterEditCell: function (id,name,val,iRow,iCol){ 
						if(name=='ImportDate'){
							jQuery("#"+iRow+"_ImportDate","#celltbl").datepicker({dateFormat:"yy-mm-dd"});
						}
						if(name=='ExportDate'){
							jQuery("#"+iRow+"_ExportDate","#celltbl").datepicker({dateFormat:"yy-mm-dd"});
						}
					} */					
				});	
				jQuery("#invoiceGrid").jqGrid('navGrid', '#gridnav',{view:false,del:false,edit:false,add:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true});

				/*invoiceitems grid*/
				jQuery("#grid").jqGrid({
					caption:"",
					url:listurl,editurl:editurl,datatype: "json",mtype: 'GET',
					colNames:colNames,colModel :colModel,pager: '#gridnav',
					viewrecords: true,rowNum:100,sortorder: 'dept',//rowList:[10,20,30],sortname: 'code',sortorder: 'code',
					autowidth: true,height:'auto',multiselect:false,rownumbers:true,rownumWidth:40
					/*onSelectRow: function(id) {
						//if(id && id!==lastsel){
						/*if (id) {
							jQuery('#grid').jqGrid('restoreRow', lastsel);
							jQuery('#grid').jqGrid('editRow', id, true);
							lastsel = id;
<<<<<<< bpInvoicesShow.gsp
						}
					}*/

				});
				jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:true,add:false,search:false,edittext:'Modify'},{addCaption: "Add Record",
				     editCaption: "Edit InvoiceItem",
				     bSubmit: "Save",
				     bCancel: "Cancel",
				     bClose: "Close",
				     saveData: "Data has been changed! Save changes?",
				     bYes : "Yes",
				     bNo : "No",
				     bExit : "Cancel"},{},{},{multipleSearch : true},{closeOnEscape:true});
				  
				var acOption = {
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
				}		
				$("#hawb").autocomplete("${resource(dir:'')}/${controllerName}/listDistinctHawbJson",acOption);
				
				$("#bpInvoicesForm").validate({
					rules: {
				    	totalIssuance: {required: true,number: true},
				    	totalFreight: {required: true,number: true},
				    	hawb: {required: false}, //Kurt updated
				    	totalAdjDiff: {required: true,number: true}
				  	},
					messages: {
						totalIssuance: " Required",
						totalFreight: " Required",
						//hawb: " Required",  //kurt edited
						totalAdjDiff: " Required"
					}
				});
			});
			$(window).resize(resize_the_grid);
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

<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
	<div class="fg-buttonset ui-helper-clearfix">
	<a href="${resource(dir:'')}/${controllerName}/showBP/${fieldValue(bean: instance, field: 'code')}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
	 <span class="ui-icon ui-icon-refresh"></span>r
	</a>
	<a href="${resource(dir:'')}/${controllerName}/listBpInvoices" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
	 <span class="ui-icon ui-icon-back"></span>b
	</a>
	</div>
</div>
<div class="main">
	<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
		<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
		  <span class="ui-jqgrid-title">Invoice</span>
		</div>
		<form id="bpInvoicesForm" method="POST" action="${resource(dir:'')}/${controllerName}/updateBusiness">
		<g:hiddenField name="id" value="${fieldValue(bean: instance, field: 'id')}"/>
		<div class="ui-dialog-content ui-widget-content">
			<table>
			<tbody>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.bpcode.label" default="BP Code"/></td>
			 <td class="value">${fieldValue(bean: instance, field: "code")}</td>
			 <td class="name"><g:message code="invoice.reinvoice.label" default="Re Invoice"/></td>
			 <td class="value">${reInvoiceNumbers?reInvoiceNumbers:''}</td>
			</tr>
			<tr class="prop">
			</tr>
			<tr class="prop">
			 <td class="name"><g:message code="customer.code.label" default="Customer Code"/></td>
			 <td class="value">${fieldValue(bean: instance, field: "customer.customerLongCode")}</td>
			 <td class="name"><g:message code="customer.name.label" default="Customer Name"/></td>
			 <td class="value">${fieldValue(bean: instance, field: "customer.customerCateName")}</td>
			</tr>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.totalfob.label" default="FOB"/></td>
			 <td class="value">${fieldValue(bean: instance, field: "totalAmount")}</td>
			 <td class="name"><g:message code="invoice.totalquantity.label" default="Quantity"/></td>
			 <td class="value">${fieldValue(bean: instance, field: "totalQuantity")}</td>
			</tr>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.totalissuance.label" default="Issuance"/> *</td>
			 <td class="value"><g:textField name="totalIssuance" maxlength="50" value="${instance?instance.totalIssuance:'0'}"/></td>
			 <td class="name"><g:message code="invoice.totalfreight.label" default="Freight"/> *</td>
			 <td class="value"><g:textField name="totalFreight" maxlength="50" value="${instance?instance.totalFreight:'0'}"/></td>
			</tr>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.hawb.label" default="Hawb"/></td>
			 <td class="value"><g:textField name="hawb" maxlength="50" value="${fieldValue(bean: instance, field:'hawb')}"/></td>
			 <td class="name"><g:message code="invoice.currency.label" default="Currency"/></td>
			<td class="value"><g:select name="currency"
			   from="['RMB','EUR']"
			   value="${instance?.currency}"
			  />
			</td>
			</tr>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.difference.label" default="Difference"/></td>
			<td id="adjTotalAmount" class="value">
			<g:if test="${instance.totalDifference != 0}">
			<font style="color:red">${instance?instance.totalDifference:''}</font>
			</g:if>
			<g:else>
			0
			</g:else>
			</td>
			 <td class="name">Diff Adjust *</td>
			 <td class="value"><g:textField name="totalAdjDiff" maxlength="50" value="${instance?instance.totalAdjDiff:'0'}"/></td>
			</tr>
			<tr>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.totalamount.label" default="Amount"/></td>
			 <td class="value">${fieldValue(bean: instance, field: "totalFinalamount")}</td>
			 <td class="name"><g:message code="invoice.co.label" default="CO@CITIS"/></td>
			<td class="value"><g:checkBox name="cocitis" value="${instance?.cocitis}" /></td>
			</tr>
			<tr class="prop">
			 <td class="name"><g:message code="invoice.parentCompany.label" default="Parent Company"/></td>
			 <td class="value"><g:select id="parentCompany" name="parentCompany"
			   from="['A. Hermes (China) CO., LTD','B. Hermes (China) Trading Co., Ltd']"
			   value="${instance?.customer.parentCompany}"/>(For exporting BP Inovice Heading only)
			   
			   </td>
			 <td class="name"><g:message code="invoice.co.label" default="CO@CITIS Remark"/></td>
			<td class="value"><g:select name="cocitisRemark"
			   from="['','CO','CITIS']"
			   value="${instance?.cocitisRemark}"
			  /> </td>
			</tr>
			<td colspan="4">
			<table id="invoiceGrid"></table>
			<div id="invoiceGridnav"></div>	
			</td>
			</tr>
			</tbody>
			</table>
	    </div>
	    <div class="buttons">
	      <span class="button"><input type="submit" class="save" value="${message(code: 'default.button.update.label', default: 'Save')}"/></span>
 	     <span class="button"><input id="deleteBP" type="button" class="delete" value="${message(code: 'default.button.Delete BP.label', default: 'Delete BP')}"/></span>
 	     <span class="button"><input id="exportBP" type="button" class="save" value="${message(code: 'default.button.Export BP.label', default: 'Export BP')}"/></span>
 	    <!-- 
	    	<span><a href="${resource(dir:'')}/${controllerName}/exportInvoiceList?id=${instance?instance.id:''}" class="save">Export</a></span>
	    	
	    	 -->
	    </div>
		</form>
	</div>
	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
	<script type="text/javascript">
			if ("${notnull}"=="0"){
				alert("The amount of item cann't be null!");
			}
	</script>
</html>
	