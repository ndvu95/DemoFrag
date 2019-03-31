package vund.itplus.vn.demorealmdb;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentTest extends Fragment implements SmoothScrollCallBack {
    RecyclerView listData;
    RecyclerViewAdapter adapter;
    ArrayList<TestModel> datas;
    public SmoothScrollCallBack mListener;

    public void setCallbackListener(SmoothScrollCallBack mListener) {
        this.mListener = mListener;
    }

    private int i;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_test_behaviour, container, false);
        listData = v.findViewById(R.id.listView);

        datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(new TestModel("Ten: " + i, "Tuoi: " + i));
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listData.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(listData, getActivity(), datas);
        listData.setAdapter(adapter);
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (datas.size() <= 100) {
                    datas.add(null);
                    adapter.notifyItemInserted(datas.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            datas.remove(datas.size() - 1);
                            adapter.notifyItemRemoved(datas.size() - 1);

                            //random more data
                            int index = datas.size();
                            int end = index + 10;
                            for (int a = index; a < end; a++) {
                                datas.add(new TestModel("Ten: " + a, "Tuoi: " + a));
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    }, 3000);
                } else {
                    Toast.makeText(getActivity(), "Load data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public void goToTop() {
        listData.smoothScrollToPosition(0);
        Toast.makeText(getActivity(), "Vi tri hien tai: " + 0, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSmoothScoll() {
        goToTop();
    }
}
