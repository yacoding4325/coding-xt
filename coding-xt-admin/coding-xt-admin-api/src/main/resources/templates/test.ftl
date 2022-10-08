<!DOCTYPE html>
<html>
<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
</head>

<body>

    <#if flag==1>
        1 进来这里
    <#elseif flag==2>
        2 进来这里
    <#else>
        其他来这里
    </#if>

<br />
   用户信息: ${user!'vistor'}
<br />
    <#macro greet>
        <span style="font-size: 144%; ">Hello Mszlu!</span>
    </#macro>

    <#--未带参数宏调用-->
    <@greet></@greet>

    <#macro greet_2 person>
        <span style="font-size: 144%; ">Hello ${person}!</span>
    </#macro>
    <@greet_2 person="码神之路"></@greet_2>

    <br/>

    <@strstr username="码神" city="北京">
        ${result}
    </@strstr>

    <br />
    <hr>
    ${timeAgo(date)}
<br />
<#include "footer.ftl">
</body>
</html>