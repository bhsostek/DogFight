/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptingEngine.exceptions;

/**
 *
 * @author Bailey
 */
public class WindowNotFoundException extends Exception{
    public WindowNotFoundException(){
        System.err.println("Window was not found.");
    }
}
