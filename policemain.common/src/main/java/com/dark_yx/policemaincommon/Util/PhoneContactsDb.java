package com.dark_yx.policemaincommon.Util;

import android.os.Environment;

import com.dark_yx.policemaincommon.Models.PhoneContacts;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by dark_yx on 2016-03-23.
 */

/**
 * 数据库处理
 */
public class PhoneContactsDb {

    private static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("contacts.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbDir(new File(Environment.getExternalStorageDirectory().getPath() + "/PolicePass/Db"))
            .setDbVersion(4)
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
                    // TODO: ...
//                     db.addColumn(PhoneContacts.class,"sii");
                    // db.dropTable(...);
                    // ...
                    // or
                    try {
                        db.dropTable(PhoneContacts.class);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });
    private static DbManager manager = x.getDb(daoConfig);

    /**
     * 添加白名单联系人
     *
     * @param contacts
     */
    public static boolean addContacts(List<PhoneContacts> contacts) {
        try {
            manager.saveOrUpdate(contacts);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<PhoneContacts> getContacts() {
        List<PhoneContacts> contacts = null;
        try {
            contacts = manager.selector(PhoneContacts.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public static List<PhoneContacts> getContacts(String where) {
        List<PhoneContacts> contacts = null;
        try {
            contacts = manager.selector(PhoneContacts.class).where("UserName", "like", "%" + where + "%").
                    or("PhoneNumber", "like", "%" + where + "%").
                    or("Unit", "like", "%" + where + "%").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public static void delContacts() {
        try {
            manager.dropTable(PhoneContacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
