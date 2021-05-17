package petstone.project.animalisland.component;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import petstone.project.animalisland.R;

public class MypageComponent extends Fragment {

    ListView list;
    MypageCustomListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mypage_component, container, false);

        list = rootView.findViewById(R.id.mypage_list);
        listAdapter = new MypageCustomListAdapter(getContext());
        list.setAdapter(listAdapter);

        return rootView;
    }
}
