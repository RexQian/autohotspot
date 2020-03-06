package com.tmindtech.autohotspot;

import android.support.annotation.DrawableRes;

/**
 * @author RexQian
 * @date 2020-03-06
 */
public class FrontServiceInfo {
    //点击前台服务后跳转的Activity
    public Class activityClass;
    //前台服务应用大图标
    @DrawableRes
    public int largeIcon;
    //前台服务应用小图标
    @DrawableRes
    public int smallIcon;
    //前台服务显示的标题 传入应用名
    public String appName;
    //前台服务显示的内容 一般是xxx正在运行
    public String runContent;
    //关闭服务按钮图标
    public int closeIcon;
    //关闭服务提示内容
    public String closeContent;

    public FrontServiceInfo(Class activityClass, int largeIcon, int smallIcon,
                            String appName, String runContent, int closeIcon, String closeContent) {
        this.activityClass = activityClass;
        this.largeIcon = largeIcon;
        this.smallIcon = smallIcon;
        this.appName = appName;
        this.runContent = runContent;
        this.closeIcon = closeIcon;
        this.closeContent = closeContent;
    }
}
