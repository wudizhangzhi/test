# coding=utf-8
__author__ = 'Administrator'
import MySQLdb, time


# conn = MySQLdb.connect(
#     host='localhost',
#     port=3306,
#     user='root',
#     passwd='13961000804',
#     db='web_test'
# )
#
# cur = conn.cursor()
# sql_insert = u'INSERT test_server(img,name) VALUES("%s","%s")'
# data = [[u'p2187004396', u'电影'], [u'p2188108927', u'呵呵']]
# try:
#     for d in data:
#         print d
#         s = sql_insert % tuple(d)
#         print s
#         cur.execute(s.encode('utf-8'))
# finally:
#     conn.commit()
#     cur.close()
#     conn.close()
'CREATE TABLE IF NOT EXISTS blog(id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,name VARCHAR(20),title VARCHAR(50),blog TEXT,time VARCHAR(30));'
'INSERT blog(name,title,blog,time) VALUES("测试用户2","这是标题2","正文内容2","Wed Oct 21 10:48:23 2015");'
'CREATE TABLE IF NOT EXISTS chat(id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,name VARCHAR(20),content TEXT,time VARCHAR(30));'
'CREATE TABLE IF NOT EXISTS comment(id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,blog_id SMALLINT,name VARCHAR(20),content TEXT,time VARCHAR(30));'
# 博客点击量
'CREATE TABLE IF NOT EXISTS blog_click_rate(blog_id SMALLINT UNSIGNED,click_rate INT UNSIGNED,FOREIGN KEY(blog_id) REFERENCES blog(id));'
import tornado.escape

print type(tornado.escape.json_encode('name'))
print type(tornado.escape.json_decode(tornado.escape.json_encode('name')))

import time


# def performance(f):
#     def fn(*args, **kw):
#         t1 = time.time()
#         f1 = f(*args, **kw)
#         t2 = time.time()
#         print 'run %fs' % (t2 - t1)
#         return f1
#
#     return fn
#
#
# @performance
# def factorial(n):
#     return reduce(lambda x, y: x * y, range(1, n + 1))
#
#
# print factorial(10)

import email.mime
# class A:
#     member = "this is a test."
#     def __init__(self):
#         pass
#
#     @classmethod
#     def Print1(cls):
#         print "print 1: ", cls.member
#
#     def Print2(self):
#         print "print 2: ", self.member
#
# a=A()
# A.Print1()
# a.Print1()
