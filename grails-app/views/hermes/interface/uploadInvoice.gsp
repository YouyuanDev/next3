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
              'key': 'hermes_bpinvoice',
              'inYear': document.getElementById("inYear").options[document.getElementById("inYear").options.selectedIndex].value,
              'overWrite': document.getElementById("overWrite").options[document.getElementById("overWrite").options.selectedIndex].value
              
            });
            $('#UploadForm .logmessage').text('Uploading ' + file);
          } else {
            // extension is not allowed
            $('#UploadForm .logmessage').text('Error: only cvs or xls are allowed');
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
  <tr>
    <td class="name">
      <label><g:message code="common.logmessage.label" default="Invoice Year"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'code', 'errors')}">
      <g:select from="${['09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59','60','61','62','63','64','65','66','67','68','69','70','71','72','73','74','75','76','77','78','79','80','81','82','83','84','85','86','87','88','89','90','91','92','93','94','95','96','97','98','99']}" name="inYear" value="${inYear}"></g:select> 
      <input type="hidden" name="txtYear" id="txtYear" value="${inYear}"/> 
    </td>
  </tr>   
  <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.upload.label" default="Invoice File"/></label>
    </td>
    <td class="value">
      <button id="UploadButton" class="fg-button ui-state-default ui-priority-primary ui-corner-all"><g:message code="bpinvoice.upload.label" default="Upload"/></button>
    </td>
  </tr>
    <tr class="prop">
    <td class="name">
      <label><g:message code="bpinvoice.overwrite.label" default="Overwrite if BP already exists?"/></label>
    </td>
    <td class="value">
     <g:select from="${['No','Yes']}" name="overWrite" value="${overWrite}"></g:select> 
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
