package com.jc.zjcan.jctools.threadpool;

public class TaskKey {

	/**
	 * 
	 */
	public static final int TASK_KEY_REQUEST_FAILED = -1;
	/**
	 * query IM message undownload.
	 */
	public static final int KEY_QUERY_UNDOWNLOAD_IMMESSAGE = 0X1;

	/**
	 * CCP SDK regist.
	 */
	public static final int KEY_SDK_REGIST = 0x2;

	/**
	 * SDK unregist.
	 */
	public static final int KEY_SDK_UNREGIST = 0x3;

	/**
	 * Delete IM message .
	 */
	public static final int TASK_KEY_DEL_MESSAGE = 0x4;
}
