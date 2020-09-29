package com.dark_yx.policemain.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dark_yx.policemain.R;
import com.dark_yx.policemaincommon.Models.AppInfo;
import com.dark_yx.policemaincommon.Util.DataUtil;
import com.dark_yx.policemaincommon.Util.DbHelp;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;

/**
 * Created by lmp on 2016/4/8.
 */
public class AppsAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> infos;
    private int appSize;
    private BadgeView badgeView;
    private PackageManager pm;

    public AppsAdapter(Context context, List<AppInfo> infos) {
        this.context = context;
        this.infos = infos;
        if (infos != null) {
            appSize = infos.size();
        } else {
            appSize = 0;
        }
        pm = context.getPackageManager();
    }

    public List<AppInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<AppInfo> infos) {
        this.infos = infos;
    }

    public final int getCount() {
        return appSize + 1;
    }

    public final Object getItem(int position) {
        return infos.get(position);
    }

    public final long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.appLabel = (TextView) convertView.findViewById(R.id.textView1);
            holder.rlApps = (RelativeLayout) convertView.findViewById(R.id.rl_apps);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        badgeView = new BadgeView(context, holder.rlApps);
        if (position < appSize) {
            new BitmapWorkerTask(holder, infos.get(position).getPackageName()).execute();
            holder.appLabel.setText(infos.get(position).getName());
            if (infos.get(position).getName().equals("排班管理")) {
                DbHelp db = new DbHelp();
                //显示排班表，日志待办，换班待处理的消息个数
                int a;
                int b;
                if (db.getUnReadDiary() == null) {
                    a = 0;
                } else {
                    a = db.getUnReadDiary().size();
                }
                if (db.getUnReadExchange() == null) {
                    b = 0;
                } else {
                    b = db.getUnReadExchange().size();
                }
                int number = a + b;
                if (number != 0) {
                    badgeView.setText(String.valueOf(number));
                    badgeView.show();
                }
            }
        }
        if (position == appSize) {
            holder.imageView2.setVisibility(!DataUtil.isNoticeRead() ? View.VISIBLE : View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView appIcon, imageView2;
        TextView appLabel;
        RelativeLayout rlApps;
    }

    /*
      * 获取程序 图标
      */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable getAppIcon(String packname) {
        Drawable drawable = null;
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            drawable = info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            drawable = context.getDrawable(R.drawable.down_load_icon);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    private class BitmapWorkerTask extends AsyncTask<Void, Void, Drawable> {
        ViewHolder holder;
        String packname;

        private BitmapWorkerTask(ViewHolder holder, String packname) {
            this.holder = holder;
            this.packname = packname;
        }


        @Override
        protected Drawable doInBackground(Void... params) {
            return getAppIcon(packname);
        }

        @Override
        protected void onPostExecute(Drawable bitmap) {
            holder.appIcon.setImageDrawable(bitmap);
        }
    }
}