/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.ArrayList;

/**
 *
 * @author Shrinivas
 */
public class Question {

    public String question, helpText, Type, req, options;
    public String list[];
    public ArrayList<String> optionList = new ArrayList<>();

    public void makeOptionList() {
        list = options.split("@@@");
        for (String string : list) {
            optionList.add(string);
        }
    }
}
