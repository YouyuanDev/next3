<html>
<head>
  <meta name="layout" content="main_blank"/>
  <title>Invoice</title>
  <script type="text/javascript">
 
  $(document).ready(function() {
      var upload = new AjaxUpload('#UploadButton', {
        action: "${resource(dir:'')}/${controllerName}/uploadPackingSheet/",
        data : {
          'key1' : "This data won't",
          'key2' : "be send because",
          'key3' : "we will overwrite it"
        },
        //autoSubmit: true,
        onSubmit : function(file, ext) {
          //if (ext && new RegExp('^(' + allowed.join('|') + ')$').test(ext)){
          //if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)){
          if (ext && /^(txt)$/.test(ext)) {
            this.setData({
              'key': 'hermes_packingsheet',
              'packingId':$("#id").val()
            });
            $('#UploadForm .logmessage').text('Uploading ' + file);
          } else {
            // extension is not allowed
            $('#UploadForm .logmessage').text('Error: only file in txt format is allowed');
            // cancel upload
            return false;
          }
        },
        onComplete : function(file,response) {
          $('#UploadForm .logmessage').text( response);
        }
      });
      upload.setData({'key': 'hermes_packingsheet'});
//增加Carton PDF upload start
 var uploadCarton = new AjaxUpload('#UploadCartonButton', {
        action: "${resource(dir:'')}/${controllerName}/uploadPackingCartonPDF/",
        data : {
          'key1' : "This data won't",
          'key2' : "be send because",
          'key3' : "we will overwrite it"
        },
        //autoSubmit: true,
        onSubmit : function(file, ext) {
          //if (ext && new RegExp('^(' + allowed.join('|') + ')$').test(ext)){
          //if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)){
          if (ext && /^(pdf)$/.test(ext)) {
            this.setData({
              'key': 'hermes_packingsheetPDF',
              'packingId':$("#id").val()
            });
            $('#UploadCartonForm .logmessage').text('Uploading ' + file);
          } else {
            // extension is not allowed
            $('#UploadCartonForm .logmessage').text('Error: only file in pdf format is allowed');
            // cancel upload
            return false;
          }
        },
        onComplete : function(file,response) {
          $('#UploadCartonForm .logmessage').text( response);
        }
      });
 		uploadCarton.setData({'key': 'hermes_packingsheetPDF'});



//增加Carton PDF upload end

//增加Carton Weight PDF upload start

 var uploadCartonWeight = new AjaxUpload('#UploadCartonWeightButton', {
        action: "${resource(dir:'')}/${controllerName}/uploadPackingCartaonWeightPDF/",
        data : {
          'key1' : "This data won't",
          'key2' : "be send because",
          'key3' : "we will overwrite it"
        },
        //autoSubmit: true,
        onSubmit : function(file, ext) {
          //if (ext && new RegExp('^(' + allowed.join('|') + ')$').test(ext)){
          //if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)){
          if (ext && /^(pdf)$/.test(ext)) {
            this.setData({
              'key': 'hermes_packingsheetWeightPDF',
              'packingId':$("#id").val()
            });
            $('#UploadCartonWeightForm .logmessage').text('Uploading ' + file);
          } else {
            // extension is not allowed
            $('#UploadCartonWeightForm .logmessage').text('Error: only file in pdf format is allowed');
            // cancel upload
            return false;
          }
        },
        onComplete : function(file,response) {
          $('#UploadCartonWeightForm .logmessage').text( response);
        }
      });
 		uploadCarton.setData({'key': 'hermes_packingsheetWeightPDF'});




//增加Carton Weight PDF upload end


      
    });
  
  		var globalI;
  		function afterClose()
  		{
  			var grid = $("#"+globalI);
  			grid.trigger("reloadGrid");
			showGridTitle(globalI);
  		}
  		function closeDialog()
  		{
  	  		//alert($('#externalSite'));
  			$('#externalSite').dialog('close');
  			window.setTimeout(afterClose,20);
  			
  		}
		var grids=[];
		var readonly="${instance?.vaild}";
		function resize_the_grid(i){
			for(var i in grids){
				var g = $("#"+grids[i]);
				g.fluidGrid({base:'#grid_wrapper', offset:0});
			}
		}		
		function loadCartons(){
			var cartons = ${cartons};
			for(carton in cartons){
				Carton(cartons[carton]);
			}
		}

	 

		
		function addCarton() {
			var carton;
			var url = "${resource(dir:'')}/${controllerName}/updateCarton";
			var packingid = $("#id").val();
			if(packingid){
				$.post(url,{id:packingid},function(data){
						carton=data;
						if(carton){
							Carton(carton);
						}else{
							alert("error");
						}
					}, "text");
			}else{
				alert("packing id is empty");
			}
		}

		function onUpdateWeight() {
			//$('#dialogCartonId').val(i);
			//$('#dialog').dialog('open');\
			//alert("1323");
				var horizontalPadding = 30;
						var verticalPadding = 30;
						 
						var url = "${resource(dir:'')}/${controllerName}/showCartonWeightList/${instance?instance.id:''}";
						$('<iframe id="externalSite" class="externalSite" src="'+url+'"/>').dialog({
				            title: "Carton Weight List",
				            autoOpen: true,
				            width: 630,
				            height: 500,
				            modal: true,
				            resizable: true,
							autoResize: false,
				            overlay: {
				                opacity: 0.5,
				                background: "black"
				            },
							close: function() {
								grid.trigger("reloadGrid");
								//showGridTitle(i);
							}
				        }).width(600 - horizontalPadding).height(500-verticalPadding);
						//location.reload();	      
		};

		
		function Carton(i){
			
			var desc1="",desc2="";
			var table = $('div#cartons').append(
				'<div id=grid_wrapper_'+i+'>' +
				'<table id="'+i+'"></table><div id="pager_'+i+'"></div>'+
				'</div>&nbsp;</br>');
			var carton;
			var options = {
				dataType:"json",
				parse: function(data){
					var acd = new Array();
			        for(var i=0;i<data.length;i++){
						acd[acd.length] = { data:data[i], value:data[i].product, result:data[i].product };
			        }
			        return acd;
				},
				formatItem:function(row,i,max,value,term){
					return value;
				},
				formatMatch: function(row, i, max) {
					return row.product + " " + row.enname;
				},
				formatResult: function(row) {
					return row.product;
				}
			};
			
			var grid = $("#"+i);
			var lastsel;
			//grid.tableDnD({scrollAmount:0}); 
			grid.jqGrid({
					url:"${resource(dir:'')}/${controllerName}/listCartonItemsJson/"+i,
					editurl:"${resource(dir:'')}/${controllerName}/updateCartonItem/",
					datatype: "json",mtype: 'POST',
					colNames:[
						"cartonId",
						"cartonItemId",
						"invoiceItemId",
						"${message(code: 'product.dept.label', default: 'Code')}",
						"Sequence",
						"InvoiceCode",
						"${message(code: 'common.action.label', default: 'Dept')}",
						"${message(code: 'product.code.label', default: 'Desc ZH')}",
						"${message(code: 'category.code.label', default: 'Desc EN')}",
						"${message(code: 'carton.quantity.label', default: 'Quantity')}"],
					colModel:[
						{name:'cartonId',hidden:true,editable:true},
						{name:'cartonItemId',hidden:true,editable:true},
						{name:'invoiceItemId',hidden:true,editable:true},
						{name:'cartonProduct',hidden:false,editable:true,editoptions: {
					            size:20,
					            maxlength:20,
					            dataInit:function (elem) {
				$(elem).autocomplete("${resource(dir:'')}/${controllerName}/listCartonInvoiceItemsJson?cartonId="+i,options).result(
							 		function(e, item){
										/*
										var line = elem.id.substring(0,elem.id.indexOf("_")); 
							 			$("#"+line+"_cartonProduct").val(item.product);
										$('#'+line+'_cartonDept').val(item.dept);
							 			$('#'+line+'_cartonDescZh').val(item.zhname);
										$('#'+line+'_cartonDescEn').val(item.enname);
							 			$('#'+line+'_cartonQuantity').val(item.quantity);
										$('#'+line+'cartonId').val(item.cartonId);
										$('#'+line+'invoiceItemId').val(item.invoiceItemId);
										*/
							 			$("#cartonProduct").val(item.product);
										$('#cartonDept').val(item.dept);
							 			$('#cartonDescZh').val(item.zhname);
										$('#cartonDescEn').val(item.enname);
							 			$('#cartonQuantity').val(item.quantity);
										$('#cartonId').val(item.cartonId);
										$('#invoiceItemId').val(item.invoiceItemId);
										$('#invoiceCode').val(item.invoiceCode);
									});     
					          }}},
						{name:'sequence',hidden:false,editable:true},
						{name:'invoiceCode',hidden:false,editable:true},
						{name:'cartonDept',hidden:false,editable:true},
						{name:'cartonDescZh',hidden:false,editable:true},
						{name:'cartonDescEn',hidden:false,editable:true},
						{name:'cartonQuantity',hidden:false,editable:true}],
					pager:'#pager_'+i,
					rowNum:20, //分页数
					caption:"",
					autowidth: true,
					height:'auto',//reloadAfterSubmit:true,
					toolbar: [true,"top"]
					/*
					onSelectRow: function(id) {
					  if (id) {
					    grid.jqGrid('restoreRow', lastsel);
					    grid.jqGrid('editRow', id, true);
					    lastsel = id;
					  }
					}*/
			});
			//grid.jqGrid('navGrid', '#pager_'+i, {view:false,del:false,edit:true,add:true,search:false});
			if(readonly==='A'){
				
			}else{
				grid.jqGrid('navGrid', '#pager_'+i, {view:false,del:false,edit:true,add:false,search:false},	
				{
					beforeShowForm: function(frm) { 
			            $('#cartonDescZh').attr('readonly','readonly'); 
			            $('#cartonDescEn').attr('readonly','readonly'); 
			            $('#cartonDept').attr('readonly','readonly'); 
			            $('#invoiceCode').attr('readonly','readonly'); 
			        }
				}, // edit options 
				{
					beforeShowForm: function(frm) { 
			            $('#cartonDescZh').attr('readonly','readonly'); 
			            $('#cartonDescEn').attr('readonly','readonly'); 
			            $('#cartonDept').attr('readonly','readonly'); 
			            $('#invoiceCode').attr('readonly','readonly'); 
			        }
				}, // add options 
				{},  // del options 
				{multipleSearch:false}, // search options 
				{closeOnEscape:true});
				//grid.jqGrid('gridDnD',{connectWith:'#'+i});

				//grid.delGridRow("rowid",{delData:{myname:"myvalue"}});
				grid.jqGrid('navButtonAdd', '#pager_'+i, {
					caption: "UpdateCarton",
					title: "Update Carton",
					onClickButton : function () {
						//$('#dialogCartonId').val(i);
						//$('#dialog').dialog('open');
						var horizontalPadding = 30;
						var verticalPadding = 30;
						globalI=i;
						var url = "${resource(dir:'')}/${controllerName}/showCartonDet/"+i;
						$('<iframe id="externalSite" class="externalSite" src="'+url+'"/>').dialog({
				            title: "Carton Det",
				            autoOpen: true,
				            width: 600,
				            height: 500,
				            modal: true,
				            resizable: true,
							autoResize: false,
				            overlay: {
				                opacity: 0.5,
				                background: "black"
				            },
							close: function() {
								grid.trigger("reloadGrid");
								showGridTitle(i);
							}
				        }).width(600 - horizontalPadding).height(500-verticalPadding);
						//location.reload();	        
					}
				});
				grid.jqGrid('navButtonAdd', '#pager_'+i, {
					caption: "DelItem",
					onClickButton : function (id) {
						var gsr = grid.jqGrid('getGridParam','selrow');
			            if(gsr){
			                var data = grid.jqGrid('getRowData',gsr);
			                var cartonItemId = data.cartonItemId;					
							var url = "${resource(dir:'')}/${controllerName}/updateCartonItem";
							$.post(url,{cartonItemId:cartonItemId,oper:'del'},function(data){
									if(data==="success"){
										//TODO: delete success
									}
									grid.trigger("reloadGrid"); 
							}, "text");
						}else{
							alert("please select item to delete!")
						}
					}
				});
				grid.jqGrid('navButtonAdd', '#pager_'+i, {
					caption: "DelCarton",
					onClickButton : function (id,rowId) {
						var ret = grid.getRowData(id);
						var url = "${resource(dir:'')}/${controllerName}/deleteCarton";
						$.post(url,{id:i},function(data){
								if(data==="success"){
									$("#grid_wrapper_"+i).remove();
								}
						}, "text");
					}
				});
				grid.jqGrid('navButtonAdd', '#pager_'+i, { //复制Carton
					caption: "CloneCarton",
					onClickButton : function (id,rowId) {
						var ret = grid.getRowData(id);
						var url = "${resource(dir:'')}/${controllerName}/cloneCarton";
						$.post(url,{id:i},function(data){
							    var carton=data;//返回clone过的carton ID
								if(carton){
									Carton(carton);//加入到cartons列表
								}

						}, "text");
					}
				});
			}
			showGridTitle(i);
			grids.push(i);		
		}		
		function showGridTitle(i){
			$.get("${resource(dir:'')}/${controllerName}/showCarton/"+i, null, function(data){   
			        var json = data;    
			       	//alert(json.desc1);
					$("#t_"+i).html("<strong>&nbsp;"+json.desc1+" "+json.desc2+"</strong>"); 
			},"json");	
		}
		$(document).ready(function(){ 
			$("input#addCarton").click(function(){
				addCarton(this,1);
			});
			$("input#updateweight").click(function(){
				onUpdateWeight();
			});
			$("input#exportPackingList").click(function(){
				//alert(document.getElementById('parentCompany').value);
				 
				 window.location.href="${resource(dir:'')}/interface/exportPackingInvoice?id=${instance?instance.id:''}&parentCompany="+document.getElementById('parentCompany').value;
				// alert(document.getElementById('parentCompany').value);
			});
			
			
			//var data = "Core Selectors Attributes Traversing Manipulation CSS Events Effects Ajax Utilities".split(" ");
			var bpCodeOption = {
				dataType:"json",
				parse: function(data){
					var acd = new Array();
			        for(var i=0;i<data.length;i++){
						acd[acd.length] = { data:data[i], value:data[i].code, result:data[i].code };
						//alert(data[i].code);
			        }
			        return acd;
				},
				formatItem:function(row,i,max,value,term){
					return value;
				},
				formatMatch: function(row, i, max) {
					return row.code;
				},
				formatResult: function(row) {
					return row.code;
				}
			};			
			$("#bpCode").autocomplete("${resource(dir:'')}/${controllerName}/listBusinessCodeJson",bpCodeOption);		
			
			$("#dialog").dialog({
				bgiframe: true,
				autoOpen: false,
				modal: true,
				buttons: {
					'Submit': function() {
						var query = $('#cartonForm').formSerialize(); 
						var url = "${resource(dir:'')}/${controllerName}/updateCarton?"+query;
						$.post(url,{},function(data){
							//alert(data);	
						}, "text");
						$(this).dialog('close');
					},
					Cancel: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
				}
			});
		
			loadCartons();
			$("#packingForm").validate({
				rules: {
			    	bpCode: {required: true,number: true}
			  	},
				messages: {
					bpCode: " Required"
				}
			});
			$("#vaildDate").datepicker({ dateFormat: 'yy-mm-dd' });
		});
	
		$(window).resize(resize_the_grid);
  </script>
