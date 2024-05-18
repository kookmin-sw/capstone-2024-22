package com.capstone.android.application.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.app.ApplicationClass.Companion.tokenSharedPreferences
import com.capstone.android.application.app.composable.MomentTextField
import com.capstone.android.application.data.remote.auth.auth_code.request.PostAuthAuthCodeRequest
import com.capstone.android.application.data.remote.auth.auth_code_confirm.request.PatchAuthAuthCodeConfirmRequest
import com.capstone.android.application.data.remote.auth.change_password.request.PatchAuthChangePasswordRequest
import com.capstone.android.application.data.remote.auth.login.request.PostAuthLoginRequest
import com.capstone.android.application.presentation.AuthViewModel
import com.capstone.android.application.presentation.CountViewModel
import com.capstone.android.application.ui.theme.BigButton
import com.capstone.android.application.ui.theme.CheckButton
import com.capstone.android.application.ui.theme.CountText
import com.capstone.android.application.ui.theme.FontMoment
import com.capstone.android.application.ui.theme.ImgBackButton
import com.capstone.android.application.ui.theme.P_Bold30
import com.capstone.android.application.ui.theme.P_ExtraBold16
import com.capstone.android.application.ui.theme.P_Medium11
import com.capstone.android.application.ui.theme.P_Medium14
import com.capstone.android.application.ui.theme.P_Medium18
import com.capstone.android.application.ui.theme.YJ_Bold15
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.negative_600
import com.capstone.android.application.ui.theme.neutral_500
import com.capstone.android.application.ui.theme.neutral_600
import com.capstone.android.application.ui.theme.primary_500
import com.capstone.android.application.ui.theme.tertiary_500
import com.capstone.android.application.ui.theme.white
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

enum class OnboardingScreen(){
    Login,
    LoginCompleteSplash,
    Signup,
    SignupEmail,
    SignupNumber,
    FindPassword,
    FindPasswordNumber,
    FindPasswordSignup,
    SignupComplete,
    AgreeDetail
}

@AndroidEntryPoint
class OnboardingActivity:ComponentActivity() {
    lateinit var navController: NavHostController
    private val authViewModel : AuthViewModel by viewModels()
    private var userId:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initApi()


