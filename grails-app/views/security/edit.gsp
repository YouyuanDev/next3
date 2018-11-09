<html>
<head>
<meta name="layout" content="main_crud" />
<title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>
<table>
  <tbody>
  <tr class="prop">
    <td valign="top" class="name">
      <label for="code"><g:message code="security.code.label" default="Code"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: securityInstance, field: 'code', 'errors')}">
      <g:textField name="code" maxlength="20" value="${securityInstance?.code}"/>
    </td>
  </tr>

  <tr class="prop">
    <td valign="top" class="name">
      <label for="name"><g:message code="security.name.label" default="Name"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: securityInstance, field: 'name', 'errors')}">
      <g:textField name="name" maxlength="50" value="${securityInstance?.name}"/>
    </td>
  </tr>

  <tr class="prop">
    <td valign="top" class="name">
      <label for="description"><g:message code="security.description.label" default="Description"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: securityInstance, field: 'description', 'errors')}">
      <g:textArea name="description" cols="40" rows="5" value="${securityInstance?.description}"/>
    </td>
  </tr>

  <tr class="prop">
    <td valign="top" class="name">
      <label for="permissions"><g:message code="security.permissions.label" default="Permissions"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: securityInstance, field: 'permissions', 'errors')}">
      <g:select name="permissions" from="${Permission.list()}" multiple="yes" optionKey="id" size="5" value="${securityInstance?.permissions}"/>
    </td>
  </tr>

  <tr class="prop">
    <td valign="top" class="name">
      <label for="lastUpdated"><g:message code="security.lastUpdated.label" default="Last Updated"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: securityInstance, field: 'lastUpdated', 'errors')}">
      <g:datePicker name="lastUpdated" precision="day" value="${securityInstance?.lastUpdated}"/>
    </td>
  </tr>

  <tr class="prop">
    <td valign="top" class="name">
      <label for="dateCreated"><g:message code="security.dateCreated.label" default="Date Created"/></label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: securityInstance, field: 'dateCreated', 'errors')}">
      <g:datePicker name="dateCreated" precision="day" value="${securityInstance?.dateCreated}"/>
    </td>
  </tr>

  </tbody>
</table>
</body>
</html>
