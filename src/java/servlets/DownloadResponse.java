/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import classes.Constants;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dell
 */
public class DownloadResponse extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String accessToken = (String) request.getSession().getAttribute("access_token");
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(GSignUp.httpTransport)
                .setJsonFactory(GSignUp.jsonFactory)
                .setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                .build()
                .setAccessToken(accessToken);
        Drive drive = new Drive.Builder(GSignUp.httpTransport, GSignUp.jsonFactory, credential)
                .setApplicationName(Constants.ApplicationName)
                .build();
        String folderID = request.getParameter("fid");
        //String[] split = temp.split("_");
        //String folderID = split[0] + "_" + split[2];
        String query = "mimeType='application/vnd.google-apps.spreadsheet' and trashed=false and '" + folderID + "' in parents";
        //        Drive.Files.List list = drive
        //                .files()
        //                .list()
        //                .setQ(query);
        //        FileList filelist = list.execute();
        List<File> filelist = retrieveSpreadsheet(drive, query);
        System.out.println("NUmber of Items: " + filelist.size());
        //List<File> items = filelist.getItems();
        if (filelist.size() > 0) {
            File spreadsheetResponseFile = filelist.get(0);
            String downloadLink = spreadsheetResponseFile.getExportLinks().get("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            System.out.println(downloadLink);
            out.println(downloadLink);
        } else {
            out.println("responseFileNotFound.html");
        }
        //.execute();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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

    public static List<File> retrieveSpreadsheet(Drive service, String query) throws IOException {
        List<File> result = new ArrayList<>();
        System.out.println(query);
        Files.List request = service.files().list().setQ(query);
        do {
            try {
                FileList files = request.execute();
                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null
                && request.getPageToken().length() > 0);
        return result;
    }
}
