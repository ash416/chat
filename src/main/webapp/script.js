var ws;

function connect() {
    var username = document.getElementById("username").value;
    if (username !== "") {
        ws = new WebSocket("ws://" + document.location.host + "/chat_web/chat/" + username);
        var exit_button = document.getElementById("exit");
        exit_button.disabled = false;
        exit_button.style.visibility = "visible";
        var conn_button = document.getElementById("connect");
        conn_button.style.visibility = "hidden";
        conn_button.disabled = true;
    }

    ws.onmessage = function (event) {
        var chat = document.getElementById("chat");
        console.log(event.data);
        var data = JSON.parse(event.data);
        var usersCount = document.getElementById("count");
        usersCount.innerHTML =  data.usersCount.toString();
        var users = document.getElementById("people");
        users.innerHTML = data.users;
        if (data.content === "connected!" || data.content === "disconnected!")
            chat.innerHTML += "<div class=\"message\" style='font-style: oblique; font-weight: bold'> " + "<span style='font-weight: bold'>" + data.sender + " </span> " + " : " + data.content + "</div>";
        else
            if (data.addr === "")
                chat.innerHTML += "<div class=\"message\"> " + "<span style='font-weight: bold'>" + data.sender + " </span> " + " : " + data.content + "</div>";
            else
                chat.innerHTML += "<div class=\"message\"> " + "<span style='font-weight: bold'>" + data.sender + "</span> (for you) " + " : " + data.content + "</div>";
    };
}

function send() {
    if (ws.readyState === ws.OPEN) {
        var message = document.getElementById("message");
        var content = message.value;
        var to = document.getElementById("to").value;
        var json = JSON.stringify({
            "addr": to,
            "content": content
        });
        message.value = "";
        ws.send(json);
        if (to === "")
            chat.innerHTML += "<div class=\"message\"> " + "<span style='font-weight: bold; color: #23282b'>Me</span>   : " + content + "</div>";
        else
            chat.innerHTML += "<div class=\"message\"> " + "<span style='font-weight: bold; color: #23282b'>Me to "+ to + "</span>   : " + content + "</div>";
    }
}

function exit() {
    if (ws.readyState === ws.OPEN) {
        var exit_button = document.getElementById("exit")
        exit_button.disabled = true;
        exit_button.style.visibility = "hidden";
        var conn_button = document.getElementById("connect");
        conn_button.disabled = false;
        conn_button.style.visibility = "visible";
        chat.innerHTML += "<div class=\"message\" style='font-style: oblique; font-weight: bold'> " + "<span style='font-weight: bold; color: #23282b'>Me</span>   : " + "disconnected!</div>";
        ws.close();
    }
}