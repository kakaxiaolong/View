package com.example.lxlviews;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class WaterfallView extends ScrollView {
	/**
	 * ScrollView顶级子view。
	 */
	private View childView;
	/**
	 * 移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px 目的是达到一个延迟的效果.
	 */
	private static final float MOVE_FACTOR = 0.5f;
	/**
	 * 滑动到顶部或底部时touch的纵坐标。
	 */
	private float beginY;
	/**
	 * 滑动到顶部或底部时继续滑的距离。
	 */
	private float moveY;

	MotionEvent gotoMotionEvent;

	private boolean topOff = true;
	private boolean bottomOff = true;

	// Context mContext;
	// private View mView;
	// private float touchY;
	// private int scrollY = 0;
	// private boolean handleStop = false;
	// private int eachStep = 0;
	//
	// private static final int MAX_SCROLL_HEIGHT = 400;// 最大滑动距离
	// private static final float SCROLL_RATIO = 0.4f;// 阻尼系数,越小阻力就越大

	public WaterfallView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
		// this.mContext = context;
	}

	public WaterfallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		// this.mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		// TODO 自动生成的方法存根
		super.onFinishInflate();
		if (getChildCount() > 0) {
			childView = getChildAt(0);

		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO 自动生成的方法存根
		gotoMotionEvent = ev;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
            if(isTopOrBottom()){
            	beginY = ev.getY() ;
            }
			break;
		case MotionEvent.ACTION_MOVE:
			int y = (int) ev.getY();
			if (!topOff) {
				FrameLayout.LayoutParams params = (LayoutParams) childView
						.getLayoutParams();
				moveY = (int) (Math.abs(y - beginY) * MOVE_FACTOR);
				params.topMargin = (int) moveY;
				childView.setLayoutParams(params);
			} else if (!bottomOff) {
				FrameLayout.LayoutParams params = (LayoutParams) childView
						.getLayoutParams();
				moveY = -(int) (Math.abs(y - beginY) * MOVE_FACTOR);
				params.topMargin = (int) moveY;
				childView.setLayoutParams(params);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (!topOff) {
				moveTopUpAnim();
			} else if (!bottomOff) {
				moveBottomUpAnim();
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void moveTopUpAnim() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,
				-moveY);
		translateAnimation.setDuration(200);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO 自动生成的方法存根
				beginY = 0;
				FrameLayout.LayoutParams params1 = (LayoutParams) childView
						.getLayoutParams();
				params1.topMargin = 0;
				childView.setLayoutParams(params1);
				TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0); // 防止view动画之后闪烁.
				anim.setDuration(1);
				childView.setAnimation(anim);
				childView.clearAnimation();
				topOff = true;
				new Handler().post(new Runnable() { // ScrollView 设置到顶部
							@Override
							public void run() {
								fullScroll(ScrollView.FOCUS_UP);
							}
						});
			}
		});
		childView.startAnimation(translateAnimation);
	}

	private void moveBottomUpAnim() {
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0,
				-moveY);
		translateAnimation.setDuration(500);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO 自动生成的方法存根
				beginY = getHeight();
				FrameLayout.LayoutParams params1 = (LayoutParams) childView
						.getLayoutParams();
				params1.topMargin = -5;
				childView.setLayoutParams(params1);
				TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0); // 防止view动画之后闪烁.
				anim.setDuration(1);
				childView.setAnimation(anim);
				bottomOff = true;
				new Handler().post(new Runnable() {
					@Override
					public void run() { // ScrollView 设置到底部。
						fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			}
		});
		childView.startAnimation(translateAnimation);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO 自动生成的方法存根
		super.onScrollChanged(l, t, oldl, oldt);
		int h = childView.getHeight() - getHeight();
		if (t == h) {
			if (bottomOff) {
				bottomOff = false;
				beginY = gotoMotionEvent.getY();
				Toast.makeText(getContext(), "到达底部", Toast.LENGTH_SHORT).show();
			}
		} else if (t == 0) {
			if (topOff) {
				topOff = false;
				beginY = gotoMotionEvent.getY();
				Toast.makeText(getContext(), "到达顶部", Toast.LENGTH_SHORT).show();
			}
		} else {
			topOff = true;
			bottomOff = true;
		}
	}

	private boolean isTopOrBottom() {
		if (getScrollY() == 0
				|| getScrollY() == childView.getHeight() - getHeight()) {
			return true;
		}
		return false;
	}
}
