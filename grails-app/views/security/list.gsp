<html>
<head>
  <meta name="layout" content="main_crud"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
  <script type="text/javascript">
    var listurl = "${resource(dir:'')}/${controllerName}/listjson";
    var colNames = [
      "${message(code: 'common.id.label', default: 'Id')}",
      "${message(code: 'common.code.label', default: 'Code')}",
      "${message(code: 'common.name.label', default: 'Name')}",
      "${message(code: 'common.description.label', default: 'Description')}"
    ];
    var colModel = [
      //{name:'id', index:'id',formatter:linkformatter,hidden:true,key:true},
      {
        name:'id',
        index:'id',
        formatter:linkformatter,
        sortable:false,
        hidden:true
      },
      {
        name:'code',
        index:'code'
      },
      {
        name:'name',
        index:'name',
        editable:true,
        editoptions:{size:25}
      },
      {
        name:'description',
        index:'description',
        editable:true
      }
    ];
    var caption = "${entityName}&nbsp;";

    var rowNum = 10;
    function linkformatter(cellvalue, options, rowObject)
    {
      var linkurl = "<a href='${resource(dir:'')}/security/show/" + cellvalue + "'>" + cellvalue + "</a>"
      return linkurl;
    }

    jQuery(document).ready(
            function() {
              var lastsel;
              jQuery("#grid").jqGrid({
                url:listurl,
                datatype: "json",
                mtype: 'GET',
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
                height:'auto',
                multiselect: true,                                                //multiselect
                //multikey: "ctrlKey"												//
                //sortable: true												//grid column move
                //treeGrid: true, ExpandColumn : 'id'							//treegrid
                //rownumbers: true, rownumWidth: 40, gridview: true,			//scrolling
                //editurl:"someurl.php",
              });
              jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:true},
              {}, // use default settings for edit
              {}, // use default settings for add
              {}, // delete instead that del:false we need this
              {multipleSearch : true}, // enable the advanced searching
              {closeOnEscape:true} /* allow the view dialog to be closed when user press ESC key*/
                //{reloadAfterSubmit:false} //delete parameters
                      ).jqGrid('sortableRows');
              //jQuery("#grid").jqGrid('searchGrid', {multipleSearch:true} );
              jQuery("#grid").jqGrid('navButtonAdd', '#gridnav', {
                caption: "Columns",
                title: "Reorder Columns",
                onClickButton : function () {
                  alert("hello");
                  jQuery("#grid").jqGrid('columnChooser');
                }
              });
              jQuery("#grid").jqGrid('gridResize', {minWidth:350,maxWidth:800,minHeight:80, maxHeight:350});	//resize grid
              //jQuery("#grid").jqGrid('sortableRows');								//sortable rows
              $("#bedata").click(function() {
                jQuery("#grid").jqGrid('editGridRow', "new", {height:280,reloadAfterSubmit:false});
              });

              jQuery("#cm1").click(function() {
                var s;
                s = jQuery("#grid").jqGrid('getGridParam', 'selarrrow');
                alert(s);
              });
              jQuery("#cm1s").click(function() {
                jQuery("#grid").jqGrid('setSelection', "2");
              });
            });
  </script>
</head>
<body>
<input type="BUTTON" id="bedata" value="Edit Selected"/>
<a href="javascript:void(0)" id="cm1">Get Selected id's</a>
<a href="javascript:void(0)" id="cm1s">Select(Unselect) row 2</a>
<table id="grid"></table>
<div id="gridnav"></div>
</body>
</html>
