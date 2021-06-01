package com.worth.framework.base.view.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.worth.framework.R
import com.worth.framework.base.core.base.activity.BaseActivity
import com.worth.framework.base.core.base.constants.DIALOG_ACTIVITY
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

    }

    override fun initViewAndListener() {
//        binding.ok.setOnClickListener { finish() }
    }

    override fun initData() {

    }


}