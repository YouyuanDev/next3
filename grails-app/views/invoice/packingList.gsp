<html>
<head>
  <meta name="layout" content="main_blank"/>
  <script type="text/javascript">
	var grid;
    var caption = "${message(code:'packing.search.label',default:'Packing Search')}";
    var listurl = "${resource(dir:'')}/${controllerName}/listPackingJson";
    var colNames = [
	  "Packing",
	  "BpCode",
	  "Re Invoice",
	  "Date"
    ];
    var colModel = [
      {name:'id',index:'id',formatter:linkformatter,sortable:false},
      {name:'bpcode',index:'bpcode',sortable:false},
      {name:'reInvoice',index:'reInvoice',sortable:false},
      {name:'date',index:'date',sortable:false}
    ];
	var navOptions = {view:false,del:false,edit:false,add:false,search:false};
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showPacking/" + cellvalue + "'>" + cellvalue + "</a>"
      return linkurl;
    }
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:-5});
    }
    jQuery(document).ready(function() {
		var lastsel;
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,null,null,null,navOptions,false,true);

		//$("#receiveDate").datepicker();
		//$('#searchForm').ajaxForm(); 

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
    <span class="ui-jqgrid-title">${message(code: 'packing.packing.label', default: 'Packing')}</span>
  </div>
  <form name="searchForm" id="searchForm">
  <div class="ui-dialog-content ui-widget-content">
    <table>
      <tbody>
      <tr class="prop">
        <td class="name"><g:message code="business.code.label" default="BP Code"/></td>
        <td class="value"><g:textField name="bpCode"/></td>
        <td class="name"><g:message code="packing.reinvoice.label" default="Re Invoice"/></td>
        <td class="value"><g:textField name="reInvoice"/></td>
      </tr>
      </tbody>
    </table>
  </div>
  <div class="buttons">
    <span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
  </div>
  </form>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
</body>
</html>
