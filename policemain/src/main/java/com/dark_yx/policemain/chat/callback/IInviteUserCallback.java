package com.dark_yx.policemain.chat.callback;

import java.util.List;

/**
 * Created by Ligh on 2018/3/2 14:47
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public interface IInviteUserCallback {
    void inviteUser(String groupId, List<Long> beans);

    void inviteSuccess();

    void inviteFail(String err);
}
