
<%@ page import="hermes.HermesCustomer" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'hermesCustomer.label', default: 'HermesCustomer')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${hermesCustomerInstance}">
            <div class="errors">
                <g:renderErrors bean="${hermesCustomerInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="saveMyCustomer" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                        
                              <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerShortCode"><g:message code="hermesCustomer.customerShortCode.label" default="Customer Short Code ( For Ex. G0097267)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerShortCode', 'errors')}">
                                    <g:textField required="true" name="customerShortCode"  maxlength="20" value="${hermesCustomerInstance?.customerShortCode}" />
                                </td>
                            </tr>                      
                         
                     
                       
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerLongCode"><g:message code="hermesCustomer.customerLongCode.label" default="Customer Long Code ( For Ex.: G009726702)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerLongCode', 'errors')}">
                                    <g:textField required="true" name="customerLongCode"  maxlength="20" value="${hermesCustomerInstance?.customerLongCode}" />
                                </td>
                            </tr>
                        

                        
                   
                        
                    
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerCateName"><g:message code="hermesCustomer.customerCateName.label" default="Customer Cate Name ( For Ex.: MAGASINS DES FILIALES)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerCateName', 'errors')}">
                                    <g:textField required="true" name="customerCateName"  maxlength="200" value="${hermesCustomerInstance?.customerCateName}" />
                                </td>
                            </tr>
                        
                           
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reInvoicePreCode"><g:message code="hermesCustomer.reInvoicePreCode.label" default="Re Invoice Pre Code ( For Ex.: SH)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'reInvoicePreCode', 'errors')}">
                                    <g:textField required="true" name="reInvoicePreCode" maxlength="200"  value="${hermesCustomerInstance?.reInvoicePreCode}" />
                                </td>
                            </tr>
                        
                         
                        
                     
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="curNum"><g:message code="hermesCustomer.curNum.label" default="Cur Num ( For Ex.: 1)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'curNum', 'errors')}">
                                    <g:textField required="true" name="curNum" type="number"  value="${fieldValue(bean: hermesCustomerInstance, field: 'curNum')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerDevIndex"><g:message code="hermesCustomer.customerDevIndex.label" default="Customer Dev Index ( For Ex.: 2)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerDevIndex', 'errors')}">
                                    <g:textField required="true" name="customerDevIndex" maxlength="20" value="${fieldValue(bean: hermesCustomerInstance, field: 'customerDevIndex')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customerCateCode"><g:message code="hermesCustomer.customerCateCode.label" default="Customer Cate Code ( For Ex.: 411)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'customerCateCode', 'errors')}">
                                    <g:textField required="true" name="customerCateCode" maxlength="20"  value="${fieldValue(bean: hermesCustomerInstance, field: 'customerCateCode')}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="whId"><g:message code="hermesCustomer.whId.label" default="whId ( For Ex.: SH-66)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'whId', 'errors')}">
                                    <g:textField required="true" name="whId" maxlength="200"  value="${fieldValue(bean: hermesCustomerInstance, field: 'whId')}" />
                                </td>
                            </tr>
                            
                             <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="locId"><g:message code="hermesCustomer.locId.label" default="locId ( For Ex.: SH-66)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesCustomerInstance, field: 'locId', 'errors')}">
                                    <g:textField required="true" name="locId" maxlength="200" value="${fieldValue(bean: hermesCustomerInstance, field: 'locId')}" />
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
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
