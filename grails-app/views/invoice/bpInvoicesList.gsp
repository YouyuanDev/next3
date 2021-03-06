<html>
<head>
  <meta name="layout" content="main_blank"/>
  <script type="text/javascript">
	var grid;   
    var caption = "${message(code:'invoice.search.label',default:'Invoice Search')}";
    var listurl = "${resource(dir:'')}/${controllerName}/listBpInvoicesJson";
    var colNames = [
	  "Key",
      "${message(code: 'invoice.bpcode.label', default: 'BP Code')}",
      "${message(code: 'invoice.code.label', default: 'Invoice Code')}",
      "${message(code: 'invoice.reinvoice.label', default: 'Re Invoice')}",
      "${message(code: 'customer.code.label', default: 'Customer Code')}",
      "${message(code: 'customer.name.label', default: 'Customer Name')}",
      "${message(code: 'product.dept.label', default: 'Dept')}",
      "${message(code: 'product.material.label', default: 'Material')}",
      "${message(code: 'category.code.label', default: 'Product Short')}",
      "${message(code: 'category.name.label', default: 'ZH')}",
      "${message(code: 'product.name.label', default: 'EN')}",
      "${message(code: 'invoiceitem.quantity.label', default: 'Quantity')}",
      "${message(code: 'invoiceitem.amount.label', default: 'Amount')}",
      "${message(code: 'invoiceitem.issuance.label', default: 'Issuance')}",
      "${message(code: 'invoiceitem.Freight.label', default: 'Freight')}",
      "${message(code: 'invoiceitem.Total.label', default: 'Total')}",
    ];
    var colModel = [
      {name:'key', index:'key',hidden:true},
      {name:'business.code',index:'business.code',width:130,formatter:linkformatterbp,sortable:false},
      {name:'invoice.code',index:'invoice.code',width:170,formatter:linkformatterinvoice,sortable:false},
      {name:'invoice.reInvoice',index:'invoice.reInvoice',width:130,sortable:false},
      {name:'customer.code',index:'customer.code',width:195,sortable:false},
      {name:'customer.name',index:'customer.name',width:280,sortable:false},
      {name:'product.dept',index:'product.dept',width:220,sortable:false},
      {name:'product.material',index:'product.material',width:130,sortable:false},
      {name:'category.code',index:'category.code',width:130,sortable:false},
      {name:'category.name',index:'category.name',width:130,sortable:false},
      {name:'product.name',index:'product.name',width:'100%',sortable:false},
      {name:'invoiceitem.quantity',index:'invoiceitem.quantity',width:130,align:"right",sortable:false},
      {name:'invoiceitem.amountt',index:'invoiceitem.amount',width:130,align:"right",sortable:false},
      {name:'invoiceitem.issuance',index:'invoiceitem.issuance',width:130,align:"right",sortable:false},
      {name:'invoiceitem.freight',index:'invoiceitem.freight',width:130,align:"right",sortable:false},
      {name:'invoiceitem.total',index:'invoiceitem.total',width:130,align:"right",sortable:false}
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
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:-5});
    }
    jQuery(document).ready(function() {
		var lastsel;
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,true,true);

		$("#hawbDialog").dialog({
			bgiframe:true,autoOpen:false,height:300,modal:true,
			buttons: {
				'Submit': function() {
					var url = "${resource(dir:'')}/${controllerName}/updateHawbByItems/";
					$.post(url,{hawb:$("#hawb").val(),ids:gridSelarrrow(grid)},function(data){alert(data);}, "text");
					gridReload();
				},
				'Cancel': function() {
					$(this).dialog('close');
				}
			},
			close: function() {
			}
		});
		$("#receiveDateDialog").dialog({
			bgiframe:true,autoOpen:false,height:300,modal:true,
			buttons: {
				'Submit': function() {
				},
				'Cancel': function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				gridReload();
			}
		});
		$("#receiveDate").datepicker();
		$('#searchForm').ajaxForm(); 

    });
    $(window).resize(resize_the_grid);
    function gridReload(){
		var query = $('#searchForm').formSerialize(); 
		grid.jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid"); 
    }  
	function hawbOpen(){
		var ids=gridSelarrrow(grid);
		if(ids.length==0){
			alert("${message(code: 'warning.noselect.label', default: 'please select items')}");
		}else{
			$("#hawbDialog").dialog('open');
		}
	}
  </script>
</head>
<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">${message(code: 'invoice.invoice.label', default: 'Invoice')}</span>
  </div>
  <form name="searchForm" id="searchForm">
  <div class="ui-dialog-content ui-widget-content">
    <table>
      <tbody>
      <tr class="prop">
        <td class="name"><g:message code="invoice.code.label" default="Invoice Code"/></td>
        <td class="value"><g:textField name="invoiceCode"/></td>
        <td class="name"><g:message code="business.code.label" default="BP Code"/></td>
        <td class="value"><g:textField name="bpCode"/></td>
      </tr>
      <tr class="prop">
        <td class="name"><g:message code="invoice.reinvoice.label" default="Re Invoice"/></td>
        <td class="value"><g:textField name="reInvoice"/></td>
        <td class="name"></td>
        <td class="value"></td>
      </tr>
      <tr class="prop">
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
      </tr>
      </tbody>
    </table>
  </div>
  <div class="buttons">
    <span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
    <span class="button"><input type="button" class="save" onclick="hawbOpen()"  action="update" value="${message(code: 'default.button.hawb.label', default: 'Hawb')}"/></span>
    <span class="button"><input type="button" class="save" onclick=""  action="update" value="${message(code: 'default.button.receivedate.label', default: 'Receive Date')}"/></span>
  </div>
  </form>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
<div id="hawbDialog" title="${message(code: 'invoiceitem.updatehawb.label', default: 'Update Hawb')}">
	<p id="validateTips">All form fields are required.</p>
	<p class="ui-state-error ui-corner-all">something went wrong</p>
	<fieldset>
		<label>${message(code: 'invoiceitem.hawb.label', default: 'Hawb')}</label>
		<input type="text" name="hawb" id="hawb" class="text ui-widget-content ui-corner-all" />
	</fieldset>
</div>
<div id="receiveDateDialog" title="Update ReceiveDate">
	<p class="ui-state-error ui-corner-all">something went wrong</p>
	<fieldset>
		<label>${message(code: 'invoiceitem.receivedate.label', default: 'Receive Date')}</label>
		<input type="text" name="receiveDate" id="receiveDate" class="text ui-widget-content ui-corner-all" />
	</fieldset>
</div>
</body>
</html>
