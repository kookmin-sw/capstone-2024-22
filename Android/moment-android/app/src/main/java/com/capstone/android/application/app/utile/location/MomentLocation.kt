package com.capstone.android.application.app.utile.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class MomentLocation @Inject constructor(@ActivityContext private val context : Context){
    @SuppressLint("MissingPermission")
    fun getLocation():() -> Task<Location> {
        return {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                }
            }
            .addOnFailureListener { fail ->
                Log.d("ewagawegawe",fail.message.toString())
            }
        }
    }

}