package com.michael.moviecake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Michael on 4/28/2016.
 */
public class DetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupWindowAnimations();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container2, new DetailFragment())
                    .commit();
        }
    }

    private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setEnterTransition(slide);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private static final String MOVIE_SHARE_HASHTAG = " #CakeMovieApp";
        private MovieDb mMovieDb;
        private ImageView mMovieImage;
        private TextView mMovieTitle;
        private TextView mMovieDescription;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_SUBJECT)) {
                mMovieDb = (MovieDb) intent.getSerializableExtra(Intent.EXTRA_SUBJECT);
                ((TextView) rootView.findViewById(R.id.movie_title_text)).setText(mMovieDb.getTitle());
                Picasso.with(getContext())
                        .load("http://image.tmdb.org/t/p/w185/" + mMovieDb.getPosterPath())
                        .into((ImageView) rootView.findViewById(R.id.movie_poster_image));
                ((TextView) rootView.findViewById(R.id.movie_release_text)).setText(mMovieDb.getReleaseDate());
                ((TextView) rootView.findViewById(R.id.movie_vote_text)).setText(Integer.toString(mMovieDb.getVoteCount()));
                ((TextView) rootView.findViewById(R.id.movie_desc_text)).setText(mMovieDb.getOverview());
            }
            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);
            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);
            // Get the provider and hold onto it to set/change the share intent.
            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }

        }

        private Intent createShareMovieIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mMovieDb + MOVIE_SHARE_HASHTAG);
            return shareIntent;
        }
    }
}