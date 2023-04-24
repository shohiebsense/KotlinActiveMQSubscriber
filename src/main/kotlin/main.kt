import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQBytesMessage
import java.util.*
import javax.jms.Connection
import javax.jms.MessageListener
import javax.jms.Session

fun main() {
    val connFactory = ActiveMQConnectionFactory("tcp://192.168.1.3:61616")

    val conn = connFactory.createConnection()!!
    conn.setClientID("SampleClient")

    val sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)!!

    //receiveQueue(conn, sess)
    receiveTopic(conn, sess)

    Timer().schedule(object : TimerTask() {
        override fun run() {
            conn.close()
        }

    }, 20000)
}

fun receiveQueue(conn: Connection, sess: Session) {
    val dest = sess.createQueue("SampleQueue")

    val cons = sess.createConsumer(dest)!!

    conn.start()

    val msg = cons.receive()
    println(msg)

}


fun receiveTopic(conn: Connection, sess: Session) {

    val dest = sess.createTopic("SampleTopic")

    val cons = sess.createDurableSubscriber(dest, "SampleSubscription")!!

    conn.start()

    val listener = MessageListener { message ->

        val bytesMessage = message as ActiveMQBytesMessage
        println(String(bytesMessage.content.data))
    }
    cons.messageListener = listener

}