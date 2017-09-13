package com.androidadvance.androidsurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidadvance.androidsurvey.adapters.SurveyFragmentAdapter;
import com.androidadvance.androidsurvey.fragment.FragmentCheckboxes;
import com.androidadvance.androidsurvey.fragment.FragmentEnd;
import com.androidadvance.androidsurvey.fragment.FragmentMultiline;
import com.androidadvance.androidsurvey.fragment.FragmentNumber;
import com.androidadvance.androidsurvey.fragment.FragmentRadioboxes;
import com.androidadvance.androidsurvey.fragment.FragmentStart;
import com.androidadvance.androidsurvey.fragment.FragmentTextSimple;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.models.SurveyPojo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * The class that creates the survey, it should be initialized with an intent containing the json.
 */
public class SurveyActivity extends AppCompatActivity {

    private SurveyPojo mSurveyPojo;
    private ViewPager mPager;
    private SurveyFragmentAdapter mPagerAdapter;

    // todo: Make the activity lifecycle aware/safe.
    // todo: A better way to start a survey, using a helper class that initializes everything and creates the intent
    // todo: conditional linking: rules for given answers.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_survey);


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mSurveyPojo = new Gson().fromJson(bundle.getString("json_survey"), SurveyPojo.class);
        }


        Log.i("json Object = ", String.valueOf(mSurveyPojo.getQuestions()));

        ArrayList<Integer> initialOrder = new ArrayList<>();

        //- START -
        if (!mSurveyPojo.getSurveyProperties().getSkipIntro()) {
            initialOrder.add(-1);
        }

        //- FILL -
        if (mSurveyPojo.getSurveyProperties().getConditionalLinking()) {
            // if the question added has no linking we assume it is intended to go to the next one
            // in line.
            int nextQuestion = 0;
            Question question;
            do {
                question = mSurveyPojo.getQuestions().get(nextQuestion);
                initialOrder.add(nextQuestion);
                nextQuestion++;
            } while (question.getLinks() == null
                    && nextQuestion < mSurveyPojo.getQuestions().size());
        } else for (int i = 0; i < mSurveyPojo.getQuestions().size(); i++)
            initialOrder.add(i);

        // - SETTING UP ADAPTER AND VIEW -
        mPagerAdapter = new SurveyFragmentAdapter(getSupportFragmentManager(),
                mSurveyPojo.getQuestions(), mSurveyPojo.getSurveyProperties(), initialOrder);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
    }

    public void goToNext() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    /**
     * Adds the question on the given position to the pager and moves to it.
     *
     * @param questionNumber which index number the question has, if it doesn't exist it goes to the
     *                       end directly.
     */
    private void goToQuestion(final int questionNumber) {
        // if no conditional linking is activated or we want to go to the end then the pager just
        // goes to the next item.
        if (mSurveyPojo.getSurveyProperties().getConditionalLinking() && questionNumber >= 0 &&
                questionNumber < mSurveyPojo.getQuestions().size()) {

            // if the question added has no linking we assume it is intended to go to the next one
            // in line.
            int nextQuestion = questionNumber;
            Question question;
            do {
                question = mSurveyPojo.getQuestions().get(nextQuestion);
                mPagerAdapter.add(nextQuestion);
                nextQuestion++;
            } while (question.getLinks() == null
                    && nextQuestion < mSurveyPojo.getQuestions().size());
        }
        goToNext();
    }

    /**
     * Adds the question on the given position to the pager and moves to it.
     * <br><br>
     * If after hitting the back key a new link is selected we need to check if the link was already
     * made, if not, then the next fragment needs to be deleted from the pager.
     *
     * @param questionNumber the index of the question we should move to.
     * @param previousNext   if previously selected, then what question was chosen by the link made.
     */
    public void goToQuestion(int questionNumber, int previousNext) {
        if (previousNext == questionNumber)
            goToNext();
        else if (previousNext == -1)
            goToQuestion(questionNumber);
        else {
            // if they differ we need to delete everything that got added because of this choice.
            for (int i = mPager.getCurrentItem() + 1; i < mPagerAdapter.getCount(); i++) {
                mPagerAdapter.remove(i);
            }
            goToQuestion(questionNumber);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void event_survey_completed(Answers instance) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("answers", instance.getJsonAndResetAnswers());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
