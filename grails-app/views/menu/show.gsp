<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>
<body>
<div class="nav">
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></span>
  <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></span>
</div>
<div class="body">
&nbsp;
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="dialog">
    <table>
      <tbody>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.code.label" default="Code"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "code")}</td>
        <td valign="top" class="name"><g:message code="menu.name.label" default="Name"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "name")}</td>
      </tr>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.type.label" default="Type"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "type")}</td>
        <td valign="top" class="name"><g:message code="menu.seq.label" default="Seq"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "seq")}</td>
      </tr>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.grails_controller.label" default="Grailscontroller"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "grails_controller")}</td>
        <td valign="top" class="name"><g:message code="menu.grails_action.label" default="Grailsaction"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "grails_action")}</td>
      </tr>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.grails_param.label" default="Grailsparam"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "grails_param")}</td>
        <td valign="top" class="name"><g:message code="menu.cls.label" default="Cls"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "cls")}</td>
      </tr>


      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.description.label" default="Description"/></td>
        <td valign="top" class="value">${fieldValue(bean: menuInstance, field: "description")}</td>
      </tr>


      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.parent.label" default="Parent"/></td>
        <td valign="top" class="value"><g:link controller="menu" action="show" id="${menuInstance?.parent?.id}">${menuInstance?.parent?.name}</g:link></td>
        <td valign="top" class="name"><g:message code="menu.childs.label" default="Childs"/></td>
        <td valign="top" style="text-align: left;" class="value">
          <ul>
            <g:each in="${menuInstance.childs}" var="c">
              <li><g:link controller="menu" action="show" id="${c.id}">${c?.name}</g:link></li>
            </g:each>
          </ul>
        </td>
      </tr>
      <tr class="prop">
        <td valign="top" class="name"><g:message code="menu.dateCreated.label" default="Date Created"/></td>
        <td valign="top" class="value"><g:formatDate date="${menuInstance?.dateCreated}"/></td>
        <td valign="top" class="name"><g:message code="menu.lastUpdated.label" default="Last Updated"/></td>
        <td valign="top" class="value"><g:formatDate date="${menuInstance?.lastUpdated}"/></td>
      </tr>
      </tbody>
    </table>
  </div>
  <div class="buttons">
    <g:form>
      <g:hiddenField name="id" value="${menuInstance?.id}"/>
      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
    </g:form>
  </div>
</div>
</body>
</html>
