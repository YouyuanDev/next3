<html>
<head>
  <meta name="layout" content="main_crud"/>
    <script type="text/javascript">
    var listurl = "${resource(dir:'')}/${controllerName}/listJSON";
    var colNames = [
      "${message(code: 'shipment.code.label', default: 'Code')}",
      "${message(code: 'shipment.reinvoice.label', default: 'Re Invoice')}",
      "${message(code: 'shipment.quantity.label', default: 'Quantity')}",
      "${message(code: 'shipment.amount.label', default: 'Amount')}",
      "${message(code: 'shipment.duty.label', default: 'Duty')}",
      "${message(code: 'shipment.cost.label', default: 'Cost')}",
      "${message(code: 'shipment.total.label', default: 'Total')}",
    ];
    var colModel = [
      {name:'code',index:'code',formatter:linkformatter,sortable:false},
      {name:'reinvoice',index:'reinvoice',sortable:false},
      {name:'totalQuantity',index:'totalQuantity',sortable:false},
      {name:'totalAmount',index:'totalAmount',align:"right",sortable:false},
      {name:'totalDuty',index:'totalDuty',align:"right",sortable:false},
      {name:'totalCost',index:'totalCost',align:"right",sortable:false},
      {name:'totalPrice',index:'totalPrice',search:false}
    ];
    var caption = "${message(code:'common.results.label',default:'Results')}";

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
    jQuery(document).ready(
            function() {
              var lastsel;
              jQuery("#grid").jqGrid({
                url:listurl,datatype: "json",mtype: 'GET',
                colNames:colNames,
                colModel :colModel,
                pager: '#gridnav',
                rowNum:rowNum,
                //rowList:[10,20,30],
                sortname: 'code',
                sortorder: 'code',
                viewrecords: true,
                caption: caption,
                autowidth: true,
                height:'auto'
              });
              jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:false},
              {}, // use default settings for edit
              {}, // use default settings for add
              {}, // delete instead that del:false we need this
              {multipleSearch : true}, // enable the advanced searching
              {closeOnEscape:true} /* allow the view dialog to be closed when user press ESC key*/
                //{reloadAfterSubmit:false} //delete parameters
                      );
            });
    $(window).resize(resize_the_grid);
    function gridReload(){
      var code = jQuery("#code").val();
      var reInvoice = jQuery("#reInvoice").val();
      jQuery("#grid").jqGrid('setGridParam',{url:"listJSON?code="+code+"&reInvoice="+reInvoice,page:1}).trigger("reloadGrid");
    }       
  </script>
</head>
<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">${message(code: 'shipment.shipment.label', default: 'Shipment')}</span>
  </div>
  <div class="ui-dialog-content ui-widget-content">
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
  </div>
  <div class="buttons">
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
  </div>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
</body>
</html>
