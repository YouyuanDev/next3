<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}"/>
  <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>
<body>
<div class="nav">
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${menuInstance}">
    <div class="errors">
      <g:renderErrors bean="${menuInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form action="save" method="post">
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="code"><g:message code="menu.code.label" default="Code"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'code', 'errors')}">
            <g:textField name="code" maxlength="20" value="${menuInstance?.code}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="name"><g:message code="menu.name.label" default="Name"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'name', 'errors')}">
            <g:textField name="name" maxlength="50" value="${menuInstance?.name}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="type"><g:message code="menu.type.label" default="Type"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'type', 'errors')}">
            <g:textField name="type" maxlength="20" value="${menuInstance?.type}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="grails_controller"><g:message code="menu.grails_controller.label" default="Grailscontroller"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'grails_controller', 'errors')}">
            <g:textField name="grails_controller" maxlength="50" value="${menuInstance?.grails_controller}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="grails_action"><g:message code="menu.grails_action.label" default="Grailsaction"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'grails_action', 'errors')}">
            <g:textField name="grails_action" maxlength="50" value="${menuInstance?.grails_action}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="grails_param"><g:message code="menu.grails_param.label" default="Grailsparam"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'grails_param', 'errors')}">
            <g:textField name="grails_param" maxlength="200" value="${menuInstance?.grails_param}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="description"><g:message code="menu.description.label" default="Description"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'description', 'errors')}">
            <g:textArea name="description" cols="40" rows="5" value="${menuInstance?.description}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="seq"><g:message code="menu.seq.label" default="Seq"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'seq', 'errors')}">
            <g:textField name="seq" value="${fieldValue(bean: menuInstance, field: 'seq')}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="cls"><g:message code="menu.cls.label" default="Cls"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'cls', 'errors')}">
            <g:textField name="cls" maxlength="20" value="${menuInstance?.cls}"/>
          </td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="parent"><g:message code="menu.parent.label" default="Parent"/></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: menuInstance, field: 'parent', 'errors')}">
            <g:select name="parent.id" from="${Menu.list()}" optionKey="id" value="${menuInstance?.parent?.name}" noSelection="['null': '']"/>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
