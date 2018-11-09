<html>
<head>
  <title><g:layoutTitle default="Next"/></title>
  <link rel="stylesheet" type="text/css" href="${resource(dir: 'js/ext-3.0.3/resources/css', file: 'ext-all.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'app.css')}"/>

  <style type="text/css">
  html, body {
    font: normal 12px verdana;
    margin: 0;
    padding: 0;
    border: 0 none;
    overflow: hidden;
    height: 100%;
  }

  p {
    margin: 5px;
  }

  #loading-mask {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 20000;
    background-color: white;
  }

  #loading {
    position: absolute;
    left: 45%;
    top: 40%;
    padding: 2px;
    z-index: 20001;
    height: auto;
  }

  #loading a {
    color: #225588;
  }

  #loading .loading-indicator {
    background: white;
    color: #444;
    font: bold 13px tahoma, arial, helvetica;
    padding: 10px;
    margin: 0;
    height: auto;
  }

  #loading-msg {
    font: normal 10px arial, tahoma, sans-serif;
  }

  #header {
    border: 0 none;
    background: #1E4176 url(hd-bg.gif) repeat-x 0 0;
    padding-top: 3px;
    padding-left: 3px;
  }

  .docs-header .x-panel-body {
    background: transparent;
  }

  #header .api-title {
    font: normal 16px tahoma, arial, sans-serif;
    color: white;
    margin: 5px;
  }

  </style>
  <script type="text/javascript" src="${resource(dir: 'js/ext-3.0.3/adapter/ext', file: 'ext-base.js')}"></script>
  <script type="text/javascript" src="${resource(dir: 'js/ext-3.0.3', file: 'ext-all.js')}"></script>
  <script type="text/javascript">
    Ext.onReady(function() {

      // NOTE: This is an example showing simple state management. During development,
      // it is generally best to disable state management as dynamically-generated ids
      // can change across page loads, leading to unpredictable results.  The developer
      // should ensure that stable state ids are set for stateful components in real apps.
      Ext.state.Manager.setProvider(new Ext.state.CookieProvider());


      var topPanel = new Ext.BoxComponent({
        border: false,
        layout:'anchor',
        region:'north',
        cls: 'docs-header',
        height:35,
        /*
         autoEl: {
         tag: 'div',
         html:'<a href="http://extjs.com" style="float:right;margin-right:10px;"><img src="resources/extjs.gif" style="width:83px;height:24px;margin-top:1px;"/></a><div class="api-title">Ext 3.0 - API Documentation</div>'
         }*/
        items: [
          {
            xtype:'box',
            el:'header',
            border:false,
            anchor: 'none -25'
          }
        ]
      });
      var mainPanel = new Ext.TabPanel({
        region: 'center', // a center region is ALWAYS required for border layout
        deferredRender: false,
        enableTabScroll:true,
        activeTab: 0,     // first tab initially active
        items: [
          {
            contentEl: 'center2',
            title: 'Center Panel',
            closable: false,
            autoScroll: true
          }
        ]
      });
      var navPanel = new Ext.Panel({
        region:'west',
        id:'west-panel',
        title:'West',
        split:true,
        width: 200,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
        margins:'0 0 0 5',
        layout:'accordion',
        layoutConfig:{
          animate:true
        },
        items: [
          {
            contentEl: 'west',
            title:'Navigation',
            border:false,
            iconCls:'nav'
          },
          {
            title:'Settings',
            html:'<p>Some settings in here.</p>',
            border:false,
            iconCls:'settings'
          }
        ]
      });

      var viewport = new Ext.Viewport({
        layout: 'border',
        items: [topPanel,navPanel,mainPanel]
      });
      /*
       Ext.get("hideit").on('click', function(){
       var w = Ext.getCmp('west-panel');
       w.collapsed ? w.expand() : w.collapse();
       });
       */

      var root = new Ext.tree.TreeNode({
        id:"root",//根节点id
        text:"我是树根"
      });

      //定义树节点
      var c1 = new Ext.tree.TreeNode({
        id:'c1',//子结点id
        text:'我是大儿子'
      });

      var c2 = new Ext.tree.TreeNode({
        id:'c2',
        text:'我是小儿子'
      });
      var c22 = new Ext.tree.TreeNode({
        id:'c22',
        text:'我是大孙子'
      });
      root.appendChild(c1);//为根节点增加子结点c1
      root.appendChild(c2);//为c1增加子节点c2，相信你已经找到规律了吧^_^
      c1.appendChild(c22);//为c1增加子节点c22

      var menuTree = new Ext.tree.TreePanel({
        renderTo:"tree",
        root:root,
        animate:true,
        enableDD:false,
        border:false,
        rootVisible:false,
        listeners:{
          'click':function(node, event) {
            event.stopEvent();
            var n = mainPanel.getComponent(node.id);
            if (!n) {
              n = mainPanel.add({
                'id':node.id,
                'title':node.text,
                closable:true,
                autoScroll: true,
                html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src=""></iframe>'
                //autoLoad:{url:'/next2/role/list', scripts:true} //通过autoLoad属性载入目标页,如果要用到脚本,必须加上scripts属性
              });
            }
            mainPanel.setActiveTab(n);
          }
        }
      });


      viewport.doLayout();

      setTimeout(function() {
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
      }, 250);
    });

  </script>
</head>
<body>
<div id="header">
  <a href="" style="float:right;margin-right:10px;"><img src="${resource(dir: 'images', file: 'logo.gif')}" style="width:83px;height:24px;margin-top:1px;"/></a>
  <div class="api-title">Ext 3.0 - API Documentation</div>
</div>
<!--
		<div id="loading">
			<div class="loading-indicator"><img src="${resource(dir: 'js/ext-3.0.3/resources/images/default/shared', file: 'large-loading.gif')}" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Loading - Next System<br /><span id="loading-msg">Loading styles and images...</span></div>
		</div>
		-->
<div id="loading-mask" style=""></div>
<div id="loading">
  <div class="loading-indicator"><img src="shared/extjs/images/extanim32.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Ext 3.0 - <a href="http://extjs.com">extjs.com</a><br/><span id="loading-msg">Loading styles and images...</span></div>

</div>

<!-- use class="x-hide-display" to prevent a brief flicker of the content -->
<div id="west" class="x-hide-display">
  <div id="tree"></div>
  <p>Hi. I'm the west panel.</p>
</div>
<div id="center2" class="x-hide-display">
  <div id="toBeReplaced" style="width:100%"></div>
  <g:layoutBody/>
</div>

<div id="props-panel" class="x-hide-display" style="width:200px;height:200px;overflow:hidden;">
</div>
<div id="south" class="x-hide-display">
  <p>south - generally for informational stuff, also could be for status bar</p>
</div>
</body>
</html>