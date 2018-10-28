/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Form;
import classes.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.FormDetailOperations;
import operations.FormOperations;

/**
 *
 * @author dell
 */
public class DeleteForm extends HttpServlet {

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
        String username = (String) request.getSession().getAttribute("uname");
        String formID = request.getParameter("fid");
        String delRes = request.getParameter("delres");
        String accessToken = (String) request.getSession().getAttribute("access_token");
        PrintWriter out = response.getWriter();
        String[] split = formID.split("_");
        String formName = split[0] + "_" + split[2];
        try {
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(GSignUp.httpTransport)
                    .setJsonFactory(GSignUp.jsonFactory)
                    .setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                    .build()
                    .setAccessToken(accessToken);
            Drive drive = new Drive.Builder(GSignUp.httpTransport, GSignUp.jsonFactory, credential)
                    .setApplicationName(Constants.ApplicationName)
                    .build();
            String folderID = FormDetailOperations.getFolderID(formID);
            System.out.println(folderID);
//            if (delRes.equalsIgnoreCase("true")) {
                drive.files().delete(folderID).execute();
//            } else {
//                String fileID = null;
//                Drive.Files.List setQ = drive
//                        .files()
//                        .list()
//                        .setQ("mimeType='text/plain' and trashed=false and " + folderID + "in parents");
//                FileList files = setQ.execute();
//                System.out.println("files.size: " + files.size());
//                List<File> items = files.getItems();
//                System.out.println("items.size: " + items.size());
//                if (items.size() > 0) {
//                    fileID = items.get(0).getId();
//                    System.out.println("fileID: " + fileID);
//                }
//                drive.files().delete(fileID);
//            }
            FormDetailOperations.deleteFormEntry(formID);
            FormOperations.deleteEntry(new Form(username, formID));
            out.println("Deleted");
        } catch (SQLException ex) {
            out.println("Error occured while deleting");
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
