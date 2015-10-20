# coding=utf-8
__author__ = 'Administrator'

import MySQLdb, time

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
    sql = 'INSERT  INTO users(name,password,regtime) VALUES("%s","%s","%s")' % (str(name), str(password),regtime)
    print sql
    cur.execute(sql)
    conn.commit()
    # finally:
    #     cur.close()
    #     conn.close()
