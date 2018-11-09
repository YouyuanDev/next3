<html>
<head>
<meta name="layout" content="main_blank"/>
<title>Shipment</title>
<script type="text/javascript">
	var grid;
    var caption = ""; var rowNum = -1;
    var listurl = "${resource(dir:'')}/${controllerName}/listShipmentItemsJson/${fieldValue(bean: instance, field: "id")}";
    var editurl = "${resource(dir:'')}/${controllerName}/updateInvoiceItem";
    var colNames = ["Action","Id","Invoice","Short","Long","Dept","Magitudem","Type","Duty","Zh","En","Quantity","Baolong","Cost","UP(EUR)","AMT(RMB)","TO(RMB)","BLCost","Adj"];
    var colModel = [
      {name:'action',hidden:true,editable:true},
      {name:'invoiceitem.id',hidden:true,editable:true},
      {name:'invoice',hidden:false,editable:false},
      {name:'short',hidden:false,editable:false},
      {name:'long',hidden:false,editable:false},
      {name:'dept',hidden:false,editable:false},
      {name:'invoiceitem.magitudem',hidden:false,editable:true},
      {name:'type',hidden:false,editable:false},
      {name:'duty',hidden:false,editable:false},
      {name:'zh',hidden:false,editable:false},
      {name:'en',hidden:false,editable:false},
      {name:'quantity',hidden:false,editable:false,align:"right"},
      {name:'baolong',hidden:false,editable:false,align:"right"},
      {name:'cost',hidden:false,editable:false,align:"right"},
      {name:'up',hidden:false,editable:false,align:"right"},
      {name:'amt',hidden:false,editable:false,align:"right"},
      {name:'up',hidden:false,editable:false,align:"right"},
      {name:'cost',hidden:false,editable:false,align:"right"},
      //{name:'diff',hidden:false,editable:false,align:"right"},
      {name:'invoiceitem.adj',hidden:false,editable:false,align:"right",editable:true}
    ];
	var navOptions = {view:false,del:false,edit:false,add:false,search:false};
    function resize_the_grid()
    {
      $('#grid').fluidGrid({base:'#grid_wrapper', offset:0});
    }
	$(window).resize(resize_the_grid);
    jQuery(document).ready(function(){
		var lastsel;
		grid = jQuery("#grid").jqGrid({
			caption:"",
			url:listurl,editurl:editurl,datatype: "json",mtype: 'GET',
			colNames:colNames,colModel :colModel,pager: '#gridnav',
			viewrecords: true,rowNum:1000,//rowList:[10,20,30],sortname: 'code',sortorder: 'code',
			autowidth: true,height:'auto',multiselect:false,rownumbers:true,rownumWidth:40, 
			onSelectRow: function(id) {
				//if(id && id!==lastsel){
				if (id) {
					jQuery('#grid').jqGrid('restoreRow', lastsel);
					jQuery('#grid').jqGrid('editRow', id, true);
					lastsel = id;
				}
			}
		});
		jQuery("#grid").jqGrid('navGrid', '#gridnav', {view:false,del:false,edit:false,add:false,search:false},{},{},{},{multipleSearch : true},{closeOnEscape:true});
		
		
		var t = new $.TextboxList('#form_tags_input',{unique: true, plugins: {autocomplete: {}}});
		$.ajax({url: '../shipmenNoHasBusinessJson?id=${instance?.id}', dataType: 'json', success: function(r){
			t.plugins['autocomplete'].setValues(r);
			t.getContainer().removeClass('textboxlist-loading');
		}});	
		t.addEvent('bitRemove', function(v){
			var value = v.getValue();
			if(value[0]){
				var id = value[0];
				var name = value[1];
				$.ajax({url: '../shipmentBusinessUpdate?act=del&shipmentid=${instance?.id}&businessid='+id,type:"POST",dataType:'html',success:function(r){
					grid.trigger("reloadGrid"); 
				}});	
			}
		});
        t.addEvent('bitAdd', function(v){
			var value = v.getValue();
			if(value[0]){
				var id = value[0];
				var name = value[1];
				$.ajax({url: '../shipmentBusinessUpdate?act=add&shipmentid=${instance?.id}&businessid='+id,type:"POST",dataType:'html',success:function(r){
					if(r!=='success'){
						alert(r);
					}
					grid.trigger("reloadGrid"); 
				}});	
			}
		});
		$.get("${resource(dir:'')}/${controllerName}/shipmenHasBusinessJson/${instance?.id}", null,function(data){   
			var json = data;    
			for(var i in json){
				t.add(json[i][1],json[i][0]);
			}
		},"json");	
		
		var upload = new AjaxUpload('#UploadButton', {
	        action: "${resource(dir:'')}/interface/uploadBaoLongShipment/",
	        data : {
	          'key1' : "This data won't",
	          'key2' : "be send because",
	          'key3' : "we will overwrite it"
	        },
	        //autoSubmit: true,
	        onSubmit : function(file, ext) {
	          if (ext && /^(csv|xls)$/.test(ext)) {
	            this.setData({});
	          } else {
	            $('#logmessage').text('Error: only xls are allowed');
	            return false;
	          }
	        },
	        onComplete : function(file) {
				location.reload();
	        }
		});
		$("#shipmentForm").validate({
			rules: {
			    code: {required: true},
				//reInvoices: {required: true},
			    blGS: {required: true,number: true},
			    blYZ: {required: true,number: true},
			    blDL: {required: true,number: true},
			    rate: {required: true,number: true},
			    bfdf: {required: true,number: true},
			    bal: {required: true,number: true},
			    totalAdjDiff: {required: true,number: true}
			},
			messages: {
			    code: " Required",
				//reInvoices: "Required",
			    blGS: " Required",
			    blYZ: " Required",
			    blDL: " Required",
			    rate: " Required",
			    bfdf: " Required",
			    bal: " Required",
			    totalAdjDiff: " Required"
			}
		});
    });

    function submitshipment(){
        if (confirm("Do you want to Submit this shipment?")){
        	document.shipmentForm.check.value="1";
        	document.shipmentForm.submit();
        }
    }
