

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'invoiceHeader.label', default: 'InvoiceHeader')}" />
        <title>Report Details</title>
<script>

function export1(){
	 var month = document.report2.month.value;
	 var id=document.report2.id.value;
	 var year=document.report2.year.value;
	var invoiceno=document.report2.invoiceno.value;
	var supplier=document.report2.supplier.value;
	var podium=document.report2.podium.value;
	var paris=document.report2.paris.value;
	var stockno=document.report2.stockno.value;
	var shortref=document.report2.shortref.value;
	var dep=document.report2.dep.value;
	var nature=document.report2.nature.value;
	var magnitudecode=document.report2.magnitudecode.value;
	var rate=document.report2.rate.value;
	var bp=document.report2.bp.value;
	 
	 var url="${resource(dir:'')}/report/exportReport2?month="+month+"&id="+id+"&year="+year+"&invoiceno="+invoiceno+"&supplier="+supplier+"&podium="+podium+"&paris="+paris+"&stockno="+stockno+"&shortref="+shortref+"&dep="+dep+"&nature="+nature+"&magnitudecode="+magnitudecode+"&rate="+rate+"&bp="+bp
		 //alert(url)
	 //return
		window.location.href=url
	//document.report2.target="_blank";
	//document.report2.action="${resource(dir:'')}/report/exportReport2";
	//document.report2.submit();
}
</script>         
    </head>
    <body>
    		<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
			<div class="fg-buttonset ui-helper-clearfix">
			<a href="${resource(dir:'')}/${controllerName}/report2/${params?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
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
              <g:form action="report2" method="post" name="report2">  
                  <div class="dialog">  

                   <table  class="userForm">  
 
                     <tr class='prop'> 
                      	<td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Month:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                            
      <g:select id="month"  from="${['1','2','3','4','5','6','7','8','9','10','11','12','All months']}" name="month" value="${params.month}">

                          </g:select> 
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Store:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="id" type='text' name='id' value='${params.id}' />  
     
                         </td> 
			 			<td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Year:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             
     						<g:select id="year" name='year' from="${['2011','2012','2013','2014','2015','2016','2017','2018','2019','2020','2021','2022','2023','2024','2025','2026','2027','2028','2029','2030','All years']}" value='${params.year}'>

                          </g:select> 
                         </td>  
                     </tr>
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Reinvoice no:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="invoiceno" type='text' name='invoiceno' value='${params.invoiceno}' />  
     
                         </td> 
			 <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Supplier:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="supplier" type='text' name='supplier' value='${params.supplier}' />  
     
                         </td>  
							 <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Podium:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="podium" type='text' name='podium' value='${params.podium}' />  
     
                         </td>                           
                     </tr>
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Paris #:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="paris" type='text' name='paris' value='${params.paris}' />  
     
                         </td> 
			 <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Stock no:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="stockno" type='text' name='stockno' value='${params.stockno}' />  
     
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Short ref:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="shortref" type='text' name='shortref' value='${params.shortref}' />  
     
                         </td>                          
                     </tr>
                     <tr class='prop'>  

			 <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Dep:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="dep" type='text' name='dep' value='${params.dep}' />  
     
                         </td>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Nature:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                        
       					<g:select id="nature"  from="${['All','Seasonal','Non-Seasonal']}" name="nature" value="${params.nature}">

                          </g:select> 
                         </td> 
			 <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Magnitude code:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="magnitudecode" type='text' name='magnitudecode' value='${params.magnitudecode}' />  
     
                         </td>  
                     </tr>

                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>Rate:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="rate" type='text' name='rate' value='${params.rate}' />  
     
                         </td> 
			<td valign='top' style='text-align:left;' width='10%'>  
                             <label for='name'>BP code:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <input id="bp" type='text' name='bp' value='${params.bp}' />  
     
                         </td> 
                     </tr>
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="submit" class="save" value="Search"/></span>
					<span class="button"><input type="button" class="save" onclick="export1()"  value="Export"/></span>
					<input type="hidden" name="print"></input>
				</div>
               </g:form>  
            </div>
        
           <table class="ui-widget ui-widget-content" style="width:100%;">
	         
                    <thead>
		 
			<tr>
                        
                           <td></td> 
			   <td></td>
			   <td></td>
			   <td></td>
			   <td></td>

                           <td></td> 
			   <td></td>
			   <td></td>
			   <td></td>
			                              <td></td> 
			   <td></td>
			   <td></td>
			   <td>SUBTOTAL:</td>
			   <td>${totalqtyReport}</td>

			   <td>${totalamtReport}</td> 
			   <td></td>
			   <td>${totalamtrmb}</td>
			   <td>${totaldutyrmb}</td>
			   <td>${totalother}</td>

                           <td>${totalrmbReport}</td> 
			  

                        </tr>

			
                         <tr class="ui-widget-header">
                        
                           <td>Mon</td>
               <td>&nbsp;&nbsp;&nbsp;&nbsp;Store&nbsp;&nbsp;&nbsp;&nbsp;</td> 
               <td>Year</td>  
			   <td>Inv#</td>
			   <td>Sup</td>
			   <td>Podium</td>
			   <td>Paris#</td>
			   
			   <td>Stock No.</td>

                           <td>Short</td> 
                           <td>Description</td> 
			   <td>Dep</td>
			   <td>Nature</td>
			   <td>Magn</td>
			   <td>Qty</td>

			   <td>Amt <BR>EUR/HKD</td> 
			   <td>Rate</td>
			   <td>AMT <BR>RMB</td>
			   <td>Duty <BR>RMB</td>
			   <td>Other cost <BR>RMB</td>

                           <td>TOTAL <BR>RMB</td> 
			   <td>Unit <BR>Cost<BR>RMB</td>

                        </tr>
                    </thead>
                    <tbody>

                   <g:each in="${reportList}" status="i" var="reportInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                           
                            <td>${reportInstance.month1}</td>
                <td>${reportInstance.loc_id}</td>
                <td>${reportInstance.year1}</td>
			    <td>${reportInstance.re_invoice}</td>  
			    <td>${reportInstance.supplier}</td>
			    <td>${reportInstance.podium}</td>
			    <td>${reportInstance.invoiceheadercode}</td>
 			    <td>${reportInstance.longref}</td> 
                        
 			    <td>${reportInstance.sr}</td>
 			    <td>${reportInstance.name_zh}</td>
			    <td>${reportInstance.dept}</td>  
			    <td>${reportInstance.Nature}</td>  
			    <td>${reportInstance.magnitude}</td>
 			    <td>${reportInstance.quantity}</td>

 			    <td>${reportInstance.sellieramt}</td>
			    <td>${reportInstance.rate}</td>  
			    <td>${reportInstance.sellieramtrmb}</td>
			    <td>${reportInstance.duty}</td>
 			    <td>${reportInstance.OthercostRMB}</td>

 			    <td>${reportInstance.TOTALRMB}</td>
			    <td>${reportInstance.Unitcost}</td>  

                        
                            
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
</div>
    </body>
</html>
