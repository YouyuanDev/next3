<html>
<head>
  <meta name="layout" content="main_blank"/>
  <title>BP Invoice</title>
  <script type="text/javascript">
    var listurl = "${resource(dir:'')}/${controllerName}/listBPInvoiceItemsJson/${fieldValue(bean: instance, field: "id")}";
    var editurl = "${resource(dir:'')}/${controllerName}/updateInvoiceItem";
    var colNames = [
      "${message(code: 'common.action.label', default: 'Action')}",
      "${message(code: 'product.short.label', default: 'Short')}",
      "${message(code: 'product.color.label', default: 'Color')}",
      "${message(code: 'product.size.label', default: 'Size')}",
      "${message(code: 'product.family.label', default: 'Family')}",
      "${message(code: 'product.dept.label', default: 'Dept')}",
      "${message(code: 'product.material.label', default: 'Material')}",
      "${message(code: 'invoiceitem.country.label', default: 'Country')}",
      "${message(code: 'common.master.label', default: 'Master')}",
      "${message(code: 'product.desczh.label', default: 'Desc ZH')}",
      "${message(code: 'product.descen.label', default: 'Desc EN')}",
      "${message(code: 'invoiceitem.quantity.label', default: 'Quantity')}",
      "${message(code: 'invoiceitem.amount.label', default: 'Amount')}",
      "${message(code: 'invoiceitem.issuance.label', default: 'Issuance')}",
      "${message(code: 'invoiceitem.freight.label', default: 'Freight')}",
      "${message(code: 'invoiceitem.difference.label', default: 'Total')}"
    ];
    var colModel = [
      //{name:'id', index:'id',formatter:linkformatter,hidden:true,key:true},
      {name:'invoiceitem.id',hidden:true,editable:true},
      {name:'product.short',align:'left',sortable:false,width:120},
      {name:'product.color',sortable:false,width:200},
      {name:'product.size',sortable:false},
      {name:'product.family',sortable:false},
      {name:'product.dept',sortable:false,editable:false},
      {name:'product.material',sortable:false,editable:true},
      {name:'invoiceitem.country',sortable:false,editable:true},
      {name:'product.ismaster',align:'center',sortable:false,editable:true,edittype:"checkbox",editoptions: {value:"Yes:No"}     },
      {name:'product.desczh',sortable:false,editable:true},
      {name:'product.descen',sortable:false},
      {name:'invoiceitem.quantity',align:'right',sortable:false},
      {name:'invoiceitem.amount',align:'right',sortable:false},
      {name:'invoiceitem.issuance',align:'right',sortable:false},
      {name:'invoiceitem.freight',align:'right',sortable:false},
      {name:'invoiceitem.total',align:'right',sortable:false}
    ];
    var caption = "${message(code:'invoice.invoiceitems.label',default:'Invoice Items')}";
    var rowNum = 20;
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/show/" + cellvalue + "'>" + cellvalue + "</a>"
      return linkurl;
    }
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
    }

    var invoicelisturl = "${resource(dir:'')}/${controllerName}/listBPInvoicesJson/${fieldValue(bean: instance, field: "id")}";
	//var invoicelisturl = "";
	var invoiceediturl = "${resource(dir:'')}/${controllerName}/updateReinvoiceById";
    var invoicecolNames = [
		"",
		"Code",
		"Date",
		"Customer",
		"Number"
	];
    var invoicecolModel = [
		{name:'InvoiceId',hidden:true,sortable:false,editable:true},
		{name:'Code',align:'left',sortable:false},
      {name:'Date',align:'left',sortable:false,width:120},
      {name:'Customer',sortable:false,width:200},
      {name:'Number',sortable:false,width:200,editable:true}
	];	
	
    jQuery(document).ready(function(){
		//var data = "Core Selectors Attributes Traversing Manipulation CSS Events Effects Ajax Utilities".split(" ");
		$("#hawb").autocomplete("${resource(dir:'')}/${controllerName}/listDistinctHawbJson",{json: true});		
	
		var lastsel;
		jQuery("#invoiceGrid").jqGrid({
		  url:invoicelisturl,editurl:invoiceediturl,datatype: "json",mtype: 'GET',
		  colNames:invoicecolNames,colModel :invoicecolModel,pager: '#invoiceGridnav',
		  viewrecords: true,rowNum:100,//rowList:[10,20,30],sortname: 'code',sortorder: 'code',
		  autowidth: true,height:'auto',multiselect:false,rownumbers:true,rownumWidth:40, 
		  onSelectRow: function(id) {
		    //if(id && id!==lastsel){
		    if (id) {
		      jQuery('#invoiceGrid').jqGrid('restoreRow', lastsel);
		      jQuery('#invoiceGrid').jqGrid('editRow', id, true);
		      lastsel = id;
		    }
		  }
        });
		jQuery("#invoiceGrid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true});

		jQuery("#grid").jqGrid({
		  caption:"",
		  url:listurl,editurl:editurl,datatype: "json",mtype: 'GET',
		  colNames:colNames,colModel :colModel,pager: '#gridnav',
		  viewrecords: true,rowNum:100,//rowList:[10,20,30],sortname: 'code',sortorder: 'code',
		  autowidth: true,height:'auto',multiselect:false,rownumbers:true,rownumWidth:40, 
		  onSelectRow: function(id) {
		    //if(id && id!==lastsel){
		    if (id) {
		      jQuery('#grid').jqGrid('restoreRow', lastsel);
		      jQuery('#grid').jqGrid('editRow', id, true);
		      lastsel = id;
		    }
		  }
        });
		jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true});

		jQuery("#grid").jqGrid('navButtonAdd', '#gridnav', {
			caption: "Adjust",title: "Reorder Columns",
			onClickButton : function () {
				$('#dialog').dialog('open');
			}
		});
		$("#dialog").dialog({
			bgiframe:true,autoOpen:false,height:300,modal:true,
			buttons: {
				'Submit': function() {
					var url = "${resource(dir:'')}/${controllerName}/updateAdjAmount/"
					$.post(url,{id:$("#id").val(),adjAmount:$("#adjAmount").val()},function(data){
						$("#adjTotalAmount").val("hello");
					}, "text");
					$(this).dialog('close');
				},
				'Cancel': function() {
					$(this).dialog('close');
				}
			},
			close: function() {
			}
		});

    });
	$(window).resize(resize_the_grid);
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
      <span class="ui-jqgrid-title">Invoice</span>
    </div>
    <div class="ui-dialog-content ui-widget-content">
		<g:form method="post" action="updateBusiness">
		<g:hiddenField name="id" value="${fieldValue(bean: instance, field: 'id')}"/>
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
          <td class="value">${fieldValue(bean: instance, field: "customer.customerShortCode")}</td>
          <td class="name"><g:message code="customer.name.label" default="Customer Name"/></td>
          <td class="value">${fieldValue(bean: instance, field: "customer.customerCateName")}</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.totalamount.label" default="Amount"/></td>
          <td class="value">${fieldValue(bean: instance, field: "totalAmount")}</td>
          <td class="name"><g:message code="invoice.difference.label" default="Difference"/></td>
			<td id="adjTotalAmount" class="value">
				<g:if test="${instance.totalDifference != 0}">
					<font style="color:red">${instance?instance.totalDifference:''}</font>
				</g:if>
				<g:else>
					0
				</g:else>
			</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.totalissuance.label" default="Issuance"/></td>
          <td class="value"><g:textField name="totalIssuance" maxlength="50" value="${instance?instance.totalIssuance:'0'}"/></td>
          <td class="name"><g:message code="invoice.totalfreight.label" default="Freight"/></td>
          <td class="value"><g:textField name="totalFreight" maxlength="50" value="${instance?instance.totalFreight:'0'}"/></td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.totalquantity.label" default="Quantity"/></td>
          <td class="value">${fieldValue(bean: instance, field: "totalQuantity")}</td>
          <td class="name"><g:message code="invoice.currency.label" default="Currency"/></td>
			<td class="value"><g:select name="currency"
		          from="['EUR','RMB']"
		          value="${instance?.currency}"
		         />
			</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.hawb.label" default="Hawb"/></td>
          <td class="value"><g:textField name="hawb" maxlength="50" value="${fieldValue(bean: instance, field:'hawb')}"/></td>
          <td class="name">Diff Adjust</td>
          <td class="value"><g:textField name="totalAdjDiff" maxlength="50" value="${instance?instance.totalAdjDiff:'0'}"/></td>
        </tr>
		<tr>
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
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.export.label', default: 'Export')}"/></span>
		</g:form>
    </div>
  </div>
  <table id="grid"></table>
  <div id="gridnav"></div>
  <div id="dialog" title="${message(code: 'invoice.adjust.label', default: 'adjust')}">
    <form action="" method="post">
      <fieldset>
        <label for="name">${message(code: 'invoiceitem.amount.label', default: 'Amount')}</label>
        <input type="text" name="adjAmount" id="adjAmount" class="text ui-widget-content ui-corner-all"/>
      </fieldset>
    </form>
  </div>
</div>
</body>
</html>
	