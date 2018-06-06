package com.telappoint.apptdesk.common.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class IvrLang extends BaseResponse {

    private Integer id;
    private String langCode;
    private String voiceName;
    private int ivrDtmfInput;
    private String ivrVoiceInput;
    private String ttsLang;
    private String asrLang;
    private String language;
    private String langLink;
    private char defaultLang;
    private int placement;

    public IvrLang() {
    }

    public IvrLang(boolean status) {
        setStatus(status);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public int getIvrDtmfInput() {
        return ivrDtmfInput;
    }

    public void setIvrDtmfInput(int ivrDtmfInput) {
        this.ivrDtmfInput = ivrDtmfInput;
    }

    public String getIvrVoiceInput() {
        return ivrVoiceInput;
    }

    public void setIvrVoiceInput(String ivrVoiceInput) {
        this.ivrVoiceInput = ivrVoiceInput;
    }

    public String getTtsLang() {
        return ttsLang;
    }

    public void setTtsLang(String ttsLang) {
        this.ttsLang = ttsLang;
    }

    public String getAsrLang() {
        return asrLang;
    }

    public void setAsrLang(String asrLang) {
        this.asrLang = asrLang;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLangLink() {
        return langLink;
    }

    public void setLangLink(String langLink) {
        this.langLink = langLink;
    }

    public char getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(char defaultLang) {
        this.defaultLang = defaultLang;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }
}
