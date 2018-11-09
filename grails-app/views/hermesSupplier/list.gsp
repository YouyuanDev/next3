

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
       <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesSupplier.label', default: 'HermesSupplier')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
       <div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/list" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-refresh"></span>r
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

             <table class="ui-widget ui-widget-content" style="width:100%;">
                    <thead>
                        <tr class="ui-widget-header">
                        
                           
                        
                            <td>${message(code: 'hermesSupplier.code.label', default: 'Code')}</td>
                        
                            <td>${message(code: 'hermesSupplier.name.label', default: 'Name')}</td>
                        
                           
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${hermesSupplierInstanceList}" status="i" var="hermesSupplierInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${hermesSupplierInstance.id}">${fieldValue(bean: hermesSupplierInstance, field: "code")}</g:link></td>
                        
                            
                        
                            <td>${fieldValue(bean: hermesSupplierInstance, field: "name")}</td>
                        
                            
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${hermesSupplierInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
