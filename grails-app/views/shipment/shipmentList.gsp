<html>
<head>
  <meta name="layout" content="main_blank"/>
    <script type="text/javascript">
	var grid;
    var caption = "${message(code:'invoice.search.label',default:'Shipment Search')}";
    var listurl = "${resource(dir:'')}/${controllerName}/listShipmentsJson";
    var colNames = ["Code","ReInoive","Date","Description"];
    var colModel = [
      {name:'code',index:'code',formatter:linkformatter,sortable:false},
      {name:'reInvoice',index:'reInvoice',sortable:false},
      {name:'date',index:'date',sortable:false}
    ];
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
	jQuery(document).ready(function() {
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,true,true);
	});
    $(window).resize(resize_the_grid);
    function gridReload(){
		var query = $('#searchForm').formSerialize(); 
		grid.jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid"); 
    }  
	
  </script>
</head>
<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">${message(code: 'shipment.shipment.label', default: 'Shipment')}</span>
  </div>
  <div class="ui-dialog-content ui-widget-content">
	<form name="searchForm" id="searchForm">
    <table>
      <tbody>
      <tr class="prop">
        <td class="name"><g:message code="shipment.code.label" default="Shipment Code"/></td>
        <td class="value"><g:textField name="code"/></td>
        <td class="name"><g:message code="shipment.reInvoice.label" default="Re Invoice"/></td>
        <td class="value"><g:textField name="reInvoice"/></td>
      </tr>
      </tbody>
    </table>
	</form>
  </div>
  <div class="buttons">
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
  </div>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
</body>
</html>
