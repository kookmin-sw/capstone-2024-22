package com.capstone.android.application.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragmentDataBinding <B : ViewDataBinding>(private val bind:(View) -> B,
                                                             @LayoutRes val layoutResId: Int) : Fragment(layoutResId){
    private lateinit var _binding: B
        private set

    private val toastList = arrayListOf<Toast>()
    private val snackBarList= arrayListOf<Snackbar>()

    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,layoutResId,container,false)
        return binding.root
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






}