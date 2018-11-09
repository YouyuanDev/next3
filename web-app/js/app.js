dojo.require("dojo.parser");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dojo.date.locale");
dojo.require("dojo.date.stamp");

dojo.require("dijit.dijit");
dojo.require("dijit.Declaration");
dojo.require("dijit.form.Button");
dojo.require("dijit.Menu");
dojo.require("dijit.Tree");
dojo.require("dijit.Tooltip");
dojo.require("dijit.Dialog");
dojo.require("dijit.Toolbar");
dojo.require("dijit._Calendar");
dojo.require("dijit.ColorPalette");
dojo.require("dijit.Editor");
dojo.require("dijit._editor.plugins.LinkDialog");
dojo.require("dijit._editor.plugins.FontChoice");
dojo.require("dijit.ProgressBar");

dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.CheckBox");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.Form")

dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.AccordionContainer");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");


dojo.addOnLoad(function() {
    dojo.parser.parse();
    dijit.setWaiRole(dojo.body(), "application");
    var n = dojo.byId("preLoader");
    dojo.fadeOut({
        node:n,
        duration:1500,
        onEnd:function() {
            // dojo._destroyElement(n);
            dojo.style(n, "display", "none");
        }
    }).play();

    // make tooltips go down (from buttons on toolbar) rather than to the right
    //dijit.Tooltip.defaultPosition = ["above", "below"];
});


function getText(url) {
    var n = dojo.byId("preLoader");
    dojo.xhrGet({
        url: url,
        //method: "post",
        headers: { "Accept": "text/javascript" },
        /*
         load: function(response, ioArgs){
         dojo.fadeOut({
         node: n,
         onEnd: function(){
         dojo.byId("toBeReplaced").innerHTML = response;
         dojo.fadeIn({ node: n }).play();
         }
         }).play();
         */
        load: function(response, ioArgs) {
            dojo.byId("toBeReplaced").innerHTML = response;
            dojo.byId('toBeReplaced').show();
            dojo.byId("welcome").innerHTML = "";
            return response;
        },
        error: function(response, ioArgs) {
            dojo.byId("toBeReplaced").innerHTML = "An error occurred, with response: " + response;
            dojo.byId("welcome").innerHTML = "";
            return response;
        },
        handleAs: "javascript"
    });
}

function getFormInputs(formId){
	if(formId){
		formId="#"+formId;
	}else{
		formId="#searchForm";
	}
	var $inputs = $(formId+":input");
	// not sure if you wanted this, but I thought I'd add it.
	// get an associative array of just the values.
	var values = {};
	$inputs.each(function() {
		values[this.name] = $(this).val();
	});
	var params;
	for (var key in values) {
		//alert([key, object[key]].join("="));
		params = params + [key, values[key]].join("=") + '&';
	}
	return params;
}

function jqGrid(gridId,navId,caption,listurl,colNames,colModel,rowNum,sortName,sortOrder,navOptions,multiselect,rownumbers){
	var gridId = "#"+gridId;
	var navId = "#"+navId;
	if(!caption) caption="Caption";
	if(!rowNum) rowNum=20;
	if(!sortName) sortName="code";
	if(!sortOrder) sortOrder="code";
	if(!multiselect) multiselect=false;
	if(!rownumbers) rownumbers=false;

	var grid = jQuery(gridId).jqGrid({
        caption:caption,url:listurl,datatype: "json",mtype: 'GET',colNames:colNames,colModel :colModel,
        pager: navId,rowNum:rowNum,sortname:'code',sortorder:'code',viewrecords: true, //rowList:[10,20,30],
        autowidth:true,height:'auto',
		multiselect:multiselect,rownumbers:rownumbers,rownumWidth:40, 
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
function gridSelarrrow(grid,name){
	if(!name) name="id";
	var selects = grid.jqGrid('getGridParam','selarrrow');
	var ids = [];
	for(var i in selects){
		var row = grid.jqGrid('getRowData',selects[i]);
		ids.add(row.get(name));
		//alert(i+" "+ret.invid);
	}
	return ids.join(",");
}
