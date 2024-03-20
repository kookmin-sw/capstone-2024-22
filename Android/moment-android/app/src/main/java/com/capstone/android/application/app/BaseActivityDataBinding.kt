package com.capstone.android.application.app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivityDataBinding <B : ViewDataBinding>(val resId:Int) : AppCompatActivity(){
    protected lateinit var binding: B
        private set

    private val toastList = arrayListOf<Toast>()
    private val snackBarList= arrayListOf<Snackbar>()
//    lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,resId)

    }


    override fun onStop() {
        for (toast in toastList) {
            toast.cancel()
        }
        toastList.clear()

        for(snackBar in snackBarList){
            snackBar.anchorView=null
        }

        snackBarList.clear()

        super.onStop()
    }


    fun showCustomToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
        toastList.add(toast)
    }


    fun showLoadingDialog(context: Context) {
//        mLoadingDialog = LoadingDialog(context)
//        mLoadingDialog.show()
    }

    fun showSnackBar(message:String){

//        val snackBar= Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
//
//        snackBar.setTextColor(resources.getColor(R.color.main_blue_light,null))
//        snackBar.view.background=resources.getDrawable(R.drawable.snackbar_bg,null)
//        val params=(snackBar.view.getLayoutParams() as FrameLayout.LayoutParams).also {
//            it.gravity= Gravity.TOP
//        }
//        snackBar.view.setLayoutParams(params)
//
//        snackBar.view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
//        snackBar.show()

    }

    fun dismissLoadingDialog() {
//        if (mLoadingDialog.isShowing) {
//            mLoadingDialog.dismiss()
//        }
    }



}