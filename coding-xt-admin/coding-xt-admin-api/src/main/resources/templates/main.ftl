<!DOCTYPE html>
<html>
<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>码神学堂管理后台</title>
    <meta name="description" content="中小学题库大全">
    <meta name="keywords" content="中小学题库大全">
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 引入样式 http://loclahost:8228/lzadmin/plugins/xxx-->
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/plugins/elementui/index.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/plugins/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/css/style.css">
    <style type="text/css">
    .el-main{
        position: absolute;
        top: 70px;
        bottom: 0px;
        left: 200px;
        right: 10px;
        padding: 0;
    }
    </style>
</head>
<body class="hold-transition skin-purple sidebar-mini">
    <div id="app">

        <el-container>

            <el-header  class="main-header" style="height:70px;">
                <nav class="navbar navbar-static-top" :class=''>
                    <!-- Logo -->
                    <a href="#" class="logo" style="text-align:center">
                        <span class="logo-lg">码神学堂管理后台</span>
                    </a>
                    <div class="right-menu">
                        <span class="help"><i class="fa fa-exclamation-circle" aria-hidden="true"></i>帮助</span>
                        <el-dropdown class="avatar-container right-menu-item" trigger="click">
                            <div class="avatar-wrapper">
                                <img src="../img/head_img.png" class="user-avatar">
                                ${username!'默认用户'}
                            </div>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item divided>
                                    <span style="display:block;">修改密码</span>
                                </el-dropdown-item>
                                <el-dropdown-item divided>
                                        <a href="/lzadmin/logout"><span style="display:block;">退出</span></a>
                                    </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </nav>
            </el-header>

            <el-container>

                <el-aside width="200px">
                    <el-menu>
                        <!--freemarker有标签指令 list指令-->
                        <#list menuList as menu>
                            <el-submenu index="${menu.path}">
                                <template slot="title">
                                    <i class="fa" class="${menu.icon}"></i>
                                    ${menu.title}
                                </template>
                                <#list menu.children as child>
                                    <template>
                                        <el-menu-item index="${child.path}">
                                            <!--contextPath /lzadmin-->
                                            <a href="${springMacroRequestContext.contextPath+ "/pages/" + child.linkUrl}" target="right">${child.title}</a>
                                        </el-menu-item>
                                    </template>
                                </#list>
                            </el-submenu>
                        </#list>
                    </el-menu>
                </el-aside>

                <el-container>
                    <iframe name="right" class="el-main" src="topic.html" width="100%" height="800px" frameborder="0"></iframe>
                </el-container>

            </el-container>

        </el-container>
    </div>
</body>
<!-- 引入组件库 -->
<script src="${springMacroRequestContext.contextPath}/js/vue.js"></script>
<script src="${springMacroRequestContext.contextPath}/plugins/elementui/index.js"></script>
<script type="text/javascript" src="${springMacroRequestContext.contextPath}/js/jquery.min.js"></script>
<script src="${springMacroRequestContext.contextPath}/js/axios-0.18.0.js"></script>
<script>
    new Vue({
            el: '#app',
            method:{

            },
        created:function () {
        },
        data:{
        }
    });
    $(function() {
            var wd = 200;
            $(".el-main").css('width', $('body').width() - wd + 'px');
    });
</script>
</html>
