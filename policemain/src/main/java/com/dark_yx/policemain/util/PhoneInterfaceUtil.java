package com.dark_yx.policemain.util;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.dark_yx.policemain.fragment.presenter.UserPrivatePhone;
import com.dark_yx.policemain.fragment.presenter.UsersPhone;
import com.dark_yx.policemain.service.ListenCallService;
import com.dark_yx.policemaincommon.Util.FileUtil;
import com.dark_yx.policemaincommon.Util.WriteLogUtil;
import com.google.gson.Gson;
import com.huawei.android.app.admin.DeviceApplicationManager;
import com.huawei.android.app.admin.DeviceCameraManager;
import com.huawei.android.app.admin.DeviceControlManager;
import com.huawei.android.app.admin.DevicePackageManager;
import com.huawei.android.app.admin.DevicePhoneManager;
import com.huawei.android.app.admin.DeviceRestrictionManager;
import com.huawei.android.app.admin.DeviceSettingsManager;
import com.huawei.android.app.admin.DeviceTelephonyManager;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dark_yx on 2016/4/13.
 */
public class PhoneInterfaceUtil {
    public static final int WIFI_CLOSE = 0;
    public static final int WIFI_OPEN = 1;
    public static final int WIFI_DISABLE = 2;
    public static final int WIFI_ENABLE = 3;
    public static List<String> phoneList = new ArrayList<>();
    public static String publicPhoneJosn;
    public static String privatePhoneJosn;
    /*public static ScreenListener listener;*/

    /**
     * 关机
     *
     * @param admin
     */
    public static void shutDown(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
        manager.shutdownDevice(admin);

    }


    /**
     * 重启
     *
     * @param admin
     */
    public static void reboot(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
        manager.rebootDevice(admin);
    }


    /**
     * 格式化
     */
    public static void format(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
    }

    /**
     * 设置app为默认启动器
     *
     * @param admin
     * @param packageName
     * @param className
     */
    public static void setAppAsLauncher(ComponentName admin, String packageName, String className) {
        LogUtil.d("设置app为默认启动器: " + packageName + " " + className);
        DeviceControlManager manager = new DeviceControlManager();
        manager.clearDefaultLauncher(admin);
        manager.setDefaultLauncher(admin, packageName, className);
    }

    public static void clearDefaultLauncher(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
        manager.clearDefaultLauncher(admin);
    }

    /**
     * 启用/禁用wifi
     *
     * @param admin
     * @param disabled
     */
    public static void setWifiDisable(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setWifiDisabled(admin, disabled);
    }

    /**
     * 获取wifi状态
     *
     * @param admin
     */
    public static boolean isWifiDisable(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isWifiDisabled(admin);
    }

    /**
     * 打开/关闭GPS
     */

    public static void openGPS(ComponentName admin, boolean on) {
        DeviceControlManager manager = new DeviceControlManager();
        manager.turnOnGPS(admin, on);
    }

    /**
     * 获取GPS状态
     */

    public static boolean isGPSOn(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
        return manager.isGPSTurnOn(admin);
    }

    /**
     * 获取蓝牙状态
     *
     * @param admin
     */
    public static boolean isBluetoothDisable(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isBluetoothDisabled(admin);
    }

    /**
     * 设置系统为启动器
     *
     * @param admin
     */
    public static void setSystemAsLauncher(ComponentName admin) {
        LogUtil.d("设置系统为启动器: ");
        DeviceControlManager manager = new DeviceControlManager();
        manager.clearDefaultLauncher(admin);
    }

    /**
     * 设置nfc启用禁用
     *
     * @param admin
     * @param disabled :true 禁用;false 启用
     */
    public static void setNFCStatus(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setNFCDisabled(admin, disabled);
    }

