<%-- 
    Document   : index
    Created on : 27 Feb, 2015, 1:37:10 PM
    Author     : dell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>FormPlusPlus</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="stylesheets/jquery-ui.min.css"/>
        <link rel="stylesheet" href="stylesheets/formcss.css"/>
        <link href="stylesheets/newcss.css" rel="stylesheet">
        <link href="stylesheets/formcss.css" rel="stylesheet">
        <script type="text/javascript" src="scripts/jquery-1.10.2.js"></script>
        <script src="scripts/jquery-ui.min.js"></script>


        <script>
            function Login() {
                location.href = 'GSignUp';
            }
        </script>

    </head>
    <body onload="location.href = '#bottom'" style="overflow: hidden;">
<!--
        <video autoplay="" loop="" id="bgvid" style="position: relative;">
            <source src="video/variousways.mp4" type="video/mp4">
        </video>-->



        <div id="abs-dev" style="z-index: 20; position: fixed; left:0; top:0; width: 100vw;">

            <div class="header-div">
                <div class="logo-div">
                    <img src="img/logo.png">
                </div>
                <div class="name-div">
                    <img src="img/formplusplus.png">
                </div>
                <!--<div class="user-div" id="user-div-id">-->
                <input type="button" value="Login" style="padding-left: 2%; padding-right: 2%; float: right; margin-top: 6%; margin-right: 4%; font-size: medium" onclick="Login();">
                <!--</div>-->
            </div>
        </div>
        <div id="bottom"></div>
    </body>
</html>
