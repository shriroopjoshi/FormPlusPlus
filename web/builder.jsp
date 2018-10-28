<%-- 
    Document   : builder
    Created on : Feb 25, 2015, 8:06:12 PM
    Author     : Shrinivas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Form Builder</title>
        <link rel="stylesheet" href="stylesheets/jquery-ui.min.css"/>
        <link rel="stylesheet" href="stylesheets/formcss.css"/>
        <link rel="stylesheet" href="stylesheets/newcss.css"/>
        <script src="scripts/jquery-1.10.2.js"></script>
        <script src="scripts/jquery-ui.min.js"></script>
        <script type="text/javascript">
            var FormName = "";

            var Addques = "-1";
            var Delques = "";
            var html = "<li style='padding-top: 20px;' id='c' class='card'> <div style='width: 100%'> <input onclick='showDet(\"d\")' style='float: left;' id='nq' class='questions' type='radio' name='questions' value='' checked><label id='lq' for='nq' style='float: left;'>Question</label><div style='float: right;padding-right: 5px;'><img onclick='fun2(\"d\")' id='d' class='delete' src='img/delete_button.gif'></div></div></li><br id='b'>";
            var props = ["question:::Question 1;;;help::: ;;;type:::TEXT;;;option::: ;;;required:::1"];
            var ctr = 2;
            var num = 1;
            $(document).ready(function () {
                $('#username').click(function () {
                    $('#logout').toggle('slow');
                });
                $('#logout').click(function () {
                    location.href = 'Logout';
                });
                $('#sortable').sortable().disableSelection();
                $('#addnew').click(function () {
                    $('#sortable').append(html.replace('id=\'c\'', 'id=\'c' + ctr + '\'').replace('id=\'d\'', 'id=\'d' + ctr + '\'').replace('nq', 'nq' + ctr).replace('lq', 'lq' + ctr).replace('\'nq\'', '\'nq' + ctr + '\'').replace('\'b\'', '\'b' + ctr + '\'').replace('(\"d\")', '(\"d' + ctr + '\")').replace('(\"d\")', '(\"d' + ctr + '\")').replace('>Question', '>Question ' + ctr));
                    Addques = Addques + "-" + ctr;
                    num = ctr;
                    props[ctr - 1] = "question:::Question 1;;;help::: ;;;type:::TEXT;;;option::: ;;;required:::1".replace("Question 1", "Question " + ctr);
                    ctr++;
                });
                $('#quest').keyup(function () {
                    $('#lq' + (num + 1)).text($('#quest').val());
                });
                $('#save').click(function () {
                    var question = $('#quest').val();
                    var help = $('#helpquest').val();
                    var type = $('#typequest').val();
                    var option = $('#listquest').val();
                    arr = option.split("\n");
                    option = arr[0];
                    for (var i = 1; i < arr.length; i++) {
                        option = option + "@@@" + arr[i];
                    }
                    var required = $('#reqquest').prop('checked');
                    if (required) {
                        props[num] = "question:::" + question + " ;;;help:::" + help + " ;;;type:::" + type + ";;;option:::" + option + " ;;;required:::1";
                    } else {
                        props[num] = "question:::" + question + " ;;;help:::" + help + " ;;;type:::" + type + ";;;option:::" + option + " ;;;required:::0";
                    }
                    alert("Properties saved.");
                });
                $('#typequest').change(function () {
                    var selected = $('#typequest').val();
                    if (selected == "RADIO BUTTON" || selected == "CHECK BOX" || selected == "DROP-DOWN LIST") {
                        $('#toggle').show();
                    } else {
                        $('#toggle').hide();
                    }
                });
                $('#saveform').click(function () {
                    var send = "";
                    for (var i = 0; i < props.length; i++) {
                        send = send + props[i] + "<br>";
                    }
                    enterHeading();
                    location.href = 'SaveForm?send=' + send + '&Delques=' + Delques + '&FormName=' + FormName;
                });
            });
            function enterHeading()
            {
                FormName = $('#FormName').val();
                //document.getElementById("headButt").value = FormName;
            }

            function fun2(x) {
                var num = x.substring(1);
                Delques = Delques + "-" + num;
                $('#c' + num).remove();
                $('#b' + num).remove();
            }
            function showDet(x) {
                num = x.substring(1) - 1;
                var pro = props[num];
                var split = pro.split(";;;");
                var question = split[0].split(":::")[1];
                var help = split[1].split(":::")[1];
                var type = split[2].split(":::")[1];
                if (type == "RADIO BUTTON" || type == "CHECK BOX" || type == "DROP-DOWN LIST") {
                    $('#toggle').show();
                } else {
                    $('#toggle').hide();
                }
                var option = split[3].split(":::")[1];
                akarr = option.split("@@@");
                optionTemp = "";
                for (var i = 0; i < akarr.length; i++) {
                    optionTemp = optionTemp + akarr[i] + "\n";
                }
                option = optionTemp;
                var required = split[4].split(":::")[1];
                $('#quest').val(question.trim());
                $('#helpquest').val(help.trim());
                $('#typequest').val(type.trim());
                $('#listquest').val(option.trim());
                if (required == "1") {
                    $('#reqquest').prop('checked', true);
                }
                if (required == "0") {
                    $('#reqquest').removeAttr('checked');
                }
            }
            function asdf() {

                return true;
            }
        </script>
    </head>
    <%!
        String displayName;
        String email;
        String name;
    %>
    <%
        email = request.getParameter("email");
        name = request.getParameter("name");
    %>
    <body>


        <%
            displayName = (String) session.getAttribute("dname");
            if (displayName == null || displayName.equalsIgnoreCase("")) {
                response.sendRedirect("index.jsp");
            }
        %>

        <div id="abs-dev">
            <div class="header-div">
                <div class="logo-div">
                    <img src="img/logo.png">
                </div>
                <div class="name-div">
                    <img src="img/formplusplus.png">
                </div>
                <div class="user-div" id="user-div-id">
                    <div style="float: left; padding: 1%; width: 250px; ">
                        Welcome <b><%=displayName%></b>
                    </div>
                    <br>
                    <div id="logout-div" hidden>
                        Logout
                    </div>
                </div>
            </div>
        </div>


        <div style="float: left;width: 100%;">
            <nav>
                <br>
                <center>Form Name: <input type="text" id="FormName"  style="height:35px; font-size: 1.7em"></center>
                <br>

                <ul style="list-style-type: none;">
                    <!-- <li style="float: left;display: list-item;padding: 5px 5px;"><a href="builder.jsp">Form Builder</a></li>
                     <li style="float: left;display: list-item;padding: 5px 5px;"><a href="myforms.jsp">My Forms</a></li>
                    -->
                </ul>
            </nav>
            <br>


        </div>
        <br>
        <br>
        <br>
        <br>
        <br>
        <div>
            <input id="addnew" type="button" value="Add New" style="width: 150px;margin-left: 200px;"/>
        </div>
        <div id="container" style="float: left;width: 50%;height: 400px; overflow-y: scroll;">
            <br>

            <br>
            <ul id="sortable" style="list-style-type: none;">
                <li style='padding-top: 20px;' id='c1' class='card'> <div style='width: 100%;'><input onclick='showDet("d1")' style='float: left;' id='nq1' class='questions' type='radio' name='questions' value='' checked><label id='lq1' for='nq1' style='float: left;'>Question 1</label><div style='float: right;padding-right: 5px;'><img onclick='fun2("d1")' id='d1' class='delete' src="img/delete_button.gif"></div></div></li><br id='b1'>
            </ul>
        </div>
        <div id="properties" style="float: left;width: 50%;">
            <center>
                <table border="1" cellpadding="5">
                    <thead>
                        <tr>
                            <th>Property</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Question</td>
                            <td> <input id="quest" type="text" name="quest" value="" size="45"/> </td>
                        </tr>
                        <tr>
                            <td>Help Text</td>
                            <td> <input id="helpquest" type="text" name="helpquest" value="" size="45"/> </td>
                        </tr>
                        <tr>
                            <td>Question Type</td>
                            <td> 
                                <select id="typequest" name="typequest">
                                    <option>TEXT</option>
                                    <option>BIG TEXT</option>
                                    <option>FILE</option>
                                    <option>DATE</option>
                                    <option>RADIO BUTTON</option>
                                    <option>CHECK BOX</option>
                                    <option>DROP-DOWN LIST</option>
                                </select>
                            </td>
                        </tr>
                        <tr id="toggle" hidden>
                            <td>List</td>
                            <td> 
                                <textarea id="listquest" name="listquest" rows="8" cols="40"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td>Required</td>
                            <td> <input id="reqquest" type="checkbox" name="reqquest" checked="checked" /> </td>
                        </tr>
                        <tr>
                            <td> <input id='save' type="button" value="Save" style="width:100px;"/> </td>
                            <td> Click on the question to edit and then click on Save</td>
                        </tr>
                    </tbody>
                </table>
                <br>
                <input id="saveform" type="button" value="Save Form"/>
            </center>
        </div>
    </body>
</html>
