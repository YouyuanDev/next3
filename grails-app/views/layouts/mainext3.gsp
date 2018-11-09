<html>
<head>
<title><g:layoutTitle default="Next" /></title>
<link rel="stylesheet" type="text/css" href="${resource(dir:'js/ext-3.0.3/resources/css',file:'ext-all.css')}" />
<link rel="stylesheet" href="${resource(dir:'css',file:'next.css')}" />
<g:layoutHead />
</head>
<body>
	<div id="loading-mask" style=""></div>
	<div id="loading">
		<div class="loading-indicator"><img src="${resource(dir:'images',file:'extanim32.gif')}" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Hermes System<br/><span id="loading-msg">Loading styles and images...</span></div>
	</div>
	<g:javascript src="ext-3.0.3/adapter/ext/ext-base.js"/>
	<g:javascript src="ext-3.0.3/ext-all.js"/>
	<g:javascript src="ext-3.0.3/ux/StatusBar.js"/>
	<script type="text/javascript">
	Ext.util.CSS.swapStyleSheet('theme', 'css/xtheme-gray.css');
	var label_navigation="Navigation";
	var label_homePage="Home";
	var label_ready="Ready";
	var label_sysversion="version";
	var appName="${application["appName"]}";
	Ext.onReady(function() {
	    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	    var topPanel = new Ext.Panel({
	        border: false,
	        layout:'anchor',
	        region:'north',
	        cls: 'docs-header',
	        height:40,
	        items: [
	            {
	                xtype:'box',
	                el:'header',
	                border:false,
	                anchor: 'none -2'
	            }
	        ]
	        //tbar:[{text:"ss",menu:menu1}]
	    });
	    var mainPanel = new Ext.TabPanel({
	        region: 'center', // a center region is ALWAYS required for border layout
	        deferredRender: false,
	        enableTabScroll:true,
	        activeTab: 0,     // first tab initially active
	        items: [
	            {
	                id:'main_content',
	                contentEl: 'content',
	                title:'Home',
	                closable: false,
	                autoScroll: true,
	                border: false
	                //width:600,
	                //height:300,
	                //bodyStyle: 'padding:10px;',     // lazy inline style
	                //tbar: [{text: 'Copy'}]
	            }
	        ]
	    });
	    var navPanel = new Ext.Panel({
	        id:'navigation-panel',
	        title:label_navigation,
	        region:'west',
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
	                contentEl: 'navigation',
	                title:'Menus',
	                border:false
	                //iconCls:'nav'
	            }
				/*
	            {
	                title:'Settings',
	                html:'',
	                border:false
	                //iconCls:'settings'
	            }
				*/
	        ]
	    });

	    var viewport = new Ext.Viewport({
	        layout: 'border',
	        items: [topPanel,navPanel,mainPanel]
	    });
	    var menuTree = new Ext.tree.TreePanel({
	        renderTo:"tree",
	        animate:true,
	        enableDD:false,
	        border:false,
	        loader: new Ext.tree.TreeLoader({
	            dataUrl:'${resource(dir:'')}/menu/buildExt3Menu'
	        }),
	        root: new Ext.tree.AsyncTreeNode(),
	        rootVisible:false,
	        listeners:{
	            'click':function(node, event) {
	                event.stopEvent();
	                var n = mainPanel.getComponent(node.id);
	                if(node.attributes.url != null) {
	                    var url = '${resource(dir:'')}/' + node.attributes.url + '/'
	                    mainPanel.add({
	                        'id':node.id,
	                        'title':node.text,
	                        closable:true,
	                        autoScroll: true,
							layoutOnTabChange:true,
							autoDestroy:false,
	                        timeout: 30,
	                        text: "Loading...",
	                        //tbar: [{text: 'Copy'}],
	                        html:'<iframe id="abc" style="overflow:auto;" frameborder="0" width="100%" height="100%" src="' + url + '"></iframe>'
	                        //autoLoad:{url:url, scripts:true}
	                    }).show();
	                }					
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
	<div id="header">
		<a href="" style="float:left;margin-right:10px;"><img src="${resource(dir:'images',file:'next_logo.jpg')}" style="width:83px;height:33px;margin-top:1px;"/></a>
		<div class="api-title"><b></b>&nbsp;<a href ="http://www.youyuantech.com"><img title="上海友元信息科技有限公司 http://www.youyuantech.com" src="${resource(dir:'')}/images/youyuan-logo-A2_1.png"   height ="25PX"  /></a>|&nbsp;<a href="${createLink(controller:'auth',action:'logout')}">${message(code: 'common.logout.label', default: 'Sign out')}</a></div>
	</div>
	<div id="navigation" class="x-hide-display">
		 <div id="tree"></div>
	</div>
	<div id="content" class="x-hide-display">
		<div id="toBeReplaced" style="width:100%"></div>
		<g:layoutBody />
	</div>
	<div id="props-panel" class="x-hide-display" style="width:200px;height:200px;overflow:hidden;">
	</div>
	<div id="south" class="x-hide-display">
	</div>
</body>
</html>