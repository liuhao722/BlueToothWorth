package com.worth.framework.base.core.utils

import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.core.view.iterator


/**
 * 复制黏贴辅助
 * @author LiuHao
 */
class SystemMenuHelper {

    companion object {
        // 需要删除的弹窗部分 限制
        const val ID_COPY = android.R.id.copy
        const val ID_PASTE = android.R.id.paste
        const val ID_SHARE = android.R.id.shareText
        const val ID_CUT = android.R.id.cut

        /**
         * 关闭粘贴
         */
        fun disableSystemMenuCopy(editText: EditText?) {
            disableSystemMenu(editText, false, ID_PASTE)
        }

        /**
         * 关闭指定的功能，可全关闭或者单独的关闭
         * @param editText 文本框
         * @param displayAllSystemMenu 是否关闭所有的系统弹窗，只有在不关闭的情况下 具体的删除某个id才有效
         * @param ids 需要删除的弹出复制 黏贴框 自行增加对应的id 如上面
         */
        fun disableSystemMenu(editText: EditText?, displayAllSystemMenu: Boolean, vararg ids: Int) {
            editText?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    customInsertionActionModeCallback = object : ActionMode.Callback {
                        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                            return !displayAllSystemMenu
                        }

                        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                            menu?.run {
                                var iter = iterator()
                                while (iter.hasNext()) {
                                    var item = iter.next()
                                    var itemId = item.itemId
                                    ids?.forEach {
                                        if (it == itemId) {
                                            removeItem(it)
                                        }
                                    }
                                }
                            }
                            return false
                        }

                        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                            ids?.forEach {
                                if (it == ID_PASTE && item?.itemId == ID_PASTE) {                   //  屏蔽粘贴按钮
                                    mode?.finish()
                                    return true
                                }
                            }
                            return false
                        }

                        override fun onDestroyActionMode(mode: ActionMode) {
                            L.d("onDestroyActionMode")
                        }
                    }
                }

                customSelectionActionModeCallback = object : ActionMode.Callback {
                    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                        return !displayAllSystemMenu
                    }

                    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                        menu?.run {
                            var iter = iterator()
                            while (iter.hasNext()) {
                                var item = iter.next()
                                var itemId = item.itemId
                                ids?.forEach {
                                    if (it == itemId) {
                                        removeItem(it)
                                    }
                                }
                            }
                        }
                        return false
                    }

                    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                        ids?.forEach {
                            if (it == ID_PASTE && item?.itemId == ID_PASTE) {                       //  屏蔽粘贴按钮
                                mode?.finish()
                                return true
                            }
                        }
                        return false
                    }

                    override fun onDestroyActionMode(mode: ActionMode) {
                        L.d("onDestroyActionMode")
                    }
                }
            }
        }
    }
}