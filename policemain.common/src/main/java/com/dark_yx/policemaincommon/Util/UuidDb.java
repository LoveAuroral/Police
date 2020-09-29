package com.dark_yx.policemaincommon.Util;

import android.os.Environment;

import com.dark_yx.policemaincommon.Models.UUID;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

/**
 * Created by dark_yx on 2016-03-23.
 */

/**
 * 数据库处理
 */
public class UuidDb {

    static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("UUID.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbDir(new File(Environment.getExternalStorageDirectory().getPath() + "/PolicePass/Db"))
            .setDbVersion(1)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {//版本更新
                }
            });

    static DbManager db = x.getDb(daoConfig);

    public static String getUUID() {
        UUID uuid = null;
        try {
            uuid = db.findFirst(UUID.class);
            if (uuid != null)
                return uuid.getID();
            else {
                db.save(new UUID(SystemInfo.getMyUUID()));
                return db.findFirst(UUID.class).getID();
            }
        } catch (DbException e) {
            e.printStackTrace();
            return SystemInfo.getMyUUID();
        }
    }
}