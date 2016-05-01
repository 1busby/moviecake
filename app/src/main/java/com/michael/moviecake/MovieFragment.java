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
    SharedPreferences sharedPref;
    MovieAdapter mMovieAdapter;
    List<MovieDb> movieDbList;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new FetchMovieTask().execute();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_movies);
        mMovieAdapter = new MovieAdapter(getActivity());
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Object movie = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, "helloworld lol");
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateAdapter() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {

            TmdbApi tmdbApi = new TmdbApi("e688e37074adf33cd49e7960d8705601");
            TmdbMovies tmdbMovies = tmdbApi.getMovies();
            String sort_order = sharedPref.getString(getString(R.string.sort_order),
                    getString(R.string.sort_order_popular));
            MovieResultsPage movieResultsPage;
            if (sort_order.equals(getString(R.string.sort_order_popular))){
                movieResultsPage = tmdbMovies.getPopularMovies("english", 1);
                Log.v(LOG_TAG, "Popular");
            } else if (sort_order.equals(getString(R.string.sort_order_top_rated))) {
                movieResultsPage = tmdbMovies.getPopularMovies("english", 1);
                Log.v(LOG_TAG, "TopRated");
            } else {
                movieResultsPage = tmdbMovies.getPopularMovies("english", 1);
                Log.v(LOG_TAG, "InTheaters");
            }

            movieDbList = movieResultsPage.getResults();
            String[] posterPathArray = new String[movieDbList.size()];
            MovieDb movieDb;
            for (int i = 0; i < movieDbList.size(); i++) {
                movieDb = movieDbList.get(i);
                posterPathArray[i] = "http://image.tmdb.org/t/p/w185/" + movieDb.getPosterPath();
            }
            mMovieAdapter.setData(posterPathArray);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mMovieAdapter.notifyDataSetChanged();
        }


    }

}
