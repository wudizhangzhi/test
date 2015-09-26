# coding=utf-8
path = r"G:\老王python基础进程项目无KEY完整版{黑客教程小组}\基础\基础篇视频8-22{黑客教程小组}\基础篇8-python基本数据类型习题解答\代码答案\test.txt"
# 一般系统编码是gbk
file = open(path.decode('utf8').encode('gbk'))
content = file.read()
dcontent = content.decode('gbk')
print content
print len(content)
print len(dcontent)
dcontent.replace('\n', '')
print len(dcontent)

t = '2013来了。\n' \
    '2013不是世界末日。\n' \
    '2013欢乐多。\n'
print len(t)
print len(t.decode('utf-8'))
print content.replace('2012', '2013')
print dcontent[-3:].encode('gbk')
# 7.请从字符串的最初开始，截断该字符串，使其长度为11.
print content[:10]
print t.decode('utf-8')
print t.decode('utf-8')[:10]
# 8.请将{4}中的字符串保存为test1.py文本.
f = open(
    r"G:\老王python基础进程项目无KEY完整版{黑客教程小组}\基础\基础篇视频8-22{黑客教程小组}\基础篇8-python基本数据类型习题解答\代码答案\test.txt".decode("utf-8").encode(
        'gbk'), 'w')
f.write(content.replace('2012', '2013'))
f.close()
# 六.已知如下代码
#
# ________
#
a = "中文编程"
b = a
c = a
a = "python编程"
# b = u'%s' % a
d = "中文编程"
e = a
c = b
b2 = a.replace("中", "中")
# ________
#
# 1.请给出str对象"中文编程"的引用计数
# 2.请给出str对象"python编程"的引用计数
print "a:%s" % id(a)
import sys

print sys.getrefcount(a)

# 七.已知如下变量
# ________
a = "字符串拼接1"
b = "字符串拼接2"

print (a + b).decode('utf-8')
print "{a}{b}".format(a=a, b=b)


# 八.请阅读string模块，并且，根据string模块的内置方法输出如下几题的答案。
#
# 1.包含0-9的数字。
# 2.所有小写字母。
# 3.所有标点符号。
# 4.所有大写字母和小写字母。
# 5.请使用你认为最好的办法将{1}-{4}点中的字符串拼接成一个字符串。

import string

print string.digits
print string.lowercase
print string.punctuation
print string.letters

# a = "i,am,a,boy,in,china"
# print a.find("i")
# print a.count(",")
# f=open(r"G:\老王python基础进程项目无KEY完整版{黑客教程小组}\基础\基础篇视频8-22{黑客教程小组}\基础篇8-python基本数据类型习题解答\代码答案\infoString.txt".decode("utf-8").encode("gbk"),'w')
# sys.stdout=f
# help(string)
# f.close()

print [x for x in range(0, 100) if x > 20 and x % 2 == 0]
print [x * x for x in range(0, 10)]

a = [1, 2, 3]
b = a[:]
b[0] = 5
# del a
print a
print b


def confict(state, nextX):
    nextY = len(state)
    for i in range(nextY):
        if abs(nextX - state[i]) in (0, nextY - i):
            return True
    return False


def Queen(num=8, state=()):
    for i in range(num):
        if not confict(state, i):
            if len(state) == num - 1:
                yield (i,)
            else:
                for result in Queen(num, state + (i,)):
                    yield (i,) + result


import random


def printQueen(num):
    solution = random.choice(list(Queen(num)))
    for i in range(len(solution)):
        print "o" * solution[i] + "X" + "o" * (len(solution) - solution[i])


printQueen(8)

print 3 >> 1
print bin(1)
print 32 & 40
print 2 ** 10

# 题目1：
#
# 假如a = 5  b = 3
#
# 做下面的题目：
#
# 1 如果a > 1 并且 a < 10 输出 'a=5'
#
#
# 2 如果a等于5并且b=3输出结果'a=5,b=3'
#
a = 5
b = 3
if a > 1 and a < 10:
    print "a=5"
if a == 5 and b == 3:
    print "a=5,b=3"

# 1. 已知字符串 a = "aAsmr3idd4bgs7Dlsf9eAF",要求如下
a = "aAsmr3idd4bgs7Dlsf9eAF"
# 1.1 请将a字符串的大写改为小写，小写改为大写。
print a.swapcase()
# 1.2 请将a字符串的数字取出，并输出成一个新的字符串。
print ''.join(x for x in a if x.isdigit())
# 1.3 请统计a字符串出现的每个字母的出现次数（忽略大小写，a与A是同一个字母），并输出成一个字典。 例 {'a':4,'b':2}
print dict([(x, a.lower().count(x)) for x in set(a.lower())])
# 1.4 请去除a字符串多次出现的字母，仅留最先出现的一个。例 'abcabb'，经过去除后，输出 'abc'
list_a = list(a)
set_list = set(list_a)
list2 = list(set_list)
list2.sort(key=list_a.index)
print "".join(list2)

# 1.5 请将a字符串反转并输出。例：'abc'的反转是'cba'
a = "aAsmr3idd4bgs7Dlsf9eAF"
print a[::-1]
# 1.6 去除a字符串内的
# 数字后，请将该字符串里的单词重新排序（a-z），并且重新输出一个排序后的字符 串。（保留大小写,a与A的顺序关系为：A在a前面。例：AaBb）
print "1.6"
l = sorted(a)
a_lower_list = []
a_upper_list = []

for x in l:
    if x.isupper():
        a_upper_list.append(x)
    elif x.islower():
        a_lower_list.append(x)
    else:
        pass
for i in a_upper_list:
    i_lower = i.lower()
    if i_lower in a_lower_list:
        a_lower_list.insert(a_lower_list.index(i_lower), i)

print ''.join(a_lower_list)



# 1.7 请判断 'boy'里出现的每一个字母，是否都出现在a字符串里。如果出现，则输出True，否则，则输 出False.
search = 'boy'
u = set(a)
u.update(list(search))
print len(set(a)) == len(u)
# 1.8 要求如1.7，此时的单词判断，由'boy'改为四个，分别是 'boy','girl','bird','dirty'，请判断如上这4个字符串里的每个字母，是否都出现在a字符串里。
a = "aAsmr3idd4bgs7Dlsf9eAF"
search = ['boy', 'girl', 'bird', 'dirty']
u = set(a)
for i in search:
    u.update(list(i))
print len(set(a)) == len(u)


# 1.9 输出a字符串出现频率最高的字母  #
print a
print [(x, a.count(x)) for x in set(a)]
print sorted([(x, a.count(x)) for x in set(a)], key=lambda x: x[1], reverse=True)[0]
# 2.在python命令行里，输入import this 以后出现的文档，统计该文档中，"be" "is" "than" 的出现次数。
#
import this, os

doc = os.popen("python -m this").read()
doc = doc.replace("\n", '')
search = ["be", "is", "than"]
content = list([word for word in doc.split(" ")])
for each in search:
    print "%(word)s show %(num)d's" % {'word': each, 'num': content.count(each)}

# 3.一文件的字节数为 102324123499123，请计算该文件按照kb与mb计算得到的大小。
#
print 102324123499123 >> 10
print 102324123499123 >> 20
# 4.已知  a =  [1,2,3,6,8,9,10,14,17],请将该list转换为字符串，例如 '123689101417'.
a = [1, 2, 3, 6, 8, 9, 10, 14, 17]



print type(str(a)[1:-1].replace(', ',''))

def test():
    a='a'
    b='b'
    return a,b

print test()[0]
