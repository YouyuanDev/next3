<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main_list"/>
  <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
  <filterpane:includes/>
  <export:resource/>
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
<div class="nav">
  <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>
  <span class="menuButton"><a class="home" onclick="showElement('filterPane');
  return false;" href="">Filter</a></span>
  <span class="menuButton"><a class="excel" href="/next2/user/list?format=excel&extension=xls">EXCEL</a></span>
</div>
<div class="body">
  <filterpane:filterPane domainBean="User"/>&nbsp;
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="list">
    <table>
      <thead>
      <tr>
        <g:sortableColumn property="code" title="${message(code: 'menu.code.label', default: 'Code')}"/>
        <g:sortableColumn property="type" title="${message(code: 'menu.type.label', default: 'Type')}"/>
        <g:sortableColumn property="name" title="${message(code: 'menu.name.label', default: 'Name')}"/>
        <g:sortableColumn property="name" title="${message(code: 'menu.description.label', default: 'Description')}"/>
        <g:sortableColumn property="parent" title="${message(code: 'menu.parent.code.label', default: 'Parent')}"/>
        <g:sortableColumn property="parent" title="${message(code: 'menu.parent.name.label', default: 'Child')}"/>
        <g:sortableColumn property="grails_controller" title="${message(code: 'menu.grails_controller.label', default: 'Controller')}"/>
        <g:sortableColumn property="grails_action" title="${message(code: 'menu.grails_action.label', default: 'Action')}"/>
        <g:sortableColumn property="grails_param" title="${message(code: 'menu.grails_param.label', default: 'Param')}"/>

      </tr>
      </thead>
      <tbody>
      <g:each in="${menuInstanceList}" status="i" var="menuInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td><g:link action="show" id="${menuInstance.id}">${fieldValue(bean: menuInstance, field: "code")}</g:link></td>
          <td>${fieldValue(bean: menuInstance, field: "type")}</td>
          <td>${fieldValue(bean: menuInstance, field: "name")}</td>
          <td>${fieldValue(bean: menuInstance, field: "description")}</td>
          <td>${fieldValue(bean: menuInstance, field: "parent.code")}</td>
          <td>${fieldValue(bean: menuInstance, field: "parent.name")}</td>
          <td>${fieldValue(bean: menuInstance, field: "grails_controller")}</td>
          <td>${fieldValue(bean: menuInstance, field: "grails_action")}</td>
          <td>${fieldValue(bean: menuInstance, field: "grails_param")}</td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
  <div class="paginateButtons">
    <g:paginate total="${menuInstanceTotal}"/>
  </div>
</div>
</div>
<table id="list" width="90%"></table>
<div id="pager">
</body>
</html>
