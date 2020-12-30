/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter06;

/**
 *
 * @author reden
 */
public class DialogNode {
        
        private String characterName = "Dodgy Monkey";
        
        private String characterImage = "Interface/Image/Character/DodgyMonkey.png";
        private String characterDialog = "Hello Stranger. My banana plants are under attack by mites. Will you help me get rid of them? I will pay 5 gold for each Ladybug you bring me.";
        
        private String[] dialogOptions = new String[]{"You're in luck, mister. I happen to have a Ladybug, right here.", "Sorry, I don't have any.", "Not now."};

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(String characterImage) {
        this.characterImage = characterImage;
    }

    public String getCharacterDialog() {
        return characterDialog;
    }

    public void setCharacterDialog(String characterDialog) {
        this.characterDialog = characterDialog;
    }

    public String[] getDialogOptions() {
        return dialogOptions;
    }

    public void setDialogOptions(String[] dialogOptions) {
        this.dialogOptions = dialogOptions;
    } 
}
