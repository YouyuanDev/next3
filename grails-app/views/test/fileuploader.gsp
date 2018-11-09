<html>
<head>
  <meta name="layout" content="main"/>
  <title></title>
  <style>
  html, body {
    margin: 0;
    padding: 0;

  }
  </style>

  <script type="text/javascript">
    jQuery(document).ready(function() {
      var lastsel2;
      var gridwidth = $('.list').width();

      jQuery("#list").jqGrid({
        sortable: true,
        //shrinkToFit :false,
        datatype: 'clientSide',
        colNames:['Inv No','Date', 'Amount','Tax','Total','Notes'],
        colModel :[
          {
            name:'invid',
            index:'invid'
          },
          {
            name:'invdate',
            index:'invdate',
            editable:true,
            editoptions:{size:12, dataInit:function(el) {
              $(el).datepicker({dateFormat:'yy-mm-dd'});
            }, defaultValue: function() {
              var currentTime = new Date();
              var month = parseInt(currentTime.getMonth() + 1);
              month = month <= 9 ? "0" + month : month;
              var day = currentTime.getDate();
              day = day <= 9 ? "0" + day : day;
              var year = currentTime.getFullYear();
              return year + "-" + month + "-" + day;
            } },
            formoptions:{ rowpos:2, elmprefix:"(*)",elmsuffix:" yyyy-mm-dd" },
            editrules:{required:true}
          },
          {
            name:'amount',
            index:'amount',
            align:'right',
            stype:'select',
            editoptions:{value:":All;0.00:0.00;12:12.00;20:20.00;40:40.00;60:60.00;120:120.00"}
          },
          {
            name:'tax',
            index:'tax',
            align:'right',
            editable:true,
            edittype:'text',
            editoptions: {size:10, maxlength: 15}
          },
          {
            name:'total',
            index:'total',
            align:'right'
          },
          {
            name:'note',
            index:'note',
            sortable:false
          }
        ],
        onSelectRow: function(id) {
          if (id && id !== lastsel2) {
            jQuery('#list').jqGrid('restoreRow', lastsel2);
            jQuery('#list').jqGrid('editRow', id, true);
            lastsel2 = id;
          }
        },
        pager: '#pager',
        rowNum:10,
        rowList:[10,20,30],
        sortname: 'invid',
        sortorder: 'desc',
        viewrecords: true,
        caption: 'My first grid',
        autowidth: true
      });
      //jQuery("#list").jqGrid('editGridRow', rowid, properties );
      jQuery("#list").jqGrid('navGrid', '#pager', {view:true, del:true},
      {}, // use default settings for edit
      {            addCaption: "Add Record1",
        editCaption: "Edit Record",
        bSubmit: "Submit",
        bCancel: "Cancel",
        bClose: "Close",
        saveData: "Data has been changed! Save changes?",
        bYes : "Yes",
        bNo : "No",
        bExit : "Cancel",
      }, // use default settings for add
      {}, // delete instead that del:false we need this
      {multipleSearch : true}, // enable the advanced searching
      {closeOnEscape:true}).navButtonAdd("#pager",
      {
        caption:"Add",
        buttonicon:"ui-icon-add",
        onClickButton: function() {
          alert("Adding Row");
        },
        position:"last"
      });
      jQuery("#list").jqGridExport();
      jQuery("#list").jqGrid('filterToolbar', {autosearch:true});
      jQuery("#mysearch").jqGrid('filterGrid', '#list');

      var myfirstrow1 = {invid:"1", invdate:"2007-10-01", note:"note", amount:"200.00", tax:"10.00", total:"210.00"};
      var myfirstrow2 = {invid:"2", invdate:"2007-10-01", note:"note", amount:"200.00", tax:"10.00", total:"210.00"};
      jQuery("#list").addRowData("1", myfirstrow1);
      jQuery("#list").addRowData("2", myfirstrow2);
      jQuery("#list").jqGrid('gridResize', {minWidth:350,maxWidth:800,minHeight:80, maxHeight:350});
      //jQuery("#list").setGridWidth('100%',true);
      jQuery("#list").jqGrid('navButtonAdd', '#pager', {
        caption: "Columns",
        title: "Reorder Columns",
        onClickButton :
                function () {
                  jQuery("#list").jqGrid('columnChooser');
                }
      });
    });

  </script>

</head>
<body>
<table id="list" width="100%" border="1"></table>
<div id="pager"></div>
<table id="list2" width="100%" border="1"></table>
<div id="pager2"></div>

</body>
</html>
