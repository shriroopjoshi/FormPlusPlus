/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import classes.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dell
 */
public class GSignUp extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    static HttpTransport httpTransport;
    static GoogleAuthorizationCodeFlow authorizationCodeFlow;
    static JsonFactory jsonFactory;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
//        Enumeration<String> attributeNames = request.getParameterNames();
//        System.out.println(attributeNames.hasMoreElements());
//        //request.();
//        System.out.println(attributeNames.nextElement());
//        while(attributeNames.hasMoreElements()) {
//            System.out.println(attributeNames.nextElement());
//            out.println(attributeNames.nextElement() );
//            
//        }
        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
        authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, Constants.CLIENT_ID, Constants.CLIENT_SECRET,
                Arrays.asList(DriveScopes.DRIVE,
                DriveScopes.DRIVE_APPDATA,
                "https://www.googleapis.com/auth/drive",
                "https://spreadsheets.google.com/feeds",
                "https://www.googleapis.com/auth/plus.me",
                "https://www.googleapis.com/auth/userinfo.email"))
                .setAccessType("offline")
                .setApprovalPrompt("auto")
                .build();
        Constants.authorizationCodeFlow = this.authorizationCodeFlow;
        String url = authorizationCodeFlow.newAuthorizationUrl().setRedirectUri(Constants.REDIRECT_URL).build();
        response.sendRedirect(url);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
