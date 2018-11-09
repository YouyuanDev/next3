<html>
<head>
  <meta name="layout" content="main_crud"/>
  <g:set var="entityName" value="${message(code: 'security.label', default: 'Security')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>
<body>
<table>
  <tbody>
  <tr class="prop">
    <td class="name"><g:message code="security.id.label" default="Id"/></td>
    <td class="value">${fieldValue(bean: securityInstance, field: "id")}</td>
  </tr>
  <tr class="prop">
    <td class="name"><g:message code="security.code.label" default="Code"/></td>
    <td class="value">${fieldValue(bean: securityInstance, field: "code")}</td>
  </tr>
  <tr class="prop">
    <td class="name"><g:message code="security.name.label" default="Name"/></td>
    <td class="value">${fieldValue(bean: securityInstance, field: "name")}</td>
  </tr>
  <tr class="prop">
    <td class="name"><g:message code="security.description.label" default="Description"/></td>
    <td class="value">${fieldValue(bean: securityInstance, field: "description")}</td>
  </tr>
  <tr class="prop">
    <td class="name"><g:message code="security.permissions.label" default="Permissions"/></td>
    <td style="text-align: left;" class="value">
    </td>
  </tr>
  <tr class="prop">
    <td class="name"><g:message code="security.lastUpdated.label" default="Last Updated"/></td>
    <td class="value"><g:formatDate date="${securityInstance?.lastUpdated}"/></td>
  </tr>
  <tr class="prop">
    <td class="name"><g:message code="security.dateCreated.label" default="Date Created"/></td>
    <td class="value"><g:formatDate date="${securityInstance?.dateCreated}"/></td>
  </tr>
  </tbody>
</table>
</body>
</html>
