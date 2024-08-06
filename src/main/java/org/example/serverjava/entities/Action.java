package org.example.serverjava.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "input_text")
    private String input_text;

    @Column(name = "output_text")
    private String output_text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getInputText() {
        return input_text;
    }

    public void setInputText(String inputText) {
        this.input_text = inputText;
    }

    public String getOutputText() {
        return output_text;
    }

    public void setOutputText(String outputText) {
        this.output_text = outputText;
    }

}
