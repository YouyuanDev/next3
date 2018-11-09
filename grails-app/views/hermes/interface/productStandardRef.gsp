<html>
<head>
 <meta name="layout" content="main_blank"/>
   
      <style>
        .FixedTitleRow
        {
            position: relative; 
            top: expression(this.offsetParent.scrollTop); 
            z-index: 10;
            background-color: #E6ECF0;
        }
        
        .FixedTitleColumn
        {
            position: relative; 
            left: expression(this.parentElement.offsetParent.scrollLeft);
        }
        
        .FixedDataColumn
        {
            position: relative;
            left: expression(this.parentElement.offsetParent.parentElement.scrollLeft);
            background-color: #E6ECF0;
        }
    </style>
  <script type="text/javascript">
  function deleteRef(){
		 var selects = grid.jqGrid('getGridParam','selarrrow'); 
			var items = [];
			for(var i in selects){
				var rowNum = selects[i];
				var item = $("#grid").getRowData(rowNum).id;
				alert(item)
				items.push(item);
			}
			alert(123)
			if(items==null||items==''){
				alert("nothing to delete")
				return;
				}
			//var url = "${resource(dir:'')}/interface/exportPOProduct"+"?items="+items;
			//window.location.href=url
		  
		 }

  
  function pickdates(id){ jQuery("#"+id+"_sdate","#rowed6").datepicker({dateFormat:"yy-mm-dd"}); } 
  function resize_the_grid(){
		$('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
	}
	//var grid;   

  var caption = "";
  var editurl = "${resource(dir:'')}/${controllerName}/updateProductStandardReference"; 
  var listurl = "${resource(dir:'')}/${controllerName}/listProductStandardReference";
  var colNames = [
                  "Id",
                  "Label Name(Chinese)",
                  "Execute Standard",
                  "Valid Date",
                  "Safty Standard & Type",
                  "Remark"
        	    ];
        	    var colModel = [
			{name:'refid',index:'id',hidden:true,editable:true},
			{name:'productLabelNameZh',editable:true},
			{name:'executeStandard',hidden:false,editable:true},
			{name:'validDate',hidden:false,editable:true},
			{name:'saftyStandardType',hidden:false,editable:true},
			{name:'remark',hidden:false,editable:true}
			];
        	//	var navOptions = {view:false,del:false,edit:false,add:false,search:false};

	
  jQuery(document).ready(function() {
	  $("input#deleteRef").click(function(){
			if(window.confirm('Are you sure to delete the Reference?')){
				 
			}
						});
		var lastsel;
		jQuery("#grid").jqGrid({
			 
			url:listurl,
			
			editurl:editurl, //Kurt Added
			datatype: "json",
			mtype: 'POST', 
			colNames:colNames,
			colModel:colModel,
			pager: '#gridnav', 
			rowNum:30,
			viewrecords: true,//Kurt Updated 
			autowidth: true,
			multiselect: false, //Kurt Updated 
			height:'auto',
			rownumbers:true,
			//Kurt Added
			 onSelectRow: function(id) {
				if (id) {
					
					jQuery('#grid').jqGrid('restoreRow', lastsel);
					jQuery('#grid').jqGrid('editRow', id, true);
					lastsel = id;
				}
			}
		});
		jQuery("#grid").jqGrid('navGrid',"#gridnav",{view:false,edit:false,add:true,del:true,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true},true); 
		
	});

	 function gridReload(){
		 var query = $('#searchForm').formSerialize(); 
			jQuery("#grid").jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid");
	    }  
	 
    
  </script>
</head>
<body>


		 
<div class="main">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
					<span class="ui-jqgrid-title">Product Standard Reference</span>
				</div>
				<form id="searchForm" name="searchForm">
                  <div class="ui-dialog-content ui-widget-content">
      
                   <table  class="userForm">  
 
                      
		   <tr class="prop">
			 <td class="name"><g:message code="Label Name" default="Label Name"/>  </td>
			 <td class="value"><g:textField name="productLabelNameZh"   /></td>
			 <td class="name"><g:message code="executeStandard" default="Execute Standard"/> </td>
			 <td class="value"><g:textField name="executeStandard"   /></td>
			</tr>
                    <tr class="prop">
			 <td class="name"><g:message code="validDate" default="valid Date"/>  </td>
			 <td class="value"><g:textField name="validDate"   /></td>
			 <td class="name"><g:message code="saftyStandardType" default="Safty Standard Type"/>  </td>
			 <td class="value"><g:textField name="saftyStandardType"   /></td>
			
			</tr>     
			  <tr class="prop">   
			   <td class="name"><g:message code="remark" default="Remark"/>  </td>
			 <td class="value"><g:textField name="remark"   /></td>
			 </tr>
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="Search"/></span>
 	         <span><a href="${resource(dir:'')}/${controllerName}/exportStandardReference" class="save">Export</a></span>
				</div>
               </form>  
            </div>
            	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
</html>