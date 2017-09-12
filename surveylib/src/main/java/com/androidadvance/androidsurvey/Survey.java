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
     * The survey preferences containing colors, texts and maybe much more. Please don't edit this
     * by hand, leave the survey professionals do their job.
     */
    public static final String KEY_SURVEY_PREFERENCES = "de.alfingo.survey.preferences";

    /**
     * The keys for all the preferences saved as customization.
     */
    public static final String KEY_BUTTON_BACKGROUND_COLOR_INT = "button_background_color",
            KEY_BUTTON_TEXT_COLOR_INT = "button_text_color",
            KEY_FINISH_TEXT_RES = "finish_res",
            KEY_CONTINUE_TEXT_RES = "continue_res",
            KEY_CLOSE_TEXT_RES = "close_res",
            KEY_START_TEXT_RES = "start_res";

    /**
     * Names for passing information over intents and bundles.
     */
    public static final String KEY_SURVEY_JSON = "json_survey",
            KEY_SURVEY_PROPERTIES = "survey_properties";

    /**
     * The Json containing all the questionnaire information.
     */
    private final String mJsonString;

    /**
     * The shared preferences used for saving customization.
     */
    private final SharedPreferences mSurveyPreferences;

    /**
     * Initializes the survey object, you can just launch it as is using the launchSurvey method or
     * change the color and other aspects from the survey.
     * <br><br> Everything inputted will be saved on the
     * device in form of shared preferences, you can use this class to save the "look-and-feel"
     * at the beginning and only use the survey somewhere later.
     *
     * @param jsonString a string containing all the questions that should be displayed or
     *                   {@code null} if all you want to do is saving the configuration
     *                   todo: Link document of JSON format.
     * @param context    the context for accessing shared preferences.
     */
    public Survey(@Nullable String jsonString, @NonNull Context context) {
        mJsonString = jsonString;
        mSurveyPreferences = context.getSharedPreferences(KEY_SURVEY_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    /**
     * Sets the button text color.
     *
     * @param textColor the displayed text color of the button
     * @return the survey object to continue editing
     */
    public Survey setButtonTextColor(@ColorInt int textColor) {
        mSurveyPreferences.edit()
                .putInt(KEY_BUTTON_TEXT_COLOR_INT, textColor)
                .apply();
        return this;
    }

    /**
     * Sets the background color from the continue, finish, and start button.
     *
     * @param backgroundColor the new background color for the button.
     * @return the survey object for continued editing.
     */
    public Survey setButtonBackgroundColor(@ColorInt int backgroundColor) {
        mSurveyPreferences.edit()
                .putInt(KEY_BUTTON_BACKGROUND_COLOR_INT, backgroundColor)
                .apply();
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
        mSurveyPreferences.edit()
                .putInt(KEY_CONTINUE_TEXT_RES, continueRes)
                .apply();
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
        mSurveyPreferences.edit()
                .putInt(KEY_FINISH_TEXT_RES, finishRes)
                .apply();
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
        mSurveyPreferences.edit()
                .putInt(KEY_CLOSE_TEXT_RES, closeRes)
                .apply();
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
        mSurveyPreferences.edit()
                .putInt(KEY_START_TEXT_RES, startRes)
                .apply();
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
            i_survey.putExtra(KEY_SURVEY_JSON, mJsonString);
            returnActivity.startActivityForResult(i_survey, requestCode);
        }
    }

}
