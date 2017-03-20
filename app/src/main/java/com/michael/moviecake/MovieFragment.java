package com.michael.moviecake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MovieFragment extends Fragment {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    SharedPreferences mSharedPref;
    MovieAdapter mMovieAdapter;
    List<MovieDb> mMovieDbList;
    TmdbApi mTmdbApi;
    TmdbMovies mTmdbMovies;
    MovieDb mMovieDb;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        new CreateTmbdApiTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new FetchMovieOrderTask().execute();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);


        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies);
        mMovieAdapter = new MovieAdapter(getActivity());
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_SUBJECT, mMovieDbList.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateAdapter() {
        FetchMovieOrderTask movieTask = new FetchMovieOrderTask();
        movieTask.execute();
    }

    public class CreateTmbdApiTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = CreateTmbdApiTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            mTmdbApi = new TmdbApi("e688e37074adf33cd49e7960d8705601");
            mTmdbMovies = mTmdbApi.getMovies();

            return null;
        }
    }


    /******************
    5/11/2016 Michael Busby
     Creates new TmbdApi object
     Retrieves movies based on sort_order preference
     Sets mMovieAdapter with list
    *******************/
    public class FetchMovieOrderTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieOrderTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            String sort_order = mSharedPref.getString(getString(R.string.sort_order),
                    getString(R.string.sort_order_popular));
            MovieResultsPage movieResultsPage;
            if (sort_order.equals(getString(R.string.sort_order_popular))){
                movieResultsPage = mTmdbMovies.getPopularMovies("english", 1);
                Log.v(LOG_TAG, "Popular");
            } else if (sort_order.equals(getString(R.string.sort_order_top_rated))) {
                movieResultsPage = mTmdbMovies.getTopRatedMovies("english", 1);
                Log.v(LOG_TAG, "TopRated");
            } else {
                movieResultsPage = mTmdbMovies.getNowPlayingMovies("english", 1);
                Log.v(LOG_TAG, "InTheaters");
            }

            mMovieDbList = movieResultsPage.getResults();
            String[] posterPathArray = new String[mMovieDbList.size()];
            for (int i = 0; i < mMovieDbList.size(); i++) {
                mMovieDb = mMovieDbList.get(i);
                //Log.v(LOG_TAG, mMovieDb.getImdbID());
                posterPathArray[i] = "http://image.tmdb.org/t/p/w185/" + mMovieDb.getPosterPath();
            }
            //Log.v(LOG_TAG, mMovieDbList.get(1).getImdbID());
            mMovieAdapter.setData(posterPathArray);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mMovieAdapter.notifyDataSetChanged();
        }


    }

}
