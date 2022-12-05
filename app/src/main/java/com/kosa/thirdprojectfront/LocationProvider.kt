package com.kosa.thirdprojectfront

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.core.content.ContextCompat


class LocationProvider(val context: Context) {
    // Location 위도, 경도, 고도와 같이 위치에 관련된 정보를 가지고 있는 클래스
    private var location : Location ?=null
    // LocationManager는 시스템 위치 서비스에 접근을 제공하는 클래스
    private var locationManager : LocationManager?=null

    init{
        getLocation();
    }

    private fun getLocation() : Location?{
        try{
            locationManager = context.getSystemService(
            Context.LOCATION_SERVICE) as LocationManager

            //var gpsLocation:Location?=null
            var isGPSEnabled:Boolean= locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if(!isGPSEnabled)return null // 사용 불가시 null
            else {
                // coarse 보다 더 정밀위치
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION)
                // 도시블럭단위
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION)

                // 두 권한이 없으면 null 반환
                if(hasFineLocationPermission!=PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission!=PackageManager.PERMISSION_GRANTED)
                    return null

                // gps로 위치파악
                if(isGPSEnabled){
                    location=locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
                return location
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return location
    }

    fun getLocationLatitude():Double{
        return location?.latitude?:0.0
    }
    fun getLocationLongitude():Double{
        return location?.longitude?:0.0
    }
}