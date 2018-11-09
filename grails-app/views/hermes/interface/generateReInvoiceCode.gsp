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
  function pickdates(id){ jQuery("#"+id+"_sdate","#rowed6").datepicker({dateFormat:"yy-mm-dd"}); } 
  function resize_the_grid(){
		$('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
	}
	//var grid;   

  var caption = "";
  var editurl = "${resource(dir:'')}/${controllerName}/updateStoreSequence"; 
  var listurl = "${resource(dir:'')}/${controllerName}/listStoreSequence";
  var colNames = [
                  "Id",
                  "Name",
                  "Name",
                  "Year",
        		  "current_Value",
        	      "increment"
        	    ];
        	    var colModel = [
			{name:'seqid',hidden:true,editable:true},
			{name:'name',hidden:true,editable:true},
			{name:'name2',hidden:false,editable:false},
			{name:'year',hidden:false,editable:false},
			{name:'currentValue',hidden:false,editable:true},
			{name:'increment',hidden:false,editable:false}
			];
        		var navOptions = {view:false,del:false,edit:false,add:false,search:false};

	
  jQuery(document).ready(function() {

		var lastsel;
		jQuery("#grid").jqGrid({
			 
			url:listurl,
			
			editurl:editurl, //Kurt Added
			
			datatype: "json",
			mtype: 'GET', 
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
		jQuery("#grid").jqGrid('navGrid',"#gridnav",{view:false,edit:false,add:false,del:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true}); 
		
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
					<span class="ui-jqgrid-title">Generate ReInvoice Code</span>
				</div>
				<form id="searchForm" name="searchForm">
                  <div class="ui-dialog-content ui-widget-content">
      
                   <table  class="userForm">  
 
                     <tr class='prop'>  
                        
			 <td valign='middle' style='text-align:left;' width='10%'>  
                             <label for='name'>Year:</label>  
                         </td>  
                         <td valign=''middle'' style='text-align:left;' width='20%'>  
                          <g:select  from="${['09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59','60','61','62','63','64','65','66','67','68','69','70','71','72','73','74','75','76','77','78','79','80','81','82','83','84','85','86','87','88','89','90','91','92','93','94','95','96','97','98','99']}" name="inYear" value="${inYear}">

                          </g:select> 
                         </td> 
			 
                     </tr>
		   
                           
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="button" class="save" onclick="gridReload()"  action="update" value="Search"/></span>
 
			         
				</div>
               </form>  
            </div>
            	<table id="grid"></table>
	<div id="gridnav"></div>
</div>
</body>
</html>