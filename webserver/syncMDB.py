# coding=utf-8
__author__ = 'Administrator'

import MySQLdb, time


class mdb(object):
    def __init__(self):
        self.conn = MySQLdb.connect(
            host='localhost',
            port=3306,
            user='root',
            passwd='13961000804',
            db='web_test'
        )
        self.cur = self.conn.cursor()



    def checkName(self,name):
        self.cur.execute('SELECT name FROM users')
        data = self.fetchall()
        for i in data:
            if name == i[0]:
                return True
        return False


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
        regtime = time.ctime()
        sql = 'INSERT  INTO users(name,password,regtime) VALUES("%s","%s","%s")' % (str(name), str(password), regtime)
        print sql
        cur.execute(sql)
        conn.commit()
        return


    def showAllBlogs():
        cur.execute('SELECT * FROM blog ORDER BY id DESC ;')  # 倒序得到所有文章
        tem = cur.fetchall()
        return tem


    def saveBlog(name, title, content):
        '''保存文章到数据库'''
        regtime = time.ctime()
        sql = 'INSERT blog(name,title,blog,time) VALUES("%s","%s","%s","%s")' % (name, title, content, regtime)
        cur.execute(sql.encode('utf-8'))
        print sql
        conn.commit()
        return


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


    def insertChat(name, topic):
        regtime = getTime()
        sql = 'INSERT chat(name,content,time) VALUES("%s","%s","%s")' % (name, topic, regtime)
        cur.execute(sql.encode('utf-8'))
        print sql
        conn.commit()
        return


    def showBlog(num):
        cur.execute('SELECT * FROM blog WHERE id=%s' % num)
        return cur.fetchone()


    def showComment(blog_id):
        cur.execute('SELECT * FROM comment WHERE blog_id=%s ORDER BY id DESC ' % blog_id)
        return cur.fetchall()


    def insertComment(blog_id, name, content):
        t = time.ctime()
        sql = 'INSERT comment(blog_id,name,content,time) VALUES("%s","%s","%s","%s")' % (blog_id, name, content, t)
        cur.execute(sql.encode('utf-8'))
        conn.commit()
        print sql
        return


    def showUserBlog(username):
        sql = 'SELECT * FROM blog WHERE name="%s" ORDER BY id DESC ' % username
        cur.execute(sql.encode('utf-8'))
        print sql
        return cur.fetchall()


    def addClickRate(blog_id):
        '''增加一次点击量，并返回数字'''
        sql = 'UPDATE blog SET click_rate=click_rate+1 WHERE id=%s' % blog_id
        cur.execute(sql)
        conn.commit()
        cur.execute('SELECT click_rate FROM blog WHERE id=%s' % blog_id)
        print sql
        return cur.fetchone


    def showHotBlog(num):
        '''显示最热的文章'''
        sql = 'SELECT * FROM blog ORDER BY click_rate DESC LIMIT %s' % num
        cur.execute(sql)
        return cur.fetchall()
