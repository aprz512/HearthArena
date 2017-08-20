#!/usr/bin/env python
# -*- coding: utf-8 -*-


from setting.hearth_setting import hearth_setting
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from item.card import Card
from setting.hearth_setting import professions
from db.db_manager import db_manager
import re


def parse_section(section):
    """
    一共9个职业，也就是9个section，外加一个中立
    """
    header_node = section.find(class_='tierlist-header')
    rarity_list_node = section.find(class_='rarities tierlist-inner-wrapper')

    profession = parse_header(header=header_node)
    card_list = parse_rarity_list(rarity_list=rarity_list_node)
    for card in card_list:
        card.profession = profession

    return card_list


def parse_header(header):
    """
    获取职业的名称
    """
    return header.find('h2').find('span').get_text()


def parse_card(card):
    """
    获取卡牌的名称 图片 评分
    """
    dl = card.find('dl')
    if not dl:
        print(card)
        print('---------------------------')
        return
    dt = dl.find('dt')
    dd = dl.find('dd')

    if dt.has_attr('data-card-image'):
        card_img = dt['data-card-image']
    else:
        return

    if dt.has_attr('class') and 'any' in dt['class']:
        common = 1
    else:
        common = 0

    card_score = dd.get_text()

    new = 0
    span = dt.find('span')
    if span:
        new = 1
        span.extract()
    card_name = dt.get_text()

    item = Card()
    item.new = new
    item.name = card_name.strip()
    item.image = hearth_setting.image_url_prefix + card_img
    item.score = card_score.strip()
    item.common = common
    item.score_int = re.findall(r'\d+', item.score)[0]

    return item


def parse_card_score_level(card_score_level):
    """
    获取一个评分等级下的所有卡牌
    """
    card_score_level_text = card_score_level.find('header').get_text()
    cards = card_score_level.find('ol').find_all('li', recursive=False)
    item_list = []
    for card in cards:
        item = parse_card(card)
        if not item:
            continue
        item.score_level = card_score_level_text
        item_list.append(item)
    return item_list


def parse_rarity(rarity):
    """
    获取一个稀有度下面的所有卡牌
    """
    card_rarity = rarity.find('header').find('h3').get_text().strip()
    card_score_level_list = rarity.find('ul').find_all('li', recursive=False)
    card_list = []
    for card_score_level in card_score_level_list:
        temp_list = parse_card_score_level(card_score_level)
        for temp in temp_list:
            temp.rarity = card_rarity
        card_list = card_list + temp_list

    return card_list


def parse_rarity_list(rarity_list):
    """
    解析卡牌稀有程度列表
    卡牌品质分为4个等级：普通，稀有，史诗，传说
    """
    rarities = rarity_list.find_all('li', recursive=False)

    card_list = []
    for child in rarities:
        card_list += parse_rarity(child)

    return card_list


def start():
    try:
        driver = webdriver.PhantomJS(
            executable_path='/home/aprz/workspace/phantomjs-2.1.1-linux-x86_64/bin/phantomjs')
        driver.get(url=hearth_setting.url)

        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CLASS_NAME, "navs")))
    except:
        raise Exception('页面数据一直没有加载出来')

    response = driver.page_source
    soup = BeautifulSoup(response, 'html.parser')

    content = soup.body.find(class_='wrapper')
    sections = []
    for profession in professions:
        section = content.find(id=profession)
        sections.append(section)

    try:
        db_manager.open()
        for section in sections:
            card_list = parse_section(section)
            for card in card_list:
                db_manager.save(card)
        db_manager.commit()
    finally:
        db_manager.close()


if __name__ == "__main__":
    start()

