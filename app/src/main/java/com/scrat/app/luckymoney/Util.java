package com.scrat.app.luckymoney;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by yixuanxuan on 2016/12/27.
 */

public class Util {
    private Util() {

    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isViewIdExist(AccessibilityNodeInfo rootNode, String viewId) {
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(viewId);
        return nodes != null && nodes.size() > 0;
    }

    public static boolean isTextExist(AccessibilityNodeInfo rootNode, String text) {
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
        return nodes != null && nodes.size() > 0;
    }

    public static boolean clickChild(AccessibilityNodeInfo node) {
        if (node == null)
            return false;

        int count = node.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo currNode = node.getChild(i);
            if (currNode.performAction(AccessibilityNodeInfo.ACTION_CLICK))
                return true;
        }
        return false;
    }

    public static boolean clickParentByText(AccessibilityNodeInfo rootNode, String text) {
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
        if (nodes == null || nodes.size() == 0)
            return false;

        for (AccessibilityNodeInfo nodeInfo : nodes) {
            if (clickParent(nodeInfo))
                return true;
        }
        return false;
    }

    public static boolean clickParent(AccessibilityNodeInfo rootNode) {
        AccessibilityNodeInfo currParent = rootNode;
        while (currParent != null) {
            if (currParent.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                return true;
            }
            currParent = currParent.getParent();
        }
        return false;
    }

//    private void setText(AccessibilityNodeInfo textNode, String content) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Bundle arguments = new Bundle();
//            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content);
//            textNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
//        }
//    }
//
//    private void appendText(AccessibilityNodeInfo textNode, String content) {
//        ClipboardManager clipboard = (ClipboardManager) MyApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//        textNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
//        ClipData clip = ClipData.newPlainText("label", content);
//        clipboard.setPrimaryClip(clip);
//        textNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);
//    }
}
