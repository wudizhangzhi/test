# coding=utf-8
__author__ = 'Administrator'
import MySQLdb,time

# conn = MySQLdb.connect(
#     host='localhost',
#     port=3306,
#     user='root',
#     passwd='13961000804',
#     db='test'
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
'CREATE TABLE IF NOT EXISTS comment(id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,blog_id SMALLINT,name VARCHAR(20),content TEXT);'
