<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <gui:resources components="datePicker,dialog,accordion,expandablePanel,tabView,tab"/>
  <title></title>
</head>
<body>
<gui:dialog
        title="Modal Dialog"
        triggers="[show:[type:'link', text:'Click for dialog', on:'click']]"
        modal="true">
  This message will appear in a modal dialog.
</gui:dialog>
<gui:datePicker id="myDatePicker"/>
<script>
  YAHOO.util.Event.onDOMReady(function() {
    function selectHandler() {
      alert('date selected on myDatePicker!');
    }

    GRAILSUI.myDatePicker.selectEvent.subscribe(selectHandler);
  });
</script>

<script>
  var yesHandler = function(o) {
    alert('You clicked "Yes"');
    this.cancel();
  }
</script>
<gui:dialog
        title="Modal Confirm Dialog"
        draggable="false"
        modal="true"
        buttons="[
        [text:'Yes', handler: 'yesHandler', isDefault: true],
        [text:'No', handler: 'function() {this.cancel();}', isDefault: false]
    ]"
        triggers="[show:[type:'link', text:'Confirm', on:'click']]">
  Are you sure?
</gui:dialog>


<gui:dialog
        form="true"
        controller="demo" action="confirmSomething"
        update="thingBeingUpdatedByResponse"
        triggers="[show:[type:'link', text:'Confirm', on:'click']]">
  Are you sure?
</gui:dialog>

<gui:tabView>
  <gui:tab label="Tab 1" active="true">
    <h1>Inside Tab 1</h1>
    <p/>You can put whatever markup you want here.
  </gui:tab>
  <gui:tab label="Tab 2">
    <h1>Inside Tab 2</h2>
    <gui:richEditor id='editor' value="You can use gui components within tabs, too!"/>
  </gui:tab>
</gui:tabView>

<gui:accordion>
  <gui:accordionElement title="Accordion element 1">
    Accordion element 1 content
  </gui:accordionElement>
  <gui:accordionElement title="Accordion element 2">
    <h3>Markup is fine in here</h3>
  </gui:accordionElement>
</gui:accordion>

<gui:accordion bounce="true" slow="true" multiple="true">
  <gui:accordionElement title="Accordion element 1">
    Accordion element 1 content
  </gui:accordionElement>
  <gui:accordionElement title="Accordion element 2">
    <g:each var="i" in="(1..10)">Hello ${i}<br/></g:each>
  </gui:accordionElement>
</gui:accordion>

<gui:expandablePanel title="This panel is already expanded" expanded="true">
  I am expanded.  Close me.
</gui:expandablePanel>

<br/><br/>

<gui:expandablePanel bounce="false" title="Expand me for a fun surprise!" expanded="false">I was not expanded at first, but if you are reading this, I must be expanded now.</gui:expandablePanel>

<gui:toolTip text="This text shows in a tool tip.">
  <img src="/myImg.png"/>
</gui:toolTip>

<gui:datePicker id='withDateValue' value="${new Date()}"/>
<gui:datePicker
        id='withCalendar'
        value="${new GregorianCalendar(2008, 9, 31)}"
        formatString="yyyy/MM/dd HH:mm:ss"/>
</body>
</html>
