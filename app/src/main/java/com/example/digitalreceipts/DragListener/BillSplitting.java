package com.example.digitalreceipts.DragListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.digitalreceipts.R;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;


public class BillSplitting extends AppCompatActivity {

    public class MainActivity extends AppCompatActivity implements Listener {

        @BindView(R.id.rvTop) RecyclerView rvTop;
        @BindView(R.id.rvBottom) RecyclerView rvBottom;
        @BindView(R.id.tvEmptyListTop) TextView tvEmptyListTop;
        @BindView(R.id.tvEmptyListBottom) TextView tvEmptyListBottom;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bill_splitting);
            ButterKnife.bind(this);

            initTopRecyclerView();
            initBottomRecyclerView();

            tvEmptyListTop.setVisibility(View.GONE);
            tvEmptyListBottom.setVisibility(View.GONE);
        }

        private void initTopRecyclerView() {
            rvTop.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));

            List<String> topList = new ArrayList<>();
            topList.add("A");
            topList.add("B");

            ListAdapter topListAdapter = new ListAdapter(topList, this);
            rvTop.setAdapter(topListAdapter);
            tvEmptyListTop.setOnDragListener(topListAdapter.getDragInstance());
            rvTop.setOnDragListener(topListAdapter.getDragInstance());
        }

        private void initBottomRecyclerView() {
            rvBottom.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.HORIZONTAL, false));

            List<String> bottomList = new ArrayList<>();
            bottomList.add("C");
            bottomList.add("D");

            ListAdapter bottomListAdapter = new ListAdapter(bottomList, this);
            rvBottom.setAdapter(bottomListAdapter);
            tvEmptyListBottom.setOnDragListener(bottomListAdapter.getDragInstance());
            rvBottom.setOnDragListener(bottomListAdapter.getDragInstance());
        }

        @Override
        public void setEmptyListTop(boolean visibility) {
            tvEmptyListTop.setVisibility(visibility ? View.VISIBLE : View.GONE);
            rvTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
        }

        @Override
        public void setEmptyListBottom(boolean visibility) {
            tvEmptyListBottom.setVisibility(visibility ? View.VISIBLE : View.GONE);
            rvBottom.setVisibility(visibility ? View.GONE : View.VISIBLE);
        }
    }
}
