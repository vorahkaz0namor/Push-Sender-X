package ru.netology.pusher

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import java.io.FileInputStream

fun main() {
    val post = Post(
        id = 11,
        author = "Zakharov Roman, AN-34",
        published = "17 November, 16:43",
        content = "В 2004 году или около того Майкл Физерс прислал мне электронное письмо, в котором сообщил, что если переупорядочить мои принципы, из их первых букв можно составить слово SOLID — так появились принципы SOLID . Последующие главы подробнее описывают каждый принцип, а пока познакомьтесь с краткой аннотацией: \n" +
                "1. SRP: Single Responsibility Principle — принцип единственной ответственности . Действительное следствие закона Конвея: лучшей является такая структура программной системы, которая формируется в основном под влиянием социальной структуры организации, использующей эту систему, поэтому каждый программный модуль имеет одну и только одну причину для изменения .\n" +
                "2.  OCP: Open-Closed Principle — принцип открытости/закрытости . Этот принцип был сформулирован Бертраном Мейером в 1980-х годах . Суть его сводится к следующему: простая для изменения система должна предусматривать простую возможность изменения ее поведения добавлением нового, но не изменением существующего кода .\n" +
                "3. LSP: Liskov Substitution Principle — принцип подстановки Барбары Лисков . Определение подтипов Барбары Лисков известно с 1988 года . В двух словах, этот принцип утверждает, что для создания программных систем из взаимозаменяемых частей эти части должны соответствовать контракту, который позволяет заменять эти части друг другом . \n" +
                "4. ISP: Interface Segregation Principle — принцип разделения интерфейсов . Этот принцип призывает разработчиков программного обеспечения избегать зависимости от всего, что не используется . \n" +
                "5. DIP: Dependency Inversion Principle — принцип инверсии зависимости . Код, реализующий высокоуровневую политику, не должен зависеть от кода, реализующего низкоуровневые детали . Напротив, детали должны зависеть от политики .",
        likes = 1, likedByMe = true, shares = 3, views = 9
    )
    val user = User(id = 1, name = "Artur")
    // Здесь можно потестировать отправку сообщения, задавая ключ action'а
    val sendingAction = "LIKE"

    if (Action.values().find { it.name == sendingAction } != null)
        sendMessage(sendingAction, user, post)
    else
        println("You're strongly needs to update NMedia!")
}

fun sendMessage(action: String, user: User, post: Post) {
    val options = FirebaseOptions.builder()
        .setCredentials(
            GoogleCredentials
                .fromStream(FileInputStream("fcm.json")))
        .build()
    val messageBody = Message.builder()
        .putData("action", action)
        .putData("content", """{
          "userId": "${user.id}",
          "userName": "${user.name}",
          "postId": "${post.id}",
          "postAuthor": "${post.author}"
        }""".trimIndent())
    val messages = mutableListOf<Message>()
    for (t in tokens)
        messages.add(messageBody.setToken(t).build())

    FirebaseApp.initializeApp(options)
    FirebaseMessaging.getInstance().sendAll(messages)
}