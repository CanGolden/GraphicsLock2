package com.jc.zjcan.jctools.sqlite;

public class JcFrameSQLManager extends AbstractSQLManager {

	private static JcFrameSQLManager instance;

	public static JcFrameSQLManager getInstance() {
		if (instance == null) {
			instance = new JcFrameSQLManager();
		}

		return instance;
	}

	public JcFrameSQLManager() {
		super();
	}

	@Override
	protected void release() {
	}

}