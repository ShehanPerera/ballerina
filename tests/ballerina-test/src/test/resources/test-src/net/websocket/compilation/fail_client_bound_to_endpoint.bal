import ballerina/http;
import ballerina/io;

endpoint http:WebSocketClient wsClientEp {
    url:"wss://echo.websocket.org",
    callbackService:echo
};

service<http:WebSocketClientService> echo bind wsClientEp {
    onText(endpoint conn, string text) {
    }

    onBinary(endpoint conn, blob text, boolean more) {

    }

    onClose(endpoint conn, int val, string text) {

    }

    onIdleTimeout(endpoint conn) {

    }
    onPing(endpoint conn, blob so) {

    }
    onPong(endpoint conn, blob yes) {

    }
}