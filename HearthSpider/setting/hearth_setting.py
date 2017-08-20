#!/usr/bin/env python
# -*- coding: utf-8 -*-


import os


class Setting:

    def __init__(self):
        self.url = 'http://www.heartharena.com/zh-cn/tierlist'
        self.image_url_prefix = 'http://www.heartharena.com'

        cur_dir = os.path.dirname(__file__)
        cwd = os.path.dirname(cur_dir)
        self.db_location = cwd + '/db/arena.db'


hearth_setting = Setting()

professions = [
    'druid', 'hunter', 'mage',
    'paladin', 'priest', 'rogue',
    'shaman', 'warlock', 'warrior',
    'any',
]

# print(hearth_setting.db_location)
