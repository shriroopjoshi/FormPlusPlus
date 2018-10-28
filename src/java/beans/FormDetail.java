/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author dell
 */
public class FormDetail {

    private String formID;
    private String formName;
    private String folderID;
    private String dateCreated;
    private String publicLink;
    private String formLink;

    public FormDetail(String formID, String formName, String folderID, String dateCreated, String publicLink, String formLink) {
        this.formID = formID;
        this.formName = formName;
        this.folderID = folderID;
        this.dateCreated = dateCreated;
        this.publicLink = publicLink;
        this.formLink = formLink;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFolderID() {
        return folderID;
    }

    public void setFolderID(String folderID) {
        this.folderID = folderID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPublicLink() {
        return publicLink;
    }

    public void setPublicLink(String link) {
        this.publicLink = link;
    }

    public String getFormLink() {
        return formLink;
    }

    public void setFormLink(String formLink) {
        this.formLink = formLink;
    }

    @Override
    public String toString() {
        return "FormDetail{" + "formID=" + formID + ", formName=" + formName + ", folderID=" + folderID + ", dateCreated=" + dateCreated + '}';
    }

}
