package petstone.project.animalisland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import petstone.project.animalisland.R;
import petstone.project.animalisland.component.PetfriendCustomAdapter;

public class PetFriend extends Fragment {
    ListView listView;
    PetfriendCustomAdapter adapter;
    Button petfriend_user_apply_btn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.petfriend_component, container, false);

        listView = view.findViewById(R.id.petfriend_listview);
        adapter = new PetfriendCustomAdapter(getContext());
        listView.setAdapter(adapter);

        petfriend_user_apply_btn = view.findViewById(R.id.petfriend_apply_button);

        petfriend_user_apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), PetfriendUserApplyActivity.class);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;
                intent = new Intent(getContext(), PetfriendUserSelect.class);
                startActivity(intent);


            }
        });


        return view;
    }
}
