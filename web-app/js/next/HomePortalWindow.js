Ext.onReady(function(){
var tools = [{
	id:'gear',
	handler: function(){
		Ext.Msg.alert('Message', 'The Settings tool was clicked.');
	}
},{
	id:'close',
	handler: function(e, target, panel){
		panel.ownerCt.remove(panel, true);
	}
}];
var content=Ext.getCmp("main_content").add({
	xtype:'portal',
	//region:'center',
	margins:'35 5 5 0',
	border: false,
	items:[{
		columnWidth:.50,
		style:'padding:10px 0 10px 10px',
		items:[{
			title: 'Portal 1',
			//tools: tools,
			//html: ''
			contentEl: 'welcome',
		}]
	},{
		columnWidth:.50,
		style:'padding:10px 0 10px 10px',
		items:[{
			title: 'Portal 2',
			tools: tools,
			html: ''
		},{
			title: 'Portal 3',
			tools: tools,
			html: ''
		}]
	}]});
});