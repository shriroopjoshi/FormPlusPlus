/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author dell
 */
public class Login {
    private String username;
    private String refreshToken;
    
    public Login(String username, String refresh_token) {
        this.username = username;
        this.refreshToken = refresh_token;
    }
    
    public String getUsername () {
        return username;
    }
    
    public String getRefreshToken () {
        return refreshToken;
    }
}
