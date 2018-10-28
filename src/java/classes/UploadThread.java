/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import operations.FormDetailOperations;

/**
 *
 * @author dell
 */
public class UploadThread extends Thread {

    String link;
    String formID;

    public UploadThread(String formID, String link) {
        this.formID = formID;
        this.link = link;
    }
    
    @Override
    public void run() {
        FormDetailOperations.uploadPublicLinkForForm(formID, link);
    }
    
}
