# coding=utf-8
__author__ = 'Administrator'

import MySQLdb, time
import chardet

conn = MySQLdb.connect(
    host='localhost',
    port=3306,
    user='root',
    passwd='13961000804',
    db='web_test'
)
cur = conn.cursor()


def check(name, password=None):
    # try:
    cur.execute('SELECT * FROM users')
    data = cur.fetchall()
    print data
    if password == None:  # 如果没有密码，查询用户名是否存在
        for i in data:
            if name == i[1]:
                return True
        return False
    else:  # 密码存在，验证一致性
        for i in data:
            if name == i[1]:
                if password == i[2]:
                    return True
                else:
                    return False
        return False
        # finally:
        #     cur.close()
        #     conn.close()


def insert_user(name, password):
    # try:
    regtime = time.ctime()
    sql = 'INSERT  INTO users(name,password,regtime) VALUES("%s","%s","%s")' % (str(name), str(password), regtime)
    print sql
    cur.execute(sql)
    conn.commit()
    # finally:
    #     cur.close()
    #     conn.close()


def showAllBlogs():
    cur.execute('SELECT id,name,title,time FROM blog ORDER BY id DESC')# 倒序得到所有文章
    tem = cur.fetchall()
    print tem[0][1]
    print chardet.detect(tem[0][1])
    return tem

def saveBlog(name,title,content):
    '''保存文章到数据库'''
    regtime = time.ctime()
    sql='INSERT blog(name,title,blog,time) VALUES("%s","%s","%s","%s")' % (name,title,content,regtime)
    cur.execute(sql.encode('utf-8'))
    print sql
    conn.commit()

def showAllUsers():
    cur.execute('SELECT * FROM users')
    return cur.fetchall()

def showALLChat():
    cur.execute('SELECT * FROM chat ORDER BY id DESC')
    return cur.fetchall()

def getTime():
    '''获取当前时间'''
    t = time.localtime()
    res = str(t.tm_hour) + ':' + str(t.tm_min) + ':' + str(t.tm_sec)
    return res

def insertChat(name,topic):
    regtime=getTime()
    sql='INSERT chat(name,content,time) VALUES("%s","%s","%s")' % (name,topic,regtime)
    cur.execute(sql.encode('utf-8'))
    print sql
    conn.commit()

def showBlog(num):
    cur.execute('SELECT * FROM blog WHERE id=%s' % num)
    return cur.fetchall()

def showComment(blog_id):
    cur.execute()