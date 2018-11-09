<link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'app.css')}"/>
<filterpane:includes/>
<export:resource/>
<div class="nav">
  <span class="menuButton"><g:remoteLink class="create" action="create" update="toBeReplaced">����</g:remoteLink></span>
  <a onclick="dijit.byId('filterPane').show();
  return false;">aa</a>
</div>
<div class="body">
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>


  <div class="dialog">
    <filterpane:filterPane domainBean="Role"/>    <filterpane:filterButton text="aa"/>
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
      <!--
				<tr class="prop">
					<td valign="top" class="name">
						<label for="menus">Testing:</label>
					</td>
					<td valign="top" class="value ${hasErrors(bean: roleInstance, field: 'menus', 'errors')}">
						</br><g:datePicker name="myDate" value="${new Date()}" precision="day" years="${2009..2019}"/>
						</br><g:datePicker name="myDate" value="${new Date()}" precision="month" years="${1930..1970}"/>
						</br><g:datePicker name="myDate" value="${new Date()}" precision="year" years="${1930..1970}"/>
						</br><g:datePicker name="myDate" value="${new Date()}" noSelection="['':'-Choose-']"/>
						</br><g:currencySelect name="myCurrency" value="${currency}" from="['RMB','USD']"/>
						</br><g:timeZoneSelect name="myTimeZone" value="${tz}"/>
						</br><g:uploadForm name="myUpload"><input type="file" name="myFile" /></g:uploadForm>
						</br><g:textArea name="myField" value="myValue" rows="2" cols="80"/>
						</br><g:formatDate format="yyyy-MM-dd" date="${new Date()}"/>
						</br><g:formatDate date="${new Date()}" type="datetime" style="MEDIUM"/>
						</br><g:formatDate date="${new Date()}" type="datetime" style="LONG" timeStyle="SHORT"/>
						</br><g:formatDate date="${new Date()}" type="time" style="SHORT"/>
      <g:formatNumber number="${100}" type="currency" currencyCode="USD"/>
					</td>
				</tr>
				-->
      </tbody>
    </table>
  </div>
  <div class="buttons">
    <g:form>
      <input type="hidden" name="id" value="${roleInstance?.id}"/>
      <span class="button"><g:submitToRemote class="edit" value="��ѯ" update="toBeReplaced" action="edit"/></span>
    </g:form>
  </div>
  <h1>result</h1>
  <div class="list">
    <table>
      <thead>
      <tr>
        <g:remoteSortableColumn update="toBeReplaced" property="id" title="���"/>
        <g:remoteSortableColumn update="toBeReplaced" property="name" title="���"/>
        <g:remoteSortableColumn update="toBeReplaced" property="description" title="����"/>
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
  <div class="paginateButtons">
    <g:remotePaginate update="toBeReplaced" total="${roleInstanceTotal}"/>
  </div>
  <export:formats formats="['excel']"/>

</div>
