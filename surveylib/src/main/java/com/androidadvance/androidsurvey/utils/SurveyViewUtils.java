package com.androidadvance.androidsurvey.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;

import com.androidadvance.androidsurvey.Survey;

/**
 * This class is here just to avoid some code duplicates.
 *
 * @author Rafael Miranda
 * @version 0.1
 * @since 28.08.2017
 */
public class SurveyViewUtils {

    private static final String TAG = SurveyViewUtils.class.getSimpleName();

    /**
     * No need for instantiation.
     */
    private SurveyViewUtils() {
    }

    /**
     * Changes the color and text of the button to the saved values in shared preferences, if any.
     *
     * @param context    to get shared preferences.
     * @param textResKey under which name the text res is saved.
     * @param button     the button which should have its values changed.
     */
    public static void personalizeButton(@NonNull Context context, @NonNull String textResKey, @NonNull Button button) {
        SharedPreferences surveyPreferences = context
                .getSharedPreferences(Survey.KEY_SURVEY_PREFERENCES, Context.MODE_PRIVATE);

        if (surveyPreferences.contains(Survey.KEY_BUTTON_BACKGROUND_COLOR_INT)) {
            button.setBackgroundColor(
                    surveyPreferences.getInt(Survey.KEY_BUTTON_BACKGROUND_COLOR_INT, 0));
        }

        if (surveyPreferences.contains(Survey.KEY_BUTTON_TEXT_COLOR_INT)) {
            button
                    .setBackgroundColor(
                            surveyPreferences.getInt(Survey.KEY_BUTTON_TEXT_COLOR_INT, 0));
        }

        if (surveyPreferences.contains(textResKey)) {
            try {
                button.setText(surveyPreferences.getInt(textResKey, 0));
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, e.getMessage() + ":\nThe saved resource does not exist, please" +
                        " make sure you are setting it to the right value on the Survey Object.");
            }
        }
    }
}
