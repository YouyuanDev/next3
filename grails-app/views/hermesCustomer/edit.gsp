

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        	<meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesCustomer.label', default: 'HermesCustomer')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
	<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/edit/${hermesCustomerInstance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
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
					<span class="ui-jqgrid-title">Customer</span>
				</div>
            <g:form method="post" >
                <g:hiddenField name="id" value="${hermesCustomerInstance?.id}" />
                <g:hiddenField name="version" value="${hermesCustomerInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="customerShortCode"><g:message code="hermesCustomer.customerShortCode.label" default="Short Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerShortCode', 'errors')}">
                                    <g:textField name="customerShortCode"  maxlength="20" value="${hermesCustomerInstance?.customerShortCode}" />
                                </td>
                            </tr>                           
                           
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="customerLongCode"><g:message code="hermesCustomer.customerLongCode.label" default="Long Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerLongCode', 'errors')}">
                                    <g:textField name="customerLongCode"  maxlength="20" value="${hermesCustomerInstance?.customerLongCode}" />
                                </td>
                            </tr>
                        

                        
                      
                          
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="customerCateName"><g:message code="hermesCustomer.customerCateName.label" default="Cate Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerCateName', 'errors')}">
                                    <g:textField name="customerCateName"  maxlength="200" value="${hermesCustomerInstance?.customerCateName}" />
                                </td>
                            </tr>
                        
                         
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="reInvoicePreCode"><g:message code="hermesCustomer.reInvoicePreCode.label" default="Re Invoice Pre Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'reInvoicePreCode', 'errors')}">
                                    <g:textField name="reInvoicePreCode" value="${hermesCustomerInstance?.reInvoicePreCode}" />
                                </td>
                            </tr>
                        
                      
                      		 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="curNum"><g:message code="hermesCustomer.curNum.label" default="Cur Num" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'curNum', 'errors')}">
                                    <g:textField name="curNum" type="number"  value="${fieldValue(bean: hermesCustomerInstance, field: 'curNum')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerDevIndex"><g:message code="hermesCustomer.customerDevIndex.label" default="customerDevIndex" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerDevIndex', 'errors')}">
                                    <g:textField name="customerDevIndex" maxlength="20" value="${fieldValue(bean: hermesCustomerInstance, field: 'customerDevIndex')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerCateCode"><g:message code="hermesCustomer.customerCateCode.label" default="customerCateCode" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerCateCode', 'errors')}">
                                    <g:textField name="customerCateCode" maxlength="20"  value="${fieldValue(bean: hermesCustomerInstance, field: 'customerCateCode')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="whId"><g:message code="hermesCustomer.whId.label" default="whId" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'whId', 'errors')}">
                                    <g:textField name="whId" maxlength="200"  value="${fieldValue(bean: hermesCustomerInstance, field: 'whId')}" />
                                </td>
                            </tr>
                            
                             <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="locId"><g:message code="hermesCustomer.locId.label" default="locId" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'locId', 'errors')}">
                                    <g:textField name="locId" maxlength="200" value="${fieldValue(bean: hermesCustomerInstance, field: 'locId')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="xmag"><g:message code="hermesCustomer.xmag.label" default="xmag" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'xmag', 'errors')}">
                                    <g:textField name="xmag" maxlength="2"  value="${fieldValue(bean: hermesCustomerInstance, field: 'xmag')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="hermesCustomer.name.label" default="name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="50" value="${fieldValue(bean: hermesCustomerInstance, field: 'name')}" />
                                </td>
                            </tr>
                            
                             <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="hermesCustomer.type.label" default="type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'type', 'errors')}">
                                <g:select id="type" from="['HermesCustomer','HermesSupplier']" value="${fieldValue(bean: hermesCustomerInstance, field: 'type')}" name="type" ></g:select> 
                                    
                                </td>
                            </tr>
                         
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="hermesCustomer.parentCompany.label" default="parentCompany" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'parentCompany', 'errors')}">
                                <g:select id="parentCompany" from="['A. Hermes (China) CO., LTD','B. Hermes (China) Trading Co., Ltd']" value="${fieldValue(bean: hermesCustomerInstance, field: 'parentCompany')}" name="parentCompany" ></g:select> 
                                    
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
