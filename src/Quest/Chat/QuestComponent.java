/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Quest.Chat;

/**
 *
 * @author Bailey
 */
public abstract class QuestComponent {
    
    private final String message;
    
    public QuestComponent(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message + this.getProgress();
    }
    
    public abstract String getProgress();
    public abstract boolean condition();
}
