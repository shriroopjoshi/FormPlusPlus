<%-- 
    Document   : thanks
    Created on : Mar 24, 2015, 11:38:22 AM
    Author     : Shrinivas
--%>

<%@page import="beans.FormDetail"%>
<%@page import="classes.Email"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thank you!!</title>
    </head>
    <body>
        <%
            FormDetail fd = (FormDetail) session.getAttribute("formDetail");
            String sendTo = fd.getFormID().split("_")[1];
            new Email().sendMail(sendTo,
                    null,
                    "New submission for form '" + fd.getFormName() + "'",
                    "A new submission has been recorded for your form '" + fd.getFormName() + "'.<br>"
                    + "View the responses on your Google Drive."
            );
        %>
        <h1>
            Your responses have been submitted successfully.
        </h1>
        <h6>
            This form is brought to you by <a href="index.jsp">FormPlusPlus</a>.
        </h6>
    </body>
</html>
