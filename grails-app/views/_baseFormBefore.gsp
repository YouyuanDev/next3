<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">${title}</span>
  </div>
  <g:form name="${formId}" id="${formId}" controller="${controller}" action="${action}">
  <div class="ui-dialog-content ui-widget-content">
    
  </div>
  <div class="buttons">
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.hawb.label', default: 'Hawb')}"/></span>
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.receivedate.label', default: 'Receive Date')}"/></span>
  </div>
  </g:form>
</div>
