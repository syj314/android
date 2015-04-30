package com.sunyj.help;

import java.lang.reflect.Method;

import com.sunyj.myhomepage.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

public class FocusScrollListView extends ListView {

	// ��¼��ǰ������������
	private final byte FOCUS_MIDDLE = 0;
	private final byte FOCUS_BOTTOM = 1;
	private final byte FOCUS_TOP = 2;

	// ListView��ÿ��item�ĸ߿�
	private int itemWidth;
	private int itemHeight;

	// ����ListView�ĸ߶�
	private int listHeight;

	// ��������item�����������parent����������ľ����
	private int top;

	// ��������λ��
	private byte mFocusState = FOCUS_MIDDLE;

	// ����ͼƬ��Ҳ���Բ����κ�ͼƬ��ֱ����һ����ɫ��һ����item�ȸߵȿ�ľ���
	private Bitmap mBitmap;

	// �Ƿ񱻵�����setSelection����������˾ͱ���ǿ��ˢ�½���ͼƬ��λ��
	private boolean isSetSelection;

	// Scroller�൱ǰ���ص����֣�����Ŀ�½����Y����
	private int cordinatesY;

	// �Ƿ�����Ļ�����ˣ����ڰ�����ҳ�ģ�ǿ��ˢ�½���ͼƬλ��
	private boolean isPageScroll;

	// �Ƿ��Ѿ��õ���item�ĸ߶�
	private boolean hadHeight;

	// ���ڻ����ķ�װ�˼��ټ������ļ�����
	private Scroller mScroller;

	private Matrix m;

	// scale X��Y�����ڽ�����ͼƬ���쵽�պ����item�Ŀռ�
	private float sy;
	private float sx;

	// ���㻬����ʱ��
	private int sDuration = 1000;

	// ��ҳ��API
	private Method method_pageScroll;

	// item��������ˢ�µ�API
	private Method method_arrowScrollImpl;

	// �����Ƿ����ڻ����Ĺ����У������һ��item�������ڵ�����һ��item�һ������̽���ʱ�ñ�����Ϊfalse
	private boolean isScroll;

	// ��¼�뿪��ǰListViewʱ���ڵĽ���λ�ã����ڴ�ListView�л�������Ŀؼ���Ȼ�����л��������ܱ����ϴν������ڵ�λ��
	private int tmpSelection;

