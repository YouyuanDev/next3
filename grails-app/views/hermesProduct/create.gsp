
<%@ page import="hermes.HermesProduct" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'hermesProduct.label', default: 'HermesProduct')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${hermesProductInstance}">
            <div class="errors">
                <g:renderErrors bean="${hermesProductInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        

                        
                      
                  
                        
                    
                        
                     
                        
                      
                        
                    
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="category"><g:message code="hermesProduct.category.label" default="Category" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'category', 'errors')}">
                                    <g:select name="category.id" from="${next.Category.list()}" optionKey="id" value="${hermesProductInstance?.category?.id}" noSelection="['null': '']" />
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
                                    <label for="productCode"><g:message code="hermesProduct.productCode.label" default="Product Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'productCode', 'errors')}">
                                    <g:textField name="productCode" readonly="true" maxlength="20" value="${hermesProductInstance?.productCode}" />
                                </td>
                            </tr>
                        
                  
                        
                      
                        
         
                        

                        
                       
                        
                    

                        

                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="material"><g:message code="hermesProduct.material.label" default="Material" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: hermesProductInstance, field: 'material', 'errors')}">
                                    <g:textField name="material" maxlength="200" value="${hermesProductInstance?.material}" />
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
