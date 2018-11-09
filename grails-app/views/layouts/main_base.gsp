<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title><g:layoutTitle default="Next"/></title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <link rel="stylesheet" href="${resource(dir: 'js/jquery-ui-1.7.2/css/smoothness', file: 'jquery-ui-1.7.2.custom.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'next.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main_grid.css')}"/>
  <g:javascript src="jquery-ui-1.7.2/js/jquery-1.3.2.min.js"/>
  <g:javascript src="jquery-ui-1.7.2/js/ajaxupload.3.6.js"/>
  <g:layoutHead/>
</head>
<body>
<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<g:hasErrors bean="${securityInstance}">
  <div class="errors">
    <g:renderErrors bean="${securityInstance}" as="list"/>
  </div>
</g:hasErrors>
<div class="main">
  <div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
    <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
      <span class="ui-jqgrid-title">${pageProperty(name: 'page.formtext')}</span>
    </div>
    <div class="ui-dialog-content ui-widget-content">
      <g:layoutBody/>
    </div>
  </div>
</div>
</body>
</html>