

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
       <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesProduct.label', default: 'HermesProduct')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
	<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/show/${hermesProductInstance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
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
					<span class="ui-jqgrid-title"> Product</span>
				</div>
            <div class="dialog">
                <table>
                    <tbody>
                    
                  
		      

		       <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="code"><g:message code="hermesProduct.code.label" default="Full" /></label>
                                </td>
				 <td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "code")}</td>
                                
                            </tr>
			    <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="category"><g:message code="hermesProduct.category.label" default="Short" /></label>
                                </td>
                                
				<td valign="top" class="value">${hermesProductInstance?.category?.code}</td>
                            </tr>

                        
			    <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="category"><g:message code="hermesProduct.category.label" default="Zh" /></label>
                                </td>
                                <td valign="top" class="value">
                                    ${hermesProductInstance?.category?.name}
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="code"><g:message code="hermesProduct.code.label" default="Code" /></label>
                                </td>
                               
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "code")}</td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dept"><g:message code="hermesProduct.dept.label" default="Dept" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "dept")}</td>
                               
                            </tr>
                        
                      
                             <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="material"><g:message code="hermesProduct.material.label" default="Material" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "category.description")}</td>
                                
                            </tr>
                             <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="material"><g:message code="hermesProduct.material2.label" default="Material2" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "category.description2")}</td>
                                
                            </tr>
                            
                            
                            
                            
                            
                            
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="magnitude"><g:message code="hermesProduct.magnitude.label" default="Magnitude" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "magnitude")}</td>
                                
                                
                            </tr>
                        
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="MagnDept" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "magnDept")}</td>
                                
                               
                            </tr>
                        







                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="Speciality" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "speciality")}</td>
                                
                               
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="wholslPrice" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "wholslPrice")}</td>
                                
                               
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="retailPrice" /></label>
                                </td>
				<td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "retailPrice")}</td>
                                
                                
                            </tr>

                    
                        <!--tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesProduct.styleCode.label" default="Style Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "styleCode")}</td>
                            
                        </tr>
                    
                     
                    
                
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesProduct.colorCode.label" default="Color Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "colorCode")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesProduct.colorName.label" default="Color Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "colorName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesProduct.sizeCode.label" default="Size Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "sizeCode")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesProduct.sizeName.label" default="Size Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesProductInstance, field: "sizeName")}</td>
                            
                        </tr-->
                    

                    
                       

                    
                        
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${hermesProductInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
