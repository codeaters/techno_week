package com.app.iw.model.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Madeyedexter on 13-03-2017.
 */

@IgnoreExtraProperties
public class LeaderboardItem implements Parcelable {

    public static final Creator<LeaderboardItem> CREATOR = new Creator<LeaderboardItem>() {
        @Override
        public LeaderboardItem createFromParcel(Parcel in) {
            return new LeaderboardItem(in);
        }

        @Override
        public LeaderboardItem[] newArray(int size) {
            return new LeaderboardItem[size];
        }
    };
    private String username;
    private long totalScore = 0;
    private long totalTime = 0;
    private int correct = 0;
    private int incorrect = 0;
    private int rank;
    private String uid;
    private String imgUrl;
    private String displayName;

    public LeaderboardItem() {
    }

    protected LeaderboardItem(Parcel in) {
        username = in.readString();
        totalScore = in.readLong();
        totalTime = in.readLong();
        correct = in.readInt();
        incorrect = in.readInt();
        rank = in.readInt();
        uid = in.readString();
        imgUrl = in.readString();
        displayName = in.readString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeLong(totalScore);
        dest.writeLong(totalTime);
        dest.writeInt(correct);
        dest.writeInt(incorrect);
        dest.writeInt(rank);
        dest.writeString(uid);
        dest.writeString(imgUrl);
        dest.writeString(displayName);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
