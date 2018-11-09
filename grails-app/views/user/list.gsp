<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
  <filterpane:includes/>
  <export:resource/>
</head>

<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${resource(dir: '')}">首页</a></span>
  <span class="menuButton"><a class="home" onclick="showElement('filterPane');
  return false;" href="">Filter</a></span>
  <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label"/></g:link></span>
  <span class="menuButton"><a class="excel" href="/next2/user/list?format=excel&extension=xls">EXCEL</a></span>
</div>
<filterpane:filterPane domainBean="User"/>
<br>
<div class="body">
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="list">
    <table>
      <thead>
      <tr>

        <g:sortableColumn property="id" title="${message(code: 'user.id.label', default: 'Id')}"/>

        <g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'Email')}"/>

        <g:sortableColumn property="password" title="${message(code: 'user.password.label', default: 'Password')}"/>

        <th><g:message code="user.party.label" default="Party"/></th>

      </tr>
      </thead>
      <tbody>
      <g:each in="${userInstanceList}" status="i" var="userInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: userInstance, field: "email")}</td>

          <td>${fieldValue(bean: userInstance, field: "password")}</td>

          <td>${fieldValue(bean: userInstance, field: "party")}</td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
  <div class="x-toolbar-ct">
    <g:paginate total="${userInstanceTotal}"/>
  </div>
  <g:lookupSelect id="status" name="status" realm="order.status" value="${Confirm}"/>
  <g:lookupValue realm="order.status" value="Confirm"/>
</div>
</body>
</html>
