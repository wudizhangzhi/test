# coding=utf-8
__author__ = 'Administrator'

import sqlite3

path_db = r'G:\sqlite_master.db'
conn = sqlite3.connect(path_db)
cursor = conn.cursor()

cursor.execute('CREATE TABLE IF NOT EXISTS test2(id INTEGER PRIMARY KEY AUTOINCREMENT,bid VARCHAR,name VARCHAR)')
cursor.execute('INSERT INTO test2(bid,name) VALUES (?,?)', (u'中文爱上收到', u'呵呵爱上的撒哒'))
conn.commit()
for i in cursor.execute("SELECT  * FROM test2").fetchall():
    print i[0],i[2],i[1]

cursor.close()
conn.close()
