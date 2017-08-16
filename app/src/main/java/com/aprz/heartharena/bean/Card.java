package com.aprz.heartharena.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aprz on 17-8-5.
 *
 * bean
 */

public class Card implements Parcelable {

    private int id;

    private String name;

    private String image;

    private String score;

    private String scoreLevel;

    private String rarity;

    private String profession;

    private int newCard;

    private int common;

    private int scoreInt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreLevel() {
        return scoreLevel;
    }

    public void setScoreLevel(String scoreLevel) {
        this.scoreLevel = scoreLevel;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getNewCard() {
        return newCard;
    }

    public void setNewCard(int newCard) {
        this.newCard = newCard;
    }

    public int getCommon() {
        return common;
    }

    public void setCommon(int common) {
        this.common = common;
    }

    public int getScoreInt() {
        return scoreInt;
    }

    public void setScoreInt(int scoreInt) {
        this.scoreInt = scoreInt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.score);
        dest.writeString(this.scoreLevel);
        dest.writeString(this.rarity);
        dest.writeString(this.profession);
        dest.writeInt(this.newCard);
        dest.writeInt(this.common);
        dest.writeInt(this.scoreInt);
    }

    public Card() {
    }

    protected Card(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.score = in.readString();
        this.scoreLevel = in.readString();
        this.rarity = in.readString();
        this.profession = in.readString();
        this.newCard = in.readInt();
        this.common = in.readInt();
        this.scoreInt = in.readInt();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };




    public static Card createWithCursor(Cursor rowData) {
        int idColumnIndex = rowData.getColumnIndex(CardTable.ID);
        int nameColumnIndex = rowData.getColumnIndex(CardTable.NAME);
        int imageColumnIndex = rowData.getColumnIndex(CardTable.IMAGE);
        int scoreColumnIndex = rowData.getColumnIndex(CardTable.SCORE);
        int scoreLevelColumnIndex = rowData.getColumnIndex(CardTable.SCORE_LEVEL);
        int rarityColumnIndex = rowData.getColumnIndex(CardTable.RARITY);
        int professionColumnIndex = rowData.getColumnIndex(CardTable.PROFESSION);
        int newCardColumnIndex = rowData.getColumnIndex(CardTable.NEW_CARD);
        int scoreIntColumnIndex = rowData.getColumnIndex(CardTable.SCORE_INT);
        int commonColumnIndex = rowData.getColumnIndex(CardTable.COMMON);

        int id = rowData.getInt(idColumnIndex);
        String name = rowData.getString(nameColumnIndex);
        String image = rowData.getString(imageColumnIndex);
        String score = rowData.getString(scoreColumnIndex);
        String scoreLevel = rowData.getString(scoreLevelColumnIndex);
        String rarity = rowData.getString(rarityColumnIndex);
        String profession = rowData.getString(professionColumnIndex);
        int newCard = rowData.getInt(newCardColumnIndex);
        int commonCard = rowData.getInt(commonColumnIndex);
        int scoreInt = rowData.getInt(scoreIntColumnIndex);

        Card card = new Card();
        card.setId(id);
        card.setName(name);
        card.setImage(image);
        card.setScore(score);
        card.setScoreLevel(scoreLevel);
        card.setRarity(rarity);
        card.setProfession(profession);
        card.setNewCard(newCard);
        card.setCommon(commonCard);
        card.setScoreInt(scoreInt);

        return card;
    }
}
