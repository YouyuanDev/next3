<html>
    <head>
		<meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesCustomer.label', default: 'HermesCustomer')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        
        
        <script>
        function createNewCustomer(){
    		 
    		
    		var createurl ="${resource(dir:'')}/${controllerName}/create";
    		//alert(updateurl);
    		document.forms["searchForm"].action = createurl;
    		document.forms["searchForm"].submit();

    		//var query = $('#searchForm').formSerialize(); 
    		//jQuery("#grid").jqGrid('setGridParam',{url:listurl+"?"+query,page:1}).trigger("reloadGrid");

    		}
        
        </script>
        
        
        
        
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
	
		<div class="main">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
					<span class="ui-jqgrid-title">Customer</span>
				</div>
              <g:form id="searchForm" name="searchForm" action="list" method="post">  
                  <div class="dialog">  
      
                   <table  class="userForm">  
 
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>ShortCode:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="customerShortCode" type='text' name='customerShortCode' value='${params.customerShortCode}' />  
     
                         </td> 
			  <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>LongCode:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="customerLongCode" type='text' name='customerLongCode' value='${params.customerLongCode}' />  
     
                         </td>  
                     
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>CateName:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="customerCateName" type='text' name='customerCateName' value='${params.customerCateName}' />  
     
                         </td> 
			  
                     </tr>
                           
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="submit" class="save" value="Search"/></span>
					<span class="button"><input type="button" class="save" onclick="createNewCustomer()" value="Create"/></span>
				</div>
               </g:form>  
            </div>
            <table class="ui-widget ui-widget-content" style="width:100%;">
                    <thead>
                        <tr class="ui-widget-header">
			  <td>${message(code: 'hermesCustomer.code.label', default: 'Code')}</td>
                          <td>${message(code: 'hermesCustomer.customerShortCode.label', default: 'ShortCode')}</td> 
			  <td>${message(code: 'hermesCustomer.customerLongCode.label', default: 'LongCode')}</td>
			  <td>${message(code: 'hermesCustomer.customerCateName.label', default: 'CateName')}</td>
			       <td>${message(code: 'hermesCustomer.parentCompany.label', default: 'ParentCompany')}</td>
			 
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${hermesCustomerInstanceList}" status="i" var="hermesCustomerInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${hermesCustomerInstance.id}">${fieldValue(bean: hermesCustomerInstance, field: "code")}</g:link></td>
                        
                           
                        
                            <td>${fieldValue(bean: hermesCustomerInstance, field: "customerShortCode")}</td>
                        
                            <td>${fieldValue(bean: hermesCustomerInstance, field: "customerLongCode")}</td>
                        
                            <td>${fieldValue(bean: hermesCustomerInstance, field: "customerCateName")}</td>
                        
                         <td>${fieldValue(bean: hermesCustomerInstance, field: "parentCompany")}</td>
                                                
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            <div class="paginateButtons">
				<g:paginate total="${count}" params="${params}" /> 
            </div>
		</div>
    </body>
</html>
