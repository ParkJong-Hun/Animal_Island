package petstone.project.animalisland;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import petstone.project.animalisland.activity.RehomeFreeSubmitActivity;
import petstone.project.animalisland.activity.RehomeSellSubmitActivity;

public class Sell_Rehome extends Fragment {

    FloatingActionButton sell_submit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sell_rehome, container, false);

        sell_submit = view.findViewById(R.id.sell_submit);

        sell_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RehomeSellSubmitActivity.class);
                startActivityForResult(intent, 0 );
            }
        });

        return view;
    }
}
