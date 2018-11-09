<html>
<head>
  <meta name="layout" content="main_base"/>
  <parameter name="formtext" value="${message(code: 'common.upload.label', default: 'Upload')}"/>
  <script type="text/javascript">
    $(document).ready(function() {
      var upload = new AjaxUpload('#UploadButton', {
        action: "${resource(dir:'')}/${controllerName}/uploadInvoice/",
        data : {
          'key1' : "This data won't",
          'key2' : "be send because",
          'key3' : "we will overwrite it"
        },
        //autoSubmit: true,
        onSubmit : function(file, ext) {
          //if (ext && new RegExp('^(' + allowed.join('|') + ')$').test(ext)){
          //if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)){
          if (ext && /^(csv|xls)$/.test(ext)) {
            this.setData({
              'key': 'hermes_bpinvoice'
            });
            $('#UploadForm .logmessage').text('Uploading ' + file);
          } else {
            // extension is not allowed
            $('#UploadForm .logmessage').text('Error: only cvs or xls are allowed');
            // cancel upload
            return false;
          }
        },
        onComplete : function(file) {
          $('#UploadForm .logmessage').text('Uploaded ' + file);
        }
      });
      upload.setData({'key': 'hermes_bpinvoice_1'});
    });
  </script>
</head>
<body>
<table id="UploadForm">
  <tbody>
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Invoice File"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'code', 'errors')}">
      <button id="UploadButton" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button>
    </td>
  </tr>
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Invoice Year"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'code', 'errors')}">
   		<select name="inYear" id="inYear" >
			<option value="09" >09</option>
			<option value="10" >10</option>
			<option value="11" >11</option>
			<option value="12" >12</option>
			<option value="13" >13</option>
			<option value="14" >14</option>
			<option value="15" >15</option>
		</select>      
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
</body>
</html>
