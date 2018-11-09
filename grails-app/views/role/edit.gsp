<div class="nav">
  <span class="menuButton"><g:remoteLink class="list" action="list" update="toBeReplaced">列表</g:remoteLink></span>
  <span class="menuButton"><g:remoteLink class="create" action="create" update="toBeReplaced">创建</g:remoteLink></span>
</div>
<div class="body">
  <h1>Edit Role</h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${roleInstance}">
    <div class="errors">
      <g:renderErrors bean="${roleInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:formRemote name="roleForm" method="post" update="toBeReplaced" url="[action:'edit']">
    <input type="hidden" name="id" value="${roleInstance?.id}"/>
    <input type="hidden" name="version" value="${roleInstance?.version}"/>
    <div class="dialog">
      <table>
        <tbody>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="name">Name:</label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'name', 'errors')}">
            <input type="text" id="name" name="name" value="${fieldValue(bean: roleInstance, field: 'name')}"/>
          </td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="description">Description:</label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'description', 'errors')}">
            <input type="text" id="description" name="description" value="${fieldValue(bean: roleInstance, field: 'description')}"/>
          </td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="menus">Menus:</label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'menus', 'errors')}">
            <g:select name="menus"
                    from="${Menu.list()}"
                    size="5" multiple="yes" optionKey="id"
                    value="${roleInstance?.menus}"/>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" value="保存" action="update"/></span>
      <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="删除" action="delete"/></span>
    </div>
  </g:formRemote>
</div>
