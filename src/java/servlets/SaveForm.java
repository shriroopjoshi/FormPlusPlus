/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import beans.Form;
import beans.FormDetail;
import classes.Constants;
import classes.Question;
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
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class SaveForm extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //HttpServletRequest r;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //AKSHAY PART
        String props = request.getParameter("send");
        String Delques = request.getParameter("Delques");
        String dels[] = Delques.split("-");
        String Ques[] = props.split("<br>");
        String FormName = request.getParameter("FormName");
        ArrayList<Integer> deletedQ = new ArrayList<Integer>();
        String fileToWrite = "";
        for (int i = 1; i < dels.length; i++) {
            deletedQ.add(Integer.parseInt(dels[i]));
        }

        Collections.sort(deletedQ);
        out.println(props);
        out.println(Delques);
        for (Integer integer : deletedQ) {
            out.println(integer + "<br>");
        }
        for (int i = 0; i < Ques.length; i++) {
            if (deletedQ.contains(i + 1)) {
                //deletedQ.remove(0);
            } else {
                fileToWrite = fileToWrite + Ques[i] + "<br>";
            }

        }
        out.println(fileToWrite);

        //AKSHAY PART
        try {
            System.out.println((String) request.getSession().getAttribute("folder_id"));
            //        String parameter = request.getParameter("tb_1");
            //        String formName = request.getParameter("form_name");
            //        Enumeration<String> parameterNames = request.getParameterNames();
            //        System.out.println("Parameters:");
            //        while(parameterNames.hasMoreElements()) {
            //            //System.out.println(parameterNames.nextElement());
            //            String paramName = parameterNames.nextElement();
            //            String[] values = request.getParameterValues(paramName);
            //            if(values.length == 1) {
            //                System.out.println("Values for parameter " + paramName);
            //                String param = values[0];
            //                if(param.length() == 0) {
            //                    System.out.println("---NO VALUE FOUND");
            //                } else {
            //                    System.out.println("---" + param);
            //                }
            //            } else {
            //                System.out.println("Values for parameter " + paramName);
            //                for (String param : values) {
            //                    System.out.println("---" + param);
            //                }
            //            }
            //        }
            /*
             * Since above code is not working, I'm goind to hardcode values for now.
             * SOmeone need to help me out here.
             */
            String formName = FormName;
            String parentFolderID = (String) request.getSession().getAttribute("folder_id");
            String accessToken = (String) request.getSession().getAttribute("access_token");
            String username = (String) request.getSession().getAttribute("uname");
            String childFolderID = "";
            //System.out.println("\nSaveForm:\nFolderID: " + parentFolderID
            //        + "\nAccess Token: " + accessToken + "\nForm Name: " + formName);

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
            List<File> result = new ArrayList<>();
            //System.out.println("Before");
            //System.out.println("After");
            Files.List newRequest = drive.files().list()
                    .setQ("title contains '" + formName + "_' and mimeType = 'application/vnd.google-apps.folder' "
                            + "and '" + parentFolderID + "' in parents");
            do {
                try {
                    FileList execute = newRequest.execute();
                    result.addAll(execute.getItems());
                    newRequest.setPageToken(execute.getNextPageToken());
                } catch (IOException ex) {
                    System.err.println("ERROR OCCURED WHILE RETRIVING FILE LIST");
                    newRequest.setPageToken(null);
                    Logger.getLogger(SaveForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (newRequest.getPageToken() != null && newRequest.getPageToken().length() > 0);
            System.out.println("SIze of list: " + result.size());
            /*
             * Create contents for file using parameters recieved
             * Same problem - someone need to help me out.
             */
            String contents = fileToWrite;
            /*
             * Snippet for creating a FORM file inside a folder
             */
            String formId = formName + "_" + username + "_form" + String.format("%05d", result.size());
            String folderName = formName + "_form" + String.format("%05d", result.size());
            childFolderID = createFolderForForm(parentFolderID, drive, folderName);
            File newFormFile = new File();
            newFormFile.setTitle(folderName);
            newFormFile.setMimeType("text/plain");
            newFormFile.setDescription("A form created using FormPlusPlus app. Please do not change the contents");
            newFormFile.setParents(Arrays.asList(new ParentReference().setId(childFolderID)));
            InputStreamContent inputStreamContent = new InputStreamContent("text/plain", new ByteArrayInputStream(contents.getBytes()));
            File formFile = drive.files().insert(newFormFile, inputStreamContent).execute();

            Permission pp = new Permission();
            pp.setType("anyone");
            pp.setRole("reader");
            Permission exep = drive.permissions().insert(formFile.getId(), pp).execute();

            String formLink = formFile.getWebContentLink();

            System.out.println("FORM LINK : " + formLink);

            System.out.println(formFile.getAlternateLink());
            System.out.println(formFile.getDownloadUrl());
            System.out.println(formFile.getEmbedLink());
            System.out.println(formFile.getSelfLink());
            System.out.println(formFile.getWebContentLink());
            System.out.println(formFile.getWebViewLink());
            System.out.println(formFile.getId());

            /*
             * Snippet for creating a SPREADSHEET for recording responses.
             */
            File body = new File();
            body.setTitle(folderName + " - Responses");
            body.setDescription("A new spreadsheet");
            body.setMimeType("application/vnd.google-apps.spreadsheet");
            body.setParents(Arrays.asList(new ParentReference().setId(childFolderID)));
            File spreadsheetFile = drive.files().insert(body).execute();

            SpreadsheetService spreadsheetService = new SpreadsheetService(Constants.ApplicationName);
            spreadsheetService.setOAuth2Credentials(new Credential(BearerToken.authorizationHeaderAccessMethod())
                    .setFromTokenResponse(new TokenResponse().setAccessToken(
                                    (String) request.getSession().getAttribute("access_token"))));
            spreadsheetService.useSsl();
            String feedUrl = "https://spreadsheets.google.com/feeds/worksheets/"
                    + spreadsheetFile.getId() + "/private/full";
            WorksheetFeed worksheetFeed = spreadsheetService.getFeed(new URL(feedUrl), WorksheetFeed.class);
            //            SpreadsheetFeed feed = spreadsheetService.getFeed(
            //                    new URL(Constants.SPREADSHEET_FEED_URL), SpreadsheetFeed.class);
            //            List<SpreadsheetEntry> spreadsheets = feed.getEntries();
            //            if (spreadsheets.isEmpty()) {
            //                return;
            //            }
            //            SpreadsheetEntry spreadsheet = null;
            //            System.out.println("SIZE: " + spreadsheets.size());
            //            for (SpreadsheetEntry spreadsheetEntry : spreadsheets) {
            //                if (spreadsheetEntry.getId().equalsIgnoreCase(spreadsheetFile.getId())) {
            //                    spreadsheet = spreadsheetEntry;
            //                }
            //            }
            //            if (spreadsheet == null) {
            //                System.out.println("NO SPREADSHEET FOUND");
            //                return;
            //            }
            //            SpreadsheetEntry sheet = new SpreadsheetEntry();
            //            Source ss = new Source();
            //            ss.setId(spreadsheetFile.getId());
            //            sheet.setSource(ss);
            //System.out.println("Title of SHEET: " + spreadsheet.getId());
            List<WorksheetEntry> entries = worksheetFeed.getEntries();
            WorksheetEntry workSheet = entries.get(0);
            //WorksheetEntry workSheet = new WorksheetEntry();
            workSheet.setTitle(new PlainTextConstruct("Responses"));
            workSheet.setColCount(Ques.length);
            workSheet.update();

            URL listFeedUrl = workSheet.getListFeedUrl();
            ListFeed listfeed = spreadsheetService.getFeed(listFeedUrl, ListFeed.class);

            CellQuery cellQuery = new CellQuery(workSheet.getCellFeedUrl());
            CellFeed cellFeed = spreadsheetService.getFeed(cellQuery, CellFeed.class);

//            CellEntry cellEntry = new CellEntry(1, 1, "firstname");
//            cellFeed.insert(cellEntry);
//            cellEntry = new CellEntry(1, 2, "lastname");
//            cellFeed.insert(cellEntry);
//            cellEntry = new CellEntry(1, 3, "age");
//            cellFeed.insert(cellEntry);
//            cellEntry = new CellEntry(1, 4, "height");
//            cellFeed.insert(cellEntry);
            String[] split = fileToWrite.trim().split("<br>");
            System.out.println(split.length);
            for (int i = 0; i < split.length; i++) {
                String question = split[i].split(";;;")[0].split(":::")[1].trim().replace(" ", "_").toLowerCase();
                CellEntry ce = new CellEntry(1, i + 1, question);
                cellFeed.insert(ce);
//                ce = new CellEntry(2, i + 1, question + "XXX");
//                cellFeed.insert(ce);
            }

//            ListEntry listEntry = new ListEntry();
//            listEntry.getCustomElements().setValueLocal("firstname", "Joe");
//            listEntry.getCustomElements().setValueLocal("lastname", "Smith");
//            listEntry.getCustomElements().setValueLocal("age", "26");
//            listEntry.getCustomElements().setValueLocal("height", "176");
//
//            listEntry = spreadsheetService.insert(listFeedUrl, listEntry);
            Permission p = new Permission();
            p.setType("anyone");
            p.setRole("reader");
            Permission execute1 = drive.permissions().insert(spreadsheetFile.getId(), p).execute();
            out.println("<br>Spreadsheet created: " + spreadsheetFile);
            String webContentLink = spreadsheetFile.getWebContentLink();

            System.out.println("NEW: " + webContentLink);

            /*
             * Update Database for form creation.
             */
            FormOperations.insert(new Form((String) request.getSession().getAttribute("uname"), formId));
            FormDetailOperations.insert(new FormDetail(formId, formName, childFolderID, new Date().toString(), formLink, null));
            response.sendRedirect("mainPage.jsp");
        } catch (NullPointerException ex) {
            Logger.getLogger(SaveForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception exe) {
            Logger.getLogger(SaveForm.class.getName()).log(Level.SEVERE, null, exe);
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

    private String createFolderForForm(String parentFolderID, Drive drive, String formName) {
        String childFolderID = "";
        try {
            File body = new File();
            body.setTitle(formName);
            body.setDescription("A folder generated from FormPlusPlus");
            body.setMimeType("application/vnd.google-apps.folder");
            body.setParents(Arrays.asList(new ParentReference().setId(parentFolderID)));
            File execute = drive.files().insert(body).execute();
            childFolderID = execute.getId();
        } catch (IOException ex) {
            Logger.getLogger(SaveForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("childFolderID: " + childFolderID);
        return childFolderID;
    }

    public static String makeHTML(String formFile) {
        //String preHTML = "<html><head><title>TITLE</title></head><body><link rel='stylesheet' href='E:\\formcss.css'>";
        //String postHTML = "</body></html>";

        int index = 0;
        String HTML = "<div id='bigdiv'>";
        String htmlTemp = "";
        String props[] = formFile.trim().split("<br>");
        System.out.println("props : " + props.length);
        while (index < props.length) {
            String string = props[index];
            index++;
            String diffAttribes[] = string.split(";;;");

            Question temp = new Question();
            System.out.println("0 : " + diffAttribes[0]);
            temp.question = diffAttribes[0].split(":::")[1];
            temp.helpText = diffAttribes[1].split(":::")[1];
            temp.Type = diffAttribes[2].split(":::")[1];
            temp.options = diffAttribes[3].split(":::")[1];
            temp.req = diffAttribes[4].split(":::")[1];
            temp.makeOptionList();

            String required = "";
            if ("1".equals(temp.req)) {
                required = "required";
            } else {
                required = "";
            }
            htmlTemp += "<br>";
            htmlTemp += "<div class='question'>";
            htmlTemp += "<div class='questionHeading'>" + temp.question + "</div>";
            htmlTemp += "<div class='helpText'>" + temp.helpText + "</div>";
            switch (temp.Type) {
                case "TEXT":
                    htmlTemp += "<div class='inputType'>" + "<input type='text' name='" + index + "_tb' id='' " + required + ">" + "</div>";
                    break;
                case "BIG TEXT":
                    htmlTemp += "<div class='inputType'>" + "<textarea rows='5'  cols=\"30\" name='" + index + "_btb' id='' " + required + "></textarea>" + "</div>";
                    break;
                case "DATE":
                    htmlTemp += "<div class='inputType'>" + "<input type='date' name='" + index + "_dt' id='' " + required + ">" + "</div>";
                    break;
                case "FILE":
                    htmlTemp += "<div class='inputType'>" + "<input type='file' name='" + index + "_fl' id='' " + required + ">" + "</div>";
                    break;
                case "RADIO BUTTON":
                    htmlTemp += "<table><tbody>";
                    for (int x = 0; x < temp.optionList.size(); x++) {
                        htmlTemp += "<tr>\n"
                                + "<td><input type='radio' name='" + index + "rb' id='' value='" + temp.optionList.get(x) + "'></td>\n"
                                + "<td>" + temp.optionList.get(x) + "</td>\n"
                                + "</tr>";
                        //htmlTemp += temp.optionList.get(x) + ":<div class='inputType'>" + "<input type='radio' name='" + index + "rb' id='' value='" + temp.optionList.get(x) + "'>" + "</div>";
                    }
                    htmlTemp += "</tbody></table>";
                    break;
                case "CHECK BOX":
                    htmlTemp += "<table><tbody>";
                    for (int x = 0; x < temp.optionList.size(); x++) {
                        htmlTemp += "<tr>\n"
                                + "<td><input type='checkbox' name='" + index + "cb' id='' value='" + temp.optionList.get(x) + "'></td>\n"
                                + "<td>" + temp.optionList.get(x) + "</td>\n"
                                + "</tr>";
                        //htmlTemp += temp.optionList.get(x) + ":<div class='inputType'>" + "<input type='checkbox' name='" + index + "cb' id='' value='" + temp.optionList.get(x) + "'>" + "</div>";
                    }
                    htmlTemp += "</tbody></table>";
                    break;
                case "DROP-DOWN LIST":
                    htmlTemp += "<div class='inputType'>" + "<select name='" + index + "_dd' id=''>";
                    for (int x = 0; x < temp.optionList.size(); x++) {
                        htmlTemp += "<option>" + temp.optionList.get(x) + "</option>";
                    }
                    htmlTemp += "</select>" + "</div>";
                    break;
                default:
                    break;
            }
            htmlTemp += "</div>";
        }

        HTML += htmlTemp;
        HTML += "</div>";

        return HTML;

    }

}
