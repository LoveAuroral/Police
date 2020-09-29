package com.dark_yx.policemaincommon.Util;

import com.dark_yx.policemaincommon.Models.AppInfo;
import com.dark_yx.policemaincommon.Models.ChangeWork;
import com.dark_yx.policemaincommon.Models.NoticeInfo;
import com.dark_yx.policemaincommon.Models.UnReadDiary;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
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
public class DbHelp {
    private DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("police.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File(Environment.getExternalStorageDirectory().getPath() + "/PolicePass/DbNew"))
            .setDbVersion(7)
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
//                    try {
//                        db.dropTable(BassIP.class);
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                }
            });


    //添加待处理日志
    public boolean addUnReadDiary(UnReadDiary diary) {
        DbManager manager = x.getDb(daoConfig);
        try {
            manager.save(diary);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    //获取未读日志
    public List<UnReadDiary> getUnReadDiary() {
        List<UnReadDiary> diaries = null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            diaries = dbManager.selector(UnReadDiary.class).where("isRead", "like", "0").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return diaries;
    }

    //删除已处理日志
    public void deleteDiary(UnReadDiary diary) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.delete(diary);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过id删除换班
     *
     * @param id
     */
    public void delExchangeById(int id) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.deleteById(ChangeWork.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过id删除日志
     *
     * @param id
     */
    public void delDiaryById(int id) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.deleteById(UnReadDiary.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //添加待处理换班
    public boolean addUnReadExchange(ChangeWork changeWork) {
        DbManager manager = x.getDb(daoConfig);
        try {
            manager.save(changeWork);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    //获取待换班
    public List<ChangeWork> getUnReadExchange() {
        List<ChangeWork> changeWork = null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            changeWork = dbManager.selector(ChangeWork.class).where("isRead", "like", "0").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return changeWork;
    }

    //删除换班
    public void deldeteExhange(ChangeWork changeWork) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.delete(changeWork);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    //添加公告
    public void addNotices(NoticeInfo info) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.save(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //获取公告
    public final List<NoticeInfo> getNotices() {
        List<NoticeInfo> unReads = null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            List<NoticeInfo> reads = dbManager.selector(NoticeInfo.class).where("isRead", "like", "1").orderBy("id", true).findAll();//查询数据库时通过公告时间降序排列
            unReads = dbManager.selector(NoticeInfo.class).where("isRead", "like", "0").orderBy("id", true).findAll();
            if (reads != null) {
                unReads.addAll(reads);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return unReads;
    }

    //修改公告
    public void changeNotices(NoticeInfo info) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.update(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //搜索公告
    public List<NoticeInfo> getSearch(String s) {
        List<NoticeInfo> infos = null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            infos = dbManager.selector(NoticeInfo.class).where("Title", "like", "%" + s + "%").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return infos;
    }

    //删除公告
    public void deleteNotice(NoticeInfo info) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.delete(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //获取未读公告
    public List<NoticeInfo> getUnreadNotice() {
        List<NoticeInfo> infos = null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            infos = dbManager.selector(NoticeInfo.class).where("isRead", "like", "0").orderBy("id", true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return infos;
    }


    /**
     * 添加应用列表
     */
    public void addAppInfo(List<AppInfo> infos) {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.save(infos);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新应用
     */
    public void updateAppInfo(List<AppInfo> infos) {
        if (deleteAppInfo()) {
            addAppInfo(infos);
        }
    }

    /**
     * 清除应用
     */
    public boolean deleteAppInfo() {
        DbManager dbManager = x.getDb(daoConfig);
        try {
            dbManager.delete(AppInfo.class);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 获取应用
     */
    public List<AppInfo> getAppInfos() {
        List<AppInfo> infos = null;
        DbManager dbManager = x.getDb(daoConfig);
        try {
            infos = dbManager.selector(AppInfo.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return infos;
    }

//    private void checkPermissons() {
//        boolean perimissionFlas = false;
//        for (String permissionStr : permissions) {
//            // 检查该权限是否已经获取
//            int per = ContextCompat.checkSelfPermission(getApplicationContext(), permissionStr);
//            LogUtil.d("权限申请：" + per + "  " + permissionStr);
//            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//            if (per != PackageManager.PERMISSION_GRANTED) {
//                perimissionFlas = true;
//            }
//        }
//        if (perimissionFlas) {
//
//        } else {
//        }
//    }
}

