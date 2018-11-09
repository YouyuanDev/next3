<html>
<head>
  <meta name="layout" content="main_blank"/>
  <title>Invoice</title>
  <script type="text/javascript">
    var listurl = "${resource(dir:'')}/${controllerName}/listInvoiceItemsJson/${fieldValue(bean: instance, field: "code")}";
    var editurl = "${resource(dir:'')}/${controllerName}/updateInvoiceItem";
    var colNames = [
      "${message(code: 'common.action.label', default: 'Action')}",
      "${message(code: 'product.short.label', default: 'Short')}",
      "${message(code: 'product.color.label', default: 'Color')}",
      "${message(code: 'product.size.label', default: 'Size')}",
      "${message(code: 'product.family.label', default: 'Family')}",
      "${message(code: 'invoiceitem.country.label', default: 'Country')}",
      "${message(code: 'product.material.label', default: 'Material')}",
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
      {
        name:'invoiceitem.id',
        hidden:true,
        editable:true
      },
      {
        name:'product.short',
        align:'left',
        sortable:false,
        width:120
      },
      {
        name:'product.color',
        sortable:false,
        width:200
      },
      {
        name:'product.size',
        sortable:false,
      },
      {
        name:'product.family',
        sortable:false,
      },
      {
        name:'invoiceitem.country',
        sortable:false,
        editable:true
      },
      {
        name:'product.material',
        sortable:false,
        editable:true
      },
      {
        name:'product.ismaster',
        align:'center',
        sortable:false,
        editable:true,
        edittype:"checkbox",
        editoptions: {value:"Yes:No"}
      },
      {
        name:'product.desczh',
        sortable:false,
        editable:true
      },
      {
        name:'product.descen',
        sortable:false,
      },
      {
        name:'invoiceitem.quantity',
        align:'right',
        sortable:false,
      },
      {
        name:'invoiceitem.amount',
        align:'right',
        sortable:false,
      },
      {
        name:'invoiceitem.issuance',
        align:'right',
        sortable:false,
      },
      {
        name:'invoiceitem.freight',
        align:'right',
        sortable:false,
      },
      {
        name:'invoiceitem.total',
        align:'right',
        sortable:false,
      }
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
    jQuery(document).ready(
            function() {
              var lastsel;
              jQuery("#grid").jqGrid({
                url:listurl,
                editurl: editurl,
                datatype: "json",
                mtype: 'GET',
                colNames:colNames,
                colModel :colModel,
                pager: '#gridnav',
                rowNum:100,
                //rowList:[10,20,30],
                sortname: 'code',
                sortorder: 'code',
                viewrecords: true,
                caption: caption,
                autowidth: true,
                height:'auto',
                multiselect:false,rownumbers:true,rownumWidth:40, 
                onSelectRow: function(id) {
                  //if(id && id!==lastsel){
                  if (id) {
                    jQuery('#grid').jqGrid('restoreRow', lastsel);
                    jQuery('#grid').jqGrid('editRow', id, true);
                    lastsel = id;
                  }
                }
				/*
                gridComplete: function() {
                  var maxval = 0;
                  var maxrow = 1;
                  var col = $('#grid').jqGrid('getCol', 'invoiceitem.amount', false);
                  var ids = jQuery("#grid").jqGrid('getDataIDs');
                  for (var i = 0; i < ids.length; i++) {
                    var row = ids[i];
                    var val = col[row - 1];
                    if (val > maxval) {
                      maxval = val;
                      maxrow = row;
                    }
                  }
                  jQuery('#grid').setCell(maxrow, 'invoiceitem.amount', '', {'background-color': 'red'});
                },*/
              });
              jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:false},
              {}, // use default settings for edit
              {}, // use default settings for add
              {}, // delete instead that del:false we need this
              {multipleSearch : true}, // enable the advanced searching
              {closeOnEscape:true} /* allow the view dialog to be closed when user press ESC key*/
                //{reloadAfterSubmit:false} //delete parameters
                      );
              jQuery("#grid").jqGrid('navButtonAdd', '#gridnav', {
                caption: "Adjust",
                title: "Reorder Columns",
                onClickButton : function () {

                  $('#dialog').dialog('open');
                }
              });

              /*
               jQuery("#grid").jqGrid('navButtonAdd','#gridnav',{
               caption: "Country",
               title: "Reorder Columns",
               onClickButton : function (){
               alert("set country");
               }
               });
               jQuery("#grid").jqGrid('navButtonAdd','#gridnav',{
               caption: "Product",
               title: "Reorder Columns",
               onClickButton : function (){
               alert("set Product");
               }
               });
               jQuery("#grid").jqGrid('navButtonAdd','#gridnav',{
               caption: "Product",
               title: "Reorder Columns",
               onClickButton : function (){
               alert("set Difference");
               }
               });*/
              jQuery('#grid').setCell('2', 'product.color', '', {'background-color': 'red'});


              $("#dialog").dialog({
                bgiframe: true,
                autoOpen: false,
                modal: true,
                buttons: {
                  'Submit': function() {
                    //document.country.submit();
                    $("#country").submit();
                    $(this).dialog('close');
                  },
                  Cancel: function() {
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
      <table>
        <tbody>
        <tr class="prop">
          <td class="name"><g:message code="invoice.code.label" default="Invoice Code"/></td>
          <td class="value">${fieldValue(bean: instance, field: "code")}</td>
          <td class="name"><g:message code="invoice.bpcode.label" default="BP Code"/></td>
          <td class="value">${fieldValue(bean: instance, field: "business.code")}</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.reinvoice.label" default="Re Invoice"/></td>
          <td class="value"><g:textField name="name" maxlength="50" value="${instance.reInvoice}"/></td>
          <td class="name"><g:message code="invoice.invoiceDate.label" default="Invoice Date"/></td>
          <td class="value"><g:formatDate date="${fieldValue(bean: instance, field: "invoiceDate")}" format="yyyy-MM-dd"/></td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="customer.code.label" default="Customer Code"/></td>
          <td class="value">${fieldValue(bean: instance, field: "business.customer.code")}</td>
          <td class="name"><g:message code="customer.name.label" default="Customer Name"/></td>
          <td class="value">${fieldValue(bean: instance, field: "business.customer.name")}</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.totalamount.label" default="Amount"/></td>
          <td class="value">${fieldValue(bean: instance, field: "totalAmount")}</td>
          <td class="name"><g:message code="invoice.difference.label" default="Difference"/></td>
          <td class="value">${fieldValue(bean: instance, field: "totalDifference")}</td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.totalissuance.label" default="Issuance"/></td>
          <td class="value"><g:textField name="name" maxlength="50" value="${instance.totalIssuance}"/></td>
          <td class="name"><g:message code="invoice.totalfreight.label" default="Freight"/></td>
          <td class="value"><g:textField name="name" maxlength="50" value="${instance.totalFreight}"/></td>
        </tr>
        <tr class="prop">
          <td class="name"><g:message code="invoice.totalquantity.label" default="Quantity"/></td>
          <td class="value">${fieldValue(bean: instance, field: "totalQuantity")}</td>
          <td class="name"><g:message code="invoice.currency.label" default="Currency"/></td>
          <td class="value"></td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Save')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.export.label', default: 'Export')}"/></span>
    </div>
  </div>
  <table id="grid"></table>
  <div id="gridnav"></div>
  <div id="dialog" title="${message(code: 'invoice.adjust.label', default: 'adjust')}">
    <form id="country" action="" method="post">
      <fieldset>
        <label for="name">${message(code: 'invoiceitem.amount.label', default: 'Amount')}</label>
        <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all"/>
      </fieldset>
    </form>
  </div>
</div>
</body>
</html>
