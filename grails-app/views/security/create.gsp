<head>
  <meta name="layout" content="main_crud"/>
  <g:set var="entityName" value="${message(code: 'security.label', default: 'Security')}"/>
  <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>
<body>
<table>
  <tbody>
  <tr class="prop">
    <td class="name">
      <label for="code"><g:message code="common.code.label" default="Code"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'code', 'errors')}">
      <g:textField name="code" maxlength="20" value="${securityInstance?.code}"/>
    </td>
  </tr>

  <tr class="prop">
    <td class="name">
      <label for="name"><g:message code="common.name.label" default="Name"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'name', 'errors')}">
      <g:textField name="name" maxlength="50" value="${securityInstance?.name}"/>
    </td>
  </tr>

  <tr class="prop">
    <td class="name">
      <label for="description"><g:message code="common.description.label" default="Description"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'description', 'errors')}">
      <g:textArea name="description" cols="40" rows="5" value="${securityInstance?.description}"/>
    </td>
  </tr>

  <tr class="prop">
    <td class="name">
      <label for="lastUpdated"><g:message code="common.lastUpdated.label" default="Last Updated"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'lastUpdated', 'errors')}">
      <g:datePicker name="lastUpdated" precision="day" value="${securityInstance?.lastUpdated}"/>
    </td>
  </tr>

  <tr class="prop">
    <td class="name">
      <label for="dateCreated"><g:message code="common.dateCreated.label" default="Date Created"/></label>
    </td>
    <td class="value ${hasErrors(bean: securityInstance, field: 'dateCreated', 'errors')}">
      <g:datePicker name="dateCreated" precision="day" value="${securityInstance?.dateCreated}"/>
    </td>
  </tr>
  </tbody>
</table>
</body>

