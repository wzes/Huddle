package com.wzes.huddle.activities.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.wzes.huddle.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTeamActivity extends AppCompatActivity {

    @BindView(R.id.team_search_view)
    FloatingSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team);
        ButterKnife.bind(this);
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

            //get suggestions based on newQuery
            //pass them on to the search view

            List<SearchSuggestion> newSuggestions = new ArrayList<>();
            mSearchView.swapSuggestions(newSuggestions);
        });
    }
}
