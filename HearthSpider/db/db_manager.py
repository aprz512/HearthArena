#!/usr/bin/env python
# -*- coding: utf-8 -*-


from setting.hearth_setting import hearth_setting
import sqlite3


class DBManager:

    def __init__(self):
        self.connection = None
        self.cursor = None

    def open(self):
        self.connection = sqlite3.connect(hearth_setting.db_location)
        self.cursor = self.connection.cursor()

    def close(self):
        self.cursor.close()
        self.connection.close()

    def save(self, item):
        self.cursor.execute('INSERT INTO cards VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)', item.to_tuple())

    def commit(self):
        self.connection.commit()


db_manager = DBManager()


