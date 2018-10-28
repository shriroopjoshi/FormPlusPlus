<%-- 
    Document   : viewForm
    Created on : 16 Mar, 2015, 4:23:47 PM
    Author     : dell
--%>

<%@page import="servlets.SaveForm"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="beans.FormDetail"%>
<%@page import="operations.FormDetailOperations"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    String title;
    String HTML;
    FormDetail formDetail;
%>
<%
    String folder_id = request.getParameter("form");
    formDetail = FormDetailOperations.selectAllFromFolderId(folder_id);
    if (formDetail == null) {
        response.sendRedirect("wrongFrom.jsp");
    } else {
        session.setAttribute("formDetail", formDetail);
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(formDetail.getPublicLink()).openStream()));
        String str = br.readLine();
        String finalStr = "";
        while (str != null) {
            finalStr = finalStr + str + "\n";
            str = br.readLine();
        }
        session.setAttribute("rawForm", finalStr);
        HTML = SaveForm.makeHTML(finalStr);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="stylesheets/formcss.css" rel="stylesheet" />
        <link href="stylesheets/jquery-ui.min.css" rel="stylesheet" />
        <!--<link href="stylesheets/newcss.css" rel="stylesheet" />-->
        <title><%=formDetail.getFormName()%></title>
        <style>
            td {
                padding: 5px 5px 5px 5px;
            }
            input[type="text"],textarea,input[type="date"] {
                border: 2px solid #dadada;
                width: 300px;
                border-radius: 7px;
                height: 35px;
                font-size: 1em;
                font-family: Ubuntu;
                margin-top: 15px;
            }
        </style>
    </head>
    <body style="background-image: url(img/pencil.jpg);">
        <br>
    <center><h2><b><%=formDetail.getFormName()%></b></h2></center>
    <br>
    <form action="SubmitForm" method="POST" enctype="multipart/form-data">
        <%=HTML%>
        <br>
        <center><input type="submit" value="Submit" /></center>
    </form>
</body>
</html>
<%
    }
%>