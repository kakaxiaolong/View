package com.example.lxlviews;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lxlviews.PullToRefreshView.OnFooterRefreshListener;
import com.example.lxlviews.PullToRefreshView.OnHeaderRefreshListener;

public class PullToRefreshViewTest extends Activity {

	PullToRefreshView pullToRefreshView;
	ListView listView;

	Handler TestHandle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				pullToRefreshView.onFooterRefreshComplete();
				break;

			case 1:
				pullToRefreshView.onHeaderRefreshComplete();
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.xx);
		listView = (ListView) findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item, R.id.cc, new String[] { "1", "1", "1", "1", "1",
						"1", "1", "1", "1", "1", "1", "1", "1", "1" });
		listView.setAdapter(adapter);
		pullToRefreshView.showFooterView(false);
		pullToRefreshView.showHeaderView(false);
		pullToRefreshView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						// TODO 自动生成的方法存根
						TestHandle.sendEmptyMessageDelayed(0, 2000);
					}
				});
		pullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(PullToRefreshView view) {
						// TODO 自动生成的方法存根
						TestHandle.sendEmptyMessageDelayed(1, 2000);
					}
				});
	}

}
