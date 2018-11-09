<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <resource:include components="tagCloud,resource:tooltip,accordion,autoComplete, dateChooser,resource:calendarDayView,calendarMonthView"/>
  <title></title>
</head>
<body>
<resource:dateChooser skin="default"/>
<g:form><richui:dateChooser name="birthday" format="dd.MM.yyyy"/></g:form>
<resource:accordion skin="default"/>
<richui:tagCloud values="['Java': 5, 'Grails': 16, 'Groovy': 12]"/>
<resource:tooltip skin="classic"/>
<a id="sample" href="#" title="This is a tooltip">A link</a> <richui:tooltip id="sample"/>
<richui:accordion style="width: 500px;"><richui:accordionItem caption="Sample 1">A sample text.</richui:accordionItem>

  <richui:accordionItem caption="Sample 2">Another sample text.</richui:accordionItem>

  <richui:accordionItem caption="Sample 3">Even another sample text.</richui:accordionItem></richui:accordion>
</body>
</html>
