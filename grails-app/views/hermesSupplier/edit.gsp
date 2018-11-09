

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesSupplier.label', default: 'HermesSupplier')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
      
	<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/edit/${hermesSupplierInstance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-refresh"></span>r
			</a>
			<a href="${resource(dir:'')}/${controllerName}/list" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-back"></span>b
			</a>
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
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
					<span class="ui-jqgrid-title"> Supplier</span>
				</div>
            <g:form method="post" >
                <g:hiddenField name="id" value="${hermesSupplierInstance?.id}" />
                <g:hiddenField name="version" value="${hermesSupplierInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="code"><g:message code="hermesSupplier.code.label" default="Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesSupplierInstance, field: 'code', 'errors')}">
                                    <g:textField name="code" maxlength="50" value="${hermesSupplierInstance?.code}" readonly="true" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="hermesSupplier.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesSupplierInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="50" value="${hermesSupplierInstance?.name}" />
                                </td>
                            </tr>
                        
                 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
