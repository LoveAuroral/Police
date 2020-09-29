package com.dark_yx.policemain.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dark_yx.policemain.R;
import com.dark_yx.policemain.broadCastReceiver.DeviceReceiver;
import com.dark_yx.policemain.fragment.presenter.UsersPhone;
import com.dark_yx.policemain.login.view.LoginActivity;
import com.dark_yx.policemain.service.BaseSationService;
import com.dark_yx.policemain.util.CommonMethod;
import com.dark_yx.policemain.util.PhoneInterfaceUtil;
import com.dark_yx.policemain.util.WhiteListUtil;
import com.dark_yx.policemaincommon.Models.User;
import com.dark_yx.policemaincommon.Models.UserTreeResult;
import com.dark_yx.policemaincommon.Models.UsersBean;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.dark_yx.policemain.PoliceMainApplication.mContext;

/**
 * Created by lmp on 2016/4/29.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view, messageSet, rl_vision, rl_white, rl_exit, rl_Phone, rl_exit1, rl_setnfc, rl_set_mymessage1, rl_clock;
    private TextView tvMyName, tvIp, tvVedioIp, tvVersion;
    List<Tb_contacts> mList;
    ArrayList<UsersBean> nameList = new ArrayList<>();
    UserTreeResult result = new UserTreeResult();
    String phoneJosn;

    ComponentName admin;

    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        return view;
    }

    @Override
    public void initData() {
        init();

    }

    //把读取出来的json数据进行解析
    public void addUser(String response) {
        try {
            mList = new ArrayList<>();
            Gson gson = new Gson();
            UsersPhone usersPhone = gson.fromJson(response, UsersPhone.class);
            int m = usersPhone.getResult().get(0).getChildren().size();
            for (int i = 0; i < m; i++) {
                int n = usersPhone.getResult().get(0).getChildren().get(i).getUsers().size();
                for (int j = 0; j < n; j++) {
                    UsersPhone.ResultBean.ChildrenBean.UsersBean usersBean = usersPhone.getResult().get(0).getChildren().get(i).getUsers().get(j);
                    String name = usersBean.getName();
                    String phone = usersBean.getLandline();
                    Tb_contacts tb_contacts = new Tb_contacts();
                    tb_contacts.setName(name);
                    tb_contacts.setNumber(phone);
                    mList.add(tb_contacts);
                }
            }
            Log.d("mList", mList.toString());
            Log.d("getChildren()", m + "");
            try {
                BatchAddContact(mList);
                Toast.makeText(context, "同步成功", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("handleCitiesResponse", e.toString());
        }
    }

    private void init() {
        admin = new ComponentName(getActivity(), DeviceReceiver.class);
        rl_exit1 = view.findViewById(R.id.rl_exit1);
        rl_setnfc = view.findViewById(R.id.rl_setnfc);
        messageSet = view.findViewById(R.id.rl_set_mymessage);
        rl_set_mymessage1 = view.findViewById(R.id.rl_set_mymessage1);
        rl_clock = view.findViewById(R.id.rl_clock);
        tvMyName = (TextView) view.findViewById(R.id.tv_myname);
        messageSet.setOnClickListener(this);
        tvIp = (TextView) view.findViewById(R.id.tv_ip);
        tvVedioIp = (TextView) view.findViewById(R.id.tv_ip1);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        rl_vision = view.findViewById(R.id.rl_vision);
        rl_white = view.findViewById(R.id.rl_white);
        rl_exit = view.findViewById(R.id.rl_exit);
        rl_Phone = (RelativeLayout) view.findViewById(R.id.rl_exit18);

        rl_vision.setOnClickListener(this);
        rl_white.setOnClickListener(this);
        rl_exit.setOnClickListener(this);
        //rl_exit1.setOnClickListener(this);
        rl_Phone.setOnClickListener(this);
        rl_exit1.setOnClickListener(this);
        rl_setnfc.setOnClickListener(this);
        tvVedioIp.setOnClickListener(this);
        rl_set_mymessage1.setOnClickListener(this);
        rl_clock.setOnClickListener(this);

        User account = DataUtil.getAccount();
        tvMyName.setText(account.getUserName());
//        tvIp.setText(DataUtil.getBassIp());
//        tvVedioIp.setText(DataUtil.getVideoIpOnly());
        tvVersion.setText(CommonMethod.getVersion(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataUtil.getBaseSation(getContext()) != 0) {
            tvIp.setText(String.valueOf(DataUtil.getBaseSation(getContext())));
        } else {
            tvIp.setText("0");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_vision:
                break;
            case R.id.rl_white:
                Toast.makeText(context, "正在同步白名单", Toast.LENGTH_SHORT).show();
//                PhoneInterfaceUtil.removeMdmNumberList(CommonMethod.getComponentName(getContext()));
                new WhiteListUtil(context, true).getData();
                break;
            case R.id.rl_exit:
                DataUtil.setLogin(context, false);
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.rl_setnfc:
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.rl_exit1:
                Intent intent1 = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.rl_clock:
                Intent intent2 = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.deskclock");
                if (intent2 != null) {
                    // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
                    startActivity(intent2);
                } else {
                    // 未安装应用
                }
                break;
            case R.id.rl_exit18:
//                File file = new File(Environment.getExternalStorageDirectory() + "/Police/", "treeUser.txt");
//                if (file.exists()) {
//                    Toast.makeText(context, "正在同步电话簿，请稍等", Toast.LENGTH_LONG).show();
//                    phoneJosn = FileUtil.getFile("treeUser.txt");
//                    Log.d("phoneJosn=", phoneJosn);
//                    addUser(phoneJosn);
//                } else {
//                    Toast.makeText(context, "请先同步白名单", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.rl_set_mymessage1:
                final EditText input = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("请输入密码")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(input)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String result = input.getText().toString();//获取密码输入框输入值
                                if (result.equals("admin@123")) {
                                    CommonMethod.sendStatus(false, getContext());
                                    PhoneInterfaceUtil.exitApp(getContext(), admin);
                                } else {
                                    Toast.makeText(getContext(), "密码错误", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.show();//显示dialog对话框
                break;
        }
    }


    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    public static void BatchAddContact(List<Tb_contacts> list) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (Tb_contacts contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation.newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.NUMBER, contact.getNumber())
                    .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                    .withValue(Phone.LABEL, "").withYieldAllowed(true).build());
        }
        if (ops != null) {
            // 真正添加
            ContentProviderResult[] results = mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            for (ContentProviderResult result : results) {


            }
        }
    }

}
