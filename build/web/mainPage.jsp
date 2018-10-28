<%-- 
    Document   : mainPage
    Created on : 8 Mar, 2015, 9:48:34 AM
    Author     : dell
--%>

<%@page import="beans.Form"%>
<%@page import="beans.FormDetail"%>
<%@page import="operations.FormDetailOperations"%>
<%@page import="operations.FormOperations"%>
<%@ page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%! String displayName;
    ArrayList<FormDetail> foarr;
    ArrayList<String> arr;
    int i = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="stylesheets/formcss.css" rel="stylesheet" />
        <link href="stylesheets/newcss.css" rel="stylesheet" />
        <link href="stylesheets/jquery-ui.min.css" rel="stylesheet" />
        <script src="scripts/jquery-1.10.2.js"></script>
        <!--<script src="http://code.jquery.com/jquery-latest.min.js"></script>-->
        <script>
            var xmlHttp;

            function download(id) {
                $.ajax({
                    url: "DownloadResponse?fid=" + id,
                    success: function (result) {
                        window.open(result, '_blank');
                    }
                });
            }

            function deleteForm(id, i) {
                alert(i);
                var res = $('#res_' + i).prop('checked');
                $.ajax({
                    url: "DeleteForm?fid=" + id,
                    success: function (result) {
                        alert(result);
                        $('#tr_' + i).remove();
                    },
                    error: function () {
                        alert('Error');
                    }
                });
            }

            function generateLink(fid, i) {
                //var res = $('#res_' + i).prop('checked');
                $.ajax({
                    url: "GenerateFormLink?fid=" + fid,
                    success: function (result) {
                        $('#but_' + i).remove();
                        $('#link_' + i).html("<a href='" + result + "'>" + result + "</a>");
                        //document.getElementById("link_" + i).innerHTML = result;
                    },
                    error: function () {
                        alert('Error');
                    }
                });
            }

            $(document).ready(function () {
                $('#user-div-id').click(function () {
                    $('#logout-div').toggle('slow');
                });
                $('#logout-div').click(function () {
                    location.href = 'logout.html';
                });
            });
        </script>
    </head>
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
        <br>
        <input style="padding-left: 2%; padding-right: 2%;" type="button" value="Create New" onclick="location.href = 'builder.jsp'"/>
        <br><br>
        <%
            arr = FormOperations.getFormIdFromUserId((String) session.getAttribute("uname"));
            if (arr.isEmpty()) {
        %>
        <font color="gray"><b><h4>No Forms created yet</h4></b></font>

        <%            } else {
            System.out.println("AAAAAAAAA");
            ArrayList<FormDetail> ttt;
            foarr = new ArrayList<FormDetail>();
            for (String s : arr) {
                System.out.println("BB");
                ttt = FormDetailOperations.selectAll(s);
                System.out.println("CC");
                foarr.addAll(ttt);
                System.out.println("DD");
            }
            if (foarr.isEmpty() || foarr == null) {
        %>
        No Forms created yet
        <%                } else {
        %>
        <table style="border-collapse: collapse; width: 750px; border: 1px;">
            <tr>
                <th style="width: 48px;text-align: left;">Number</th>
                <th style="width: 500px; text-align: left;">Form Name</th>
                <th style="width: 200px;text-align: left;">Date Created</th>
                <th style="width: 100px;text-align: left;">Delete</th>
                <th style="width: 100px;text-align: left;">Download Response</th>
                <th style="width: 100px;text-align: left;">Generate link</th>
                <th style="width: 100px;text-align: left;">Analyze</th>
            </tr>
            <% //loop space for
                i = 1;
                for (FormDetail f : foarr) {
            %>
            <tr id="tr_<%=i%>">
                <td style="width: 48px; text-align: left;"><%=i%></td>
                <td style="width: 500px; text-align: left;"><%=f.getFormName()%></td>
                <td style="width: 200px; text-align: left;"><%=f.getDateCreated()%></td>
                <td style="width: 200px; text-align: left;"><center><input type="button" onclick="deleteForm('<%=f.getFormID()%>', '<%=i%>');" value="Delete"></center></td>
        <td style="width: 200px; text-align: left;"><center><a href="javascript:download('<%=f.getFolderID()%>');">Download</a></center></td>
    <td style="width: 200px; text-align: left;" id="link_<%=i%>"><center><input type="button" id="but_<%=i%>" onclick="generateLink('<%=f.getFormID()%>', '<%=i%>');" value="Generate"></center></td>
<td style="width: 200px; text-align: left;"><center><input type="button" onclick="location.href = 'Reading?ind=1&formId=<%=f.getFormID()%>'" value="Analyze"></center></td>
    <%
        i++;
    %>
</tr>
<%
        }
    }
%>
</table>
<%
    }
%>
<br><br><br>
</body>
</html>
