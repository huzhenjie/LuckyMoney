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
public class LuckyMoneyAccessilibity extends AccessibilityService {

    private static final String LUNCHER_CLASSNAME = "com.tencent.mm.ui.LauncherUI";
    private static final String RECEIVE_LUCK_MONEY_CLASSNAME = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    private boolean isGetLuckyMoney = false;
    private boolean isOpenLuckyMoney = false;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        log("onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:// 通知栏事件
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains("[微信红包]")) {
                            // 监听到微信红包的notification，打开通知
                            if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    isGetLuckyMoney = false;
                                    isOpenLuckyMoney = false;
                                    pendingIntent.send();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (!isGetLuckyMoney && LUNCHER_CLASSNAME.equals(event.getClassName().toString())) {
                    getPacket();// 领取红包
                } else if (!isOpenLuckyMoney && RECEIVE_LUCK_MONEY_CLASSNAME.equals(event.getClassName().toString())) {
                    openPacket();// 打开红包
                }
                break;
        }

    }

    @Override
    public void onInterrupt() {
        log("onInterrupt");
    }

    private void log(String content) {
        Log.e("Accessibility", content);
    }

    private synchronized void getPacket() {
        log("领取红包");
        final AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null)
            return;

        List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a13");
        for (int i = nodeInfos.size() - 1; i >= 0; i--) {
            AccessibilityNodeInfo nodeInfo = nodeInfos.get(i);
            if (nodeInfo.isClickable()) {
                List<AccessibilityNodeInfo> tmp = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
                if (tmp.size() > 0) {
                    log("点击 " + nodeInfo);
                    isGetLuckyMoney = true;
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }
    }

    private synchronized void openPacket() {
        final AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null)
            return;

        List<AccessibilityNodeInfo> infos = rootNode.findAccessibilityNodeInfosByText("看看大家的手气");
        if (infos.size() > 0) {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a13");
            for (AccessibilityNodeInfo info : nodeInfos) {
                if (info.isClickable()) {
                    log("关闭");
                    isOpenLuckyMoney = true;
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        } else {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ba_");
            for (AccessibilityNodeInfo info : nodeInfos) {
                if (info.isClickable()) {
                    log("打开红包");
                    isOpenLuckyMoney = true;
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    break;
                }
            }
        }

    }
}
