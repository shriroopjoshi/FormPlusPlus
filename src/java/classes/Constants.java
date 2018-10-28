/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import servlets.GRedirect;

/**
 *
 * @author dell
 */
public class Constants {
    public static String CLIENT_ID = "620058119342-lv91hms8c7gra0rvd9spnaeks01r9mgt.apps.googleusercontent.com";
    public static String CLIENT_SECRET = "WyaJkcUAMTlJ4qRKg1n7gSlL";
    public static String REDIRECT_URL = "http://localhost:8080/FormPlusPlus/GRedirect";
    
    public static String ApplicationName = "NewFormPlusPlus";
    
    public static String Admin_Mail = "gformplusplus@gmail.com";
    public static String Admin_Password = "GroupPersistent";
    
    public static String DriverName = "com.mysql.jdbc.Driver";
    public static String DBUrl = "jdbc:mysql://db4free.net:3306/gformplus";
    public static String username = "gform";
    public static String password = "gformrocks";
//    public static String DriverName = "com.mysql.jdbc.Driver";
//    public static String DBUrl = "jdbc:mysql://mysql39840-formplusplus.jelastic.elastx.net:3306/FormPlusPlus";
//    public static String username = "root";
//    public static String password = "FXEmvm94937";
    
    public static GoogleAuthorizationCodeFlow authorizationCodeFlow;
    
    public static String SPREADSHEET_FEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
    
    public static String getAccessTokenFromRefreshToken(String refreshToken) {
        String accessToken = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://accounts.google.com/o/oauth2/token");
            httpPost.setHeader("User-Agent", "HTTPie/0.6.0");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            List<NameValuePair> params = new ArrayList<>();
            //System.out.println("refreshToken : " + refreshToken);
            params.add(new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            params.add(new BasicNameValuePair("refresh_token", refreshToken));
            params.add(new BasicNameValuePair("client_id", Constants.CLIENT_ID));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse execute = httpClient.execute(httpPost);
            InputStream content = execute.getEntity().getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(content));
            String str = br.readLine();
            while (str != null) {
                //System.out.println(str);
                if (str.contains("\"access_token\"")) {
                    str = str.substring(str.indexOf(":") + 1, str.indexOf(","));
                    str = str.replace("\"", "").trim();
                    //System.out.println(str);
                    accessToken = str;
                    break;
                }
                str = br.readLine();
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GRedirect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GRedirect.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*GoogleCredential.Builder credentialBuilder = new GoogleCredential.Builder()
         .setTransport(SignupServlet.httpTransport).setJsonFactory(new JacksonFactory())
         .setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
         //        credentialBuilder.addRefreshListener(new MyCredentialRefreshListener());

         GoogleCredential credential = credentialBuilder.build();
         credential.setRefreshToken(refreshToken);

         try {
         boolean refreshToken1 = credential.refreshToken();
         if (refreshToken1) {
         accessToken = credential.getAccessToken();
         System.out.println("JAM GAYA");
         }
         } catch (IOException e) {
         e.printStackTrace();
         }*/
        return accessToken;
    }
}
