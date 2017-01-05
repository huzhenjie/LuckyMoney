package com.scrat.app.luckymoney;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by yixuanxuan on 16/8/22.
 */
public class LuckyMoneyAccessibility extends AccessibilityService {

    private static final String LUNCHER_CLASSNAME = "com.tencent.mm.ui.LauncherUI";
    private static final String LISTVIEW_CLASSNAME = "android.widget.ListView";
    private static final String RECEIVE_LUCK_MONEY_CLASSNAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        log("onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null)
            return;

        int eventType = event.getEventType();
        final AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null)
            return;

//        log("type = " + eventType + " ; " + event.getClassName().toString() + " " + isGetLuckyMoney);

        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
                clickNotification(event);
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                if (LISTVIEW_CLASSNAME.equals(event.getClassName().toString())) {
                    getPacket(rootNode);// 领取红包
                }
                break;
        }
        openPacket(rootNode);// 打开红包
    }

    @Override
    public void onInterrupt() {
        log("onInterrupt");
    }

    private void log(String content) {
//        Log.e("Accessibility", content);
    }

    private boolean clickNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (texts == null || texts.isEmpty())
            return false;

        for (CharSequence text : texts) {
            if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification && text != null && text.toString().contains("[微信红包]")) {
                try {
                    Notification notification = (Notification) event.getParcelableData();
                    PendingIntent pendingIntent = notification.contentIntent;
                    pendingIntent.send();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private synchronized boolean getPacket(AccessibilityNodeInfo rootNode) {
        if (Util.isTextExist(rootNode, "领取红包")) {
            log("领取红包");
            List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText("领取红包");
            for (int i = nodes.size() - 1; i >= 0; i--) {
                AccessibilityNodeInfo node = nodes.get(i);
                if (Util.clickParent(node))
                    return true;
            }
        }
        return false;
    }

    private synchronized void openPacket(AccessibilityNodeInfo rootNode) {
        String text = "发了一个红包，金额随机";
        if (!Util.isTextExist(rootNode, text)) {
            text = "给你发了一个红包";
            if (!Util.isTextExist(rootNode, text))
                return;
        }

        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
        if (nodes == null || nodes.isEmpty())
            return;

        AccessibilityNodeInfo parentNode = nodes.get(0).getParent();
        if (Util.clickChild(parentNode)) {
            log("打开红包");
        }
    }
}
