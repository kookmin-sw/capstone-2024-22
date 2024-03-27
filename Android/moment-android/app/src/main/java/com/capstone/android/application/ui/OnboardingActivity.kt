package com.capstone.android.application.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.capstone.android.application.MainActivity
import com.capstone.android.application.R
import com.capstone.android.application.app.composable.MomentTextField
import com.capstone.android.application.ui.theme.BackButton_Onboarding
import com.capstone.android.application.ui.theme.BigButton
import com.capstone.android.application.ui.theme.CheckButton
import com.capstone.android.application.ui.theme.P_Bold30
import com.capstone.android.application.ui.theme.P_Medium11
import com.capstone.android.application.ui.theme.P_SemiBold18
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.negative_600
import com.capstone.android.application.ui.theme.neutral_600
import com.capstone.android.application.ui.theme.primary_500
import com.capstone.android.application.ui.theme.tertiary_500
import com.capstone.android.application.ui.theme.white
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

enum class OnboardingScreen(){
    Login,
    Signup,
    Signup_email,
    Signup_number,
    FindPassword,
    FindPassword_number,
    FindPassword_Signup,
    SignupComplete
}
class OnboardingActivity:ComponentActivity() {
    lateinit var navController: NavHostController
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
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
                    composable(route=OnboardingScreen.Signup_email.name){ Signup_email() }
                    composable(route=OnboardingScreen.Signup_number.name){ Signup_number() }
                    composable(route=OnboardingScreen.Signup.name){ Signup() }
                    composable(route=OnboardingScreen.FindPassword.name){ FindPassword() }
                    composable(route=OnboardingScreen.FindPassword_number.name){ FindPassword_number() }
                    composable(route=OnboardingScreen.FindPassword_Signup.name){ FindPassword_Signup() }
                    composable(route=OnboardingScreen.SignupComplete.name){ SignupComplete() }
                }
            }
        }
    }

    @Composable
    fun Login(){
        val id = remember{mutableStateOf("")}
        val password = remember{mutableStateOf("")}
        val focusRequester =  remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {

                BackButton(onClick = {startActivity(Intent(this@OnboardingActivity, SplashActivity::class.java))}, "로그인")
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)){

                Column( modifier = Modifier
                    .padding(horizontal = 8.dp)){
                    P_Medium11("아이디", black)}
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
                Divider(color = black)
                Spacer(modifier = Modifier.height(16.dp))


                Column( modifier = Modifier
                    .padding(horizontal = 8.dp)){
                    P_Medium11("비밀번호", black)}
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
                Divider(color = black)
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
                    Row(modifier = Modifier
                        .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        P_Medium11(content = "자동로그인",color = black)
                        Spacer(modifier = Modifier.width(8.dp))
                        //CheckButton(autologin)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                if (id.value.isNotEmpty() && password.value.isNotEmpty()){
                    BigButton("로그인하기", true, onClick = {startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))})
                }else{
                    BigButton("로그인하기", false, onClick = {startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))})
                }


                Spacer(modifier = Modifier.height(8.dp))
                BigButton("가입하기", true) { navController.navigate(OnboardingScreen.Signup_email.name) }

            }

        }
    }

    @Composable
    fun Signup_email(){
        val email = remember{mutableStateOf("")}

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                BackButton(onClick = {
                    navController.navigate(OnboardingScreen.Login.name)
                }, "회원가입")
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11("이메일", black)
                }
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
                Divider(color = black)
                Spacer(modifier = Modifier.height(8.dp))
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
                    CheckButton()
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Bottom
            ) {if (email.value.isNotEmpty()){
                BigButton("다음", true) { navController.navigate(OnboardingScreen.Signup_number.name) }
            }else{
                BigButton("다음", false) { navController.navigate(OnboardingScreen.Signup_number.name) }            }
            }
        }
    }

    @Composable
    fun Signup_number(){
        val number = remember{
            mutableStateOf("")
        }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                BackButton(onClick = {
                    navController.navigate(OnboardingScreen.Signup_email.name)
                }, "회원가입")
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11("인증번호", black)
                }
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
                Divider(color = black)
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11(content = "받으신 인증 번호 6자리는 차후 복구코드와 동일하게 사용됩니다", color = neutral_600 )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                if ( number.value.isNotEmpty()){
                    BigButton("다음", true) { navController.navigate(OnboardingScreen.Signup.name) }
                }else{
                    BigButton("다음", false) { navController.navigate(OnboardingScreen.Signup.name) }
                }

            }
        }
    }

    @Composable
    fun Signup(){
        val id = remember{
            mutableStateOf("")
        }
        val password = remember{
            mutableStateOf("")
        }
        val passwordcheck = remember{
            mutableStateOf("")
        }

        val focusManager = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(id) {
            focusRequester.requestFocus()
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 54.dp)
                    .wrapContentSize()
            ) {
                BackButton(onClick = {
                    navController.navigate(OnboardingScreen.Signup_number.name)
                }, "회원가입")
            }
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
                MomentTextField(
                    hint = "abcd1234@naver.com 자동입력하기",
                    onValueChanged = { id.value = it },
                    onClicked = {},
                    text = id,
                    keyboardType = KeyboardType.Text,
                    changecolor = black,
                    focusRequester = focusRequester,
                    move = "manyfirstmove",
                    focusManager = focusManager
                )
                Divider(color = black)
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11("비밀번호", black)
                }
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
                Divider(color = black)
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    P_Medium11("비밀번호 확인", black)
                }
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
                Divider(color = black)

            }
            Column(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                if (id.value.isNotEmpty() && password.value.isNotEmpty() && passwordcheck.value.isNotEmpty()){
                    BigButton("다음", true) { navController.navigate(OnboardingScreen.SignupComplete.name) }
                }else{
                    BigButton("다음", false) { navController.navigate(OnboardingScreen.SignupComplete.name) }
                }

            }
        }
    }

    @Composable
    fun SignupComplete(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = tertiary_500)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box() {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    P_Bold30("회원가입이 완료되었어요\n만나서 반가워요!", black, TextAlign.Center)
                }
                Column(modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom)  {

                    BigButton("로그인하기", true) { navController.navigate(OnboardingScreen.Login.name) }
                    Spacer(modifier = Modifier.height(72.dp))

                }
            }
        }
    }

    @Composable
    fun FindPassword(){

        val id = remember{ mutableStateOf("") }
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
                BackButton(onClick = {
                    navController.navigate(OnboardingScreen.Login.name)
                }, "비밀번호 찾기")
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 344.dp)) {
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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp),
            ) {if (id.value.isNotEmpty()){
                BigButton("다음", true) { navController.navigate(OnboardingScreen.FindPassword_number.name) }
            }else{
                BigButton("다음", false) { navController.navigate(OnboardingScreen.FindPassword_number.name) }            }
            }
        }
    }

    @Composable
    fun FindPassword_number(){
        val number = remember{
            mutableStateOf("")
        }
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
                BackButton(onClick = {
                    navController.navigate(OnboardingScreen.Signup_email.name)
                }, "비밀번호 찾기")
            }

            //Toast.makeText(this@OnboardingActivity, R.drawable.img_alarm_grey , Toast.LENGTH_LONG).show()
            //Toast.makeText(this@OnboardingActivity, "dfd", Toast.LENGTH_LONG).show()

            Column(modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 298.dp)) {
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
                    .padding(top = 344.dp)
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
                    .clickable { /*인증번호 재전송 기능*/ }) {
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



            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp)
            ) {
                if ( number.value.isNotEmpty()){
                    BigButton("다음", true) { navController.navigate(OnboardingScreen.FindPassword_Signup.name) }
                }else{
                    BigButton("다음", false) { navController.navigate(OnboardingScreen.FindPassword_Signup.name) }
                }

            }
        }
    }

    @Composable
    fun  FindPassword_Signup() {

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
                BackButton(onClick = {
                    navController.navigate(OnboardingScreen.Signup_number.name)
                }, "비밀번호 찾기")
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 277.dp)
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

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp)
            ) {
                if (password.value.isNotEmpty() && passwordcheck.value.isNotEmpty()) {
                    BigButton("로그인하기", true) {
                        if (password.value == passwordcheck.value) {
                            pwequel.value = true
                            navController.navigate(OnboardingScreen.Login.name)
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
    }




    @Preview
    @Composable
    fun OnboardingPreView(){
        Login()
    }
}