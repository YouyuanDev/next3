<html>
<head>
<meta name="layout" content="main_blank"/>
<script type="text/javascript">
	var grid;
    var caption = "";
    var listurl = "${resource(dir:'')}/${controllerName}/listPackingJson";
    var colNames = [
	  "Packing",
	  "BpCode",
	  "Re Invoice",
	  "Date"
    ];
    
    var colModel = [
      {name:'id',index:'id',hidden:true,sortable:true},
      {name:'bpcode',index:'bpcode',formatter:linkformatter,sortable:true},
      {name:'reInvoice',index:'reInvoice',sortable:false},
      {name:'date',index:'date',sortable:true}
    ];
	var navOptions = {view:false,del:false,edit:false,add:false,search:false};
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/${controllerName}/showPacking/" + rowObject[0] + "'>" + cellvalue + "</a>"
      return linkurl;
    }
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:-5});
    }
    jQuery(document).ready(function() {
		var lastsel;
		grid=jqGrid('grid','gridnav',
				caption,
				listurl,
				colNames,
				colModel,
				null,//jsonreader
				null,//width
				null,//height
				navOptions,
				false,
				true
			);
		grid.jqGrid('setGridParam',
				{
					sortname:"id",
					sortorder: "desc"
				}
		); 

		//$("#receiveDate").datepicker();
		//$('#searchForm').ajaxForm(); 
		//$("#officeItemList").
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

<div class="main">	
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
	      <td class="value"><!-- <g:textField name="bpCode"/> -->
	      <select name="bpCode" id="bpCode" >
				<option value="" ></option>
				<g:each in="${busiList}" status="i" var="Busi">
				    <option value="${Busi.code}" >${Busi.code}</option>
				</g:each>
			</select>
	      
	      </td>
	      <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='middle' style='text-align:left;' width='20%'>  
                             <g:select id="store" from="${storeList}" name="store" ></g:select> 
                         </td> 
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
	</div>
	<div class="buttons">
	  <span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
	</div>
	</form>
</div>
<table id="grid"></table>
<div id="gridnav"></div>
</div>
</body>
</html>
