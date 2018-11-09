<g:render template="baseFormBefore" model="['title':message(code: 'shipment.shipment.label', default: 'Shipment'),'form':'Y','formId':'formId','action':'action','controller':'controller'"/>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
    <span class="ui-jqgrid-title">${title}</span>
  </div>
  <g:form name="${formId}" id="${formId}" controller="${controller}" action="${action}">
  <div class="ui-dialog-content ui-widget-content">
    <table>
      <tbody>
      <tr class="prop">
        <td class="name"><g:message code="invoice.code.label" default="Invoice Code"/></td>
        <td class="value"><g:textField name="invoiceCode"/></td>
        <td class="name"><g:message code="business.code.label" default="BP Code"/></td>
        <td class="value"><g:textField name="bpCode"/></td>
      </tr>
      <tr class="prop">
        <td class="name"><g:message code="invoice.reinvoice.label" default="Re Invoice"/></td>
        <td class="value"><g:textField name="reInvoice"/></td>
        <td class="name"></td>
        <td class="value"></td>
      </tr>
      <tr class="prop">
        <td class="name"><g:message code="category.code.label" default="Product Short"/></td>
        <td class="value"><g:textField name="categoryCode"/></td>
        <td class="name"><g:message code="product.code.label" default="Product Full"/></td>
        <td class="value"><g:textField name="productCode"/></td>
      </tr>
      <tr class="prop">
        <td class="name"><g:message code="product.dept.label" default="Dept"/></td>
        <td class="value"><g:textField name="productDept"/></td>
        <td class="name"><g:message code="product.material.label" default="Material"/></td>
        <td class="value"><g:textField name="productMaterial"/></td>
      </tr>
      <tr class="prop">
        <td class="name"><g:message code="common.sort.label" default="Sort"/></td>
        <td class="value">
          <g:radioGroup name="sort" labels="['Dept','Material','Product']" values="['Dept','Material','Product']" value="Dept">
           ${it.label} ${it.radio}
          </g:radioGroup>
        </td>
        <td class="name"></td>
        <td class="value"></td>
      </tr>
      </tbody>
    </table>
  </div>
  <div class="buttons">
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.search.label', default: 'Search')}"/></span>
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.hawb.label', default: 'Hawb')}"/></span>
    <span class="button"><g:actionSubmit class="save" onclick="gridReload()"  action="update" value="${message(code: 'default.button.receivedate.label', default: 'Receive Date')}"/></span>
  </div>
  </g:form>
</div>
