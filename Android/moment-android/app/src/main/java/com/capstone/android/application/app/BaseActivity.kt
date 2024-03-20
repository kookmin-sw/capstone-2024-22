package com.capstone.android.application.app

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<B : ViewBinding> (private val inflate:(LayoutInflater) -> B) : AppCompatActivity() {
    protected lateinit var binding: B
        private set

    private val toastList = arrayListOf<Toast>()
    private val snackBarList = arrayListOf<Snackbar>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // recovering the instance state
//        gameState = savedInstanceState?.getString(GAME_STATE_KEY)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }


    // Invoked when the act ivity might be temporarily destroyed; save the instance state here.
    /*
       호출 포인트 : onDestroy가 호출이 되면 인스턴스를 저장하기 위해서 해당 메서드가 호출이 된다.
     */
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {

        // Bundle에 인스턴스를 저장한다. 저장된 인스턴스는 onCreate의 파라미터로 전달된다.
//        outState?.run {
//            putString(GAME_STATE_KEY, gameState)
//            putString(TEXT_VIEW_KEY, textView.text.toString())
//        }
        super.onSaveInstanceState(outState, outPersistentState)
    }


    override fun onStop() {
        for (toast in toastList) {
            toast.cancel()
        }
        toastList.clear()

        for (snackBar in snackBarList) {
            snackBar.anchorView = null
        }

        snackBarList.clear()

        super.onStop()
    }


    fun showCustomToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
        toastList.add(toast)
    }


//    fun showLoadingDialog(context: Context) {
//
//    }
//
//    fun dismissLoadingDialog() {
//
//    }


}
