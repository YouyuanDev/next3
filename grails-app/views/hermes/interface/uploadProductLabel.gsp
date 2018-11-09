<html>
<head>
  <meta name="layout" content="main_base"/>
  <parameter name="formtext" value="${message(code: 'common.upload.label', default: 'Upload')}"/>
  <script type="text/javascript">
    $(document).ready(function() {
      var upload = new AjaxUpload('#UploadButton', {
        action: "${resource(dir:'')}/${controllerName}/uploadProductLabel/",
        data : {
          'key1' : "This data won't",
          'key2' : "be send because",
          'key3' : "we will overwrite it"
        },
        //autoSubmit: true,
        onSubmit : function(file, ext) {
          //if (ext && new RegExp('^(' + allowed.join('|') + ')$').test(ext)){
          //if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)){
          if (ext && /^(xls)$/.test(ext)) {
            this.setData({
              'key': 'hermes_bpinvoice'
            });
            $('#UploadForm .logmessage').text('Uploading ' + file);
          } else {
            // extension is not allowed
            $('#UploadForm .logmessage').text('Error: only xls are allowed');
            // cancel upload
            return false;
          }
        },
        onComplete : function(file,response) {
          $('#UploadForm .logmessage').text('Uploaded ' + response);
        }
      });
      upload.setData({'key': 'hermes_bpinvoice_1'});
    });
    
    function export1(){

    	
    	 var url="${resource(dir:'')}/interface/exportProductLabel"
    		// alert("Please wait...")
    	 //return
    	 
    	  
    	 
    		window.location.href=url

    }
     
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
	
<table id="UploadForm">
  <tbody>
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Standard Excel Format(case sensitive)"/></label>
    </td>
     
  </tr>
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Column Name: Please see the export format"/></label>
    </td>
     
  </tr>
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Sheet Name: "/></label><font color="red">upload</font>
    </td>
     
  </tr>
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Product Label File(.xls) "/></label>
    </td>
    <td class="value">
      <button id="UploadButton" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button>
    
    </td>
  </tr>
  <tr>
    <td class="name">
      <label><g:message code="common.logmessage.label" default="Message"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'code', 'errors')}">
      <p class="logmessage"></p>
    </td>
  </tr>
  </tbody>
</table>
<div class="buttons">
					
<span class="button"><input type="button" class="save" onclick="export1()" value="Export Product Label(standard format)"/></span>
</div>
</body>
</html>