    /**
     * 静默卸载某应用（EMUI4)
     *
     * @param admin
     * @param packagePath
     */
    public static void uninstallPackage(ComponentName admin, String packagePath) {
        DevicePackageManager manager = new DevicePackageManager();
        try {
            manager.uninstallPackage(admin, packagePath, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置HOME键禁用/启用
     *
     * @param admin
     * @param disable
     */
    public static void setHomedisable(ComponentName admin, boolean disable) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setHomeButtonDisabled(admin, disable);
    }

    /**
     * 查询 HOME 键功能是否被禁用（EMUI5.0）
     *
     * @param admin
     */
    public static boolean isHomeButtonDisabled(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isHomeButtonDisabled(admin);
    }

    /**
     * 禁用/启用任务键
     *
     * @param admin
     * @param disabled
     */
    public static void setMenuStatus(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setTaskButtonDisabled(admin, disabled);
    }

    /**
     * 查询任务键功能是否被禁用（EMUI5.0）
     *
     * @param admin
     * @return
     */
    public static boolean isTaskButtonDisabled(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isTaskButtonDisabled(admin);
    }

    /**
     * 查询录音功能状态
     */
    public static boolean isMicrophoneDisabled(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isMicrophoneDisabled(admin);
    }

    /**
     * 查询录像功能状态
     */
    public static boolean isVideoDisabled(ComponentName admin) {
        DeviceCameraManager manager = new DeviceCameraManager();
        return manager.isVideoDisabled(admin);
    }

    /**
     * 添加公共白名单
     *
     * @param response
     */
    public static void addPublicNumbers(String response) {
        try {
            Gson gson = new Gson();
            UsersPhone usersPhone = gson.fromJson(response, UsersPhone.class);
            int m = usersPhone.getResult().get(0).getChildren().size();
            for (int i = 0; i < m; i++) {
                int n = usersPhone.getResult().get(0).getChildren().get(i).getUsers().size();
                for (int j = 0; j < n; j++) {
                    UsersPhone.ResultBean.ChildrenBean.UsersBean usersBean = usersPhone.getResult().get(0).getChildren().get(i).getUsers().get(j);
                    String phone = usersBean.getLandline();
                    phoneList.add(String.valueOf(phone));
                }
            }
            Log.d("PublicList", phoneList.toString());
        } catch (Exception e) {
            Log.d("handleCitiesResponse", e.toString());
        }
    }

    /**
     * 添加个人白名单
     *
     * @param response
     */
    public static void addPraviteNumbers(String response) {
        try {
            Gson gson = new Gson();
            UserPrivatePhone usersPhone = gson.fromJson(response, UserPrivatePhone.class);
            int m = usersPhone.getResult().getItems().size();
            for (int i = 0; i < m; i++) {
                String number = usersPhone.getResult().getItems().get(i).getPhoneNumber();
                phoneList.add(number);
            }
            Log.d("PraviteList", phoneList.toString());
        } catch (Exception e) {
            Log.d("handleCitiesResponse", e.toString());
        }
    }

    /**
     * 添加电话白名单（不受华为接口200条限制）
     *
     * @param context
     */
    public static void addWhitePhone(Context context) {
        if (phoneList.size() > 0 || phoneList != null) {
            phoneList.clear();
        }
        File file1 = new File(Environment.getExternalStorageDirectory() + "/Police/", "treeUser.txt");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/Police/", "privateNumber1.txt");
        if (file1.exists() && file2.exists()) {
            publicPhoneJosn = FileUtil.getFile("treeUser.txt");
            privatePhoneJosn = FileUtil.getFile("privateNumber1.txt");
        } else {
            WhiteListUtil whiteListUtil = new WhiteListUtil(context, true);
            whiteListUtil.getData2();
            publicPhoneJosn = FileUtil.getFile("treeUser.txt");
            privatePhoneJosn = FileUtil.getFile("privateNumber1.txt");
        }
        PhoneInterfaceUtil.addPublicNumbers(publicPhoneJosn);
        PhoneInterfaceUtil.addPraviteNumbers(privatePhoneJosn);
    }

    /**
     * 添加电话白名单（华为接口）
     *
     * @param admin
     * @param numbers
     */
    public static void addMdmNumberList(ComponentName admin, ArrayList<String> numbers) {
        String str1 = "添加华为白名单个数： " + numbers.size() + numbers.toString();
        DevicePhoneManager manager = new DevicePhoneManager();

        boolean out = manager.addMdmNumberList(admin, numbers, 0, true, false);//去电
        boolean come = manager.addMdmNumberList(admin, numbers, 0, false, false);//来电
        String s = "";
        if (out)
            s += "添加华为名单：去电添加成功";
        else
            s += "添加华为名单：去电添加失败";

        if (come)
            s += ",来电添加成功";
        else
            s += ",来电添加失败";

        LogUtil.d(numbers.size() + s);
        WriteLogUtil.writeLog("白名单", str1 + s);
    }

    /**
     * 移除电话白名单（华为接口）
     *
     * @param admin
     */
    public static void removeMdmNumberList(ComponentName admin) {
        LogUtil.d("移除华为白名单");
        DevicePhoneManager manager = new DevicePhoneManager();
        manager.removeMdmNumberList(admin, null, 0, true, true);//去电
        manager.removeMdmNumberList(admin, null, 0, false, true);//来电
    }

    public static void setPowerDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setPowerDisabled(admin, disabled);
    }

    /**
     * 设置下拉栏启用禁用
     *
     * @param admin
     * @param disabled
     */
    public static void setBarStatus(ComponentName admin, boolean disabled) {
        LogUtil.d("设置下拉栏启用禁用: ");
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setStatusBarExpandPanelDisabled(admin, disabled);
    }


    /**
     * 查询状态栏下拉菜单是否被禁用（EMUI4.1）
     *
     * @param admin
     * @return
     */
    public static boolean isStatusBarExpandPanelDisabled(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isStatusBarExpandPanelDisabled(admin);
    }

    /**
     * 防卸载
     * 添加阻止某应用被卸载名单
     *
     * @param admin
     */
    public static void disDeleteApp(ComponentName admin, Context context) {
        List<String> mList = new ArrayList<>();
        DevicePackageManager mPackageManager = new DevicePackageManager();
        mList.add(context.getPackageName());
        mPackageManager.addDisallowedUninstallPackages(admin, mList);
    }

    /**
     * 解除防卸载
     * 删除阻止某应用被卸载名单
     *
     * @param admin
     */
    public static void DeleteApp(ComponentName admin, Context context) {
        List<String> mList = new ArrayList<>();
        DevicePackageManager mPackageManager = new DevicePackageManager();
        mList.add(context.getPackageName());
        mPackageManager.removeDisallowedUninstallPackages(admin, mList);
    }


    /**
     * 禁用 恢复出厂设置
     *
     * @param admin
     * @param disable
     */
    public static void setFactoryDisabled(ComponentName admin, boolean disable) {
        DeviceSettingsManager mSettingsManager = new DeviceSettingsManager();
        mSettingsManager.setRestoreFactoryDisabled(admin, disable);
    }

    /**
     * 获取恢复出厂设置禁用状态(EMUI5.1)
     *
     * @param admin
     * @return
     */
    public static boolean isRestoreFactoryDisabled(ComponentName admin) {
        DeviceSettingsManager mSettingsManager = new DeviceSettingsManager();
        return mSettingsManager.isRestoreFactoryDisabled(admin);
    }

    /**
     * 设置相机功能启用禁用
     *
     * @param admin
     */
    public static void setVideo(ComponentName admin, boolean disable) {
        DeviceCameraManager deviceCameraManager = new DeviceCameraManager();
        deviceCameraManager.setVideoDisabled(admin, disable);
    }

    /**
     * 设置禁用/启用Setting
     *
     * @param admin
     * @param disable
     */
    public static void setSetting(ComponentName admin, boolean disable) {
        DeviceRestrictionManager mRectrictionManager = new DeviceRestrictionManager();
        mRectrictionManager.setSettingsApplicationDisabled(admin, disable);
    }

    /**
     * 设置静默激活设备管理器
     *
     * @param admin
     */
    public static void setActiveAdmin(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
        manager.setSilentActiveAdmin(admin);
    }

    /**
     * @param admin
     * @param context
     */
    public static void openNotCancelActivationAdmin(ComponentName admin, Context context) {
        List<String> list = new ArrayList<>();
        DevicePackageManager devicePackageManager = new DevicePackageManager();
        list.add(context.getPackageName());
        devicePackageManager.addDisabledDeactivateMdmPackages(admin, list);
    }

    public static void closeNotCancelActivationAdmin(ComponentName admin, Context context) {
        List<String> list = new ArrayList<>();
        DevicePackageManager devicePackageManager = new DevicePackageManager();
        list.add(context.getPackageName());
        devicePackageManager.removeDisabledDeactivateMdmPackages(admin, list);
    }

    /**
     * 启用/禁用USB传输数据
     *
     * @param admin
     * @param disable
     */
    public static void setUSBdisable(ComponentName admin, boolean disable) {
        LogUtil.e("qaswqas");
        DeviceRestrictionManager deviceRestrictionManager = new DeviceRestrictionManager();
        deviceRestrictionManager.setUSBDataDisabled(admin, disable);
    }

    /**
     * 获取root状态
     *
     * @param admin
     * @return
     */
    public static boolean isRoot(ComponentName admin) {
        DeviceControlManager manager = new DeviceControlManager();
        return manager.isRooted(admin);
    }

    /**
     * 添加保持某应用始终运行名单
     *
     * @param admin
     */
    public static void addPersistentAppRun(ComponentName admin, Context context) {
        List<String> list = new ArrayList<>();
        list.add(context.getPackageName());
//        list.add("com.newabel.nfcdemo");
        LogUtil.d("packageName:" + list.toString());
        DeviceApplicationManager manager = new DeviceApplicationManager();
        manager.addPersistentApp(admin, list);
        List<String> persistentApp = manager.getPersistentApp(admin);
        LogUtil.d("persistentApp: " + persistentApp.toString());
    }

    public static void addIgnoreFrequentRelaunchAppList(ComponentName admin, Context context) {
        ArrayList<String> apps = new ArrayList<>();
        apps.add(context.getPackageName());
        DeviceApplicationManager manager = new DeviceApplicationManager();
        manager.addIgnoreFrequentRelaunchAppList(admin, apps);
    }

    /**
     * 查询短信功能是否被禁用
     *
     * @param admin
     */
    public static boolean isSMSDisable(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isSMSDisabled(admin);
    }

    /**
     * 启动短信功能
     *
     * @param admin
     * @param disabled
     */
    public static void setSMSDisable(ComponentName admin, boolean disabled) {
        if (PhoneInterfaceUtil.isSMSDisable(admin)) {
            DeviceRestrictionManager manager = new DeviceRestrictionManager();
            manager.setSMSDisabled(admin, disabled);
        }
    }

    /**
     * 获取禁止某应用运行名单
     *
     * @param admin
     * @return
     */
    public static List<String> getDisallowApp(ComponentName admin) {
        DeviceApplicationManager manager = new DeviceApplicationManager();
        return manager.getDisallowedRunningApp(admin);
    }

    /**
     * 删除禁止某应用运行名单
     *
     * @param admin
     */
    public static void removeDisallowApp(ComponentName admin) {
        List<String> disallowApp = PhoneInterfaceUtil.getDisallowApp(admin);
        LogUtil.d("disallowApp:" + disallowApp.toString());
        if (disallowApp != null && disallowApp.size() > 0) {
            DeviceApplicationManager manager = new DeviceApplicationManager();
            manager.removeDisallowedRunningApp(admin, disallowApp);
        }
    }


    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState(Context pContext, Object[] arg) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }
            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);
            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
            return isOpen;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("得到移动数据状态出错");
            return false;
        }
    }

    /**
     * 设置手机的移动数据
     *
     * @param pContext
     * @param pBoolean
     */
    public static void setMobileData(Context pContext, boolean pBoolean) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
            method.invoke(mConnectivityManager, pBoolean);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("移动数据设置错误: " + e.toString());
        }
    }

    public static void setUserDisabled(ComponentName admin, boolean disabled) {
        DeviceSettingsManager manager = new DeviceSettingsManager();
        manager.setAddUserDisabled(admin, disabled);
    }

    /**
     * 禁用/启用系统升级功能
     *
     * @param admin
     * @param disabled
     */
    public static void setSystemUpdateDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setSystemUpdateDisabled(admin, disabled);
    }

    /**
     * 获取添加多用户禁用状态(EMUI5.1)
     *
     * @param admin
     * @return
     */
    public static boolean isUserDisabled(ComponentName admin) {
        DeviceSettingsManager manager = new DeviceSettingsManager();
        return manager.isAddUserDisabled(admin);
    }

    /**
     * 禁用/启用应用发送通知功能
     */
    public static void setSendNotificationDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setSendNotificationDisabled(admin, disabled);
    }

    /**
     * 启用/禁用wifi
     *
     * @param admin
     * @param disabled
     */
    public static void setWifiApDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setWifiApDisabled(admin, disabled);
    }

    /**
     * 启用/禁用蓝牙
     *
     * @param admin
     * @param disabled
     */
    public static void setBluetoothDisable(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setBluetoothDisabled(admin, disabled);
    }

    /**
     * 禁用/启用 USB 调试模式、数据传输
     *
     * @param admin
     * @param disabled
     */
    public static void setUSBDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setUSBDataDisabled(admin, disabled);
    }

    /**
     * 禁用/启用 adb 调试
     *
     * @param admin
     * @param disabled
     */
    public static void setAdbDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setAdbDisabled(admin, disabled);
    }

    /**
     * 禁用/启用录音功能
     *
     * @param admin
     * @param disabled
     */
    public static void setMicrophoneDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setMicrophoneDisabled(admin, disabled);
    }

    /**
     * 允许/禁止开发人员选项（EMUI8.0）
     *
     * @param admin
     * @param disable
     */
    public static void setDevelopmentOptionDisabled(ComponentName admin, Boolean disable) {
        DeviceSettingsManager mSettingsManager = new DeviceSettingsManager();
        mSettingsManager.setDevelopmentOptionDisabled(admin, disable);
    }

    /**
     * 获取Menu状态
     *
     * @param admin
     * @return
     */
    public static boolean getMenuStatus(ComponentName admin) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        return manager.isTaskButtonDisabled(admin);
    }

    /**
     * 设置数据流量禁用/启用
     *
     * @param admin
     * @param disabled
     */
    public static void setDataConnectivityDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setDataConnectivityDisabled(admin, disabled);
    }

    /**
     * 禁用卡2
     * （接口文档权限不对）
     *
     * @param admin
     * @param disabled
     */
    public static void setSlot2Disabled(ComponentName admin, boolean disabled) {
        DeviceTelephonyManager manager = new DeviceTelephonyManager();
        manager.setSlot2Disabled(admin, disabled);
    }

    /**
     * 禁用卡2数据业务
     * （接口文档权限不对）
     *
     * @param admin
     * @param disabled
     */
    public static void setSlot2DataConnectivityDisabled(ComponentName admin, boolean disabled) {
        DeviceTelephonyManager manager = new DeviceTelephonyManager();
        manager.setSlot2DataConnectivityDisabled(admin, disabled);
    }

    /**
     * 禁用/启用 google 的备份和恢复
     *
     * @param admin
     * @param disabled
     */
    public static void setGoogleBackupRestoreDisabled(ComponentName admin, boolean disabled) {
        DeviceSettingsManager manager = new DeviceSettingsManager();
        manager.setGoogleBackupRestoreDisabled(admin, disabled);
    }

    /**
     * 禁用/启用“设置”菜单中的“huawei beam”
     *
     * @param admin
     * @param disabled
     */
    public static void setHuaweiBeamDisabled(ComponentName admin, boolean disabled) {
        DeviceSettingsManager manager = new DeviceSettingsManager();
        manager.setHuaweiBeamDisabled(admin, disabled);

    }

    /**
     * 禁用/启用 USB 网络共享菜单
     *
     * @param admin
     * @param disabled
     */
    public static void setUSBTetheringDisabled(ComponentName admin, boolean disabled) {
        DeviceSettingsManager manager = new DeviceSettingsManager();
        manager.setUSBTetheringDisabled(admin, disabled);
    }

    /**
     * 禁止/允许所有系统以及三方应用通知消息
     *
     * @param admin
     * @param disabled
     */
    public static void setNotificationDisabled(ComponentName admin, boolean disabled) {
        DeviceSettingsManager manager = new DeviceSettingsManager();
        manager.setNotificationDisabled(admin, disabled);
    }

    /**
     * 禁止/允许文件分享
     *
     * @param admin
     * @param disabled
     */
    public static void setFileShareDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setFileShareDisabled(admin, disabled);
    }

    /**
     * 禁用系统预装浏览器
     *
     * @param admin
     * @param disabled
     */
    public static void setSystemBrowserDisabled(ComponentName admin, boolean disabled) {
        DeviceRestrictionManager manager = new DeviceRestrictionManager();
        manager.setSystemBrowserDisabled(admin, disabled);
    }

    public static void killApplicationProcess(ComponentName admin, String packageName) {
        DeviceApplicationManager manager = new DeviceApplicationManager();
        try {
            manager.killApplicationProcess(admin, packageName);
        }catch (Exception ignored) {
        }
    }

    public static void openInit(final ComponentName admin, String packageName, String className, Context context) {
        SPUtil.getInstance(context).put(SPUtil.Phone.MODE, 1);
        new WhiteListUtil(context, true).getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ListenCallService.class));
        }
        PhoneInterfaceUtil.setPowerDisabled(admin, true);//禁用通过电源键进入关机界面