        setContent{

            val authCodeInFindPassword = remember {
                mutableStateOf("")
            }

            val authCodeInSignup = remember {
                mutableStateOf("")
            }


            val isLoginActivity = remember{
                mutableStateOf(false)
            }


            val isEmaliWrite = remember{
                mutableStateOf(false)
            }

            val isAuthCodeWrite = remember{
                mutableStateOf(false)
            }

            val isFindPasswordWrite = remember {
                mutableListOf(false)
            }

            val email = remember{mutableStateOf("")}
            val FindPW_id = remember{ mutableStateOf("") }

            navController = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {

                }
            ) { innerPadding ->
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController, startDestination = OnboardingScreen.Login.name
                ){
                    composable(route=OnboardingScreen.Login.name){ Login() }
                    composable(route=OnboardingScreen.LoginCompleteSplash.name){ LoginCompleteSplash()}
                    composable(route=OnboardingScreen.SignupEmail.name){ SignupEmail(email) }
                    composable(route=OnboardingScreen.SignupNumber.name){
                        SignupNumber(authCode=authCodeInSignup, email = email)
                    }
                    composable(route=OnboardingScreen.Signup.name){ Signup(authCodeInSignup,email) }
                    composable(route=OnboardingScreen.FindPassword.name){ FindPassword(FindPW_id) }
                    composable(route=OnboardingScreen.FindPasswordNumber.name){
                        FindPasswordNumber(authCodeInFindPassword, FindPW_id)
                    }
                    composable(route=OnboardingScreen.FindPasswordSignup.name){
                        FindPasswordSignup(code = authCodeInFindPassword.value)
                    }
                    composable(route=OnboardingScreen.SignupComplete.name){ SignupComplete() }
                    composable(route=OnboardingScreen.AgreeDetail.name){ AgreeDetail() }
                }
            }
        }

    }

    private fun initApi(){
        // 로그인 성공
        authViewModel.postAuthLoginSuccess.observe(this@OnboardingActivity){ response->
            tokenSharedPreferences.edit().putString("accessToken",response.data.accessToken).apply()
            navController.navigate(OnboardingScreen.LoginCompleteSplash.name)
        }

        // 로그인 실패
        authViewModel.postAuthLoginFailure.observe(this@OnboardingActivity){ response->
            Toast.makeText(this@OnboardingActivity,"로그인에 실패",Toast.LENGTH_SHORT).show()
        }

        // 인증코드 요청
        authViewModel.postAuthAuthCodeSuccess.observe(this@OnboardingActivity){ response->
            tokenSharedPreferences.edit().putString("accessToken",response.data.accessToken).apply()
            if(navController.currentDestination?.route == OnboardingScreen.SignupEmail.name){
                navController.navigate(OnboardingScreen.SignupNumber.name)
            }

            if(navController.currentDestination?.route == OnboardingScreen.FindPassword.name){
                navController.navigate(OnboardingScreen.FindPasswordNumber.name)
            }

            userId=response.data.userId
        }

        // 인증코드 요청 실패
        authViewModel.postAuthAuthCodeFailure.observe(this@OnboardingActivity){ response->
            Toast.makeText(this@OnboardingActivity,"인증코드요청에 실패했습니다.",Toast.LENGTH_SHORT).show()

        }

        // 인증코드 확인
        authViewModel.patchAuthAuthCodeConfirmSuccess.observe(this@OnboardingActivity){ response ->
            tokenSharedPreferences.edit().putString("accessToken",response.data.accessToken).apply()
            if(navController.currentDestination?.route == OnboardingScreen.SignupNumber.name){
                navController.navigate(OnboardingScreen.Signup.name)
            }

            if(navController.currentDestination?.route == OnboardingScreen.FindPassword.name){
                navController.navigate(OnboardingScreen.Login.name)
            }
        }

        // 인증코드 확인 실패
        authViewModel.patchAuthAuthCodeConfirmFailure.observe(this@OnboardingActivity){ response->
            Toast.makeText(this@OnboardingActivity,"인증번호가 틀렸습니다.",Toast.LENGTH_SHORT).show()

        }

        // 비밀번호 변경
        authViewModel.patchAuthChangePasswordSuccess.observe(this@OnboardingActivity){ response->
            navController.navigate(OnboardingScreen.Login.name)
        }

        // 비밀번호 변경 실패
        authViewModel.patchAuthChangePasswordFailure.observe(this@OnboardingActivity){ response->
            Toast.makeText(this@OnboardingActivity,"서버오류.",Toast.LENGTH_SHORT).show()
        }




    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun Login(){
        val id = remember{
            mutableStateOf("")
        }
        val password = remember{
            mutableStateOf("")
        }
        var idpwState = remember{
            mutableStateOf(true)
        }
        val autologin = remember{
            mutableStateOf(false)
        }

        val focusRequester =  remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {startActivity(Intent(this@OnboardingActivity, SplashActivity::class.java).putExtra("MoveScreen","intro"))}, "로그인")
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center){

                Column( modifier = Modifier
                    .padding(horizontal = 8.dp)){
                    P_Medium11("아이디", if(idpwState.value)black else negative_600)}
                Spacer(modifier = Modifier.height(4.dp))

                MomentTextField(
                    hint = "이메일을 입력해주세요",
                    onValueChanged = { id.value=it},
                    onClicked = {} ,
                    text = id,
                    keyboardType = KeyboardType.Text,
                    changecolor = black,
                    focusRequester = focusRequester,
                    move = "manyfirstmove",
                    focusManager = focusManager
                )
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = if(idpwState.value)black else negative_600)

                if(idpwState.value){
                }else{
                    Spacer(modifier = Modifier.height(4.dp))
                    Column( modifier = Modifier.padding(horizontal = 8.dp)){
                        P_Medium11(content = "앗 ! 가입되어 있지 않은 아이디에요", color = negative_600)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Column( modifier = Modifier
                    .padding(horizontal = 8.dp)){
                    P_Medium11("비밀번호", if(idpwState.value)black else negative_600)}
                Spacer(modifier = Modifier.height(4.dp))
                MomentTextField(
                    hint = "비밀번호를 입력해주세요",
                    onValueChanged = {password.value=it },
                    onClicked = {} ,
                    text = password,
                    keyboardType = KeyboardType.Password,
                    changecolor = black,
                    focusRequester = focusRequester,
                    move = "manyendmove",
                    focusManager = focusManager
                )

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = if(idpwState.value)black else negative_600)
                if(idpwState.value){
                }else{
                    Spacer(modifier = Modifier.height(4.dp))
                    Column( modifier = Modifier.padding(horizontal = 8.dp)){
                        P_Medium11(content = "앗 ! 틀린 비밀번호에요", color = negative_600)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier
                        .width(76.dp)
                        .clickable { navController.navigate(OnboardingScreen.FindPassword.name) }) {
                        Column(modifier = Modifier
                            .padding(horizontal = 8.dp)) {
                            P_Medium11(
                                content = "비밀번호 찾기",
                                color = black
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(color = black)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.clickable {
                        tokenSharedPreferences.edit().putString("accessToken","Bearer " +
                                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                                "eyJzdWIiOiJNb21lbnQiLCJpc3MiOiJNb21lbnQiLCJ1c2VySWQiOjEsInJvbGUiOiJST0xFX0FVVEhfVVNFUiIsImlhdCI6MTcxNTA4NzY0NCwiZXhwIjoxNzU4Mjg3NjQ0fQ" +
                                ".6n5w1g87Pyoy11yyzsrxtwpdFHiGMzdjn56EgzFc9gc"
                        ).apply()
                        if(!tokenSharedPreferences.getString("accessToken","").isNullOrEmpty()){
                            startActivity(Intent(this@OnboardingActivity,MainActivity::class.java))
                            finish()
                        }
                    },
                    text = "무료체험하기",
                    textDecoration = TextDecoration.Underline,
                    fontFamily = FontMoment.preStandardFont,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (id.value.isNotEmpty() && password.value.isNotEmpty()){

                    BigButton("로그인하기", true,
                        onClick = {
                            authViewModel.postAuthLogin(
                                body = PostAuthLoginRequest(
                                    email = id.value,
                                    password = password.value
                                )
                            )
//                            navController.navigate(OnboardingScreen.LoginComplete.name)
                        }
                    )

                }else{
                    BigButton("로그인하기", false, onClick = {startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))})
                }
                Spacer(modifier = Modifier.height(8.dp))
                BigButton("가입하기", true) { navController.navigate(OnboardingScreen.SignupEmail.name) }
            }
        }
    }

    @Composable
    fun LoginCompleteSplash(){
        LoginComplete()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@OnboardingActivity,MainActivity::class.java))
            finish()
        }, 1000)
    }

    @Composable
    fun LoginComplete(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(249.dp)
                    .height(46.dp)
            ) {
                Image(modifier = Modifier.fillMaxSize(),
                    painter =  painterResource(R.drawable.img_logo),
                    contentDescription = "LOGO")
            }
            Column(modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 112.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                P_Medium14(content = "반가워요!", color = black)
                //P_Medium14(content = "사용자이메일@naver.com", color = black)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SignupEmail(email : MutableState<String>){
        //keyboard
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        //bottomsheet
        val sheetState = rememberModalBottomSheetState()
        val recordOpen = remember { mutableStateOf(false)}

        val agree1 = remember { mutableStateOf(false) }
        val agree2 = remember { mutableStateOf(false) }
        val agree3 = remember { mutableStateOf(false) }
        if(recordOpen.value){
            BottomNavigatesheet(recordOpen, agree1, agree2, agree3,
                sheetState = sheetState, closeSheet = {recordOpen.value = false})
        }

        //agree
        val agree = remember { mutableStateOf(false) }
        if(agree1.value && agree2.value){
            if (agree3.value) agree.value = true else agree.value = true }
        else{ agree.value = false }

        //data state
        val emailState = remember{ mutableStateOf(true) }


        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = tertiary_500)
            .padding(horizontal = 20.dp)) {

            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {
                    navController.navigate(OnboardingScreen.Login.name)
                }, "회원가입")
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11("이메일", if(emailState.value)black else negative_600)
                }
                Spacer(modifier = Modifier.height(4.dp))
                MomentTextField(
                    hint = "인증가능한 이메일을 입력해주세요",
                    onValueChanged = { email.value = it },
                    onClicked = {},
                    text = email,
                    keyboardType = KeyboardType.Text,
                    changecolor = black,
                    focusRequester = focusRequester,
                    move = "onemove",
                    focusManager = focusManager
                )
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = if(emailState.value)black else negative_600)
                Spacer(modifier = Modifier.height(8.dp))
                if(emailState.value){  }
                else{
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ){
                        P_Medium11(content = "앗 ! 올바른 이메일 형식이 아니에요", color = negative_600)
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11(content = "해당 이메일은 회원가입 시 본인인증수단으로서 활용되며\n" +
                            "아이디 / 비밀번호 분실 시 복구코드를 보내드리는 용도로 사용됩니다", color = neutral_600 )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                    P_Medium11(content = "이용약관 ", color = primary_500)
                    P_Medium11(content = "및 ", color = black)
                    P_Medium11(content = "개인정보처리방침 ", color = primary_500)
                    P_Medium11(content = "동의", color = black)
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            recordOpen.value = true
                        }
                    ) {
                        Image(modifier = Modifier.size(16.dp),
                            painter = if(agree.value) painterResource(R.drawable.ic_checkbox_true)
                            else painterResource(id = R.drawable.ic_checkbox_false),
                            contentDescription = "check button")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp),
            ) {if (email.value.isNotEmpty() && agree.value){
                BigButton(
                    "다음", true
                ) {
                    authViewModel.postAuthAuthCode(
                        body = PostAuthAuthCodeRequest(
                            email = email.value,
                            isSignUp = "true"
                        )
                    )

                }
            }else{
                BigButton("다음", false) { navController.navigate(OnboardingScreen.SignupNumber.name) }            }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun  BottomNavigatesheet(recordOpen : MutableState<Boolean>,
                             agree1: MutableState<Boolean>, agree2: MutableState<Boolean>, agree3: MutableState<Boolean>
                             , sheetState: SheetState, closeSheet: () -> Unit
    ){
        val coroutineScope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = closeSheet,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
            containerColor = tertiary_500,
            dragHandle = null
        ){
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(tertiary_500)
                .padding(10.dp)
                .height(250.dp)){

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {
                    CheckButton(agree1)
                    Spacer(modifier =Modifier.height(16.dp))
                    CheckButton(agree2)
                    Spacer(modifier =Modifier.height(16.dp))
                    CheckButton(agree3)
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 50.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {
                    Column (
                        Modifier.clickable { navController.navigate(OnboardingScreen.AgreeDetail.name) }){
                        P_Medium11(content = "보기", color = neutral_600)
                    }
                    Spacer(modifier =Modifier.height(16.dp))
                    Column (
                        Modifier.clickable { navController.navigate(OnboardingScreen.AgreeDetail.name) }){
                        P_Medium11(content = "보기", color = neutral_600)
                    }
                    Spacer(modifier =Modifier.height(16.dp))
                    Column (
                        Modifier.clickable { navController.navigate(OnboardingScreen.AgreeDetail.name) }){
                        P_Medium11(content = "보기", color = neutral_600)
                    }
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center) {
                    Row(modifier = Modifier.wrapContentWidth()) {
                        P_Medium11(content = "(필수) ", color = primary_500)
                        P_Medium11(content = "이용약관 동의", color = black)
                    }
                    Spacer(modifier =Modifier.height(16.dp))
                    Row(modifier = Modifier.wrapContentWidth()) {
                        P_Medium11(content = "(필수) ", color = primary_500)
                        P_Medium11(content = "개인정보 수집 이용 및 조회 동의", color = black)
                    }
                    Spacer(modifier =Modifier.height(16.dp))
                    Row(modifier = Modifier.wrapContentWidth()) {
                        P_Medium11(content = "(선택) ", color = black)
                        P_Medium11(content = "E-mail 마케팅 정보 수신 동의", color = black)
                    }
                }


                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp)
                        .padding(bottom = 45.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom) {
                    Column(
                        Modifier
                            .wrapContentSize()
                            .clickable {
                                coroutineScope
                                    .launch {
                                        sheetState.hide()
                                    }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            recordOpen.value = false
                                        }
                                    }
                            }) {
                        YJ_Bold15(content = "확인", color = black)
                    }
                }
            }
        }
    }


    @Composable
    fun SignupNumber(countViewModel: CountViewModel = viewModel(), authCode:MutableState<String>,
                     email : MutableState<String>){

        val timeLeft by countViewModel.timeLeft.collectAsState()
        val number = remember{mutableStateOf("")}
        val signnumState = remember {mutableStateOf(true)}
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        authCode.value = number.value

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        DisposableEffect(Unit) {
            countViewModel.startCountdown() // 카운트다운 시작
            onDispose {
                // DisposableEffect가 해제될 때 카운트다운을 중지하거나 정리할 필요 없음
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = tertiary_500)
            .padding(horizontal = 20.dp)){

            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {
                    navController.navigate(OnboardingScreen.SignupEmail.name)
                }, "회원가입")
            }

            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center) {
                Column(modifier = Modifier
                    .padding(horizontal = 24.dp)) {
                    Box(
                        contentAlignment = Alignment.Center
                    ){
                        Spacer(modifier = Modifier.width(18.dp))
                        Image(
                            modifier = Modifier
                                .width(205.dp)
                                .height(42.dp),
                            painter = painterResource(id = R.drawable.img_alarm_grey), contentDescription = ""
                        )
                        P_Medium11(content = "입력하신 이메일로 인증번호가 전송되었어요\n" +
                                "메일함을 확인해 주세요", color = white)
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("인증번호", if(signnumState.value) black else negative_600)
                        CountText(timeLeft)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "인증번호를 입력해주세요",
                        onValueChanged = { number.value = it },
                        onClicked = {},
                        text = number,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "onemove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color =  if(signnumState.value) black else negative_600)

                    if(signnumState.value){}else{
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(modifier = Modifier.padding(horizontal = 8.dp)
                        ){
                            P_Medium11(content = "인증번호를 다시한번 확인해 주세요", color = negative_600)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11(content = "받으신 인증 번호 6자리는 차후 복구코드와 동일하게 사용됩니다\n" +
                                "인터넷 상태에 따라 소요시간이 발생할 수 있습니다", color = neutral_600 )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier
                        .width(86.dp)
                        .align(Alignment.End)
                        .clickable { countViewModel.restartCountdown() }) {
                        Column {
                            Column(modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    authViewModel.postAuthAuthCode(
                                        body = PostAuthAuthCodeRequest(
                                            email = email.value,
                                            isSignUp = "true"
                                        )
                                    )
                                }) {
                                P_Medium11(
                                    content = "인증번호 재전송",
                                    color = black
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider(color = black)
                        }
                    }
                    Row(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.End)){
                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            Spacer(modifier = Modifier.width(18.dp))
                            Image(
                                modifier = Modifier
                                    .width(155.dp)
                                    .height(26.dp),
                                painter = painterResource(id = R.drawable.img_alarmup_grey), contentDescription = ""
                            )
                            P_Medium11(content = "동일한 이메일로 재전송되었어요", color = white)
                        }
                    }
                }
            }



            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp)
            ) {
                if ( number.value.isNotEmpty()){
                    BigButton("다음", true) {
                        authViewModel.patchAuthAuthCodeConfirm(
                            body = PatchAuthAuthCodeConfirmRequest(
                                userId = userId.toString(),
                                code = number.value
                            )
                        )

                    }
                }else{
                    BigButton("다음", false) { navController.navigate(OnboardingScreen.Signup.name) }
                }

            }
        }
    }

    @Composable
    fun Signup(emailAuthCode:MutableState<String>, email : MutableState<String>){
        val password = remember{
            mutableStateOf("")
        }
        val passwordcheck = remember{
            mutableStateOf("")
        }
        val wificheck = remember{
            mutableStateOf(false)
        }
        val pwequel = remember{ mutableStateOf(true) }
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(password) {
            focusRequester.requestFocus()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {
                    navController.navigate(OnboardingScreen.SignupNumber.name)
                }, "회원가입")
            }

            Column(modifier=Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("아이디", black)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    P_Medium18(content = email.value, color = black)
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = black)
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("비밀번호", black)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "비밀번호를 입력해주세요",
                        onValueChanged = { password.value = it },
                        onClicked = {},
                        text = password,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "manynextmove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = black)
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("비밀번호 확인", if(pwequel.value)black else primary_500)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "비밀번호를 다시 한 번 입력해주세요",
                        onValueChanged = { passwordcheck.value = it },
                        onClicked = {},
                        text = passwordcheck,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "manyendmove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = if(pwequel.value)black else primary_500)

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End){
                        Column(horizontalAlignment = Alignment.End) {
                            P_Medium11(content = "와이파이가 없는 환경에서도 녹음 파일 저장하기",color = black)
                            P_Medium11(content = "나중에 설정에서 바꿀 수 있어요",color = neutral_500)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        CheckButton(wificheck)
                    }
                }
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp)
            ) {
                if ( password.value.isNotEmpty() && passwordcheck.value.isNotEmpty()){
                    BigButton("가입하기", true) {
                        if (password.value == passwordcheck.value) {
                            authViewModel.patchAuthChangePassword(
                                body = PatchAuthChangePasswordRequest(
                                    code = emailAuthCode.value,
                                    newPassword = password.value
                                )
                            )
                            pwequel.value = true
                            navController.navigate(OnboardingScreen.Login.name)
                        } else {
                            pwequel.value = false
                        }
                    }
                }else{
                    BigButton("가입하기", false) { navController.navigate(OnboardingScreen.SignupComplete.name) }
                }

            }
        }
    }

    @Composable
    fun SignupComplete(){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = tertiary_500)
            .padding(horizontal = 20.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                P_Bold30("회원가입이 완료되었어요\n만나서 반가워요!", black, TextAlign.Center)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp)
            ) {
                BigButton("로그인하기", true) { navController.navigate(OnboardingScreen.Login.name) }
            }
        }
    }


    // 비밀번호 찾기
    @Composable
    fun FindPassword(id: MutableState<String>){

        val idState = remember{ mutableStateOf(true) }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = tertiary_500)
            .padding(horizontal = 20.dp)){

            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {
                    navController.navigate(OnboardingScreen.Login.name)
                }, "비밀번호 찾기")
            }

            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("아이디", if (idState.value) black else negative_600)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "가입한 이메일을 입력해주세요",
                        onValueChanged = { id.value = it },
                        onClicked = {},
                        text = id,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "onemove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color =  if (idState.value) black else negative_600)

                    if(idState.value){ }else{
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ){
                            P_Medium11("앗 ! 올바른 이메일 형식이 아니에요", negative_600)
                        }}

                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11(content = "해당 이메일로 비밀번호 초기화 코드가 발송됩니다", color = neutral_600 )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp),
            ) {if (id.value.isNotEmpty()){
                BigButton("다음", true) {
                    authViewModel.postAuthAuthCode(
                        body = PostAuthAuthCodeRequest(
                            email = id.value,
                            isSignUp = "false"
                        )
                    )
                }
            }else{
                BigButton("다음", false) { navController.navigate(OnboardingScreen.FindPasswordNumber.name) }            }
            }
        }
    }

    // 비밀번호 찾기에서 인증코드 확인
    @Composable
    fun FindPasswordNumber(authCode:MutableState<String>, id: MutableState<String>){

        val findpwnumState = remember{
            mutableStateOf(true)
        }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = tertiary_500)
            .padding(horizontal = 20.dp)){

            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {
                    navController.navigate(OnboardingScreen.SignupEmail.name)
                }, "비밀번호 찾기")
            }

            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center) {
                Column(modifier = Modifier
                    .padding(horizontal = 24.dp)) {
                    Box(
                        contentAlignment = Alignment.Center
                    ){
                        Spacer(modifier = Modifier.width(18.dp))
                        Image(
                            modifier = Modifier
                                .width(205.dp)
                                .height(42.dp),
                            painter = painterResource(id = R.drawable.img_alarm_grey), contentDescription = ""
                        )
                        P_Medium11(content = "입력하신 이메일로 복구코드가 전송되었어요\n" +
                                "메일함을 확인해 주세요", color = white
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("복구코드", if(findpwnumState.value)black else negative_600)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "복구코드 6자리를 입력해주세요",
                        onValueChanged = { authCode.value = it },
                        onClicked = {},
                        text = authCode,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "onemove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = if(findpwnumState.value)black else negative_600)

                    if(findpwnumState.value){ }else{
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ){
                            P_Medium11("복구코드를 다시한번 확인해 주세요", negative_600)
                        }}

                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11(content = "인터넷 상태에 따라 소요시간이 발생할 수 있습니다.", color = neutral_600 )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier
                        .width(86.dp)
                        .align(Alignment.End)
                        .clickable {
                            authViewModel.postAuthAuthCode(
                                body = PostAuthAuthCodeRequest(
                                    email = id.value,
                                    isSignUp = "false"
                                )
                            )
                        }) {
                        Column {
                            Column(modifier = Modifier
                                .padding(horizontal = 8.dp)) {
                                P_Medium11(
                                    content = "복구코드 재전송",
                                    color = black
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider(color = black)
                        }
                    }
                    Row(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.End)

                ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            Spacer(modifier = Modifier.width(18.dp))
                            Image(
                                modifier = Modifier
                                    .width(155.dp)
                                    .height(26.dp),
                                painter = painterResource(id = R.drawable.img_alarmup_grey), contentDescription = ""
                            )

                            P_Medium11(content = "동일한 이메일로 재전송되었어요", color = white)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp)
            ) {
                if ( authCode.value.isNotEmpty()){
                    BigButton("다음", true) {
                        authViewModel.patchAuthAuthCodeConfirm(
                            body = PatchAuthAuthCodeConfirmRequest(
                                userId = userId.toString(),
                                code = authCode.value
                            )
                        )
                        navController.navigate(OnboardingScreen.FindPasswordSignup.name)
                    }
                }else{
                    BigButton("다음", false) { navController.navigate(OnboardingScreen.FindPasswordSignup.name) }
                }

            }
        }
    }

    @Composable
    fun  FindPasswordSignup(code:String) {

        val password = remember {
            mutableStateOf("")
        }
        val passwordcheck = remember {
            mutableStateOf("")
        }
        val pwequel = remember {
            mutableStateOf(true)
        }
        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(password) {
            focusRequester.requestFocus()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                ImgBackButton(onClick = {
                    navController.navigate(OnboardingScreen.SignupNumber.name)
                }, "비밀번호 찾기")
            }

            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("비밀번호", black)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "비밀번호를 입력해주세요",
                        onValueChanged = { password.value = it },
                        onClicked = {},
                        text = password,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "manyfirstmove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = black)
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    ) {
                        P_Medium11("비밀번호 확인", if (pwequel.value) black else negative_600)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    MomentTextField(
                        hint = "비밀번호를 다시 한 번 입력해주세요",
                        onValueChanged = { passwordcheck.value = it },
                        onClicked = {},
                        text = passwordcheck,
                        keyboardType = KeyboardType.Text,
                        changecolor = black,
                        focusRequester = focusRequester,
                        move = "manyendmove",
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = if (pwequel.value) black else negative_600)

                    if (pwequel.value) {
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            P_Medium11(content = "비밀번호가 동일하지 않습니다.", color = negative_600)
                        }
                    }
                }
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 45.dp)
            ) {
                if (password.value.isNotEmpty() && passwordcheck.value.isNotEmpty()) {
                    BigButton("로그인하기", true) {
                        if (password.value == passwordcheck.value) {
                            authViewModel.patchAuthChangePassword(
                                body = PatchAuthChangePasswordRequest(
                                    code = code,
                                    newPassword = password.value
                                )
                            )
                            pwequel.value = true

                        } else {
                            pwequel.value = false
                        }
                    }
                } else {
                    BigButton("로그인하기", false) {
                        navController.navigate(OnboardingScreen.Login.name)
                    }
                }
            }
        }
    }

    @Composable
    fun AgreeDetail(){
        //데이터 결정되면 디자인 작은 수정 있을 수 있음
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = tertiary_500)
            .padding(horizontal = 27.dp)){

            P_ExtraBold16(content = "약관동의", color = black)

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 92.dp)
                .verticalScroll(rememberScrollState())) {
                P_Medium11(content = "(필수)약관동의\n\n\n\n\n\n\n약관동의\n\n\n\n\n\n\n약관동의\n\n\n\n\n\n\n\n\n약관동의\n\n\n\n\n약관동의\n\n\n\n\n약관동의\n\n\n\n\n약관동의\n\n\n\n\n약관동의\n\n\n\n\n약관동의약\n\n\n\n\n관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의약관동의", color = black)
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
            ) { Column(modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .clickable { navController.navigate(OnboardingScreen.SignupEmail.name) }) {
                YJ_Bold15("확인",black)
            }  }
        }
    }

    /*@Composable
    fun BackButton(onClick : () -> Unit, content : String){
        Row(modifier = Modifier
            .wrapContentWidth()
            .height(20.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            BackButton_Onboarding(onClick)
            Spacer(modifier = Modifier.width(16.dp))
            P_SemiBold18(content, black)
        }
    }*/




    @Preview
    @Composable
    fun OnboardingPreView(){
        Login()
    }
}