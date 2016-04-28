package com.azy.app.news.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * ��װ��WebView������ˢ��
 * 
 * @author ����
 * @since 2014-11-11
 */
public class PullToRefreshWebView extends PullToRefreshBase<WebView> {
	/**
	 * ���췽��
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshWebView(Context context) {
		this(context, null);
	}

	/**
	 * ���췽��
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshWebView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * ���췽��
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public PullToRefreshWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#createRefreshableView(android.content.Context,
	 *      android.util.AttributeSet)
	 */
	@Override
	protected WebView createRefreshableView(Context context, AttributeSet attrs) {
		WebView webView = new WebView(context);
		return webView;
	}

	/**
	 * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullDown()
	 */
	@Override
	protected boolean isReadyForPullDown() {
		return mRefreshableView.getScrollY() == 0;
	}

	/**
	 * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullUp()
	 */
	@Override
	protected boolean isReadyForPullUp() {
		return false;
	}
}
