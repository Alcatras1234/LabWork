package org.example.serverjava.entities;

import jakarta.persistence.*;


public class InputData {
    private String sourceLanguage;
    private String targetLanguage;
    private String text;
    public InputData() {
    }
    public InputData(String sourceLanguage, String targetLanguage, String text) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.text = text;
    }
    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
