import kotlinx.css.*
import styled.StyleSheet

object WelcomeStyles : StyleSheet("WelcomeStyles", isStatic = true) {
    val textContainer by css {
        padding(5.px)
        textAlign = TextAlign.center
        backgroundColor = Color.cornflowerBlue
        color = Color.mistyRose
    }

    val textInput by css {
        margin(vertical = 5.px)
        fontSize = 14.px
    }
} 