//        PhoneInterfaceUtil.setSystemBrowserDisabled(admin, true);//禁用系统预装浏览器
//        PhoneInterfaceUtil.setNotificationDisabled(admin, true);//禁止/允许所有系统以及三方应用通知消息
        PhoneInterfaceUtil.setFileShareDisabled(admin, true);//禁止/允许文件分享
//        PhoneInterfaceUtil.setSlot2Disabled(admin, true);
        LogUtil.d("禁用状态是---->" + PhoneInterfaceUtil.getMenuStatus(admin));
        PhoneInterfaceUtil.setUSBTetheringDisabled(admin, true);//禁用/USB 网络共享菜单
        PhoneInterfaceUtil.setHuaweiBeamDisabled(admin, true);//禁用/“设置”菜单中的“huawei beam”
        PhoneInterfaceUtil.setMenuStatus(admin, true);//禁用任务键
//        PhoneInterfaceUtil.setMicrophoneDisabled(admin, true);//设置语音功能禁用
        PhoneInterfaceUtil.setAppAsLauncher(admin, packageName, className);//设置app为桌面启动器
        addIgnoreFrequentRelaunchAppList(admin, context);
        PhoneInterfaceUtil.setGoogleBackupRestoreDisabled(admin, true);//禁用 google 的备份和恢复
