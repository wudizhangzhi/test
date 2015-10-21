# coding=utf-8
__author__ = 'Administrator'

import os
import tornado.web
import tornado.ioloop
from handlers import *

requestHandlers = [
    (r'/', mainHandler),
    (r'/register', registerHandler),
    (r'/login', loginHandler),
    (r'/index/(\w+)', indexHandler),  # \w匹配包括下划线的任何单词字符
    (r'/write', writeBlogHandler),
    (r'/member', memberHandler),
    (r'/chat/(\w+)', chatHandler),
    (r'/blog/(\w+)', blogHandler),
]

settings = {
    'static_path': os.path.join(os.path.dirname(__file__), 'static'),
    'template_path': os.path.join(os.path.dirname(__file__), 'templates')
}

app = tornado.web.Application(requestHandlers, **settings)
app.listen(9001)
print 'start listen port 9001'
tornado.ioloop.IOLoop.current().start()
