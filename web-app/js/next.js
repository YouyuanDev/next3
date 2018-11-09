Ext.util.CSS.swapStyleSheet('theme', 'css/xtheme-gray.css');

Ext.onReady(function() {

    // NOTE: This is an example showing simple state management. During development,
    // it is generally best to disable state management as dynamically-generated ids
    // can change across page loads, leading to unpredictable results.  The developer
    // should ensure that stable state ids are set for stateful components in real apps.
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    var menu1 = new Ext.menu.Menu({
        items:[
            {
                text:"ss",
                href:"http://jaychaoqun.cnblogs.cn",
                hrefTarget:"_blank"
                //icon:"icon-info.gif"
            }
        ]
    });

    /*
     var panel=new Ext.Panel({ //new 一个Panel 用以显示菜单
     //width:800,
     border:false,
     renderTo:"sysmenu",
     tbar:[{text:"ss",menu:menu1}
     ]
     });
     */
    var action = new Ext.Action({
        text: 'Action 1',
        handler: function() {
            Ext.example.msg('Click', 'You clicked on "Action 1".');
        },
        iconCls: 'blist'
    });
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
                border: false,
                //width:600,
                //height:300,
                //bodyStyle: 'padding:10px;',     // lazy inline style
                //tbar: [{text: 'Copy'}]
            }
        ]
        /*
         bbar: new Ext.ux.StatusBar({
         id: 'basic-statusbar',
         // defaults to use when the status is cleared:
         defaultText: 'Default status text',
         //defaultIconCls: 'default-icon',

         // values to set initially:
         text: label_ready,
         iconCls: 'x-status-valid',
         items: [
         '-',
         label_sysversion
         ]
         })*/
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
                title:'Administraotr',
                border:false
                //iconCls:'nav'
            },
            {
                title:'Settings',
                html:'<p>Some settings in here.</p>',
                border:false
                //iconCls:'settings'
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

    var menuTree = new Ext.tree.TreePanel({
        renderTo:"tree",
        animate:true,
        enableDD:false,
        border:false,

        loader: new Ext.tree.TreeLoader({
            dataUrl:'./menu/buildExt3Menu'
        }),
        root: new Ext.tree.AsyncTreeNode(),
        /*
         root: new Ext.tree.AsyncTreeNode({
         text: 'Autos',
         draggable:false,
         id:'source',
         children: json
         }),*/
        //root:root,
        rootVisible:false,
        listeners:{
            'click':function(node, event) {
                event.stopEvent();
                var n = mainPanel.getComponent(node.id);
				//alert(n);
				/*
                if (!n && node.attributes.url != null) {
                    var url = '/' + appName + '/' + node.attributes.url + '/'
                    n = mainPanel.add({
                        'id':node.id,
                        'title':node.text,
                        closable:true,
                        autoScroll: true,
                        timeout: 30,
                        text: "Loading...",
                        //tbar: [{text: 'Copy'}],
                        html:'<iframe style="overflow:auto;" frameborder="0" width="100%" height="100%" src="' + url + '"></iframe>'
                        //autoLoad:{url:'/next2/role/list', scripts:true}
                    });
                }
				*/
				
				if (n){
					n.remove();
					//n.destory();
				}
                if(node.attributes.url != null) {
                    var url = '/' + appName + '/' + node.attributes.url + '/'
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
                        //autoLoad:{url:'/next2/role/list', scripts:true}
                    }).show();
                }					
				
				//else{
				//	var tab = Ext.getCmp(node.id);
				//	tab.load();
					//tab.show();
					//window.frames["abc"].location.replace(url);
					//alert("a");
					//n.getUpdater().refresh();
					//Ext.getCmp(node.id).getUpdater().refresh();
					/*
					n.show();
					alert(window.frames[node.id]);
					window.frames[node.id].location.replace(url);
					*/
				//}
                /*
                 Ext.getCmp('main_content').load({
                 url: '/next2'+node.attributes.url,
                 discardUrl: false,
                 nocache: false,
                 text: "Loading...",
                 timeout: 30,
                 scripts: true
                 });
                 */
                //mainPanel.setActiveTab(n);
            }
        }
    });


    viewport.doLayout();

    setTimeout(function() {
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);
});
	