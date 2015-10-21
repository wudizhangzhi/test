# coding=utf-8
__author__ = 'Administrator'

import sys

reload(sys)  # Python2.5 初始化后会删除 sys.setdefaultencoding 这个方法，我们需要重新载入
sys.setdefaultencoding('utf-8')

import tornado.web
from db import *


class mainHandler(tornado.web.RequestHandler):
    '''主页'''

    def get(self):
        self.redirect('/index/1_1')  # 没登录时候转到默认索引页面

    def post(self):
        name = self.get_argument('username')
        pw = self.get_argument('password')
        res = check(name, pw)
        if res:
            self.set_cookie('cookieName', name)
        else:
            pass
        self.redirect('/')


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
            # TODO 用更安全的方式
            self.set_cookie('cookieName', name)
            self.redirect('/')  # 前往主页


class loginHandler(tornado.web.RequestHandler):
    '''登录'''

    def get(self):
        self.render('login.html')

    def post(self):
        return


class indexHandler(tornado.web.RequestHandler):
    '''文章索引页面'''

    def get(self, page):
        username = self.get_cookie('cookieName')
        blog = showAllBlogs()
        self.render('index.html', cookieName=username, blogs=blog, footer='啊啊啊啊')

        # def post(self):
        # name = self.get_argument('username')
        # pw = self.get_argument('password')
        # res = check(name, pw)
        # if res:
        #     self.set_cookie('cookieName', name)
        # else:
        #     pass
        # self.redirect('/')


class writeBlogHandler(tornado.web.RequestHandler):
    '''写文章并保存的页面'''

    def get(self):
        cookieName = self.get_cookie('cookieName')
        self.render('write.html', cookieName=cookieName)

    def post(self):
        title = self.get_argument('title')
        content = self.get_argument('content')
        saveBlog(self.get_cookie('cookieName'), title, content)
        self.redirect('/')


class memberHandler(tornado.web.RequestHandler):
    '''显示所有成员的页面'''

    def get(self):
        print 'get member'
        member = showAllUsers()
        self.render('member.html', cookieName=self.get_cookie('cookieName'), member=member)


class chatHandler(tornado.web.RequestHandler):
    '''群聊页面
        num:页码
        每页显示10条
        '''

    def get(self, num):
        print 'get chat:' + num
        chats = showALLChat()
        n = len(chats)
        if n % 10 == 0:
            pages = (n / 10)
        else:
            pages = (n / 10) + 1
        # TODO 页码的显示设定
        self.render('chat.html', cookieName=self.get_cookie('cookieName'), chat=chats, pages=pages, num=int(num))

    def post(self, num):
        # 保存聊天内容到数据库
        topic = self.get_argument('topic')
        name = self.get_cookie('cookieName')
        if not name:
            # TODO 提示登录
            return
        insertChat(name, topic)
        self.redirect('/chat/1')


class blogHandler(tornado.web.RequestHandler):
    def get(self,num):
        cookieName=self.get_cookie('cookieName')
        blog=showBlog(num)
        self.render('blog.html',cookieName=cookieName)

    def post(self,num):
        return