</script>
</head>
<body>
<div class="fg-toolbar ui-widget-header ui-helper-clearfix">
	<div class="fg-buttonset ui-helper-clearfix">
	<a href="${resource(dir:'')}/${controllerName}/showShipment/${instance?.id}" class="fg-button ui-state-default fg-button-icon-solo ui-corner-all" title="Refresh">
	 <span class="ui-icon ui-icon-refresh"></span>r
	</a>
	<a href="${resource(dir:'')}/${controllerName}/listShipment" class="fg-button ui-state-default fg-button-icon-solo  ui-corner-all" title="Refresh">
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
      <span class="ui-jqgrid-title">Shipment</span>
    </div>
	<form id="shipmentForm" name="shipmentForm"  method="POST" action="${resource(dir:'')}/${controllerName}/updateShipment">
	<g:hiddenField name="id" value="${instance?.id}"/>
    <div class="ui-dialog-content ui-widget-content">
		<table>
		  <tbody>
		  <tr class="prop">
		    <td class="name"><g:message code="shipment.code.label" default="Code"/> *</td>
		    <td class="value">
				<g:if test="${instance}">
					${instance.code}
				</g:if>
				<g:else>
			 
					<g:textField id="code" name="code" maxlength="50" value="${scode}"/>
				</g:else>
			</td>
		    <td class="name"><g:message code="shipment.reinvoice.label" default="BPs"/></td>
		    <td class="value">
			   
				<input type="text" name="test" value="" id="form_tags_input" />
			</td>
		  </tr>
		 
		   <tr class="prop">
			
		    <td class="name">运杂 *</td>
		    <td class="value">${instance?instance.blYZ:'0'}<br></td>
		    <td class="name">代理 *</td>
		    <td class="value">${instance?instance.blDL:'0'}<br></td>
		  </tr>
		  <tr class="prop">
		    <td class="name">百福东方 *</td>
		    <td class="value">${instance?instance.bfdf:'0'}<br></td>
		    <td class="name">关税 *</td>
		    <td class="value">${instance?instance.totalBLGS:'0'}<br></td>
		  </tr>
		  <tr class="prop">
		    <td class="name">汇率 *</td>
		    <td class="value"><g:textField id="rate" name="rate" maxlength="10" value="${instance?instance.rate:'1'}" class="required"/><br></td>
		    <td class="name"><g:message code="shipment.quantity.label" default="Quantity"/></td>
		    <td class="value">${instance?.totalQuantity}</td>
			
			</td>
		  </tr>
		  <tr class="prop">
		    <td class="name">BL Total Cost</td>
		    <td class="value">${instance?.totalBLCost}</td>
		    <td class="name">Hermes Total Cost</td>
		    <td class="value">${instance?.totalHCost}/${instance?.totalDifference}</td>
			
			</td>
		  </tr>
		  <tr class="prop">
		    <td class="name">
		      <label><g:message code="shipment.description.label" default="Description"/></label>
		    </td>
		    <td class="value"><g:textField id="description" name="description" maxlength="50" value="${instance?.description}"/><br></td>
		    <td class="name">
		      <label><g:message code="bpinvoice.upload.label" default="Baolong"/></label>
		      <input type="hidden" name="check" value="0"/>
		    </td>
		    <td class="value">	
		    	<g:if test="${instance}">
					<g:if test="${instance?.submit==0}">
					  <table id="UploadForm">
						<tr><button id="UploadButton" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button><p class="logmessage"></p></tr>
					  </table>
					</g:if>
					<g:else>Uploaded</g:else>
				</g:if>
				<g:else></g:else>
		    </td>
		  </tr>	
		   <tr class="prop">
		    <td class="name">
		      <label><g:message code="shipment.Status.label" default="Status"/></label>
		    </td>
		    <td class="value">
		    <g:if test="${instance}">
		    <g:if test="${instance?.submit==1}">
		    <g:message   code="shipment.Status.label"  default="Submitted"/>
		    </g:if>	
			<g:else>
		  	 <g:message   code="shipment.Status.label"  default="Not Submitted"/> 
			</g:else>
			</g:if>
		    
		    <br></td>
		    </tr>
		  </tbody>
		</table>
	</div>
	<div class="buttons">
	<g:if test="${instance}">
		<g:if test="${instance?.submit!=1}">
		  <span class="button"><input type="submit" class="save" value="${message(code: 'default.button.update.label', default: 'Save')}"/></span>
		  <span class="button"><input type="button" class="save" onclick="submitshipment();" value="Submit"/></span>
		</g:if>	
		
	</g:if>
	<g:else>
		  <span class="button"><input type="submit" class="save" value="${message(code: 'default.button.update.label', default: 'Save')}"/></span>
	</g:else>
	</div>
	
	</form>
  </div>
  <table id="grid"></table>
  <div id="gridnav"></div>
</div>

</body>
</html>
	