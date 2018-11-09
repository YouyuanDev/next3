<html>
<head>
<meta name="layout" content="main_blank"/>
<script type="text/javascript">
	var grid;
    var caption = ""; var rowNum=40;
    var listurl = "${resource(dir:'')}/${controllerName}/listShipmentsJson";
    var colNames = ["Id","Code","BPs","运杂","代理","汇率","百福东方","宝隆关税","Desc","Status"];
    var colModel = [
      {name:'id',index:'id',hidden:true,sortable:false},
      {name:'code',index:'code',formatter:linkformatter,sortable:false},
      {name:'bps',index:'bps',sortable:false},
      {name:'yz',index:'bps',sortable:false},
      {name:'dl',index:'bps',sortable:false},
      {name:'rate',index:'bps',sortable:false},
      {name:'bfdf',index:'bps',sortable:false},
      {name:'bal',index:'bps',sortable:false},
      {name:'description',index:'description',sortable:false},
      {name:'submit',index:'submit',sortable:false}
    ];
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showShipment/" + rowObject[0] + "'>" + cellvalue + "</a>"
      return linkurl;
    }
	var navOptions = {view:true,del:false,edit:false,add:false,search:false};
	jQuery(document).ready(function() {
		grid=jqGrid('grid','gridnav',caption,listurl,colNames,colModel,rowNum,
				 null,null,navOptions,false,true);
		$("#reInvoice").autocomplete('${resource(dir:'')}/${controllerName}/listBusinessJson', {
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
		$("#reInvoice").result(function(event, data, formatted) {
			var hidden = $('#reInvoiceHidden');
			hidden.val(data.code);
		});
	});
	function formatItem(row) {
		return row.desc;
	}
	function formatResult(row) {
		return row[0].replace(/(<.+?>)/gi,'');
	}
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
    }
	$(window).resize(resize_the_grid);
    function gridReload(){
		var query = $('#searchForm').formSerialize(); 
		grid.jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid"); 
    }  
</script>
</head>
<body>
<div class="main">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">${message(code: 'shipment.shipment.label', default: 'Shipment')}</span>
  </div>
  <div class="ui-dialog-content ui-widget-content">
	<form name="searchForm" id="searchForm">
    <table border="0">
      <tbody>
      <tr class="prop">
        <td width="5%"><g:message code="shipment.code.label" default="Shipment Code"/></td>
        <td  width="20%" ><g:textField name="code"/></td>
         <td  width="5%">  <g:message code="shipment.Store.label" default="Store"/></td>  
         <td  width="20%"><g:select id="store" from="${getStoreList()}" name="store" ></g:select> 
		 </td> 
        <td  width="5%">Re Invoice</td>
        <td  width="20%"><g:textField name="reInvoice" id="reInvoice" length="80"/><input id="reInvoiceHidden" length="80" type="hidden"></td>
      </tr>
       <tr  class="prop">
                          <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Year:</label>  
                         </td>  
          <td valign='middle' style='text-align:left;' width='20%'>  
                          <g:select  from="${['this year','2010','2011','2012','2013','2014','2015','2016','2017','2018','2019','2020','2021','2022','2023','2024','2025','2026','2027','2028','2029','2030','2031','2032','2033','2034','2035','2036','2037','2038','2039','2040','2041','2042','2043','2044','2045','2046','2047','2048','2049','2050','2051','2052','2053','2054','2055','2056','2057','2058','2059','2060','2061','2062','2063','2064','2065','2066','2067','2068','2069','2070','2071','2072','2073','2074','2075','2076','2077','2078','2079','2080','2081','2082','2083','2084','2085','2086','2087','2088','2089','2090','2091','2092','2093','2094','2095','2096','2097','2098','2099']}" name="inYear" value="${inYear}">

                          </g:select> 
                         </td> 
                          <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Month:</label>  
                         </td>  
                          <td valign='middle' style='text-align:left;' width='20%'>  
                          <g:select  from="${['this month','1','2','3','4','5','6','7','8','9','10','11','12','All']}" name="inMonth" value="${inMonth}">

                          </g:select> 
                         </td> 
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
</div>
</body>
</html>
