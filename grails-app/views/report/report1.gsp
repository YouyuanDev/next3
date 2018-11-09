

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'invoiceHeader.label', default: 'InvoiceHeader')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
		<script type="text/javascript">
		function export1(){
			
		 var store = document.report1.store.value;
		 var reinvoice = document.report1.reinvoice.value;
		 var year1	= document.report1.year1.value;
		 var suppliercode= document.report1.suppliercode.value;
		 var sellierinv=document.report1.sellierinv.value;
		 var inMonth=document.report1.inMonth.value;
		 //alert(store)
		
		var url = "${resource(dir:'')}/report/exportReport1?store="+store+"&reinvoice="+reinvoice+"&year1="+year1+"&suppliercode="+suppliercode+"&sellierinv="+sellierinv+"&inMonth="+inMonth;
		//alert(url)
		window.location.href=url

		//document.report1.target="_blank";
			//document.report1.action="${resource(dir:'')}/report/exportReport1";
			//document.report1.submit();
			//var url = "${resource(dir:'')}/interface/exportPurchaseOrder?id=${instance?instance.id:''}&items="+items;
			//window.open(url); 
		}

		function RefreshSaga(){
	    //alert("123");
	     var year1	= document.report1.year1.value;
	
		 var inMonth=document.report1.inMonth.value;
	    if(window.confirm('Are you sure to refresh Saga? It needs some time to calculate.')){
			 window.location.href="${resource(dir:'')}/report/refreshSaga?&year1="+year1+"&inMonth="+inMonth;
			}
			}
		</script>        
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
	






		<div class="main">
			<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
				<div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
					<span class="ui-jqgrid-title">Report</span>
				</div>
              <g:form action="report1" method="post" name="report1">  
                  <div class="dialog">  
      
                   <table  class="userForm">  
 
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="id" type='text' name='store' value='${params.store}' />  
     
                         </td> 
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Re-invoice:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="reinvoice" type='text' name='reinvoice' value='${params.reinvoice}' />  
     
                         </td> 	
			  <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Year:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             
     					<g:select id="year1" name='year1' from="${['2011','2012','2013','2014','2015','2016','2017','2018','2019','2020','2021','2022','2023','2024','2025','2026','2027','2028','2029','2030','All years']}" value='${params.year1}'>

                          </g:select> 
                         </td> 
                          <td valign='middle' style='text-align:left;' width='5%'>  
                             <label for='name'>Month:</label>  
                         </td>  
                         <td valign='middle' style='text-align:left;' width='10%'>  
                          <g:select  from="${['1','2','3','4','5','6','7','8','9','10','11','12','All']}" id="inMonth" name="inMonth" value="${params.inMonth}">

                          </g:select> 
                         </td> 
                     </tr>
                     <tr class='prop'>  

                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Supplier Code:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="suppliercode" type='text' name='suppliercode' value='${params.suppliercode}' />  
     
                         </td>
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Sellier Inv:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="sellierinv" type='text' name='sellierinv' value='${params.sellierinv}' />  
     
                         </td> 
                         <td></td>  
                         <td></td> 
                     </tr>
		     
		     
                           
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="submit" class="save" value="Search"/></span>&nbsp;&nbsp;
					<span><a href="javascript:export1();" class="save">Export</a></span>
					<span class="button"><a href="javascript:RefreshSaga();" class="save">[Refresh Saga (Year & Month)]</a></span>
 	    
					<input type="hidden" name="print"></input>
				</div>
               </g:form>  
            </div>

        
           <table class="ui-widget ui-widget-content" style="width:100%;">
                    <thead>
			<tr align="right">

			   <td></td>
			   <td></td>
			   <td></td> 
			   <td></td>
			   <td></td>

			   <td>SUBTOTAL</td>
			   <td>${subamt}</td>
			   <td></td>
			   <td>${subamtrmb}</td> 
			   <td>${subqty}</td>
			   
			   <td>${subtariff}</td>
			   <td>${subfa}</td>
			   <td>${subcost}</td>
			   <td></td>
			   <td></td>
			   
			   <td>${subapbl}</td>			   
                           <td>${subap}</td>
                           <td>${subaphl}</td>
                            
                        </tr>                    
                         <tr class="ui-widget-header" align="center">
                <td>Contract Code</td>
			    <td>&nbsp;&nbsp;&nbsp;Store&nbsp;&nbsp;&nbsp;</td>
			    <td>Re-invoice </td>
			    <td>YEAR </td>
			    <td>Supplier Code </td>
			    <td>Sellier Inv </td>
			    
				<!-- 
			    <td>ReSellier Inv </td>
			     -->
			    <td>Sellier Amt </td>
			    <td>@ </td>
			    <td>Sellier Amt (RMB) </td>
			    <td>Qty </td>
				<!-- 
			    <td>CIF </td>
			     -->
			    <td>Tariff </td>
			    <td>=Freight +Agent </td>
			    <!-- 
			    <td>diff </td>
			    -->
			    <td>Cost </td>
				
			    <td>Month </td>
			    <!-- 
		
			    <td>Status </td>
			     -->
			    <td>Dpt </td>
			    <!-- 
			    <td>Diff (<-10 or >10 will be Yellow) </td>
					-->

			    <td>AP-BL(RMB) </td>
			    <td>AP-Saga(RMB) </td>
			   <td>AP-HL(RMB) </td>

                        
                        </tr>
                    </thead>
                    <tbody>
	

		  <g:each in="${reportList}" status="i" var="reportInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" align="right">
                         <td>${reportInstance.contractcode}</td>
                            <td><g:link action="report2" id="${reportInstance.store}">${reportInstance.loc_id}</g:link></td>
 			    <!-- <td>${reportInstance.reinvoice}</td> -->
 			    <td>${reportInstance.so_code}</td>
			    <td>${reportInstance.year1}</td>  
			    <td>${reportInstance.suppliercode}</td>
			    <td>${reportInstance.sellierinv}</td>  
                   <!--          	
 			    <td></td>
 			    -->
			    <td>${reportInstance.sellieramt}</td>  
			    <td>${reportInstance.rate}</td>
			    <td>${reportInstance.sellieramtRMB}</td>
 			    <td>${reportInstance.quantity}</td>

 				 <!-- 
 			    <td> reportInstance.cif </td>
 			    -->
			    <td>${reportInstance.tariff} </td>  
			    <td>${reportInstance.FreightAgent}</td>
			    <!-- 
			    <td></td>
			     -->
 			    <td>${reportInstance.totalBLCost}</td>
			    
			    <td>${reportInstance.Month}</td>  
			    <!-- 
			    <td>${reportInstance.to_scala}</td>
			     -->
			    <td>${reportInstance.dept}</td>
			     
 			    <td>${reportInstance.apbl}</td>
			    <td>${reportInstance.bfdf}</td>  
                        <td>${reportInstance.aphl}</td>  
                        </tr>
                    </g:each>

                    </tbody>
                </table>
</div>
    </body>
</html>
