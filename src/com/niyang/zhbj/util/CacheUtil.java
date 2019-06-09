package com.niyang.zhbj.util;

import android.content.Context;

/**
 * 网络缓存工具类
 * @author niyang
 *
 */
public class CacheUtil {
	/**以url为key,以json为value,存储在本地
	 * @param ctx
	 * @param url
	 * @param json
	 */
	public static void setCache(Context ctx,String url,String json) {
		SpUtil.putString(ctx, url, json);
	}
	
	/**获取存储在本地的缓存
	 * @param ctx
	 * @param url
	 * @return
	 */
	public static String getCache(Context ctx,String url) {
		return SpUtil.getString(ctx, url, null);
	}
}
