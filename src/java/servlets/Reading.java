/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import classes.Constants;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.introspection.Categories;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.model.atom.Category;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.FormDetailOperations;
import static servlets.GSignUp.httpTransport;

/**
 *
 * @author Akshay Patil
 */
public class Reading extends HttpServlet {

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
        /* TODO output your page here. You may use following sample code. */
        String user_id = (String) request.getSession().getAttribute("uname");
        String accessToken = (String) request.getSession().getAttribute("access_token");
        String formId = request.getParameter("formId");

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                .build()
                .setAccessToken(accessToken);
        Drive drive = new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(Constants.ApplicationName)
                .build();

        String[] split = formId.split("_");
        String folderId = "" + split[0] + "_" + split[2] + " - Responses";

        //String folderId = formId;
        String query = "mimeType='application/vnd.google-apps.spreadsheet' and trashed=false and title='" + folderId + "'";
        List<File> filelist = DownloadResponse.retrieveSpreadsheet(drive, query);
        File spreadsheetResponseFile = null;

        if (filelist.size() > 0) {
            spreadsheetResponseFile = filelist.get(0);
        }

        SpreadsheetService spreadsheetService = new SpreadsheetService(Constants.ApplicationName);
        spreadsheetService.setOAuth2Credentials(new Credential(BearerToken.authorizationHeaderAccessMethod())
                .setFromTokenResponse(new TokenResponse().setAccessToken(
                                (String) request.getSession().getAttribute("access_token"))));
        spreadsheetService.useSsl();
        String feedUrl = "https://spreadsheets.google.com/feeds/worksheets/"
                + spreadsheetResponseFile.getId() + "/private/full";

        try {
            WorksheetFeed worksheetFeed = spreadsheetService.getFeed(new URL(feedUrl), WorksheetFeed.class);

            List<WorksheetEntry> entries = worksheetFeed.getEntries();
            WorksheetEntry workSheet = entries.get(0);
            URL listFeedUrl = workSheet.getListFeedUrl();
            ListFeed listfeed = spreadsheetService.getFeed(listFeedUrl, ListFeed.class);
            //
            ArrayList<ArrayList<String>> tab = new ArrayList<ArrayList<String>>();

            Set<String> tags = null;

            ArrayList<String> att = new ArrayList<>();

            for (ListEntry row : listfeed.getEntries()) {
                // Print the first column's cell value
                tags = row.getCustomElements().getTags();
                System.out.print(row.getTitle().getPlainText() + "\tjjj");
                // Iterate over the remaining columns, and print each cell value
                ArrayList<String> temp = new ArrayList<>();
                for (String tag : row.getCustomElements().getTags()) {
                    System.out.print(row.getCustomElements().getValue(tag) + "\t");

                    temp.add(row.getCustomElements().getValue(tag));

                }
                System.out.println();
                tab.add(temp);

                for (String string : tags) {
                    att.add(string);

                }
            }
            request.getSession().setAttribute("tab", tab);
            request.getSession().setAttribute("att", att);
            response.sendRedirect("graph2.jsp?ind=0");
        } catch (IOException e) {
            Logger.getLogger(Reading.class.getName()).log(Level.SEVERE, null, e);
        } catch (ServiceException ex) {
            Logger.getLogger(Reading.class.getName()).log(Level.SEVERE, null, ex);
        }

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

}
