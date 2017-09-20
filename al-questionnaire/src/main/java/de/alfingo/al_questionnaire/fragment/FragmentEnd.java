package de.alfingo.al_questionnaire.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.alfingo.al_questionnaire.Answers;
import de.alfingo.al_questionnaire.R;
import de.alfingo.al_questionnaire.Survey;
import de.alfingo.al_questionnaire.SurveyActivity;
import de.alfingo.al_questionnaire.models.SurveyProperties;
import de.alfingo.al_questionnaire.utils.SurveyViewUtils;

public class FragmentEnd extends Fragment {

    private FragmentActivity mContext;
    private TextView textView_end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_end, container, false);


        Button button_finish = rootView.findViewById(R.id.button_finish);
        textView_end = rootView.findViewById(R.id.textView_end);


        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((SurveyActivity) mContext).event_survey_completed(Answers.getInstance());

            }
        });

        // Personalizing
        SurveyViewUtils
                .personalizeButton(getActivity(), Survey.KEY_FINISH_TEXT_RES, button_finish);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        SurveyProperties survery_properties = (SurveyProperties) getArguments().getSerializable("survery_properties");

        assert survery_properties != null;
        textView_end.setText(Html.fromHtml(survery_properties.getEndMessage()));

    }
}