</head>
<body>

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<g:hasErrors bean="${securityInstance}">
  <div class="errors">
    <g:renderErrors bean="${securityInstance}" as="list"/>
  </div>
</g:hasErrors>

<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
	<div class="fg-buttonset ui-helper-clearfix">
	<a href="${resource(dir:'')}/${controllerName}/showPacking/${instance?instance.id:''}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
	 <span class="ui-icon ui-icon-refresh"></span>r
	</a>
	<a href="${resource(dir:'')}/${controllerName}/listPacking" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
	 <span class="ui-icon ui-icon-back"></span>b
	</a>
	</div>
</div>
<div class="main">
	<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
	    <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
	      <span class="ui-jqgrid-title">Packing</span>
	    </div>
		<form id="packingForm" method="POST" action="${resource(dir:'')}/${controllerName}/updatePacking">
		<g:hiddenField name="id" value="${instance?instance.id:''}"/>
	    <div class="ui-dialog-content ui-widget-content">
	      <table>
	        <tbody>
	        <tr class="prop">
	          <td class="name"><g:message code="business.code.label" default="BP Code"/> *</td>
	          <td class="value">
				<g:if test="${instance}">
					${instance?instance.business.code:''}
				</g:if>
				<g:else>
					<!--<g:textField name="bpCode" maxlength="50" value=""/> -->
					<select name="bpCode" id="bpCode" >
						<option value="" ></option>
						<g:each in="${bpList}" status="i" var="Busi">
						    <option value="${Busi.code}" >${Busi.code}</option>
						</g:each>
					</select>
				</g:else>
			  </td>
	          <td class="name"><g:message code="business.reInvoice.label" default="Re Invoice"/></td>
	          <td class="value">${reInvoiceNumbers?reInvoiceNumbers:''}</td>
	        </tr>
	        <tr class="prop">
	          <td class="name"><g:message code="packing.hardcopy.label" default="No d'expedition"/></td>
	          <td class="value"><g:textField name="hardCopy" maxlength="50" value="${fieldValue(bean: instance, field:'hardCopy')}"/></td>
	          <td class="name"><g:message code="packing.date.label" default="Packing Date"/></td>
	          <td class="value"><g:formatDate date="${fieldValue(bean: instance, field: "date")}" format="yyyy-MM-dd"/></td>
	        </tr>
	        <tr class="prop">
			 <td class="name"><g:message code="invoice.parentCompany.label" default="Parent Company"/></td>
			 
			 <g:if test="${instance}">
					
				
			 <td class="value"><g:select id="parentCompany" name="parentCompany"
			   from="['A. Hermes (China) CO., LTD','B. Hermes (China) Trading Co., Ltd']"
			   value="${instance?.business.customer.parentCompany}"/>(For exporting Packing List Heading only)
			   
			   </td>
			   </g:if>
			   </tr>
	        
	        
	        <tr class="prop">
	          <td class="name">Export All</td>
	          <td class="value">${instance?.export}
	          <td class="name">Export Date</td>
	          <td class="value">${formatDate(format:'yyyy-MM-dd',date:instance?.exportDate)}
			</td>
	        </tr>
	        <tr class="prop">
	          <td class="name">Vaild</td>
	          <td class="value">${instance?.vaild}
	          <td class="name">Valid Date(Default: today)</td>
	          <td class="value"><input id="vaildDate" name="vaildDate" type="text" value="${formatDate(format:'yyyy-MM-dd',date:instance?.vaildDate)}"></td>
	        
	        </tr>
	        <tr class="prop">
	        <td class="name">Upload PackingSheet(optional)</td>
	        <td class="value">	
	         <table id="UploadForm">
	         <tr><td>
		    	<g:if test="${instance}">
					<g:if test="${instance?.vaild!='X'}"> 
   						<button id="UploadButton"  class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button><p class="logmessage"></p>
				 	</g:if>
					<g:else>
						<button id="UploadButton" disabled="true" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload Not Available"/></button><p class="logmessage"></p>
					</g:else>
				</g:if>
				<g:else></g:else>
				</td></tr>
				  </table>
		    </td>
		    
		    
		     <td class="name">Upload Packing Carton(PDF)</td>
		    <td class="value">	
	         <table id="UploadCartonForm">
	         <tr><td>
		    	<g:if test="${instance}">
					<g:if test="${instance?.vaild!='X'}"> 
   						<button id="UploadCartonButton"  class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button><p class="logmessage"></p>
				 	</g:if>
					<g:else>
						<button id="UploadCartonButton" disabled="true" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload Not Available"/></button><p class="logmessage"></p>
					</g:else>
				</g:if>
				<g:else></g:else>
				</td></tr>
				  </table>
		    </td>
		    		     <td class="name">Upload Packing Carton Wights(PDF)</td>
		    <td class="value">	
	         <table id="UploadCartonWeightForm">
	         <tr><td>
		    	<g:if test="${instance}">
					<g:if test="${instance?.vaild!='X'}"> 
   						<button id="UploadCartonWeightButton"  class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button><p class="logmessage"></p>
				 	</g:if>
					<g:else>
						<button id="UploadCartonWeightButton" disabled="true" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload Not Available"/></button><p class="logmessage"></p>
					</g:else>
				</g:if>
				<g:else></g:else>
				</td></tr>
				  </table>
		    </td>
	        </tr>
	        
	        
