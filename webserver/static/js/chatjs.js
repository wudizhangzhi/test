/**
 * Created by Administrator on 2015/10/28.
 */
$(document).ready(function () {
    chatsocket.start();
    //name=
    //发送聊天内容
    $('#send').click(function () {
        var message = $('#topic').val();
        if (message.trim() == '') {
            alert('发送内容不能为空')
        } else {
            var msg = {
                'type':'msg',
                'content': message,
                'num':1,
            };
            chatsocket.socket.send(JSON.stringify(msg));
            $('#topic').val('');
        }
    });
});

String.prototype.trim=function() {

    return this.replace(/(^\s*)|(\s*$)/g,'');
};

var chatsocket = {
    socket: null,
    start: function () {
        var url = 'ws://' + location.host + '/chatsocket';
        chatsocket.socket = new WebSocket(url);
        chatsocket.socket.onmessage = function (event) {

            chatsocket.showMessage(event.data);
        }
    },

    showMessage: function (message) {
        //var res=jQuery.parseJSON(message);
        //var node=$(message.html);
        //node.hide();
        //document.getElementById('chat').append(node);
        //node.slideDown();
        document.getElementById('chat').innerHTML =message;
    }
};
