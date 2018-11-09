<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main_blank"/>
        <g:set var="entityName" value="${message(code: 'hermesProduct.label', default: 'HermesProduct')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        
        <script>
function export1(){

	
	 var url="${resource(dir:'')}/report/exportProductList"
		// alert("Please wait...")
	 //return
		window.location.href=url

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
					<span class="ui-jqgrid-title">Product</span>
				</div>
              <g:form action="list" method="post">  
                  <div class="dialog">  
       
                   <table  class="userForm">  
 
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>code:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="code" type='text' name='code' value='${params.code}' />  
     
                         </td> 
			  <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>dept:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="dept" type='text' name='dept' value='${params.dept}' />  
     
                         </td>  
                     </tr>
		     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>productCode:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="productCode" type='text' name='productCode' value='${params.productCode}' />  
     
                         </td> 
			  <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>material:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="material" type='text' name='material' value='${params.material}' />  
     
                         </td>  
                     </tr>
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>magnitude:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="magnitude" type='text' name='magnitude' value='${params.magnitude}' />  
     
                         </td> 
			  <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>styleCode:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="styleCode" type='text' name='styleCode' value='${params.styleCode}' />  
     
                         </td>  
                     </tr>  
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>colorCode:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="colorCode" type='text' name='colorCode' value='${params.colorCode}' />  
     
                         </td> 
			  <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>colorName:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="colorName" type='text' name='colorName' value='${params.colorName}' />  
     
                         </td>  
                     </tr>  
                     <tr class='prop'>  
                         <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>sizeCode:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="sizeCode" type='text' name='sizeCode' value='${params.sizeCode}' />  
     
                         </td> 
			  <td valign='top' style='text-align:left;' width='20%'>  
                             <label for='name'>sizeName:</label>  
                         </td>  
                         <td valign='top' style='text-align:left;' width='30%'>  
                             <input id="sizeName" type='text' name='sizeName' value='${params.sizeName}' />  
     
                         </td>  
                     </tr>  
                  </table>      
                  </div>  
				<div class="buttons">
					<span class="button"><input type="submit" class="save" value="Search"/></span>
					<!-- <span class="button"><input type="button" class="save" onclick="export1()" value="Export All"/></span> -->
					
				</div>
               </g:form>  
            </div>
             <table class="ui-widget ui-widget-content" style="width:100%;">
                    <thead>
                        <tr class="ui-widget-header">
                        
                           
                        
                            <td>${message(code: 'hermesProduct.code.label', default: 'Code')}</td>
			    <td>${message(code: 'hermesProduct.dept.label', default: 'dept')}</td>
                        
                           
                        
                            <td>${message(code: 'hermesProduct.material.label', default: 'Material')}</td>
                        
                        
                        <td>${message(code: 'hermesProduct.material.label', default: 'Material2')}</td>
                            <td>${message(code: 'hermesProduct.magnitude.label', default: 'Magnitude')}</td>
                        
                            <td>${message(code: 'hermesProduct.styleCode.label', default: 'Style Code')}</td>

			    <td>${message(code: 'hermesProduct.colorCode.label', default: 'Color Code')}</td>
			    <td>${message(code: 'hermesProduct.colorName.label', default: 'ColorName')}</td>
			    <td>${message(code: 'hermesProduct.sizeCode.label', default: 'SizeCode')}</td>
			    <td>${message(code: 'hermesProduct.sizeName.label', default: 'SizeName')}</td>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${hermesProductInstanceList}" status="i" var="hermesProductInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${hermesProductInstance.id}">${fieldValue(bean: hermesProductInstance, field: "code")}</g:link></td>
                        
                            <td>${fieldValue(bean: hermesProductInstance, field: "dept")}</td>
                        
                            <!-- <td>${fieldValue(bean: hermesProductInstance, field: "productCode")}</td> -->
                        
                            <!-- <td>${fieldValue(bean: hermesProductInstance, field: "material")}</td>-->
                            <td>${fieldValue(bean: hermesProductInstance, field: "category.description")}</td>
                            <td>${fieldValue(bean: hermesProductInstance, field: "category.description2")}</td>
                            <td>${fieldValue(bean: hermesProductInstance, field: "magnitude")}</td>
                        
                            <td>${fieldValue(bean: hermesProductInstance, field: "styleCode")}</td>

                            <td>${fieldValue(bean: hermesProductInstance, field: "colorCode")}</td>
                            <td>${fieldValue(bean: hermesProductInstance, field: "colorName")}</td>
                            <td>${fieldValue(bean: hermesProductInstance, field: "sizeCode")}</td>
                            <td>${fieldValue(bean: hermesProductInstance, field: "sizeName")}</td>
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
   