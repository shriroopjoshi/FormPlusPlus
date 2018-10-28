/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import classes.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.LoginOperations;

/**
 *
 * @author dell
 */
public class CheckServlet extends HttpServlet {

    private String username, displayName, accessToken;

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
        username = (String) request.getSession().getAttribute("uname");
        displayName = (String) request.getSession().getAttribute("dname");
        accessToken = (String) request.getSession().getAttribute("access_token");

        String refreshToken = LoginOperations.getRefreshToken(username);
        accessToken = Constants.getAccessTokenFromRefreshToken(refreshToken);

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(GSignUp.httpTransport)
                .setJsonFactory(GSignUp.jsonFactory)
                .setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                .build()
                .setAccessToken(accessToken);
        Drive drive = new Drive.Builder(GSignUp.httpTransport, GSignUp.jsonFactory, credential)
                .setApplicationName(Constants.ApplicationName)
                .build();
        String folderID = createFolderIfNotPresent(drive);
        request.getSession().setAttribute("uname", username);
        request.getSession().setAttribute("dname", displayName);
        request.getSession().setAttribute("access_token", accessToken);
        request.getSession().setAttribute("folder_id", folderID);
        response.sendRedirect("mainPage.jsp");
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

    private String createFolderIfNotPresent(Drive drive) {
        try {
            Drive.Files.List fileList = drive
                    .files()
                    .list()
                    .setQ("mimeType='application/vnd.google-apps.folder' and trashed=false and title='FormPlusPlus'");
            FileList files = fileList.execute();
            List<File> items = files.getItems();
            if (items.size() > 0) {
                String id = items.get(0).getId();
                return id;
            }
            File f = new File();
            f.setTitle("FormPlusPlus");
            f.setMimeType("application/vnd.google-apps.folder");
            File ff = drive.files().insert(f).execute();
            return ff.getId();
        } catch (IOException ex) {
            Logger.getLogger(CheckServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
