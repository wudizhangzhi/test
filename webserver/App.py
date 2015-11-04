# coding=utf-8
__author__ = 'Administrator'

import os
import tornado.web
import tornado.ioloop
from handlers import *
from tornado.httpserver import HTTPServer

requestHandlers = [
    (r'/', mainHandler),
    (r'/register', registerHandler),
    (r'/index/(\w+)', indexHandler),  # \w匹配包括下划线的任何单词字符
    (r'/write', writeBlogHandler),
    (r'/member', memberHandler),
    (r'/chatting/', chattingHandler),
    (r'/chat/(\w+)', chatHandler),
    (r'/blog/(\w+)', blogHandler),
    (r'/logout', logoutHandler),
    (r'/login', loginHandler),
    (r'/user/(\w+)', userHandler),
    (r'/checkRegister', checkRegisterHandler),
    (r'/chatsocket', chatsocketHandler),
]

settings = {
    'static_path': os.path.join(os.path.dirname(__file__), 'static'),
    'template_path': os.path.join(os.path.dirname(__file__), 'templates'),
    'login_url':'/index/1',
    'cookie_secret':'__TODO__type_in',
    # 'xsrf_cookies':True,
    # TODO 设置域名
    # 'home_url':'http://testweb.org',
    # 'domain': "poweredsites.org",
}

app = tornado.web.Application(requestHandlers, **settings)
httpserver=HTTPServer(app,xheaders=True)
httpserver.bind(9001)
httpserver.start(1)
# app.listen(9001)
print 'start listen port 9001'
tornado.ioloop.IOLoop.instance().start()
