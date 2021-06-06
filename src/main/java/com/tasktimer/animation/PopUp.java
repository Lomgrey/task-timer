package com.tasktimer.animation;

public class PopUp {
    String text;
    PopUpType popUpType;

    public PopUp setText(String text) {
        this.text = text;
        return this;
    }

    public PopUp setPopUpType(PopUpType popUpType) {
        this.popUpType = popUpType;
        return this;
    }

}
