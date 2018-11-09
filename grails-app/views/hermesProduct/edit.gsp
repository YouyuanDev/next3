

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesProduct.label', default: 'HermesProduct')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
	<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/edit/${hermesProductInstance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
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
            <g:form method="post" >
                <g:hiddenField name="id" value="${hermesProductInstance?.id}" />
                <g:hiddenField name="version" value="${hermesProductInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>





                   <!--   <tr class="prop">
                                <td valign="top" class="name">
                                 <label for="code"><g:message code="hermesProduct.code.label" default="Full" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'code', 'errors')}">
                                <g:message value="${hermesProductInstance?.code}" default="Short" />
                                    <g:textField name="code" readonly="true" maxlength="50" value="${hermesProductInstance?.code}" />
                                </td>
                            </tr> --> 
			    <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="category"><g:message code="hermesProduct.category.label" default="Short" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'category', 'errors')}">
                                    <g:textField name="category.code" readonly="true"  value="${hermesProductInstance?.category?.code}" />
                                </td>
                            </tr>

                        
			    <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="category"><g:message code="hermesProduct.category.label" default="Zh" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'category', 'errors')}">
                                    <g:textField name="category.name" value="${hermesProductInstance?.category?.name}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="code"><g:message code="hermesProduct.code.label" default="Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'code', 'errors')}">
                                    <g:textField name="code" readonly="true" maxlength="50" value="${hermesProductInstance?.code}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="dept"><g:message code="hermesProduct.dept.label" default="Dept" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'dept', 'errors')}">
                                    <g:textField name="dept" value="${hermesProductInstance?.dept}" />
                                </td>
                            </tr>
                        
                         
                        
             
                        
                          
                        

                
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="material"><g:message code="hermesProduct.material.label" default="Material" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'category.description', 'errors')}">
                                    <g:textField name="category.description" maxlength="200" value="${hermesProductInstance?.category.description}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="material"><g:message code="hermesProduct.material.label" default="Material2" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'category.description2', 'errors')}">
                                    <g:textField name="category.description2" maxlength="200" value="${hermesProductInstance?.category.description2}" />
                                </td>
                            </tr>
                            
                            
                            
                            
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="magnitude"><g:message code="hermesProduct.magnitude.label" default="Magnitude" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'magnitude', 'errors')}">
                                    <g:textField name="magnitude" maxlength="200" value="${hermesProductInstance?.magnitude}" />
                                </td>
                            </tr>
                        
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="MagnDept" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'mandept', 'errors')}">
                                    <g:textField name="magnDept"  maxlength="20" value="${hermesProductInstance?.magnDept}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="Speciality" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'mandept', 'errors')}">
                                    <g:textField name="speciality"  maxlength="20" value="${hermesProductInstance?.speciality}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="wholslPrice" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'wholslPrice', 'errors')}">
                                    <g:textField name="wholslPrice" readonly="true" maxlength="20" value="${hermesProductInstance?.wholslPrice}" />
                                </td>
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="productCode"><g:message code="hermesProduct.productCode.label" default="retailPrice" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'retailPrice', 'errors')}">
                                    <g:textField name="retailPrice" readonly="true" maxlength="20" value="${hermesProductInstance?.retailPrice}" />
                                </td>
                            </tr>











     
                        
                         
                            <!--tr class="prop">
                                <td valign="top" class="name">
                                  <label for="styleCode"><g:message code="hermesProduct.styleCode.label" default="Style Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'styleCode', 'errors')}">
                                    <g:textField name="styleCode" readonly="true" maxlength="20" value="${hermesProductInstance?.styleCode}" />
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="colorCode"><g:message code="hermesProduct.colorCode.label" default="Color Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'colorCode', 'errors')}">
                                    <g:textField name="colorCode" readonly="true" maxlength="20" value="${hermesProductInstance?.colorCode}" />
                                </td>
                            </tr>
			    <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="colorName"><g:message code="hermesProduct.colorName.label" default="Color Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'colorName', 'errors')}">
                                    <g:textField name="colorName" readonly="true" maxlength="200" value="${hermesProductInstance?.colorName}" />
                                </td>
                            </tr>
			    <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="sizeCode"><g:message code="hermesProduct.sizeCode.label" default="Size Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'sizeCode', 'errors')}">
                                    <g:textField name="sizeCode" readonly="true" maxlength="20" value="${hermesProductInstance?.sizeCode}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="sizeName"><g:message code="hermesProduct.sizeName.label" default="Size Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'sizeName', 'errors')}">
                                    <g:textField name="sizeName" readonly="true" maxlength="200" value="${hermesProductInstance?.sizeName}" />
                                </td>
                            </tr-->
                        
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