	public FocusScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);

		// ����ListView����Ľ�����Ե��Ҳ���Բ�����
		setVerticalFadingEdgeEnabled(false);
		m = new Matrix();

		// ��ȡ���㱳��ͼƬ
		mBitmap = BitmapFactory
				.decodeResource(getResources(), R.drawable.focus);

		// ͨ�������ʼ��˽�з���
		initPrivateMethods();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		itemWidth = getWidth();
		listHeight = getHeight();

		// ListView�մ�����ʱ��û��child����Ϊ��û��setAdapter�����Ǵ�����ʱ������layout������Ҫ�ж�һ�£�Ҫ��Ȼ�ᱨ��
		if (getChildCount() > 0) {

			// �����ȡListView�ĸ߶��˾Ͳ�Ҫ�ٵ�����������ˣ�Ҫ��ȻĳЩ����߶Ȼ���0��ͬʱ������Ҳ�кô�
			if (!hadHeight) {
				itemHeight = getChildAt(0).getHeight();
				hadHeight = true;
			}

			sx = (float) itemWidth / mBitmap.getWidth();
			sy = (float) itemHeight / mBitmap.getHeight();
			m.setScale(sx, sy);

			// ����item��ߵ��������챳��ͼƬ
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), m, true);
		}
	}

	// ���е�Ч������ͨ������ص�������ɣ������������Ҫ
	@Override
	protected void onDraw(Canvas canvas) {

		// ������������ListView��setSelection�����ͻ�ˢ�²��ҷ��أ���ִ������Ĵ���
		if (isSetSelection) {
			if (null != getSelectedView()) {
				canvas.drawBitmap(mBitmap, 0, getSelectedView().getTop(), null);
				setScroller(getSelectedView().getTop());
				isSetSelection = false;
				return;
			}
		}

		// ����һ�߻���һ��ˢ�£�֪��Scroller������������isScroll��false
		if (mScroller.computeScrollOffset()) {
			// ���ϵĻص�onDraw
			invalidate();
		} else {
			if (isScroll) {
				isScroll = false;
			}
		}

		// ����Ƿ�ҳ��ˢ��
		if (isPageScroll) {
			if (null != getSelectedView()) {
				cordinatesY = getSelectedView().getTop();
				setScroller(getSelectedView().getTop());
				isPageScroll = false;
			}
		} else {
			cordinatesY = mScroller.getCurrY();
		}

		// ����һ�ж���Ϊ�����׼��
		canvas.drawBitmap(mBitmap, 0, cordinatesY, null);
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

		// �뿪ListViewʱ���¼����λ��
		if (getChildCount() > 0) {
			if (!gainFocus) {
				tmpSelection = getSelectedItemPosition();
			} else {
				if (null != getSelectedView()) {
					setSelectionFromTop(tmpSelection, getSelectedView()
							.getTop());
				}
			}
		}
	}

	@Override
	public void setSelection(int position) {
		super.setSelection(position);
		updateFocus();
	}

	/**
	 * The method of setMSelection() instead of setSelection(), so please call
	 * setMSelection to set position of item
	 * 
	 * @param position
	 */
	public void setMSelection(int position) {
		setTmpSelection(position);
		setSelection(position);
		isPageScroll = true;
	}

	private void updateFocus() {
		isSetSelection = true;
	}

	private void setTmpSelection(int position) {
		tmpSelection = position;
	}

	/**
	 * return the number of items at present
	 * 
	 * @return the number of items at present
	 */
	public int getItemNum() {
		return getChildCount();
	}

	private void setScroller(int newY) {
		mScroller.setFinalY(newY);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setMSelection(0);
	}

	// ListView��item����ʵ�����Ƕ�̬�ı�ģ�����һ����ֵx��x+1����x+2֮���ǻ�����������item�����������㽹����ƶ��ǲ��еģ��������ӵ�ʵ�ִ˹��ܵĸ��Ӷ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// ��õ�ǰѡ�е�item
		View view = getSelectedView();

		// ����ľ����߼��ϵĶ����ˣ������ϣ����£����м���в�ͬ���ƶ�
		if (null != view) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if (getLastVisiblePosition() == getAdapter().getCount() - 1
						&& getSelectedItemPosition() == getLastVisiblePosition() - 1
						&& mFocusState == FOCUS_MIDDLE) {
					top = view.getTop() + itemHeight + getDividerHeight();
					mScroller.startScroll(0, view.getTop(), 0,
							top - view.getTop(), sDuration);
					isScroll = true;
					mFocusState = FOCUS_MIDDLE;
					break;
				}

				if (getSelectedItemPosition() < getLastVisiblePosition() - 1) {
					top = view.getTop() + itemHeight + getDividerHeight();
					mScroller.startScroll(0, view.getTop(), 0,
							top - view.getTop(), sDuration);
					isScroll = true;
					mFocusState = FOCUS_MIDDLE;
				} else if (getSelectedItemPosition() == getLastVisiblePosition() - 1) {
					if (mFocusState != FOCUS_BOTTOM) {
						top = listHeight - itemHeight
								- getVerticalFadingEdgeLength()
								- getDividerHeight();
						mScroller.startScroll(0, view.getTop(), 0,
								top - view.getTop(), sDuration);
						mFocusState = FOCUS_BOTTOM;
					}
				}

				break;

			case KeyEvent.KEYCODE_DPAD_UP:
				if (getSelectedItemPosition() == getFirstVisiblePosition() + 1) {
					if (mFocusState != FOCUS_TOP) {
						top = 0 + getDividerHeight()
								+ getVerticalFadingEdgeLength();
						mScroller.startScroll(0, view.getTop(), 0,
								top - view.getTop(), sDuration);
						mFocusState = FOCUS_TOP;
					}
					break;
				}

				if (getSelectedItemPosition() > getFirstVisiblePosition()) {
					top = view.getTop() - itemHeight - getDividerHeight();
					mScroller.startScroll(0, view.getTop(), 0,
							top - view.getTop(), sDuration);
					mFocusState = FOCUS_MIDDLE;
				}
				break;

			}
		}
		Log.i("ListView",
				String.valueOf("listHeight " + listHeight + " itemHeight "
						+ itemHeight + " top " + top));
		return super.onKeyDown(keyCode, event);
	}

	// ��ʼ��˽�з���
	private void initPrivateMethods() {
		try {
			method_pageScroll = ListView.class.getDeclaredMethod("pageScroll",
					int.class);
			method_arrowScrollImpl = ListView.class.getDeclaredMethod(
					"arrowScrollImpl", int.class);
			method_pageScroll.setAccessible(true);
			method_arrowScrollImpl.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ͨ���˷������ý��㱳��ͼƬ
	public void setFocusBitmap(int resourceId) {
		mBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
	}

}