<div class="nav">
  <span class="menuButton"><g:remoteLink class="list" action="list" update="toBeReplaced">列表</g:remoteLink></span>
  <span class="menuButton"><g:remoteLink class="create" action="create" update="toBeReplaced">创建</g:remoteLink></span>
</div>
<div class="body">
  <h1>Show Role</h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="dialog">
    <table>
      <tbody>
      <tr class="prop">
        <td valign="top" class="name">Id:</td>
        <td valign="top" class="value">${fieldValue(bean: roleInstance, field: 'id')}</td>
      </tr>
      <tr class="prop">
        <td valign="top" class="name">Name:</td>

        <td valign="top" class="value">${fieldValue(bean: roleInstance, field: 'name')}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name">Description:</td>

        <td valign="top" class="value">${fieldValue(bean: roleInstance, field: 'description')}</td>

      </tr>
      <tr class="prop">
        <td valign="top" class="name">Menus:</td>

        <td valign="top" style="text-align:left;" class="value">
          <ul>
            <g:each var="m" in="${roleInstance.menus}">
              <li><g:remoteLink controller="menu" action="show" id="${m.id}" update="toBeReplaced">${m?.encodeAsHTML()}</g:remoteLink></li>
            </g:each>
          </ul>
        </td>

      </tr>

      </tbody>
    </table>
  </div>
  <div class="buttons">
    <g:form>
      <input type="hidden" name="id" value="${roleInstance?.id}"/>
      <span class="button"><g:submitToRemote class="edit" value="编辑" update="toBeReplaced" action="edit"/></span>
      <span class="button"><g:submitToRemote class="delete" onclick="return confirm('Are you sure?');" value="删除" update="toBeReplaced" action="delete"/></span>
    </g:form>
  </div>
</div>
		
