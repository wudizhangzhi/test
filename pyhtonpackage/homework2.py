# -*- coding:utf-8 -*-

a = "中文"
print a
print a.decode("utf-8")
print len(a)
print len(a.decode("utf-8"))

# name = raw_input(u"请输入你的姓名：").decode("gbk")
# print u"%s ,真是一个好名字呢" % name

fips = [0, 1]
for i in range(8):
    fips.append(fips[-2] + fips[-1])
print fips
print len(fips)

a = [1, 2, 3, 4]
b = a[:]
c = a
print id(a)
print id(b)
print id(c)


def checkIndex(key):
    '''所给的键是合法的索引么'''
    if not isinstance(key, (int, long)): raise TypeError
    if key < 0: raise IndexError


class ArithmeticSequence:
    def __init__(self, start=0, step=1):
        self.start = start
        self.step = step
        self.change = {}  # 保存修改项目

    def __getitem__(self, key):
        checkIndex(key)
        try:
            return self.change[key]
        except KeyError:
            return self.start + key * self.step

    def __setitem__(self, key, value):
        checkIndex(key)
        self.change[key] = value


s = ArithmeticSequence(1, 2)
print s[4]
s[4] = 10
print s[4]
print s[5]


# import sqlite3
#
# conn=sqlite3.connect("somedatabase.db")
# curs=conn.cursor()

def str2num(s):
    return {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}[s]


def fn(x, y):
    return x * 10 + y


print reduce(fn, map(str2num, "1398"))


# 输入：['adam', 'LISA', 'barT']，输出：['Adam', 'Lisa', 'Bart']。
def name(n):
    if isinstance(n, str) and len(n) > 0:
        n = n[0].upper() + n[1:].lower()
    elif len(n) == 1:
        n = n.upper()
    else:
        pass
    return n


print map(name, ['adam', 'LISA', 'barT'])


# Python提供的sum()函数可以接受一个list并求和，请编写一个prod()函数，可以接受一个list并利用reduce()求积
def function(x, y):
    return x * y


def prod(l):
    return reduce(function, l)


print prod([1, 2, 3])

import math


def not_prime(x):
    if x == 1:
        return False
    for i in range(2, int(math.sqrt(x)) + 1):
        if x % i == 0:
            return True


def prime(x):
    if not_prime(x):
        return False
    else:
        return True


print filter(prime, range(1, 101))

from multiprocessing import Pool
import os, time, random


# def long_time_task(name):
#     print "Run task %s (%s)" % (name, os.getpid())
#     start = time.time()
#     time.sleep(random.random() * 3)
#     end = time.time()
#     print "Task %s run %0.2f second" % (name, end - start)
#
# if __name__=="__main__":
#     print "Parent process %s" % os.getpid()
#     p=Pool()
#     for i in range(5):
#         p.apply_async(long_time_task,args=(i,))
#     p.close()
#     p.join()#等待子进程结束后再继续往下运行，通常用于进程间的同步。
#     print "all done"

# 一 元组；a = (1,2,3)
#
# 1 有2种方法输出实现下面的结果：
#
# (5,2,3)
#
a = (1, 2, 3)
b = list(a)
b[0] = 5
a = tuple(b)
print a
#
# 2 判断2是否在元组里
#
print 2 in a
# 二 集合a = set(['a','b','c'])做下面的操作：
#
# 1 添加字符串'jay'到集合a里。
a = set(['a', 'b', 'c'])
a.add('jay')
print a
#
# 2 集合b = set(['b','e','f','g']) 用2种方法求集合a 和集合b的并集。
b = set(['b', 'e', 'f', 'g'])
print a | b

# 字典习题:
#
# 已知字典：ainfo = {'ab':'liming','ac':20}
#
# 完成下面的操作
#
# 1 使用2个方法，输出的结果：
#
# ainfo = {'ab':'liming','ac':20,'sex':'man','age':20}
#
ainfo = {'ab': 'liming', 'ac': 20}
ainfo["sex"] = 'man'
ainfo['age'] = 20
print ainfo
# 2 输出结果：['ab','ac']
#
print ainfo.keys()[:2]
# 3 输出结果：['liming',20]
print ainfo.values()[:2]
#
# 4 通过2个方法返回键名ab对应的值。
#
print ainfo["ab"]
print ainfo.get('ab')
# 5 通过2个方法删除键名ac对应的值。
print ainfo.pop('ac')
del ainfo["ab"]
print ainfo