<%--	         <tr class="prop">--%>
<%--	        <td class="name">Upload Packing Carton(PDF)</td>--%>
<%--	        <td class="value">	--%>
<%--	         <table id="UploadCartonForm">--%>
<%--	         <tr><td>--%>
<%--		    	<g:if test="${instance}">--%>
<%--					<g:if test="${instance?.vaild!='X'}"> --%>
<%--   						<button id="UploadCartonButton"  class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button><p class="logmessage"></p>--%>
<%--				 	</g:if>--%>
<%--					<g:else>--%>
<%--						<button id="UploadCartonButton" disabled="true" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload Not Available"/></button><p class="logmessage"></p>--%>
<%--					</g:else>--%>
<%--				</g:if>--%>
<%--				<g:else></g:else>--%>
<%--				</td></tr>--%>
<%--				  </table>--%>
<%--		    </td>--%>
<%--	        </tr>--%>
	        
	        
	        
	        </tbody>
	      </table>
	    </div>
	    <div class="buttons">
			<span class="button"><input type="submit" class="save" action="updatePacking" value="${message(code: 'default.button.update.label', default: 'Save Packing')}"/></span>
			<g:if test="${instance}">
				<span class="button"><input type="button" id="addCarton" class="save" action="add" value="${message(code: 'default.button.add.label', default: 'Add Carton')}"/></span>
				<span class="button"><input type="button" id="updateweight" class="save" action="add" value="${message(code: 'default.button.updateCartonWeight.label', default: 'Update Carton Weight')}"/></span>
				<span class="button"><input id="exportPackingList" type="button" class="save" value="${message(code: 'default.button.Export Packing List.label', default: 'Export Packing List')}"/></span>
<%--				<span><a href="${resource(dir:'')}/interface/exportPackingInvoice?id=${instance?instance.id:''}&parentCompany="+document.getElementById('parentCompany').value" class="save">Export</a></span>--%>
<%--			   --%>
			</g:if>
	    </div>
		</form>
	</div>
	<div id="cartons"></div>
</div>
</body>
</html>
