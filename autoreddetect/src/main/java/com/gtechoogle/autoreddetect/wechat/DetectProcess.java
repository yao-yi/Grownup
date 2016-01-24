package com.gtechoogle.autoreddetect.wechat;

/**
 * Created by edy on 2016/1/23.
 */
public class DetectProcess {
    public static String NOTIFICATION_KEY_WORD = "[微信红包]";
    public static String MONEY_PACKET_KEY_WORD = "领取红包";
    public static String LAUNCHER_PKG_NAME = "com.tencent.mm.ui.LauncherUI";
    public static String CONVERSATION_LIST_ID = "com.tencent.mm:id/ny";
    public static String RECEIVE_MONEY__PKG_NAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static String MONEY_BT_ID1 = "com.tencent.mm:id/b2c";
    public static String MONEY_BT_ID2 = "拆红包";
    public static boolean isWeChatLauncher(String pkgName) {
        return pkgName.equals(LAUNCHER_PKG_NAME);
    }
    public static boolean isMoneyReceiverUIOpen(String pkgName) {
        return pkgName.equals(RECEIVE_MONEY__PKG_NAME);
    }
}
