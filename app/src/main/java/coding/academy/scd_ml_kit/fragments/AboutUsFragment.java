package coding.academy.scd_ml_kit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import coding.academy.scd_ml_kit.R;


public class AboutUsFragment extends Fragment {
    ImageView mgmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);

    }
    public void gmail(View view) {

        mgmail = view.findViewById(R.id.gmail);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        String [] to={"loolaladimi@gmail.com"};
        startActivity(i);
    }


    public void phon(View view) {
    }

}