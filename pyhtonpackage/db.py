# coding=utf-8
__author__ = 'Administrator'

# import sqlite3
#
# conn=sqlite3.connect("G:/test.db")
# cursor=conn.cursor()
# cursor.execute('create table user2(id integer primary key AUTOINCREMENT ,name varchar(20))')
# cursor.execute('insert into user2(id,name) values (null,\'Michal\')')
#
# cursor.close()
# conn.commit()
# conn.close()

import urllib, re
from bs4 import BeautifulSoup


def getContentFromHtml(html):
    '''从页面获取球员基本信息'''
    team_data = html.find('div', 'team_data')
    playername = team_data.find('h2').get_text()
    player_basic_data = team_data.find('div', class_='font').get_text()
    content_url = playername + "\n" + player_basic_data
    return playername, content_url


def saveFile(filename, content):
    '''保存文件'''
    filename = filename.decode('utf-8') + u'.txt'
    file = open(u'G:/spider/%(name)s' % {'name': filename}, 'w')
    file.write(content)
    file.close()
    print '保存文件：%s 完成'.decode('utf-8') % filename


def getHtmlFrom(url):
    '''从网址获取网页全部信息'''
    return BeautifulSoup(urllib.urlopen(url).read(), 'html.parser')


def saveContentFromUrl(url):
    '''从网址获取内容并保存'''
    html = getHtmlFrom(url)
    result = getContentFromHtml(html)
    saveFile(result[0].encode('utf-8'), result[1].encode('utf-8'))
    print '尝试保存：%s' % result[0].encode('utf-8')


def getUrlsFromHtml(html):
    '''从网页中获取球员urls'''
    players_all_urls = []
    i = 0
    part = re.compile(r'href="([^\s]*)"')
    team_urls = part.findall(str(html.find('div', class_='players_left')))
    for team_url in team_urls:
        team_url = r'http://g.hupu.com' + team_url
        # print team_url
        # 打开网页获取球员urls
        web = getHtmlFrom(team_url)
        # 获取该球队所有球员的urls
        div = web.find('div', class_='players_right').find_all('td', class_='left')
        for player in div:
            player_team_urls = part.findall(str(player))
            print player_team_urls
            if player_team_urls not in players_all_urls:
                players_all_urls.extend(player_team_urls)
        i += 1
        print '完成了 %d 只球队'.decode('utf-8') % i
    return players_all_urls


def start(url):
    urls = getUrlsFromHtml(getHtmlFrom(url))
    for each in urls:
        try:
            print each
            saveContentFromUrl(each)
        except:
            print '-----'
print '完成'.decode('utf-8')

url = u"http://g.hupu.com/nba/players/Warriors"
start(url)
