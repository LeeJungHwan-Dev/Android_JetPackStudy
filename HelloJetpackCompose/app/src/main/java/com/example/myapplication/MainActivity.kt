package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        //프로젝트 이름Theme 형식으로 선언해줘야만 MaterialTheme을 맞춤설정 할 수 있는 프로젝트의 기본 테마를 생성할 수 있다.
                        //일괄적용에 용의하다.
                        //Surface는 일종의 요소를 담기위한 컨테이너이다.
                        //modifier는 클릭 이벤트 터치 이벤트와 같이 여러 이벤트를 처리 하고 , 디자인 적인 요소를 제어할 수 있는 요소이다.
                        Conversation(SampleData.conversationSample)
                    }
                }
            }
        }
}

data class Message(val author: String, val body : String) // data class 복잡한 getter setter을 손쉽게 사용할 수 있다.

@Composable //compossable을 지정해줘야만 SetContent 함수안에 레이아웃으로 정의해 사용할 수 있다.
fun MessageCard(msg: Message){
    Row (modifier = Modifier.padding(all = 8.dp)){ //Row를 사용하면 요소를 가로로 배치할 수 있다. all = 8.dp는 기본적으로 상하좌우에 8의 padding을 부여한다는 뜻이다.
       Image( // 이미지를 사용하기 위한 함수이다.
           painter = painterResource(id = R.drawable.profile_picture), //painterResource 함수를 사용해 id 값을 읽어와 painter(이미지)를 지정해준다.
           contentDescription = "Contact profile picture", //시각장애인을 위한 이미지 설명 항목이다.
           modifier = Modifier //이미지 특성을 적용하기 위해 선언한 Modifier
               .size(40.dp) // 상하좌우 이미지의 사이즈를 설정한다.
               .clip(CircleShape)//이미지뷰의 모양을 지정한다. (원)
               .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape) //테두리를 생성하는 코드이다, 1.5dp의 두깨를 가지고 있으며 머티리얼 디자인의 secondary컬러를 사용하고, 원 모양 테두리이다.
       )

        Spacer(modifier = Modifier.width(8.dp)) // 요소와 요소 사이의 간격을 조정한다. ( 좌, 우 ) 8.dp 만큼 분리한다.

        var isExpanded by remember { mutableStateOf(false) } //compose가 state가 변화하며 recompose될 때 이전 state의 상태를 기억하고 불러올때 사용 by를 사용해 "위임" * 상속과는 다른 개념이다.
        val surfaceColor by animateColorAsState(targetValue = // animateColorAsStat는 compose에 애니메이션을 적용하기 위한 함수이다. by를 사용해 "위임"했다.
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface, //isExpanded가 true일 경우 primary컬러를 아닐 경우 surface 컬러리를 사용한다.
        )

       Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) { //modifier을 사용해 클릭 이벤트 정의 클릭시 isExpanded true,flase 반대로 변경
            Text(text = msg.author, //msg.author 부분을 가져온다.
                 color = MaterialTheme.colors.secondaryVariant, // 컬러를 secondaryVariant로 지정
                 style = MaterialTheme.typography.body2 // 머티리얼 테마에서 텍스트의 스타일을 typography body2 스타일로 지정한다.
                )
            Spacer(modifier = Modifier.height(4.dp)) // 상하 4.dp로 마진

           Surface(shape = MaterialTheme.shapes.medium, // Text의 기본적인 모양을 지정한다.
                   elevation = 1.dp, // 그림자 정도를 지정한다.
                   color = surfaceColor, // color를 지정한다. color는 59번째와 같이 특정 조건을 설정해 지정할 수도 있다.
                   modifier = Modifier.animateContentSize().padding(1.dp) //내부 padding을 1dp로 설정
           ) {
               Text(text = msg.body, //msg의 body 값을 불러온다.
                   modifier = Modifier.padding(all = 4.dp), //내부 padding을 전체 4dp로 설정한다.
                   maxLines = if (isExpanded) Int.MAX_VALUE else 1, // maxLines를 특정 조건에 작동하게 지정한다.
                   style = MaterialTheme.typography.body2, // Text의 style을 설정한다 ex.) h1을 할 경우 엄청 커진다.
               )
           }
        }
    }
}

@Composable // setcontent에서 사용하기 위한 composable 선언
fun Conversation(messages: List<Message>){ // data class의 Message를 전달 받기 위한 매개 변수
    LazyColumn{// 기본적으로 RecyclerView를 기반으로 작성된 함수로 리스트 뷰와 같은 역할을 한다.
        items(messages) {//items는 요소를 뜻하고 messages를 MessageCard함수에 전달한다.
            message -> MessageCard(msg = message)
        }
    }
}

@Preview // PreView를 사용하면 Split또는 Design 부분에서 바로 확인할 수 있다. 단, 적절한 인자값을 넘겨줘야한다.
@Composable
fun PreviewConversation() {
    MyApplicationTheme {
        Conversation(SampleData.conversationSample)
    }
}