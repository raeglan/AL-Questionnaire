package com.androidadvance.androidsurvey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.io.File;

/**
 * The class used to personalize and initialize a survey.
 *
 * @author Rafael Miranda
 * @version 0.1
 * @since 24.08.2017
 */
public class Survey {

    /**
     * The survey preferences containing colors, texts and maybe much more.
     */
    static final String KEY_SURVEY_PREFERENCES = "de.alfingo.survey.preferences";

    static final String KEY_BUTTON_BACKGROUND_COLOR_INT = "button_background_color",
            KEY_BUTTON_TEXT_COLOR_INT = "button_text_color",
            KEY_FINISH_TEXT_RES = "finish_res",
            KEY_CONTINUE_TEXT_RES = "continue_res",
            KEY_CLOSE_TEXT_RES = "close_res",
            KEY_START_TEXT_RES = "start_res";

    /**
     * The Json containing all the questionnaire information.
     */
    private final String mJsonString;

    /**
     * The color for the Continue, finish and start button.
     */
    private @ColorInt
    int mButtonBackgroundColor;

    /**
     * The color for the Continue, finish and start button text.
     */
    private @ColorInt
    int mButtonTextColor;

    /**
     * Resources which should be used instead of the standard ones.
     */
    private @StringRes
    int mContinueRes, mFinishRes, mStartRes, mCloseRes;

    /**
     * Initializes the survey object, you can just launch it as is using the launchSurvey method or
     * change the color and other aspects from the survey.
     * <br><br> After launching or selecting save, all the given specifications will be saved on
     * device in form of a shared preferences, you can use this class to save the "look-and-feel"
     * at the beginning and only use the survey somewhere later.
     *
     * @param jsonString a string containing all the questions that should be displayed or
     *                   {@code null} if all you want to do is saving the configuration
     *                   todo: Link document of JSON format.
     */
    public Survey(@Nullable String jsonString) {
        mJsonString = jsonString;
    }

    /**
     * Sets the button background and text color. If the background is left null, then it will
     * continue being green and making the text color null will leave it white.
     *
     * @param backgroundColor the background color of the button
     * @param textColor       the displayed text color of the button
     * @return the survey object to continue editing
     */
    public Survey setButtonColor(@Nullable Color backgroundColor, @Nullable Color textColor) {
        // todo: change color to ints.
        mButtonBackgroundColor = backgroundColor;
        mButtonTextColor = textColor;
        return this;
    }

    /**
     * Set which text resource should be displayed instead of Continue, this is important as the
     * text is not localized to any language.
     * <br><br> If the resource ID is not changed it will continue using the
     * following:
     * <ul>
     * <li>xcontinue</li>
     * <li>finish</li>
     * <li>start</li>
     * <li>close</li>
     * </ul>
     *
     * @param continueRes the resource ID for the "Continue" replacement.
     * @return the object for continued editing.
     */
    public Survey setContinueText(@StringRes int continueRes) {
        mContinueRes = continueRes;
        return this;
    }

    /**
     * Set which text resource should be displayed instead of Finish, this is important as the
     * text is not localized to any language.
     * <br><br> If the resource ID is not changed it will continue using the
     * following:
     * <ul>
     * <li>xcontinue</li>
     * <li>finish</li>
     * <li>start</li>
     * <li>close</li>
     * </ul>
     *
     * @param finishRes the resource ID for the "Finish" replacement.
     * @return the object for continued editing.
     */
    public Survey setFinishText(@StringRes int finishRes) {
        mFinishRes = finishRes;
        return this;
    }

    /**
     * Set which text resource should be displayed instead of Close, this is important as the
     * text is not localized to any language.
     * <br><br> If the resource ID is not changed it will continue using the
     * following:
     * <ul>
     * <li>xcontinue</li>
     * <li>finish</li>
     * <li>start</li>
     * <li>close</li>
     * </ul>
     *
     * @param closeRes the resource ID for the "Close" replacement.
     * @return the object for continued editing.
     */
    public Survey setCloseText(@StringRes int closeRes) {
        mCloseRes = closeRes;
        return this;
    }

    /**
     * Set which text resource should be displayed instead of Start, this is important as the
     * text is not localized to any language.
     * <br><br> If the resource ID is not changed it will continue using the
     * following:
     * <ul>
     * <li>xcontinue</li>
     * <li>finish</li>
     * <li>start</li>
     * <li>close</li>
     * </ul>
     *
     * @param startRes the resource ID for the "Start" replacement.
     * @return Survey, for continued editing.
     */
    public Survey setStartText(@StringRes int startRes) {
        mStartRes = startRes;
        return this;
    }

    /**
     * Saves all the previously inputted configuration, if any.
     *
     * @param packageContext the context which will be used to get shared preferences.
     * @return the object for continued editing.
     */
    public Survey saveConfiguration(@NonNull Context packageContext) {
        SharedPreferences preferences =
                packageContext.getSharedPreferences(KEY_SURVEY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (mButtonBackgroundColor != null) {
            editor.putInt(KEY_BUTTON_BACKGROUND_COLOR_INT, mButtonBackgroundColor);
        }
        return this;
    }

    /**
     * Starts the survey using the activity given for returning the results to onActivityResult()
     * <br><br> Note: if the Survey was created without a json then it will just call
     * saveConfiguration().
     *
     * @param context        the package context
     * @param returnActivity the activity which should receive the onActivityResult callback.
     * @param requestCode    a request code to identify the onActivityResult.
     */
    public void launchSurvey(@NonNull Context context, @NonNull Activity returnActivity,
                             int requestCode) {
        if (mJsonString != null) {
            Intent i_survey = new Intent(context, SurveyActivity.class);
            i_survey.putExtra("json_survey", mJsonString);
            returnActivity.startActivityForResult(i_survey, requestCode);
        }
    }

}
