package vund.itplus.vn.demorealmdb;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView txtUserName;
    TextView txtAge;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        txtUserName = (TextView) itemView.findViewById(R.id.txtName);
        txtAge = (TextView) itemView.findViewById(R.id.txtAge);
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar pgBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        pgBar = itemView.findViewById(R.id.progressBar);
    }
}

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TestModel> data = new ArrayList<>();
    private ILoadMore loadMore;
    boolean isLoading;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    Activity activity;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public RecyclerViewAdapter(RecyclerView recyclerView, Activity activity, ArrayList<TestModel> data) {
        this.data = data;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.list_view_test_single_item, parent, false);
            return new RecyclerViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RecyclerViewHolder) {
            TestModel model = data.get(i);
            RecyclerViewHolder holder = (RecyclerViewHolder) viewHolder;
            holder.txtUserName.setText(model.getName());
            holder.txtAge.setText(model.getAge());

        } else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder holder=  (LoadingViewHolder)viewHolder;
            holder.pgBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setLoaded(){
        isLoading = false;
    }

}

