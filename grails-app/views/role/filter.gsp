<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>Book List</title>
  <g:javascript library="prototype"/>
  <filterpane:includes/>
</head>
<body>

<div class="nav">
  <span class="menuButton"><g:remoteLink class="create" action="create" update="toBeReplaced">创建</g:remoteLink></span>
</div>
<div class="body">
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <filterpane:filterPane domainBean="Role"/>    <filterpane:filterButton text="aa"/>
  <h1>result</h1>
  <div class="list">
    <table>
      <thead>
      <tr>
        <g:remoteSortableColumn update="toBeReplaced" property="id" title="编号"/>
        <g:remoteSortableColumn update="toBeReplaced" property="name" title="名称"/>
        <g:remoteSortableColumn update="toBeReplaced" property="description" title="描述"/>
      </tr>
      </thead>
      <tbody>
      <g:each in="${roleInstanceList}" status="i" var="roleInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
          <td width="4%"><g:remoteLink action="show" update="toBeReplaced" id="${roleInstance.id}">${fieldValue(bean: roleInstance, field: 'id')}</g:remoteLink></td>
          <td width="48%">${fieldValue(bean: roleInstance, field: 'name')}</td>
          <td width="48%">${fieldValue(bean: roleInstance, field: 'description')}</td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>