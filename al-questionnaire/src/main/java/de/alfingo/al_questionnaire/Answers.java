package de.alfingo.al_questionnaire;

import com.google.gson.Gson;


import java.util.LinkedHashMap;


/**
 * A singleton containing all the answers given until now.
 */
public class Answers {
    private volatile static Answers uniqueInstance;
    private final LinkedHashMap<String, String> mAnswers = new LinkedHashMap<>();


    private Answers() {
    }

    public void put_answer(String key, String value) {
        mAnswers.put(key, value);
    }

    /**
     * Gives all the answers in form from "Question":"Answer" and resets the values stored on this
     * class.
     *
     * @return the values in a Json format based on "Question":"Answer, Answer, Answer".
     */
    String getJsonAndResetAnswers() {
        Gson gson = new Gson();
        String json = gson.toJson(mAnswers, LinkedHashMap.class);
        mAnswers.clear();
        return json;
    }

    @Override
    public String toString() {
        return String.valueOf(mAnswers);
    }

    public static Answers getInstance() {
        if (uniqueInstance == null) {
            synchronized (Answers.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Answers();
                }
            }
        }
        return uniqueInstance;
    }
}
