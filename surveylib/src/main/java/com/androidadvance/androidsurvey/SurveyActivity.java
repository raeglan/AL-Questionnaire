package com.androidadvance.androidsurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.androidadvance.androidsurvey.adapters.AdapterFragmentQ;
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

/**
 * The class that creates the survey, it should be initialized with an intent containing the json.
 */
public class SurveyActivity extends AppCompatActivity {

    private SurveyPojo mSurveyPojo;
    private ViewPager mPager;
    private String style_string = null;
    /**
     * All the fragments which are to be displayed in the pager. When using linking this list will
     * be edited while answering the survey.
     */
    private ArrayList<Fragment> pagerFragments;

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

        pagerFragments = new ArrayList<>();

        //- START -
        if (!mSurveyPojo.getSurveyProperties().getSkipIntro()) {
            FragmentStart frag_start = new FragmentStart();
            Bundle sBundle = new Bundle();
            sBundle.putSerializable("survery_properties", mSurveyPojo.getSurveyProperties());
            frag_start.setArguments(sBundle);
            pagerFragments.add(frag_start);
        }

        //- FILL -
        if (mSurveyPojo.getSurveyProperties().getConditionalLinking())
            addQuestion(mSurveyPojo.getQuestions().get(0));
        else for (Question question : mSurveyPojo.getQuestions())
            addQuestion(question);

        //- END -
        FragmentEnd frag_end = new FragmentEnd();
        Bundle eBundle = new Bundle();
        eBundle.putSerializable("survery_properties", mSurveyPojo.getSurveyProperties());
        frag_end.setArguments(eBundle);
        pagerFragments.add(frag_end);


        mPager = findViewById(R.id.pager);
        AdapterFragmentQ mPagerAdapter = new AdapterFragmentQ(getSupportFragmentManager(), pagerFragments);
        mPager.setAdapter(mPagerAdapter);


    }

    /**
     * Adds the question given as the second to last item on the pager, so that the end always
     * takes the last laugh.
     *
     * @param question The question pojo which should be converted and added.
     */
    private void addQuestion(Question question) {
        Fragment fragment;
        switch (question.getQuestionType()) {
            case "String":
                fragment = new FragmentTextSimple();
                break;
            case "Checkboxes":
                fragment = new FragmentCheckboxes();
                break;
            case "Radioboxes":
                fragment = new FragmentRadioboxes();
                break;
            case "Number":
                fragment = new FragmentNumber();
                break;
            case "StringMultiline":
                fragment = new FragmentMultiline();
                break;
            default:
                throw new UnsupportedOperationException("Question type not known: " +
                        question.getQuestionType());
        }

        Bundle arguments = new Bundle();
        arguments.putSerializable("data", question);
        fragment.setArguments(arguments);

        pagerFragments.add(pagerFragments.size() - 1, fragment);
    }

    /**
     * Appends the end screen to the pager.
     */
    private void putAnEndToItAll() {

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
    public void goToQuestion(int questionNumber) {
        // if no conditional linking is activated we only want to move to the next question.
        if (mSurveyPojo.getSurveyProperties().getConditionalLinking()) {
            if (questionNumber < 0 || questionNumber >= mSurveyPojo.getQuestions().size()) {

            } else {

            }
        }
        goToNext();
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
