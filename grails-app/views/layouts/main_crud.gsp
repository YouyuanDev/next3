<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title><g:layoutTitle default="Next"/></title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <g:set var="entityName" value="${message(code: '${controllerName}.label', default: 'Security')}"/>
  <link rel="stylesheet" href="${resource(dir: 'js/jquery-ui-1.7.2/css/smoothness', file: 'jquery-ui-1.7.2.custom.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'js/jquery-ui-1.7.2/css', file: 'ui.jqgrid.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'next.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main_grid.css')}"/>
  <g:javascript src="jquery-ui-1.7.2/js/jquery-1.3.2.min.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/jquery-ui-1.7.2.custom.min.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/jquery.form.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/i18n/grid.locale-cn.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/jquery.jqGrid.min.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/ajaxupload.3.6.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/jquery.jqGrid.fluid.js"/>

  <g:javascript src="next3.js"/>
  <g:layoutHead/>
</head>
<body>
<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
  <div class="fg-buttonset ui-helper-clearfix">
    <g:link class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" action="list" title="Search">
      <span class="ui-icon ui-icon-search"></span>Search
    </g:link>
    <g:link class="create" action="create" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Create">
      <span class="ui-icon ui-icon-document"></span>Create
    </g:link>
  <!--
  <g:link class="create" action="create" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all">
    <span class="ui-icon ui-icon-disk"></span>Save
  </g:link>
  <g:link class="create" action="create" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all">
    <span class="ui-icon ui-icon-trash"></span>Delete
  </g:link>
  <g:link class="create" action="create" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all">
    <span class="ui-icon ui-icon-print"></span>Excel
  </g:link>
  -->
  </div>
</div>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<g:hasErrors bean="${securityInstance}">
  <div class="errors">
    <g:renderErrors bean="${securityInstance}" as="list"/>
  </div>
</g:hasErrors>

<div class="main">
  <g:if test="${actionName == 'list'}">
    <g:layoutBody/>
  </g:if>
  <g:else>
    <div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
      <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
        <span class="ui-jqgrid-title">${entityName}&nbsp;</span>
      </div>

      <g:if test="${actionName == 'create'}">
        <g:form action="save" method="post">
          <div class="ui-dialog-content ui-widget-content">
            <g:layoutBody/>
          </div>
          <div class="buttons">
            <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
          </div>
        </g:form>
      </g:if>
      <g:if test="${actionName == 'edit'}">
        <g:form method="post">
          <div class="ui-dialog-content ui-widget-content">
            <g:hiddenField name="id" value="${securityInstance?.id}"/>
            <g:hiddenField name="version" value="${securityInstance?.version}"/>
            <g:layoutBody/>
          </div>
          <div class="buttons">
            <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}"/></span>
            <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
          </div>
        </g:form>

      </g:if>
      <g:if test="${actionName == 'show'}">
        <div class="ui-dialog-content ui-widget-content" id="dialog">
          <g:layoutBody/>
        </div>
        <div class="buttons">
          <g:form>
            <g:hiddenField name="id" value="${securityInstance?.id}"/>
            <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
            <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
          </g:form>
        </div>
      </g:if>
    </div>
  </g:else>
</div>
</body>
</html>