
<#include "/admin/common/css.ftl" >
<#include "/admin/common/js.ftl" >

    <link href="${ctx!}/hadmin/css/login.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>
        if (window.top !== window.self) {
            window.top.location = window.location;
        }
    </script>

</head>

<body class="signin" style="margin-top: 250px">
    <div class="signinpanel">
        <div class="row">
            <div class="col-sm-12">
                <img src="${ctx!}/hadmin/img/login-txt.png">
            	<#if message?exists >
	            	<div class="alert alert-danger">
	                    ${message!}
	                </div>
                </#if>

                <form autocomplete="off"  method="post" action="${ctx!}/admin/login" id="frm" style="margin-left: 80px;">
                    <h4 class="no-margins">登录：</h4>
                    <input type="text" class="form-control uname" name="username" id="username" placeholder="工号" />
                    <input type="password" class="form-control pword m-b" name="password" id="password"  placeholder="密码" />
                    <button class="btn btn-success btn-block">登录</button>
                </form>
            </div>
        </div>
    </div>

	<script type="text/javascript">
    $().ready(function() {
    	// 在键盘按下并释放及提交后验证提交表单
    	  $("#frm").validate({
    	    rules: {
    	      username: {
    	        required: true,
    	      },
                password:{
    	          required: true
                }
    	    },
    	    messages: {
    	      username: {
    	        required: "请输入工号",
    	      },
                password:"请输入密码"
    	    },
    	    submitHandler:function(form){
                form.submit();
            }
    	});
    });

    </script>
