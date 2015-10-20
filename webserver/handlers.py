# coding=utf-8
__author__ = 'Administrator'

import tornado.web
from db import *


class mainHandler(tornado.web.RequestHandler):
    '''主页'''

    def get(self):
        return

    def post(self):
        return


class registerHandler(tornado.web.RequestHandler):
    '''注册'''

    def get(self):
        self.render('register.html')

    def post(self):
        # TODO 检查两次密码输入是否一致
        name = self.get_argument('username')
        password = self.get_argument('password')
        print name, password
        res = check(name)
        if res:  # 用户名已经存在
            self.redirect('/register')
        else:
            insert_user(name, password)
            self.redirect('/')  # 前往主页


class loginHandler(tornado.web.RequestHandler):
    '''登录'''

    def get(self):
        self.render('login.html')

    def post(self):

        return
