package com.androidadvance.androidsurvey.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidadvance.androidsurvey.Answers;
import com.androidadvance.androidsurvey.R;
import com.androidadvance.androidsurvey.Survey;
import com.androidadvance.androidsurvey.SurveyActivity;
import com.androidadvance.androidsurvey.models.Question;
import com.androidadvance.androidsurvey.utils.SurveyViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentRadioboxes extends QuestionAbstractFragment {

    private final static String TAG = Survey.LIBRARY_NAME + ":" +
            FragmentRadioboxes.class.getSimpleName();

    private Question mQuestion;
    private FragmentActivity mContext;
    private Button mContinueButton;
    private TextView mTitleTextView;
    private RadioGroup mRadioGroup;
    private final ArrayList<RadioButton> mRadioBoxes = new ArrayList<>();
    private boolean atLeastOneChecked = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_radioboxes, container, false);

        mContinueButton = rootView.findViewById(R.id.button_continue);
        mTitleTextView = rootView.findViewById(R.id.textview_q_title);
        mRadioGroup = rootView.findViewById(R.id.radioGroup);

        // Personalizing
        SurveyViewUtils
                .personalizeButton(getActivity(), Survey.KEY_CONTINUE_TEXT_RES, mContinueButton);

        return rootView;
    }

    private void collect_data() {

        //----- collection & validation for is_required
        String the_choice = "";
        atLeastOneChecked = false;
        for (RadioButton rb : mRadioBoxes) {
            if (rb.isChecked()) {
                atLeastOneChecked = true;
                the_choice = rb.getText().toString();
            }
        }

        if (the_choice.length() > 0) {
            Answers.getInstance().put_answer(mTitleTextView.getText().toString(), the_choice);
        }


        if (mQuestion.getRequired()) {
            if (atLeastOneChecked) {
                mContinueButton.setVisibility(View.VISIBLE);
            } else {
                mContinueButton.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mContext = getActivity();
        mQuestion = (Question) getArguments().getSerializable("data");

        // Letting everyone knows when something horrible goes wrong.
        if (mQuestion == null) {
            Log.e(TAG, "A fragment without data was initialized, that shouldn't happen.");
            ((SurveyActivity) mContext).goToNext();
            return;
        }

        mTitleTextView.setText(mQuestion.getQuestionTitle());


        List<String> choices = mQuestion.getChoices();
        if (mQuestion.getRandomChoices()) {
            Collections.shuffle(choices);
        }

        for (String choice : choices) {
            RadioButton rb = new RadioButton(mContext);
            rb.setText(Html.fromHtml(choice));
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mRadioGroup.addView(rb);
            mRadioBoxes.add(rb);

            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    collect_data();
                }
            });
        }

        if (mQuestion.getRequired()) {
            if (atLeastOneChecked) {
                mContinueButton.setVisibility(View.VISIBLE);
            } else {
                mContinueButton.setVisibility(View.GONE);
            }
        }

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> links = mQuestion.getLinks();
                if (links != null) {
                    for(int i = 0; i < mRadioBoxes.size(); i++) {
                        if(mRadioBoxes.get(i).isChecked()) {
                            int link = i >= 0 && i < links.size() ? links.get(i) : -2;
                            ((SurveyActivity) mContext).goToQuestion(link, previousLink);
                            previousLink = link;
                            break;
                        }
                    }
                } else
                    ((SurveyActivity) mContext).goToNext();
            }
        });


    }


}