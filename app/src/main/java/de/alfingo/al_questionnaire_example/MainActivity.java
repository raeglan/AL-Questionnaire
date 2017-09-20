package de.alfingo.al_questionnaire_example;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.alfingo.al_questionnaire.Survey;
import de.alfingo.al_questionnaire.R;
import de.alfingo.al_questionnaire.SurveyActivity;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int SURVEY_REQUEST = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Nothing fancy here. Plain old simple buttons....

        Button button_survey_example_1 = findViewById(R.id.button_survey_example_1);
        Button button_survey_example_2 = findViewById(R.id.button_survey_example_2);
        Button button_survey_example_3 = findViewById(R.id.button_survey_example_3);

        button_survey_example_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Survey surveyOne = new Survey(loadSurveyJson("example_survey_4.json"),
                        MainActivity.this);
                surveyOne
                        .setStartText(R.string.test_start)
                        .setFinishText(R.string.test_finish)
                        .setContinueText(R.string.test_continue)
                        .setButtonBackgroundColor(Color.WHITE)
                        .setButtonTextColor(Color.BLACK)
                        .launchSurvey(MainActivity.this, MainActivity.this,
                                SURVEY_REQUEST);
            }
        });

        button_survey_example_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_survey = new Intent(MainActivity.this, SurveyActivity.class);
                i_survey.putExtra("json_survey", loadSurveyJson("example_survey_2.json"));
                startActivityForResult(i_survey, SURVEY_REQUEST);
            }
        });

        button_survey_example_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_survey = new Intent(MainActivity.this, SurveyActivity.class);
                i_survey.putExtra("json_survey", loadSurveyJson("example_survey_3.json"));
                startActivityForResult(i_survey, SURVEY_REQUEST);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SURVEY_REQUEST) {
            if (resultCode == RESULT_OK) {

                String answers_json = data.getExtras().getString("answers");
                Log.d("****", "****************** WE HAVE ANSWERS ******************");
                Log.v("ANSWERS JSON", answers_json);
                Log.d("****", "*****************************************************");

                //do whatever you want with them...
            }
        }
    }


    //json stored in the assets folder. but you can get it from wherever you like.
    private String loadSurveyJson(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
