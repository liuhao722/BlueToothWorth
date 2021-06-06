package com.worth.framework.base.view.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.worth.framework.R
import com.worth.framework.base.core.base.activity.BaseActivity
import com.worth.framework.base.core.base.constants.ARouterPath.DIALOG_ACTIVITY
import com.worth.framework.base.core.utils.LDBus
import com.worth.framework.business.ext.EVENT_WITH_INPUT_ASR_RESULT
import com.worth.framework.business.ext.EVENT_WITH_USER_INPUT_RESULT
import com.worth.framework.databinding.SdkActivityDialogLayoutBinding

@Route(path = DIALOG_ACTIVITY, name = "全局的弹窗dialogActivity")
class DialogActivity : BaseActivity<SdkActivityDialogLayoutBinding>() {
    override fun initBefore() {

    }

    override fun injectARouter() {
        ARouter.getInstance().inject(this)
    }

    override fun initArgs() {

    }

    override fun initLayoutId(): Int {
        return R.layout.sdk_activity_dialog_layout
    }

    override fun initObserve() {
        LDBus.observer(EVENT_WITH_USER_INPUT_RESULT) {
            if (!isFinishing && !isDestroyed) {
                it?.run {                                   //  网络返回的查询结果

                }
            }
        }

        LDBus.observer(EVENT_WITH_INPUT_ASR_RESULT) {
            if (!isFinishing && !isDestroyed) {
                it?.run {                                   //  网络返回的查询结果

                }
            }
        }
    }

    override fun initViewAndListener() {
//        binding.ok.setOnClickListener { finish() }
    }

    override fun initData() {

    }


}