# coding=utf-8
import tornado.ioloop
import tornado.web
import os
import MySQLdb,math

sql_create = 'CREATE TABLE IF NOT EXISTS test_server(id SMALLINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,username VARCHAR(20) NOT NULL)'
sql_select = 'SELECT * FROM test_server'


class MainHandler(tornado.web.RequestHandler):
    '''主页'''
    def get(self):
        print self.request.arguments
        print self.get_current_user()
        data = dict()
        try:
            conn = MySQLdb.connect(host='localhost', port=3306, user='root', passwd='13961000804', db='test')
            cur = conn.cursor()
            cur.execute(sql_select)
            total=cur.rowcount
            num=int(math.ceil(total/5.0))
            data = [(dict(img=row[1],name=row[2]))for row in cur.fetchall()]
            print data,type(data)
        except Exception as e:
            print 'error:' + str(e)
        finally:
            conn.close()
            cur.close()
        self.render('templates/index.html',data=data,num=num)

    def post(self, *args, **kwargs):
        self.get_argument('')
        return


class LoginHandler(tornado.web.RequestHandler):
    def get(self):
        print self.request.headers
        print self.request.arguments
        self.render('templates/register.html')

    def post(self):
        username = self.get_argument('form_email')
        password = self.get_argument('form_password')
        print username, password
        if username == 'test' and password == '123':
            self.redirect('/', True)


class ExtendHandler(tornado.web.RequestHandler):
    def get(self):
        self.render('templates/extend.html', header_text='test')


settings = {
    'static_path': os.path.join(os.path.dirname(__file__), 'static'),
}

application = tornado.web.Application([
    (r"/", MainHandler),
    (r"/login", LoginHandler),
    (r"/extend", ExtendHandler),
],
    **settings)

if __name__ == "__main__":
    application.listen(8888)
    tornado.ioloop.IOLoop.current().start()
