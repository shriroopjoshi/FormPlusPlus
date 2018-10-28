/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.FormDetail;
import classes.Constants;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import operations.LoginOperations;

/**
 *
 * @author dell
 */
@MultipartConfig
public class SubmitForm extends HttpServlet {

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
        try {
//            SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");
//            service.setOAuth2Credentials(new Credential(BearerToken.authorizationHeaderAccessMethod())
//                    .setFromTokenResponse(new TokenResponse().setAccessToken(
//                    (String)request.getSession().getAttribute("access_token"))));
//            URL SPREADSHEET_FEED_URL = new URL(Constants.SPREADSHEET_FEED_URL);
//            SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
//            List<SpreadsheetEntry> sheets = feed.getEntries();
//            out.println("Enlisting spreadsheets");
//            for (SpreadsheetEntry sheet : sheets) {
//                out.println(sheet.getTitle().getPlainText());
//            }

            Enumeration<String> parameterNames = request.getParameterNames();

            System.out.println("parameters size : " + request.getParameterMap().size());

            FormDetail fd = (FormDetail) request.getSession().getAttribute("formDetail");
            String rawForm = (String) request.getSession().getAttribute("rawForm");
            String username = fd.getFormID().split("_")[1];
            String refreshToken = LoginOperations.getRefreshToken(username);
            String accessToken = Constants.getAccessTokenFromRefreshToken(refreshToken);

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
            //title contains '" + formName + "_' and
            List<File> retrieveSpreadsheet = DownloadResponse.retrieveSpreadsheet(drive, "mimeType = 'application/vnd.google-apps.spreadsheet' "
                    + "and '" + fd.getFolderID() + "' in parents");

            System.out.println(retrieveSpreadsheet.size());

            File spreadsheetFile = retrieveSpreadsheet.get(0);

            System.out.println(spreadsheetFile.getTitle());

            SpreadsheetService spreadsheetService = new SpreadsheetService(Constants.ApplicationName);
            spreadsheetService.setOAuth2Credentials(new Credential(BearerToken.authorizationHeaderAccessMethod())
                    .setFromTokenResponse(new TokenResponse().setAccessToken(accessToken)));
            spreadsheetService.useSsl();
            String feedUrl = "https://spreadsheets.google.com/feeds/worksheets/"
                    + spreadsheetFile.getId() + "/private/full";
            WorksheetFeed worksheetFeed = spreadsheetService.getFeed(new URL(feedUrl), WorksheetFeed.class);
            List<WorksheetEntry> entries = worksheetFeed.getEntries();
            WorksheetEntry workSheet = entries.get(0);

            URL listFeedUrl = workSheet.getListFeedUrl();
            ListFeed listfeed = spreadsheetService.getFeed(listFeedUrl, ListFeed.class);

            System.out.println("A" + listfeed.getEntries().size());
            System.out.println("B" + listfeed.getTotalResults());

            CellQuery cellQuery = new CellQuery(workSheet.getCellFeedUrl());
            CellFeed cellFeed = spreadsheetService.getFeed(cellQuery, CellFeed.class);

            int xx = 0;
            String[] split = rawForm.split("<br>");

            ArrayList<String> keys = new ArrayList<>();

            for (int i = 0; i < split.length; i++) {
                Part part = request.getPart((i + 1) + "_fl");
                if (part != null) {
                    keys.add((i + 1) + "_fl");
                }
            }

            ListEntry listEntry = new ListEntry();
            System.out.println("ROWS : " + listfeed.getEntries().size());
            System.out.println("ROWS : " + listfeed.getTotalResults());

            while (parameterNames.hasMoreElements()) {
                keys.add(parameterNames.nextElement());
            }

            Collections.sort(keys, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    if (Integer.parseInt(o1.split("_")[0]) > Integer.parseInt(o2.split("_")[0])) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

            for (String key : keys) {
                System.out.println("SORT : " + key);
            }

            for (String key : keys) {

                String nextElement = key;
                System.out.println("nextElement : " + nextElement);
                String type = nextElement.split("_")[1];
                String parameter = "";
                switch (type) {
                    case "tb":
                    case "btb":
                    case "dt":
                    case "rb":
                    case "dd":
                        parameter = request.getParameter(nextElement);
                        break;
                    case "fl":
                        Part part = request.getPart(nextElement);
                        InputStream inputStream = part.getInputStream();
                        byte[] data = new byte[(int) part.getSize()];
                        inputStream.read(data);
                        ByteArrayInputStream queBais = new ByteArrayInputStream(data);
                        File queFile = new File();
                        System.out.println(part.getSubmittedFileName());
                        System.out.println(part.getContentType());
                        queFile.setTitle(part.getSubmittedFileName());
                        queFile.setMimeType("text/plain");
                        queFile.setParents(Arrays.asList(new ParentReference().setId(fd.getFolderID())));
                        InputStreamContent queInputStreamContent = new InputStreamContent("text/plain", queBais);
                        File queUploaded = drive.files().insert(queFile, queInputStreamContent).execute();
                        Permission pp = new Permission();
                        pp.setType("anyone");
                        pp.setRole("reader");
                        drive.permissions().insert(queUploaded.getId(), pp).execute();
                        String queLink = queUploaded.getWebContentLink();
                        System.out.println(queLink);
                        parameter = "=HYPERLINK(\"" + queLink + "\", \"" + part.getSubmittedFileName() + "\")";
                        break;
                    case "cb":
                        String[] parameterValues = request.getParameterValues(nextElement);
                        for (int i = 0; i < parameterValues.length; i++) {
                            if (i == parameterValues.length - 1) {
                                parameter = parameter + parameterValues[i];
                            } else {
                                parameter = parameter + parameterValues[i] + "/";
                            }
                        }
                        break;
                    default:
                        break;
                }
                System.out.println("parameter : " + parameter);

                CellEntry ce = new CellEntry(listfeed.getTotalResults() + 2, Integer.parseInt(nextElement.split("_")[0]), parameter);
                cellFeed.insert(ce);

                //listEntry.getCustomElements().setValueLocal(split[xx].split(";;;")[0].split(":::")[1].trim().replace(" ", "_").toLowerCase(), parameter);
                //System.out.println("tag : " + split[xx].split(";;;")[0].split(":::")[1].trim().replace(" ", "_").toLowerCase());
                xx++;
            }
            //listEntry = spreadsheetService.insert(listFeedUrl, listEntry);

            System.out.println(listfeed.getEntries().size());

            response.sendRedirect("thanks.jsp");

        } catch (Exception ex) {
            Logger.getLogger(SubmitForm.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Exception occured");
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
