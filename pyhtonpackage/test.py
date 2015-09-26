# coding=utf-8
__author__ = 'Administrator'
import sqlite3
# bid    消息ID
# uid     用户ID
# username 用户名
# ugrade 用户等级字段
# content(text) 微博内容
# img(message包含图片链接)
# created_at 微博发布时间
# source(来源)
# rt_num, 转发数
# cm_num, 评论数
# rt_uid, 转发UID
# rt_username, 转发用户名
# rt_v_class, 转发者等级
# rt_content, 转发内容
# rt_img, 转发内容所涉及图片
# src_rt_num, 源微博回复数
# src_cm_num, 源微博评论数
# gender,(用户性别)
# rt_mid（转发mid）
# geo 地区
# lat() 经度
# lon 纬度
# place 地点
# hashtags
# ats  @谁
# rt_hashtags, 回复标签
# rt_ats, 回复@谁
# v_url, 源微博URL
# rt_v_url 转发URL

path = r'G:\老王python基础进程项目无KEY完整版{黑客教程小组}\基础\基础篇视频8-22{黑客教程小组}\基础篇21-文本操作应用\twitter数据挖掘片段.txt'.decode('utf-8').encode(
    'gbk')
path_out = r'G:\老王python基础进程项目无KEY完整版{黑客教程小组}\基础\基础篇视频8-22{黑客教程小组}\基础篇21-文本操作应用\out.txt'.decode('utf-8').encode(
    'gbk')
path_db = r'G:\sqlite_master.db'

conn = sqlite3.connect(path_db)
cursor = conn.cursor()
# cursor.execute('create table if not exists twitterInfo(id integer primary key AUTOINCREMENT,bid VARCHAR,uid VARCHAR )')
# cursor.execute('insert into twitterInfo(bid,uid) VALUES (?,?)',['lalala','lalalal'])
#
# conn.commit()
#
# print cursor.execute("select name from sqlite_master where type = 'table' order by name").fetchall()
# print cursor.execute('select * from twitterInfo').fetchall()
# cursor.close()
# conn.close()


file = open(path, 'r')
cursor.execute(
    'CREATE TABLE IF NOT EXISTS twitterInfo(id INTEGER PRIMARY KEY AUTOINCREMENT,bid VARCHAR,uid VARCHAR ,username VARCHAR,v_class VARCHAR,'
    'content VARCHAR,img VARCHAR,times VARCHAR,source VARCHAR,rt_num VARCHAR,cm_num VARCHAR,rt_uid VARCHAR,rt_username VARCHAR,rt_v_class VARCHAR,'
    'rt_content VARCHAR,rt_img VARCHAR,src_rt_num VARCHAR,src_cm_num VARCHAR,gender VARCHAR,rt_mid VARCHAR,location VARCHAR,rt_mid_2 VARCHAR,mid VARCHAR,'
    'lat VARCHAR,lon VARCHAR,lbs_type VARCHAR,lbs_title VARCHAR,poiid VARCHAR,links VARCHAR,hashtags VARCHAR,ats VARCHAR,rt_links VARCHAR,'
    'rt_hashtags VARCHAR,rt_ats VARCHAR,v_url VARCHAR,rt_v_url VARCHAR)')
i = 0
j = 0
for line in file:
    list_line = line.replace('"', '').decode('utf-8').split(',')
    i += 1
    if len(list_line) != 35:  # 如果信息数量不等于35则跳过
        continue
    cursor.execute(
        'INSERT INTO twitterInfo(bid,uid,username,v_class,content,img,times,source,rt_num,'
        'cm_num,rt_uid,rt_username,rt_v_class,rt_content,rt_img,src_rt_num,src_cm_num,gender,'
        'rt_mid,location,rt_mid,mid,lat,lon,lbs_type,lbs_title,poiid,links,hashtags,ats,rt_links,'
        'rt_hashtags,rt_ats,v_url,rt_v_url) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)',
        list_line)
    # break;
    j += 1
    print 'end:处理%d条信息，保存%d 条'.decode('utf-8') % (i, j)  # print cursor.execute('SELECT * FROM twitterInfo').fetchall()
# print len(cursor.execute('SELECT username FROM twitterInfo').fetchall())

conn.commit()
cursor.close()
conn.close()
# list_line = line.split(',')
#
#
# file_out = open(path_out, 'w')
# file_out.write(line)
# file_out.close()
# file.read
# file.close()
# print list_line
