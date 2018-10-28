/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Login;
import classes.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.LoginOperations;

/**
 *
 * @author dell
 */
public class GRedirect extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String parameter = request.getParameter("code");
        GoogleTokenResponse tokenResponse = GSignUp.authorizationCodeFlow
                .newTokenRequest(parameter)
                .setRedirectUri(Constants.REDIRECT_URL)
                .execute();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(GSignUp.httpTransport)
                .setJsonFactory(GSignUp.jsonFactory)
                .setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                .build()
                .setFromTokenResponse(tokenResponse);
        Drive drive = new Drive.Builder(GSignUp.httpTransport, GSignUp.jsonFactory, credential)
                .setApplicationName(Constants.ApplicationName)
                .build();
        About about = drive.about().get().execute();
        String displayName = about.getUser().getDisplayName();
        String username = about.getUser().getEmailAddress();
        String refreshToken = credential.getRefreshToken();
        if (LoginOperations.isUserPresent(new Login(username, refreshToken))) {
            String OldRefreshToken = LoginOperations.getRefreshToken(username);
            System.out.println("OLD REFRESH TOKEN: " + OldRefreshToken);
            System.out.println("New Refresh Token: " + refreshToken);
            if (refreshToken != null) {
                LoginOperations.updateRefreshToken(new Login(username, refreshToken));
            }
            String accessToken = Constants.getAccessTokenFromRefreshToken(refreshToken);
            request.getSession().setAttribute("uname", username);
            request.getSession().setAttribute("dname", displayName);
            request.getSession().setAttribute("access_token", accessToken);
            response.sendRedirect("CheckServlet");
        } else {
            LoginOperations.Insert(new Login(username, refreshToken));
            String accessToken = Constants.getAccessTokenFromRefreshToken(refreshToken);
            request.getSession().setAttribute("uname", username);
            request.getSession().setAttribute("dname", displayName);
            request.getSession().setAttribute("access_token", accessToken);
            response.sendRedirect("CheckServlet");
        }
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
