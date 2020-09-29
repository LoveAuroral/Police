package com.dark_yx.policemain.chat.database;

import android.text.TextUtils;

import com.dark_yx.policemain.chat.beans.ChatGroupBean;
import com.dark_yx.policemain.chat.beans.ChatGroupUserBean;
import com.dark_yx.policemain.chat.beans.ChatMessage;
import com.dark_yx.policemain.chat.beans.RecentMessageBean;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemaincommon.Util.DataUtil;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dark_yx.policemain.chat.view.chatui.util.Constants.GROUP;

/**
 * Created by dark_yx-i on 2018/2/6.
 */

public class ChatDb {
    private DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("chat.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File(Environment.getExternalStorageDirectory().getPath() + "/PolicePass/DbNew"))
            .setDbVersion(6)
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
                    try {
                        db.dropTable(RecentMessageBean.class);
                        db.dropTable(ChatMessage.class);
                        db.dropTable(ChatGroupBean.class);
                        db.dropTable(UsersBean.class);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });

    DbManager dbManager;

    public ChatDb() {
        dbManager = x.getDb(daoConfig);
    }

    public void saveMessage(ChatMessage chatMessage) throws DbException {
        LogUtil.d(chatMessage.toString());
        dbManager.save(chatMessage);
    }

    public void updateMessage(ChatMessage chatMessage, int type) {
        try {
            ChatMessage first = dbManager.selector(ChatMessage.class).where("type", "=", 2).//发送
                    and("toUserId", "=", chatMessage.getToUserId()).and("tickets", "=", chatMessage.getTickets()).findFirst();
            if (first != null) {
                first.setSendState(type);
                dbManager.update(first);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户Id获取单聊记录
     *
     * @param userId
     * @return
     */
    public List<ChatMessage> getUserChatMessages(long userId, int type, int count, int offset) throws DbException {
        List<ChatMessage> chatMessageList;
        if (userId == 0) return null;

        if (type == GROUP) {
            chatMessageList = dbManager.selector(ChatMessage.class).where("toGroupId", "=", userId).limit(count - offset)
                    .offset(offset).findAll();
            ;
        } else {
            chatMessageList = dbManager.selector(ChatMessage.class).
                    where("type", "=", 2).//发送
                    and("toUserId", "=", userId).
                    and("chatSendType", "=", 0).
                    or("type", "=", 1).//接收
                    and("creatorUserId", "=", userId).
                    and("chatSendType", "=", 0).
                    limit(count - offset)
                    .offset(offset).findAll();
        }
        return chatMessageList;
    }

    /**
     * 根据用户Id获取单聊记录
     *
     * @param userId
     * @return
     */
    public int getUserChatMessagesCount(long userId, int type) throws DbException {
        List<ChatMessage> chatMessageList;
        if (userId == 0) return 0;

        if (type == GROUP) {
            chatMessageList = dbManager.selector(ChatMessage.class).where("toGroupId", "=", userId).findAll();
        } else {
            chatMessageList = dbManager.selector(ChatMessage.class).
                    where("type", "=", 2).//发送
                    and("toUserId", "=", userId).
                    and("chatSendType", "=", 0).

                    or("type", "=", 1).//接收
                    and("creatorUserId", "=", userId).
                    and("chatSendType", "=", 0).
                    findAll();
        }
        return chatMessageList != null ? chatMessageList.size() : 0;
    }

    public void saveGroups(List<ChatGroupBean> chatGroupBean) throws DbException {
        dbManager.delete(ChatGroupBean.class);
        dbManager.save(chatGroupBean);
    }

    public void saveGroup(ChatGroupBean chatGroupBean) throws DbException {
        dbManager.save(chatGroupBean);
    }


    public List<ChatGroupBean> getGroups() {
        try {
            return dbManager.selector(ChatGroupBean.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveGroupUsers(List<ChatGroupUserBean> chatGroupUserBeans) throws DbException {
        dbManager.save(chatGroupUserBeans);
    }

    public void deleteGroupUsers() {
        try {
            dbManager.delete(ChatGroupUserBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<UsersBean> getGroupUser(List<Integer> userId) {
        List<UsersBean> userBeans = new ArrayList<>();
        try {
            for (Integer s : userId) {
                UsersBean usersBean = dbManager.selector(UsersBean.class).where("id", "=", s).findFirst();
                if (usersBean != null)
                    userBeans.add(usersBean);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return userBeans;
    }

    public List<UsersBean> getGroupUser() {
        try {
            return dbManager.selector(UsersBean.class).where("userName", "!=", DataUtil.getAccount().getUserName()).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<UsersBean> getUserByKey(String key) {
        try {
            if (TextUtils.isEmpty(key)) {
                return getGroupUser();
            } else {
                LogUtil.d("key: " + key);
                return dbManager.selector(UsersBean.class).where("userName", "like", "%" + key + "%").
                        or("phoneNumber", "like", "%" + key + "%")
                        .or("name", "like", "%" + key + "%").findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveUser(List<UsersBean> beans) {
        try {
            dbManager.delete(UsersBean.class);
            dbManager.save(beans);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getUserId(String id) {
        List<Integer> strings = new ArrayList<>();
        try {
            List<ChatGroupUserBean> chatGroupId = dbManager.selector(ChatGroupUserBean.class).where("chatGroupId", "=", id).findAll();
            for (ChatGroupUserBean bean : chatGroupId) {
                strings.add(bean.getUserId());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public long getId() {
        try {
            UsersBean userName = dbManager.selector(UsersBean.class).where("userName", "=",
                    DataUtil.getAccount().getUserName()).findFirst();
            if (userName != null) {
                return userName.getId();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public RecentMessageBean getRecentMessage(long objId, int type) throws DbException {
        if (objId == 0) return null;
        return dbManager.selector(RecentMessageBean.class).where("sendObjectId", "=", objId).and("chatSendType", "=", type).findFirst();
    }

    public void saveRecentMessage(RecentMessageBean recentMessageBean) throws DbException {
        LogUtil.d(recentMessageBean.toString());
        dbManager.saveOrUpdate(recentMessageBean);
    }

    public List<RecentMessageBean> getRecentMessages() {
        try {
            return dbManager.selector(RecentMessageBean.class).orderBy("tickets", true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteRecentMessage(RecentMessageBean id) throws DbException {
        dbManager.delete(id);
    }

    public ChatGroupBean getDefaultGroup() {
        try {
            ChatGroupBean isDefaultGroup = dbManager.selector(ChatGroupBean.class).where("id", "=", DataUtil.getDefaultGroup()).findFirst();
            return isDefaultGroup;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        try {
            UsersBean userName = dbManager.selector(UsersBean.class).where("userName", "=",
                    DataUtil.getAccount().getUserName()).findFirst();
            if (userName != null) {
                return userName.getName();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void deleteMessage(ChatMessage id) {
        try {
            dbManager.delete(id);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public void deleteGroupUser(long groupId, long userId) throws DbException {
        dbManager.delete(ChatGroupUserBean.class, WhereBuilder.b("chatGroupId", "=", groupId).and("userId", "=", userId));
    }

    public void deleteGroup(long groupId) throws DbException {
        dbManager.delete(ChatGroupBean.class, WhereBuilder.b("id", "=", groupId));

        // 删除最近聊天里面的群组
        dbManager.delete(RecentMessageBean.class, WhereBuilder.b("chatSendType", "=", 1).and("sendObjectId", "=", groupId));
    }

    public ChatGroupBean GetGroupById(long id) throws DbException {
        return dbManager.selector(ChatGroupBean.class).where("id", "=", id).findFirst();
    }
}
