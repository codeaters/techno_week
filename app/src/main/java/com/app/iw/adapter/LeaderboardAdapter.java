package com.app.iw.adapter;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.innovationweek.R;
import com.app.iw.model.LeaderboardEntry;
import com.app.iw.model.holder.LeaderboardEntryHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by zeeshan on 3/11/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardEntryHolder> implements
        View.OnClickListener {
    public static final String TAG = LeaderboardAdapter.class.getSimpleName();
    private List<LeaderboardEntry> leaderboardEntryList;
    private String leaderboardType, hightlightUser;
    private Animator.AnimatorListener colorAnimatorListener;

    {
        colorAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                hightlightUser = null;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }

    public LeaderboardAdapter(List<LeaderboardEntry> leaderboardEntries, String leaderboardType) {
        this.leaderboardEntryList = leaderboardEntries;
        this.leaderboardType = leaderboardType;
        sort();
    }

    @Override
    public LeaderboardEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (leaderboardType == null)
            return new LeaderboardEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R
                    .layout.item_leaderboard, parent, false), this, leaderboardType);
        switch (leaderboardType) {
            case LEADERBOARD_TYPE.RANK:
            case LEADERBOARD_TYPE.SCORE:
            case LEADERBOARD_TYPE.SCORE_TIME:
                return new LeaderboardEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.item_leaderboard_static, parent, false), this, leaderboardType);
            default:
                return new LeaderboardEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.item_leaderboard, parent, false), this, leaderboardType);
        }
    }

    @Override
    public void onBindViewHolder(LeaderboardEntryHolder holder, int position) {
        holder.setLeaderboardEntry(position, leaderboardEntryList.get(position), hightlightUser,
                colorAnimatorListener);

    }

    @Override
    public int getItemCount() {
        return leaderboardEntryList == null ? 0 : leaderboardEntryList.size();
    }

    public void removeEntry(@NonNull String userId) {
        int i = 0;
        boolean found = false;
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (le.getUserId().equals(userId)) {
                found = true;
                break;
            }
            i++;
        }
        if (found) {
            leaderboardEntryList.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, leaderboardEntryList.size() - 1);
        }
    }

    public void setLeaderboardEntryList(List<LeaderboardEntry> leaderboardEntryList) {
        this.leaderboardEntryList = leaderboardEntryList;
        if (leaderboardType != null)
            sort();
        notifyDataSetChanged();
    }

    public void updateLeaderboardEntry(@NonNull LeaderboardEntry leaderboardEntry) {
        if (leaderboardType == null)
            updateBySortTime(leaderboardEntry);
        else
            switch (leaderboardType) {
                case LEADERBOARD_TYPE.RANK:
                case LEADERBOARD_TYPE.SCORE:
                    updateAndSort(leaderboardEntry);
                    sort();
                    break;
                case LEADERBOARD_TYPE.SCORE_TIME:
                default:
                    updateBySortTime(leaderboardEntry);
            }
    }

    private void updateAndSort(LeaderboardEntry leaderboardEntry) {
        int index = 0;
        boolean found = false;
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (le.getUserId().equals(leaderboardEntry.getUserId())) {
                found = true;
                break;
            }
            index++;
        }
        if (found) {
            leaderboardEntryList.set(index, leaderboardEntry);
        } else {
            leaderboardEntryList.add(leaderboardEntry);
        }
    }

    private void updateBySortTime(LeaderboardEntry leaderboardEntry) {
        int index = 0;
        int oldIndex = 0;
        boolean found = false;
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (le.getUserId().equals(leaderboardEntry.getUserId())) {
                leaderboardEntryList.remove(oldIndex);
                found = true;
                break;
            }
            oldIndex++;
        }
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (leaderboardEntry.getScore() > le.getScore()) {
                leaderboardEntryList.add(index, leaderboardEntry);
                Log.d(TAG, "added at" + index);
                break;
            } else if (leaderboardEntry.getScore() == le.getScore()) {
                if (leaderboardEntry.getTotalTime() < le.getTotalTime()) {
                    leaderboardEntryList.add(index, leaderboardEntry);
                    Log.d(TAG, "added at" + index);
                    break;
                } else if (leaderboardEntry.getTotalTime() == le.getTotalTime()) {
                    int order = leaderboardEntry.getUser().getUsername().compareTo(le.getUser().getUsername());
                    if (order < 0) {
                        leaderboardEntryList.add(index, leaderboardEntry);
                        Log.d(TAG, "added at" + index);
                        break;
                    }
                }
            }
            index++;
        }
        if (index == leaderboardEntryList.size())
            leaderboardEntryList.add(leaderboardEntry);
        Log.d(TAG, "OldIndex=" + oldIndex + ", index=" + index);
        if (!found) {
            notifyItemInserted(index);
            notifyItemRangeChanged(index, getItemCount() - index);
        } else if (oldIndex == index)
            notifyItemChanged(oldIndex);
        else {
            notifyItemMoved(oldIndex, index);
            notifyItemRangeChanged(index < oldIndex ? index : oldIndex, Math.abs(oldIndex -
                    index) + 1);
        }
        Log.d(TAG, "list:" + leaderboardEntryList.toString());
    }

    @Override
    public void onClick(View view) {
        String msg;
        switch (view.getId()) {
            case R.id.team:
                msg = "This is team name";
                break;
            case R.id.time:
                msg = "This is total time taken to answer the questions";
                break;
            case R.id.score:
                msg = "This is total score received";
                break;
            case R.id.correct_count:
                msg = "Number of question answered correctly";
                break;
            case R.id.incorrect_count:
                msg = "Number of question answered incorrectly";
                break;
            default:
                msg = "Click not implmented";
        }
        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void sort() {
        if (leaderboardType == null) {
            notifyDataSetChanged();
            return;
        }
        switch (leaderboardType) {
            case LEADERBOARD_TYPE.SCORE:
                Collections.sort(leaderboardEntryList, LeaderboardEntry.SCORE_DEC);
                break;
            case LEADERBOARD_TYPE.RANK:
                Collections.sort(leaderboardEntryList, LeaderboardEntry.RANK);
                break;
            case LEADERBOARD_TYPE.SCORE_TIME:
            default:
                Collections.sort(leaderboardEntryList, LeaderboardEntry.SCORE_DEC_TIME_ASC);
        }
        notifyDataSetChanged();
    }

    public int getPosition(@NonNull String userId) {
        int position = 0;
        boolean found = false;
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (le.getUser().getId().equals(userId)) {
                found = true;
                break;
            }
            position++;
        }
        Log.d(TAG, "user found at" + (found ? position : -1));
        return found ? position : -1;
    }

    public void highlight(int currentPosition, String userId) {
        hightlightUser = userId;
        notifyItemChanged(currentPosition);
    }

    public interface LEADERBOARD_TYPE {
        String SCORE = "score";
        String RANK = "rank";
        String SCORE_TIME = "score_time";
    }

}
