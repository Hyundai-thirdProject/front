package com.kosa.thirdprojectfront

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * LocationProvider
 * @author 김민찬
 * <pre>
수정자                 수정내용
--------------   --------------------------------------
김민찬              최초 생성

 **/


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

            var gpsLocation : Location ?=null
            var networkLocation : Location ?=null


            //var gpsLocation:Location?=null
            var isGPSEnabled:Boolean= locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            var isNetworkEnabled:Boolean = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGPSEnabled && !isNetworkEnabled) {
                Log.d("location","gps, network 둘다 사용불가")
                return null
            }
            else {
                // coarse 보다 더 정밀위치
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION)
                // 도시블럭단위
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION)

                // 두 권한이 없으면 null 반환
                if(hasFineLocationPermission!=PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission!=PackageManager.PERMISSION_GRANTED) {
                    Log.d("location","두 권한 확인")
                    return null
                }

                // network로 위치파악
                if(isNetworkEnabled){
                    networkLocation=locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    Log.d("location","network로 값 받기" +
                            "${networkLocation?.latitude}, ${networkLocation?.longitude} ")
                }

                // gps로 위치파악
                if(isGPSEnabled){
                    gpsLocation=locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    Log.d("location","gps로 값 받기" +
                            "${gpsLocation?.latitude}, ${gpsLocation?.longitude} ")
                }

                if(gpsLocation != null && networkLocation !=null){
                    // 정확도 높은것을 선택
                    if(gpsLocation.accuracy > networkLocation.accuracy){
                        location=gpsLocation
                        return gpsLocation
                    }else{
                        location=networkLocation
                        return networkLocation
                    }
                }else{
                    if(gpsLocation!=null){
                        location = gpsLocation
                    }
                    if(networkLocation!=null){
                        location = networkLocation
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return location
    }

    // null 이면 0 반환
    fun getLocationLatitude():Double{
        return location?.latitude?:0.0
    }
    fun getLocationLongitude():Double{
        return location?.longitude?:0.0
    }
}