##习题1：


# 列表a = [11,22,24,29,30,32]
#
#
a = [11, 22, 24, 29, 30, 32]
# 1 把28插入到列表的末端
#
a.append(28)
#
# 2 在元素29后面插入元素57
#
a.insert(a.index(29) + 1, 57)
print a
#
# 3 把元素11修改成6
#
a[0] = 26
#
# 3 删除元素32
#
a.remove(32)
#
# 4 对列表从小到大排序
#
a.sort()
print a
#
# ##习题2：
#
#
# 列表b = [1,2,3,4,5]
#
b = [1, 2, 3, 4, 5]
#
# 1 用2种方法输出下面的结果：
#
#
# [1,2,3,4,5,6,7,8]
#
b = b + [6, 7, 8]
print b
#
#
#
# 2 用列表的2种方法返回结果：[5,4]
#
print b[-2:]
#
# 3 判断2是否在列表里
#
#
# ##习题3：
#
#
# b = [23,45,22,44,25,66,78]
#
b = [23, 45, 22, 44, 25, 66, 78]
#
# 用列表解析完成下面习题：
#
#
# 1 生成所有奇数组成的列表
#
print [m for m in b if m % 2 == 0]
#
# 2 输出结果: ['the content 23','the content 45']

print ['the content is %d' % m for m in b[:2]]
#
# 3 输出结果: [25, 47, 24, 46, 27, 68, 80]

print [m + 2 for m in b]

##习题4：


# 用range方法和列表推导的方法生成列表：


# [11,22,33]

print [m * 11 for m in range(1, 4)]


##习题5：


# 已知元组:a = (1,4,5,6,7)
#
a = (1, 4, 5, 6, 7)
# 1 判断元素4是否在元组里
#
print 4 in a
#
# 2 把元素5修改成8
#
#
b = list(a)
b[2] = 8
print tuple(b)
# ##习题6：
#
#
# 已知集合:setinfo = set('acbdfem')和集合finfo = set('sabcdef')完成下面操作：
#
#
setinfo = set('acbdfem')
finfo = set('sabcdef')
# 1 添加字符串对象'abc'到集合setinfo
#
setinfo.add("abc")
#
# 2 删除集合setinfo里面的成员m
#
setinfo.remove("m")
#
# 3 求2个集合的交集和并集
#
print setinfo & finfo
print setinfo | finfo
#
# ##习题7：
#
#
# 用字典的方式完成下面一个小型的学生管理系统。
#
#
# 1 学生有下面几个属性：姓名，年龄，考试分数包括：语文，数学，英语得分。
#
#
# 比如定义2个同学：
#
#
# 姓名：李明，年龄25，考试分数：语文80，数学75，英语85
#
studentinfo = {'李明': {'age': 25, 'fenshu': {'语文': 80, '数学': 75, '英语': 85}}}
studentinfo['张强'] = {'age': 23, 'fenshu': {'语文': 75, '数学': 82, '英语': 78}}
#
# 姓名：张强，年龄23，考试分数：语文75，数学82，英语78
#
#
# 2 给学生添加一门python课程成绩，李明60分，张强：80分
#
studentinfo['李明']['fenshu']['python'] = 60
studentinfo['张强']['fenshu']['python'] = 80
#
# 3 把张强的数学成绩由82分改成89分
#
#
# 4 删除李明的年龄数据
#
#
# 5 对张强同学的课程分数按照从低到高排序输出。
#
binfo=studentinfo['张强']['fenshu']
print sorted(binfo.iteritems(),key=lambda asd:asd[1])
#
# 6 外部删除学生所在的城市属性，不存在返回字符串 beijing
print studentinfo.pop('city','beijing')