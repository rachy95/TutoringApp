package com.excellassonde.tutoringapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by praiseayorinde on 2017-12-29.
 */

public class TutorMainPageFragment extends Fragment {
    FirebaseAuth auth;
    public TutorMainPageFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        View rootView;


        //1. if the tutor's email is not yet verified then display not a tutor view
        if(user != null && !user.isEmailVerified()) {
            rootView = inflater.inflate(R.layout.not_a_tutor_view, container, false);
        }
        else if (!Utils.getIsTranscriptSubmitted()){
            rootView = inflater.inflate(R.layout.not_a_tutor_view, container, false);
        }
        else if(TutorMainPage.getNumberOfSessions() == 0){
            rootView = inflater.inflate(R.layout.no_sessions_to_confirm, container, false);
            Button backButton = (Button) rootView.findViewById(R.id.go_back_btn);
            backButton.setVisibility(View.INVISIBLE);
        }
        else{
            rootView = inflater.inflate(R.layout.sessions_to_confirm, container, false);
            TextView session = (TextView) rootView.findViewById(R.id.sessions_text);
            String numOfSessions = "" + TutorMainPage.getNumberOfSessions();
            String sessionsToConfirm = String.format(getString(R.string.display_message_sessions), numOfSessions);
            session.setText(sessionsToConfirm);
        }
        return rootView;
    }
}
