package com.androidadvance.androidsurvey.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

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
                    .setTextColor(
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

    /**
     * Opens the soft keyboard and puts the selected focus into view.
     * <br> <br> This action happens using a handler with a small delay to avoid getting in the way
     * of more important things like ending the slide animation from the page viewer.
     *
     * @param viewToFocus the view that should come into focus
     * @param context     to get the input method manager from the system services.
     */
    public static void showSoftInput(@NonNull final TextView viewToFocus, @NonNull final Context context) {
        // todo: Find a better solution to stop the stuttering when opening the soft keyboard.
        // making the keyboard visible, it runs on a handler with a delay to avoid stuttering.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // making the soft input only appear when the fragment is visible.
                viewToFocus.requestFocus();
                InputMethodManager imm =
                        (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
                // shouldn't be the case, but who knows. Then we just don't show the soft input
                if (imm != null) {
                    imm.showSoftInput(viewToFocus, 0);
                }
            }
        }, 100);
    }

    /**
     * Simple method to hide the keyboard from view
     *
     * @param currentActivity To get the context and the view in focus
     */
    public static void hideSoftInput(@NonNull final Activity currentActivity) {
        // Check if no view has focus:
        final View view = currentActivity.getCurrentFocus();
        if (view != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm =
                            (InputMethodManager) currentActivity
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }, 100);
        }
    }
}
