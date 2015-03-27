package com.jc.zjcan.jctools.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jc.zjcan.jcframework.JcFrameworkApplication;

public abstract class AbstractSQLManager {
    public static final int VERSION_2 = 2;
    public static final int VERSION_3560 = 3560;
    public static final String TAG = AbstractSQLManager.class.getName();
    private static CCPDBHelper databaseHelper;
    private static SQLiteDatabase sqliteDB;

    public AbstractSQLManager() {
        openDatabase(JcFrameworkApplication.getInstance(),
                JcFrameworkApplication.getInstance().getVersionCode());
    }

    private final String DATABASE_NAME = "jcframework.db";

    private void openDatabase(Context context, int databaseVersion) {
        if (databaseHelper == null) {
            databaseHelper = new CCPDBHelper(context, databaseVersion);
        }
        if (sqliteDB == null) {
            sqliteDB = databaseHelper.getWritableDatabase();
        }
    }

    public void destroy() {
        try {
            if (databaseHelper != null) {
                databaseHelper.close();
                databaseHelper = null;
            }

            closeDB();

            release();

        } catch (Exception e) {
            Log.v("======", "Exception");
        }
    }

    private void open(boolean isReadonly) {
        if (databaseHelper == null) {
            openDatabase(JcFrameworkApplication.getInstance(),
                    JcFrameworkApplication.getInstance().getVersionCode());
        }

        if (sqliteDB == null) {
            if (isReadonly) {
                sqliteDB = databaseHelper.getReadableDatabase();
            } else {
                sqliteDB = databaseHelper.getWritableDatabase();
            }
        }
    }

    public final void reopen() {
        closeDB();
        open(false);
    }

    private void closeDB() {
        if (sqliteDB != null) {
            sqliteDB.close();
            sqliteDB = null;
        }
    }

    protected final SQLiteDatabase sqliteDB() {
        return sqliteDB(false);
    }

    /**
     * @param isReadonly
     * @return
     */
    protected final SQLiteDatabase sqliteDB(boolean isReadonly) {
        open(isReadonly);
        return sqliteDB;
    }

    // ---------------------------------------------------------------

    class BaseColumn {

        public static final String id = "_id";
    }

    class CCPDBHelper extends SQLiteOpenHelper {
        static final String DESC = "DESC";
        static final String ASC = "ASC";

        static final String TABLES_NAME_1 = "1_info";
        static final String TABLES_NAME_2 = "2_notice";
        static final String TABLES_NAME_3 = "3_message";

        final String[] TABLE_NAME = {TABLES_NAME_1, TABLES_NAME_2,
                TABLES_NAME_3,};

        public CCPDBHelper(Context context, int version) {
            this(context, DATABASE_NAME, null, version);
        }

        public CCPDBHelper(Context context, String name, CursorFactory factory,
                           int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                if (oldVersion <= VERSION_3560) {
                    dropTableByTableName(db, new String[]{"im_message",
                            "group_info"});
                    dropTableByTableName(db, new String[]{DATABASE_NAME});
                    createTables(db);
                }
            }
        }

		/*
         * private void updateTables(SQLiteDatabase db) {
		 * 
		 * // If you need to update the database table structure, this //
		 * operation }
		 */

        void createTables(SQLiteDatabase db) {
            try {
                createTableForGroupInfo(db);
            } catch (Exception e) {
            }
        }

        // Group information
        class IMGroupInfoColumn extends BaseColumn {
            public static final String GROUP_ID = "GROUPID";
            public static final String GROUP_NAME = "NAME"; // group name
            public static final String GROUP_OWNER = "OWNER"; // Group creator
            // information
            public static final String GROUP_TYPE = "TYPE"; // Group property
            // type (whether it
            // is normal group,
            // VIP group.)
            public static final String GROUP_DECLARED = "DECLARED"; // The group
            // information
            // bulletin
            public static final String GROUP_DATECREATED = "CREATE_DATE"; // Group
            // creation
            // time
            public static final String GROUP_MEMBER_COUNTS = "COUNT"; // The
            // number
            // of
            // group
            // members
            public static final String GROUP_PERMISSION = "PERMISSION"; // Join
            // the
            // required
            // permission

        }

        private void createTableForGroupInfo(SQLiteDatabase db) {

            String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLES_NAME_1
                    + " ( " // ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMGroupInfoColumn.GROUP_ID + " TEXT PRIMARY KEY , "
                    + IMGroupInfoColumn.GROUP_NAME + " TEXT , "
                    + IMGroupInfoColumn.GROUP_DATECREATED + " TEXT , "
                    + IMGroupInfoColumn.GROUP_DECLARED + " TEXT , "
                    + IMGroupInfoColumn.GROUP_OWNER + " TEXT , "
                    + IMGroupInfoColumn.GROUP_MEMBER_COUNTS + " INTEGER , "
                    + IMGroupInfoColumn.GROUP_PERMISSION + " INTEGER , "
                    + IMGroupInfoColumn.GROUP_TYPE + " INTEGER)";
            db.execSQL(sql);
        }

        // ---------------------------------------------------------------------------------------------------------------

        /**
         * Drop table
         *
         * @param db
         * @param TABLENAME
         */
        void dropTableByTableName(SQLiteDatabase db, String[] TABLENAME) {
            StringBuffer sql = new StringBuffer("DROP TABLE IF EXISTS ");
            int len = sql.length();
            for (String name : TABLENAME) {
                try {
                    sql.append(name);
                    db.execSQL(sql.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sql.delete(len, sql.length());
                }
            }
        }

        void alterTable(SQLiteDatabase db, String table, String text,
                        String type, String def) {
            try {
                String sql = "";
                if (def == null || def.equals("")) {
                    sql = "alter table " + table + " add " + text + " " + type;
                } else {
                    sql = "alter table " + table + " add " + text + " " + type
                            + " default " + def;
                }
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected abstract void release();
}
