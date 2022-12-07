package com.kosa.thirdprojectfront

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kosa.thirdprojectfront.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // 런타임 권한 요청 시 필요한 요청 코드
    private val PERMISSIONS_REQUEST_CODE = 100
    // 권한 목록
    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    // GPS 서비스 요청 시 필요한 런처
    lateinit var  getGPSPermissionLauncher: ActivityResultLauncher<Intent>

    // 위도경도 - 사용하는 fragment에 선언하기??
    lateinit var locationProvider:LocationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()


        // 권한 확인 (gps)
        checkAllPermissions()
        //checkDistance()
    }

    // 프래그먼트 안의 버튼 클릭으로 프래그먼트 전환
    open fun onFragmentChange(index: Int) {
        if (index == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.createQRFrameLayout, HomeFragment())
                .commit()
        } else if (index == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.createQRFrameLayout, HomeFragment())
                .commit()
        } else if (index == 2) {
            supportFragmentManager.beginTransaction().replace(R.id.emptyQRFrameLayout, HomeFragment())
                .commit()
        } else if (index == 3) {
            supportFragmentManager.beginTransaction().replace(R.id.fr_login, HomeFragment())
                .commit()
        } else if (index == 4) {
            supportFragmentManager.beginTransaction().replace(R.id.home_main, ReservationFragment())
                .commit()
        }
    }

    private fun initBottomNavigation() {

        var text = "2022-12-06"

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, HomeFragment())
            .commitAllowingStateLoss()

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameLayout, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.loginFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFrameLayout, LoginFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> {
                    if (text != ""){
                        Log.d("CreateQR", "실행중")
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.mainFrameLayout, CreateQRFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                    else {
                        Log.d("EmptyQR", "실행중")
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.mainFrameLayout, EmptyQRFragment())
                            .commitAllowingStateLoss()
                        return@setOnItemSelectedListener true
                    }
                }
            }
            false
        }
    }


    // 권한 확인
    fun checkAllPermissions() {
        // GPS 켜져있는지 확인
        if (!isLocationServicesAvailable()) {
            Log.d("permission","isLocationServicesAvailable 사용불가라 설정 옵션열기")
            showDialogForLocationServiceSetting();
        } else { // 런타임 앱 권한이 허용되어있는지 확인
            Log.d("permission","isLocationServicesAvailable 사용가능 -> 런타임 권한확인")
            isRunTimePermissionsGranted();
        }
    }


    // GPS 켜져있는지 확인
    fun isLocationServicesAvailable(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        // 네트워크 권한 추가
    }

    // 위치 권한 가지고 있는지 체크 fun
    fun isRunTimePermissionsGranted() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        // 둘중에 권한이 하나도 없으면
        if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
            hasCoarseLocationPermission !=PackageManager.PERMISSION_GRANTED){
            Log.d("permission","location 권한 둘중에 하나라도 없다")
            // 권한 요청하기
            ActivityCompat.requestPermissions(this@MainActivity,
                REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSIONS_REQUEST_CODE &&
            grantResults.size==REQUIRED_PERMISSIONS.size){
            Log.d("permission","코드 일치, 개수일치?")
            // 요청 코드가 PERMISSIONS_REQUEST_CODE로 일치하고
            // 요청한 퍼미션 개수만큼 수신되었으면
            var checkResult = true

            // 모든 permission 체크
            for(result in grantResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    checkResult=false
                    break
                }
            }
            if(checkResult){
                // 위치값 가져올수있다
            } else{
                // 퍼미션이 거부되었을경우
                Toast.makeText(this@MainActivity,
                "permission이 거부되었습니다. 앱을 다시실행해주세요",
                Toast.LENGTH_LONG).show()
                //finish() // 앱종료
            }
        }
    }

    private fun showDialogForLocationServiceSetting() {
        // 먼저 activityResultLauncher를 설정.
        // 이 런처를 이용하여 결과값을 반환해야 하는 인텐트를 실행가능
        getGPSPermissionLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){result ->
            // 결과값을 받았을때 로직
            if(isLocationServicesAvailable()){
                //사용자가 GPS켰는지 확인
                if(isLocationServicesAvailable()){
                    isRunTimePermissionsGranted() // 런타임 권한 확인
                } else{
                    //위치 서비스가 허용되지 않았으면
                    Toast.makeText(this@MainActivity,
                    "위치 서비스를 사용할수없습니다",
                    Toast.LENGTH_LONG).show()
                    // 액티비티종료
                }
            }
        }
        
        // 사용자한테 의사를 물어보는 alertdialog
        val builder : AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("위치 서비스가 꺼져있습니다. 설정해주세요")
        builder.setCancelable(true)// 다이어로그 창 바깥 터치시 창 닫힘
        builder.setPositiveButton("설정",
        DialogInterface.OnClickListener{
            dialog, id -> // 확인버튼설정
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                getGPSPermissionLauncher.launch(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소",
        DialogInterface.OnClickListener{
            dialog, id ->
            dialog.cancel()
            Toast.makeText(this@MainActivity,
            "기기에서 GPS 설정후 사용바랍니다",
            Toast.LENGTH_SHORT).show()
            // 종료
        })
        builder.create().show() // 다이얼로그 생성
    }

    // 거리 구하기
    fun checkDistance() : Boolean{
        locationProvider = LocationProvider(this@MainActivity)

        val latitude:Double = locationProvider.getLocationLatitude() //위도
        val longitude:Double = locationProvider.getLocationLongitude()  //경도
        Log.d("location","위도 : ${latitude}   경도 : ${longitude}")
        // db에서 값 가져오기
        //더현대 서울 37.525, 126.928
        val distance = DistanceManager.getDistance(latitude,longitude,37.525,126.928)
        if(distance<500){
            // 지점 예약가능
            Toast.makeText(this@MainActivity,
                "${distance}M 차이납니다",
                Toast.LENGTH_SHORT).show()
            return true
        }else{
            // 예약못하게 막기
            Toast.makeText(this@MainActivity,
                "${distance}M 차이납니다 예약이 불가능합니다",
                Toast.LENGTH_SHORT).show()
            return false
        }
    }
}