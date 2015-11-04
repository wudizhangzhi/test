# coding=utf-8
__author__ = 'Administrator'

import sys, json

reload(sys)  # Python2.5 初始化后会删除 sys.setdefaultencoding 这个方法，我们需要重新载入
sys.setdefaultencoding('utf-8')

import tornado.web, tornado.escape, tornado.websocket
from db import *


class baseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie('cookieName')


class mainHandler(baseHandler):
    '''主页'''

    @tornado.web.authenticated
    def get(self):
        print 'main get'
        self.redirect('/index/1')  # 没登录时候转到默认索引页面

    def post(self):
        print 'main post'
        name = self.get_argument('username')
        pw = self.get_argument('password')
        if check(name, pw):
            # self.set_cookie('cookieName', name)
            print 'set_secure_cookie: ' + name
            self.set_secure_cookie('cookieName', name)
        else:
            pass
        self.redirect('/')


class loginHandler(baseHandler):
    '''监测登录信息'''

    def post(self):
        print 'post login'
        name = self.get_argument('name')
        pw = self.get_argument('pw')
        if checkName(name):
            if check(name, pw):
                re = 0
            else:
                re = 2  # 密码错误
        else:
            re = 1  # 用户民不存在
        self.write(dict(checklogin=re))


class checkRegisterHandler(baseHandler):
    def post(self):
        name = self.get_argument('username')
        pw1 = self.get_argument('password1')
        pw2 = self.get_argument('password2')
        print 'check register:' + name, pw1, pw2
        res = check(name)
        if res:  # 用户名已经存在
            name = 1
        elif len(name) < 2 or len(name) > 6:  # 用户名长度不对
            name = 2
        else:
            name = 0
        if len(pw1) < 6 or len(pw1) > 16:  # 密码长度不对
            pw = 2
        elif pw1 != pw2:  # 密码不一致
            pw = 1
        else:
            pw = 0
        self.write(dict(name=name, pw=pw))


class registerHandler(baseHandler):
    '''注册'''

    def get(self):
        print 'get register'
        self.render('register.html', samename=False)

    def post(self):
        print 'post register'
        # TODO 检查两次密码输入是否一致
        name = self.get_argument('username')
        password = self.get_argument('password')
        # print name, password
        print name
        res = check(name)
        if res:  # 用户名已经存在
            # self.render('register.html',samename=res)
            self.write('samename')
        else:
            insert_user(name, password)
            # TODO 用更安全的方式
            self.set_secure_cookie('cookieName', name)
            self.redirect('/')  # 前往主页


class indexHandler(baseHandler):
    '''文章索引页面'''

    def get(self, num):
        print 'get index'
        username = self.get_current_user()
        # TODO 最热文章
        hot = showHotBlog(2)
        # 最新文章
        blog = showAllBlogs()
        n = len(blog)
        if n % 10 == 0:
            pages = (n / 10)
        else:
            pages = (n / 10) + 1
        self.render('index.html', cookieName=username, hot=hot, blogs=blog, footer='copyright@zzc', num=int(num),
                    pages=pages)

        # def post(self):
        # name = self.get_argument('username')
        # pw = self.get_argument('password')
        # res = check(name, pw)
        # if res:
        #     self.set_cookie('cookieName', name)
        # else:
        #     pass
        # self.redirect('/')


class writeBlogHandler(baseHandler):
    '''写文章并保存的页面'''

    def get(self):
        cookieName = self.get_current_user()
        self.render('write.html', cookieName=cookieName)

    def post(self):
        title = self.get_argument('title')
        content = self.get_argument('content')
        saveBlog(self.get_current_user(), title, content)
        self.redirect('/')


class memberHandler(baseHandler):
    '''显示所有成员的页面'''

    def get(self):
        print 'get member'
        member = showAllUsers()
        self.render('member.html', cookieName=self.get_current_user(), member=member)


import tornado.escape


class chatsocketHandler(tornado.websocket.WebSocketHandler, baseHandler):
    ''''''
    waiters = set()

    def open(self, num=1):
        print 'websocket open:' + str(num)
        chatsocketHandler.waiters.add(self)
        chats = showALLChat()
        n = len(chats)
        if n % 10 == 0:
            pages = (n / 10)
        else:
            pages = (n / 10) + 1
        self.write_message(self.render_string('chat.html', cookieName=self.get_current_user(), chat=chats, pages=pages, num=num))

    def on_close(self):
        print 'websocket close'
        chatsocketHandler.waiters.remove(self)

    def on_message(self, message):
        print 'websocket onmessage:'+message
        self.send_update(message)

    # @classmethod
    def send_update(self, message):
        print 'websocket send_update'
        s = tornado.escape.json_decode(message)
        num = 1
        if (s['type']=='msg'):
            insertChat(self.get_current_user(), s['content'].encode('utf8'))
        else:
            num = s['num']
        chats = showALLChat()
        n = len(chats)
        if n % 10 == 0:
            pages = (n / 10)
        else:
            pages = (n / 10) + 1
        for waiter in self.waiters:
            waiter.write_message(self.render_string('chat.html', chat=chats, pages=pages, num=num))


class chatHandler(tornado.websocket.WebSocketHandler, baseHandler):
    def get(self, num):
        print 'get chat:' + num
        chats = showALLChat()
        n = len(chats)
        if n % 10 == 0:
            pages = (n / 10)
        else:
            pages = (n / 10) + 1
        # TODO 页码的显示设定
        self.on_message({'num': num})
        # self.render('chat.html', cookieName=self.get_current_user(), chat=chats, pages=pages, num=int(num))
        return


class chattingHandler(baseHandler):
    '''群聊页面
        num:页码
        每页显示10条
        '''

    def get(self):
        print 'get chating:'
        chats = showALLChat()
        n = len(chats)
        if n % 10 == 0:
            pages = (n / 10)
        else:
            pages = (n / 10) + 1
        self.render('chatting.html', cookieName=self.get_current_user(), num=1, pages=pages)


class blogHandler(baseHandler):
    '''显示文章页面'''

    def get(self, blog_id):
        addClickRate(blog_id)
        cookieName = self.get_current_user()
        blog = showBlog(blog_id)
        comments = showComment(blog_id)
        self.render('blog.html', cookieName=cookieName, blog_id=blog_id, blog=blog, comments=comments)

    def post(self, blog_id):
        comment = self.get_argument('comment')
        name = self.get_current_user()
        insertComment(blog_id, name, comment)
        self.redirect('/blog/%s' % blog_id)


class logoutHandler(baseHandler):
    def get(self):
        print 'get logout'
        self.clear_cookie('cookieName')
        self.redirect('/')


class userHandler(baseHandler):
    '''个人主页'''

    def get(self, name):
        blogs = showUserBlog(name)
        self.render('user.html', cookieName=self.get_current_user(), blogs=blogs, name=name)
