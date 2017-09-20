package de.alfingo.al_questionnaire.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class SurveyProperties implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("intro_message")
    @Expose
    private String introMessage;
    @SerializedName("end_message")
    @Expose
    private String endMessage;
    @SerializedName("skip_intro")
    @Expose
    private Boolean skipIntro = Boolean.FALSE;
    @SuppressWarnings("FieldCanBeLocal")
    @SerializedName("conditional_linking")
    @Expose
    private Boolean conditionalLinking = false;

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The introMessage
     */
    public String getIntroMessage() {
        return introMessage;
    }

    /**
     * @param introMessage The intro_message
     */
    public void setIntroMessage(String introMessage) {
        this.introMessage = introMessage;
    }

    /**
     * @return The endMessage
     */
    public String getEndMessage() {
        return endMessage;
    }

    /**
     * @param endMessage The end_message
     */
    public void setEndMessage(String endMessage) {
        this.endMessage = endMessage;
    }

    /**
     * @return The skipIntro
     */
    public Boolean getSkipIntro() {
        return skipIntro;
    }

    /**
     * @param skipIntro The skip_intro
     */
    public void setSkipIntro(Boolean skipIntro) {
        this.skipIntro = skipIntro;
    }

    /**
     * gets if conditional linking is on.
     *
     * @return A boolean containing if the conditional linking should be used or not.
     */
    public Boolean getConditionalLinking() {
        return conditionalLinking;
    }

}