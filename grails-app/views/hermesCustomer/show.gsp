
<%@ page import="hermes.HermesCustomer" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesCustomer.label', default: 'HermesCustomer')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
       <div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/show/${hermesCustomerInstance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
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
            
                  <div class="dialog">  
      
                   <table  class="userForm">  
                    
        
                   
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesCustomer.customerShortCode.label" default="Short Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "customerShortCode")}</td>
                            
                        </tr>                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesCustomer.customerLongCode.label" default="Long Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "customerLongCode")}</td>
                            
                        </tr>
                    

                    
                   
                    
                   
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesCustomer.customerCateName.label" default="Cate Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "customerCateName")}</td>
                            
                        </tr>
                    
                      
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="hermesCustomer.reInvoicePreCode.label" default="Re Invoice Pre Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "reInvoicePreCode")}</td>
                            
                        </tr>
                    
                                      
                      
                      		 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="curNum"><g:message code="hermesCustomer.curNum.label" default="Cur Num" /></label>
                                </td>
                                 <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "curNum")}</td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerDevIndex"><g:message code="hermesCustomer.customerDevIndex.label" default="customerDevIndex" /></label>
                                </td>
                                <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "customerDevIndex")}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerCateCode"><g:message code="hermesCustomer.customerCateCode.label" default="customerCateCode" /></label>
                                </td>
                               <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "customerCateCode")}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="whId"><g:message code="hermesCustomer.whId.label" default="whId" /></label>
                                </td>
                               <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "whId")}</td>
                            </tr>
                            
                             <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="locId"><g:message code="hermesCustomer.locId.label" default="locId" /></label>
                                </td>
                                <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "locId")}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="xmag"><g:message code="hermesCustomer.xmag.label" default="xmag" /></label>
                                </td>
                                <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "xmag")}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="hermesCustomer.name.label" default="name" /></label>
                                </td>
                                <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "name")}</td>
                            </tr>
                            
                             <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="hermesCustomer.type.label" default="type" /></label>
                                </td>
                                <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "type")}</td>
                            </tr>
                        
                        <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="parentCompany"><g:message code="hermesCustomer.parentCompany.label" default="parentCompany" /></label>
                                </td>
                                <td valign="top" class="value">${fieldValue(bean: hermesCustomerInstance, field: "parentCompany")}</td>
                            </tr>
                    
                        
                    
                       
                    
                        
                    
                    </tbody>
                </table>
 </div>  
	   <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${hermesCustomerInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
             
            </div>
            
        </div>
    </body>
</html>
