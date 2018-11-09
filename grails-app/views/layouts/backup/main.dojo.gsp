<html>
<head>
  <title><g:layoutTitle default="Next"/></title>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'app.css')}"/>
  <g:javascript library="dojo"/>
  <g:javascript library="prototype"/>
  <g:javascript library="scriptaculous"/>
  <g:javascript library="application"/>
  <g:javascript library="app"/>
  <g:layoutHead/>
</head>
<body class="soria" role="application">
<div id="preLoader"><p></p></div>
<div dojoType="dijit.layout.BorderContainer" id="main">
  <div dojoType="dijit.Toolbar" region="top">
    <button id="options1" dojoType="dijit.form.Button" style="float:right;">帮助</button>
    <div dojoType="dijit.Tooltip" connectId="options">Set various options</div>
    <button id="options" dojoType="dijit.form.Button" style="float:right;">退出
      <script type="dojo/method" event="onClick">
        dojo.xhrGet("/userLogin/logout");
      </script>
    </button>
    <span style="float:left; height:30px">Welcome &nbsp;</span>
  </div>
  <div dojoType="dijit.layout.AccordionContainer" id="accordion" region="leading" minSize="20" style="width:18%;" splitter="true">
    <div dojoType="dijit.layout.AccordionPane" title="系统导航">
      <div dojoType="dojo.data.ItemFileReadStore" jsId="menuStore" url="<g:createLink controller="menu" action="buildmenu"/>"></div>
      <div dojoType="dijit.tree.ForestStoreModel" jsId="continentModel" store="menuStore" query="{type:'category'}" rootId="categoryRoot" rootLabel="菜单" childrenAttrs="children"></div>
      <div dojoType="dijit.Tree" id="menutree" model="continentModel" openOnClick="true">
        <script type="dojo/method" event="onClick" args="item">
          if (item != null && menuStore.getValue(item, "type") == 'poptart') {
          getText("/next2"+menuStore.getValue(item,"url"));
          }
        </script>
        <a href="" onclick="javascript:">test</a>
      </div>
    </div>
    <div dojoType="dijit.layout.AccordionPane" title="Administrator">
      PartyMgr
      RoleMgr
      UserMgr
    </div>
  </div>
  <div dojoType="dijit.layout.TabContainer" region="center">
    <div dojoType="dijit.layout.ContentPane" title="用户->新增->">
      <div id="toBeReplaced" style="width:100%"></div>
      <g:layoutBody/>
    </div>
  </div>
</div>
</body>
</html>