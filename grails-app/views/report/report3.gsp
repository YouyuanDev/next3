

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'invoiceHeader.label', default: 'InvoiceHeader')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    		<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/report1" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
			 <span class="ui-icon ui-icon-refresh"></span>r
			</a>
			<a href="${resource(dir:'')}/${controllerName}/report1" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
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
	

        
           <table class="ui-widget ui-widget-content" style="width:100%;">
                    <thead>
                         <tr class="ui-widget-header">
                        
                      
    						 															
                    
			    <td>Store </td>
			    <td>Re-invoice </td>
			    <td>YEAR </td>
			    <td>Supplier Code </td>
			    <td>Sellier Inv </td>
			    

			    <td>ReSellier Inv </td>
			    <td>Sellier Amt </td>
			    <td>@ </td>
			    <td>Sellier Amt (RMB) </td>
			    <td>Qty </td>

			    <td>CIF </td>
			    <td>Tariff </td>
			    <td>=Freight +Agent </td>
			    <td>diff </td>
			    <td>Cost </td>
			    

			    <td>Who is in chg. </td>
			    <td>Month </td>
			    <td>Status </td>
			    <td>Dpt </td>
			    <td>Diff (<-10 or >10 will be Yellow) </td>


			    <td>AP-BL(RMB) </td>
			    <td>AP(RMB) </td>
			   

                        
                        </tr>
                    </thead>
                    <tbody>
	

		  <g:each in="${reportList}" status="i" var="reportInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="report2" id="${reportInstance.reinvoice}">${reportInstance.store}</g:link></td>
 			    <td>${reportInstance.reinvoice}</td>
			    <td>${reportInstance.year1}</td>  
			    <td>${reportInstance.suppliercode}</td>
			    <td>${reportInstance.sellierinv}</td>  
                            
 			    <td>${reportInstance.reSellierinv}</td>
			    <td>${reportInstance.sellieramt}</td>  
			    <td>${reportInstance.rate}</td>
			    <td>${reportInstance.sellieramt}</td>
 			    <td>${reportInstance.quantity}</td>

 			    <td>${reportInstance.sellieramt}</td>
			    <td>${reportInstance.tariff} </td>  
			    <td>${reportInstance.FreightAgent}</td>
			    <td></td>
 			    <td>${reportInstance.shipment_cost}</td>
			    
 			    <td></td>
			    <td></td>  
			    <td></td>
			    <td></td>
 			    <td></td>
                            
 			    <td>${reportInstance.apbl}</td>
			    <td>${reportInstance.bfdf}</td>  
                        
                        </tr>
                    </g:each>

                    </tbody>
                </table>
</div>
    </body>
</html>
