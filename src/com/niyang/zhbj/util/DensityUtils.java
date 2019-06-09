package com.niyang.zhbj.util;

import android.content.Context;

public class DensityUtils {

	public static int dip2px(float dp, Context context) {
		// 根据px,dp转化公式 dp=px/设备密度 求出px
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density + 0.5f);// 四舍五入
		return px;
	}

	public static float px2dip(float px, Context context) {
		// 根据px,dp转化公式 dp=px/设备密度 求出dip
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
	}
}
