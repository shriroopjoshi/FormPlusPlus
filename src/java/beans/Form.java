/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author dell
 */
public class Form {
    private String userID;
    private String formID;

    public Form(String uname, String form_id) {
        this.userID = uname;
        this.formID = form_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    @Override
    public String toString() {
        return "Form{" + "uname=" + userID + ", form_id=" + formID + '}';
    }
    
}