//        PhoneInterfaceUtil.disDeleteApp(admin, context);//防卸载
//        PhoneInterfaceUtil.setWifiDisable(admin, true);//禁用wifi
        PhoneInterfaceUtil.setSystemUpdateDisabled(admin, true);//禁用系统升级功能
        PhoneInterfaceUtil.setDevelopmentOptionDisabled(admin, true);//禁止开发人员选项
        PhoneInterfaceUtil.setFactoryDisabled(admin, true);//禁用恢复出厂设置
        PhoneInterfaceUtil.setUSBDisabled(admin, true);//禁用 USB 调试模式、数据传输
        PhoneInterfaceUtil.setAdbDisabled(admin, true);//禁用 ADB 调试模式
//        PhoneInterfaceUtil.setWifiApDisabled(admin, true);//禁用热点
        PhoneInterfaceUtil.setUserDisabled(admin, true);//禁用 添加多用户
//        PhoneInterfaceUtil.setBluetoothDisable(admin, true);//禁用蓝牙
        PhoneInterfaceUtil.setBarStatus(admin, true);//设置下拉状态栏启用
        PhoneInterfaceUtil.setHomedisable(admin, true);//禁用HOME键
        PhoneInterfaceUtil.addPersistentAppRun(admin, context);//添加保持某应用始终运行名单
        LogUtil.d("openInit");
    }

    public static void exitApp(Context context, ComponentName admin) {
        SPUtil.getInstance(context).put(SPUtil.Phone.MODE, 2);
        context.stopService(new Intent(context, ListenCallService.class));
        PhoneInterfaceUtil.setSMSDisable(admin, false);
        PhoneInterfaceUtil.removeDisallowApp(admin);
        PhoneInterfaceUtil.setPowerDisabled(admin, false);//启用通过电源键进入关机界面
//        PhoneInterfaceUtil.setSystemBrowserDisabled(admin, false);//禁用系统预装浏览器
        PhoneInterfaceUtil.setNotificationDisabled(admin, false);//禁止/允许所有系统以及三方应用通知消息
        PhoneInterfaceUtil.setFileShareDisabled(admin, false);//禁止/允许文件分享
        PhoneInterfaceUtil.setWifiDisable(admin, false);//启用wifi
        PhoneInterfaceUtil.setSendNotificationDisabled(admin, false);//启用应用发送通知功能
        PhoneInterfaceUtil.setUSBDisabled(admin, false);//启用 USB 调试模式、数据传输
        PhoneInterfaceUtil.setAdbDisabled(admin, false);//启用 ADB 调试模式
        PhoneInterfaceUtil.setDevelopmentOptionDisabled(admin, false);//启用开发人员选项
        PhoneInterfaceUtil.setWifiApDisabled(admin, false);//启用热点
        PhoneInterfaceUtil.setBluetoothDisable(admin, false);//启用蓝牙
//        PhoneInterfaceUtil.setMicrophoneDisabled(admin, false);//设置语音功能启用
        PhoneInterfaceUtil.removeMdmNumberList(admin);
        PhoneInterfaceUtil.setMenuStatus(admin, false);
        PhoneInterfaceUtil.setBarStatus(admin, false);//设置下拉状态栏启用
        PhoneInterfaceUtil.setHomedisable(admin, false);
        PhoneInterfaceUtil.setSystemAsLauncher(admin);
        PhoneInterfaceUtil.clearDefaultLauncher(admin);
        LogUtil.d("exitApp");

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(startMain);
    }

}
