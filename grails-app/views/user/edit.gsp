<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
  <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>
<body>
<!--
        <div class="nav">
            <span class="menuButton"><a class="home" href="${resource(dir: '')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>
        </div>
	-->
<div class="body">
<!--<h1><g:message code="default.edit.label" args="[entityName]"/></h1>-->
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${userInstance}">
    <div class="errors">
      <g:renderErrors bean="${userInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form method="post">
    <g:hiddenField name="id" value="${userInstance?.id}"/>
    <g:hiddenField name="version" value="${userInstance?.version}"/>
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="email"><g:message code="user.email.label" default="Email"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'email', 'errors')}">
            <g:textField name="email" value="${userInstance?.email}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="password"><g:message code="user.password.label" default="Password"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'password', 'errors')}">
            <g:passwordField name="password" value="${userInstance?.password}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="party"><g:message code="user.party.label" default="Party"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'party', 'errors')}">
            <g:select name="party.id" from="${Party.list()}" optionKey="id" value="${userInstance?.party?.id}" noSelection="['null': '']"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="roles"><g:message code="user.roles.label" default="Roles"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'roles', 'errors')}">
            <g:select name="roles" from="${Role.list()}" multiple="yes" optionKey="id" size="5" value="${userInstance?.roles}"/>
          </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
