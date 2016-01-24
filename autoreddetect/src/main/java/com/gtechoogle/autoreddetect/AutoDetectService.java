package com.gtechoogle.autoreddetect;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gtechoogle.autoreddetect.utils.Utils;
import com.gtechoogle.autoreddetect.wechat.DetectProcess;

import java.util.List;

/**
 * Created by edy on 2016/1/19.
 */
public class AutoDetectService extends AccessibilityService {
//    private static String CONVERSATION_LIST_ID = "com.tencent.mm:id/ny";
//    private static String MONEY_PKG_ID = "com.tencent.mm:id/b_";

    private boolean isOnLauncherUI = false;
    AccessibilityNodeInfo mClickedNodeInfo;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (MainActivity.getSwitchState() == Utils.OFF) {
            Utils.ServiceLog("Turned off, no monitor");
            return;
        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Utils.ServiceLog("eventType TYPE_NOTIFICATION_STATE_CHANGED");
                detectNotification(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = event.getClassName().toString();
                Utils.ServiceLog("className = " + className);
                isOnLauncherUI = DetectProcess.isWeChatLauncher(className);
                if (isOnLauncherUI) {
                    openMoneyPacket();
                } else if (DetectProcess.isMoneyReceiverUIOpen(className)) {
                    unpackMoneyPacket();
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                if (isOnLauncherUI) {
//                    Utils.ServiceLog("eventType TYPE_WINDOW_CONTENT_CHANGED");
//                    openMoneyPacket();
//                }
                break;
        }
    }

    private void detectNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                Utils.ServiceLog("content = " + content);
                if (content.contains(DetectProcess.NOTIFICATION_KEY_WORD)) {
                    clickNotification(event);
                }
            }
        }
    }
    private void clickNotification(AccessibilityEvent event) {
        if (event.getParcelableData() != null &&
                event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

//

    private void openMoneyPacket() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> listInfo = rootNode.findAccessibilityNodeInfosByViewId(DetectProcess.CONVERSATION_LIST_ID);
        Utils.ServiceLog("listInfo = " + listInfo);
        if (!listInfo.isEmpty()) {
            AccessibilityNodeInfo listNodeInfo = listInfo.get(0);
            Utils.ServiceLog("listNodeInfo = " + listNodeInfo);
            AccessibilityNodeInfo moneyPacketNodeInfo = findMoneyPacketInfo(listNodeInfo);
            Utils.ServiceLog("moneyPacketNodeInfo = " + moneyPacketNodeInfo);
            clickMoneyPacket(moneyPacketNodeInfo);
        }
    }

    private void clickMoneyPacket(AccessibilityNodeInfo moneyPacketNodeInfo) {
        if (isNewMoneyPacket(moneyPacketNodeInfo)) {
            //mClickedNodeInfo = moneyPacketNodeInfo;
            AccessibilityNodeInfo parent = moneyPacketNodeInfo;
            while (parent != null) {
                if(parent.isClickable()){
                    Utils.ServiceLog("Click the item");
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                } else {
                    parent = parent.getParent();
                }
            }
        }
    }

    private boolean isNewMoneyPacket(AccessibilityNodeInfo parent) {
        if (mClickedNodeInfo == null) {
            Utils.ServiceLog("mClickedNodeInfo = " + mClickedNodeInfo);
            return true;
        }
        Utils.ServiceLog("mClickedNodeInfo = " + mClickedNodeInfo.hashCode() + " parent = " +  parent.hashCode());
        return mClickedNodeInfo.hashCode() != parent.hashCode();
    }

    private AccessibilityNodeInfo findMoneyPacketInfo(AccessibilityNodeInfo info) {
        Utils.ServiceLog("getChildCount = " + info.getChildCount() + " with info = " + info);
        int childNum = info.getChildCount();
        AccessibilityNodeInfo moneyInfo;
        if (childNum > 0) {
            for (int i = childNum - 1; i >=0; i--) {
                Utils.ServiceLog("************");
                moneyInfo = findMoneyPacketInfo(info.getChild(i));
                if (moneyInfo != null) {
                    Utils.ServiceLog("find the money packet moneyInfo = " + moneyInfo);
                    return moneyInfo;
                }
            }
        } else {
            if (info.getText()!= null &&
                    info.getText().toString().equals(DetectProcess.MONEY_PACKET_KEY_WORD)) {
                return info;
            }
        }
        return null;
    }


    private void unpackMoneyPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            Utils.ServiceLog("nodeInfo text = " + nodeInfo.getText());
            List<AccessibilityNodeInfo> list = findOpenNodeInfo(nodeInfo);
            for (AccessibilityNodeInfo n : list) {
                if (n.isClickable()) {
                    n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }
    private List<AccessibilityNodeInfo> findOpenNodeInfo(AccessibilityNodeInfo nodeInfo) {
        Utils.ServiceLog("findOpenNodeInfo");
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(DetectProcess.MONEY_BT_ID2);
        if (list.isEmpty()) {
            list = nodeInfo.findAccessibilityNodeInfosByViewId(DetectProcess.MONEY_BT_ID1);
        }
        return list;
    }
    @Override
    public void onInterrupt() {

    }
}
