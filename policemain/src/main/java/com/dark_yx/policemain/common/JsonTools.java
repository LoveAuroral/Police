package com.dark_yx.policemain.common;

import android.util.Log;

import com.dark_yx.policemain.entity.User;
import com.dark_yx.policemaincommon.Util.SystemInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class JsonTools {


	public static List<User> getUsers(String jsonString) {
		Log.d("jsonString", jsonString);
		List<User> list = new ArrayList<User>();
		try {
			 JSONArray jsonArray = new JSONArray(jsonString);  
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				User user = new User();
				user.setiMEI(jsonObject.getString("IMEI"));

				user.setRealName(jsonObject.getString("RealName"));
				user.setUserName(jsonObject.getString("UserName"));
				Log.d("PhoneMainActivity","本机:"+SystemInfo.GetIMEI(x.app()));
				Log.d("PhoneMainActivity","用户:"+user.getiMEI());
				if(!user.getiMEI().equals(SystemInfo.GetIMEI(x.app()))){
				list.add(user);
				}
			}
		} catch (Exception e) {
		}
		return list;
	}



}
