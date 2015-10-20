# coding=utf-8
__author__ = 'Administrator'
import MySQLdb

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
import time

print time.ctime(),type(time.ctime())