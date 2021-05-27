package petstone.project.animalisland;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import petstone.project.animalisland.activity.MypageInfoEditActivity;
import petstone.project.animalisland.activity.RehomeFreeSubmitActivity;

public class Free_Rehome extends Fragment {
    FloatingActionButton free_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.free_rehome, container, false);

        free_submit = view.findViewById(R.id.free_submit);

        free_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RehomeFreeSubmitActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        return view;
    }
}
