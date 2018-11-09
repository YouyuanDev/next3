<html>
<head>
  <title>${message(code: "default.login.label",default:"Login")}</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="blankext"/>

  <script type="text/javascript">



  
    var label_username = "${message(code: "default.common.username")}";
    var label_password = "${message(code: "default.common.password")}";
    var label_loginWindowWelcome = "${message(code: "default.window.loginWelcome")}";
    var label_loginWindowTitle = "${message(code: "default.window.loginTitle")}";
    var button_login = "${message(code: "default.buttons.login")}";
    var loginwindow_img = "${resource(dir:'')}/images/next_login.jpg";
    var loginwindow_img_tip = 'This is Hermes Logistics System';
    var blankText = 'username is null';
    var appName = "${application["appName"]}";

Ext.ns('Next');
Ext.util.CSS.swapStyleSheet('theme','../css/xtheme-gray.css');
Next.Form = Ext.extend(Ext.form.FormPanel, {

// defaults - can be changed from outside
	border:false
	,frame:true
	,labelWidth:80
	//,url:'/'+appName+'/auth/login'
    ,url:"${createLink(controlloer:"auth",action:"login")}" 
	,method:'POST'

	,constructor:function(config) {
		config = config || {};
		config.listeners = config.listeners || {};
		Ext.applyIf(config.listeners, {
			actioncomplete:function() {
				if(console && console.log) {
					console.log('actioncomplete:', arguments);
				}
			}
			,actionfailed:function() {
				if(console && console.log) {
					console.log('actionfailed:', arguments);
				}
			}
		});
		Next.Form.superclass.constructor.call(this, config);
	}
    ,initComponent:function() {

		// hard coded - cannot be changed from outsid
		var config = {
		//id: 'container',
        frame: true,
		border:false,
        title:"${message(code:"common.loginWindowWelcome.label",default:"Welcome to Hermes Logistics System")}",
        bodyStyle:'padding:5px',
		//layout:'fit',
        //width: 700,
        layout: 'column',    // Specifies that the items will now be arranged in columns
        items: [{
            columnWidth: 0.5,
            //layout: 'fit',
            items: {
				xtype:'box'
				,anchor:''
				//,isFormField:true
				,fieldLabel:''
				,autoEl:{
					tag:'div', children:[{
						tag:'img'
						,qtip:"${message(code: 'common.logwindowImageTip.label', default: 'This is the Hermes Logistics system')}"
						,src:"${resource(dir:'')}/images/next_login.jpg"
						//,layout:'fit'
						,width:320
						,height:200
						//,style:'{width:300px;height:300px;}'
						}
						/*
						,{
						tag:'div'
						,style:'{margin:0 0 0px 0;width:300px;height:300px;}'
						,html:'please login system'
						}*/
					]
				}
            }
        },{
            columnWidth: 0.5,
            xtype: 'fieldset',
			//defaults:{anchor:'-24'},
			//monitorValid:true,
            //labelWidth: 90,
            title:"${message(code:"common.loginWindowTitle",default:"Please Login System")}",
            //defaults: {width: 140, border:false},    // Default config options for child items
            //defaultType: 'textfield',
            //autoHeight: true,
            bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
            ///border: false,
            //style: {
            //    "margin-left": "10px", // when you add custom margin in IE 6...
            //    "margin-right": Ext.isIE6 ? (Ext.isStrict ? "-10px" : "-13px") : "0"  // you have to adjust for it somewhere else
            //},
            items: [{
				xtype:'textfield',
                fieldLabel: "${message(code: 'common.username.label', default: 'User ID')}",
				allowBlank:false,
				blankText : "${message(code: 'warning.userIsEmptympty.label', default: 'User is Empty')}",
				width: 180,
                name: 'login'
            },{
				xtype:'textfield',
                fieldLabel: "${message(code: 'common.password.label', default: 'Password')}",
				allowBlank:false,
				inputType:'password',
				blankText :  "${message(code: 'warning.passwordIsEmpty.label', default: 'Password is Empty')}",
				width: 180,
                name: 'password'
            }],
			buttonAlign:'left',
			buttons:[{
			 text:"${message(code: 'common.login.label', default: 'Login')}"
			 ,scope:this
			 ,formBind:true
			 ,handler:this.submit
			 }]
        }]
		}; // eo config object
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Next.Form.superclass.initComponent.apply(this, arguments);
    } // eo function initComponent

    ,onRender:function() {
        Next.Form.superclass.onRender.apply(this, arguments);
		this.getForm().waitMsgTarget = this.getEl();
		//this.on('afterlayout', this.onLoadClick, this, {single:true});
     } // eo function onRender
	,submit:function() {
		this.getForm().submit({
			 url:this.url
			,method : 'POST'
			//,scope:this
			,success:this.onSuccess
			,failure:this.onFailure
			,waitMsg:"${message(code: 'common.logining.label', default: 'Logining...')}"
		});

	}
	,reset:function(){
		this.getForm().reset();
	}
	,onSuccess:function(form, action) {
		/*
		Ext.Msg.show({
			 title:'Success'
			,msg:'Form submitted successfully'
			,modal:true
			,icon:Ext.Msg.INFO
			,buttons:Ext.Msg.OK
		});
		*/
		window.location = "${resource(dir:'')}/main.gsp";
	}

	,onFailure:function(form, action) {
		if(action.failureType == 'server'){
			obj = Ext.util.JSON.decode(action.response.responseText);
			Ext.Msg.alert('Login Failed!', obj.errors.reason);
			form.reset();
		}else{
			Ext.Msg.alert('Warning!', 'Please input username & password');
			form.reset();
		}
	}

	,showError:function(msg, title) {
		title = title || 'Error';
		Ext.Msg.show({
			 title:title
			,msg:msg
			,modal:true
			,icon:Ext.Msg.ERROR
			,buttons:Ext.Msg.OK
		});
	}
});
Ext.reg('loginform', Next.Form);
Ext.onReady(function() {
   Ext.QuickTips.init();
   Ext.form.Field.prototype.msgTarget = 'side';
	var win = new Ext.Window({
		 id:'formloadsubmit-win'
		,title:''
		,layout:'fit'
		,width:680
		,height:260
		,closable:false
		,border:false
		,items:{id:'formloadsubmit-form', xtype:'loginform'}
	});
	win.show();
}); // eo function onReady
    
 </script>
</head>
<body>
<br><br><br><br><br><br><br><br><br><br>
<br><br><br><br><br><br><br><br><br><br>
<br><br><br><br><br><br><br><br><br><br>
<br><br><br><br><br><br><br><br><br><br>
<br><br><br><br>
<h1  align="center" style="margin-left:80px;color:gray;"><a href ="http://www.youyuantech.com"><img title="上海友元信息科技有限公司 http://www.youyuantech.com" src="${resource(dir:'')}/images/youyuan-logo-A2.png"   height ="80PX"  /></a></h1>
</body>
</html>