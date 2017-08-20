#!/usr/bin/env python
# -*- coding: utf-8 -*-


class Card:

    def __init__(self):
        self.name = None
        self.image = None
        self.score = None
        self.rarity = None
        self.score_level = None
        self.profession = None
        self.new = 0
        self.common = 0
        self.score_int = 0

    def __str__(self):
        attrs = vars(self)
        return ', '.join("%s: %s" % item for item in attrs.items())

    def to_tuple(self):
        return None, self.name, self.image, self.score, self.score_level,\
               self.rarity, self.profession, self.new, self.common, self.score_int





