# coding=utf-8
__author__ = 'Administrator'

import sqlite3, urllib
from bs4 import BeautifulSoup

URL = r'http://g.hupu.com/nba/players/lamarcusaldridge-1209.html'  # 球员个人信息地址测试
DB_PATH = r'G:/players.db'  # 数据库地址


def openWeb(url):
    '''根据网页地址获取网页正文'''
    return BeautifulSoup(urllib.urlopen(url).read(), 'html.parser')


def getPlayerDict(webSoup):
    '''根据网页内容获取球员信息资料'''
    player_basic = webSoup.find('div', class_='team_data')
    player_basic_lines = webSoup.find('div', class_='team_data').find_all('p')
    # 获取球员姓名
    player_name = player_basic.find('h2').get_text()
    dict = {}
    for line in player_basic_lines:
        # 处理每一行数据
        putDataInDict(player_name, line.get_text(), dict)
    return dict


def putDataInDict(playername, line, dict):
    # print '输入的一行信息: %s'.decode('utf-8') % line
    '''返回球员基本信息字典'''
    dict['球员姓名'.decode('utf-8')] = playername
    basic_data = line.split('：'.decode('utf-8'))
    keys = ['位置', '身高', '体重', '生日', '球队', '学校', '选秀', '出生地', '本赛季薪金', '合同']
    for key in keys:
        # print '尝试 %s'.decode('utf-8') % key.decode('utf-8')
        # print basic_data[0]
        # print key.decode('utf-8')
        if basic_data[0] == key.decode('utf-8'):
            # print '使用key: %s'.decode('utf-8') % key.decode('utf-8')
            dict[key.decode('utf-8')] = basic_data[1]
    return dict


def getTupleFromDict(dict):
    '''将字典转化为输入数据库的元组'''
    keys = ['球员姓名', '位置', '身高', '体重', '生日', '球队', '学校', '选秀', '出生地', '本赛季薪金', '合同']
    list =[]
    # 测试用
    # print dict
    # print dict[keys[0]]
    # print dict[keys[1].decode('utf-8')]
    # print dict[keys[2].decode('utf-8')]
    # print dict[keys[3].decode('utf-8')]

    for key in keys:
        # print 'dit:' + key.decode('utf-8')
        i = key.decode('utf-8')
        list.append(dict[i])
    # for item in dict.items():
    #     print item[0]
    return list


def putDataInDB(data):
    '''将信息输入数据库'''
    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()
    # 还需要添加很多数据
    cur.execute(
        'create TABLE if NOT EXISTS playersinfo(id INTEGER PRIMARY key autoincrement,player_name VARCHAR,player_position VARCHAR,height VARCHAR ,'
        'weight VARCHAR ,birth VARCHAR,team VARCHAR,school VARCHAR,draft VARCHAR,born VARCHAR,season_salary VARCHAR,'
        'contract VARCHAR)')
    cur.execute(
        'insert INTO playersinfo(player_name,player_position,height,weight,birth,team,school,draft,born,season_salary,contract) '
        'VALUES (?,?,?,?,?,?,?,?,?,?,?)', data)
    conn.commit()
    cur.close()
    conn.close()
    print u'数据写入完成'
    return


def openDB():
    conn = sqlite3.connect(DB_PATH)
    cur = conn.cursor()
    # db=cur.execute('select * from playersinfo').fetchall()
    db=cur.execute("PRAGMA table_info(playersinfo)").fetchall()
    cur.close()
    conn.close()
    print db
    for each in db:
        print each
    return


def start():
    player_basic_dict = getPlayerDict(openWeb(URL))

    player_basic_list=getTupleFromDict(player_basic_dict)
    putDataInDB(player_basic_list)
    print u'结束'
    return


# start()
# openDB()