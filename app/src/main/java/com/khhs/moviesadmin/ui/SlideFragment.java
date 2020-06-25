package com.khhs.moviesadmin.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.khhs.moviesadmin.R;
import com.khhs.moviesadmin.models.SlideModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {

    public SlideFragment() {
        // Required empty public constructor
    }


    EditText edts1,edts2,edts3,edts4,edts5;
    Button btnsave,btncancel;
    View myView;
    Fragment activeFrag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      myView = inflater.inflate(R.layout.fragment_slide, container, false);
       edts1 = myView.findViewById(R.id.slide1);
       edts2 = myView.findViewById(R.id.slide2);
       edts3 = myView.findViewById(R.id.slide3);
       edts4 = myView.findViewById(R.id.slide4);
       edts5 = myView.findViewById(R.id.slide5);
       btnsave = myView.findViewById(R.id.save);
       btncancel = myView.findViewById(R.id.cancel);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference slideRef = db.collection(getString(R.string.slide_ref));
       btnsave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(
                       edts1.getText().toString().trim().equals("")
                       && edts2.getText().toString().trim().equals("")
                       && edts3.getText().toString().trim().equals("")
                       && edts4.getText().toString().trim().equals("")
                       && edts5.getText().toString().trim().equals("")
               )
               {
                   Toast.makeText(getContext(),"Fill Data!",Toast.LENGTH_LONG).show();
               }
               else
               {
                   SlideModel model = new SlideModel();
                   model.s1 = edts1.getText().toString().trim();
                   model.s2 = edts2.getText().toString().trim();
                   model.s3 = edts3.getText().toString().trim();
                   model.s4 = edts4.getText().toString().trim();
                   model.s5 = edts5.getText().toString().trim();
                   slideRef.add(model);
                   Toast.makeText(getContext(),"Save OK",Toast.LENGTH_LONG).show();;
                   edts1.setText("");
                   edts2.setText("");
                   edts3.setText("");
                   edts4.setText("");
                   edts5.setText("");
               }
           }
       });
       btncancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               edts1.setText("");
               edts2.setText("");
               edts3.setText("");
               edts4.setText("");
               edts5.setText("");
           }
       });

        return myView;
    }
}
