

function jqGrid(gridId,navId,caption,listurl,colNames,colModel,rowNum,sortName,sortOrder,navOptions,multiselect,rownumbers){
	jqGrid(gridId,navId,caption,listurl,colNames,colModel,rowNum,sortName,sortOrder,navOptions,multiselect,rownumbers,false);
}
function jqGrid(gridId,navId,caption,listurl,colNames,colModel,rowNum,sortName,sortOrder,navOptions,multiselect,rownumbers,hiddengrid){
	var gridId = "#"+gridId;
	var navId = "#"+navId;
	if(!caption) caption="";
	if(!rowNum) rowNum=20;
	if(!sortName) sortName="code";
	if(!sortOrder) sortOrder="code";
	if(!multiselect) multiselect=false;
	if(!rownumbers) rownumbers=false;
	var onSelectRow=false;
	var lastsel;
	var grid = jQuery(gridId).jqGrid({
        caption:caption,url:listurl,datatype: "json",mtype: 'GET',colNames:colNames,colModel :colModel,
        pager: navId,rowNum:rowNum,sortname:'code',sortorder:'code',viewrecords: true, //rowList:[10,20,30],
        autowidth:true,height:'auto',
		multiselect:multiselect,rownumbers:rownumbers,rownumWidth:40,
		hiddengrid: hiddengrid
    });
    grid.jqGrid('navGrid', navId, {view:false,del:false,edit:false,add:false,search:false},{},{},{},{multipleSearch:true,sopt:['eq','ne','lt','le','gt','ge','cn','nc']},{closeOnEscape:true});//edit,add,delete,search,//{reloadAfterSubmit:false} //delete parameters}
	return grid;
}
function resize_the_grid(grid)
{
	//var gridId = "#"+gridId;
	//$('#grid').fluidGrid({base:'#grid_wrapper', offset:-5});
	grid.fluidGrid({base:'#grid_wrapper', offset:-5});
}
function gridReload(grid,formId){
	var params = getFormInputs(formId);
	alert(params);
	grid.jqGrid('setGridParam',{url:listurl+"?"+params,page:1}).trigger("reloadGrid");
}     
function gridSelarrrow(grid){
	var selects = grid.jqGrid('getGridParam','selarrrow');
	var ids = [];
	for(var i in selects){
		var row = grid.jqGrid('getRowData',selects[i]);
		ids.push(row.key);
		//alert(i+" "+ret.invid);
	}
	return ids.join(",");
}
